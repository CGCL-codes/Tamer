package plp_converter.gui.panels;

import java.awt.Insets;
import javax.swing.filechooser.FileNameExtensionFilter;
import plp_converter.listeners.ExportPathListener;
import plp_converter.listeners.FileImportListener;
import plp_converter.listeners.FilesToPLPPreviewListener;
import plp_converter.listeners.PLPExportListener;

/**
 * Compiles a PLP playlist out of selected files
 */
public class FilesToPLP extends ToPLP {

    private static final long serialVersionUID = -6783241005548383555L;

    public FilesToPLP(Insets m) {
        super("Choose files to import", m);
        addListeners();
    }

    protected void addListeners() {
        FileImportListener fil = new FileImportListener(in);
        ExportPathListener epl = new ExportPathListener(out, new FileNameExtensionFilter("PLP Playlists", "plp"));
        PLPExportListener pel = new PLPExportListener(previewbutton, previewarea, out);
        inb.addActionListener(fil);
        outb.addActionListener(epl);
        previewbutton.addActionListener(new FilesToPLPPreviewListener(fil, previewarea, prefixfield));
        exportbutton.addActionListener(pel);
    }
}
