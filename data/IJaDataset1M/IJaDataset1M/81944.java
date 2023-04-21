package com.ixora.rms.ui.artefacts.dataview.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JDialog;
import com.ixora.rms.ResourceId;
import com.ixora.common.typedproperties.TypedProperties;
import com.ixora.common.typedproperties.exception.VetoException;
import com.ixora.common.typedproperties.ui.list.PropertyListTableModel;
import com.ixora.common.typedproperties.ui.list.TypedPropertiesListEditor;
import com.ixora.common.ui.UIUtils;
import com.ixora.common.ui.forms.FormPanel;
import com.ixora.common.utils.Utils;
import com.ixora.rms.client.model.SessionModel;
import com.ixora.rms.dataengine.definitions.ResourceDef;
import com.ixora.rms.exception.RMSException;
import com.ixora.rms.ui.ResourceSelectorDialog;
import com.ixora.rms.ui.SessionTreeExplorer;
import com.ixora.rms.ui.artefacts.dataview.wizard.messages.Msg;

/**
 * @author Daniel Moraru
 */
public class DistinctQueryDefPanel extends QueryDefPanel {

    private static final long serialVersionUID = -3923851412641381434L;

    private static final String LABEL_COUNTERS = "Counters";

    private FormPanel fForm;

    private TypedPropertiesListEditor fEditorResource;

    private TypedProperties fPrototypeResource;

    private ResourceId fContext;

    private SessionTreeExplorer fExplorer;

    private EventHandler fEventHandler;

    private int fResourceCounter;

    private JDialog fParent;

    private class EventHandler implements PropertyListTableModel.Listener {

        /**
		 * @see com.ixora.common.typedproperties.ui.list.PropertyListTableModel.Listener#aboutToAddNewProperties(com.ixora.common.typedproperties.ui.list.PropertyListTableModel, com.ixora.common.typedproperties.TypedProperties)
		 */
        public void aboutToAddNewProperties(PropertyListTableModel model, TypedProperties prop) throws Exception {
            if (model == fEditorResource.getModel()) {
                handleAboutToAddNewResource(prop);
            }
        }

        /**
		 * @see com.ixora.common.typedproperties.ui.list.PropertyListTableModel.Listener#deletedProperties(com.ixora.common.typedproperties.ui.list.PropertyListTableModel, com.ixora.common.typedproperties.TypedProperties)
		 */
        public void deletedProperties(PropertyListTableModel model, TypedProperties prop) {
        }
    }

    /**
	 * @param model
	 */
    public DistinctQueryDefPanel(JDialog parent, SessionModel sessionModel, ResourceId context, SessionTreeExplorer explorer) {
        super(parent, sessionModel);
        fEventHandler = new EventHandler();
        fForm = new FormPanel(FormPanel.VERTICAL2);
        fContext = context;
        fExplorer = explorer;
        fParent = parent;
        fPrototypeResource = new TypedProperties();
        fPrototypeResource.setProperty(Msg.COUNTER, TypedProperties.TYPE_STRING, true, true);
        fPrototypeResource.setProperty(Msg.ID, TypedProperties.TYPE_STRING, true, true);
        fPrototypeResource.setProperty(Msg.INAME, TypedProperties.TYPE_STRING, true, true);
        fPrototypeResource.setProperty(Msg.NAME, TypedProperties.TYPE_STRING, true, true);
        fPrototypeResource.setProperty(Msg.DESCRIPTION, TypedProperties.TYPE_STRING, true, false);
        fPrototypeResource.setProperty(Msg.MAX, TypedProperties.TYPE_FLOAT, true, false);
        fPrototypeResource.setProperty(Msg.MIN, TypedProperties.TYPE_FLOAT, true, false);
        fPrototypeResource.setProperty(Msg.CODE, TypedProperties.TYPE_STRING, true, false);
        fEditorResource = new TypedPropertiesListEditor(new PropertyListTableModel(fPrototypeResource, null, null) {

            private static final long serialVersionUID = -6978540683892572161L;

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 0) {
                    return false;
                }
                return super.isCellEditable(rowIndex, columnIndex);
            }
        }, TypedPropertiesListEditor.BUTTON_ALL);
        fForm.addPairs(new String[] { LABEL_COUNTERS, LABEL_FUNCTIONS, LABEL_REACTIONS }, new Component[] { fEditorResource, fEditorFunction, fEditorReaction });
        add(fForm, BorderLayout.NORTH);
        fEditorResource.getTable().getColumnModel().getColumn(0).setPreferredWidth(300);
        fEditorResource.getTable().getColumnModel().getColumn(1).setPreferredWidth(25);
        fEditorResource.getTable().getColumnModel().getColumn(2).setPreferredWidth(120);
        fEditorResource.getTable().getColumnModel().getColumn(3).setPreferredWidth(120);
        fEditorResource.setPreferredSize(new Dimension(650, 150));
        fEditorFunction.setPreferredSize(new Dimension(650, 70));
        fEditorReaction.setPreferredSize(new Dimension(650, 70));
        fEditorResource.getModel().addListener(fEventHandler);
    }

    /**
	 * @param prop
	 * @throws VetoException
	 */
    private void handleAboutToAddNewResource(TypedProperties prop) throws VetoException {
        ResourceSelectorDialog dlg = new ResourceSelectorDialog(fParent, ResourceSelectorDialog.SELECT_COUNTER, fExplorer, fSessionModel, fContext, true);
        UIUtils.centerDialogAndShow(fParent, dlg);
        List<ResourceId> result = dlg.getResult();
        if (Utils.isEmptyCollection(result)) {
            throw new VetoException();
        }
        ResourceId rid = result.get(0);
        fResourceCounter++;
        prop.setString(Msg.COUNTER, rid.toString());
        prop.setString(Msg.ID, "id" + fResourceCounter);
        StringBuffer buff = new StringBuffer("$host/$agent/");
        int len = rid.getEntityId().getPathLength();
        for (int i = 1; i < len; ++i) {
            buff.append("$entity[" + i + "]/");
        }
        buff.append("$counter");
        prop.setString(Msg.INAME, buff.toString());
        prop.setString(Msg.NAME, "$counter");
    }

    /**
	 * @see com.ixora.rms.ui.artefacts.dataview.wizard.QueryDefPanel#getResources()
	 */
    public List<ResourceDef> getResources() throws RMSException {
        fEditorResource.stopEditing();
        List<ResourceDef> ret = new LinkedList<ResourceDef>();
        List<TypedProperties> props = fEditorResource.getModel().getAllProperties();
        for (TypedProperties prop : props) {
            ResourceDef rdef = new ResourceDef(new ResourceId(prop.getString(Msg.COUNTER)));
            rdef.setID(prop.getString(Msg.ID));
            rdef.setIName(prop.getString(Msg.INAME));
            rdef.setName(prop.getString(Msg.NAME));
            Object obj = prop.getObject(Msg.MAX);
            if (obj != null) {
                rdef.setMAX(new Double((Float) obj));
            }
            obj = prop.getObject(Msg.MIN);
            if (obj != null) {
                rdef.setMIN(new Double((Float) obj));
            }
            obj = prop.getObject(Msg.CODE);
            if (obj != null) {
                rdef.setCode(obj.toString());
            }
            obj = prop.getObject(Msg.DESCRIPTION);
            if (obj != null) {
                rdef.setDescription(obj.toString());
            }
            ret.add(rdef);
        }
        return ret;
    }

    /**
	 * @see com.ixora.rms.ui.artefacts.dataview.wizard.QueryDefPanel#getPotentialParameters()
	 */
    protected String[] getPotentialParameters() {
        List<TypedProperties> props = fEditorResource.getModel().getAllProperties();
        List<String> params = new LinkedList<String>();
        for (TypedProperties prop : props) {
            params.add(prop.getString(Msg.ID));
        }
        if (!Utils.isEmptyCollection(params)) {
            return params.toArray(new String[params.size()]);
        }
        return null;
    }

    /**
	 * @see com.ixora.rms.ui.artefacts.dataview.wizard.QueryDefPanel#getFunctionIName(java.lang.String)
	 */
    protected String getFunctionIName(String funcName) {
        return funcName;
    }
}
