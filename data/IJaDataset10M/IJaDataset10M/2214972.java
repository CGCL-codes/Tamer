package net.sf.jcgm.core;

import java.io.*;

/**
 * Class=2, Element=2
 * @author xphc (Philippe Cadé)
 * @author BBNT Solutions
 * @version $Id: ColorSelectionMode.java 3 2009-10-16 08:51:15Z phica $
 */
class ColorSelectionMode extends Command {

    enum Type {

        INDEXED, DIRECT
    }

    private static Type type;

    static {
        reset();
    }

    public ColorSelectionMode(int ec, int eid, int l, DataInput in) throws IOException {
        super(ec, eid, l, in);
        int e = makeEnum();
        if (e == 0) ColorSelectionMode.type = Type.INDEXED; else if (e == 1) ColorSelectionMode.type = Type.DIRECT; else {
            ColorSelectionMode.type = Type.INDEXED;
            unsupported("color selection mode " + e);
        }
        assert (this.currentArg == this.args.length);
    }

    public static void reset() {
        type = Type.INDEXED;
    }

    public static Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ColorSelectionMode " + type;
    }
}
