package com.hack23.cia.web.impl.ui.viewfactory.api.common;

import java.io.Serializable;
import com.hack23.cia.model.api.dto.application.UserSessionDto;
import com.hack23.cia.web.api.common.ControllerAction;

/**
 * The Class AbstractModelAndView.
 */
public abstract class AbstractModelAndView implements ModelAndView, Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The controller action. */
    private final ControllerAction controllerAction;

    /** The user session dto. */
    private final UserSessionDto userSessionDTO;

    /**
     * Instantiates a new abstract model and view.
     * 
     * @param userSessionDTO the user session dto
     * @param controllerAction the controller action
     */
    public AbstractModelAndView(final UserSessionDto userSessionDTO, final ControllerAction controllerAction) {
        super();
        this.userSessionDTO = userSessionDTO;
        this.controllerAction = controllerAction;
    }

    public final ControllerAction getControllerAction() {
        return controllerAction;
    }

    public final UserSessionDto getUserSessionDTO() {
        return userSessionDTO;
    }

    @Override
    public abstract String getViewSpecificationDescription();
}
