package cz.langteacher.gui.mainwindow.table;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public interface LessonSelectionListenerIface extends ListSelectionListener {

    public abstract void valueChanged(ListSelectionEvent e);
}
