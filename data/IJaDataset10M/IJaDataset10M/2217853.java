package net.esle.sinadura.gui.sections.main.events;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import net.esle.sinadura.gui.application.DesktopHelper;
import net.esle.sinadura.gui.application.LanguageResource;
import net.esle.sinadura.gui.application.LoggingDesktopController;
import net.esle.sinadura.gui.application.PDFInfo;
import net.esle.sinadura.gui.sections.main.windows.TablePDF;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class SendPDFListener implements SelectionListener {

    private static Logger logger = Logger.getLogger(SendPDFListener.class.getName());

    private TablePDF tablePDF = null;

    public SendPDFListener(TablePDF t) {
        this.tablePDF = t;
    }

    public void widgetSelected(SelectionEvent event) {
        if (tablePDF.getSelectedPDFs().size() != 0) {
            List<String> attachemntList = new ArrayList<String>();
            for (PDFInfo pdfParameter : tablePDF.getSelectedPDFs()) {
                attachemntList.add(pdfParameter.getPath());
            }
            DesktopHelper.openDefaultMailClient(attachemntList);
        } else {
            logger.severe(LanguageResource.getLanguage().getString("error.no_selected_file"));
            LoggingDesktopController.printError(LanguageResource.getLanguage().getString("error.no_selected_file"));
        }
    }

    public void widgetDefaultSelected(SelectionEvent event) {
        widgetSelected(event);
    }
}
