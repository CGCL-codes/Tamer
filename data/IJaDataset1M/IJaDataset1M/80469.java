package javax.media.ding3d.utils.scenegraph.io.state.javax.media.ding3d;

import java.io.*;
import javax.media.ding3d.IndexedGeometryStripArray;
import javax.media.ding3d.SceneGraphObject;
import javax.media.ding3d.vecmath.*;
import javax.media.ding3d.utils.scenegraph.io.retained.Controller;
import javax.media.ding3d.utils.scenegraph.io.retained.SymbolTableData;

public abstract class IndexedGeometryStripArrayState extends IndexedGeometryArrayState {

    protected int[] stripIndexCounts;

    public IndexedGeometryStripArrayState(SymbolTableData symbol, Controller control) {
        super(symbol, control);
    }

    protected void writeConstructorParams(DataOutput out) throws IOException {
        super.writeConstructorParams(out);
        stripIndexCounts = new int[((IndexedGeometryStripArray) node).getNumStrips()];
        ((IndexedGeometryStripArray) node).getStripIndexCounts(stripIndexCounts);
        out.writeInt(stripIndexCounts.length);
        for (int i = 0; i < stripIndexCounts.length; i++) out.writeInt(stripIndexCounts[i]);
    }

    protected void readConstructorParams(DataInput in) throws IOException {
        super.readConstructorParams(in);
        stripIndexCounts = new int[in.readInt()];
        for (int i = 0; i < stripIndexCounts.length; i++) stripIndexCounts[i] = in.readInt();
    }
}
