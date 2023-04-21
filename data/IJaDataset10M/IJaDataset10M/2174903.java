package com.afp.medya.importer.implementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Hashtable;
import org.afp.util.jxpath.JXPathHelper;
import org.afp.util.xml.XMLHelper;
import org.w3c.dom.Document;
import com.afp.medya.importer.Constantes;
import com.afp.medya.importer.interfaces.IProcessFilter;
import com.afp.medya.importer.process.Taskes;
import com.afp.medya.importer.transform.XSLTTransformer;
import com.afp.medya.importer.util.FileHelper;

/**
 * @author frederic
 * 
 */
public class XsltTransformProcessFilter implements IProcessFilter {

    private String mvar_xsltFile;

    private String mvar_catalogRefUrl;

    private Hashtable<String, Object> mvar_xsltArguments;

    private String mvar_createMessage = "no";

    private String mvar_createXHTML = "yes";

    private String mvar_PCLExtensions = "yes";

    private String mvar_IgnoreFormerCodeCategory = "yes";

    private JXPathHelper mvar_Helper;

    private String mvar_forceG2Transformation = "no";

    public String isIgnoreFormerCodeCategory() {
        return mvar_IgnoreFormerCodeCategory;
    }

    public void setIgnoreFormerCodeCategory(String ignoreFormerCodeCategory) {
        this.mvar_IgnoreFormerCodeCategory = ignoreFormerCodeCategory;
    }

    public void setForceG2Transformation(String forceG2Transformation) {
        this.mvar_forceG2Transformation = forceG2Transformation;
    }

    public boolean isForceG2Transformation() {
        return mvar_forceG2Transformation.toLowerCase() == "yes";
    }

    /**
	 * 
	 */
    public XsltTransformProcessFilter() {
        mvar_Helper = new JXPathHelper();
        mvar_Helper.registerNamespace(Constantes.NEWSML2PREF, Constantes.NEWSML2NS);
        mvar_Helper.registerNamespace(Constantes.XMLNSPREFIX, Constantes.XMLNS);
    }

    private boolean isNewsMLG2Document(Document document) {
        if (isForceG2Transformation()) return false;
        mvar_Helper.setCurrentContext(document);
        return (mvar_Helper.selectSingleNode("//news:contentSet") != null);
    }

    public Document processDocument(File inputFile, Document document, String lang) throws Exception {
        if (isNewsMLG2Document(document) == false) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XSLTTransformer transformer = new XSLTTransformer();
            transformer.transform(getXsltFile(), document, outputStream, getXsltArguments());
            document = XMLHelper.fileinputStreamToDocument(new ByteArrayInputStream(outputStream.toByteArray()));
        }
        return document;
    }

    /**
	 * @return the mvar_xsltFile
	 */
    public String getXsltFile() {
        return mvar_xsltFile;
    }

    /**
	 * @param mvar_xsltFile
	 *            the mvar_xsltFile to set
	 */
    public void setXsltFile(String xsltFile) {
        this.mvar_xsltFile = FileHelper.MatchHomeDirectory(xsltFile);
    }

    public void Prepare() throws Exception {
        if (getXsltFile() == null) throw new IllegalArgumentException(String.format("Input line[%s], xsltFile n'est pas défini", Taskes.getTaskes().getCurrentTask().getName()));
    }

    public String getCatalogRefUrl() {
        return mvar_catalogRefUrl;
    }

    public void setCatalogRefUrl(String catalogRefUrl) {
        this.mvar_catalogRefUrl = FileHelper.MatchHomeDirectory(catalogRefUrl);
    }

    public Hashtable<String, Object> getXsltArguments() {
        if (mvar_xsltArguments == null) {
            mvar_xsltArguments = new Hashtable<String, Object>();
            mvar_xsltArguments.put("CatalogRefUrl", String.format("file://%s", getCatalogRefUrl()));
            mvar_xsltArguments.put("createMessage", getCreateMessage());
            mvar_xsltArguments.put("createXHTML", getCreateXHTML());
            mvar_xsltArguments.put("PCLExtensions", getPCLExtensions());
            mvar_xsltArguments.put("IgnoreFormerCodeCategory", isIgnoreFormerCodeCategory());
        }
        return mvar_xsltArguments;
    }

    public String getCreateMessage() {
        return mvar_createMessage;
    }

    public void setCreateMessage(String createMessage) {
        this.mvar_createMessage = createMessage;
    }

    public String getCreateXHTML() {
        return mvar_createXHTML;
    }

    public void setCreateXHTML(String createXHTML) {
        this.mvar_createXHTML = createXHTML;
    }

    public String getPCLExtensions() {
        return mvar_PCLExtensions;
    }

    public void setPCLExtensions(String PCLExtensions) {
        this.mvar_PCLExtensions = PCLExtensions;
    }
}
