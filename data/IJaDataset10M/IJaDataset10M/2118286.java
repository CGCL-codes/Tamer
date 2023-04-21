package net.sourceforge.mededis.dataaccess.hibernate;

import net.sourceforge.mededis.central.Mededis;
import net.sourceforge.mededis.central.MDException;
import net.sourceforge.mededis.model.*;
import net.sourceforge.mededis.dataaccess.FieldDefinitionAccess;
import net.sourceforge.mededis.dataaccess.DataAccessFactory;
import net.sourceforge.mededis.dataaccess.BasicAccess;
import net.sourceforge.mededis.dataaccess.UserAccess;
import net.sourceforge.mededis.dataaccess.hibernate.HibernateAccessFactory;
import net.sourceforge.mededis.people.model.Resident;
import net.sourceforge.mededis.people.model.Role;
import net.sourceforge.mededis.people.model.Department;
import net.sf.hibernate.metadata.ClassMetadata;
import net.sf.hibernate.metadata.CollectionMetadata;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.type.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.lang.reflect.Method;

/**
 *   Generate default field definitions (confidentiality=0) for the listed objects
 */
public class GenerateMetaData {

    private User user;

    private static Class[] classes = { CodeSet.class, CodeValue.class, User.class, Name.class, Person.class, Role.class, Resident.class, Department.class };

    public void go() {
        try {
            user = new User();
            FieldDefinitionAccess access = (FieldDefinitionAccess) Mededis.getInstance().getDataAccess(user, FieldDefinition.class);
            for (int i = 0; i < classes.length; i++) {
                Class owner = classes[i];
                System.out.println("processing class: " + owner.getName());
                ClassMetadata classInfo = ((HibernateAccessFactory) DataAccessFactory.getInstance()).getDataSessionFactory().getClassMetadata(owner);
                String[] fieldnames = classInfo.getPropertyNames();
                ArrayList fields = new ArrayList();
                for (int j = 0; j < fieldnames.length; j++) {
                    String fieldName = fieldnames[j];
                    String first = "" + fieldName.charAt(0);
                    String capFirst = first.toUpperCase();
                    String capFieldName = fieldName.replaceFirst(first, capFirst);
                    Method getter = null;
                    try {
                        getter = owner.getMethod("get" + capFieldName, null);
                    } catch (NoSuchMethodException e) {
                        try {
                            getter = owner.getMethod("is" + capFieldName, null);
                        } catch (NoSuchMethodException e1) {
                            System.out.println("No getter for: get" + capFieldName);
                        }
                    }
                    if (getter != null) fields.add(fieldnames[j]);
                }
                newLevel(owner, owner, (String[]) fields.toArray(new String[fields.size()]), access, classInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void newLevel(Class owner, Class currentClass, String[] fieldnames, FieldDefinitionAccess access, ClassMetadata classInfo) throws HibernateException, MDException {
        for (int j = 0; j < fieldnames.length; j++) {
            String fieldname = fieldnames[j];
            System.out.println("field: " + fieldname);
            Type fieldType;
            Class fieldClass;
            fieldType = classInfo.getPropertyType(fieldname);
            fieldClass = fieldType.getReturnedClass();
            Class elementClass = null;
            System.out.println(fieldClass);
            if (fieldType.isPersistentCollectionType()) {
                CollectionMetadata collInfo = ((HibernateAccessFactory) DataAccessFactory.getInstance()).getDataSessionFactory().getCollectionMetadata(currentClass.getName() + "." + fieldname);
                Type collElementType = collInfo.getElementType();
                elementClass = collElementType.getReturnedClass();
            }
            FieldDefinition def = new FieldDefinition();
            def.setName(fieldname);
            def.setDescription(fieldname);
            def.setConfidentiality(FieldDefinition.CONFIDENTIALITY_PUBLIC);
            def.setType(fieldClass.getName());
            if (elementClass != null) def.setElementType(elementClass.getName());
            def.setRootClass(owner.getName());
            access.save(def);
        }
    }

    public void generateHierarchy() {
        try {
            FieldDefinitionAccess defAccess = (FieldDefinitionAccess) Mededis.getInstance().getDataAccess(user, FieldDefinition.class);
            BasicAccess fieldAccess = (BasicAccess) Mededis.getInstance().getDataAccess(user, Field.class);
            for (int i = 0; i < classes.length; i++) {
                Class owner = classes[i];
                Collection defs = defAccess.findByRootClass(owner.getName());
                createFields(fieldAccess, defAccess, defs, 0, owner.getName());
            }
        } catch (MDException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createFields(BasicAccess fieldAccess, FieldDefinitionAccess defAccess, Collection defs, long parentId, String rootClass) throws MDException, ClassNotFoundException {
        Iterator iter = defs.iterator();
        while (iter.hasNext()) {
            FieldDefinition def = (FieldDefinition) iter.next();
            Field field = new Field();
            field.setFieldDefinition(def);
            field.setParentId(parentId);
            field.setRootClass(rootClass);
            fieldAccess.save(field);
            Class type = Class.forName(def.getType());
            if (MDObject.class.isAssignableFrom(type)) {
                Collection subFieldDefs = defAccess.findByRootClass(def.getType());
                createFields(fieldAccess, defAccess, subFieldDefs, field.getId(), rootClass);
            } else if (Collection.class.isAssignableFrom(type)) {
                Collection subFieldDefs = defAccess.findByRootClass(def.getElementType());
                createFields(fieldAccess, defAccess, subFieldDefs, field.getId(), rootClass);
            }
        }
    }

    public static void main(String[] args) {
        GenerateMetaData gen = new GenerateMetaData();
        gen.go();
        gen.generateHierarchy();
    }
}
