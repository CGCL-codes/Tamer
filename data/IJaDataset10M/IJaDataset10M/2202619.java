package cx.fbn.nevernote.signals;

import com.trolltech.qt.QSignalEmitter;

public class NoteIndexSignal extends QSignalEmitter {

    public Signal1<Boolean> listChanged = new Signal1<Boolean>();

    public Signal0 notebookSelectionChanged = new Signal0();

    public Signal0 tagSelectionChanged = new Signal0();
}
