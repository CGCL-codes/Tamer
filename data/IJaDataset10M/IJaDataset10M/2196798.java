package toolkit.levelEditor.actions;

import game.resourceObjects.LevelResource;
import game.util.Conf;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import toolkit.levelEditor.LevelEditor;
import toolkit.levelEditor.gui.BlockingDialog;
import toolkit.levelEditor.gui.EditorCanvas;
import toolkit.levelEditor.tools.FileExtensionFilter;
import toolkit.levelEditor.tools.SVGImporter;

public class ImportSVGAction extends AbstractAction {

    public ImportSVGAction() {
        putValue(SHORT_DESCRIPTION, "Import SVG");
        putValue(SMALL_ICON, Conf.ICON_DOCUMENT_IMPORT_SVG);
    }

    public void actionPerformed(final ActionEvent e) {
        final JFileChooser fc = new JFileChooser(".");
        fc.setFileFilter(new FileExtensionFilter("svg"));
        if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(LevelEditor.get().getFrame())) {
            final BlockingDialog dialog = new BlockingDialog(LevelEditor.get().getFrame(), "Loading SVG...");
            dialog.setMessage("Please wait...");
            new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    final LevelResource resource = SVGImporter.load(fc.getSelectedFile());
                    if (null == resource) {
                        Logger.getLogger(getClass().getName()).warning("Failed loading svg: " + fc.getSelectedFile().getAbsolutePath());
                    } else {
                        Logger.getLogger(getClass().getName()).info("Adding geometry to editor...");
                        EditorCanvas.getInstance().addLevelResource(resource);
                        Logger.getLogger(getClass().getName()).info("Done.");
                    }
                    return null;
                }

                @Override
                protected void done() {
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            }.execute();
            dialog.setVisible(true);
        }
    }
}
