package net.sourceforge.timetrack;

import java.util.Observable;
import java.util.Observer;
import javax.swing.JTextField;
import net.sourceforge.timetrack.utils.NumericFilter;

public class DurationField extends JTextField implements Observer {

    private static final long serialVersionUID = 7166261566065805555L;

    private EntryList _entryList;

    private Entry _entry;

    public DurationField(EntryList entryList) {
        addKeyListener(new NumericFilter(this));
        _entryList = entryList;
        _entryList.addObserver(this);
        synchronized (_entryList) {
            _entry = _entryList.getCurrentEntry();
            if (_entry != null) {
                _entry.addEntryListener(updater);
            }
        }
        updateText();
    }

    public void update(Observable o, Object obj) {
        if (o == _entryList) {
            _entry.removeEntryListener(updater);
            _entry = _entryList.getCurrentEntry();
            _entry.addEntryListener(updater);
            updateText();
        }
    }

    public void updateText() {
        if (_entry != null) {
            setText("" + _entry.getDuration());
        }
    }

    public Entry getEntry() {
        return _entry;
    }

    protected class EntryUpdater extends EntryAdapter {

        public void durationChanged() {
            updateText();
        }
    }

    EntryUpdater updater = new EntryUpdater();
}
