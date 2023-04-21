package org.scribble.model.outliner;

import org.eclipse.swt.graphics.Image;
import org.scribble.model.*;

/**
 * This is the abstract base implementation of the ModelOutliner
 * interface, responsible for the model objects in the generic
 * Scribble model package.
 */
public class AbstractModelOutlinerRule implements OutlinerRule {

    /**
	 * This method determines whether the outliner rules is
	 * appropriate to process the supplied element.
	 * 
	 * @param element The element
	 * @return Whether the outliner rule can process the
	 * 						supplied element
	 */
    public boolean isSupported(Object element) {
        boolean ret = false;
        if (element.getClass() == org.scribble.model.Namespace.class || element.getClass() == org.scribble.model.Import.class || element.getClass() == org.scribble.model.Definition.class || element instanceof java.util.List || element.getClass() == org.scribble.model.Block.class || element instanceof Interaction || element instanceof RoleList || element instanceof Role) {
            ret = true;
        }
        return (ret);
    }

    /**
	 * This method returns the label to be displayed
	 * for the supplied object.
	 * 
	 * @param obj The object
	 * @return The label
	 */
    public String getLabel(Object obj) {
        String ret = null;
        if (obj instanceof org.scribble.model.Namespace) {
            ret = ((org.scribble.model.Namespace) obj).getName();
        } else if (obj instanceof org.scribble.model.Import) {
            ret = ((org.scribble.model.Import) obj).getName();
        } else if (obj instanceof org.scribble.model.RoleList) {
            ret = "Roles";
        } else if (obj instanceof org.scribble.model.Role) {
            ret = ((org.scribble.model.Role) obj).getName();
        } else if (obj instanceof org.scribble.model.Interaction) {
            Interaction interaction = (Interaction) obj;
            ret = "";
            if (interaction.getFromRole() != null) {
                ret += interaction.getFromRole().getName();
            }
            ret += "->";
            if (interaction.getToRole() != null) {
                ret += interaction.getToRole().getName();
            }
            ret += " : ";
            if (interaction.getMessageSignature() != null) {
                if (interaction.getMessageSignature().getOperation() != null) {
                    ret += interaction.getMessageSignature().getOperation();
                    ret += "(";
                }
                for (int i = 0; i < interaction.getMessageSignature().getTypes().size(); i++) {
                    if (i > 0) {
                        ret += ",";
                    }
                    ret += interaction.getMessageSignature().getTypes().get(i).getAlias();
                }
                if (interaction.getMessageSignature().getOperation() != null) {
                    ret += ")";
                }
            }
        } else if (obj instanceof ContainmentList) {
            Class<?> type = ((ContainmentList<?>) obj).getType();
            if (type != null) {
                ret = type.getName();
                int index = ret.lastIndexOf('.');
                if (index != -1) {
                    ret = ret.substring(index + 1);
                }
            }
        } else if (obj instanceof java.util.List && ((java.util.List<?>) obj).size() > 0) {
            Class<?> cls = ((java.util.List<?>) obj).get(0).getClass();
            if (cls.getInterfaces().length > 0) {
                ret = cls.getInterfaces()[0].getName();
            } else {
                ret = cls.getName();
            }
            int index = ret.lastIndexOf('.');
            if (index != -1) {
                ret = ret.substring(index + 1);
            }
        }
        return (ret);
    }

    /**
	 * This method returns an optional image associated with
	 * the supplied object.
	 * 
	 * @param obj The object
	 * @return The image, or null if no image to display
	 */
    public org.eclipse.swt.graphics.Image getImage(Object obj) {
        Image ret = null;
        if (obj instanceof org.scribble.model.Namespace) {
            ret = org.scribble.model.images.ScribbleImages.getImage("namespace.png");
        } else if (obj instanceof org.scribble.model.Import) {
            ret = org.scribble.model.images.ScribbleImages.getImage("import.png");
        } else if (obj instanceof org.scribble.model.Role || obj instanceof org.scribble.model.RoleList) {
            ret = org.scribble.model.images.ScribbleImages.getImage("role.png");
        } else if (obj instanceof java.util.List && ((java.util.List<?>) obj).size() > 0) {
            ret = getImage(((java.util.List<?>) obj).get(0));
        }
        return (ret);
    }

    /**
	 * This method returns the list of children associated
	 * with the supplied object.
	 * 
	 * @param obj The object
	 * @return The list of child objects
	 */
    public java.util.List<Object> getChildren(Object obj) {
        java.util.List<Object> ret = null;
        if (obj instanceof Definition) {
            Definition defn = (Definition) obj;
            ret = getChildren(defn.getBlock());
        } else if (obj instanceof Block) {
            ret = new java.util.Vector<Object>(((Block) obj).getContents());
        } else if (obj instanceof MultiPathBehaviour) {
            ret = new java.util.Vector<Object>(((MultiPathBehaviour) obj).getPaths());
        } else if (obj instanceof RoleList) {
            ret = new java.util.Vector<Object>(((RoleList) obj).getRoles());
        } else if (obj instanceof java.util.List) {
            ret = new java.util.Vector<Object>();
            ret.addAll((java.util.List<?>) obj);
        }
        return (ret);
    }

    /**
	 * This method returns the children for the model.
	 * 
	 * @param model The model
	 * @return The list of children
	 */
    protected java.util.List<Object> getModelChildren(org.scribble.model.Model model) {
        java.util.List<Object> ret = new java.util.Vector<Object>();
        if (model.getNamespace() != null) {
            ret.add(model.getNamespace());
        }
        ret.add(model.getImports());
        return (ret);
    }

    /**
	 * This method determines whether the supplied object has
	 * children.
	 * 
	 * @param obj The object
	 * @return Whether the object has children
	 */
    public boolean hasChildren(Object obj) {
        boolean ret = false;
        if (obj instanceof Definition || obj instanceof Block || obj instanceof RoleList || obj instanceof MultiPathBehaviour || obj instanceof java.util.List) {
            ret = true;
        }
        return (ret);
    }

    /**
	 * This method returns the textual description associated
	 * with the supplied reference.
	 * 
	 * @param ref The reference
	 * @return The description
	 */
    protected String getReferenceDescription(ModelReference ref) {
        String ret = "";
        if (ref.getNamespace() != null) {
            ret += ref.getNamespace();
        }
        if (ret.length() > 0) {
            ret += ".";
        }
        ret += ref.getLocalpart();
        if (ref.getLocatedRole() != null) {
            ret += "@" + ref.getLocatedRole();
        }
        return (ret);
    }
}
