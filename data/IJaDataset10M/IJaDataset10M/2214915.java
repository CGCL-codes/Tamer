package org.foo.didl;

import info.repo.didl.ComponentType;
import info.repo.didl.DIDLType;
import info.repo.didl.DescriptorType;
import info.repo.didl.ResourceType;
import info.repo.didl.StatementType;
import info.repo.didl.impl.content.ByteArray;
import info.repo.didl.impl.tools.Identifier;
import java.net.MalformedURLException;
import java.net.URI;
import org.foo.didl.content.MyContent;

/**
 * <code>MySimpleComponent</code> provides a simple didl component implementation.  
 * The create() and parse() are used to construct and deconstruct a component.  
 * The structure defined in the create() method must be in synch with that of the 
 * parse() method.
 * <p/>
 * This example will produce a Component similar to the structure outlined below:
 * <br/> 
 *     <Component><br/> 
 *       <Descriptor><Statement><content>...</content></Statement></Desciptor><br/>
 *       <Resource mimeType="..." ref="..."/><br/> 
 *       <Resource mimeType="...">...</Resource><br/>
 *     </Component><br/>
 * 
 * @author Ryan Chute <rchute@lanl.gov>
 * 
 */
public class MySimpleComponent {

    private String id;

    private String mimetype;

    private URI resourceURI;

    private String content;

    private String copyright;

    private String usage;

    /**
     * Constructor to create an empty component object
     *
     */
    public MySimpleComponent() {
    }

    /**
     * Constructor to create a new component object
     * 
     * @param id
     *            value of the component id
     * @param mimetype
     *            the value of the mimetype of the resource
     * @param copyright
     *            statement indicating copyright of component
     * @param usage
     *            statement indicating allowed scope of usage
     * @param resourceUri
     *            reference to the resolvable resource
     * @param content
     *            content of the resource, applies to resources stored by-value
     */
    public MySimpleComponent(String id, String mimetype, String copyright, String usage, URI resourceUri, String content) {
        this.setId(id);
        this.setMimetype(mimetype);
        this.setResourceURI(resourceUri);
        this.setCopyright(copyright);
        this.setUsage(usage);
        this.setContent(content);
    }

    /**
     * Creates a new component of the structure defined in this implementation
     * @param didl
     *             DIDLType to which the component will be added
     * @return
     *             a ComponentType of the structure defined in the method
     * @throws MyDidlException
     */
    public ComponentType create(DIDLType didl) throws MyDidlException {
        ComponentType com = didl.newComponent();
        com.setId(Identifier.createXMLIdentifier());
        MyContent mc = new MyContent();
        mc.setId(id);
        if (resourceURI != null) {
            try {
                mc.setResourceUri(resourceURI.toURL().toString());
            } catch (MalformedURLException e) {
                throw new MyDidlException(e.getMessage(), e);
            }
        }
        mc.setCopyright(copyright);
        mc.setUsage(usage);
        StatementType stmt = didl.newStatement();
        stmt.setMimeType("application/xml; charset=utf-8");
        stmt.setContent(mc);
        com.addDescriptor(didl.newDescriptor()).addStatement(stmt);
        if (getResourceURI() != null) {
            ResourceType resource = didl.newResource();
            resource.setMimeType(getMimetype());
            resource.setRef(getResourceURI());
            try {
                resource.setContent(new ByteArray(getResourceURI().toURL()));
            } catch (MalformedURLException e) {
                throw new MyDidlException(e.getMessage(), e);
            }
            com.addResource(resource);
        }
        if (getContent() != null) {
            ResourceType resource = didl.newResource();
            resource.setMimeType(getMimetype());
            resource.setContent(new ByteArray(getContent()));
            com.addResource(resource);
        }
        return com;
    }

    /**
     * Parses a generic component type to create a MySimpleComponent object
     * @param com
     *             a ComponentType obtained from a parsed didl
     * @return
     *             constructed MySimpleComponent object
     * @throws Exception
     */
    public static MySimpleComponent parse(ComponentType com) throws Exception {
        MySimpleComponent msc = new MySimpleComponent();
        for (DescriptorType desc : com.getDescriptors()) {
            Object content = desc.getStatements().get(0).getContent();
            if (MyContent.class.isInstance(content)) {
                MyContent mc = (MyContent) content;
                msc.setId(mc.getId());
                msc.setCopyright(mc.getCopyright());
                msc.setUsage(mc.getUsage());
            }
        }
        for (ResourceType r : com.getResources()) {
            msc.setResourceURI(r.getRef());
            msc.setMimetype(r.getMimeType());
            if (msc.getResourceURI() == null) {
                ByteArray ba = (ByteArray) (r.getContent());
                msc.setContent(ba.getString());
            }
        }
        return msc;
    }

    /** Gets the content type identifier */
    public String getId() {
        return id;
    }

    /** Sets the content type identifier */
    public void setId(String id) {
        this.id = id;
    }

    /** Gets the content type copyright notice */
    public String getCopyright() {
        return copyright;
    }

    /** Sets the content type copyright notice */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /** Gets the content type usage notice */
    public String getUsage() {
        return usage;
    }

    /** Sets the content type usage notice */
    public void setUsage(String usage) {
        this.usage = usage;
    }

    /** Gets the content type resource uri */
    public URI getResourceURI() {
        return resourceURI;
    }

    /** Sets the content type resource uri */
    public void setResourceURI(URI resourceURI) {
        this.resourceURI = resourceURI;
    }

    /** Gets the content type mimetype */
    public String getMimetype() {
        return mimetype;
    }

    /** Sets the content type mimetype */
    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    /** Gets the content of the resource */
    public String getContent() {
        return content;
    }

    /** Sets the content of the resource */
    public void setContent(String content) {
        this.content = content;
    }
}
