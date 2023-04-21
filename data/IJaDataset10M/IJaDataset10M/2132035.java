package org.xmlcml.cmlimpl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import java.net.URL;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.CellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.co.demon.ursus.dom.PMRDelegate;
import uk.co.demon.ursus.dom.PMRDocumentImpl;
import uk.co.demon.ursus.dom.PMRElement;
import uk.co.demon.ursus.dom.PMRElementImpl;
import uk.co.demon.ursus.dom.PMRNode;

import jumbo.xml.util.Util;

import org.xmlcml.cml.AbstractBase;
import org.xmlcml.cml.AttributeConvention;
import org.xmlcml.cml.AttributeCount;
import org.xmlcml.cml.AttributeDictRef;
import org.xmlcml.cml.AttributeId;
import org.xmlcml.cml.AttributeSize;
import org.xmlcml.cml.AttributeTitle;
import org.xmlcml.cml.AttributeUnits;
import org.xmlcml.cml.CMLAtomFactory;
import org.xmlcml.cml.CMLBondFactory;
import org.xmlcml.cml.CMLDocument;
import org.xmlcml.cml.CMLException;
import org.xmlcml.cml.CMLMoleculeFactory;
import org.xmlcml.cml.CMLStringVal;

import org.xmlcml.cml.metadata.CMLUnits;
import org.xmlcml.cml.metadata.CMLDictionaryEntry;

// not happy about the architecture... but
import org.xmlcml.cml.style.TreeStyle;

import org.xmlcml.cmlimpl.metadata.DictionaryImpl;

import uk.co.demon.ursus.util.PMRTrace;

/** <p>The base class for all elementObjects mentioned in the CML
DTD.
</p>
<p>
 Any CML element may have attributes:
<ul>
<li>title</li>
<li>id</li>
<li>dictRef</li>
<li>convention</li>
</ul>
and convenience get/set methods are provided for all, through
the interfaces AttributeTitle, AttributeId and AttributeConvention.</p>
<p>An element (FOO) subclassed from a AbstractBase may be constructed in the
following ways:
<ul>
<li>FOO(). Creates a new empty element with null values of the
attributes. Required for newInstance(), but use carefully since
it has no tagName and no document associated with it.</li>
<li>FOO(String tagName, Document document). Creates an empty
 element with null attribute names.</li>
<li>FOO(org.w3c.dom.Element element). Creates a subclassed Element with
the same attribute values as the input Element. Used when a DOM has
been created with non-CML-aware software. The routine
makeAndProcessSubclass(Element element) will replace the current Element
with the appropriate CML subclass.</li>
<li>FOO(String title, String id, String dictRef, String conventionName).
Makes subclassed Element and sets attribute values.
</ul>
@author P.Murray-Rust, 1999, 2000
*/
public abstract class CMLBaseImpl extends PMRElementImpl
	implements org.xmlcml.cml.AbstractBase, PMRTrace	{

	static CMLMoleculeFactory moleculeFactory = null;

	public final static void showElementNames() {
		System.out.println("\nElement  Names:");
		for (int i = 0; i < ELEMENT_COUNT; i++) {
			System.out.println("   "+ELEMENT_NAMES[i]);
		}
	}

	protected static Hashtable conventionTable = new Hashtable();

	protected String title;
	protected String id;
	protected String dictRef;
	protected String conventionName;

	protected ListImpl thisLog;
	protected TreeStyle treeStyle = null;
	protected JPanel dictionaryEntryPanel;

/** messages */
	static int BASE_GENERAL	= 0;

	static String[] messageCodes = {
		"BASE_GENERAL",
	};
	static String[] messageBodies = {
		"General message",
	};
	protected String[] getMessageCodes() {return messageCodes;}
	protected String[] getMessageBodies() {return messageBodies;}


/** create a Node WITHOUT tagName OR document. Use with care */
// awful, but there seems to be no no-arg constructor
	public CMLBaseImpl() {}

/** used when creating new nodes in a DOM */
// this is the proper method...
	public CMLBaseImpl(String tagName, Document document) {
//		super(document, NAMESPACE_URI, ((getDefaultPrefix() == null) ? tagName : getDefaultPrefix()+":"+
//		((tagName.indexOf(":") == -1) ? tagName : tagName.substring(0, tagName.indexOf(":")+1))) );
		super(document, null, tagName);
	}

//	public CMLBaseImpl(String tagName, Document document) {
//		super(tagName, document);
//	}

/** used when analysing a DOM */
	public CMLBaseImpl(Element element) {
		super(element);
		copyAndExtractCommonAttributes(element);
	}
	public void copyAndExtractCommonAttributes(Element element) {
		PMRDelegate.copyAttributesFrom(this, element);
		title	= this.getAttribute(TITLE);
		id		= this.getAttribute(ID);
		dictRef = this.getAttribute(DICTREF);
		conventionName = this.getAttribute(CONVENTION);
	}

	public CMLBaseImpl(String tagName, Document document, String title, String id, String dictRef,
		String conventionName) {
		this(tagName, document);
		this.setTitle(title);
		this.setId(id);
		this.setDictRef(dictRef);
		this.setConventionName(conventionName);
	}

	protected abstract String getClassTagName();

//	public static String getDefaultPrefix() {return "cml";}
	public static String getDefaultPrefix() {return null;}

	public void setTitle(String title) {
		this.title = title;
		if (!PMRDelegate.isEmptyAttribute(title)) {
			this.setAttribute(TITLE, title);
		}
	}
	public String getTitle() {return title;}

	public void setId(String id) {
		this.id = id;
		if (!PMRDelegate.isEmptyAttribute(id)) {
			this.setAttribute(ID, id);
		}
	}
	public String getId() {
		if (id == null) id = this.getAttribute(ID);
		return id;
	}

	public void setDictRef(String dictRef) {
		this.dictRef = dictRef;
		if (!PMRDelegate.isEmptyAttribute(dictRef)) {
			this.setAttribute(DICTREF, dictRef);
		}
	}
	public String getDictRef() {return dictRef;}

	public void setConventionName(String conventionName) {
		this.conventionName = conventionName;
		if (!PMRDelegate.isEmptyAttribute(conventionName)) {
			this.setAttribute(CONVENTION, conventionName);
		}
	}
	public String getConventionName() {return conventionName;}

/** recursively process all nodes. Assumes we have built a DOM
	and all Nodes are of type Element. They need to be converted
	to PMRElement or a subclass of it*/
	public static PMRElement convertToSubclass(Element element) throws Exception {

		if (moleculeFactory == null) {
			moleculeFactory = new CMLDocumentImpl().getMoleculeFactory();
			if (moleculeFactory == null) throw new CMLException("No default moleculeFactory");
		}

		String tagName = element.getTagName();

// crude namespace processing
		int idx = tagName.indexOf(":");
		if (idx != -1) {
			tagName = tagName.substring(idx+1);
		}

// crude, but we have a performance hit with newInstance()
		boolean processMe = true;
		if (false) {
		} else if (tagName.equals(ELEMENT_NAMES[ANGLE])) {
			element = new AngleImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[ATOMARRAY])) {
			element = new AtomArrayImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[ATOM])) {
			element = moleculeFactory.getAtomFactory().createAtom(element.getOwnerDocument());
			((AtomImpl)element).initialise0();
			((CMLBaseImpl)element).copyAndExtractCommonAttributes(element);
			System.out.println("Atom: "+((AtomImpl)element).getId());
		} else if (tagName.equals(ELEMENT_NAMES[BONDARRAY])) {
			element = new BondArrayImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[BOND])) {
			element = moleculeFactory.getBondFactory().createBond(element.getOwnerDocument());
			((CMLBaseImpl)element).copyAndExtractCommonAttributes(element);
		} else if (tagName.equals(ELEMENT_NAMES[CML])) {
			element = new CMLImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[CRYSTAL])) {
			element = new CrystalImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[ELECTRON])) {
			element = new ElectronImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[FEATURE])) {
			element = new FeatureImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[FLOATARRAY])) {
			element = new FloatArrayImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[FLOATMATRIX])) {
			element = new FloatMatrixImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[FLOAT])) {
			element = new FloatValImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[FORMULA])) {
			element = new FormulaImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[INTEGER])) {
			element = new IntegerValImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[INTEGERARRAY])) {
			element = new IntegerArrayImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[LINK])) {
			element = new LinkImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[LIST])) {
			element = new ListImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[MOLECULE])) {
			element = moleculeFactory.createMolecule(element.getOwnerDocument());
			((CMLBaseImpl)element).copyAndExtractCommonAttributes(element);
		} else if (tagName.equals(ELEMENT_NAMES[REACTION])) {
			element = new ReactionImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[SEQUENCE])) {
			element = new SequenceImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[STRINGARRAY])) {
			element = new StringArrayImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[STRING])) {
			element = new StringValImpl(element);
		} else if (tagName.equals(ELEMENT_NAMES[TORSION])) {
			element = new TorsionImpl(element);
		} else {
			element = new PMRElementImpl(element);
			processMe = false;
		}

		CMLBaseImpl.convertChildrenToSubclass(element);

		return (PMRElement) element;
	}

	protected static void convertChildrenToSubclass(Element element) throws Exception {
// recurse through descendants
		Vector childVector = PMRDelegate.getChildElements(element);
		for (int i = 0; i < childVector.size(); i++) {
            Element childElement = (Element) childVector.elementAt(i);
			try {
				childElement = CMLBaseImpl.convertToSubclass(childElement);
			} catch (Exception e) {
				System.out.println("invalid: "+childElement+" ("+e+")");
				e.printStackTrace();
			}
		}
// process element
		((PMRElement) element).processDOM();
//		if (element instanceof AbstractBase && !((AbstractBase) element).isValid()) {
//			System.out.println("invalid: "+element);
//		}
	}

/** create a PMRElement (usually a AbstractBase) and processDOM() it.
*/
	public static PMRElement getSubClassedElement(String name, CMLDocument cmlDocument)
		throws Exception {
		if (moleculeFactory == null) {
			moleculeFactory = new CMLDocumentImpl().getMoleculeFactory();
			if (moleculeFactory == null) throw new CMLException("No default moleculeFactory");
		}

		// crude namespace processing
		int idx = name.indexOf(":");
		if (idx != -1) {
			name = name.substring(idx+1);
		}
		PMRElement element;
		if (false) {
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.ANGLE])) {
			element = new AngleImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.ATOMARRAY])) {
			element = new AtomArrayImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.ATOM])) {
			element = (CMLBaseImpl) moleculeFactory.getAtomFactory().createAtom(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.BONDARRAY])) {
			element = new BondArrayImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.BOND])) {
			element = (CMLBaseImpl) moleculeFactory.getBondFactory().createBond(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.CML])) {
			element = new CMLImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.CRYSTAL])) {
			element = new CrystalImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.ELECTRON])) {
			element = new ElectronImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.FEATURE])) {
			element = new FeatureImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.FLOATARRAY])) {
			element = new FloatArrayImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.FLOATMATRIX])) {
			element = new FloatMatrixImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.FLOAT])) {
			element = new FloatValImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.FORMULA])) {
			element = new FormulaImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.INTEGER])) {
			element = new IntegerValImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.INTEGERARRAY])) {
			element = new IntegerArrayImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.LINK])) {
			element = new LinkImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.LIST])) {
			element = new ListImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.MOLECULE])) {
			element = (CMLBaseImpl) moleculeFactory.createMolecule(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.REACTION])) {
			element = new ReactionImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.SEQUENCE])) {
			element = new SequenceImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.STRINGARRAY])) {
			element = new StringArrayImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.STRING])) {
			element = new StringValImpl(cmlDocument);
		} else if (name.equals(AbstractBase.ELEMENT_NAMES[AbstractBase.TORSION])) {
			element = new TorsionImpl(cmlDocument);
		} else {
			element = new PMRElementImpl(name, cmlDocument);
		}

		// do NOT process the element now

		return element;
	}

/** subclassed if necessary */
	public boolean processDOM() throws CMLException {
		if (!domNeedsProcessing) return true;
//		super.processDOM();
		if (!processDOMHasConvention()) return false;
		if (!processDOMHasDictRef()) return false;
		if (!processDOMHasId()) return false;
		if (!processDOMHasTitle()) return false;
		domNeedsProcessing = false;
		return true;
	}
	public void processDOM(boolean processMe) throws CMLException {
		domNeedsProcessing = processMe;
		processDOM();
	}

// static convenience methods
	public static boolean processDOMHasConvention(AttributeConvention element) {
		element.setConventionName(((Element) element).getAttribute(AttributeConvention.CONVENTION));
		return true;
	}

	public static boolean processDOMHasDictRef(AttributeDictRef element) {
		element.setDictRef(((Element) element).getAttribute(AttributeDictRef.DICTREF));
		return true;
	}

	public static boolean processDOMHasCount(AttributeCount element) {
		double count = 1;
		String dd = ((Element) element).getAttribute(AttributeCount.COUNT);
		if (!PMRDelegate.isEmptyAttribute(dd)) {
			try {
				count = new Double(dd).doubleValue();
			} catch (Exception e) {}
		}
		element.setCount(count);
		return true;
	}

	public static boolean processDOMHasId(AttributeId element) {
		element.setId(((Element) element).getAttribute(AttributeId.ID));
		return true;
	}

	public static boolean processDOMHasTitle(AttributeTitle element) {
		element.setTitle(((Element) element).getAttribute(AttributeTitle.TITLE));
		return true;
	}

/** gets the SIZE attribute */
	public static boolean processDOMHasSize(AttributeSize element) {
		int size;
		String sz = ((Element) element).getAttribute(AttributeSize.SIZE);
		if (!PMRDelegate.isEmptyAttribute(sz)) {
			try {
				size = Integer.parseInt(sz);
				((Element) element).setAttribute(AttributeSize.SIZE, ""+size);
			} catch (Exception e) {}
		}
		return true;
	}

	public static boolean processDOMHasUnits(AttributeUnits element) {
//		element.setUnits(((Element) element).getAttribute(AttributeUnits.UNITS));
		System.out.println("set units NYI");
		return true;
	}

	public boolean processDOMHasConvention() {return CMLBaseImpl.processDOMHasConvention(this);}
	public boolean processDOMHasDictRef() {return CMLBaseImpl.processDOMHasDictRef(this);}
	public boolean processDOMHasId() {return CMLBaseImpl.processDOMHasId(this);}
	public boolean processDOMHasTitle() {return CMLBaseImpl.processDOMHasTitle(this);}

/** output debug info to Writer and return String equivalent (e.g. with
	new StringWriter()
	@param Writer w Writer to output to
	@return the String equivalent
	*/
	public void debug(Writer w) throws IOException {
		PMRDelegate.outputStartTag(this, w, PMRNode.TAGGED, 0/*, "cml", null*/);
		outputChildContent(this, w, PMRNode.TAGGED, 0/*, "cml", null*/);
		PMRDelegate.outputEndTag(this, w, PMRNode.TAGGED, 0);
	}

/** output debug info to System.out
	*/
	public void debug() {
		StringWriter w = new StringWriter();
		try {this.debug(w); System.out.println(w.toString());} catch (Exception e) {}
	}

	static void outputChildContent
		(Element element, Writer w, int type, int level) throws IOException {
		NodeList childList = element.getChildNodes();
		for (int i = 0; i < childList.getLength(); i++) {
			CMLBaseImpl.debug(childList.item(i), w);
		}
	}

	static void debug(Node node, Writer w) throws IOException {
		if (node instanceof AbstractBase) {
			((AbstractBase)node).debug(w);
		} else if (node instanceof Element) {
			Element element = (Element) node;
			PMRDelegate.outputStartTag(element, w, PMRNode.TAGGED, 0/*, null, null*/);
			outputChildContent(element, w, PMRNode.TAGGED, 0/*, "cml", null*/);
			PMRDelegate.outputEndTag(element, w, PMRNode.TAGGED, 0);
		} else {
			w.write(node.getNodeValue());
		}
	}

//	/** output in array format
//	@param Writer w output Writer (must be opened/closed outside)
//	*/
//	public void outputArray(Writer w) throws IOException {
//		PMRDelegate.outputStartTag(this, w, PMRNode.TAGGED, 0);
//		NodeList childs = this.getChildNodes();
//		for (int i = 0; i < child.length(); i++) {
//			Node child = childs.item(i);
//			int type = child.getNodeType();
//			if (type == Node.TEXT) {
//				w.write(child.getNodeValue());
//			} else if (child instanceof AbstractBase) {
//				this.outputArray(w);
//			} else if (child instanceof Element) {
//				PMRDelegate.outputEventStream((Element) child, w, PMRNode.TAGGED, 0);
//			} else {
//				// comments and PIs omitted
//			}
//		}
//		PMRDelegate.outputEndTag(this, w, PMRNode.TAGGED, 0);
//	}

	public static void help() {
		showElementNames();
		AbstractAtomImpl.showBuiltinNames();
		AbstractBondImpl.showBuiltinNames();
		CrystalImpl.showBuiltinNames();
	}

// static convenience methods
	public static boolean updateDOMHasConvention(AttributeConvention element) {
		String conventionName = element.getConventionName();
		if (conventionName != null && conventionName != "") {
			((Element) element).setAttribute(AttributeConvention.CONVENTION, ""+conventionName);
		}
		return true;
	}

	public static boolean updateDOMHasDictRef(AttributeDictRef element) {
		String dictRef = element.getDictRef();
		if (dictRef != null && dictRef != "") {
			((Element) element).setAttribute(AttributeDictRef.DICTREF, ""+dictRef);
		}
		return true;
	}

	public static boolean updateDOMHasCount(AttributeCount element) {
		double count = element.getCount();
		if (!Double.isNaN(count)) {
			((Element) element).setAttribute(AttributeCount.COUNT, ""+count);
		}
		return true;
	}

	public static boolean updateDOMHasId(AttributeId element) {
		String id = element.getId();
		if (id != null && id != "") {
			((Element) element).setAttribute(AttributeId.ID, ""+id);
		}
		return true;
	}

	public static boolean updateDOMHasTitle(AttributeTitle element) {
		String title = element.getTitle();
		if (title != null && title != "") {
			((Element) element).setAttribute(AttributeTitle.TITLE, ""+title);
		}
		return true;
	}

/** calculates the SIZE attribute from the array. should not be
set by user. If size <= 0 do not update
*/
	public static boolean updateDOMHasSize(AttributeSize element) {
		int size = element.getSize();
		if (size > 0) {
			((Element) element).setAttribute(AttributeSize.SIZE, ""+size);
		}
		return true;
	}

	public static boolean updateDOMHasUnits(AttributeUnits element) {
		CMLUnits units = element.getUnits();
		if (units != null) {
			((Element) element).setAttribute(AttributeUnits.UNITS, ""+units.toString());
		}
		return true;
	}

	public boolean updateDOMHasConvention() {return CMLBaseImpl.updateDOMHasConvention(this);}
	public boolean updateDOMHasDictRef() {return CMLBaseImpl.updateDOMHasDictRef(this);}
	public boolean updateDOMHasId() {return CMLBaseImpl.updateDOMHasId(this);}
	public boolean updateDOMHasTitle() {return CMLBaseImpl.updateDOMHasTitle(this);}

	public boolean updateDOM(boolean updateMe) throws CMLException {
		domNeedsUpdating = updateMe;
		return updateDOM();
	}

	public boolean updateDOM() throws CMLException {
		if (!domNeedsUpdating) return true;
		if (!updateDOMHasConvention()) return false;
		if (!updateDOMHasDictRef()) return false;
		if (!updateDOMHasId()) return false;
		if (!updateDOMHasTitle()) return false;
		domNeedsUpdating = false;
		return true;
	}

/** replaces all elements of this type with contents of vector */
	protected boolean updateDOMFromVector(Vector elementVector, String elementName) throws CMLException {
		if (elementVector != null) {
			Vector childVector = PMRDelegate.getChildrenWithElementName(this, elementName);
			if (childVector != null) {
				for (int i = 0; i < childVector.size(); i++) {
					this.removeChild((Node) childVector.elementAt(i));
				}
			}
			for (int i = 0; i < elementVector.size(); i++) {
				CMLBaseImpl elementImpl = (CMLBaseImpl) elementVector.elementAt(i);
				if (!elementImpl.updateDOM()) {
					return false;
				}
				this.appendChild(elementImpl);
			}
		}
		return true;
	}

	public static void test1(String args[]) throws Exception {
		URL url = new URL(Util.makeAbsoluteURL(args[0]));
		CMLDocument document = CMLDocumentImpl.createCMLDocument(url.toString());
		PrintWriter writer = new PrintWriter(System.out);
		PMRDelegate.outputEventStream(document.getDocumentElement(), writer, PMRNode.PRETTY, 0);
		writer.println("**** Finished output *****");
		writer.flush();
		writer.close();
	}

	static void readXMLTest(String[]args, String className, String tagName) throws Exception {
		if (args.length == 0) {
			System.out.println("Usage: java "+className+" testUri");
			System.exit(1);
		}
		URL url = new URL(Util.makeAbsoluteURL(args[0]));
		CMLDocument document = CMLDocumentImpl.createCMLDocument(url.toString());
		document.readXMLTest(tagName);
	}

	public void readXMLTest() throws CMLException {
		System.out.println("Testing element: "+this.getTagName());
		System.out.println("Class: "+this.getClass());
	}

	public static void buildXMLTest() throws Exception {
//		System.out.println("Testing element: "+this.getTagName());
//		System.out.println("Class: "+this.getClass());
	}

	/** set a trace for objects of a class. */
	public void addTracer(Class theClass) {
		try {
			Object obj = theClass.newInstance();
			if (obj instanceof AbstractBase) {
				CMLBaseImpl c = (CMLBaseImpl) obj;
				c.setTrace(true);
			}
		} catch (Exception e) {System.out.println(""+e);}
	}

	/** remove a trace.for objects of a class  */
	public void removeTracer(Class theClass) {
		try {
			Object obj = theClass.newInstance();
			if (obj instanceof AbstractBase) {
				CMLBaseImpl c = (CMLBaseImpl) obj;
				c.setTrace(true);
			}
		} catch (Exception e) {System.out.println(""+e);}
	}

	public abstract void setTrace(boolean t);
	public abstract boolean isTrace();

	public void trace(String message) {
		if (isTrace()) {
			String s = ""+this.getClass();
			int idx = s.lastIndexOf(".");
			System.out.println(s.substring(idx+1)+": "+message);
		}
	}

	public void addToThisLog(int messageId, String message) {
		String messageCode = (messageId >= 0) ? getMessageCodes()[messageId] : "";
		String messageBody = (messageId >= 0) ? getMessageBodies()[messageId]+": "+message : message;
		CMLStringVal sv = new StringValImpl(this.getOwnerDocument(), messageBody, null);
		if (!messageCode.equals("")) sv.setAttribute("title", messageCode);
		if (thisLog == null) {
			thisLog = new ListImpl(this.getOwnerDocument());
			thisLog.setAttribute("title", "log");
			this.appendChild(thisLog);
		}
		thisLog.appendChild(sv);
	}

	/** non-CORE. how to display object
	@return Component component which can be add'ed to a parent;
	default is JTextArea with object.getClass() and object.toString() content
	*/
	public Component getDisplay() {
		JTextArea textArea = new JTextArea(5, 20);
		textArea.setText("$"+this.getClass()+": "+this.toString());
		return textArea;
	}

	/** non-CORE. how to display object in a tree; subclassed as appropriate
	@return TreeStyle the style (null by default)*/
	public TreeStyle getTreeStyle() {
		if (treeStyle == null) treeStyle = createDefaultTreeStyle(this);
		return treeStyle;
	}

	/** non-CORE. how to display object in a tree; subclassed id appropriate
	@param TreeStyle the style*/
	public void setTreeStyle(TreeStyle treeStyle) {
		this.treeStyle = treeStyle;
	}

	/** non-core */
	public static TreeStyle createDefaultTreeStyle(AbstractBase ab) {
		TreeStyle treeStyle = null;
		try {
			treeStyle = (TreeStyle) Class.forName("org.xmlcml.cmlimpl.style.TreeStyleImpl").newInstance();
			treeStyle.setCMLObject(ab);
			String title = ab.getTitle();
			String tooltip = title;
			String dictRef = ab.getDictRef();
			if (dictRef != null) {
				CMLDictionaryEntry entry = DictionaryImpl.getDictionaryEntryById(dictRef);
				if (entry != null) {
					String dTitle = entry.getTitle();
					if (dTitle != null && !dTitle.equals("")) tooltip = dTitle;
					String abbrev = entry.getAbbreviation();
					if (abbrev != null && !abbrev.equals("")) title = abbrev;
				}
			}
			treeStyle.setTitle(title);
			treeStyle.setToolTip(tooltip);
		} catch (Exception e) {Util.bug(e);}
		return treeStyle;
	}

	/** NON-CORE supports rendering of JTables */
	public static CMLBaseImpl getCMLBaseImpl(TableModel model, int row, int column) {
		Object obj = model.getValueAt(row, column);
		if (obj != null && obj instanceof CMLBaseImpl) return (CMLBaseImpl) obj;
		return null;
	}

	/** gets the dictionary entry based on the dictRef attribute */
	public CMLDictionaryEntry getDictionaryEntry() {
		String dictRef = this.getDictRef();
		if (dictRef == null || dictRef.trim().equals("")) return null;
		return DictionaryImpl.getDictionaryEntryById(dictRef);
	}

	public static TableCellRenderer getTableCellRenderer(TableModel model, int row, int column) {
		CMLBaseImpl cmlBase = CMLBaseImpl.getCMLBaseImpl(model, row, column);
		if (cmlBase == null) {
			return new DefaultTableCellRenderer();
		}
		TableCellRenderer tcr = cmlBase.getTableCellRenderer();
		return tcr;
	}

// default is a DefaultTableCellRenderer
	public TableCellRenderer getTableCellRenderer() {
		CMLDictionaryEntry entry = this.getDictionaryEntry();
		if (entry != null) {
			Vector enum = entry.getEnumeration();
			if (enum != null) {
				ComboCellRenderer cocr = new ComboCellRenderer();
				cocr.setEnumeration(enum);
				cocr.setCMLBaseImpl(this);
				return cocr;
			}
		}
		TextCellRenderer tcr = new TextCellRenderer();
		tcr.setCMLBaseImpl(this);
		return tcr;
	}

	public static TableCellEditor getTableCellEditor(TableModel model, int row, int column) {
		CMLBaseImpl cmlBase = CMLBaseImpl.getCMLBaseImpl(model, row, column);
		if (cmlBase == null) return null;
		return cmlBase.getTableCellEditor();
	}

	/** NON-CORE - returns a table cell editor; this is based on the dictionary
	when possible */
	public TableCellEditor getTableCellEditor() {
		CMLDictionaryEntry entry = this.getDictionaryEntry();
		if (entry != null) {
			Vector enum = entry.getEnumeration();
			if (enum != null) {
				ComboCellEditor coce = new ComboCellEditor();
				coce.setEnumeration(enum);
				coce.setCMLBaseImpl(this);
				return coce;
			}
		}
		TextCellEditor tce = new TextCellEditor();
		tce.setCMLBaseImpl(this);
		return tce;
	}
	public void showDictionaryEntry(JPanel p) {
		CMLDictionaryEntry entry = this.getDictionaryEntry();
		if (entry != null) {
			entry.display(p);
		} else {
			p.removeAll();
			p.setLayout(new BorderLayout());
			p.add(new JLabel("NO DICTIONARY ENTRY"), BorderLayout.CENTER);
			p.invalidate();
			p.revalidate();
			Container cont = p.getParent();
			if (cont != null) {
				cont.invalidate();
				cont.validate();
			}
		}
	}
	public void setDictionaryEntryPanel(JPanel p) {
		this.dictionaryEntryPanel = p;
	}
	public String getStringValue() {return "No Value";}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage: java org.xmlcml.cmlimpl.CMLBaseImpl [-help] url.cml ");
			System.exit(0);
		}
		try {
			if (args[0].equalsIgnoreCase("-help")) {
				help();
			} else {
				test1(args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


