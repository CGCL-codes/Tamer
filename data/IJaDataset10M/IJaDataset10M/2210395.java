package net.sf.xpontus.codecompletion.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.impl.dtd.XMLDTDLoader;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogManager;
import org.xml.sax.InputSource;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author Yves Zoundi
 */
public class DTDCompletionParser implements ICompletionParser {

    private Log logger = LogFactory.getLog(DTDCompletionParser.class);

    private List tagList = new ArrayList();

    private Map nsTagListMap = new HashMap();

    /** Creates a new instance of DTDCompletionParser */
    public DTDCompletionParser() {
    }

    public void init(List tagList, Map nsTagListMap) {
        this.tagList = tagList;
        this.nsTagListMap = nsTagListMap;
    }

    protected TagInfo getTagInfo(String name) {
        TagInfo info = null;
        for (int i = 0; i < tagList.size(); i++) {
            info = (TagInfo) tagList.get(i);
            if (info.getTagName().equals(name)) {
                break;
            }
        }
        if (info == null) {
            info = new TagInfo(name, true);
            tagList.add(info);
        }
        return info;
    }

    public void updateCompletionInfo(String pubid, String uri, Reader in) {
        try {
            XMLDTDLoader loader = new XMLDTDLoader() {

                public void element(java.lang.String elementName, org.apache.xerces.xni.Augmentations augs) throws org.apache.xerces.xni.XNIException {
                    super.element(elementName, augs);
                    TagInfo tagInfo = new TagInfo(elementName, true);
                    if (!tagList.contains(tagInfo)) {
                        tagList.add(tagInfo);
                    }
                }

                public void attributeDecl(java.lang.String elementName, java.lang.String attributeName, java.lang.String type, java.lang.String[] enumeration, java.lang.String defaultType, org.apache.xerces.xni.XMLString defaultValue, org.apache.xerces.xni.XMLString nonNormalizedDefaultValue, org.apache.xerces.xni.Augmentations augs) throws org.apache.xerces.xni.XNIException {
                    TagInfo tagInfo = getTagInfo(elementName);
                    AttributeInfo attrInfo = new AttributeInfo(attributeName, true, AttributeInfo.NONE, true);
                    tagInfo.addAttributeInfo(attrInfo);
                    if (enumeration != null) {
                        for (int i = 0; i < enumeration.length; i++) {
                            attrInfo.addValue(enumeration[i]);
                        }
                    } else {
                        if (defaultValue != null) {
                            attrInfo.addValue(defaultValue.toString());
                        }
                    }
                }

                public void externalEntityDecl(java.lang.String name, org.apache.xerces.xni.XMLResourceIdentifier identifier, org.apache.xerces.xni.Augmentations augs) throws org.apache.xerces.xni.XNIException {
                    super.externalEntityDecl(name, identifier, augs);
                }
            };
            FileObject fo = VFS.getManager().resolveFile(uri);
            DTDGrammar grammar = (DTDGrammar) loader.loadGrammar((new XMLInputSource(pubid, uri, fo.getURL().toExternalForm(), in, null)));
        } catch (Exception ex) {
            ex.printStackTrace();
            ;
        }
    }
}
