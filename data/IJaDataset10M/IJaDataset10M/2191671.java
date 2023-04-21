package net.kano.partypad;

import net.kano.partypad.objects.converters.SingleValueOutputPort;
import net.kano.partypad.pipeline.DataType;
import net.kano.partypad.pipeline.PipelineObject;
import net.kano.partypad.pipeline.PipelineObjectInfo;
import net.kano.partypad.pipeline.ProcessorObject;
import net.kano.partypad.pipeline.ports.PortInfo;
import net.kano.partypad.pipeline.ports.PortTypeInfo;

public final class TestingTools {

    private TestingTools() {
    }

    public static final PipelineObjectInfo DUMMY_OBJECT_INFO = new PipelineObjectInfo("dummy", "dummy");

    public static <V> V getProcessorOutputValue(ProcessorObject<?, V> conv) {
        return conv.getProcessorOutput().getValue(null);
    }

    public static <V> PortTypeInfo<V> createDummyPortTypeInfo(DataType<V> type) {
        return new PortTypeInfo<V>("dummy", "Dummy", type);
    }

    public static PipelineObjectInfo createDummyObjectInfo() {
        return new PipelineObjectInfo("dummy", "Dummy");
    }

    public static PortInfo createDummyPortInfo(PipelineObject obj) {
        return new PortInfo("dummy", "dummy", obj);
    }

    public static <V> void setProcessorInput(ProcessorObject<V, ?> conv, DataType<V> type, V val) {
        conv.getProcessorInput().setBinding(SingleValueOutputPort.getInstance(type, val));
    }
}
