package tudresden.ocl20.pivot.modelbus.ui.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;
import tudresden.ocl20.pivot.modelbus.IModel;
import tudresden.ocl20.pivot.modelbus.IModelInstance;
import tudresden.ocl20.pivot.modelbus.IModelRegistry;
import tudresden.ocl20.pivot.modelbus.ModelAccessException;
import tudresden.ocl20.pivot.modelbus.ModelBusPlugin;

/**
 * 
 * 
 * @author Ronny Brandt
 * @version 1.0 31.08.2007
 */
public class LoadModelInstanceDelegate extends ActionDelegate implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    public void run(IAction action) {
        IModelRegistry registry = ModelBusPlugin.getModelRegistry();
        IModel[] models = registry.getModels();
        IModel umlexample = null, pmlexample = null;
        Iterator<IModel> it = Arrays.asList(models).iterator();
        while (it.hasNext()) {
            IModel m = it.next();
            if (m.getDisplayName().endsWith("UML-Beispiel.xmi")) umlexample = m;
            if (m.getDisplayName().endsWith("pml.ecore")) pmlexample = m;
        }
        URL miUrl = null;
        try {
            miUrl = new URL("file:/D:/Dokumente und Einstellungen/Ronny Brandt/Eclipse/workspaceDA1/tudresden.ocl20.pivot.examples.living/src/tudresden/ocl20/pivot/examples/living/ModelProviderClass.java");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        IModelInstance umlexampleInstance = null;
        try {
            umlexampleInstance = umlexample.getModelInstanceProvider().getModelInstance(miUrl);
            ModelBusPlugin.getModelInstanceRegistry().addModelInstance(umlexample, umlexampleInstance);
        } catch (ModelAccessException e) {
            e.printStackTrace();
        }
        ModelBusPlugin.getModelInstanceRegistry().setActiveModelInstance(umlexample, umlexampleInstance);
        IModelInstance pmlexampleInstance = null;
        URI modelURI = null;
        URL modelURL = null;
        try {
            modelURL = FileLocator.resolve(FileLocator.find(Platform.getBundle("tudresden.ocl20.pivot.examples.pml"), new Path("model/My.pml"), null));
            modelURI = URI.createURI(modelURL.toString());
        } catch (Exception e) {
        }
        try {
            pmlexampleInstance = pmlexample.getModelInstanceProvider().getModelInstance(modelURL);
            ModelBusPlugin.getModelInstanceRegistry().addModelInstance(pmlexample, pmlexampleInstance);
        } catch (ModelAccessException e1) {
            e1.printStackTrace();
        }
        IModelInstance pmlexampleInstanceETS = null;
        try {
            modelURL = FileLocator.resolve(FileLocator.find(Platform.getBundle("tudresden.ocl20.pivot.examples.pml"), new Path("model/ExpressionTestSuite.pml"), null));
            modelURI = URI.createURI(modelURL.toString());
        } catch (Exception e) {
        }
        try {
            pmlexampleInstanceETS = pmlexample.getModelInstanceProvider().getModelInstance(modelURL);
            ModelBusPlugin.getModelInstanceRegistry().addModelInstance(pmlexample, pmlexampleInstanceETS);
        } catch (ModelAccessException e1) {
            e1.printStackTrace();
        }
        IModelInstance pmlexampleInstanceCT = null;
        try {
            modelURL = FileLocator.resolve(FileLocator.find(Platform.getBundle("tudresden.ocl20.pivot.examples.pml"), new Path("model/CachingTest.pml"), null));
            modelURI = URI.createURI(modelURL.toString());
        } catch (Exception e) {
        }
        try {
            pmlexampleInstanceCT = pmlexample.getModelInstanceProvider().getModelInstance(modelURL);
            ModelBusPlugin.getModelInstanceRegistry().addModelInstance(pmlexample, pmlexampleInstanceCT);
        } catch (ModelAccessException e1) {
            e1.printStackTrace();
        }
        ModelBusPlugin.getModelInstanceRegistry().setActiveModelInstance(pmlexample, pmlexampleInstance);
    }
}
