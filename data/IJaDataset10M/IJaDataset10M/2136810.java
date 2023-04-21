package co.edu.unal.ungrid.services.client.applet.bimler.view.action.file;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JMenuItem;
import co.edu.unal.ungrid.services.client.applet.bimler.util.BimlerKeyStrokes;
import co.edu.unal.ungrid.services.client.applet.segmentation.SegmentationServiceFactory;
import co.edu.unal.ungrid.services.client.applet.view.action.SuperAction;

public class FileOpenDirectory extends SuperAction {

    public static final long serialVersionUID = 1L;

    public FileOpenDirectory() {
        putValue(Action.NAME, "Open Directory...");
        putValue(Action.SHORT_DESCRIPTION, "Load a local image directory");
    }

    @Override
    public JMenuItem getMenuItem() {
        JMenuItem mi = super.getMenuItem();
        mi.setAccelerator(BimlerKeyStrokes.FILE_OPEN_DIRECTORY);
        return mi;
    }

    public void actionPerformed(ActionEvent ae) {
        SegmentationServiceFactory.getInstance().fileOpenDirectory();
        super.actionPerformed(ae);
    }
}
