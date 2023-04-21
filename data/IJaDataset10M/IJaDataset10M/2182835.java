package org.matsim.vis.otfvis.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.matsim.core.mobsim.queuesim.QueueLink;
import org.matsim.core.utils.misc.ByteBufferUtils;
import org.matsim.vis.otfvis.data.OTFDataWriter;
import org.matsim.vis.otfvis.data.OTFServerQuad;
import org.matsim.vis.otfvis.interfaces.OTFDataReader;

public class OTFLinkLanesAgentsNoParkingHandler extends OTFLinkAgentsHandler {

    static {
        OTFDataReader.setPreviousVersion(OTFLinkLanesAgentsNoParkingHandler.class.getCanonicalName() + "V1.3", ReaderV1_3.class);
    }

    public static class Writer extends OTFLinkAgentsHandler.Writer {

        private static final long serialVersionUID = 6541770536927233851L;

        public void writeConstData(ByteBuffer out) throws IOException {
            String id = this.src.getLink().getId().toString();
            ByteBufferUtils.putString(out, id);
            out.putFloat((float) (this.src.getLink().getFromNode().getCoord().getX() - OTFServerQuad.offsetEast));
            out.putFloat((float) (this.src.getLink().getFromNode().getCoord().getY() - OTFServerQuad.offsetNorth));
            out.putFloat((float) (this.src.getLink().getToNode().getCoord().getX() - OTFServerQuad.offsetEast));
            out.putFloat((float) (this.src.getLink().getToNode().getCoord().getY() - OTFServerQuad.offsetNorth));
            out.putInt(this.src.getLink().getLanesAsInt(0));
        }

        @Override
        public OTFDataWriter<QueueLink> getWriter() {
            return new Writer();
        }
    }

    public void readConstData(ByteBuffer in) throws IOException {
        String id = ByteBufferUtils.getString(in);
        this.quadReceiver.setQuad(in.getFloat(), in.getFloat(), in.getFloat(), in.getFloat(), in.getInt());
        this.quadReceiver.setId(id.toCharArray());
    }

    /***
	 * PREVIOUS VERSION of the reader
	 * 
	 * @author dstrippgen
	 */
    public static final class ReaderV1_3 extends OTFLinkAgentsHandler {

        @Override
        public void readConstData(ByteBuffer in) throws IOException {
            this.quadReceiver.setQuad(in.getFloat(), in.getFloat(), in.getFloat(), in.getFloat(), in.getInt());
        }
    }
}
