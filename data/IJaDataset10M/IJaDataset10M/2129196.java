package ircam.jmax.editors.sequence;

import java.awt.datatransfer.*;

public class SequenceClipboard extends Clipboard {

    static SequenceClipboard clipboard = new SequenceClipboard();

    public SequenceClipboard() {
        super("Sequence Clipboard");
    }

    public static SequenceClipboard getClipboard() {
        return clipboard;
    }
}
