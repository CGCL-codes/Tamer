package org.infoset.xml.xerces;

import java.net.*;
import java.util.*;
import java.util.logging.*;
import org.infoset.xml.*;
import org.apache.xerces.xni.*;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.SymbolTable;

/**
 *
 * @author R. Alexander Milowski
 */
public class Item2DocumentHandler implements ItemDestination {

    static final int levelIncrement = 20;

    XMLDocumentHandler documentHandler;

    Element[] context;

    List<String>[] prefixes;

    int level;

    boolean[] startSent;

    SymbolTable symbolTable;

    protected Logger log;

    protected boolean fineLog;

    protected boolean finerLog;

    class LocalNamespaceContext implements NamespaceContext {

        public boolean declarePrefix(String str, String str1) {
            throw new RuntimeException("declarePrefix() not allowed.");
        }

        public java.util.Enumeration getAllPrefixes() {
            if (level < 0) {
                return null;
            }
            Vector v = new Vector();
            for (Iterator prefixes = context[level].getNamespaceScope().getPrefixes(); prefixes.hasNext(); ) {
                v.add(prefixes.next());
            }
            return v.elements();
        }

        public String getDeclaredPrefixAt(int param) {
            if (level >= 0) {
                return prefixes[level].get(param);
            } else {
                return null;
            }
        }

        public int getDeclaredPrefixCount() {
            if (level >= 0) {
                if (context[level].hasNamespaceDeclarations()) {
                    return prefixes[level].size();
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }

        public String getPrefix(String str) {
            if (level >= 0) {
                return context[level].getNamespaceScope().getNearestPrefix(URI.create(str));
            } else {
                return null;
            }
        }

        public String getURI(String str) {
            if (level >= 0) {
                URI ns = context[level].getNamespaceScope().getNamespace(str);
                return ns == Name.NO_NAMESPACE ? "" : ns.toString();
            } else {
                return null;
            }
        }

        public void popContext() {
            throw new RuntimeException("popContext() not allowed.");
        }

        public void pushContext() {
            throw new RuntimeException("pushContext() not allowed.");
        }

        public void reset() {
            throw new RuntimeException("reset() not allowed.");
        }
    }

    /** Creates a new instance of Item2DocumentHandler */
    public Item2DocumentHandler() {
        documentHandler = null;
        context = new Element[levelIncrement];
        prefixes = new List[levelIncrement];
        level = -1;
        startSent = new boolean[levelIncrement];
        symbolTable = new SymbolTable();
        log = Logger.getLogger(Item2DocumentHandler.class.getName());
        fineLog = log.isLoggable(Level.FINE);
        finerLog = log.isLoggable(Level.FINER);
    }

    public void setDocumentHandler(XMLDocumentHandler handler) {
        documentHandler = handler;
    }

    public void setSymbolTable(SymbolTable table) {
        symbolTable = table;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public void send(Item item) throws XMLException {
        try {
            switch(item.getType()) {
                case DocumentItem:
                    {
                        fineLog = log.isLoggable(Level.FINE);
                        finerLog = log.isLoggable(Level.FINER);
                        Document doc = (Document) item;
                        if (fineLog) {
                            log.fine("Base URI: " + doc.getBaseURI());
                        }
                        documentHandler.startDocument(new ItemLocator(doc.getBaseURI(), doc), null, new LocalNamespaceContext(), null);
                    }
                    break;
                case DocumentEndItem:
                    documentHandler.endDocument(null);
                    level = -1;
                    break;
                case ElementItem:
                    {
                        if (level >= 0 && !startSent[level]) {
                            handleStartElement(context[level], false);
                            startSent[level] = true;
                        }
                        level++;
                        if (level == context.length) {
                            Element[] newcontext = new Element[context.length + levelIncrement];
                            System.arraycopy(context, 0, newcontext, 0, context.length);
                            context = newcontext;
                            List<String>[] newprefixes = (List<String>[]) new List[prefixes.length + levelIncrement];
                            System.arraycopy(prefixes, 0, newprefixes, 0, prefixes.length);
                            prefixes = newprefixes;
                            boolean[] newstartSent = new boolean[startSent.length + levelIncrement];
                            System.arraycopy(startSent, 0, newstartSent, 0, startSent.length);
                            startSent = newstartSent;
                        }
                        Element e = (Element) item;
                        if (fineLog) {
                            if (level == 0) {
                                log.fine(this + "Document element=" + e.getName());
                            } else {
                                log.fine(this + "element=" + e.getName());
                            }
                        }
                        context[level] = e;
                        prefixes[level] = new ArrayList<String>();
                        prefixes[level].addAll(e.getNamespaceScope().getDataProxy().keySet());
                        startSent[level] = false;
                    }
                    break;
                case ElementEndItem:
                    if (!startSent[level]) {
                        handleStartElement(context[level], true);
                        startSent[level] = true;
                    } else {
                        ElementEnd end = (ElementEnd) item;
                        documentHandler.endElement(toQName(end.getName(), context[level].getPrefix()), null);
                        context[level] = null;
                        prefixes[level] = null;
                        level--;
                    }
                    break;
                case CharactersItem:
                    {
                        if (level >= 0 && !startSent[level]) {
                            handleStartElement(context[level], false);
                            startSent[level] = true;
                        }
                        Characters c = (Characters) item;
                        char[] ch = c.getText().toCharArray();
                        documentHandler.characters(new XMLString(ch, 0, ch.length), null);
                    }
                    break;
                case CommentItem:
                    {
                        if (level >= 0 && !startSent[level]) {
                            handleStartElement(context[level], false);
                            startSent[level] = true;
                        }
                        Comment c = (Comment) item;
                        char[] ch = c.getText().toCharArray();
                        documentHandler.comment(new XMLString(ch, 0, ch.length), null);
                    }
                    break;
                case ProcessingInstructionItem:
                    {
                        if (level >= 0 && !startSent[level]) {
                            handleStartElement(context[level], false);
                            startSent[level] = true;
                        }
                        ProcessingInstruction p = (ProcessingInstruction) item;
                        char[] ch = p.getText().toCharArray();
                        documentHandler.processingInstruction(p.getName().getLocalName(), new XMLString(ch, 0, ch.length), null);
                    }
                    break;
            }
        } catch (XNIException ex) {
            throw new XMLException(ex.getMessage(), ex.getCause());
        }
    }

    public void handleStartElement(Element start, boolean empty) {
        QName qname = toQName(start.getName(), start.getPrefix());
        XMLAttributes atts = new XMLAttributesImpl();
        for (Attribute att : start.getAttributes().values()) {
            atts.addAttribute(toQName(att.getName(), att.getPrefix()), null, att.getText());
        }
        if (empty) {
            documentHandler.emptyElement(qname, atts, null);
        } else {
            documentHandler.startElement(qname, atts, null);
        }
    }

    public QName toQName(Name name, String prefix) {
        URI ns = name.getNamespaceName();
        String uri = ns == Name.NO_NAMESPACE ? null : ns.toString();
        if (uri != null) {
            uri = symbolTable.addSymbol(uri);
        }
        String lname = symbolTable.addSymbol(name.getLocalName());
        return new QName(prefix, lname, prefix.length() == 0 ? lname : prefix + ':' + lname, uri);
    }
}
