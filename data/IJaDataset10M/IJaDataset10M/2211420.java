package biz.ivanov.ant4pde.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicAttribute;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.selectors.AndSelector;
import org.apache.tools.ant.types.selectors.ContainsSelector;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.apache.tools.ant.types.selectors.OrSelector;
import biz.ivanov.ant4pde.EclipseProject;
import biz.ivanov.ant4pde.PdeProjectCollection;

/**
 * @author arcivanov
 */
public class PdeProjectCleaner extends Ant4PdeTask implements DynamicAttribute, DynamicElement {

    private static final OrSelector filesetSelector;

    static {
        filesetSelector = new OrSelector();
        FilenameSelector sel = new FilenameSelector();
        sel.setName("javaCompiler...args");
        filesetSelector.add(sel);
        sel = new FilenameSelector();
        sel.setName("temp.folder/**");
        filesetSelector.add(sel);
        sel = new FilenameSelector();
        sel.setName("@dot/**");
        filesetSelector.add(sel);
        sel = new FilenameSelector();
        sel.setName("@dot*");
        filesetSelector.add(sel);
        AndSelector andSelector = new AndSelector();
        sel = new FilenameSelector();
        sel.setName("build.xml");
        andSelector.add(sel);
        ContainsSelector containsSelector = new ContainsSelector();
        containsSelector.setText("<target name=\"@dot\" depends=\"init\" unless=\"@dot\"");
        andSelector.add(containsSelector);
        containsSelector = new ContainsSelector();
        containsSelector.setText("<antcall target=\"@dot\"/>");
        andSelector.add(containsSelector);
        filesetSelector.add(andSelector);
        andSelector = new AndSelector();
        sel = new FilenameSelector();
        sel.setName("build.xml");
        andSelector.add(sel);
        containsSelector = new ContainsSelector();
        containsSelector.setText("<eclipse.idReplacer featureFilePath=");
        andSelector.add(containsSelector);
        containsSelector = new ContainsSelector();
        containsSelector.setText("<antcall target=\"gather.bin.parts\">");
        andSelector.add(containsSelector);
        filesetSelector.add(andSelector);
    }

    private PdeProjectCollection pdeProjects;

    @Override
    public Object createDynamicElement(String name) throws BuildException {
        if ("pde-projects".equals(name)) {
            return pdeProjects = new PdeProjects();
        }
        if ("feature-projects".equals(name)) {
            return this.pdeProjects = new FeatureReferencedProjects();
        }
        return null;
    }

    @Override
    public void execute() throws BuildException {
        if (pdeProjects == null) {
            throw new BuildException("must contain one PDE project collection", getLocation());
        }
        Delete delete = (Delete) getProject().createTask("delete");
        delete.setIncludeEmptyDirs(true);
        for (EclipseProject project : pdeProjects) {
            FileSet fs = (FileSet) getProject().createDataType("fileset");
            fs.setDir(project.getProjectDirectory());
            fs.addOr(filesetSelector);
            delete.add(fs);
        }
        delete.execute();
    }

    @Override
    public void setDynamicAttribute(String name, String value) throws BuildException {
        throw new BuildException("[" + getTaskName() + "] does not support the '" + name + "' attribute");
    }
}
