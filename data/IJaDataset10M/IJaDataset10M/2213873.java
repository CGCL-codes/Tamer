package net.sf.xpontus.plugins.codecompletion.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Schema based code completion provider
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class XSDCompletionParser implements ICompletionParser {

    protected static final String CONTINUE_AFTER_FATAL_ERROR = Constants.XERCES_FEATURE_PREFIX + Constants.CONTINUE_AFTER_FATAL_ERROR_FEATURE;

    private static final Log LOG = LogFactory.getLog(XSDCompletionParser.class);

    private List<TagInfo> tagList = new ArrayList<TagInfo>();

    private Map nsTagListMap = new HashMap();

    /** Creates a new instance of XMLSchemaCompletionParser */
    public XSDCompletionParser() {
    }

    public void init(List tagList, Map nsTagListMap) {
        this.tagList = tagList;
        this.nsTagListMap = nsTagListMap;
    }

    public void updateCompletionInfo(String pubid, String uri, Reader in) {
        try {
            String[] schemas = uri.split("[ \t\n\r]+");
            for (String schema : schemas) {
                if (schema.indexOf(".xsd") != -1) {
                    String base = null;
                    if (schema.indexOf("\\") == -1) {
                        base = schema.substring(0, schema.lastIndexOf("/"));
                    } else {
                        base = schema.substring(0, schema.lastIndexOf("\\"));
                    }
                    final String baseid = base;
                    Reader m_reader = new InputStreamReader(new URL(schema).openStream());
                    XMLSchemaLoader xsLoader = new XMLSchemaLoader();
                    xsLoader.setFeature(CONTINUE_AFTER_FATAL_ERROR, true);
                    xsLoader.setEntityResolver(new XSDEntityResolver(baseid));
                    SchemaGrammar grammer = (SchemaGrammar) xsLoader.loadGrammar(new XMLInputSource(null, null, null, m_reader, null));
                    String targetNS = grammer.getTargetNamespace();
                    nsTagListMap.put(targetNS, new ArrayList());
                    List<TagInfo> tagList = (List<TagInfo>) nsTagListMap.get(targetNS);
                    XSNamedMap map = grammer.getComponents(XSConstants.ELEMENT_DECLARATION);
                    for (int i = 0; i < map.getLength(); i++) {
                        XSElementDeclaration element = (XSElementDeclaration) map.item(i);
                        parseXSDElement(tagList, element);
                    }
                }
            }
        } catch (Exception ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(ex.getMessage(), ex);
            }
        }
    }

    private void parseXSDElement(List<TagInfo> tagList, XSElementDeclaration element) {
        TagInfo tagInfo = new TagInfo(element.getName(), true);
        if (!tagList.contains(tagInfo)) {
            tagList.add(tagInfo);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Adding :" + tagInfo.getTagName());
            }
        }
        XSTypeDefinition type = element.getTypeDefinition();
        if (type instanceof XSComplexTypeDefinition) {
            XSParticle particle = ((XSComplexTypeDefinition) type).getParticle();
            if (particle != null) {
                XSTerm term = particle.getTerm();
                if (term instanceof XSElementDeclaration) {
                    parseXSDElement(tagList, (XSElementDeclaration) term);
                    XSElementDeclaration m_declaration = (XSElementDeclaration) term;
                    tagInfo.addChildTagName(m_declaration.getName());
                }
                if (term instanceof XSModelGroup) {
                    parseXSModelGroup(tagInfo, tagList, (XSModelGroup) term);
                }
            }
            XSObjectList attrs = ((XSComplexTypeDefinition) type).getAttributeUses();
            for (int i = 0; i < attrs.getLength(); i++) {
                XSAttributeUse attrUse = (XSAttributeUse) attrs.item(i);
                XSAttributeDeclaration attr = attrUse.getAttrDeclaration();
                AttributeInfo attrInfo = new AttributeInfo(attr.getName(), true, AttributeInfo.NONE, attrUse.getRequired());
                tagInfo.addAttributeInfo(attrInfo);
            }
        }
    }

    private void parseXSModelGroup(TagInfo tagInfo, List<TagInfo> tagList, XSModelGroup term) {
        XSObjectList list = ((XSModelGroup) term).getParticles();
        for (int i = 0; i < list.getLength(); i++) {
            XSObject obj = list.item(i);
            if (obj instanceof XSParticle) {
                XSTerm term2 = ((XSParticle) obj).getTerm();
                if (term2 instanceof XSElementDeclaration) {
                    parseXSDElement(tagList, (XSElementDeclaration) term2);
                    tagInfo.addChildTagName(((XSElementDeclaration) term2).getName());
                } else if (term2 instanceof XSModelGroup) {
                    parseXSModelGroup(tagInfo, tagList, (XSModelGroup) term2);
                }
            }
        }
    }
}
