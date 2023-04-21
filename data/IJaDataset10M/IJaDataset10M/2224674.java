package br.unb.unbiquitous.ubiquitos.uos.ontology;

import java.util.List;

/**
 *
 * @author anaozaki
 */
public interface StartReasoner {

    public boolean isInstanceOf(String instanceName, String className);

    public boolean isSubClassOf(String subClassName, String className);

    public boolean hasObjectProperty(String instanceName1, String objectPropertyName, String instanceName2);

    public boolean hasDataProperty(String instanceName1, String dataPropertyName, String string);

    public boolean hasDataProperty(String instanceName1, String dataPropertyName, boolean booleanValue);

    public boolean hasDataProperty(String instanceName1, String dataPropertyName, int intNumber);

    public boolean hasDataProperty(String instanceName1, String dataPropertyName, float floatNumber);

    public boolean isConsistent();

    public List<String> getInstancesFromClass(String className, boolean direct);

    public List<String> getSubClassesFromClass(String className, boolean direct);

    public List<String> getSuperClassesFromClass(String className, boolean direct);

    public boolean areDisjointClasses(String className1, String className2);

    public boolean areEquivalentClasses(String className1, String className2);

    public List<String> getSuperDataPropertiesFromDataProperty(String dataPropertyName, boolean direct);

    public List<String> getDataPropertyValues(String instanceName, String dataPropertyName);

    public List<String> getObjectPropertyValues(String instanceName, String objectPropertyName);
}
