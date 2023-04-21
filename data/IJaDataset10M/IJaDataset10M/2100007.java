package edu.ucsd.ncmir.jinx.listeners.gui.main_panel;

import edu.sdsc.grid.io.GeneralFile;
import edu.ucsd.ncmir.asynchronous_event.AbstractAsynchronousEventListener;
import edu.ucsd.ncmir.asynchronous_event.AsynchronousEvent;
import edu.ucsd.ncmir.imod_model.Model;
import edu.ucsd.ncmir.jinx.events.JxErrorEvent;
import edu.ucsd.ncmir.jinx.events.JxLogEvent;
import edu.ucsd.ncmir.jinx.events.gui.JxFileChooserEvent;
import edu.ucsd.ncmir.jinx.gui.support.JxFileFilter;
import edu.ucsd.ncmir.spl.io.Accessor;
import edu.ucsd.ncmir.spl.io.AccessorFactory;

public class JxImportIMODContoursEventListener extends AbstractAsynchronousEventListener {

    public void handler(AsynchronousEvent event, Object object) {
        JxFileChooserEvent file_chooser_event = new JxFileChooserEvent(JxFileFilter.READABLE_XVOXTRACE);
        file_chooser_event.sendWait();
        GeneralFile file = file_chooser_event.getFile();
        if (file != null) {
            try {
                new JxLogEvent().send("Importing IMOD Contour format from " + file.toString());
                Accessor accessor = AccessorFactory.createAccessor(file);
                Model model = new Model(accessor);
            } catch (Throwable t) {
                new JxErrorEvent().send(t);
            }
        }
    }
}
