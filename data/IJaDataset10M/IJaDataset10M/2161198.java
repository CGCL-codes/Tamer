package net.sourceforge.coffea.actors;

import java.io.File;
import net.sourceforge.coffea.actors.abilities.CodeWorking;
import net.sourceforge.coffea.actors.abilities.ModelBuilding;
import net.sourceforge.coffea.tools.ModelHandler;
import net.sourceforge.coffea.tools.capacities.IModelHandling;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Links an <a href="http://www.omg.org/technology/documents/formal/uml.htm">
 * <em>UML</em></a> model processed via the <a href="
 * http://www.eclipse.org/modeling/mdt/">MDT</a> to a <a href="
 * http://www.java.net"><em>Java</em></a> project
 * @see CodeWorking
 * @see JavaCodeWorker
 * @see UMLModelWatcher
 */
public class CodeModelWorker implements ModelBuilding {

    /**
	 * Project name, when a file is parsed takes the file name
	 * @see #parse(File)
	 */
    protected String coffeeName;

    /** Model */
    protected IModelHandling model;

    /** Code side worker */
    protected JavaCodeWorker javaWorker;

    /** Model resource (only once saved and before any other parsing) */
    protected IResource modelResource;

    /** Workbench window */
    protected IWorkbenchWindow workbenchWindow;

    /** Boolean value indicating if the worker is used in an edition */
    protected boolean editing;

    /** 
	 * Coffee worker construction
	 * @param w
	 * Value of {@link #workbenchWindow}
	 * @param e
	 * Value of {@link #editing}
	 */
    public CodeModelWorker(IWorkbenchWindow w, boolean e) {
        coffeeName = "default";
        workbenchWindow = w;
        editing = e;
    }

    /** Coffee worker initialization */
    public void init() {
        if ((javaWorker == null)) {
            javaWorker = new JavaCodeWorker();
            javaWorker.init(this);
        }
    }

    /**
	 * Returns {@link #workbenchWindow}
	 * @return Value of {@link #workbenchWindow}
	 */
    public IWorkbenchWindow getWorkbenchWindow() {
        return workbenchWindow;
    }

    public IModelHandling parse(File target) {
        init();
        if (target != null) {
            if (getCoffeeName() == null) {
                this.setCoffeeName(target.getName());
            }
            model = new ModelHandler(this);
            javaWorker.parse(target);
        }
        return model;
    }

    public IModelHandling getModelHandler() {
        init();
        return model;
    }

    public void save(String uri, String name) {
        init();
        model.setUpUMLModelElement();
        model.setName(name);
        model.createModelFile(uri);
    }

    /**
	 * Returns {@link #coffeeName}
	 * @return
	 * 	Value of {@link #coffeeName}
	 */
    public String getCoffeeName() {
        return coffeeName;
    }

    /**
	 * Sets {@link #coffeeName}
	 * @param n
	 * 	Value of {@link #coffeeName}
	 */
    public void setCoffeeName(String n) {
        coffeeName = n;
    }

    /**
	 * Returns {@link #javaWorker}
	 * @return
	 * 	Value of {@link #javaWorker}
	 */
    public CodeWorking getJavaWorker() {
        return javaWorker;
    }

    /**
	 * Sets {@link #javaWorker}
	 * @param j
	 * 	Value of {@link #javaWorker}
	 */
    public void setJavaWorker(JavaCodeWorker j) {
        javaWorker = j;
    }

    /**
	 * Returns {@link #editing}
	 * @return Value of {@link #editing}
	 */
    public boolean isEditing() {
        return editing;
    }
}
