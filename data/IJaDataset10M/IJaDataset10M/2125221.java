package net.taylor.mda.jpagen.actions;

import java.util.Iterator;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;

/**
 * Provide an entity with a primary key id having the standard stereotypes @Id
 * and @GeneratedValue.
 * 
 * @author Robert
 */
public class AddPrimaryKeyActionExtension extends BaseUtiltiyActionExtension {

    public void fillContextMenu(IMenuManager aMenu) {
        aMenu.appendToGroup(ICommonMenuConstants.GROUP_NEW, new PrimaryKeyAction());
    }

    protected void execute(Class clazz) {
        Type longType = getLongType(clazz);
        Property property = clazz.createOwnedAttribute("id", longType, 1, 1);
        applyIdStereotype(property);
        applyGeneratedValueStereotype(property);
        addComment(property, "The primary key.");
    }

    protected void applyGeneratedValueStereotype(Property property) {
        for (Iterator it = property.getApplicableStereotypes().iterator(); it.hasNext(); ) {
            Stereotype stereotype = (Stereotype) it.next();
            if (stereotype.getName().equals("javax.persistence.GeneratedValue")) {
                property.applyStereotype(stereotype);
            }
        }
    }

    protected void applyIdStereotype(Property property) {
        for (Iterator it = property.getApplicableStereotypes().iterator(); it.hasNext(); ) {
            Stereotype stereotype = (Stereotype) it.next();
            if (stereotype.getName().equals("javax.persistence.Id")) {
                property.applyStereotype(stereotype);
            }
        }
    }

    protected Type getLongType(Class clazz) {
        Package pkg = clazz.getModel();
        Profile types = pkg.getAppliedProfile("types");
        return types.getOwnedType("Long");
    }

    private class PrimaryKeyAction extends BaseUtilityAction {

        private static final String LABEL = "Add Primary Key";

        public PrimaryKeyAction() {
            super(LABEL);
        }
    }
}
