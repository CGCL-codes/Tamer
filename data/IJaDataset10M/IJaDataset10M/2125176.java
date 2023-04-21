package com.google.code.gronono.gps.ui.panels.pages;

import com.google.code.gronono.commons.i18n.BundleKey;
import com.google.code.gronono.commons.i18n.BundleName;
import com.google.code.gronono.gps.ui.controller.GPSController;

/**
 * Panneau des résultats de tri.
 */
@BundleName(value = "com.google.code.gronono.gps.gui")
public class SortPanel extends AbstractResultPanel {

    /** serialVersionUID. */
    private static final long serialVersionUID = 14138336862378906L;

    /** Titre de la page. */
    @BundleKey(value = "sort.page.title")
    private static String TITLE;

    /** Description de la page. */
    @BundleKey(value = "sort.page.description")
    private static String DESCR;

    /**
	 * Constructeur.
	 * @param controller Le contrôleur de l'application.
	 */
    public SortPanel(final GPSController controller) {
        super(controller, TITLE, DESCR);
    }
}
