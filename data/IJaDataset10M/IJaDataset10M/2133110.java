package org.exmaralda.partitureditor.deprecated;

import org.exmaralda.partitureditor.partiture.transcriptionActions.AbstractFSMSegmentationAction;
import org.exmaralda.partitureditor.deprecated.segmentationActions.*;
import org.exmaralda.partitureditor.jexmaraldaswing.fileDialogs.SaveSegmentedTranscriptionAsDialog;
import org.exmaralda.partitureditor.jexmaralda.SegmentedTranscription;
import org.exmaralda.partitureditor.jexmaralda.BasicTranscription;
import org.exmaralda.partitureditor.fsm.FSMException;
import org.exmaralda.partitureditor.partiture.*;
import org.exmaralda.partitureditor.jexmaralda.*;
import org.exmaralda.partitureditor.jexmaraldaswing.*;
import org.exmaralda.partitureditor.jexmaraldaswing.*;
import org.exmaralda.partitureditor.jexmaralda.*;
import org.exmaralda.partitureditor.fsm.*;
import java.util.*;
import org.xml.sax.*;

/**
 *
 * @author  thomas
 */
public class ExportGenericSegmentedTranscriptionAction extends AbstractFSMSegmentationAction {

    /** Creates a new instance of ExportGenericSegmentedTranscriptionAction */
    public ExportGenericSegmentedTranscriptionAction(PartitureTableWithActions t, javax.swing.ImageIcon icon) {
        super("Segmented Transcription (XML)", icon, t);
    }

    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        System.out.println("exportGenericSegmentedTranscriptionAction!");
        table.commitEdit(true);
        exportGenericSegmentedTranscription();
    }

    private void exportGenericSegmentedTranscription() {
        BasicTranscription bt = table.getModel().getTranscription().makeCopy();
        try {
            SegmentedTranscription st = new org.exmaralda.partitureditor.jexmaralda.segment.GenericSegmentation().BasicToSegmented(bt);
            SaveSegmentedTranscriptionAsDialog dialog = new SaveSegmentedTranscriptionAsDialog(table.homeDirectory, st);
            boolean success = dialog.saveTranscriptionAs(table);
        } catch (SAXException se) {
        } catch (FSMException fsme) {
        }
    }
}
