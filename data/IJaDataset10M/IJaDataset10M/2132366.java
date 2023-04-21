package mecca.sis.payment;

import mecca.portal.action.AbstractPanelModule;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1
 */
public class MainPanelModule extends AbstractPanelModule {

    public MainPanelModule() {
        templateName = "vtl/sis/payment/main_window.vm";
        menuFilename = "/sis/payment/paymentMenu.xml";
        submitFilename = "/sis/payment/paymentSubmit.xml";
        actionFilename = "/sis/payment/paymentAction.xml";
    }
}
