package org.jnetpcap.packet.analysis;

import java.util.Iterator;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.JPacket;

/**
 * @author Mark Bednarczyk
 * @author Sly Technologies, Inc.
 */
public class FragmentAssembly extends AbstractAnalysis<FragmentAssembly, FragmentAssemblyEvent> {

    private static final String TITLE = "Fragment Reassembly";

    public enum Field implements JStructField {

        PACKET_SEQUENCE(REF), PACKET(REF);

        private final int len;

        int offset;

        private Field() {
            this(4);
        }

        private Field(int len) {
            this.len = len;
        }

        public int length(int offset) {
            this.offset = offset;
            return this.len;
        }

        public final int offset() {
            return offset;
        }
    }

    /**
	 * @param size
	 * @param name
	 */
    public FragmentAssembly(JPacket packet, FragmentSequence sequence) {
        super(TITLE, Field.values());
        setFragmentSequence(sequence);
        setPacket(packet);
    }

    private void setFragmentSequence(FragmentSequence sequence) {
        super.setObject(Field.PACKET_SEQUENCE.offset(), sequence);
    }

    public FragmentSequence getFragmentSequence() {
        return super.getObject(FragmentSequence.class, Field.PACKET_SEQUENCE.offset());
    }

    /**
	 * @param type
	 * @param size
	 * @param name
	 */
    public FragmentAssembly() {
        super(JMemory.Type.POINTER);
    }

    @SuppressWarnings("unchecked")
    public <T extends JAnalysis> T getAnalysis(T analysis) {
        if (analysis.getType() == AnalysisUtils.getType(FragmentSequence.class)) {
            return (T) getFragmentSequence();
        } else {
            return null;
        }
    }

    public <T extends JAnalysis> boolean hasAnalysis(T analysis) {
        return super.hasAnalysis(analysis.getType());
    }

    public <T extends JAnalysis> boolean hasAnalysis(Class<T> analysis) {
        return super.hasAnalysis(AnalysisUtils.getType(analysis));
    }

    @Override
    public Iterator<JAnalysis> iterator() {
        return getFragmentSequence().iterator();
    }

    /**
	 * @return
	 */
    public JPacket getPacket() {
        return super.getObject(JPacket.class, Field.PACKET.offset());
    }

    public void setPacket(JPacket packet) {
        super.setObject(Field.PACKET.offset(), packet);
    }
}
