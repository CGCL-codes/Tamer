package com.metanology.mde.core.ui.common;

import org.eclipse.core.runtime.IAdaptable;
import com.metanology.mde.core.metaModel.AssociationRole;
import com.metanology.mde.core.metaModel.Attribute;
import com.metanology.mde.core.metaModel.CardinalityEnum;
import com.metanology.mde.core.metaModel.Component;
import com.metanology.mde.core.metaModel.MetaClass;
import com.metanology.mde.core.metaModel.MetaObject;
import com.metanology.mde.core.metaModel.Model;
import com.metanology.mde.core.metaModel.Operation;
import com.metanology.mde.core.metaModel.Package;
import com.metanology.mde.core.metaModel.Parameter;
import com.metanology.mde.core.metaModel.ScopeEnum;
import com.metanology.mde.core.metaModel.Subsystem;
import com.metanology.mde.core.ui.plugin.MDEPlugin;
import com.metanology.mde.utils.Messages;

/**
 * TreeNode class represents a node in a tree.
 * It can be parent or leaf.
 */
public class CustomTreeNode implements IAdaptable {

    /**
	 * ROLES contains the association roles of a meta class which is attached
	 * as its data the IFile resource -- NOT its parent!
	 */
    public static final int ROLES = 1;

    /**
	 * association role of a MetaClass
	 */
    public static final int ASSOCIATION = 2;

    /**
	 * Attribute of a MetaClass
	 */
    public static final int ATTRIBUTE = 3;

    /**
	 * Operation of a MetaClass
	 */
    public static final int OPERATION = 4;

    public static final int COMPONENTCLASS = 5;

    public static final int PACKAGE = 6;

    public static final int SUBSYSTEM = 7;

    public static final int CLASS = 8;

    public static final int COMPONENT = 9;

    public static final int CLASSDIAGRAM = 10;

    public static final int COMPONENTDIAGRAM = 11;

    public static final int USECASE_DIAGRAM = 12;

    private String name;

    private Object parent = null;

    private int type;

    private Object dataObj = null;

    public CustomTreeNode(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String val) {
        this.name = val;
    }

    public String toString() {
        try {
            String retVal = this.getName();
            if (retVal == null) {
                return Messages.MSG_UNDEFINED;
            }
            return retVal;
        } catch (Exception ignore) {
            return Messages.MSG_UNDEFINED;
        }
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object node) {
        parent = node;
    }

    /**
	 * get the enumeration type of the TreeNode
	 * @return 	int
	 */
    public int getType() {
        return this.type;
    }

    /**
	 * set the type of this node
	 * @param	type -- int, the enumeration of TreeNode types
	 */
    public void setType(int t) {
        this.type = t;
    }

    /**
	 * get the attached model data of this node
	 * data object retrieved depends on the node type. They may be
	 * Attribute, Operations or AssociationRole, for a ROLES type
	 * this data objet is the owner meta class.
	 * @return Object
	 */
    public Object getData() {
        if (dataObj instanceof MetaClass) {
            return getLatestData((MetaClass) dataObj);
        } else if (dataObj instanceof Package) {
            return getLatestData((Package) dataObj);
        } else if (dataObj instanceof Component) {
            return getLatestData((Component) dataObj);
        } else if (dataObj instanceof Subsystem) {
            return getLatestData((Subsystem) dataObj);
        }
        return this.dataObj;
    }

    private Object getLatestData(MetaClass data) {
        Model model = MDEPlugin.getDefault().getRuntime().getModel();
        if (model != null) {
            return model.findMetaClass(data.getObjId());
        }
        return null;
    }

    private Object getLatestData(Package data) {
        Model model = MDEPlugin.getDefault().getRuntime().getModel();
        if (model != null) {
            return model.findPackage(data.getObjId());
        }
        return null;
    }

    private Object getLatestData(Component data) {
        Model model = MDEPlugin.getDefault().getRuntime().getModel();
        if (model != null) {
            return model.findComponent(data.getObjId());
        }
        return null;
    }

    private Object getLatestData(Subsystem data) {
        Model model = MDEPlugin.getDefault().getRuntime().getModel();
        if (model != null) {
            return model.findSubsystem(data.getObjId());
        }
        return null;
    }

    /**
	 * set the attached data object of this node
	 * For ROLES node, it is the owner meta class
	 * For other package type nodes, its either Attribute, Operation
	 * or AssociationRole.
	 * @param Object
	 */
    public void setData(Object data) {
        this.dataObj = data;
    }

    /**
	 * get display label for a given tree node
	 * @param	Object -- the node for which the label is needed
	 * @return String
	 */
    public static String getLabel(Object o) {
        if (!(o instanceof CustomTreeNode)) {
            return "";
        }
        CustomTreeNode node = (CustomTreeNode) o;
        MetaObject metaObject = (MetaObject) node.getData();
        switch(((CustomTreeNode) o).getType()) {
            case ATTRIBUTE:
                return getAttributeLabel((Attribute) metaObject);
            case OPERATION:
                Operation op = (Operation) metaObject;
                return getOperationLabel(op);
            case ROLES:
                return Messages.MSG_CUSTOM_TREE_NODE_ROLES;
            case ASSOCIATION:
                return CustomTreeNode.getRoleLabel((AssociationRole) node.getData());
            case COMPONENTCLASS:
                if (node.getData() instanceof MetaClass) {
                    MetaClass mcls = (MetaClass) node.getData();
                    StringBuffer label = new StringBuffer(mcls.getName());
                    if (mcls.getStereotype().length() > 0) {
                        label.append("<<").append(mcls.getStereotype()).append(">>");
                    }
                    return label.toString();
                }
            default:
        }
        return "";
    }

    private static String getAttributeLabel(Attribute a) {
        StringBuffer label = new StringBuffer();
        label.append(a.getName());
        if (a.getDataType().length() > 0) {
            label.append(":").append(a.getDataType());
        }
        if (a.getInitValue().length() > 0) {
            label.append("=").append(a.getInitValue());
        }
        if (a.getStereotype().length() > 0) {
            label.append("<<").append(a.getStereotype()).append(">>");
        }
        return label.toString();
    }

    private static String getOperationLabel(Operation op) {
        StringBuffer label = new StringBuffer();
        label.append(op.getName());
        label.append("(");
        if (op.getParameters().size() > 0) {
            Parameter[] parms = new Parameter[op.getParameters().size()];
            op.getParameters().toArray(parms);
            for (int i = 0; i < parms.length; i++) {
                Parameter p = parms[i];
                if (i > 0) {
                    label.append(", ");
                }
                label.append(p.getName()).append(":").append(p.getDataType());
            }
        }
        label.append(")");
        if (op.getReturnType().length() > 0) {
            label.append(":").append(op.getReturnType());
        }
        if (op.getStereotype().length() > 0) {
            label.append("<<").append(op.getStereotype()).append(">>");
        }
        return label.toString();
    }

    private static String getRoleLabel(AssociationRole role) {
        if (role == null) {
            return "";
        }
        StringBuffer label = new StringBuffer();
        label.append(getRoleDescription(role));
        label.append(" -- ");
        label.append(getRoleDescription(role.getRelatedRole()));
        return label.toString();
    }

    private static String getRoleDescription(AssociationRole role) {
        if (role == null) return "";
        StringBuffer label = new StringBuffer();
        label.append(getScopeSign(role.getScope()));
        label.append(role.getName());
        label.append("[" + role.getMetaClass().getName() + "]");
        label.append(getCardinalitySign(role.getMultiplicity()));
        return label.toString();
    }

    private static String getScopeSign(int scope) {
        switch(scope) {
            case ScopeEnum.PRIVATE:
                return new String("-");
            case ScopeEnum.PROTECTED:
                return new String("");
            case ScopeEnum.PUBLIC:
            default:
        }
        return new String("+");
    }

    private static String getCardinalitySign(int card) {
        switch(card) {
            case CardinalityEnum.MANY:
            case CardinalityEnum.ONE_OR_MORE:
            case CardinalityEnum.ZERO_OR_MORE:
                return "*";
            case CardinalityEnum.OPTIONAL:
                return "?";
            case CardinalityEnum.EXACTLY_ONE:
            default:
        }
        return "";
    }

    public Object getAdapter(Class adapter) {
        return null;
    }
}
