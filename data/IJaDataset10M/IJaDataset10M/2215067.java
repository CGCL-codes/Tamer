package com.qasystems.qstudio.java.integration.jdeveloper;

import javax.swing.*;
import oracle.ide.layout.ViewId;
import oracle.ide.log.AbstractLogPage;
import oracle.ide.log.LogPage;
import oracle.ide.log.LogManager;
import oracle.ide.Ide;

/**
 * This class represents one category (each project has its own tab
 * which contains an observation table). Each log page is registered
 * by JDeveloper.
 */
public class QStudioLogPage extends AbstractLogPage {

    private JDevObservationControl observationView;

    QStudioLogPage(ViewId id) {
        super(id);
    }

    /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
    public JDevObservationControl getObservationControl() {
        return (observationView);
    }

    /**
   * Add the QStudioLogPage.
   *
   * Either create the logpage and add it to the LogWindow or return the
   * already existing logpage.
   *
   * @return the logPage
   */
    public java.awt.Component getGUI() {
        JPanel logPage = null;
        final LogPage[] pages = LogManager.getLogManager().getPages();
        for (int i = 0; i < pages.length; i++) {
            if (pages[i] instanceof QStudioLogPage) {
                logPage = (JPanel) ((QStudioLogPage) pages[i]).getObservationControl().getObservationPane();
                observationView = ((QStudioLogPage) pages[i]).getObservationControl();
            }
        }
        if (logPage == null) {
            observationView = new JDevObservationControl();
            logPage = (JPanel) observationView.getObservationPane();
        }
        return (logPage);
    }
}
