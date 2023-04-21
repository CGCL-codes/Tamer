package org.yaoqiang.bpmn.editor.dialog;

import java.util.Map;
import org.yaoqiang.bpmn.editor.BPMNEditor;
import org.yaoqiang.bpmn.model.elements.XMLElement;
import org.yaoqiang.bpmn.model.elements.activities.ResourceParameterBinding;
import org.yaoqiang.bpmn.model.elements.activities.ResourceRole;
import org.yaoqiang.bpmn.model.elements.activities.ResourceRoles;
import org.yaoqiang.bpmn.model.elements.core.common.PartnerEntity;
import org.yaoqiang.bpmn.model.elements.core.common.PartnerRole;
import org.yaoqiang.bpmn.model.elements.data.Assignment;
import org.yaoqiang.bpmn.model.elements.data.DataAssociation;
import org.yaoqiang.bpmn.model.elements.data.ItemAwareElement;
import org.yaoqiang.bpmn.model.elements.events.EventDefinition;
import org.yaoqiang.graph.editor.dialog.BaseDialog;
import org.yaoqiang.graph.editor.dialog.Panel;
import com.mxgraph.util.mxResources;

/**
 * BPMNElementDialog
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class BPMNElementDialog extends BaseDialog {

    private static final long serialVersionUID = 1L;

    public BPMNElementDialog(BPMNEditor editor) {
        super(editor);
    }

    public BPMNElementDialog initDialog() {
        return (BPMNElementDialog) super.initDialog();
    }

    public BPMNPanelContainer getPanelContainer() {
        return (BPMNPanelContainer) panelContainer;
    }

    public XMLPanel getParentPanel() {
        return (XMLPanel) parentPanel;
    }

    public BPMNEditor getEditor() {
        return (BPMNEditor) editor;
    }

    public void editBPMNElement(Object el) {
        editObject(null, el, "", false);
    }

    public void editBPMNElement(XMLPanel parentPanel, Object el) {
        editObject(parentPanel, el, "", false);
    }

    public void editBPMNElement(XMLPanel parentPanel, String type, Object el) {
        editObject(parentPanel, el, type, false);
    }

    public void editBPMNElement(Object el, String type) {
        editObject(null, el, type, false);
    }

    public void editBPMNElement(Object el, boolean fireEvent) {
        editObject(null, el, "", fireEvent);
    }

    public void editObject(Panel parentPanel, Object el, String type, boolean fireEvent) {
        this.parentPanel = parentPanel;
        this.fireEvent = fireEvent;
        panelContainer.setActiveObject(el, type);
        String title = "";
        if (el instanceof Map.Entry<?, ?>) {
            title = mxResources.get("namespace");
        } else if (el instanceof ResourceRoles) {
            title = mxResources.get("resourceAssignment");
        } else if (el instanceof ResourceRole) {
            title = mxResources.get("resource");
        } else if (el instanceof ResourceParameterBinding) {
            title = mxResources.get("resourceParameterBinding");
        } else {
            if (el instanceof EventDefinition || el instanceof DataAssociation || el instanceof Assignment || el instanceof PartnerEntity || el instanceof PartnerRole) {
                title = mxResources.get(((XMLElement) el).toName());
            } else {
                if ((type == null || type.length() == 0 || type.startsWith("addon")) && el instanceof XMLElement || el instanceof ItemAwareElement) {
                    title = mxResources.get(((XMLElement) el).toName());
                } else {
                    title = mxResources.get(type);
                }
            }
        }
        setTitle(title);
        setDialogVisible();
    }

    protected void init() {
        panelContainer = new BPMNPanelContainer(this);
    }
}
