package javax.media.ding3d.utils.scenegraph.io.state.javax.media.ding3d;

import java.io.*;
import javax.media.ding3d.GeometryStripArray;
import javax.media.ding3d.SceneGraphObject;
import javax.media.ding3d.vecmath.*;
import javax.media.ding3d.utils.scenegraph.io.retained.Controller;
import javax.media.ding3d.utils.scenegraph.io.retained.SymbolTableData;

public abstract class GeometryStripArrayState extends GeometryArrayState {

    protected int[] stripVertexCounts;

    public GeometryStripArrayState(SymbolTableData symbol, Controller control) {
        super(symbol, control);
    }

    protected void writeConstructorParams(DataOutput out) throws IOException {
        super.writeConstructorParams(out);
        stripVertexCounts = new int[((GeometryStripArray) node).getNumStrips()];
        ((GeometryStripArray) node).getStripVertexCounts(stripVertexCounts);
        out.writeInt(stripVertexCounts.length);
        for (int i = 0; i < stripVertexCounts.length; i++) out.writeInt(stripVertexCounts[i]);
    }

    protected void readConstructorParams(DataInput in) throws IOException {
        super.readConstructorParams(in);
        stripVertexCounts = new int[in.readInt()];
        for (int i = 0; i < stripVertexCounts.length; i++) stripVertexCounts[i] = in.readInt();
    }
}
