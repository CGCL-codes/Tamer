package name.angoca.zemucan.grammarReader.impl.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import name.angoca.zemucan.AbstractZemucanException;
import name.angoca.zemucan.ParameterNullException;
import name.angoca.zemucan.core.graph.model.Graph;
import name.angoca.zemucan.core.graph.model.StartingNode;
import name.angoca.zemucan.grammarReader.api.AbstractGrammarFileReaderException;
import name.angoca.zemucan.grammarReader.api.AbstractGrammarReaderException;
import name.angoca.zemucan.grammarReader.api.AbstractTwoPhaseGrammarReader;
import name.angoca.zemucan.grammarReader.api.DelimitersIncorrectlyDefinedException;
import name.angoca.zemucan.grammarReader.api.GeneralGrammarFileProblemException;
import name.angoca.zemucan.grammarReader.api.GrammarFileNotDefinedException;
import name.angoca.zemucan.grammarReader.api.GrammarReaderController;
import name.angoca.zemucan.tools.Constants;
import name.angoca.zemucan.tools.configurator.Configurator;
import name.angoca.zemucan.tools.file.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This class deals with the XML errors.
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>1.0.0 Class creation</li>
 * <li>1.0.1 Get stream</li>
 * <li>1.0.2 Access to private object</li>
 * <li>1.1.0 Extends two phase grammar reader.</li>
 * </ul>
 *
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m" >(AngocA)</a>
 * @version 1.1.0 2010-10-30
 */
class Handler implements ErrorHandler {

    /**
	 * Logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(Handler.class);

    /**
	 * Process the message to show when an issue is detected.
	 *
	 * @param level
	 *            Message's level.
	 * @param exception
	 *            exception that explain the problem.
	 * @return The descriptive message.
	 */
    private String createMessage(final String level, final SAXParseException exception) {
        String message = "There was " + level + " at the line " + exception.getLineNumber() + " column " + exception.getColumnNumber();
        final String filename = exception.getSystemId();
        if (filename != null) {
            message += " of file " + exception.getSystemId();
        } else {
            message += " from the input";
        }
        return message + ": " + exception.getMessage();
    }

    @Override
    public void error(final SAXParseException exception) throws SAXException {
        final String message = this.createMessage("an error", exception);
        Handler.LOGGER.error(message);
    }

    @Override
    public void fatalError(final SAXParseException exception) throws SAXException {
        final String message = this.createMessage("a problem", exception);
        Handler.LOGGER.error(message);
    }

    @Override
    public void warning(final SAXParseException exception) throws SAXException {
        final String message = this.createMessage("an alert", exception);
        Handler.LOGGER.warn(message);
    }
}

/**
 * This class builds the graph from the content of the files.
 * <p>
 * Based on a tutorial that can be found in
 * http://www.totheriver.com/learn/xml/xmltutorial.html
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>0.0.1 Class creation.</li>
 * <li>0.0.2 Recommendations from PMD.</li>
 * <li>0.0.3 Organized.</li>
 * <li>0.0.4 License token.</li>
 * <li>0.0.5 License token reserved.</li>
 * <li>0.0.6 Ending node after create table.</li>
 * <li>0.1.0 Use of XML to create the graph.</li>
 * <li>0.1.1 Help Token.</li>
 * <li>0.1.2 Messages, exceptions and format.</li>
 * <li>0.1.3 Starting and ending node as constants.</li>
 * <li>0.1.4 Variable names and final.</li>
 * <li>1.0.0 Moved to version 1.</li>
 * <li>1.1.0 Delimiters as part of the grammar.</li>
 * <li>1.2.0 Exception hierarchy changed.</li>
 * <li>1.2.1 Log modified.</li>
 * <li>1.3.0 Extends a class.</li>
 * <li>1.3.1 Strings externalized.</li>
 * <li>1.3.2 XML names as constants.</li>
 * <li>1.4.0 Assert, throws and constants names.</li>
 * <li>1.4.1 Assert, signature and null validation.</li>
 * <li>1.4.2 compareTo -> equals.</li>
 * <li>1.5.0 Check graph and addParent.</li>
 * <li>1.5.1 GraphToken renamed by GraphNode</li>
 * <li>1.6.0 GrammarReader separated from Graph.</li>
 * <li>1.7.0 Reading grammar for different files.</li>
 * <li>1.7.1 Validation with XML Schema. Delimiters is an attribute.</li>
 * <li>1.7.2 Validation in an independent stream and method.</li>
 * <li>1.8.0 New structure.</li>
 * <li>1.9.0 Return graph.</li>
 * <li>1.10.0 Protected attributes -> private.</li>
 * <li>1.10.1 Extra nodes from configuration.</li>
 * </ul>
 *
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m" >(AngocA)</a>
 * @version 1.10.1 2010-08-08
 */
public final class ImplementationXMLGrammarReader extends AbstractTwoPhaseGrammarReader {

    /**
	 * Grammar delimiters.
	 */
    private static final String DELIMITERS = "delimiters";

    /**
	 * Logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ImplementationXMLGrammarReader.class);

    /**
	 * Token's id.
	 */
    private static final String NODE_ID = "id";

    /**
	 * Token's name.
	 */
    private static final String NODE_NAME = "name";

    /**
	 * Token.
	 */
    private static final String TOKEN = "token";

    /**
	 * Token's children.
	 */
    private static final String TOKEN_ID_TOKEN = "idToken";

    /**
	 * Token represents a reserved word.
	 */
    private static final String TOKEN_RESERVED = "reserved";

    /**
	 * Value of the property to validate the grammar file.
	 */
    private static final String TRUE = "true";

    /**
	 * Property to validate the grammar file.
	 */
    private static final String VALIDATE_GRAMMAR_PROPERTY = "ValidateGrammar";

    /**
	 * Read the grammar file.
	 *
	 * @param fileName
	 *            Name of the xml file.
	 * @return The xml file ready to be processed.
	 * @throws AbstractGrammarFileReaderException
	 *             When there is a problem opening the file.
	 */
    private static InputSource readSource(final String fileName) throws AbstractGrammarReaderException, AbstractGrammarFileReaderException {
        if (fileName == null) {
            throw new GrammarFileNotDefinedException();
        }
        InputStream inputStream = null;
        try {
            inputStream = FileReader.getStream(fileName);
        } catch (final AbstractZemucanException e) {
            throw new GeneralGrammarFileProblemException(e);
        }
        final InputSource content = new InputSource(inputStream);
        assert content != null;
        return content;
    }

    /**
	 * Content to parse.
	 */
    private InputSource contentToParse;

    /**
	 * Content to validate.
	 */
    private InputSource contentToValidate;

    /**
	 * Document structure.
	 */
    private Document dom;

    /**
	 * Graph that represents the grammar.
	 */
    private Graph graph;

    /**
	 * Default constructor that fill the given graph with the grammar content.
	 *
	 * @param graphToFill
	 *            Graph to fill with the grammar content.
	 * @param contentToBeParsed
	 *            Data from the grammar file to parse. This stream is open.
	 * @param contentToBeValidated
	 *            Data from the grammar file to validate. This stream is open.
	 * @throws AbstractZemucanException
	 *             When there is a problem reading the grammar file or building
	 *             the graph. If there is a null parameter. When there is a
	 *             problem with the delimiters.
	 */
    ImplementationXMLGrammarReader(final Graph graphToFill, final InputSource contentToBeParsed, final InputSource contentToBeValidated) throws AbstractZemucanException {
        super();
        if (contentToBeParsed == null) {
            throw new ParameterNullException("contentToParse");
        }
        if (contentToBeValidated == null) {
            throw new ParameterNullException("contentToValidate");
        }
        if (graphToFill == null) {
            throw new ParameterNullException("graph");
        }
        ImplementationXMLGrammarReader.LOGGER.debug("Creating the grammar reader");
        this.contentToParse = contentToBeParsed;
        this.contentToValidate = contentToBeValidated;
        this.graph = graphToFill;
        this.dom = null;
    }

    /**
	 * Public constructor that reads the grammar from a file.
	 *
	 * @param graphToFill
	 *            Graph where the read nodes are placed.
	 * @param fileName
	 *            Name of the file that contains the grammar.
	 * @throws AbstractZemucanException
	 *             If the grammar file defined in the configuration file is not
	 *             found. If the graph that represents the grammar is invalid.
	 *             If a parameter is null. If there is a problem reading the
	 *             grammar.
	 */
    public ImplementationXMLGrammarReader(final Graph graphToFill, final String fileName) throws AbstractZemucanException {
        super();
        if (graphToFill == null) {
            throw new ParameterNullException("graphToFill");
        }
        if (fileName == null) {
            throw new ParameterNullException("fileDescriptor");
        }
        this.graph = graphToFill;
        this.fileDescriptor = fileName;
        this.contentToParse = null;
        this.contentToValidate = null;
        this.dom = null;
    }

    /**
	 * Reads the document and creates all the nodes.
	 * <p>
	 * NOTE: This phase does not create the relations between nodes.
	 *
	 * @throws AbstractZemucanException
	 *             When there are several nodes in the grammar file with the
	 *             same id, or when the name or the id of a node is invalid.
	 *             When the graph is inconsistent.
	 */
    protected final void firstPhase() throws AbstractZemucanException {
        final Element docEle = this.dom.getDocumentElement();
        final NodeList nodelist = docEle.getElementsByTagName(ImplementationXMLGrammarReader.TOKEN);
        final int size = nodelist.getLength();
        ImplementationXMLGrammarReader.LOGGER.debug("Processing {} nodes", Integer.toString(size));
        if (size > 0) {
            for (int i = 0; i < size; i += 1) {
                final Element eleNode = (Element) nodelist.item(i);
                this.readAndAddNode(eleNode);
            }
        }
    }

    @Override
    public Graph generateGraph() throws AbstractZemucanException {
        if ((this.contentToParse == null) && (this.contentToValidate == null)) {
            final String[] files = this.getFileNamesFromDescriptor();
            if (files.length <= 0) {
                this.contentToParse = ImplementationXMLGrammarReader.readSource(this.fileDescriptor);
                this.contentToValidate = ImplementationXMLGrammarReader.readSource(this.fileDescriptor);
            } else {
                final List<Graph> graphs = new ArrayList<Graph>(files.length);
                final List<String> delimiters = new ArrayList<String>(files.length);
                for (int i = 0; i < files.length; i++) {
                    final String filename = files[i];
                    final boolean extraNodes = !Boolean.parseBoolean(System.getProperty(Constants.WITHOUT_EXTRA_NODES));
                    final Graph newGraph = new Graph(this.fileDescriptor + "/" + filename, extraNodes);
                    graphs.add(newGraph);
                    final ImplementationXMLGrammarReader grammar = new ImplementationXMLGrammarReader(newGraph, this.fileDescriptor + File.separatorChar + filename);
                    grammar.generateGraph();
                    grammar.getStartingNode();
                    delimiters.add(grammar.getDelimiters());
                }
                this.graph = GrammarReaderController.mergeGraphs(graphs.toArray(new Graph[0]));
                super.setDelimiters(GrammarReaderController.processDelimiters(delimiters));
            }
        }
        if ((this.contentToParse != null) && (this.contentToValidate != null)) {
            this.validation(this.contentToValidate);
            this.dom = this.parseXmlFile();
            super.setStartingNode(this.readGraph());
            super.setDelimiters(this.readDelimiters());
        }
        super.setGrammarProcessed(true);
        return this.graph;
    }

    /**
	 * Retrieves the children of a node and put them in it. Takes the nodes read
	 * from the first phase and stored in the node class attribute.
	 *
	 * @param eleNode
	 *            Description of a node.
	 * @throws AbstractZemucanException
	 *             if there is a not existing node. Or if the child graph node
	 *             is null. Or if endingNode has children. If a parameter is
	 *             null.
	 */
    private void getChildren(final Element eleNode) throws AbstractZemucanException {
        assert eleNode != null;
        final String nodeId = this.getTextValue(eleNode, ImplementationXMLGrammarReader.NODE_ID);
        if (nodeId != null) {
            final NodeList childList = eleNode.getElementsByTagName(ImplementationXMLGrammarReader.TOKEN_ID_TOKEN);
            final int size = childList.getLength();
            if (size > 0) {
                for (int i = 0; i < size; i += 1) {
                    final Element childElement = (Element) childList.item(i);
                    final String childId = childElement.getFirstChild().getNodeValue();
                    this.graph.addRelation(nodeId, childId);
                }
            }
        }
    }

    /**
	 * It takes an XML element and the tag name, then it looks for the tag and
	 * gets the text content.
	 *
	 * @param element
	 *            XML element to read.
	 * @param tagName
	 *            Name of the element.
	 * @return Content of the element with the given name.
	 */
    private String getTextValue(final Element element, final String tagName) {
        assert element != null;
        assert tagName != null;
        String textValue = null;
        final NodeList nodelist = element.getElementsByTagName(tagName);
        if (nodelist.getLength() > 0) {
            final Element elem = (Element) nodelist.item(0);
            textValue = elem.getFirstChild().getNodeValue();
        }
        return textValue;
    }

    /**
	 * Loads the document builder.
	 *
	 * @return The DOM read from the file.
	 * @throws AbstractGrammarFileReaderException
	 *             If there is a problem reading the XML file that contains the
	 *             grammar.
	 */
    private Document parseXmlFile() throws AbstractGrammarFileReaderException {
        assert this.contentToParse != null;
        ImplementationXMLGrammarReader.LOGGER.debug("Parsing the XML file");
        DocumentBuilder parser = null;
        Document document = null;
        try {
            parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            parser.setErrorHandler(new Handler());
            document = parser.parse(this.contentToParse);
        } catch (final ParserConfigurationException e) {
            throw new XMLProblemException(e);
        } catch (final SAXException exception) {
            throw new XMLProblemException(exception);
        } catch (final IOException exception) {
            throw new GeneralGrammarFileProblemException(exception);
        }
        assert document != null;
        return document;
    }

    /**
	 * It takes a node element and reads the values in it, then creates a graph
	 * node object and returns it.
	 *
	 * @param eleNode
	 *            element where the data of the node can be found.
	 * @throws AbstractZemucanException
	 *             The name is invalid, or the id. When the graph is
	 *             inconsistent.
	 */
    private void readAndAddNode(final Element eleNode) throws AbstractZemucanException {
        assert eleNode != null;
        final String nodeId = this.getTextValue(eleNode, ImplementationXMLGrammarReader.NODE_ID);
        final String name = this.getTextValue(eleNode, ImplementationXMLGrammarReader.NODE_NAME);
        final NodeList nodelist = eleNode.getElementsByTagName(ImplementationXMLGrammarReader.TOKEN_RESERVED);
        boolean reserved = false;
        if (nodelist.getLength() > 0) {
            reserved = true;
        }
        if (nodeId == null) {
            if (name == null) {
                throw new NoIdNoNameDefinedInNodeException();
            }
            throw new NoIdDefinedInNodeException(name);
        } else if (name == null) {
            throw new NoNameDefinedInNodeException(nodeId);
        }
        this.graph.addNode(nodeId, name, reserved);
    }

    /**
	 * Reads the delimiters of the grammar.
	 *
	 * @param dom
	 *            XML file parsed.
	 * @return The delimiters that separates the tokens.
	 * @throws DelimitersIncorrectlyDefinedException
	 *             When the grammar has not defined the delimiters.
	 */
    private String readDelimiters() throws DelimitersIncorrectlyDefinedException {
        ImplementationXMLGrammarReader.LOGGER.debug("Reading the delimiters");
        String delimitersRead = null;
        final Element docEle = this.dom.getDocumentElement();
        final Attr attributes = docEle.getAttributeNode(ImplementationXMLGrammarReader.DELIMITERS);
        try {
            delimitersRead = attributes.getValue();
        } catch (final NullPointerException exception) {
            throw new DelimitersIncorrectlyDefinedException();
        }
        if ((delimitersRead == null) || delimitersRead.equals("")) {
            throw new DelimitersIncorrectlyDefinedException();
        }
        assert delimitersRead != null;
        return delimitersRead;
    }

    /**
	 * Reads the graph and return the entry point that is the starting node.
	 *
	 * @return The startingNode that is the entry point of the graph.
	 * @throws AbstractZemucanException
	 *             If the grammar is invalid. If the graph becomes invalid. If a
	 *             parameter is null.
	 */
    private StartingNode readGraph() throws AbstractZemucanException {
        this.firstPhase();
        this.graph.firstPhaseFinished();
        this.secondPhase();
        final StartingNode node = this.graph.secondPhaseFinished();
        assert node != null;
        return node;
    }

    /**
	 * Creates the relations between nodes.
	 *
	 * @throws AbstractZemucanException
	 *             when there is a not existing node. Or if the child graph node
	 *             is null. Or if endingNode has children. If a parameter is
	 *             null.
	 */
    protected final void secondPhase() throws AbstractZemucanException {
        final Element docEle = this.dom.getDocumentElement();
        final NodeList nodeList = docEle.getElementsByTagName(ImplementationXMLGrammarReader.TOKEN);
        final int size = nodeList.getLength();
        if (size > 0) {
            for (int i = 0; i < size; i += 1) {
                final Element eleNode = (Element) nodeList.item(i);
                this.getChildren(eleNode);
            }
        }
    }

    /**
	 * Validates the xml file.
	 *
	 * @param input
	 *            Content of the XML file.
	 * @throws GeneralGrammarFileProblemException
	 *             IO Exception.
	 * @throws GrammarFileNotFoundException
	 *             If the file is not found.
	 * @throws XMLProblemException
	 *             If there is a problem parsing the file.
	 * @throws GrammarFileNotDefinedException
	 *             If the file is not defined.
	 */
    private void validation(final InputSource input) throws GeneralGrammarFileProblemException, XMLProblemException, GrammarFileNotDefinedException {
        final String validate = Configurator.getInstance().getProperty(ImplementationXMLGrammarReader.VALIDATE_GRAMMAR_PROPERTY);
        if ((validate != null) && validate.equals(ImplementationXMLGrammarReader.TRUE)) {
            Schema schema = null;
            final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource schemaSource;
            try {
                schemaSource = new StreamSource(FileReader.getStream("grammar.xsd"));
            } catch (final AbstractZemucanException e) {
                throw new GeneralGrammarFileProblemException(e);
            }
            try {
                schema = schemaFactory.newSchema(schemaSource);
            } catch (final SAXException exception) {
                throw new XMLProblemException(exception);
            }
            final Validator validator = schema.newValidator();
            validator.setErrorHandler(new Handler());
            final Source source = new StreamSource(input.getByteStream());
            try {
                validator.validate(source);
            } catch (final SAXException exception) {
                throw new XMLProblemException(exception);
            } catch (final IOException exception) {
                throw new GeneralGrammarFileProblemException(exception);
            }
        }
    }
}
