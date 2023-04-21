package javaframework.datalayer.xml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javaframework.base.exceptions.FrameworkException;
import javaframework.datalayer.filesystem.operations.FileOperations;
import javaframework.datalayer.filesystem.paths.FileSystemPath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Reresents a XML document and methods to parse it. These methods allow to perform search/insertion/deletion of tags, atributes and values using sequential seeking or by means of XPath.
 *
 * <br/><br/>
 *
 * <b><u>Dependencies</u></b><br/> Base<br/> FileSystem<br/> <br/><br/>
 *
 * <b><u>Design notes</u></b><br/>
 *
 * This implementation follows the conventios established for the XML
 * <code>DOM</code> parser. Under this conventions, the XML document is completely load into memory. This implies an improvement in the read/write operations because the are made against the memory
 * instead using the filesystem. On the other hand this can lead a leak of memory when working with very large XML documents. In such scenarios, the
 * <code>SAX</code> parser may be more convenient. <br/><br/>
 *
 * When instantiating a new object, this is created in memory and only contains the prologue tag and a root tag. To backup this structure in disk it is needed to invoke the
 * <code>saveToDisk</code> method.<br/><br/>
 *
 * According to the DOM model, these naming conventions are used in this implementation: <br/><br/>
 *
 * <code><b>NODE</b></code>:	Any component of the XML document. This includes tags, attributes and values<br/>
 * <code><b>ELEMENT</b></code>:	It's a tag.<br/>
 * <code><b>ATTRIBUTE</b></code>:	One or more fields included in a tag,<br/>
 *
 * Each element contains a value field. When an element has descendants there is a value field between the parent and the descendat tag. The same way, there is a value field between the first and the
 * second descendant tag and so on unti reach the value field between the last descendant tag and the closing parent tag.
 *
 * Under this implementation closing tags are ignored while performing seeking operations. <br/><br/>
 *
 * <b><u>About XPATH:</u></b><br/><br/>
 *
 * Lets' suppose a XML fragment like this: <br/><br/>
 *
 * &lt;root&gt; 
 *		&lt;element id="tag1"&gt; 
 *			&lt;subelement_1&gt;hello world&lt;/subelement_1&gt; 
 *			&lt;subelement_2&gt;100&lt;/subelement_2&gt; 
 *		&lt;/element&gt; 
 * &lt;/root&gt;
 *
 * <br/><br/>
 *
 * ... it would be possible to define XPaths like this:<br/><br/>
 *
 *	//element/[@id=tag1]/subelement_1	--> Returns "hello world"<br/> //element/[@id=tag1]/subelement_2"	--> Returns "100"<br/><br/>
 *
 * The double slash means "at any depth level in the document". The filter expressions ([@id=tag1]) are not quoted.
 *
 * <br/><br/>
 *
 * <b>· Creation time:</b> 01/01/2007<br/> <b>· Revisions:</b> 02/05/2010, 03/04/2011<br/><br/> <b><u>State</u></b><br/> <b>· Debugged:</b> Yes<br/> <b>· Structural tests:</b> -<br/> <b>· Functional
 * tests:</b> -<br/>
 *
 * @author Francisco Pérez R. de V. (franjfw{@ literal @}yahoo.es)
 * @version JavaFramework.0.1.0.en
 * @version XMLDocument.0.0.2
 * @since JavaFramework.0.0.0.en
 * @see <a href=””></a>
 *
 */
public class XMLDocument implements InterfaceXMLDocument
	{

	private Document XMLDoc;

	private void setXMLDoc(final Document XMLDoc)
		{
		this.XMLDoc = XMLDoc;
		}

	private Document getXMLDoc()
		{
		return this.XMLDoc;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Element createNewDocumentInMemory(final boolean standAlone, final String XMLVersion, final String rootTagName) throws ParserConfigurationException
		{
		final DocumentBuilderFactory DOCUMENT_BUILDER_GENERATOR = DocumentBuilderFactory.newInstance();
		final DocumentBuilder DOCUMENT_BUILDER = DOCUMENT_BUILDER_GENERATOR.newDocumentBuilder();
		final Document XML_DOCUMENT = DOCUMENT_BUILDER.newDocument();
		XML_DOCUMENT.setXmlStandalone(standAlone);
		XML_DOCUMENT.setXmlVersion(XMLVersion);
		this.setXMLDoc(XML_DOCUMENT);
		final Element INSERTED_TAG = this.insertTagBeforeReference(rootTagName, null);
		return INSERTED_TAG;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void loadDocument(final FileSystemPath XMLDocumentPath) throws ParserConfigurationException, SAXException, IOException
		{
		final DocumentBuilderFactory DOCUMENT_BUILDER_GENERATOR = DocumentBuilderFactory.newInstance();
		final DocumentBuilder DOCUMENT_BUILDER = DOCUMENT_BUILDER_GENERATOR.newDocumentBuilder();
		final Boolean WELL_FORMED_DOCUMENT = validateDocument(XMLDocumentPath);
		if (WELL_FORMED_DOCUMENT != null)
			{
			if (WELL_FORMED_DOCUMENT)
				{
				final Document XML_DOCUMENT = DOCUMENT_BUILDER.parse(XMLDocumentPath.getRepresentedFileOrDirectory());
				this.setXMLDoc(XML_DOCUMENT);
				}
			}
		}

	/**
	 * Validates if a document is well formed.
	 *
	 * @param	XMLDocumentPath	Path of the XML document file.
	 * @return
	 * <code>true</code> if the specified document is well-formed. Elsewhere returns
	 * <code>false</code>.
	 * @throws	ParserConfigurationException	If a new document cannot be generated in memory.
	 * @throws	IOException	If any I/O exception occurs.
	 */
	public static Boolean validateDocument(final FileSystemPath XMLDocumentPath) throws ParserConfigurationException, IOException
		{
		boolean wellFormedDocument = true;
		final DocumentBuilderFactory DOCUMENT_BUILDER_GENERATOR = DocumentBuilderFactory.newInstance();
		final DocumentBuilder DOCUMENT_BUILDER = DOCUMENT_BUILDER_GENERATOR.newDocumentBuilder();
		try
			{
			DOCUMENT_BUILDER.parse(XMLDocumentPath.getRepresentedFileOrDirectory());
			}
		catch (SAXException s)
			{
			wellFormedDocument = false;
			//throw s;
			}
		return wellFormedDocument;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final NodeList searchNodeWithXPath(final String XPathSearchString) throws XPathExpressionException
		{
		final XPathFactory XPATH_OBJECT_BUILDER = XPathFactory.newInstance();
		final XPath XPATH = XPATH_OBJECT_BUILDER.newXPath();
		final XPathExpression XPATH_EXPRESSION = XPATH.compile(XPathSearchString);
		final NodeList FOUND_NODES = (NodeList) XPATH_EXPRESSION.evaluate(this.getXMLDoc(), XPathConstants.NODESET);
		return FOUND_NODES;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final ArrayList<String> searchNodeValuesWithXPath(final String XPathSearchString) throws XPathExpressionException
		{
		final NodeList FOUND_NODES = this.searchNodeWithXPath(XPathSearchString);
		final ArrayList<String> VALUES = new ArrayList<>();

		for (int i = 0; i < VALUES.size(); i++)
			{
			VALUES.add(FOUND_NODES.item(i).getNodeValue());
			}
		return VALUES;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String searchNodeValue(final Node nodeWhereToSearch)
		{
		// El método getTextContent devuelve el contenido de texto sin trimar, incluido
		// los campos de texto de las subetiquetas.
		final String NODE_VALUE = nodeWhereToSearch.getNodeValue();
		return NODE_VALUE;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean areTheSameNode(final Node node1, final Node node2)
		{
		final boolean ARE_THE_SAME = node1.isSameNode(node2);
		return ARE_THE_SAME;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final NodeList searchDescendantNodes(final Node initialSearchNode)
		{
		final NodeList DESCENDANT_NODE_LIST = initialSearchNode.getChildNodes();
		return DESCENDANT_NODE_LIST;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Element searchRootTag()
		{
		final Element ROOT_TAG = this.getXMLDoc().getDocumentElement();
		return ROOT_TAG;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Element searchNextTagAtTheSameLevel(final Node initialSearchNode)
		{
		Element foundTag = null;
		Node nextNode = initialSearchNode;

		//getNextSibling devuelve la siguiente etiqueta o campo (de texto, CDATA u otra sección) que se encuentre en el mismo nivel de profundidad (o null si no lo hay)
		while (((nextNode = nextNode.getNextSibling()) != null) && (foundTag == null))
			{
			if (nextNode.getNodeType() == XMLNodeType.TAG.getValue())
				foundTag = (Element) nextNode;
			}
		return foundTag;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Element searchNextDescendantTag(final Node initialSearchNode)
		{
		final Element FOUND_TAG;
		final Node DESCENDANT_NODE = initialSearchNode.getFirstChild();
		if (DESCENDANT_NODE != null) // Si tiene un nodo anidado
			{
			if (DESCENDANT_NODE.getNodeType() == XMLNodeType.TAG.getValue()) //si es una etiqueta
				FOUND_TAG = (Element) DESCENDANT_NODE;
			else // si no, busca la siguiente en el mismo nivel
				FOUND_TAG = this.searchNextTagAtTheSameLevel(DESCENDANT_NODE);
			}
		else
			{
			FOUND_TAG = null;
			}

		return FOUND_TAG;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Element searchParentTag(final Node initialSearchNode)
		{
		Element foundTag = null;
		Node parentNode = initialSearchNode;

		while (((parentNode = parentNode.getParentNode()) != null) && (foundTag == null))
			{
			if (parentNode.getNodeType() == XMLNodeType.TAG.getValue())
				foundTag = (Element) parentNode;
			}

		return foundTag;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Element searchNextTag(final Node initialSearchNode)
		{
		final Element FOUND_TAG;
		Node nextNode = this.searchNextDescendantTag(initialSearchNode);
		if (nextNode != null) //si tiene etiquetas anidadas, busca la primera etiqueta hija
			FOUND_TAG = (Element) nextNode;
		else
			{
			nextNode = (Element) this.searchNextTagAtTheSameLevel(initialSearchNode);
			if (nextNode != null)
				FOUND_TAG = (Element) nextNode;
			else
				FOUND_TAG = (Element) this.searchNextTagAtTheSameLevel(this.searchParentTag(initialSearchNode));
			}
		return FOUND_TAG;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final NodeList searchTagsByName(final String tagName)
		{
		final NodeList FOUND_TAGS = this.getXMLDoc().getElementsByTagName(tagName);
		return FOUND_TAGS;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Attr searchAttributeByNameAndValue(final Element tagWhereToSearch, final String attributeName, final String attributeValue)
		{
		boolean foundAttribute = false;
		Attr atttribute = tagWhereToSearch.getAttributeNode(attributeName);
		if (atttribute != null)
			{
			if ((atttribute.getValue().equals(attributeValue)) || (attributeValue == null))
				foundAttribute = true;
			}

		if (!foundAttribute)
			atttribute = null;

		return atttribute;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final NamedNodeMap searchAttributesInTag(final Element tagWhereToSearch)
		{
		final NamedNodeMap NNM = tagWhereToSearch.getAttributes();
		return NNM;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Element searchTagByNameAndAttributeValues(final String tagName, final HashMap<String, String> attributesAndValues)
		{
		final NodeList NODE_LIST = this.searchTagsByName(tagName);
		Element foundTag = null;
		boolean found = false;
		for (int i = 0; ((i < NODE_LIST.getLength()) && (!found)); i++)
			{
			foundTag = (Element) NODE_LIST.item(i);

			int foundAttributes = 0;
			String key;
			Attr attribute;
			while (attributesAndValues.keySet().iterator().hasNext())
				{
				key = attributesAndValues.keySet().iterator().next();
				attribute = (Attr) foundTag.getAttributes().getNamedItem(key);
				if (attribute != null) //.getNodeType() == Node.ATTRIBUTE_NODE)
					{
					final String ATTRIBUTE_VALUE = attributesAndValues.get(key);
					if (ATTRIBUTE_VALUE != null)		// Este fragmento no ha sido probado. 
						{
						if (attribute.getValue().equals(attributesAndValues.get(key)))
							foundAttributes++;
						}
					}
				}

			if (foundAttributes == attributesAndValues.size())
				found = true;
			}

		return foundTag;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Element insertTagBeforeReference(final String newTagName, final Element referenceTag)
		{
		final Element NEW_TAG = this.getXMLDoc().createElement(newTagName);
		this.getXMLDoc().insertBefore(NEW_TAG, referenceTag);
		return NEW_TAG;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Element insertDescendantTag(final String newDescendantTagName, final Element parentTag)
		{
		final Element NEW_TAG = this.getXMLDoc().createElement(newDescendantTagName);
		parentTag.appendChild(NEW_TAG);
		return NEW_TAG;
		}

	// TODO: ¿Qué ocurre si el atributo ya existe?
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void insertAttributeIntoTag(final Element tag, final String attributeName, final String attributeValue)
		{
		tag.setAttribute(attributeName, attributeValue);
		}

	// TODO: ¿Qué ocurre si la etiqueta ya tiene contenido?
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void insertContentIntoTag(final Element tag, final String content)
		{
		tag.setTextContent(content);
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void deleteTag(final Element tag)
		{
		this.getXMLDoc().removeChild(tag);
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void deleteAttributeFromTag(final Element tag, final String attributeName)
		{
		tag.removeAttribute(attributeName);
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() //throws TransformerException
		{
		try
			{
			// Esto no hay ser humano que lo entienda... ¿podría haberse hecho más feo? ¿Y más difícil?
			// ¿Les costaba mucho hacer un método toString en la clase Document? ¿Y un método Save?...
			final TransformerFactory XML_TRANSFORMATION_GENERATOR = TransformerFactory.newInstance();
			final Transformer TRANSFORMATION = XML_TRANSFORMATION_GENERATOR.newTransformer();
			TRANSFORMATION.setOutputProperty(OutputKeys.INDENT, "yes");

			final String STANDALONE;
			if (this.getXMLDoc().getXmlStandalone())
				STANDALONE = "yes";
			else
				STANDALONE = "no";
			TRANSFORMATION.setOutputProperty(javax.xml.transform.OutputKeys.STANDALONE, STANDALONE);

			final StringWriter SW = new StringWriter();
			final StreamResult TRANSFORMATION_STREAM = new StreamResult(SW);

			final DOMSource XML_SOURCE_TREE = new DOMSource(this.getXMLDoc());
			TRANSFORMATION.transform(XML_SOURCE_TREE, TRANSFORMATION_STREAM);

			return SW.toString();
			}
		catch (Exception e)
			{
			return null;
			}

		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void saveToDisk(final FileSystemPath targetFileSystemPath, final boolean overwrite, final CharacterEncoding encoding) throws IOException, FrameworkException
		{
		boolean save = false;

		if (FileOperations.exists(targetFileSystemPath, true))//targetFileSystemPath.getRepresentedFileOrDirectory().exists())
			{
			if (overwrite)
				{
				FileOperations.delete(targetFileSystemPath);
				save = true;
				}
			}
		else
			{
			save = true;
			}

		if (save)
			{

			try (
					PrintWriter pw = new PrintWriter(targetFileSystemPath.getRepresentedFileOrDirectory(), encoding.getValue());
					BufferedWriter bw = new BufferedWriter(pw);
				)
				{
				bw.write(this.toString());
				bw.flush();
				pw.flush();
				}
			}
		}
	}

// TODO:
// esto necesita de otro método que setee el atributo como ID (un atributo con nombre Id o id no es un identificador hasta que no se hace el setIdAttribute)
//	public final Element buscarEtiquetaPorId(final String id)
//		{
//		try
//			{
//			Element etiquetaEncontrada = null;
//			etiquetaEncontrada = this.getXMLDoc().getElementById(id);
//			super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "buscarEtiquetaPorId", null, null, id);
//			return etiquetaEncontrada;
//			}
//		catch (Exception e)
//			{
//			super.registrarEstado(new Date(), this.getClass().getCanonicalName(), "buscarEtiquetaPorId", e, null, id);
//			return null;
//			}
//		}