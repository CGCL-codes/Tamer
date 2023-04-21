package java2uml;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;

/**
   This environment is a package.
*/
class PackageEnvironment extends Environment {

    /** The package this environment represents. */
    private MPackage mPackage;

    /** 
	Create a new environment from a package.
	
	@param base Based on this environment.
	@param mPackage Represents this package.
    */
    public PackageEnvironment(Environment base, MPackage mPackage) {
        super(base);
        this.mPackage = mPackage;
    }

    /**
       Get a classifier from the model. If it is not in the model, try
       to find it with the CLASSPATH. If found, in the classpath, the
       classifier is created and added to the model. If not found at
       all, a datatype is created and added to the model.

       @param classifierName The name of the classifier to find.
       @returns Found classifier.
    */
    public MClassifier get(String classifierName) {
        MClassifier mClassifier = (MClassifier) mPackage.lookup(classifierName);
        if (mClassifier == null) {
            try {
                Class classifier;
                if (mPackage instanceof MModelImpl) {
                    classifier = Class.forName(classifierName);
                } else {
                    classifier = Class.forName(mPackage.getName() + "." + classifierName);
                }
                if (classifier.isInterface()) {
                    mClassifier = new MInterfaceImpl();
                } else {
                    mClassifier = new MClassImpl();
                }
                mClassifier.setName(classifierName);
                mClassifier.setNamespace(mPackage);
            } catch (Exception e) {
                if (environment != null) {
                    mClassifier = environment.get(classifierName);
                } else {
                    if (classifierName.equals("int") || classifierName.equals("long") || classifierName.equals("short") || classifierName.equals("byte") || classifierName.equals("char") || classifierName.equals("float") || classifierName.equals("double") || classifierName.equals("boolean") || classifierName.equals("void") || classifierName.indexOf("[]") != -1) {
                        mClassifier = new MDataTypeImpl();
                        mClassifier.setName(classifierName);
                        mClassifier.setNamespace(mPackage);
                    } else {
                        mClassifier = null;
                    }
                }
            }
        }
        return mClassifier;
    }
}
