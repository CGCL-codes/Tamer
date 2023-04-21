package be.vds.jtbdive.client.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.processes.WorkingProcess;
import be.vds.jtbdive.client.swing.util.WindowUtils;
import be.vds.jtbdive.client.view.LogBookApplFrame;
import be.vds.jtbdive.client.view.panels.logbook.LogBookChooserDialog;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.core.util.ObjectSerializer;
import be.vds.jtbdive.core.xml.DiveSiteParser;
import be.vds.jtbdive.core.xml.DiverParser;
import be.vds.jtbdive.core.xml.LogBookParser;

public class ExportLogBookAction extends AbstractAction {

    private LogBookManagerFacade logBookManagerFacade;

    private LogBookApplFrame logBookApplFrame;

    private LogBookChooserDialog chooserDialog;

    private static final Syslog logger = Syslog.getLogger(ExportLogBookAction.class);

    public ExportLogBookAction(LogBookApplFrame logBookApplFrame, LogBookManagerFacade logBookManagerFacade) {
        this.logBookManagerFacade = logBookManagerFacade;
        this.logBookApplFrame = logBookApplFrame;
        putValue(Action.NAME, "export");
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.ALT_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (logBookManagerFacade.getCurrentLogBook() == null) {
            JOptionPane.showMessageDialog(logBookApplFrame, "You need a logbook to export...", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ExportLogBookDialog logbookDialog = new ExportLogBookDialog(logBookApplFrame);
        WindowUtils.centerWindow(logbookDialog);
        int i = logbookDialog.showDialog();
        if (i == ExportLogBookDialog.OPTION_OK) {
            Element exEl = getRootElement();
            try {
                writeExport(new Document(exEl), logbookDialog.getFile());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private Element getRootElement() {
        LogBook logbook = (LogBook) ObjectSerializer.cloneObject(logBookManagerFacade.getCurrentLogBook());
        Set<DiveSite> dls = logbook.getDiveLocations();
        Set<Diver> divers = logbook.getDivers();
        long diveLocationid = 1;
        DiveSiteParser p = new DiveSiteParser();
        Element dlsEl = new Element("divesites");
        for (DiveSite diveLocation : dls) {
            diveLocation.setId(diveLocationid++);
            dlsEl.addContent(p.createDiveLocationElement(diveLocation));
        }
        long diverid = 1;
        DiverParser pa = new DiverParser();
        Element diversEl = new Element("divers");
        for (Diver diver : divers) {
            diver.setId(diverid++);
            diversEl.addContent(pa.createDiverElement(diver));
        }
        long diveid = 1;
        LogBookParser lbP = new LogBookParser();
        for (Diver diver : divers) {
            diver.setId(diveid++);
        }
        Element exEl = new Element("jtbexport");
        exEl.addContent(dlsEl);
        exEl.addContent(diversEl);
        exEl.addContent(lbP.createLogBookRootElement(logbook));
        return exEl;
    }

    private void writeExport(Document document, File file) throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(file);
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());
        outputter.output(document, os);
        os.close();
    }

    class InnerWorkingProcess extends WorkingProcess {

        public InnerWorkingProcess(String id) {
            super(id);
        }

        @Override
        protected Object doInBackground() throws Exception {
            logger.info("opening logbook");
            fireProcessStarted(100, "process started");
            LogBook lb = chooserDialog.getSelectedLogBook();
            publish("loading logbook " + lb.getName() + "...");
            logBookManagerFacade.loadLogBook(lb.getId());
            return null;
        }

        @Override
        protected void process(List<String> arg0) {
            for (String string : arg0) {
                fireProcessProgressed(25, string);
            }
        }

        @Override
        protected void done() {
            fireProcessProgressed(100, "LogBook loaded");
            fireProcessFinished("LogBook loaded!");
        }
    }
}
