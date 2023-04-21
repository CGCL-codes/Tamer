package jogamp.graph.font.typecast.ot.table;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:davidsch@dev.java.net">David Schweinsberg</a>
 * @version $Id: LigatureSubstFormat1.java,v 1.2 2007-01-24 09:47:47 davidsch Exp $
 */
public class LigatureSubstFormat1 extends LigatureSubst {

    private int _coverageOffset;

    private int _ligSetCount;

    private int[] _ligatureSetOffsets;

    private Coverage _coverage;

    private LigatureSet[] _ligatureSets;

    /** Creates new LigatureSubstFormat1 */
    protected LigatureSubstFormat1(DataInputStream dis, int offset) throws IOException {
        _coverageOffset = dis.readUnsignedShort();
        _ligSetCount = dis.readUnsignedShort();
        _ligatureSetOffsets = new int[_ligSetCount];
        _ligatureSets = new LigatureSet[_ligSetCount];
        for (int i = 0; i < _ligSetCount; i++) {
            _ligatureSetOffsets[i] = dis.readUnsignedShort();
        }
        dis.reset();
        dis.skipBytes(offset + _coverageOffset);
        _coverage = Coverage.read(dis);
        for (int i = 0; i < _ligSetCount; i++) {
            _ligatureSets[i] = new LigatureSet(dis, offset + _ligatureSetOffsets[i]);
        }
    }

    public int getFormat() {
        return 1;
    }

    public String getTypeAsString() {
        return "LigatureSubstFormat1";
    }
}
