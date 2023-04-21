package edu.rit.uml2.uml.edit;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.edit.providers.AssociationClassItemProvider;

public class TYDAssociationClassItemProvider extends AssociationClassItemProvider {

    public TYDAssociationClassItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    public String getText(Object object) {
        StringBuffer text = appendType(appendKeywords(new StringBuffer(), object), "_UI_AssociationClass_type");
        AssociationClass associationClass = (AssociationClass) object;
        if (associationClass.isDerived()) {
            appendString(text, " is derived  ");
        }
        String label = associationClass.getLabel(shouldTranslate());
        if (!UML2Util.isEmpty(label)) {
            appendString(text, label);
        } else {
            EList<Property> memberEnds = associationClass.getMemberEnds();
            if (!memberEnds.isEmpty()) {
                appendString(text, "A");
                for (Property memberEnd : memberEnds) {
                    String memberEndName = memberEnd.getName();
                    text.append('_');
                    if (!UML2Util.isEmpty(memberEndName)) {
                        text.append(memberEndName);
                    } else {
                        Type type = memberEnd.getType();
                        if (type != null) {
                            String typeName = type.getName();
                            if (!UML2Util.isEmpty(typeName)) {
                                memberEndName = Character.toLowerCase(typeName.charAt(0)) + typeName.substring(1);
                            }
                        }
                        if (!UML2Util.isEmpty(memberEndName)) {
                            text.append(memberEndName);
                        } else {
                            text.append(getTypeText(memberEnd));
                        }
                    }
                }
            }
        }
        return text.toString();
    }
}
