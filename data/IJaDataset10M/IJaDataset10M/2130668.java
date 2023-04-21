package org.adapit.wctoolkit.fomda.events.actions.fomda;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import org.adapit.wctoolkit.fomda.diagram.featuresdiagram.FeaturesDiagramPainterInternalFrame;
import org.adapit.wctoolkit.infrastructure.events.actions.DefaultAbstractApplicationAction;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.FomdaModel;
import org.adapit.wctoolkit.uml.ext.fomda.xml.XMLFeatureExporter;

public class SaveFeaturesDiagramAction extends DefaultAbstractApplicationAction {

    public SaveFeaturesDiagramAction() {
        super();
    }

    @Override
    protected void doAction(ActionEvent evt) {
        XMLFeatureExporter xml = new XMLFeatureExporter();
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Saving Features Diagram");
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String str = xml.toXMLBeanFactory(((FomdaModel) ((FeaturesDiagramPainterInternalFrame) defaultApplicationFrame.getDefaultContentPane().getDiagram()).getRootParent()).getRootFeatures().get(0), 1);
            File outputFile = fc.getSelectedFile();
            FileWriter out = null;
            String absolutePath = outputFile.getAbsolutePath();
            if (outputFile.exists()) {
                String fileName = outputFile.getAbsolutePath().substring(0, (absolutePath.indexOf(".")));
                File file2 = new File(fileName + ".bkp");
                outputFile.renameTo(file2);
            }
            try {
                out = new FileWriter(outputFile);
                out.write(str);
                out.close();
            } catch (Exception ex) {
            } finally {
            }
        }
    }
}
