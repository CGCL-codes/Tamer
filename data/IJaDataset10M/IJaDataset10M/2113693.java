package jalview.gui.event;

import java.util.*;
import jalview.datamodel.*;

public class TreeSelectionEvent extends EventObject {

    AlignSequenceI seq;

    public TreeSelectionEvent(Object source, AlignSequenceI seq) {
        super(source);
        this.seq = seq;
    }

    public AlignSequenceI getSequence() {
        return seq;
    }

    public Object getSource() {
        return source;
    }
}
