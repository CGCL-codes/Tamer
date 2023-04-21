package wilos.presentation.web.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import wilos.presentation.web.template.MenuBean;
import wilos.presentation.web.template.PageContentBean;
import wilos.resources.LocaleBean;
import wilos.utils.Constantes.State;

public class WebCommonService {

    /**
	 * Gets a bean thanks to its name
	 * @param _beanName the name of the bean
	 * @return the expected bean
	 */
    public static Object getBean(String _beanName) {
        return getCurrentFacesContext().getApplication().getVariableResolver().resolveVariable(getCurrentFacesContext(), _beanName);
    }

    /**
	 * Changes the content of the page 
	 * @param _pageUrl the new url of the new page to display
	 */
    public static void changeContentPage(String _pageUrl) {
        getMenuBean().changePage(_pageUrl);
    }

    /**
	 * Gets the currently selected panel
	 * @return the selected panel
	 */
    public static PageContentBean getSelectedPanel() {
        return getMenuBean().getSelectedPanel();
    }

    /**
	 * Displays a information message
	 * @param _message the message to display
	 */
    public static void addInfoMessage(String _message) {
        FacesMessage message = new FacesMessage();
        message.setSummary(_message);
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        getCurrentFacesContext().addMessage(null, message);
    }

    /**
	 * Displays an error message
	 * @param _message the message to display
	 */
    public static void addErrorMessage(String _message) {
        FacesMessage message = new FacesMessage();
        message.setSummary(_message);
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        getCurrentFacesContext().addMessage(null, message);
    }

    /**
	 * Gets the state currently displayed
	 * @param _state the current state
	 * @return the state to display
	 */
    public static String getDisplayedState(String _state) {
        String stateInString = "";
        if (_state.equals(State.CREATED)) {
            stateInString = LocaleBean.getText("constantes.state.created");
        } else if (_state.equals(State.READY)) {
            stateInString = LocaleBean.getText("constantes.state.ready");
        } else if (_state.equals(State.STARTED)) {
            stateInString = LocaleBean.getText("constantes.state.started");
        } else if (_state.equals(State.SUSPENDED)) {
            stateInString = LocaleBean.getText("constantes.state.suspended");
        } else if (_state.equals(State.FINISHED)) {
            stateInString = LocaleBean.getText("constantes.state.finished");
        }
        return stateInString;
    }

    /**
	 * Gets the state currently displayed fo a milestone
	 * @param _state the current state
	 * @return the state to display
	 */
    public static String getDisplayedStateForMilestone(String _state) {
        String stateInString = "";
        if (_state.equals(State.CREATED)) {
            stateInString = LocaleBean.getText("constantes.state.created.ms");
        } else if (_state.equals(State.READY)) {
            stateInString = LocaleBean.getText("constantes.state.ready.ms");
        } else if (_state.equals(State.FINISHED)) {
            stateInString = LocaleBean.getText("constantes.state.finished.ms");
        }
        return stateInString;
    }

    /**
	 * Gets the state currently displayed for a work product
	 * @param _state the current state
	 * @return the state to display
	 */
    public static String getDisplayedStateForWorkProduct(String _state) {
        String stateInString = "";
        if (_state.equals(State.CREATED)) {
            stateInString = LocaleBean.getText("constantes.state.created.wp");
        } else if (_state.equals(State.READY)) {
            stateInString = LocaleBean.getText("constantes.state.ready.wp");
        } else {
            stateInString = _state;
        }
        return stateInString;
    }

    /**
	 * Gets the current faces context
	 * @return the faces context
	 */
    private static FacesContext getCurrentFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    /**
	 * Gets the menu bean
	 * @return the menu bean
	 */
    private static MenuBean getMenuBean() {
        MenuBean menuBean = (MenuBean) getCurrentFacesContext().getApplication().createValueBinding("#{menu}").getValue(getCurrentFacesContext());
        return menuBean;
    }
}
