package com.prolix.editor.mainmenue.environments;

import org.eclipse.swt.graphics.Image;
import uk.ac.reload.straker.datamodel.DataComponent;
import uk.ac.reload.straker.datamodel.IDataModelListener;
import uk.ac.reload.straker.datamodel.learningdesign.LearningDesign;
import uk.ac.reload.straker.datamodel.learningdesign.components.environments.Conference;
import uk.ac.reload.straker.datamodel.learningdesign.components.environments.Environment;
import uk.ac.reload.straker.datamodel.learningdesign.components.environments.EnvironmentRef;
import uk.ac.reload.straker.datamodel.learningdesign.components.environments.IndexSearch;
import uk.ac.reload.straker.datamodel.learningdesign.components.environments.LearningObject;
import uk.ac.reload.straker.datamodel.learningdesign.components.environments.Monitor;
import uk.ac.reload.straker.datamodel.learningdesign.components.environments.SendMail;
import com.prolix.editor.LDT_Constrains;
import com.prolix.editor.dialogs.services.ConferenceDialog;
import com.prolix.editor.graph.model.ModelDiagramElement;
import com.prolix.editor.mainmenue.MenueGroup;
import com.prolix.editor.mainmenue.MenueItem;

/**
 * <<class description>>
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public class MenueGroupDetailServiceEnvionments extends MenueGroup implements IDataModelListener {

    private Class containertype;

    private String conferenceType;

    public MenueGroupDetailServiceEnvionments(String name, LearningDesign ld, MenueItem parent, Class containertype) {
        super(name, null, parent);
        super._ld = ld;
        this.containertype = containertype;
        buildChildren();
        ld.getDataModel().addIDataModelListener(this);
    }

    public MenueGroupDetailServiceEnvionments(String name, LearningDesign ld, MenueItem parent, String conferenceType) {
        super(name, null, parent);
        super._ld = ld;
        containertype = Conference.class;
        this.conferenceType = conferenceType;
        buildChildren();
        ld.getDataModel().addIDataModelListener(this);
    }

    protected void buildChildren() {
        Environment[] envis = _ld.getComponents().getEnvironments().getEnvironmentsToArray();
        for (int i = 0; i < envis.length; i++) if (checkType(envis[i])) addChild(new MenueService(this, envis[i]));
    }

    private boolean checkType(Environment environment) {
        if (environment.getChildAt(0) == null) return false;
        if (conferenceType == null) return containertype.equals(environment.getChildAt(0).getClass());
        if (!containertype.equals(environment.getChildAt(0).getClass())) return false;
        String confType = ((Conference) environment.getChildAt(0)).getConferenceType();
        if (conferenceType.equalsIgnoreCase(confType)) return true;
        return false;
    }

    public Image getImage() {
        if (SendMail.class.equals(containertype)) return LDT_Constrains.ICON_SENDMAIL;
        if (Conference.class.equals(containertype)) {
            if (conferenceType.equalsIgnoreCase(ConferenceDialog.ASYNCHRONOUS)) return LDT_Constrains.ICON_FORUM;
            if (conferenceType.equalsIgnoreCase(ConferenceDialog.SYNCHRONOUS)) return LDT_Constrains.ICON_CHAT;
            if (conferenceType.equalsIgnoreCase(ConferenceDialog.ANNOUNCEMENT)) return LDT_Constrains.ICON_ANNOUNCEMENT;
        }
        if (Monitor.class.equals(containertype)) return LDT_Constrains.ICON_MONITOR;
        if (IndexSearch.class.equals(containertype)) return LDT_Constrains.ICON_INDEXSERACH;
        if (LearningObject.class.equals(containertype)) return LDT_Constrains.ICON_LEARNING_OBJECT;
        if (EnvironmentRef.class.equals(containertype)) return LDT_Constrains.ICON_ENVIRONMENT_REF;
        return LDT_Constrains.ImageEnvironment;
    }

    protected String getMenueIdentifier() {
        return getName();
    }

    public ModelDiagramElement getModel() {
        return null;
    }

    /**
	 * @return the containertype
	 */
    public Class getContainertype() {
        return containertype;
    }

    /**
	 * @return the conferenceType
	 */
    public String getConferenceType() {
        return conferenceType;
    }

    public void componentAdded(DataComponent component) {
        if (!(component instanceof Environment)) return;
        Environment environment = (Environment) component;
        if (!checkType(environment)) return;
        addChild(new MenueService(this, environment));
        refresh();
    }

    public void componentChanged(DataComponent component) {
    }

    public void componentMoved(DataComponent component) {
    }

    public void componentRemoved(DataComponent component) {
    }
}
