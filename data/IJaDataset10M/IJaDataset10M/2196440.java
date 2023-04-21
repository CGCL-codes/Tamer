package deduced.javagenerator.controller.packagegen;

import java.util.*;
import deduced.generator.ProjectData;
import deduced.generator.ProjectGenerator;
import deduced.javagenerator.JavaNamingParameters;
import deduced.javagenerator.packagegen.JavaPackageGenerator;

/**
 * ProjectGenerator
 * 
 * @author Duff
 */
public class JavaControllerPackageGenerator extends ProjectGenerator {

    public JavaControllerPackageGenerator() {
    }

    public void generateProject() {
        JavaControllerPackageStructureGenerator javaModelStructureGenerator = new JavaControllerPackageStructureGenerator();
        javaModelStructureGenerator.setProjectGenerator(this);
        javaModelStructureGenerator.generateStep();
        JavaControllerPackageFiller filler = new JavaControllerPackageFiller();
        filler.setProjectGenerator(this);
        filler.setNamingParameters(new JavaNamingParameters());
        filler.generateStep();
    }

    /**
     * (non-Javadoc)
     * 
     * @see deduced.generator.ProjectGenerator#generateFiles()
     */
    public void generateFiles() {
    }

    /**
     * (non-Javadoc)
     * 
     * @see deduced.generator.ProjectGenerator#getDependentGenerators()
     */
    public List getDependentGenerators() {
        List retVal = new ArrayList();
        ProjectData projectData = (ProjectData) getRootObjectConverter().getSourceObject();
        addDependentGenerator(retVal, JavaPackageGenerator.class.getName(), projectData);
        addSelfDependencies(retVal);
        return retVal;
    }

    /**
     * (non-Javadoc)
     * 
     * @see deduced.generator.ProjectGenerator#configureCodeBlockMap(java.util.Map)
     */
    public void configureCodeBlockMap(Map codeBlockMap) {
    }

    /**
     * (non-Javadoc)
     *
     * @see deduced.generator.ProjectGenerator#getDependentFileGenerators()
     */
    public List getDependentFileGenerators() {
        return null;
    }
}
