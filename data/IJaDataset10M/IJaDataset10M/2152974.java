package org.coode.browser.protege;

import org.apache.log4j.Logger;
import org.coode.html.OWLHTMLKit;
import org.coode.html.OntologyExporter;
import org.coode.html.impl.OWLHTMLKitImpl;
import org.coode.owl.mngr.OWLServer;
import org.protege.editor.core.ProtegeApplication;
import org.protege.editor.core.ui.progress.BackgroundTask;
import org.protege.editor.core.ui.util.NativeBrowserLauncher;
import org.protege.editor.core.ui.util.UIUtil;
import org.protege.editor.owl.ui.action.ProtegeOWLAction;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author: Nick Drummond<br>
 *
 * http://www.cs.man.ac.uk/~drummond<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Jun 11, 2007<br><br>
 * <p/>
 */
public class ExportOWLDocAction extends ProtegeOWLAction {

    private static URL DEFAULT_BASE;

    static {
        try {
            DEFAULT_BASE = new URL("http://www.co-ode.org/");
        } catch (MalformedURLException e) {
            Logger.getLogger(ProtegeServerImpl.class).error(e);
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        final File folder = UIUtil.chooseFolder(getOWLWorkspace(), "Select a base for OWLDoc");
        if (folder != null) {
            final BackgroundTask exportTask = ProtegeApplication.getBackgroundTaskManager().startTask("Exporting OWLDoc");
            Runnable export = new Runnable() {

                public void run() {
                    try {
                        OWLServer svr = new ProtegeServerImpl(getOWLModelManager());
                        OWLHTMLKit owlhtmlKit = new OWLHTMLKitImpl("owldoc-kit", svr, DEFAULT_BASE);
                        OntologyExporter exporter = new OntologyExporter(owlhtmlKit);
                        File index = exporter.export(folder);
                        ProtegeApplication.getBackgroundTaskManager().endTask(exportTask);
                        NativeBrowserLauncher.openURL("file://" + index.getPath());
                        svr.dispose();
                    } catch (Throwable e) {
                        ProtegeApplication.getErrorLog().handleError(Thread.currentThread(), e);
                    }
                }
            };
            Thread exportThread = new Thread(export, "Export OWLDoc");
            exportThread.start();
        }
    }

    public void initialise() throws Exception {
    }

    public void dispose() {
    }
}
