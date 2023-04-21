package net.sf.timeslottracker.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.SwingWorker;
import net.sf.csv4j.CSVReader;
import net.sf.timeslottracker.data.Attribute;
import net.sf.timeslottracker.gui.LayoutManager;

/**
 * Import csv file gui action
 * 
 * @version File version: $Revision: 1.23 $, $Date: 2007/04/07 03:59:30 $
 * @author Last change: $Author: cnitsa $
 */
public class ImportFromCSVAction extends AbstractAction {

    private final LayoutManager layoutManager;

    public ImportFromCSVAction(LayoutManager layoutManager) {
        super(layoutManager.getCoreString("import.action.name") + " ...", layoutManager.getIcon("new"));
        this.layoutManager = layoutManager;
    }

    public void actionPerformed(ActionEvent e) {
        ImportFromCSVDialog importFromCSVDialog = new ImportFromCSVDialog(layoutManager);
        importFromCSVDialog.activate();
        if (importFromCSVDialog.isCanceled()) {
            return;
        }
        File file = importFromCSVDialog.getFile();
        final int col = importFromCSVDialog.getColumn();
        final int firstRow = importFromCSVDialog.getFirstDataRow();
        char delimiter = importFromCSVDialog.getDelimiter();
        Charset encoding = importFromCSVDialog.getEncoding();
        try {
            final CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(file), encoding), delimiter);
            new SwingWorker<List<String>, String>() {

                @Override
                protected List<String> doInBackground() throws Exception {
                    try {
                        List<String> readLine = csvReader.readLine();
                        int lineCounter = -1;
                        while (!readLine.isEmpty()) {
                            lineCounter += 1;
                            if (lineCounter >= firstRow) {
                                publish(readLine.get(col));
                            }
                            readLine = csvReader.readLine();
                        }
                    } catch (Exception e2) {
                        layoutManager.getTimeSlotTracker().errorLog(e2);
                    }
                    return Collections.emptyList();
                }

                @Override
                protected void process(List<String> tasknames) {
                    for (String taskname : tasknames) {
                        if (isCancelled()) {
                            break;
                        }
                        layoutManager.getTasksInterface().addWoDialog(taskname, new ArrayList<Attribute>());
                    }
                }

                @Override
                protected void done() {
                    if (isCancelled()) {
                        return;
                    }
                }
            }.execute();
        } catch (Exception e2) {
            layoutManager.getTimeSlotTracker().errorLog(e2);
        }
    }
}
