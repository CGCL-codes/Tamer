package org.openemed.CTS;

/**
 * Created by IntelliJ IDEA.
 * User: dwf
 * Date: Jul 13, 2005
 * Time: 11:31:58 PM
 */
public class CodingFactory {

    String type;

    static String stype = "hashstore";

    static CodingFactory factory;

    static SystemsContainer container;

    public CodingFactory(String storageType) {
        type = storageType;
    }

    public static CodingFactory newInstance() {
        String type = System.getProperties().getProperty("org.openemed.CTS.PersistenceType", stype);
        return new CodingFactory(type);
    }

    public static void setType(String storageType) {
        stype = storageType;
    }

    public static CodingFactory getCurrent() {
        if (factory == null) factory = newInstance();
        return factory;
    }

    public CodedConcept newCodedConcept() {
        if (type.equals("hashstore") || type.equals("jdbm")) {
            try {
                Class c = Class.forName("org.openemed.CTS." + type + ".CodedConceptImpl");
                CodedConcept concept = (CodedConcept) c.newInstance();
                return concept;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public CodeSystem newCodeSystem() {
        if (type.equals("hashstore") || type.equals("jdbm")) {
            try {
                Class c = Class.forName("org.openemed.CTS." + type + ".CodeSystemImpl");
                CodeSystem system = (CodeSystem) c.newInstance();
                return system;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public SystemsContainer newCodeSystems() {
        if (type.equals("hashstore") || type.equals("jdbm")) {
            try {
                Class c = Class.forName("org.openemed.CTS." + type + ".SystemsContainerImpl");
                SystemsContainer system = (SystemsContainer) c.newInstance();
                return system;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Association newAssociation() {
        if (type.equals("hashstore") || type.equals("jdbm")) {
            try {
                Class c = Class.forName("org.openemed.CTS." + type + ".AssociationImpl");
                Association assoc = (Association) c.newInstance();
                return assoc;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static SystemsContainer getCurrentContainer(String database) {
        if (container == null) {
            container = getCurrent().newCodeSystems();
            System.out.println("getCurrentContainer: " + database + " being initialized");
            container.init(database);
        }
        return container;
    }
}
