package com.google.appengine.eclipse.datatools.ui;

import com.google.appengine.eclipse.datatools.utils.DatatoolsUtils;
import org.eclipse.datatools.connectivity.ui.wizards.ExtensibleProfileDetailsWizardPage;

/**
 * The Google Cloud SQL Wizard page.
 */
public class GoogleSqlProfileDetailsWizardPage extends ExtensibleProfileDetailsWizardPage {

    public GoogleSqlProfileDetailsWizardPage(String pageName) {
        super(pageName, DatatoolsUtils.GOOGLE_SQL_DRIVER_CATEGORY_ID);
    }
}
