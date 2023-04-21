package tudresden.ocl20.pivot.ocl2parser.testcasegenerator.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import tudresden.ocl20.pivot.modelbus.IMetamodel;
import tudresden.ocl20.pivot.modelbus.IMetamodelRegistry;
import tudresden.ocl20.pivot.modelbus.IModel;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.abstractsyntax.IGenModelFactory;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.abstractsyntax.IVariable;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.testcasegenerator.InvalidArgumentException;

public interface IEnvironment {

    public IModel getModel();

    public IMetamodelRegistry getMetamodelReg();

    public void setMetamodel(IMetamodel metamodel);

    public IMetamodel getMetamodel();

    public void setModel(IModel model);

    public List<IVariable> getVariables();

    public void setVariables(List<IVariable> var);

    public IVariable lookupVariable(IVariable var);

    public IEnvironment nestedEnvironment();

    public List<FilenameContainer> lookupTestElement(File file, String testName) throws InvalidArgumentException;

    public FilenameContainer getTestElement();

    public void setTestElement(FilenameContainer testElement);

    public IGenModelFactory getFactory();

    public HashMap getTemplateHashMap();
}
