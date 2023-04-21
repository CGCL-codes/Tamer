package com.technosophos.rhizome.document;

import java.util.ArrayList;
import java.util.List;
import java.io.CharArrayWriter;
import java.io.OutputStream;
import java.io.Writer;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import com.technosophos.rhizome.document.DocumentID;
import com.technosophos.rhizome.document.Extension;
import com.technosophos.rhizome.document.Metadatum;
import com.technosophos.rhizome.document.Relation;
import com.technosophos.rhizome.document.RhizomeData;
import com.technosophos.rhizome.controller.Presentable;
import static com.technosophos.rhizome.document.XMLElements.*;

/**
 * A document is a foundational piece of the Rhizome architecture.
 * Each document in Rhizome is identified by a unique ID (called a 
 * Document ID or docID). The RhizomeDocument stores all parts of a document
 * that Rhizome cares about.
 * <p>
 * A document is composed of four parts: metadata, relations, the body of
 * the document, and extensions.</p>
 * <p><b>Metadata:</b> metadata is data about the contents of the document.
 * Typically, thigs like title, modification and creation dates, author
 * information, and the like are considered metadata. A metadata section 
 * contains zero or more metadatum items, where each metadatum has one name, 
 * and any number of values.</p>
 * <p><b>Relations:</b> Relations are ties between this document and other
 * documents contained in Rhizome's content repo. Typically, a relation 
 * contains two pieces of data. The first is the type of relation (in hierarchical 
 * structures, we might talk about parents, children, etc. No hierarchy is
 * imposed by Rhizome, though.). The second is the ID of the document to
 * which this document is related. While any given relation can point to
 * only one document, there is no constraint to prohibit multiple 
 * relationships, all of the same relationship type.</p>
 * <p><b>The Body:</b> The body of the document contains whatever data is
 * considered the document itself. This might be HTML, XML, or text. (To
 * store binary information, use some encoding type, such as Base64, that
 * outputs text.) XML and XHTML content may be parsed, but HTML and Text
 * data are not parsed by Rhizome.</p>
 * <p><b>Extensions:</b> The extension mechanism provides a way to add on 
 * additional components to the Rhizome document structure. Generally, the 
 * extension is treated as structured content (XML), and is parsed.</p>
 * @see com.technosophos.rhizome.document.Metadatum
 * @see com.technosophos.rhizome.document.Relation
 * @see com.technosophos.rhizome.document.RhizomeData
 * @see com.technosophos.rhizome.document.Extension 
 * @author mbutcher
 *
 */
public class RhizomeDocument implements Presentable {

    private List<Metadatum> metadata = null;

    private RhizomeData body = null;

    private ArrayList<Extension> extensions = null;

    private ArrayList<Relation> relations = null;

    private String docID = null;

    /**
	 * Construct an empty document.
	 * This will generate a document ID automatically, using the 
	 * DocumentID class.
	 * @see com.technosophos.rhizome.DocumentID
	 */
    private RhizomeDocument() {
        this(DocumentID.generateDocumentID());
    }

    /** 
	 * Create an empty document with an ID.
	 * @param docID
	 */
    public RhizomeDocument(String docID) {
        this(docID, new ArrayList<Metadatum>(), new ArrayList<Relation>(), new RhizomeData(), new ArrayList<Extension>());
    }

    /**
	 * This is a convenience constructor for quickly creating a document
	 * with a document ID and with a body. The body will be assigned the 
	 * MIME type that is set as default for RhizomeDocument (usually 
	 * text/html), and it will be stored in a new RhizomeData object.
	 * @param docID
	 * @param data
	 * @see RhizomeData
	 */
    public RhizomeDocument(String docID, String data) {
        this(docID, new ArrayList<Metadatum>(), new ArrayList<Relation>(), new RhizomeData(data), new ArrayList<Extension>());
    }

    public RhizomeDocument(String docID, List<Metadatum> meta) {
        this(docID, meta, new ArrayList<Relation>(), new RhizomeData(""), new ArrayList<Extension>());
    }

    /**
	 * This is the full constructor.
	 * @param docID The String unique document ID.
	 * @param metadata
	 * @param relations
	 * @param body
	 * @param extensions
	 */
    public RhizomeDocument(String docID, List<Metadatum> metadata, ArrayList<Relation> relations, RhizomeData body, ArrayList<Extension> extensions) {
        this.docID = docID;
        this.metadata = metadata;
        this.relations = relations;
        this.extensions = extensions;
        this.body = body;
    }

    /**
	 * Get the document ID for this document.
	 * @return The document ID.
	 */
    public String getDocumentID() {
        return this.docID;
    }

    /**
	 * Get the document ID for this document.
	 * A convenience call for getDocumentID() (Since getDocID was the 
	 * method name in Pilaster).
	 * @return The document ID
	 */
    public String getDocID() {
        return this.getDocumentID();
    }

    /**
	 * Get a list of Metadatum items.
	 * 
	 * @return ArrayList of Metadatum items.
	 */
    public List<Metadatum> getMetadata() {
        return this.metadata;
    }

    /**
	 * Returns the Metadatum with the given name.
	 * If no such metadatum is found, this returns null.
	 * @param name Name of the metadatum
	 * @return The metadatum with that name, or null.
	 */
    public Metadatum getMetadatum(String name) {
        for (Metadatum m : this.metadata) {
            if (m.getName().equals(name)) return m;
        }
        return null;
    }

    /**
	 * Get the number of Metadatum items.
	 * @return
	 */
    public int metadataSize() {
        return this.metadata.size();
    }

    /**
	 * Get relations list
	 * @return list of Relation objects.
	 */
    public ArrayList<Relation> getRelations() {
        return this.relations;
    }

    /**
	 * Checks to see if this document is related to the given document ID.
	 * 
	 * This method is relatively slow -- especially on documents with lots of relations.
	 * 
	 * @param docID Doc ID to check
	 * @return True if the document ID is in one of this document's Relations.
	 */
    public boolean isRelatedTo(String docID) {
        for (Relation r : this.relations) if (r.getDocID().equals(docID)) return true;
        return false;
    }

    public boolean isRelatedTo(String docID, String parentType) {
        for (Relation r : this.relations) {
            if (r.getRelationType().equals(parentType) && r.getDocID().equals(docID)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Get the body (the data) of the document.
	 * @return the body as a RhizomeData object.
	 */
    public RhizomeData getData() {
        return this.body;
    }

    /**
	 * Get a list of extensions
	 * @return a list of Extension objects.
	 */
    public ArrayList<Extension> getExtensions() {
        return this.extensions;
    }

    /**
	 * Gets extension by name.
	 * @param name
	 */
    public Extension getExtensionByName(String name) {
        Extension ext;
        for (int i = 0; i < this.extensions.size(); ++i) {
            ext = this.extensions.get(i);
            if (ext.getName().equals(name)) return ext;
        }
        return null;
    }

    /**
	 * Checks to see if extension exists.
	 * @param name
	 * @return true if the extension is found.
	 */
    public boolean hasExtension(String name) {
        Extension ext;
        for (int i = 0; i < this.extensions.size(); ++i) {
            ext = this.extensions.get(i);
            if (ext.getName().equals(name)) return true;
        }
        return false;
    }

    /**
	 * Set the body text for the object.
	 * This string gets wrapped in a RhizomeData object.
	 * Generally, you should use either setBody(String, String)
	 * to set the content type, or you should use setBody(RhizomeData).
	 * @param txt
	 */
    public void setBody(String txt) {
        this.setBody(new RhizomeData(txt));
    }

    /**
	 * Set the body text for the object.
	 * Creates a new RhizomeData object with the correct
	 * MIME type, and then sets the text.
	 * @param mimeType
	 * @param txt
	 */
    public void setBody(String mimeType, String txt) {
        this.setBody(new RhizomeData(mimeType, txt));
    }

    /**
	 * Set the body text for the object.
	 * @param rd
	 */
    public void setBody(RhizomeData rd) {
        this.body = rd;
    }

    /**
	 * Add a relation to the existing list.
	 * @param rel
	 * 
	 */
    public void addRelation(Relation rel) {
        this.relations.add(rel);
    }

    /**
	 * Add a new relation.
	 * This is a convenience method to quickly add a new relation.
	 * @param relType The type of relation (sive "the relation name")
	 * @param relDocID The docID of the related document.
	 */
    public void addRelation(String relType, String relDocID) {
        this.relations.add(new Relation(relType, relDocID));
    }

    /**
	 * Convenience method to add a relation.
	 * Note that this will use the default relation type.
	 * @param relDocID The document ID of the related document.
	 * @see Relation(String)
	 */
    public void addRelation(String relDocID) {
        this.relations.add(new Relation(relDocID));
    }

    /**
	 * Add an extension to the existing list.
	 * @param ext
	 */
    public void addExtension(Extension ext) {
        this.extensions.add(ext);
    }

    /**
	 * Add a metadatum item to the existing list.
	 * This does NOT overwrite existing metadata.
	 * @param meta Initialized Metadatum object.
	 * @see replaceMetadatum(Metadatum)
	 */
    public void addMetadatum(Metadatum meta) {
        this.metadata.add(meta);
    }

    /**
	 * Replace all existing metadata of the same name with this one.
	 * @param meta
	 */
    public void replaceMetadatum(Metadatum meta) {
        int i, j = this.metadataSize();
        List<Metadatum> remove = new ArrayList<Metadatum>();
        for (i = 0; i < j; ++i) {
            if (this.metadata.get(i).getName().equalsIgnoreCase(meta.getName())) {
                remove.add(this.metadata.get(i));
            }
        }
        this.metadata.removeAll(remove);
        this.metadata.add(meta);
    }

    /**
	 * Changes the relation based on document ID.
	 * @param rel
	 */
    public void changeRelationType(Relation rel) {
        int i, j = this.relations.size();
        for (i = 0; i < j; ++i) {
            if (this.relations.get(i).getDocID().equals(rel.getDocID())) {
                this.relations.set(i, rel);
            }
        }
    }

    /**
	 * Remove all relations that point to the given document ID.
	 * @param docID
	 * @return True if anything was removed
	 */
    public boolean removeRelation(String relDocID) {
        int i, j = this.relations.size();
        boolean removed = false;
        for (i = 0; i < j; ++i) {
            if (this.relations.get(i).getDocID().equals(relDocID)) {
                this.relations.remove(i);
                removed = true;
            }
        }
        return removed;
    }

    /**
	 * Remove all relations that point to the document ID of the given document.
	 * @param rel
	 * @return True if anything was removed.
	 */
    public boolean removeRelation(Relation rel) {
        int i, j = this.relations.size();
        boolean removed = false;
        for (i = 0; i < j; ++i) {
            if (this.relations.get(i).getDocID().equals(rel.getDocID())) {
                this.relations.remove(i);
                removed = true;
            }
        }
        return removed;
    }

    /**
	 * Remove a relation.
	 * Remove all relations that point to the document ID of the given Relation. If
	 * strict is set to true, then this will only remove the relation if it matches
	 * both the document ID AND the Relation type.
	 * @param rel
	 * @param strict If this is TRUE, then the relation will be removed only if the ID
	 *   and the type match.
	 * @return True if anything was removed.
	 */
    public boolean removeRelation(Relation rel, boolean strict) {
        if (strict == false) return this.removeRelation(rel);
        int i, j = this.relations.size();
        boolean removed = false;
        for (i = 0; i < j; ++i) {
            if (this.relations.get(i).getDocID().equals(rel.getDocID()) && this.relations.get(i).getRelationType().equals(rel.getRelationType())) {
                this.relations.remove(i);
                removed = true;
            }
        }
        return removed;
    }

    /**
	 * Remove all metadata from this document.
	 */
    public void clearMetadata() {
        this.metadata.clear();
    }

    /**
	 * Convenience method for quickly adding metadata.
	 * @param name
	 * @param values
	 */
    public void addMetadatum(String name, ArrayList<String> values) {
        this.metadata.add(new Metadatum(name, values));
    }

    /**
	 * Convenience method for quickly adding metdata. Note that this
	 * one also takes a dataType.
	 * @param name
	 * @param values
	 * @param dataType
	 * @see Metdatum#getDataType()
	 */
    public void addMetadatum(String name, ArrayList<String> values, String dataType) {
        Metadatum md = new Metadatum(name, values);
        md.setDataType(dataType);
        this.metadata.add(md);
    }

    /**
	 * Get the DOM for this present object.
	 * @see getDom(Document)
	 * @return
	 * @throws ParserConfigurationException
	 */
    public Document getDOM() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        return this.getDOM(doc);
    }

    /**
	 * Get the object as an XML DOM.
	 * This is one way of converting a RhizomeDocument into
	 * an XML document.
	 * <p>Currently, the XML conversion of the document is
	 * monolithic -- that is, this one method does all of
	 * the traversal of the child objects and creates XML
	 * DOM representations. This is done for two reasons:
	 * (1). I may switch to BetterXML instead of DOM, and
	 * (2). I may make a second encoding besides DOM.</p>
	 * @return The document in the form of a DOM.
	 */
    public Document getDOM(Document doc) {
        Element rhizome_ele = doc.createElementNS(RHIZOME_DOC_XMLNS, RHIZOME_DOC_ROOT);
        rhizome_ele.setAttribute(RHIZOME_DOC_ATTR_DOCID, this.docID);
        doc.appendChild(rhizome_ele);
        this.addRhizomeElements(doc, rhizome_ele);
        return doc;
    }

    /**
	 * Get the DOM, given the document and the parent element.
	 * 
	 * This takes a DOM Document and a starting element, and adds rhizome child nodes
	 * to the given element. The DOM Document is required for create*() methods.
	 * @param doc
	 * @param parent_ele
	 * @return
	 */
    public Document getDOM(Document doc, Element parent_ele) {
        Element rhizome_ele = doc.createElementNS(RHIZOME_DOC_XMLNS, RHIZOME_DOC_ROOT);
        rhizome_ele.setAttribute(RHIZOME_DOC_ATTR_DOCID, this.docID);
        parent_ele.appendChild(rhizome_ele);
        this.addRhizomeElements(doc, rhizome_ele);
        return doc;
    }

    /**
	 * Add the requisite body elements, filled out from object data.
	 * @param doc
	 * @param rhizome_ele
	 */
    private void addRhizomeElements(Document doc, Element rhizome_ele) {
        Element meta_ele = doc.createElement(RHIZOME_DOC_METADATA);
        Element relations_ele = doc.createElement(RHIZOME_DOC_RELATIONS);
        Element data_ele = doc.createElement(RHIZOME_DOC_DATA);
        Element extensions_ele = doc.createElement(RHIZOME_DOC_EXTENSIONS);
        rhizome_ele.appendChild(meta_ele);
        rhizome_ele.appendChild(relations_ele);
        rhizome_ele.appendChild(data_ele);
        rhizome_ele.appendChild(extensions_ele);
        Element md_ele, val_ele;
        Text val_pcd;
        for (Metadatum m : this.getMetadata()) {
            md_ele = doc.createElement(RHIZOME_DOC_METADATUM);
            md_ele.setAttribute(RHIZOME_DOC_ATTR_NAME, m.getName());
            md_ele.setAttribute(RHIZOME_DOC_ATTR_DATATYPE, m.getDataType());
            meta_ele.appendChild(md_ele);
            if (m.hasValues()) {
                for (String txt : m.getValues()) {
                    val_ele = doc.createElement(RHIZOME_DOC_VALUE);
                    val_pcd = doc.createTextNode(txt);
                    val_ele.appendChild(val_pcd);
                    md_ele.appendChild(val_ele);
                }
            }
        }
        if (this.relations.size() > 0) {
            Element rel_ele;
            Text rel_txt;
            for (Relation r : this.getRelations()) {
                rel_ele = doc.createElement(RHIZOME_DOC_RELATION);
                rel_txt = doc.createTextNode(r.getDocID());
                rel_ele.appendChild(rel_txt);
                if (r.hasRelationType()) rel_ele.setAttribute(RHIZOME_DOC_ATTR_RELATIONTYPE, r.getRelationType());
                relations_ele.appendChild(rel_ele);
            }
        }
        if (this.body.getDataLength() > 0) {
            data_ele.setAttribute(RHIZOME_DOC_ATTR_MIMETYPE, this.body.getMimeType());
            data_ele.setAttribute(RHIZOME_DOC_ATTR_INDEX, this.body.isIndexible() ? "true" : "false");
            if (this.body.isXMLParseable()) {
                try {
                    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document tempdoc = db.parse(new java.io.ByteArrayInputStream(this.body.getData().getBytes()));
                    Element rn = tempdoc.getDocumentElement();
                    data_ele.appendChild(doc.importNode(rn, true));
                } catch (Exception e) {
                    CDATASection cdata = doc.createCDATASection(this.body.getData());
                    data_ele.appendChild(cdata);
                }
            } else {
                CDATASection cdata = doc.createCDATASection(this.body.getData());
                data_ele.appendChild(cdata);
            }
        }
        if (this.extensions.size() > 0) {
            Element ext_ele;
            for (Extension ext : this.getExtensions()) {
                ext_ele = doc.createElement(RHIZOME_DOC_EXTENSION);
                ext_ele.setAttribute(RHIZOME_DOC_ATTR_NAME, ext.getName());
                extensions_ele.appendChild(ext_ele);
                Node extroot_ele = doc.importNode(ext.getDOMDocument().getDocumentElement(), true);
                ext_ele.appendChild(extroot_ele);
            }
        }
    }

    /**
	 * Get an XML String.
	 * 
	 * This converts the present object to an XML string.
	 * @return
	 * @throws ParserConfigurationException
	 */
    public String toXML() throws ParserConfigurationException {
        CharArrayWriter output = new CharArrayWriter();
        this.toXML(output);
        return output.toString();
    }

    /**
	 * Transform the object to XML and write it to the given output stream.
	 * @param output
	 * @throws ParserConfigurationException
	 */
    public void toXML(OutputStream output) throws ParserConfigurationException {
        Document d = this.getDOM();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.transform(new DOMSource(d), new StreamResult(output));
        } catch (Exception e) {
            throw new ParserConfigurationException("Could not create Transformer: " + e.getMessage());
        }
    }

    /**
	 * Transform the object to XML and write it to the given Writer.
	 * @param output
	 * @throws ParserConfigurationException
	 */
    public void toXML(Writer output) throws ParserConfigurationException {
        Document d = this.getDOM();
        if (d == null) System.err.println("WARNING: RhizomeDocument.toXML() doc is null.");
        if (output == null) System.err.println("WARNING: RhizomeDocument.toXML() output is null.");
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.transform(new DOMSource(d), new StreamResult(output));
        } catch (Exception e) {
            throw new ParserConfigurationException("Could not create Transformer: " + e.getMessage());
        }
    }

    /**
	 * Get a String representation of the object.
	 * 
	 * This attempts to convert the object to XML, but if that fails, it uses
	 * the generic toString() method from Object.
	 * @see java.lang.Object
	 */
    public String toString() {
        try {
            return this.toXML();
        } catch (ParserConfigurationException pce) {
            return super.toString() + " (Parser Not Found)";
        }
    }

    /**
	 * Use toString() instead.
	 * @deprecated object will not be a {@link Presentable} in the future.
	 */
    public String toPresentation() {
        return this.toString();
    }

    /**
	 * Use toString() instead.
	 * @deprecated object will not be a {@link Presentable} in the future.
	 */
    public java.util.List<String> toPresentationList() {
        ArrayList<String> a = new ArrayList<String>();
        a.add(this.toString());
        return a;
    }
}
