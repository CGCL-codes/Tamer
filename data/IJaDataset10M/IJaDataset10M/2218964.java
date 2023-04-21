package com.prolix.editor.interaction.model.basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.ui.PlatformUI;
import org.jdom.Element;
import uk.ac.reload.straker.datamodel.learningdesign.components.properties.LD_Property;
import uk.ac.reload.straker.datamodel.learningdesign.components.properties.LocalPersonalProperty;
import uk.ac.reload.straker.datamodel.learningdesign.components.properties.LocalProperty;
import uk.ac.reload.straker.datamodel.learningdesign.components.properties.LocalRoleProperty;
import com.prolix.editor.dialogs.BasicGLMDialog;
import com.prolix.editor.interaction.config.asset.ConfigAssetBasic;
import com.prolix.editor.interaction.config.comment.ConfigCommentBasic;
import com.prolix.editor.interaction.dialogs.model.BasicInteractionConfigDialog;
import com.prolix.editor.interaction.model.GroupPropertyManager;
import com.prolix.editor.interaction.model.InteractionGroup;
import com.prolix.editor.interaction.model.InteractionImpl;
import com.prolix.editor.interaction.operation.OperationImpl;
import com.prolix.editor.interaction.operation.tree.TreeOperation;
import com.prolix.editor.roleview.roles.RoleRole;

/**
 * <<class description>>
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public abstract class BasicInteraction extends InteractionImpl {

    private GroupPropertyManager groupProperties;

    /**
	 * @param parent
	 */
    public BasicInteraction(InteractionGroup parent) {
        super(parent);
        setConfigComment(new ConfigCommentBasic(this));
        setConfigAsset(new ConfigAssetBasic(this));
    }

    public void removeAllListeners() {
    }

    public BasicGLMDialog getManagementDialog(boolean create) {
        return new BasicInteractionConfigDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), this, create);
    }

    public Element marshall2XML(Element parentElement) {
        Element element = super.marshall2XML(parentElement);
        return element;
    }

    public void unmarshallXML(Element element) {
        super.unmarshallXML(element);
        if (element.getChild(ConfigCommentBasic.XML_Name) != null) setConfigComment(new ConfigCommentBasic(this));
        if (element.getChild(ConfigAssetBasic.XML_Name) != null) setConfigAsset(new ConfigAssetBasic(this));
    }

    public abstract LD_Property getContentProperty();

    public LD_Property getContentProperty(Object role) {
        if (isPersonal() || role == null) return getContentProperty();
        return (LD_Property) groupProperties.get(role);
    }

    protected LocalProperty buildProperty() {
        return buildProperty(null);
    }

    protected LocalProperty buildProperty(RoleRole role) {
        LocalProperty ret;
        if (role == null) ret = new LocalPersonalProperty(getLearningDesignDataModel()); else {
            ret = new LocalRoleProperty(getLearningDesignDataModel());
            ((LocalRoleProperty) ret).setRoleRef(role.getData().getIdentifier());
        }
        ret.setTitle(getName() + " - Text");
        ret.setDataType(getPropertyDataType());
        getProperties().addChild(ret);
        addPropertyToGroup(ret);
        return ret;
    }

    protected abstract String getPropertyDataType();

    protected void buildChild() {
        if (isPersonal()) return;
        groupProperties = new GroupPropertyManager();
        for (Iterator it = getAllGroupPropertyRelevantRoles().iterator(); it.hasNext(); ) {
            RoleRole role = (RoleRole) it.next();
            addGroupProperty(role, buildProperty(role));
        }
    }

    protected void addGroupProperty(RoleRole role, LD_Property property) {
        if (role == null || property == null) return;
        if (groupProperties.containsKey(role)) return;
        groupProperties.addGroupProperty(role, property);
    }

    public List getAllGroupPropertyRelevantRoles() {
        List ret = new ArrayList();
        for (Iterator it = getAllActivityOperations().iterator(); it.hasNext(); ) {
            RoleRole role = ((OperationImpl) it.next()).getActingRole();
            if (!ret.contains(role)) ret.add(role);
        }
        return ret;
    }

    private List getAllActivityOperations() {
        List ret = new ArrayList();
        for (Iterator it = getOperations().iterator(); it.hasNext(); ) for (Iterator it2 = ((TreeOperation) it.next()).getOperations().iterator(); it2.hasNext(); ) ret.add(it2.next());
        return ret;
    }

    protected void cleanChild() {
        if (groupProperties == null) return;
        for (Iterator it = groupProperties.iterator(); it.hasNext(); ) getProperties().removeChild((LD_Property) it.next());
        groupProperties = null;
    }
}
