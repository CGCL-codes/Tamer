package org.argouml.language.java.reveng.classfile;

import org.apache.log4j.Logger;
import org.argouml.model.Facade;
import org.argouml.model.Model;

/**
 * This context is a package.
 *
 * @author Marcus Andersson
 */
class PackageContext extends Context {

    static final Logger LOG = Logger.getLogger(PackageContext.class);

    /** The package this context represents. */
    private Object mPackage;

    /** The java style name of the package. */
    private String javaName;

    /**
       Create a new context from a package.

       @param base Based on this context.
       @param thePackage Represents this package.
    */
    public PackageContext(Context base, Object thePackage) {
        super(base);
        this.mPackage = thePackage;
        javaName = getJavaName(thePackage);
    }

    public Object getInterface(String name) throws ClassifierNotFoundException {
        return get(name, true);
    }

    /**
     * Get a classifier from the model. If it is not in the model, try
     * to find it with the CLASSPATH. If found, in the classpath, the
     * classifier is created and added to the model. If not found at
     * all, a datatype is created and added to the model.
     *
     * @param name The name of the classifier to find.
     * @return Found classifier.
     * @throws ClassifierNotFoundException if classifier couldn't be located
     */
    public Object get(String name) throws ClassifierNotFoundException {
        return get(name, false);
    }

    /**
     * Get a classifier from the model. If it is not in the model, try
     * to find it with the CLASSPATH. If found, in the classpath, the
     * classifier is created and added to the model. If not found at
     * all, a datatype is created and added to the model.
     *
     * @param name The name of the classifier to find.
     * @return Found classifier.
     * @throws ClassifierNotFoundException if classifier couldn't be located
     */
    public Object get(String name, boolean interfacesOnly) throws ClassifierNotFoundException {
        Object mClassifier = Model.getFacade().lookupIn(mPackage, name);
        if (mClassifier == null) {
            Class classifier;
            String clazzName = name;
            if (!Model.getFacade().isAModel(mPackage)) {
                clazzName = javaName + "." + name;
            }
            classifier = findClass(clazzName, interfacesOnly);
            if (classifier != null) {
                if (classifier.isInterface()) {
                    mClassifier = Model.getCoreFactory().buildInterface(name, mPackage);
                } else {
                    mClassifier = Model.getCoreFactory().buildClass(name, mPackage);
                }
                if (mClassifier != null) {
                    setGeneratedTag(mClassifier);
                }
            }
        }
        if (mClassifier == null) {
            if (getContext() != null) {
                mClassifier = getContext().get(name, interfacesOnly);
            } else {
                if (!interfacesOnly && name.equals("int") || name.equals("long") || name.equals("short") || name.equals("byte") || name.equals("char") || name.equals("float") || name.equals("double") || name.equals("boolean") || name.equals("void") || name.indexOf("[]") != -1) {
                    mClassifier = Model.getCoreFactory().buildDataType(name, mPackage);
                }
            }
        }
        if (mClassifier == null) {
            throw new ClassifierNotFoundException(name);
        }
        return mClassifier;
    }

    private static final String GENERATED_TAG_VALUE = "true";

    /**
     * Set the tagged value which indicates this element was
     * generated as a result of reverse engineering.
     *
     * @param element the ModelElement to set the tag on
     */
    private void setGeneratedTag(Object element) {
        Object tv = Model.getFacade().getTaggedValue(element, Facade.GENERATED_TAG);
        if (tv == null) {
            Model.getExtensionMechanismsHelper().addTaggedValue(element, Model.getExtensionMechanismsFactory().buildTaggedValue(Facade.GENERATED_TAG, GENERATED_TAG_VALUE));
        } else {
            Model.getExtensionMechanismsHelper().setValueOfTag(tv, GENERATED_TAG_VALUE);
        }
    }
}
