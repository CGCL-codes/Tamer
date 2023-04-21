package org.bluprint.app.mapper.adapter.uml2;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.bluprint.app.mapper.adapter.ClassifierAdapter;
import org.bluprint.app.mapper.adapter.OperationAdapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.Classifier;
import org.eclipse.uml2.Class;
import org.eclipse.uml2.Interface;
import org.eclipse.uml2.Package;
import org.eclipse.uml2.Stereotype;
import org.eclipse.uml2.Type;
import org.eclipse.uml2.Operation;

/**
 * Abstract base implementation of the ClassifierAdapter interface.
 * 
 * @author Peter Long
 */
public abstract class UML2ClassifierAdapter extends UML2NamedElementAdapter implements ClassifierAdapter {

    private Classifier classifier;

    public Classifier getClassifier() {
        return classifier;
    }

    public void setClassifier(Classifier aClassifier) {
        classifier = aClassifier;
    }

    public String[] getImportedClassNames() {
        return null;
    }

    /**
	 * Return all the operations supported by his Classifier, wrapping each in its own OperationAdapter.
	 */
    public OperationAdapter[] getOperations() {
        EList operations = getOwnedOperations();
        Vector<OperationAdapter> operationAdapters = new Vector<OperationAdapter>();
        OperationAdapter[] result = new OperationAdapter[0];
        for (int i = 0; i < operations.size(); i++) {
            Operation operation = (Operation) operations.get(i);
            operationAdapters.add(new UML2OperationAdapter(operation));
        }
        return operationAdapters.toArray(result);
    }

    public Package getPackage() {
        return null;
    }

    public String getPackageName() {
        return null;
    }

    /**
	 * Get all the Types referenced by this classifier.  This includes
	 * a) all the Types which this classifier inherits from.
	 * b) all the Types referenced in all the operations supported by this Classifier including
	 *    - all return Types
	 *    - all parameter Types
	 */
    public Type[] getReferencedTypes() {
        HashSet<Type> typeSet = new HashSet<Type>();
        typeSet.addAll(getSuperTypes());
        EList operations = getOwnedOperations();
        OperationAdapter operationAdapter = new UML2OperationAdapter(null);
        Type[] operationTypes = new Type[0];
        for (int i = 0; i < operations.size(); i++) {
            operationAdapter.wrap((Operation) operations.get(i));
            operationTypes = operationAdapter.getReferencedTypes();
            for (int j = 0; j < operationTypes.length; j++) {
                if (operationTypes[j] != null) {
                    typeSet.add(operationTypes[j]);
                }
            }
        }
        return typeSet.toArray(new Type[0]);
    }

    /**
	 * Get all the super types from which this classifier inherits.
	 * 
	 * @return The set of all inherited types.
	 */
    private HashSet<Type> getSuperTypes() {
        HashSet<Type> typeSet = new HashSet<Type>();
        EList generalClassifiers = getClassifier().getGenerals();
        for (int i = 0; i < generalClassifiers.size(); i++) {
            Type generalType = (Type) generalClassifiers.get(i);
            if (generalType != null) {
                typeSet.add((Type) generalClassifiers.get(i));
            }
        }
        return typeSet;
    }

    /**
	 * The following is a work around until we upgrade from EMF 1.1.1 to EMF 2.*
	 * The getOwnedOperation() method is in the Classifier interface within EMF 2.*, but in the sub-classes for EMF 1.1.1
	 * 
	 * @return the ownedOperations() of the classifier
	 */
    private EList getOwnedOperations() {
        EList operations = null;
        if (getClassifier() instanceof Class) {
            Class aClass = (Class) getClassifier();
            operations = aClass.getOwnedOperations();
        }
        if (getClassifier() instanceof Interface) {
            Interface anInterface = (Interface) getClassifier();
            operations = anInterface.getOwnedOperations();
        }
        return operations;
    }

    /**
	 * Return the fully qualified names of all the classifiers from which this classifier inherits.
	 */
    public String[] getSuperClasses() {
        HashSet<Type> superTypes = getSuperTypes();
        Vector<String> superClasses = new Vector<String>();
        Iterator<Type> iterator = superTypes.iterator();
        while (iterator.hasNext()) {
            superClasses.add(iterator.next().getQualifiedName());
        }
        return superClasses.toArray(new String[0]);
    }

    public static final String NOGEN = "nogen";

    /**
	 * Determine if this classifier has been tagged with the <<nogen>> stereotype.
	 */
    public boolean isNogen() {
        boolean isNogen = false;
        if (!this.getClassifier().getAppliedStereotypes().isEmpty()) {
            Set stereotypes = this.getClassifier().getAppliedStereotypes();
            Iterator iterator = stereotypes.iterator();
            while (iterator.hasNext() && !isNogen) {
                Stereotype stereotype = (Stereotype) iterator.next();
                isNogen = stereotype.getKeyword().equals(NOGEN);
            }
        }
        return isNogen;
    }

    public void setIsNogen(boolean flag) {
    }

    public void setName(String name) {
    }

    public void setQualifiedName(String name) {
    }

    public void setOperations(OperationAdapter[] operations) {
    }

    public void wrap(Classifier classifier) {
        setClassifier(classifier);
    }

    public UML2ClassifierAdapter(Classifier classifier) {
        super(classifier);
        wrap(classifier);
    }
}
