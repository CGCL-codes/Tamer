package net.sf.japi.progs.jeduca.jtest.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import net.sf.japi.progs.jeduca.jtest.MCAnswerText;
import net.sf.japi.progs.jeduca.jtest.MCQuestionText;
import net.sf.japi.progs.jeduca.jtest.QuestionCollection;
import net.sf.japi.progs.jeduca.jtest.QuestionText;
import net.sf.japi.progs.jeduca.swing.io.Exporter;
import net.sf.japi.xml.FilteredNodeList;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import static org.w3c.dom.Node.ELEMENT_NODE;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.xml.sax.SAXException;

/** Interface for reading and writing KEduca files.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @since 0.1
 */
public class KEduca extends AbstractJTestImport<QuestionCollection> implements Exporter<QuestionCollection> {

    /** The DocumentBuilderFactory. */
    private final DocumentBuilderFactory dbf;

    /** The DocumentBuilder. */
    private final DocumentBuilder db;

    /** The DOMImplementation in use. */
    private final DOMImplementation domImpl;

    /** The XPathFactory. */
    private final XPathFactory xpf;

    /** The XPath. */
    private final XPath xp;

    /** Create a new KEduca interface.
     * @throws Error in case of ParserConfigurationExceptions
     */
    public KEduca() {
        try {
            dbf = DocumentBuilderFactory.newInstance();
            try {
                dbf.setCoalescing(true);
                dbf.setExpandEntityReferences(true);
                dbf.setIgnoringComments(true);
                dbf.setIgnoringElementContentWhitespace(true);
                dbf.setNamespaceAware(false);
                dbf.setValidating(false);
                dbf.setXIncludeAware(false);
            } catch (final NoSuchMethodError ignore) {
            } catch (final UnsupportedOperationException ignore) {
            }
            db = dbf.newDocumentBuilder();
            domImpl = db.getDOMImplementation();
            xpf = XPathFactory.newInstance();
            xp = xpf.newXPath();
        } catch (final ParserConfigurationException e) {
            throw new Error(e);
        }
    }

    /** Get all questions from a .edu URI.
     * @param url .edu-URI
     * @return all questions from uri
     * @throws IOException in case of I/O-problems
     * @throws SAXException in case of XML parsing errors
     */
    public QuestionCollection load(final String url) throws IOException, SAXException {
        return load(db.parse(url));
    }

    /** Get all questions from a .edu document.
     * @param doc .edu-Document
     * @return all questions from doc
     */
    private QuestionCollection load(final Document doc) {
        final List<QuestionText> questions = new ArrayList<QuestionText>();
        final NodeList questionEls = doc.getElementsByTagName("question");
        final int questionElsSize = questionEls.getLength();
        for (int i = 0; i < questionElsSize; i++) {
            final Element questionEl = (Element) questionEls.item(i);
            final List<MCAnswerText> answers = new ArrayList<MCAnswerText>();
            String questionText = null;
            for (final Element n : new FilteredNodeList<Element>(questionEl, ELEMENT_NODE)) {
                final String tagName = n.getNodeName();
                if ("text".equals(tagName)) {
                    questionText = n.getFirstChild().getNodeValue();
                } else if ("true".equals(tagName)) {
                    answers.add(new MCAnswerText(n.getFirstChild().getNodeValue(), true, true));
                } else if ("false".equals(tagName)) {
                    answers.add(new MCAnswerText(n.getFirstChild().getNodeValue(), false, true));
                }
            }
            final QuestionText question = new MCQuestionText(questionText, answers);
            question.setType(questionEl.getAttribute("type"));
            question.setHTML(true);
            questions.add(question);
        }
        final QuestionCollection col = new QuestionCollection(questions);
        setInfo(col, doc);
        return col;
    }

    /** Read the information from a KEduca document and write it to a QuestionCollection.
     * @param col QuestionCollection to write information to
     * @param doc KEduca document to read from
     */
    private void setInfo(final QuestionCollection col, final Document doc) {
        try {
            col.setTitle(xp.evaluate("Document/Info/title", doc));
            col.setCategory(xp.evaluate("Document/Info/category", doc));
            col.setType(xp.evaluate("Document/Info/type", doc));
            col.setLevel(xp.evaluate("Document/Info/level", doc));
            col.setLanguage(xp.evaluate("Document/Info/language", doc));
            col.setAuthorName(xp.evaluate("Document/Info/author/name", doc));
            col.setAuthorEMail(xp.evaluate("Document/Info/author/email", doc));
            col.setAuthorWWW(xp.evaluate("Document/Info/author/www", doc));
        } catch (final XPathExpressionException e) {
            throw new Error(e);
        }
    }

    /** Export a QuestionCollection as KEduca file.
     * @param doc QuestionCollection to create document from
     * @param file File to save
     * @throws IOException in case of I/O-problems
     */
    public void save(final QuestionCollection doc, final File file) throws IOException {
        if (!(domImpl instanceof DOMImplementationLS)) {
            throw new IOException("DOM Implementation with LS-Feature required, upgrade your Java XML Library");
        }
        final DOMImplementationLS ls = (DOMImplementationLS) domImpl;
        ls.createLSSerializer().writeToURI(export(doc), file.toURI().toString());
    }

    /** Create a KEduca XML Document from a QuestionCollection.
     * @param col QuestionCollection to create document from
     * @return XML Document (KEduca) for <var>col</var>
     */
    private Document export(final QuestionCollection col) {
        final DocumentType keduca = domImpl.createDocumentType("educa", null, null);
        final Document doc = domImpl.createDocument(null, "Document", keduca);
        final Element docEl = doc.getDocumentElement();
        final Element infoEl = doc.createElement("Info");
        docEl.appendChild(infoEl);
        infoEl.appendChild(create(doc, "title", col.getTitle()));
        infoEl.appendChild(create(doc, "category", col.getCategory()));
        infoEl.appendChild(create(doc, "type", col.getType()));
        infoEl.appendChild(create(doc, "level", col.getLevel()));
        infoEl.appendChild(create(doc, "language", col.getLanguage()));
        final Element authorEl = doc.createElement("author");
        infoEl.appendChild(authorEl);
        infoEl.appendChild(create(doc, "name", col.getAuthorName()));
        infoEl.appendChild(create(doc, "email", col.getAuthorEMail()));
        infoEl.appendChild(create(doc, "www", col.getAuthorWWW()));
        final Element dataEl = doc.createElement("Data");
        docEl.appendChild(dataEl);
        for (int i = 0; i < col.getSize(); i++) {
            final QuestionText qt = col.getQuestion(i);
            if (qt instanceof MCQuestionText) {
                dataEl.appendChild(createQuestionEl(doc, (MCQuestionText) qt));
            }
        }
        return doc;
    }

    /** Create a Question element for a given MCQuestionText.
     * @param doc document to create element for
     * @param question MCQuestionText to create Question for
     * @return element for question
     */
    private static Element createQuestionEl(final Document doc, final MCQuestionText question) {
        final Element questionEl = doc.createElement("question");
        if (question.getType() != null) {
            questionEl.setAttribute("type", question.getType());
        }
        questionEl.appendChild(create(doc, "text", question.getText()));
        for (int i = 0; i < question.getSize(); i++) {
            final MCAnswerText answer = question.getAnswer(i);
            questionEl.appendChild(create(doc, answer.isCorrect() ? "true" : "false", answer.getText()));
        }
        return questionEl;
    }

    /** Create a simple element with a given qname and a given text as child.
     * @param doc document to create element for
     * @param name element name
     * @param text element text content
     * @return element with name <var>name</var> containing <var>text</var>
     */
    private static Element create(final Document doc, final String name, final String text) {
        final Element el = doc.createElement(name);
        el.appendChild(doc.createTextNode(text));
        return el;
    }

    /** {@inheritDoc} */
    public String getName() {
        return "KEduca";
    }

    /** {@inheritDoc} */
    public Class<QuestionCollection> getType() {
        return QuestionCollection.class;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean canLoadImpl(final String url) {
        try {
            db.setErrorHandler(XMLUtils.getInstance().getQuietHandler());
            final Document doc = db.parse(url);
            final DocumentType dt = doc.getDoctype();
            if (!"educa".equals(dt.getName())) {
                return false;
            }
            final Element docEl = doc.getDocumentElement();
            return "Document".equals(docEl.getNodeName());
        } catch (final Exception ignore) {
        }
        return false;
    }
}
