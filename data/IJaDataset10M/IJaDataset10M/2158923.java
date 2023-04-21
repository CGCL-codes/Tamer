package net.sf.jcgm.core;

import java.io.DataInput;
import java.io.IOException;

/**
 * Element=5, Element=19
 * @author xphc (Philippe Cadé)
 * @version $Id: CharacterSetIndex.java 46 2011-12-14 08:26:44Z phica $
 * @since Jun 12, 2009
 */
public class CharacterSetIndex extends Command {

    private int index;

    public CharacterSetIndex(int ec, int eid, int l, DataInput in) throws IOException {
        super(ec, eid, l, in);
        this.index = makeIndex();
        unimplemented("");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CharacterSetIndex ").append(this.index);
        return sb.toString();
    }
}
