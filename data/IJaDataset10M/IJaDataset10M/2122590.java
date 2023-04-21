package com.genia.toolbox.uml_generator.model.impl;

import com.genia.toolbox.basics.exception.BundledException;
import com.genia.toolbox.uml_generator.visitor.ModelVisitor;

/**
 * this class represent a class defined in UML.
 */
public class ClassModel extends AbstractClassModelImpl {

    /**
   * returns wether this object is an interface.
   * 
   * @return wether this object is an interface
   */
    @Override
    public boolean isInterface() {
        return false;
    }

    /**
   * the actual visitor method.
   * 
   * @param modelVisitor
   *          the visitor to use
   * @throws BundledException
   *           when an error occurred
   * @see com.genia.toolbox.uml_generator.visitor.Visitable#visit(com.genia.toolbox.uml_generator.visitor.ModelVisitor)
   */
    public void visit(ModelVisitor modelVisitor) throws BundledException {
        modelVisitor.visitClassModel(this);
    }
}
