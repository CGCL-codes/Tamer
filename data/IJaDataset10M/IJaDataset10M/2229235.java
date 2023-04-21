package spooky.model.web;

import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;
import spooky.config.*;
import spooky.control.event.*;
import spooky.debug.*;

/**
 *
 * @author  Erik Karlsson
 * @version 
 */
public class ModelManager implements java.io.Serializable {

    public static String CONTEXT_NAME = "model_manager";

    private ServletContext servletContext;

    private HttpSession httpSession;

    /** Creates new ModelManager */
    public ModelManager(ServletContext servletContext, HttpSession httpSession) {
        this.servletContext = servletContext;
        this.httpSession = httpSession;
    }

    /**
     * Returns the model mappings
     *
     * @return the model mappings
     */
    public HashMap getModelMappings() {
        Configurations configurations = (Configurations) servletContext.getAttribute(Configurations.CONTEXT_NAME);
        return configurations.getModelMappings();
    }

    public ModelCollection getSessionModels() {
        ModelCollection theCollection = (ModelCollection) httpSession.getAttribute((String) (ModelCollection.CONTEXT_NAME + "-session"));
        if (theCollection == null) {
            theCollection = new ModelCollection();
            httpSession.setAttribute((String) (ModelCollection.CONTEXT_NAME + "-session"), theCollection);
        }
        return theCollection;
    }

    public ModelCollection getContextModels() {
        ModelCollection theCollection = (ModelCollection) httpSession.getAttribute((String) (ModelCollection.CONTEXT_NAME + "-application"));
        if (theCollection == null) {
            theCollection = new ModelCollection();
            httpSession.setAttribute((String) (ModelCollection.CONTEXT_NAME + "-application"), theCollection);
        }
        return theCollection;
    }

    /**
     *  Returns specific model within the scope. If no model
     *  is found it will be created. If the creation fails
     *  an null value is returned.
     *
     *  @param nameOfTheModel The name of the model
     *  @param scope    scope: <code>session</code> or <code>application</code>
     */
    public Object getModel(String nameOfTheModel, String scope) {
        try {
            SimpleTrace.printMessage("ModelManager::getModel()");
            HashMap modelConfigurations = getModelMappings();
            ModelConfig modelConfig = (ModelConfig) modelConfigurations.get(nameOfTheModel);
            ModelCollection theCollection = null;
            Object obj = null;
            SimpleTrace.printMessage("ModelManager::getModel()::check the scope:" + scope);
            if (scope.equals("session")) {
                theCollection = getSessionModels();
            } else if (scope.equals("application")) {
                theCollection = getContextModels();
            }
            if (theCollection.containsKey(nameOfTheModel)) {
                obj = theCollection.getModelAsObject(nameOfTheModel);
            } else {
                WebModel theModel = (WebModel) getClass().getClassLoader().loadClass(modelConfig.getModelClass()).newInstance();
                theCollection.addModel(nameOfTheModel, theModel);
                obj = (Object) theModel;
            }
            return obj;
        } catch (Exception e) {
            System.err.println("ModelManager::getModel::" + e.toString());
        }
        return null;
    }

    private WebModel getWebModel(String nameOfTheModel, ModelCollection theCollection, ModelConfig modelConfig) throws ClassNotFoundException {
        try {
            if (!theCollection.containsKey(nameOfTheModel)) {
                WebModel theModel = (WebModel) getClass().getClassLoader().loadClass(modelConfig.getModelClass()).newInstance();
                theCollection.addModel(nameOfTheModel, theModel);
                System.err.println("getWebModel::" + nameOfTheModel);
            }
            WebModel webModel = theCollection.getModel(nameOfTheModel);
            return webModel;
        } catch (ClassNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ClassNotFoundException("unspecified");
        }
    }

    /**
     *  This method is called by the request processer. The parameters
     *  contains information of modified models. This is importan!
     *
     */
    public void notify(EventResponse eventResponse) throws WebModelException {
        try {
            SimpleTrace.printMessage("ModelManager::Notify()");
            ModelCollection sessionModels = getSessionModels();
            ModelCollection contextModels = getContextModels();
            HashMap modelConfigurations = getModelMappings();
            Iterator modifiedModelsIterator = eventResponse.getUpdatedModels().iterator();
            while (modifiedModelsIterator.hasNext()) {
                String nameOfTheModel = (String) modifiedModelsIterator.next();
                SimpleTrace.printMessage("ModelManager::Notify()::Modified model::" + nameOfTheModel);
                if (modelConfigurations.containsKey(nameOfTheModel)) {
                    ModelConfig modelConfig = (ModelConfig) modelConfigurations.get(nameOfTheModel);
                    String scope = modelConfig.getScope();
                    if (scope.equals("session")) {
                        WebModel webModel = getWebModel(nameOfTheModel, sessionModels, modelConfig);
                        webModel.update(eventResponse.getAttribute(nameOfTheModel));
                    } else if (scope.equals("application")) {
                        WebModel webModel = getWebModel(nameOfTheModel, contextModels, modelConfig);
                        webModel.update(eventResponse.getAttribute(nameOfTheModel));
                    } else if (scope.equals("request")) {
                        WebModel webModel = getWebModel(nameOfTheModel, contextModels, modelConfig);
                        webModel.update(eventResponse.getAttribute(nameOfTheModel));
                    } else {
                        throw new WebModelException("Model " + nameOfTheModel + " scope is wrong, check the spelling please.");
                    }
                } else {
                    throw new WebModelException("The requested model doesn't exist");
                }
            }
        } catch (WebModelException wme) {
            throw wme;
        } catch (ClassNotFoundException cnfe) {
            throw new WebModelException("class was not found: " + cnfe.getMessage());
        } catch (Exception e) {
            throw new WebModelException(e.toString());
        }
    }
}
