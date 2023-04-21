package org.opencms.setup.xml;

import org.opencms.configuration.CmsConfigurationManager;
import org.opencms.i18n.CmsEncoder;
import org.opencms.setup.CmsSetupBean;
import org.opencms.util.CmsStringUtil;
import org.opencms.xml.CmsXmlUtils;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Node;

/**
 * Skeleton for xml update plugins.<p>
 * 
 * @author Michael Moossen
 * 
 * @version $Revision: 1.2 $ 
 * 
 * @since 6.1.8 
 */
public abstract class A_CmsSetupXmlUpdate implements I_CmsSetupXmlUpdate {

    /**
     * @see org.opencms.setup.xml.I_CmsSetupXmlUpdate#execute(org.opencms.setup.CmsSetupBean)
     */
    public void execute(CmsSetupBean setupBean) throws Exception {
        Document doc = setupBean.getXmlHelper().getDocument(getXmlFilename());
        Iterator itRemove = getXPathsToRemove().iterator();
        while (itRemove.hasNext()) {
            String xpath = (String) itRemove.next();
            CmsSetupXmlHelper.setValue(doc, xpath, null);
        }
        Iterator itUpdate = getXPathsToUpdate().iterator();
        while (itUpdate.hasNext()) {
            String xpath = (String) itUpdate.next();
            executeUpdate(doc, xpath);
        }
    }

    /**
     * @see org.opencms.setup.xml.I_CmsSetupXmlUpdate#getCodeToChange(org.opencms.setup.CmsSetupBean)
     */
    public String getCodeToChange(CmsSetupBean setupBean) throws Exception {
        String ret = "";
        Document doc = setupBean.getXmlHelper().getDocument(getXmlFilename());
        Iterator itRemove = getXPathsToRemove().iterator();
        while (itRemove.hasNext()) {
            String xpath = (String) itRemove.next();
            Iterator it = doc.selectNodes(xpath).iterator();
            while (it.hasNext()) {
                Node node = (Node) it.next();
                if (node != null) {
                    ret += CmsXmlUtils.marshal(node, CmsEncoder.ENCODING_UTF_8);
                }
            }
        }
        String parentPath = getCommonPath();
        Document newDoc = prepareDoc(doc);
        boolean modified = false;
        Iterator itUpdate = getXPathsToUpdate().iterator();
        while (itUpdate.hasNext()) {
            String xpath = (String) itUpdate.next();
            updateDoc(doc, newDoc, xpath);
            boolean exe = executeUpdate(newDoc, xpath);
            modified = modified || exe;
            if ((parentPath == null) && exe) {
                Node node = newDoc.selectSingleNode(xpath);
                if (node != null) {
                    ret += CmsXmlUtils.marshal(node, CmsEncoder.ENCODING_UTF_8);
                }
            }
        }
        if ((parentPath != null) && modified) {
            Node node = newDoc.selectSingleNode(parentPath);
            if (node != null) {
                ret += CmsXmlUtils.marshal(node, CmsEncoder.ENCODING_UTF_8);
            }
        }
        return ret.trim();
    }

    /**
     * Updates the given doc inserting the given node corresponding to the given xpath.<p>
     * 
     * @param document the original document to update
     * @param newDoc the document to update
     * @param xpath the corresponding xpath
     */
    protected void updateDoc(Document document, Document newDoc, String xpath) {
        Node node = document.selectSingleNode(xpath);
        if (node != null) {
            CmsSetupXmlHelper.setValue(newDoc, CmsXmlUtils.removeLastComplexXpathElement(xpath), " ");
            node = (Node) node.clone();
            node.setParent(null);
            ((Branch) newDoc.selectSingleNode(CmsXmlUtils.removeLastComplexXpathElement(xpath))).add(node);
        }
    }

    /**
     * Returns a parent path that is common for all nodes to modify.<p> 
     * 
     * @return common parent path
     */
    protected String getCommonPath() {
        return null;
    }

    /**
     * @see org.opencms.setup.xml.I_CmsSetupXmlUpdate#validate(org.opencms.setup.CmsSetupBean)
     */
    public boolean validate(CmsSetupBean setupBean) throws Exception {
        return CmsStringUtil.isNotEmptyOrWhitespaceOnly(getCodeToChange(setupBean));
    }

    /**
     * Executes the adding/updating changes on the given document.<p>
     * 
     * Only needs to be overriden if {@link #getXPathsToUpdate()} is not empty.<p>
     * 
     * @param document the document to apply the changes to
     * @param xpath the xpath to execute the changes for
     * 
     * @return if something was modified
     */
    protected boolean executeUpdate(Document document, String xpath) {
        return ((Object) document == (Object) xpath);
    }

    /**
     * Returns a list of xpaths for the nodes to remove.<p>
     * 
     * @return a list of strings
     */
    protected List getXPathsToRemove() {
        return Collections.EMPTY_LIST;
    }

    /**
     * Returns a list of xpaths for the nodes to add/update.<p>
     * 
     * @return a list of strings
     */
    protected List getXPathsToUpdate() {
        return Collections.EMPTY_LIST;
    }

    /**
     * Prepares a new document.<p>
     * 
     * @param doc the original document
     * 
     * @return a new document 
     */
    protected Document prepareDoc(Document doc) {
        Document newDoc = new DocumentFactory().createDocument();
        newDoc.addElement(CmsConfigurationManager.N_ROOT);
        newDoc.setName(doc.getName());
        return newDoc;
    }
}
