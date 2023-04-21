package org.jmove.java.loader.typeanalyzer.sourcecode;

import org.jmove.core.Thing;
import org.jmove.java.model.JPackage;
import org.jmove.java.model.Type;
import org.jmove.oo.Inheritance;
import java.util.List;

/**
 * Building an inheritance dependency.
 *
 * @author Michael Juergens
 * @version $Revision: 1.2 $
 */
public class InheritanceBuilder extends DetailBuilder {

    private List mySuperTypes;

    public InheritanceBuilder(Type aLocation, List anSuperTypeList) {
        setPriority(DetailBuildProcessor.PRIORITY_INHERITANCE);
        setLocation(aLocation);
        mySuperTypes = anSuperTypeList;
    }

    public Object build() {
        for (int i = 0; i < mySuperTypes.size(); i++) {
            String superTypeName = (String) mySuperTypes.get(i);
            if (superTypeName != null) {
                createInheritanceLinkForTypeAndSuperType((Type) getLocation(), superTypeName);
            }
        }
        return null;
    }

    protected Object createInheritanceLinkForTypeAndSuperType(Type aType, String aSuperTypeName) {
        Thing superType = getResolver().find(new ReferenceDescription(aSuperTypeName), aType);
        if (superType == null) {
            return null;
        }
        Inheritance superClassLink = new Inheritance();
        superClassLink.connect(aType, Inheritance.Subtype.flyweight, superType, Inheritance.Supertype.flyweight);
        JPackage packageOfDeclaringType = aType.getPackage();
        if (packageOfDeclaringType != null) {
            if (superType instanceof Type) {
                packageOfDeclaringType.addDependency((Type) superType);
            }
        }
        return superClassLink;
    }
}
