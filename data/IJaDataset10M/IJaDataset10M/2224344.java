package com.patientis.business.results;

import com.patientis.business.controllers.DefaultCustomFormRecordListController;

/**
 * @author gcaulton
 *
 */
public class ApplicationFormRecordListController extends DefaultCustomFormRecordListController {

    public ApplicationFormRecordListController() {
        super(FormRecordMode.APPLICATION);
    }
}
