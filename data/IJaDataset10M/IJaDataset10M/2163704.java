package com.hack23.cia.web.controller.viewfactory.api;

import com.hack23.cia.model.application.ActionEvent;
import com.hack23.cia.web.action.ControllerAction;

/**
 * The Class UrlModelAndView.
 */
public class UrlModelAndView extends AbstractUserModelAndView {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The title.
     */
    private final String title;

    /**
     * The url.
     */
    private final String url;

    /**
     * Instantiates a new url model and view.
     * 
     * @param controllerAction the controller action
     * @param simpleActionEvent the simple action event
     * @param viewSpecification the view specification
     * @param title the title
     * @param url the url
     */
    public UrlModelAndView(final ControllerAction controllerAction, final ActionEvent simpleActionEvent, final ViewSpecification viewSpecification, final String title, final String url) {
        super(controllerAction, simpleActionEvent, viewSpecification);
        this.title = title;
        this.url = url;
    }

    /**
     * Gets the title.
     * 
     * @return the title
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Gets the url.
     * 
     * @return the url
     */
    public final String getUrl() {
        return url;
    }
}
