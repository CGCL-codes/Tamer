package net.kano.partypad.objects.processors;

import net.kano.partypad.datatypes.Proportion;
import net.kano.partypad.datatypes.SoundBuffer;
import net.kano.partypad.pipeline.AbstractSingleValueProcessor;
import net.kano.partypad.pipeline.PipelineObjectInfo;
import net.kano.partypad.pipeline.ports.InputPort;
import net.kano.partypad.pipeline.ports.OutputPort;
import net.kano.partypad.pipeline.ports.PortInfo;
import net.kano.partypad.pipeline.ports.SimpleBoundedInputPort;
import java.nio.ShortBuffer;
import static net.kano.partypad.pipeline.PipelineTools.TYPEINFO_PROPORTION;
import static net.kano.partypad.pipeline.PipelineTools.TYPEINFO_SOUNDBUFFER;

public class Panner extends AbstractSingleValueProcessor<SoundBuffer> {

    public static final PipelineObjectInfo OBJECT_INFO = new PipelineObjectInfo("panner", "Pan");

    private InputPort<Proportion> panInput = new SimpleBoundedInputPort<Proportion>(TYPEINFO_PROPORTION, new PortInfo("amount", "Pan", this), Proportion.HALF);

    {
        getInputHolder().add(panInput);
    }

    public Panner() {
        super(OBJECT_INFO);
        setPorts(TYPEINFO_SOUNDBUFFER, TYPEINFO_SOUNDBUFFER, new PortInfo("sound", "Sound", this), new PortInfo("sound", "Sound", this));
    }

    public SoundBuffer process(SoundBuffer soundBuffer) {
        OutputPort<Proportion> binding = panInput.getCurrentBinding();
        double pan;
        if (binding == null) {
            pan = 0;
        } else {
            Proportion value = binding.getValue(null);
            if (value == null) pan = 0; else pan = (value.getFloatValue() * 2) - 1;
        }
        if (pan == 0) return soundBuffer;
        ShortBuffer buffer = soundBuffer.getBuffer();
        double abspan = 1 - Math.abs(pan);
        boolean panNext = pan > 0;
        buffer.rewind();
        while (buffer.hasRemaining()) {
            if (panNext) {
                int pos = buffer.position();
                short panned = (short) (buffer.get() * abspan);
                buffer.put(pos, panned);
                panNext = false;
            } else {
                panNext = true;
                buffer.position(buffer.position() + 1);
            }
        }
        return soundBuffer;
    }
}
