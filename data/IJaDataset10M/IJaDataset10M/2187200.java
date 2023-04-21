package com.prolix.editor.interaction.operation.test;

import java.util.Map;
import org.jdom.Element;
import com.prolix.editor.dialogs.command.ShadowCommandChain;
import com.prolix.editor.graph.model.activities.ModelDiagramActivity;
import com.prolix.editor.interaction.dialogs.operation.OperationConfigDialog;
import com.prolix.editor.interaction.dialogs.operation.test.OperationConfigTestDisplay;
import com.prolix.editor.interaction.model.Interaction;
import com.prolix.editor.interaction.model.test.TestInteraction;
import com.prolix.editor.interaction.operation.Operation;
import com.prolix.editor.interaction.operation.OperationImpl;
import com.prolix.editor.roleview.roles.RoleRole;

/**
 * <<class description>>
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public class TestOperationDisplay extends OperationImpl {

    public static final String XML_Name = "test_view";

    private static final String XML_SHOW_COMMENT_NAME = "show_comment_name";

    private static final String XML_SHOW_NAME = "show_name";

    private static final String XML_SHOW_ORIGINAL = "show_original";

    private static final String XML_DESC_ASSET = "desc_asset";

    private static final String XML_DESC_COMMENT = "desc_comment";

    private static final String XML_ASSET = "asset";

    private static final String XML_COMMENT = "comment";

    private static final String XML_OTHER = "other";

    private static final String XML_PERSONAL = "personal";

    private static final String XML_DESC_DISPLAY = "desc_disp";

    public static final String id = "view";

    public static final String name = "Show: ";

    private String descDisplay;

    private boolean readPersonal;

    private RoleRole readOtherRole;

    private String descComment;

    private String descAsset;

    private boolean enableComment;

    private boolean enableAsset;

    private boolean showOriginal;

    private boolean showName;

    private boolean showCommentName;

    public TestOperationDisplay() {
        super();
        showName = true;
        showOriginal = true;
        descComment = "";
        descAsset = "";
        descDisplay = "";
        readPersonal = true;
    }

    public String getId() {
        return getInteraction().getId() + id;
    }

    public OperationConfigDialog getOperationActivityDialog() {
        return new OperationConfigTestDisplay(getShell(), this);
    }

    public OperationConfigDialog getOperationActivityDialog(ShadowCommandChain shadowCommandChain) {
        return new OperationConfigTestDisplay(getShell(), this, shadowCommandChain);
    }

    /**
	 * @return the descComment
	 */
    public String getDescComment() {
        return descComment;
    }

    /**
	 * @param descComment
	 *           the descComment to set
	 */
    public void setDescComment(String descComment) {
        this.descComment = descComment;
    }

    /**
	 * @return the descAsset
	 */
    public String getDescAsset() {
        return descAsset;
    }

    /**
	 * @param descAsset
	 *           the descAsset to set
	 */
    public void setDescAsset(String descAsset) {
        this.descAsset = descAsset;
    }

    /**
	 * @return the enableComment
	 */
    public boolean isEnableComment() {
        return enableComment;
    }

    /**
	 * @param enableComment
	 *           the enableComment to set
	 */
    public void setEnableComment(boolean enableComment) {
        if (this.enableComment == enableComment) return;
        this.enableComment = enableComment;
    }

    /**
	 * @return the enableAsset
	 */
    public boolean isEnableAsset() {
        return enableAsset;
    }

    /**
	 * @param enableAsset
	 *           the enableAsset to set
	 */
    public void setEnableAsset(boolean enableAsset) {
        if (this.enableAsset == enableAsset) return;
        this.enableAsset = enableAsset;
    }

    /**
	 * @return the showOriginal
	 */
    public boolean isShowOriginal() {
        return showOriginal;
    }

    /**
	 * @param showOriginal
	 *           the showOriginal to set
	 */
    public void setShowOriginal(boolean showOriginal) {
        this.showOriginal = showOriginal;
    }

    /**
	 * @return the showName
	 */
    public boolean isShowName() {
        return showName;
    }

    /**
	 * @param showName
	 *           the showName to set
	 */
    public void setShowName(boolean showName) {
        this.showName = showName;
    }

    /**
	 * @return the showCommentName
	 */
    public boolean isShowCommentName() {
        return showCommentName;
    }

    /**
	 * @param showCommentName
	 *           the showCommentName to set
	 */
    public void setShowCommentName(boolean showCommentName) {
        this.showCommentName = showCommentName;
    }

    /**
	 * @return the descDisplay
	 */
    public String getDescDisplay() {
        return descDisplay;
    }

    /**
	 * @param descDisplay
	 *           the descDisplay to set
	 */
    public void setDescDisplay(String descDisplay) {
        this.descDisplay = descDisplay;
    }

    /**
	 * @return the readPersonal
	 */
    public boolean isReadPersonal() {
        return readPersonal;
    }

    public boolean isReadOther() {
        return !readPersonal;
    }

    /**
	 * @param readPersonal
	 *           the readPersonal to set
	 */
    public void setReadPersonal(boolean readPersonal) {
        this.readPersonal = readPersonal;
    }

    public void setReadOther(boolean readOther) {
        this.readPersonal = !readOther;
    }

    /**
	 * @return the readOtherRole
	 */
    public RoleRole getReadOtherRole() {
        return readOtherRole;
    }

    /**
	 * @param readOtherRole
	 *           the readOtherRole to set
	 */
    public void setReadOtherRole(RoleRole readOtherRole) {
        this.readOtherRole = readOtherRole;
    }

    public TestInteraction getTestInteraction() {
        return (TestInteraction) getInteraction();
    }

    public boolean canMarshall2XML() {
        return true;
    }

    public String getXMLElementName() {
        return XML_Name;
    }

    public Element marshall2XML(Element parentElement) {
        Element element = super.marshall2XML(parentElement);
        Element data = new Element(XML_DESC_DISPLAY);
        element.addContent(data);
        data.setText(descDisplay);
        element.setAttribute(XML_PERSONAL, "" + readPersonal);
        if (isReadOther()) element.setAttribute(XML_OTHER, readOtherRole.getData().getIdentifier());
        element.setAttribute(XML_SHOW_ORIGINAL, "" + showOriginal);
        element.setAttribute(XML_SHOW_NAME, "" + showName);
        element.setAttribute(XML_COMMENT, "" + enableComment);
        if (isEnableComment()) element.setAttribute(XML_SHOW_COMMENT_NAME, "" + showCommentName);
        element.setAttribute(XML_ASSET, "" + enableAsset);
        if (isEnableComment()) {
            data = new Element(XML_DESC_COMMENT);
            element.addContent(data);
            data.setText(descComment);
        }
        if (isEnableAsset()) {
            data = new Element(XML_DESC_ASSET);
            element.addContent(data);
            data.setText(descAsset);
        }
        return element;
    }

    public void unmarshallXML(Element element) {
        super.unmarshallXML(element);
        Element data = element.getChild(XML_DESC_DISPLAY);
        if (data != null) descDisplay = data.getText();
        readPersonal = loadBoolean(element.getAttributeValue(XML_PERSONAL));
        if (isReadOther()) readOtherRole = loadRole(element.getAttributeValue(XML_OTHER));
        showOriginal = loadBoolean(element.getAttributeValue(XML_SHOW_ORIGINAL));
        showName = loadBoolean(element.getAttributeValue(XML_SHOW_NAME));
        enableComment = loadBoolean(element.getAttributeValue(XML_COMMENT));
        if (isEnableComment()) showCommentName = loadBoolean(element.getAttributeValue(XML_SHOW_COMMENT_NAME));
        enableAsset = loadBoolean(element.getAttributeValue(XML_ASSET));
        if (isEnableComment()) {
            data = element.getChild(XML_DESC_COMMENT);
            if (data != null) descComment = data.getText();
        }
        if (isEnableAsset()) {
            data = element.getChild(XML_DESC_ASSET);
            if (data != null) descAsset = data.getText();
        }
    }

    public Operation dublicate(Interaction parent, ModelDiagramActivity activity, Map rolesSpeicher) {
        TestOperationDisplay copy = new TestOperationDisplay();
        dublicate(copy, parent, activity, rolesSpeicher);
        return copy;
    }

    protected void dublicate(TestOperationDisplay copy, Interaction parent, ModelDiagramActivity activity, Map rolesSpeicher) {
        super.dublicate(copy, parent, activity);
        if (isReadOther()) {
            RoleRole tmp = getReadOtherRole();
            if (rolesSpeicher != null && tmp != null) {
                tmp = (RoleRole) rolesSpeicher.get(tmp.getData().getIdentifier());
            }
            copy.setReadOtherRole(tmp);
        }
        copy.setDescDisplay(getDescDisplay());
        copy.setReadPersonal(isReadPersonal());
        copy.setEnableComment(isEnableComment());
        copy.setDescComment(getDescComment());
        copy.setEnableAsset(isEnableAsset());
        copy.setDescAsset(getDescAsset());
        copy.setShowOriginal(isShowOriginal());
        copy.setShowName(isShowName());
        copy.setShowCommentName(isShowCommentName());
    }

    public boolean isRoleInUse(RoleRole role) {
        if (!isReadOther()) {
            return false;
        }
        return role.equals(this.readOtherRole);
    }
}
