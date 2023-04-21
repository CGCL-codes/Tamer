package net.sf.vex.dom;

import org.xml.sax.ext.DeclHandler;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.SAXException;

public class DocumentBuilder2 extends DocumentBuilder implements DeclHandler {

    public DocumentBuilder2(IWhitespacePolicyFactory policyFactory) {
        super(policyFactory);
    }

    protected Map<String, Entity> entities = new HashMap<String, Entity>();

    /** 
 * @return Returns the entities. 
 */
    protected Map getEntities() {
        return entities;
    }

    private String systemId;

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    private Document2 doc2;

    public Document2 getDocument() {
        return doc2;
    }

    protected boolean headless = true;

    public void elementDecl(String name, String model) throws SAXException {
        if (headless) headless = false;
    }

    public void attributeDecl(String eName, String aName, String type, String mode, String value) throws SAXException {
        if (headless) headless = false;
    }

    public void internalEntityDecl(String name, String value) throws SAXException {
        if (headless) headless = false;
        String systemId = locator.getSystemId().replace("%20", " ");
        if (systemId.equals(this.systemId)) {
            entities.put(name, new Entity(name, value));
        }
    }

    public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
        if (headless) headless = false;
        if (!name.startsWith("%")) {
            entities.put(name, new Entity(name, publicId, systemId));
        }
    }

    public void endDocument() {
        this.doc2 = new Document2(this.content, this.rootElement);
        this.doc2.setPublicID(this.dtdPublicID);
        this.doc2.setSystemID(this.dtdSystemID);
        this.doc2.setEntities(this.entities);
        this.doc2.setHeadless(this.headless);
        this.rootElement.setDocument(this.doc2);
        for (Entity entity : entities.values()) {
            for (EntityReference entityReference : entity.getEntityReferences()) this.doc2.addDocumentListener(entityReference.getDocumentListener());
        }
    }

    public void endEntity(String name) {
        super.endEntity(name);
        if (entities.get(name) != null) {
            this.appendChars(false);
            StackEntry entry = (StackEntry) stack.pop();
            EntityReference entityReference = (EntityReference) entry.element;
            this.content.insertString(content.getLength(), "\0");
            entityReference.setContent(this.content, entry.offset, content.getLength() - 1);
        }
    }

    public void startEntity(String name) throws SAXException {
        super.startEntity(name);
        Entity entity = entities.get(name);
        if (entity != null) {
            EntityReference entityReference = new EntityReference(name, entity);
            entity.addEntityReference(entityReference);
            this.appendChars(false);
            ((StackEntry) stack.peek()).element.addChild(entityReference);
            stack.push(new StackEntry(entityReference, content.getLength(), false));
            this.content.insertString(content.getLength(), "\0");
        }
    }
}
