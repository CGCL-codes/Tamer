package com.siemens.ct.exi.attributes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import com.siemens.ct.exi.Constants;
import com.siemens.ct.exi.EXIFactory;
import com.siemens.ct.exi.EncodingOptions;
import com.siemens.ct.exi.FidelityOptions;
import com.siemens.ct.exi.core.container.NamespaceDeclaration;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.8
 */
public abstract class AbstractAttributeList implements AttributeList {

    public static final int XMLNS_PFX_START = XMLConstants.XMLNS_ATTRIBUTE.length() + 1;

    protected boolean preserveSchemaLocation;

    protected boolean preservePrefixes;

    protected boolean hasXsiType;

    protected String xsiTypeRaw;

    protected String xsiTypePrefix;

    protected boolean hasXsiNil;

    protected String xsiNil;

    protected String xsiNilPrefix;

    protected List<String> attributeURI;

    protected List<String> attributeLocalName;

    protected List<String> attributeValue;

    protected List<String> attributePrefix;

    protected List<NamespaceDeclaration> nsDecls;

    public AbstractAttributeList(EXIFactory exiFactory) {
        preserveSchemaLocation = exiFactory.getEncodingOptions().isOptionEnabled(EncodingOptions.INCLUDE_XSI_SCHEMALOCATION);
        preservePrefixes = exiFactory.getFidelityOptions().isFidelityEnabled(FidelityOptions.FEATURE_PREFIX);
        attributeURI = new ArrayList<String>();
        attributeLocalName = new ArrayList<String>();
        attributeValue = new ArrayList<String>();
        attributePrefix = new ArrayList<String>();
        nsDecls = new ArrayList<NamespaceDeclaration>();
    }

    public void clear() {
        hasXsiType = false;
        hasXsiNil = false;
        attributeURI.clear();
        attributeLocalName.clear();
        attributeValue.clear();
        attributePrefix.clear();
        xsiTypeRaw = null;
        nsDecls.clear();
    }

    public boolean hasXsiType() {
        return hasXsiType;
    }

    public String getXsiTypeRaw() {
        return xsiTypeRaw;
    }

    public String getXsiTypePrefix() {
        return this.xsiTypePrefix;
    }

    public boolean hasXsiNil() {
        return hasXsiNil;
    }

    public String getXsiNil() {
        return xsiNil;
    }

    public String getXsiNilPrefix() {
        return this.xsiNilPrefix;
    }

    public int getNumberOfAttributes() {
        return attributeURI.size();
    }

    public String getAttributeURI(int index) {
        return attributeURI.get(index);
    }

    public String getAttributeLocalName(int index) {
        return attributeLocalName.get(index);
    }

    public String getAttributeValue(int index) {
        return attributeValue.get(index);
    }

    public String getAttributePrefix(int index) {
        return attributePrefix.get(index);
    }

    private void setXsiType(String rawType, String xsiPrefix) {
        this.hasXsiType = true;
        this.xsiTypeRaw = rawType;
        this.xsiTypePrefix = xsiPrefix;
    }

    private void setXsiNil(String rawNil, String xsiPrefix) {
        this.hasXsiNil = true;
        this.xsiNil = rawNil;
        this.xsiNilPrefix = xsiPrefix;
    }

    public void addNamespaceDeclaration(String uri, String pfx) {
        this.nsDecls.add(new NamespaceDeclaration(uri, pfx));
    }

    public int getNumberOfNamespaceDeclarations() {
        return nsDecls.size();
    }

    public NamespaceDeclaration getNamespaceDeclaration(int index) {
        assert (index >= 0 && index < nsDecls.size());
        return nsDecls.get(index);
    }

    public void addAttribute(String uri, String localName, String pfx, String value) {
        if (uri.equals(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI)) {
            if (localName.equals(Constants.XSI_TYPE)) {
                setXsiType(value, pfx);
            } else if (localName.equals(Constants.XSI_NIL)) {
                setXsiNil(value, pfx);
            } else if ((localName.equals(Constants.XSI_SCHEMA_LOCATION) || localName.equals(Constants.XSI_NONAMESPACE_SCHEMA_LOCATION)) && !preserveSchemaLocation) {
            } else {
                insertAttribute(uri, localName, pfx, value);
            }
        } else {
            insertAttribute(uri, localName, pfx, value);
        }
    }

    protected abstract void insertAttribute(String uri, String localName, String pfx, String value);
}
