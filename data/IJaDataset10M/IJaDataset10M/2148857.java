package javax.media.ding3d.utils.scenegraph.io.state.javax.media.ding3d;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import javax.media.ding3d.AuralAttributes;
import javax.media.ding3d.SceneGraphObject;
import javax.media.ding3d.vecmath.Vector3f;
import javax.media.ding3d.utils.scenegraph.io.retained.Controller;
import javax.media.ding3d.utils.scenegraph.io.retained.SymbolTableData;

public class AuralAttributesState extends NodeComponentState {

    public AuralAttributesState(SymbolTableData symbol, Controller control) {
        super(symbol, control);
    }

    public void writeObject(DataOutput out) throws IOException {
        super.writeObject(out);
        out.writeFloat(((AuralAttributes) node).getAttributeGain());
        float[] distance = new float[((AuralAttributes) node).getDistanceFilterLength()];
        float[] cutoff = new float[distance.length];
        ((AuralAttributes) node).getDistanceFilter(distance, cutoff);
        out.writeInt(distance.length);
        for (int i = 0; i < distance.length; i++) {
            out.writeFloat(distance[i]);
            out.writeFloat(cutoff[i]);
        }
        out.writeFloat(((AuralAttributes) node).getFrequencyScaleFactor());
        out.writeFloat(((AuralAttributes) node).getReflectionCoefficient());
        control.writeBounds(out, ((AuralAttributes) node).getReverbBounds());
        out.writeFloat(((AuralAttributes) node).getReverbDelay());
        out.writeInt(((AuralAttributes) node).getReverbOrder());
        out.writeFloat(((AuralAttributes) node).getRolloff());
        out.writeFloat(((AuralAttributes) node).getVelocityScaleFactor());
        out.writeFloat(((AuralAttributes) node).getReflectionDelay());
        out.writeFloat(((AuralAttributes) node).getReverbCoefficient());
        out.writeFloat(((AuralAttributes) node).getDecayTime());
        out.writeFloat(((AuralAttributes) node).getDecayFilter());
        out.writeFloat(((AuralAttributes) node).getDiffusion());
        out.writeFloat(((AuralAttributes) node).getDensity());
    }

    public void readObject(DataInput in) throws IOException {
        super.readObject(in);
        ((AuralAttributes) node).setAttributeGain(in.readFloat());
        float[] distance = new float[in.readInt()];
        float[] cutoff = new float[distance.length];
        for (int i = 0; i < distance.length; i++) {
            distance[i] = in.readFloat();
            cutoff[i] = in.readFloat();
        }
        ((AuralAttributes) node).setDistanceFilter(distance, cutoff);
        ((AuralAttributes) node).setFrequencyScaleFactor(in.readFloat());
        ((AuralAttributes) node).setReflectionCoefficient(in.readFloat());
        ((AuralAttributes) node).setReverbBounds(control.readBounds(in));
        ((AuralAttributes) node).setReverbDelay(in.readFloat());
        ((AuralAttributes) node).setReverbOrder(in.readInt());
        ((AuralAttributes) node).setRolloff(in.readFloat());
        ((AuralAttributes) node).setVelocityScaleFactor(in.readFloat());
        ((AuralAttributes) node).setReflectionDelay(in.readFloat());
        ((AuralAttributes) node).setReverbCoefficient(in.readFloat());
        ((AuralAttributes) node).setDecayTime(in.readFloat());
        ((AuralAttributes) node).setDecayFilter(in.readFloat());
        ((AuralAttributes) node).setDiffusion(in.readFloat());
        ((AuralAttributes) node).setDensity(in.readFloat());
    }

    protected SceneGraphObject createNode() {
        return new AuralAttributes();
    }
}
