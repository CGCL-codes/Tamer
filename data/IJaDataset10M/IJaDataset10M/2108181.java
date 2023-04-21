package javax.media.ding3d.utils.scenegraph.io.state.javax.media.ding3d;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import javax.media.ding3d.vecmath.Point3d;
import javax.media.ding3d.CompressedGeometry;
import javax.media.ding3d.SceneGraphObject;
import javax.media.ding3d.CompressedGeometryHeader;
import javax.media.ding3d.utils.scenegraph.io.retained.Controller;
import javax.media.ding3d.utils.scenegraph.io.retained.SymbolTableData;

public class CompressedGeometryState extends GeometryState {

    private byte[] bytes;

    private boolean isByReference;

    private CompressedGeometryHeader header;

    public CompressedGeometryState(SymbolTableData symbol, Controller control) {
        super(symbol, control);
    }

    public void writeConstructorParams(DataOutput out) throws IOException {
        super.writeConstructorParams(out);
        out.writeBoolean(((CompressedGeometry) node).isByReference());
        int size = ((CompressedGeometry) node).getByteCount();
        out.writeInt(size);
        bytes = new byte[size];
        ((CompressedGeometry) node).getCompressedGeometry(bytes);
        out.write(bytes);
        header = new CompressedGeometryHeader();
        ((CompressedGeometry) node).getCompressedGeometryHeader(header);
        writeCompressedGeometryHeader(out);
    }

    public void readConstructorParams(DataInput in) throws IOException {
        super.readConstructorParams(in);
        isByReference = in.readBoolean();
        bytes = new byte[in.readInt()];
        in.readFully(bytes);
        header = new CompressedGeometryHeader();
        readCompressedGeometryHeader(in);
    }

    private void writeCompressedGeometryHeader(DataOutput out) throws IOException {
        out.writeInt(header.majorVersionNumber);
        out.writeInt(header.minorVersionNumber);
        out.writeInt(header.minorMinorVersionNumber);
        out.writeInt(header.bufferType);
        out.writeInt(header.bufferDataPresent);
        out.writeInt(header.size);
        out.writeInt(header.start);
        if (header.lowerBound == null) {
            control.writePoint3d(out, new Point3d(-1, -1, -1));
        } else {
            control.writePoint3d(out, header.lowerBound);
        }
        if (header.upperBound == null) {
            control.writePoint3d(out, new Point3d(1, 1, 1));
        } else {
            control.writePoint3d(out, header.upperBound);
        }
    }

    private void readCompressedGeometryHeader(DataInput in) throws IOException {
        header.majorVersionNumber = in.readInt();
        header.minorVersionNumber = in.readInt();
        header.minorMinorVersionNumber = in.readInt();
        header.bufferType = in.readInt();
        header.bufferDataPresent = in.readInt();
        header.size = in.readInt();
        header.start = in.readInt();
        header.lowerBound = control.readPoint3d(in);
        if ((header.lowerBound.x == -1) && (header.lowerBound.y == -1) && (header.lowerBound.z == -1)) {
            header.lowerBound = null;
        }
        header.upperBound = control.readPoint3d(in);
        if ((header.upperBound.x == 1) && (header.upperBound.y == 1) && (header.upperBound.z == 1)) {
            header.upperBound = null;
        }
    }

    public SceneGraphObject createNode(Class Ding3dClass) {
        return createNode(Ding3dClass, new Class[] { CompressedGeometryHeader.class, bytes.getClass(), Boolean.TYPE }, new Object[] { header, bytes, new Boolean(isByReference) });
    }

    protected SceneGraphObject createNode() {
        return new CompressedGeometry(header, bytes, isByReference);
    }
}
