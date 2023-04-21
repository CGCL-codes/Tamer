package com.hack23.cia.web.controller.viewfactory.api;

import com.hack23.cia.model.application.ActionEvent;
import com.hack23.cia.model.sweden.CommitteeReport;
import com.hack23.cia.web.action.ControllerAction;

/**
 * The Class CommitteeReportModelAndView.
 */
public class CommitteeReportModelAndView extends AbstractUserModelAndView {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The committee report.
     */
    private final CommitteeReport committeeReport;

    /**
     * Instantiates a new committee report model and view.
     * 
     * @param controllerAction the controller action
     * @param simpleActionEvent the simple action event
     * @param viewSpecification the view specification
     * @param committeeReport the committee report
     */
    public CommitteeReportModelAndView(final ControllerAction controllerAction, final ActionEvent simpleActionEvent, final ViewSpecification viewSpecification, final CommitteeReport committeeReport) {
        super(controllerAction, simpleActionEvent, viewSpecification);
        this.committeeReport = committeeReport;
    }

    /**
     * Gets the committee report.
     * 
     * @return the committee report
     */
    public final CommitteeReport getCommitteeReport() {
        return committeeReport;
    }
}
