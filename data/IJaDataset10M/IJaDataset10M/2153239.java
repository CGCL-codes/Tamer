package org.xdoclet.plugin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.apache.commons.collections.CollectionUtils;
import org.generama.JellyTemplateEngine;
import org.generama.QDoxCapableMetadataProvider;
import org.generama.WriterMapper;
import org.generama.defaults.QDoxPlugin;
import org.generama.defaults.XMLOutputValidator;
import org.xdoclet.plugin.web.qtags.TagLibrary;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;

/**
 * @phase process-resources
 * @goal xdoclet
 * @author Gr&eacute;gory Joseph
 */
public class TaglibPlugin extends QDoxPlugin {

    public TaglibPlugin(JellyTemplateEngine jellyTemplateEngine, QDoxCapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(jellyTemplateEngine, metadataProvider, writerMapper);
        includeListeners = true;
        setMultioutput(false);
        setFilereplace("taglib.tld");
        setTaglibversion("1.0");
        setJspversion("1.2");
        setShortname("taglib");
        setOutputValidator(new XMLOutputValidator(WebUtils.DTDs));
        new TagLibrary(metadataProvider);
    }

    public boolean needsDoctype() {
        return getJspversion().startsWith("1.");
    }

    public boolean needsSchema() {
        return getJspversion().startsWith("2.");
    }

    public Collection getTags() {
        ArrayList tags = new ArrayList();
        org.apache.commons.collections.Predicate predicate = new ClassTagPredicate((String[]) tagInterfaces.get(getJspversion()), "jsp.tag");
        if (allClasses == null) allClasses = metadataProvider.getMetadata();
        CollectionUtils.select(allClasses, predicate, tags);
        return tags;
    }

    /**
     * This method returns a collection of JavaMethod from a given class, which have a jsp.attribute tag.
     * @param clazz the java class to inspect.
     * @param attributeMethods a collection of attributes methods
     * @return a collection of JavaMethod from a given class, which have a jsp.attribute tag.
     */
    public Collection getAttributeMethods(JavaClass clazz) {
        Collection attributeMethods = new ArrayList();
        getAllAttributeMethods(clazz, attributeMethods);
        return attributeMethods;
    }

    /**
     * This method returns a collection of JavaMethod from a given class, which have a jsp.attribute tag.
     * @param clazz the java class to inspect.
     * @param attributeMethods a collection of attributes methods
     * @return a collection of JavaMethod from a given class, which have a jsp.attribute tag.
     */
    private Collection getAllAttributeMethods(JavaClass clazz, Collection attributeMethods) {
        JavaMethod[] meths = clazz.getMethods();
        for (int i = 0; i < meths.length; i++) {
            DocletTag tag = meths[i].getTagByName("jsp.attribute");
            if ((tag != null) && !attributeMethods.contains(meths[i])) {
                attributeMethods.add(meths[i]);
            }
        }
        if (!(clazz.getSuperClass().getJavaClass().getName().equals("Object"))) {
            getAllAttributeMethods(clazz.getSuperClass().getJavaClass(), attributeMethods);
        }
        return attributeMethods;
    }

    public WebUtils getWebUtils() throws ClassNotFoundException {
        String servletVersion = (String) servletVersions.get(jspversion);
        if (webUtils == null) webUtils = new WebUtils(servletVersion);
        return webUtils;
    }

    public String getIncludelisteners() {
        return includeListeners ? "true" : "false";
    }

    public void setIncludelisteners(String includeListenersStr) {
        includeListeners = includeListenersStr.equalsIgnoreCase("true");
    }

    public String getJspversion() {
        return jspversion;
    }

    public void setJspversion(String jspversion) {
        this.jspversion = jspversion;
    }

    public String getTaglibversion() {
        return taglibversion;
    }

    public void setTaglibversion(String taglibversion) {
        this.taglibversion = taglibversion;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getSmallicon() {
        return smallicon;
    }

    public void setSmallicon(String smallicon) {
        this.smallicon = smallicon;
    }

    public String getLargeicon() {
        return largeicon;
    }

    public void setLargeicon(String largeicon) {
        this.largeicon = largeicon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public String getPublicId() {
        return (String) publicIDs.get(getJspversion());
    }

    public String getSystemId() {
        return (String) systemIDs.get(getJspversion());
    }

    public String getSchemaXmlNs() {
        return needsSchema() ? "http://java.sun.com/xml/ns/j2ee" : null;
    }

    public String getSchemaXmlNsXsi() {
        return needsSchema() ? "http://www.w3.org/2001/XMLSchema-instance" : null;
    }

    public String getSchemaXsiSchemaLocation() {
        return needsSchema() ? "http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd" : null;
    }

    public boolean isMinimumJspVersion(String maximalVersion) {
        return jspversion.compareTo(maximalVersion) >= 0;
    }

    public boolean isMaximumJspVersion(String maximalVersion) {
        return jspversion.compareTo(maximalVersion) <= 0;
    }

    private static final String DEFAULT_TAGLIBVERSION = "1.0";

    private static final String DEFAULT_JSPVERSION = "1.2";

    private static final String DEFAULT_SHORTNAME = "taglib";

    private static final HashMap servletVersions;

    private static final HashMap publicIDs;

    private static final HashMap systemIDs;

    private static final HashMap tagInterfaces;

    private Collection allClasses;

    private WebUtils webUtils;

    private String jspversion;

    private String taglibversion;

    private String shortname;

    private String uri;

    private String displayname;

    private String smallicon;

    private String largeicon;

    private String description;

    private String validator;

    private boolean includeListeners;

    static {
        servletVersions = new HashMap();
        servletVersions.put("1.1", "2.2");
        servletVersions.put("1.2", "2.3");
        servletVersions.put("2.0", "2.4");
        publicIDs = new HashMap();
        systemIDs = new HashMap();
        publicIDs.put("1.1", "-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.1//EN");
        publicIDs.put("1.2", "-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN");
        systemIDs.put("1.1", "http://java.sun.com/j2ee/dtds/web-jsptaglibrary_1_1.dtd");
        systemIDs.put("1.2", "http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd");
        tagInterfaces = new HashMap();
        tagInterfaces.put("1.1", new String[] { "javax.servlet.jsp.tagext.Tag" });
        tagInterfaces.put("1.2", new String[] { "javax.servlet.jsp.tagext.Tag" });
        tagInterfaces.put("2.0", new String[] { "javax.servlet.jsp.tagext.Tag", "javax.servlet.jsp.tagext.JspTag" });
    }
}
