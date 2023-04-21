package olr.content;

import java.util.Iterator;
import java.util.List;
import olr.rdf.Attribute;
import olr.rdf.Definitions;
import olr.rdf.Resource;
import olr.rdf.Tools;
import olr.rdf.util.SortedStringList;
import olr.statementpool.OlrStatementPool;
import olr.toolbar.ToolbarSession;

/** 
 * @version $Id: ContentModel.java,v 1.14 2005/06/03 11:43:21 hdhraief Exp $
 */
public final class ContentModel {

    private final OlrStatementPool statementPool;

    private final ToolbarSession toolbarSession;

    protected String aboutURI;

    protected List attributes;

    private SortedStringList availableProperties;

    /** 
     * Initializes an instance of this class.
     * @param aboutURI
     * @param statementPool
     * @param toolbarSession
     */
    public ContentModel(String aboutURI, OlrStatementPool statementPool, ToolbarSession toolbarSession) {
        this.statementPool = statementPool;
        this.toolbarSession = toolbarSession;
        attributes = new SortedContentAttributeList();
        availableProperties = new SortedStringList();
        aboutURI = Tools.correctURI(aboutURI);
        this.aboutURI = aboutURI;
        List attribs = getStatementPool().getAttributesAbout(aboutURI);
        for (Iterator it = attribs.iterator(); it.hasNext(); attributes.add(new ContentAttribute((Attribute) it.next(), statementPool, toolbarSession))) ;
        availableProperties.addAll(getStatementPool().getAvailableProperties4Resource(aboutURI));
        availableProperties.remove(Definitions.RDF_TYPE);
    }

    public String getAboutURI() {
        return aboutURI;
    }

    public String getTitle() {
        Resource resource = getStatementPool().getResource(aboutURI);
        if (resource != null) {
            return resource.getTitle();
        } else return "<untitled>";
    }

    public String getType() {
        Resource resource = getStatementPool().getResource(aboutURI);
        if (resource != null) return resource.getType(); else return "";
    }

    public List getAvailableProperties() {
        return availableProperties;
    }

    public final List getContentAttributes() {
        return attributes;
    }

    public void addProperty(String property) {
        Attribute attribute = new Attribute(property, "");
        attributes.add(new ContentAttribute(attribute, getStatementPool(), getToolbarSession()));
        getStatementPool().addAttributeAbout(getAboutURI(), attribute);
    }

    public void removeAttribute(int index) {
        if (index >= 0 && index < attributes.size()) {
            getStatementPool().removeAttributeAbout(getAboutURI(), ((ContentAttribute) attributes.get(index)).getAttribute());
            attributes.remove(index);
        }
    }

    public OlrStatementPool getStatementPool() {
        return this.statementPool;
    }

    /**
     * @return Returns the toolbarSession.
     */
    private ToolbarSession getToolbarSession() {
        return toolbarSession;
    }
}
