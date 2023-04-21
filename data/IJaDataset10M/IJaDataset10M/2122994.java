package org.foo.didl;

import info.repo.didl.ComponentType;
import info.repo.didl.DIDLType;
import info.repo.didl.DescriptorType;
import info.repo.didl.ResourceType;
import info.repo.didl.impl.content.ByteArray;
import info.repo.didl.impl.tools.Identifier;
import java.net.URI;
import org.foo.didl.content.DII;
import org.foo.didl.content.MyContent;

/**
 * MyComplexComponent provides a bit more complex didl component implementation.  
 * As in the simple component example, the create() and parse() are used to construct 
 * and deconstruct a component and the structure defined in the create() method must 
 * be in synch with that of the parse() method.
 * <p/>
 * This example introduces a new content type (DII) and component typing.  Typically
 * you'll want to handle different resource types differently.  The COMPONENT_TYPE enum
 * allows us to uniquely handle each component.  For example, for MODSXML I only want to
 * store a resource by-value, placing in-line xml in the didl, while for MARCXML I want 
 * to have a resource by-value and another resource by-reference.  In the create() method,
 * you'll notice COMPONENT_TYPE is used to determine the structure of the component.
 * 
 * This example will produce a Component similar to the structure outlined below:
 * <br/> 
 *     <Component><br/>
 *       <Descriptor><Statement><DII>...</DII></Statement></Desciptor><br/>
 *       <Descriptor><Statement><content>...</content></Statement></Desciptor><br/>
 *       <Resource mimeType="..." ref="..."/><br/> 
 *       <Resource mimeType="...">...</Resource><br/>
 *     </Component><br/>
 * 
 * @author Ryan Chute <rchute@lanl.gov>
 * 
 */
public class MyComplexComponent {

    /** Default Copyright Statement */
    public static final String DEFAULT_COPYRIGHT = "Copyright (c) 2004-2006, Some Organization";

    /** Default Usage Rights Statement */
    public static final String DEFAULT_USAGE = "Contact John Doe for usage rights.";

    /** Enumeration of valid component types */
    public enum COMPONENT_TYPE {

        MODSXML, MARCXML, RESOURCE
    }

    ;

    private COMPONENT_TYPE type;

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
    public MyComplexComponent() {
    }

    /**
     * Constructor to create a new component object
     * 
     * @param type
     *            type of component
     * @param id
     *            value of the component id
     * @param mimetype
     *            the value of the mimetype of the resource
     * @param resourceUri
     *            reference to the resolvable resource
     * @param content
     *            content of the resource, applies to resources stored by-value
     */
    public MyComplexComponent(COMPONENT_TYPE type, String id, String mimetype, URI resource_uri, String content) {
        this(type, id, mimetype, resource_uri, content, DEFAULT_COPYRIGHT, DEFAULT_USAGE);
    }

    /**
     * Constructor to create a new component object
     * 
     * @param type
     *            type of component
     * @param id
     *            value of the component id
     * @param mimetype
     *            the value of the mimetype of the resource
     * @param resourceUri
     *            reference to the resolvable resource
     * @param content
     *            content of the resource, applies to resources stored by-value
     * @param copyright
     *            statement indicating copyright of component
     * @param usage
     *            statement indicating allowed scope of usage
     */
    public MyComplexComponent(COMPONENT_TYPE type, String id, String mimetype, URI resource_uri, String content, String copyright, String usage) {
        this.setType(type);
        this.setId(id);
        this.setMimetype(mimetype);
        this.setResourceUri(resource_uri);
        this.setContent(content);
        this.setCopyright(copyright);
        this.setUsage(usage);
    }

    /**
     * Creates a new component of the structure defined in this implementation
     * @param didl
     *             DIDLType to which the component will be added
     * @return
     *             a ComponentType of the structure defined in the method
     * @throws MyDidlException
     */
    public ComponentType create(DIDLType didl) throws Exception {
        ComponentType com = didl.newComponent();
        com.setId(Identifier.createXMLIdentifier());
        com.addDescriptor(didl.newDescriptor()).addStatement(MyComplexDidlHelper.newXMLStatement(didl, new DII(DII.IDENTIFIER, getId())));
        MyContent mc = new MyContent();
        mc.setId(id);
        if (resourceURI != null) mc.setResourceUri(resourceURI.toURL().toString());
        if (copyright != null) mc.setCopyright(copyright);
        if (usage != null) mc.setUsage(usage);
        com.addDescriptor(didl.newDescriptor()).addStatement(MyComplexDidlHelper.newXMLStatement(didl, mc));
        if ((getType() == COMPONENT_TYPE.RESOURCE) && (getResourceUri() != null)) {
            ResourceType resource = didl.newResource();
            resource.setMimeType(getMimetype());
            resource.setRef(getResourceUri());
            resource.setContent(new ByteArray(getResourceUri().toURL()));
            com.addResource(resource);
        }
        if ((getType() == COMPONENT_TYPE.MODSXML) && (getContent() != null)) {
            ResourceType resource = didl.newResource();
            resource.setMimeType(getMimetype());
            resource.setContent(new ByteArray(getContent()));
            com.addResource(resource);
        }
        if ((getType() == COMPONENT_TYPE.MARCXML) && (getContent() != null)) {
            ResourceType byvalue = didl.newResource();
            byvalue.setMimeType(getMimetype());
            byvalue.setContent(new ByteArray(getContent()));
            com.addResource(byvalue);
            ResourceType byref = didl.newResource();
            byref.setMimeType(getMimetype());
            byref.setRef(getResourceUri());
            byref.setContent(new ByteArray(getResourceUri().toURL()));
            com.addResource(byref);
        }
        return com;
    }

    /**
     * Parses a generic component type to create a MySimpleComponent object
     * @param type
     *             the type of the component to be parsed
     * @param com
     *             a ComponentType obtained from a parsed didl
     * @return
     *             constructed MyComplexComponent object
     * @throws Exception
     */
    public static MyComplexComponent parse(COMPONENT_TYPE type, ComponentType com) throws Exception {
        MyComplexComponent mc = new MyComplexComponent();
        mc.setType(type);
        for (DescriptorType desc : com.getDescriptors()) {
            Object content = desc.getStatements().get(0).getContent();
            if (DII.class.isInstance(content)) {
                mc.setId(((DII) (content)).getValue());
            } else if (MyContent.class.isInstance(content)) {
                MyContent ct = (MyContent) content;
                mc.setId(ct.getId());
                if (ct.getResourceUri() != null) mc.setResourceUri(new URI(ct.getResourceUri()));
                mc.setCopyright(ct.getCopyright());
                mc.setUsage(ct.getUsage());
            }
        }
        for (ResourceType r : com.getResources()) {
            if (r.getRef() != null) mc.setResourceUri(r.getRef());
            mc.setMimetype(r.getMimeType());
            if (r.getRef() == null && r.getContent() != null) {
                ByteArray ba = (ByteArray) (r.getContent());
                mc.setContent(ba.getString());
            }
        }
        return mc;
    }

    /** Gets component type of instance */
    public COMPONENT_TYPE getType() {
        return type;
    }

    /** Sets the component type for current instance */
    public void setType(COMPONENT_TYPE type) {
        this.type = type;
    }

    /** Gets the content type identifier */
    public String getId() {
        return id;
    }

    /** Sets the content type identifier */
    public void setId(String id) {
        this.id = id;
    }

    /** Gets the content type mimetype */
    public String getMimetype() {
        return mimetype;
    }

    /** Sets the content type mimetype */
    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    /** Gets the content type resource uri */
    public URI getResourceUri() {
        return resourceURI;
    }

    /** Sets the content type resource uri */
    public void setResourceUri(URI resource_uri) {
        this.resourceURI = resource_uri;
    }

    /** Gets the content of the resource */
    public String getContent() {
        return content;
    }

    /** Sets the content of the resource */
    public void setContent(String content) {
        this.content = content;
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
}
