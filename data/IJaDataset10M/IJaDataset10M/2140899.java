package org.matsim.utils.vis.otfvis.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.matsim.mobsim.queuesim.QueueNode;
import org.matsim.utils.vis.otfvis.caching.SceneGraph;
import org.matsim.utils.vis.otfvis.data.OTFData;
import org.matsim.utils.vis.otfvis.data.OTFDataWriter;
import org.matsim.utils.vis.otfvis.data.OTFDataXYCoord;
import org.matsim.utils.vis.otfvis.data.OTFServerQuad;
import org.matsim.utils.vis.otfvis.data.OTFWriterFactory;
import org.matsim.utils.vis.otfvis.interfaces.OTFDataReader;

public class OTFDefaultNodeHandler extends OTFDataReader implements OTFDataXYCoord.Provider {

    private OTFDataXYCoord.Receiver xyReceiver = null;

    public static class Writer extends OTFDataWriter<QueueNode> implements OTFWriterFactory<QueueNode> {

        private static final long serialVersionUID = 8011757932341886429L;

        @Override
        public void writeConstData(ByteBuffer out) throws IOException {
            out.putFloat((float) (this.src.getNode().getCoord().getX() - OTFServerQuad.offsetEast));
            out.putFloat((float) (this.src.getNode().getCoord().getY() - OTFServerQuad.offsetNorth));
        }

        @Override
        public void writeDynData(ByteBuffer out) throws IOException {
        }

        public OTFDataWriter<QueueNode> getWriter() {
            return new Writer();
        }
    }

    @Override
    public void readConstData(ByteBuffer in) throws IOException {
        if (this.xyReceiver != null) {
            this.xyReceiver.setXYCoord(in.getFloat(), in.getFloat());
        } else {
            in.getFloat();
            in.getFloat();
        }
    }

    @Override
    public void readDynData(ByteBuffer in, SceneGraph graph) throws IOException {
    }

    @Override
    public void connect(OTFData.Receiver receiver) {
        if (receiver instanceof OTFDataXYCoord.Receiver) {
            this.xyReceiver = (OTFDataXYCoord.Receiver) receiver;
        }
    }

    @Override
    public void invalidate(SceneGraph graph) {
        if (this.xyReceiver != null) this.xyReceiver.invalidate(graph);
    }
}
