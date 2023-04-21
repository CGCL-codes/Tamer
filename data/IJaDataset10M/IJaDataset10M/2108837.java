package org.powerfolder.workflow.model.script;

import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.powerfolder.PFRuntimeException;
import org.powerfolder.config.ScriptTagMenuBranch;
import org.powerfolder.config.ScriptTagMenuQualifier;
import org.powerfolder.config.ScriptTagSetHolder;
import org.powerfolder.utils.xml.XMLHelper;

public class RootScriptTagHolder {

    public static final String ROOT_CONSTRAINT = "root";

    private ScriptTagCharacteristic tc = null;

    private boolean errorPresent = false;

    private ScriptTagInitializer ti = null;

    private ScriptTagConstraint tcon = null;

    private ScriptTagSetHolder tsh = null;

    private ScriptTagMenuBranch tmb = null;

    private ScriptTagMenuQualifier tmq = null;

    protected RootScriptTagHolder() {
    }

    public ScriptTagInitializer getScriptTagInitializer() {
        return this.ti;
    }

    void setScriptTagInitializer(ScriptTagInitializer inTi) {
        this.ti = inTi;
    }

    public ScriptTagCharacteristic getScriptTagCharacteristic() {
        return this.tc;
    }

    void setScriptTagCharacteristic(ScriptTagCharacteristic inTc) {
        this.tc = inTc;
    }

    public ScriptTagConstraint getScriptTagConstraint() {
        return this.tcon;
    }

    void setScriptTagConstraint(ScriptTagConstraint inTc) {
        this.tcon = inTc;
    }

    public boolean isErrorPresent() {
        return this.errorPresent;
    }

    void setErrorPresent(boolean inErrorPresent) {
        this.errorPresent = inErrorPresent;
    }

    public ScriptTagSetHolder getScriptTagSetHolder() {
        return this.tsh;
    }

    void setScriptTagSetHolder(ScriptTagSetHolder inTsh) {
        this.tsh = inTsh;
    }

    public ScriptTagMenuBranch getScriptTagMenu() {
        return this.tmb;
    }

    void setScriptTagMenu(ScriptTagMenuBranch inTmb) {
        this.tmb = inTmb;
    }

    public ScriptTagMenuQualifier getScriptTagMenuQualifier() {
        return this.tmq;
    }

    void setScriptTagMenuQualifier(ScriptTagMenuQualifier inTmq) {
        this.tmq = inTmq;
    }

    public String getXMLRepresentation() {
        try {
            StringWriter outValue = new StringWriter();
            Document doc = XMLHelper.createBlankDocument();
            getXMLRepresentation(this.tcon, doc, doc);
            XMLHelper.writeDocument(doc, outValue);
            return outValue.toString();
        } catch (ParserConfigurationException pce) {
            throw new PFRuntimeException(pce);
        } catch (IOException ioe) {
            throw new PFRuntimeException(ioe);
        }
    }

    private void getXMLRepresentation(ScriptTagConstraint inTc, Node inBase, Document inDoc) {
        String constraintName = inTc.getName();
        ScriptTagCharacteristic tc = inTc.getScriptTagCharacteristic();
        for (int i = 0; i < tc.getValueLength(); i++) {
            Element childElement = null;
            if (tc.isStatic(i)) {
                childElement = inDoc.createElementNS(BaseScriptTagConstraint.PF_NAMESPACE, BaseScriptTagConstraint.PF_TEXT);
                Text textNode = inDoc.createTextNode(tc.getValueAsString(i));
                childElement.appendChild(textNode);
                appendNode(inDoc, inBase, childElement, constraintName);
            } else if (tc.isDynamic(i)) {
                ScriptTag childTag = tc.getValueAsScriptTag(i);
                ScriptTagInitializer childTi = tc.getScriptTagInitializer(i);
                Class tagClass = childTag.getClass();
                String tagNs = this.tsh.getScriptTagNamespace(tagClass);
                String tagName = this.tsh.getScriptTagName(tagClass);
                childElement = inDoc.createElementNS(tagNs, tagName);
                appendNode(inDoc, inBase, childElement, constraintName);
                for (int j = 0; j < childTi.getScriptTagConstraintCount(); j++) {
                    ScriptTagConstraint nextTc = childTi.getScriptTagConstraint(j);
                    getXMLRepresentation(nextTc, childElement, inDoc);
                }
            } else if (tc.isCompound(i)) {
                CompoundHolder childCh = tc.getValueAsCompoundHolder(i);
                childElement = inDoc.createElementNS(BaseScriptTagConstraint.PF_NAMESPACE, BaseScriptTagConstraint.PF_COMPOUND);
                appendNode(inDoc, inBase, childElement, constraintName);
                for (int j = 0; j < childCh.getScriptTagConstraintCount(); j++) {
                    ScriptTagConstraint nextTc = childCh.getScriptTagConstraint(j);
                    getXMLRepresentation(nextTc, childElement, inDoc);
                }
            } else {
                throw new PFRuntimeException("Unknown TagChar value.");
            }
        }
    }

    private static final void appendNode(Document inDoc, Node inBase, Element inChild, String inConstraint) {
        Attr conAttr = inDoc.createAttribute(ScriptTagInitializerBean.CONSTRAINT_NAME);
        conAttr.setNodeValue(inConstraint);
        inChild.setAttributeNode(conAttr);
        inBase.appendChild(inChild);
    }

    private static final void getXMLRepresentation(ScriptTagInitializer inTi, StringBuffer inSb, int inIndent[]) {
    }

    private static final void getXMLRepresentation(CompoundHolder inCh, StringBuffer inSb, int inIndent[]) {
    }
}
