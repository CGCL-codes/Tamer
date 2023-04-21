package de.mse.mogwai.swingframework.controller;

import java.util.HashMap;
import de.mse.mogwai.swingframework.component.ComponentBean;
import de.mse.mogwai.swingframework.exception.ComponentInstantiationException;
import de.mse.mogwai.swingframework.exception.FrameworkRuntimeException;
import de.mse.mogwai.swingframework.exception.MappingException;
import de.mse.mogwai.swingframework.message.Message;
import de.mse.mogwai.swingframework.service.NavigationService;

/**
 * A controller that implements navigation features.
 * 
 * The controller relies on a navigation service. This service
 * is responsible for finding, creating and updating of the right instance.
 * 
 * @author Mirko Sertic
 */
public class NavigatableController extends Controller {

    private NavigationService m_navigationService;

    private ComponentBean m_homeComponent;

    private static final String INVOKED_INSTANCE = "INVOKED_INSTANCE";

    public NavigatableController() {
    }

    protected Object getCurrentModel() throws FrameworkRuntimeException {
        return this.getModelAdapter().getModel();
    }

    protected void setCurrentModel(Object model) throws FrameworkRuntimeException {
        this.getModelAdapter().setModel(model);
    }

    /**
     * Initialize method.
     * 
     * This method initializes the controller.
     * It tries to lookup the navigationService and also sets up the home navigation functionality.
     * 
     * @param params
     */
    public void initialize(HashMap params) {
        super.initialize(params);
        this.m_homeComponent = (ComponentBean) params.get(NavigatableController.INVOKED_INSTANCE);
        ((java.awt.Component) this.getViewAdapter().getViewMemberByName("BackButton")).setEnabled(this.m_homeComponent != null);
    }

    /**
     * Set the navigation service.
     * 
     * @param service
     */
    public void setNavigationService(NavigationService service) {
        this.m_navigationService = service;
    }

    /**
	 * Forward control to a component.
	 * 
	 * Navigation backwards is enabled.
	 * 
	 * @param forwardName
	 * @return
	 * @throws MappingException
	 * @throws ComponentInstantiationException
	 * @throws FrameworkRuntimeException
	 */
    public ComponentBean forwardControlToForLookup(String forwardName) throws MappingException, ComponentInstantiationException, FrameworkRuntimeException {
        return this.forwardControlToForLookup(forwardName, new HashMap());
    }

    /**
	 * Forward control to a component.
	 * 
	 * Navigation backwards is enabled.
	 * 
	 * @param forwardName
	 * @param params
	 * @return
	 * @throws MappingException
	 * @throws ComponentInstantiationException
	 * @throws FrameworkRuntimeException
	 */
    public ComponentBean forwardControlToForLookup(String forwardName, HashMap params) throws MappingException, ComponentInstantiationException, FrameworkRuntimeException {
        params.put(INVOKED_INSTANCE, this.getComponent());
        return this.forwardControlTo(forwardName, params);
    }

    /**
	 * Navigate to the previous object.
	 * If no object could be found or none is selected, a new enmpty model instance is created.
	 * 
	 * @param message
	 * @throws FrameworkRuntimeException
	 */
    public void doPriorButtonActionPerformed(Message message) throws FrameworkRuntimeException {
        try {
            Object model = this.m_navigationService.findPreviousBean(this.getCurrentModel());
            if (model == null) {
                this.doFirstButtonActionPerformed(message);
            } else {
                this.setCurrentModel(model);
            }
        } catch (Exception e) {
            this.doFirstButtonActionPerformed(message);
        }
    }

    /**
	 * Navigate to the next object.
	 * If no object could be found or none is selected, a new enmpty model instance is created.
	 * 
	 * @param message
	 * @throws FrameworkRuntimeException
	 */
    public void doNextButtonActionPerformed(Message message) throws FrameworkRuntimeException {
        try {
            Object model = this.m_navigationService.findNextBean(this.getCurrentModel());
            if (model == null) {
                this.doLastButtonActionPerformed(message);
            } else {
                this.setCurrentModel(model);
            }
        } catch (Exception e) {
            this.doLastButtonActionPerformed(message);
        }
    }

    /**
	 * Navigate to the first object.
	 * If no object could be found or none is selected, a new enmpty model instance is created.
	 * 
	 * @param message
	 * @throws FrameworkRuntimeException
	 */
    public void doFirstButtonActionPerformed(Message message) throws FrameworkRuntimeException {
        Object model = this.m_navigationService.findFirstBeanOfClass(this.getModelAdapter().getModelClass().getName());
        if (model == null) model = this.getModelAdapter().createNewModel();
        this.setCurrentModel(model);
    }

    /**
	 * Navigate to the last object.
	 * If no object could be found or none is selected, a new enmpty model instance is created.
	 * 
	 * @param message
	 * @throws FrameworkRuntimeException
	 */
    public void doLastButtonActionPerformed(Message message) throws FrameworkRuntimeException {
        Object model = this.m_navigationService.findLastBeanOfClass(this.getModelAdapter().getModelClass().getName());
        if (model == null) model = this.getModelAdapter().createNewModel();
        ;
        this.setCurrentModel(model);
    }

    /**
	 * Save the current selected object.
	 * 
	 * @param message
	 * @throws FrameworkRuntimeException
	 */
    public void doSaveButtonActionPerformed(Message message) throws FrameworkRuntimeException {
        this.m_navigationService.update(this.getCurrentModel());
    }

    /**
	 * Delete the current selected object.
	 * 
	 * @param message
	 * @throws FrameworkRuntimeException
	 */
    public void doDeleteButtonActionPerformed(Message message) throws FrameworkRuntimeException {
        this.m_navigationService.delete(this.getCurrentModel());
        this.doPriorButtonActionPerformed(message);
    }

    /**
	 * Create a new object of given type.
	 * 
	 * @param message
	 * @throws FrameworkRuntimeException
	 */
    public void doNewButtonActionPerformed(Message message) throws FrameworkRuntimeException {
        Object model = this.m_navigationService.create(this.getModelAdapter().getModelClass().getName());
        if (model == null) model = this.getModelAdapter().createNewModel();
        ;
        this.setCurrentModel(model);
    }

    /**
	 * Navigate back.
	 * 
	 * If the component was created by a forward, the user can navigate back to the 
	 * origin component using the back functionality.
	 * 
	 * @param message
	 */
    public void doBackButtonActionPerformed(Message message) {
        try {
            this.getComponent().destroy();
            this.m_homeComponent.reinitialize();
        } catch (Exception e) {
        }
    }
}
