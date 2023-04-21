package de.tum.in.botl.util.idRuleSetCreator.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import de.tum.in.botl.metamodel.Attribute;
import de.tum.in.botl.metamodel.BotlClass;
import de.tum.in.botl.metamodel.ClassAssociation;
import de.tum.in.botl.metamodel.ClassAssociationEnd;
import de.tum.in.botl.metamodel.ExtensibleMetamodel;
import de.tum.in.botl.metamodel.Metamodel;
import de.tum.in.botl.metamodel.MetamodelConsistencyException;
import de.tum.in.botl.metamodel.MetamodelFactory;
import de.tum.in.botl.metamodel.Type;

/**
 * @author Frank
 */
public class MetamodelHelperImpl {

    private static Map old2new;

    protected static Metamodel getCopy(Metamodel mm0, MetamodelFactory metamodelFactory) throws MetamodelConsistencyException {
        old2new = new HashMap();
        ExtensibleMetamodel mm1 = metamodelFactory.createExtensibleMetamodel(mm0.getName());
        for (Iterator it = mm0.getTypes().iterator(); it.hasNext(); ) {
            Type org = (Type) it.next();
            Type copy = mm1.addType(org.getName());
            old2new.put(org, copy);
        }
        for (Iterator it = mm0.getClasses().iterator(); it.hasNext(); ) {
            BotlClass org = (BotlClass) it.next();
            BotlClass copy = addCopyOfClassToMetamodel(org, mm1);
            old2new.put(org, copy);
        }
        for (Iterator it = mm0.getClasses().iterator(); it.hasNext(); ) {
            BotlClass org = (BotlClass) it.next();
            BotlClass copy = (BotlClass) old2new.get(org);
            for (Iterator it0 = org.getSuperClasses().iterator(); it0.hasNext(); ) {
                BotlClass superClass = (BotlClass) it0.next();
                mm1.addSuperClassToClass(copy, (BotlClass) old2new.get(superClass));
            }
        }
        for (Iterator it = mm0.getClassAssociations().iterator(); it.hasNext(); ) {
            ClassAssociation caOrg = (ClassAssociation) it.next();
            ClassAssociationEnd cae0Org = caOrg.getClassAssociationEnd0();
            ClassAssociationEnd cae1Org = caOrg.getClassAssociationEnd1();
            int lower0 = cae0Org.getMultiplicity().getLowerBound();
            int upper0 = cae0Org.getMultiplicity().getUpperBound();
            int lower1 = cae1Org.getMultiplicity().getLowerBound();
            int upper1 = cae1Org.getMultiplicity().getUpperBound();
            BotlClass newClaa0 = (BotlClass) old2new.get(cae0Org.getTheClass());
            BotlClass newClaa1 = (BotlClass) old2new.get(cae1Org.getTheClass());
            mm1.addClassAssociation(newClaa0, lower0, upper0, cae0Org.getAggregationType(), cae0Org.isNavigable(), cae0Org.getRoleName(), newClaa1, lower1, upper1, cae1Org.getAggregationType(), cae1Org.isNavigable(), cae1Org.getRoleName());
        }
        mm1.fix();
        return mm1.getImmutableMetamodel();
    }

    private static BotlClass addCopyOfClassToMetamodel(BotlClass orgClass, ExtensibleMetamodel newMM) throws MetamodelConsistencyException {
        BotlClass newClass = newMM.addNewClass(orgClass.getName());
        for (Iterator it = orgClass.getAttributes().listIterator(); it.hasNext(); ) {
            Attribute att = (Attribute) it.next();
            Type newType = newMM.getTypeByName(att.getType().getName());
            if (newType == null) newType = newMM.addType(att.getType().getName());
            Attribute newAtt = newMM.addAttributeToClass(newClass, att.getName(), newType, att.getDefaultValue());
            old2new.put(att, newAtt);
        }
        for (Iterator it = orgClass.getPrimaryKeys().iterator(); it.hasNext(); ) {
            Attribute pk = (Attribute) it.next();
            newMM.addPrimaryKeyToClass(newClass, (Attribute) old2new.get(pk));
        }
        return newClass;
    }
}
