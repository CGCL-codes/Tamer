package de.grogra.gpuflux.scene.volume;

import java.io.IOException;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import de.grogra.gpuflux.jocl.compute.ComputeByteBuffer;
import de.grogra.imp3d.objects.SensorNode;
import de.grogra.vecmath.BoundingBox3d;
import de.grogra.vecmath.geom.SensorDisc;
import de.grogra.vecmath.geom.Variables;

public class FluxSensor extends FluxVolume {

    private SensorDisc sensorDisc;

    private int groupIndex;

    private SensorNode sensorNode;

    public FluxSensor(SensorNode s, SensorDisc v, int groupIndex) {
        super();
        this.sensorDisc = v;
        this.sensorNode = s;
        this.groupIndex = groupIndex;
    }

    public void getExtent(BoundingBox3d bb, Variables temp) {
        sensorDisc.getExtent(bb.getMin(), bb.getMax(), temp);
    }

    public void serialize(ComputeByteBuffer computeByteBuffer) throws IOException {
        computeByteBuffer.writeInt(groupIndex);
        Matrix3d rot = new Matrix3d();
        Vector3d trans = new Vector3d();
        sensorDisc.getTransformation(rot, trans);
        Matrix3f rotf = new Matrix3f(rot);
        Vector3f transf = new Vector3f(trans);
        computeByteBuffer.write(rotf);
        computeByteBuffer.write(transf);
        computeByteBuffer.writeBoolean(sensorNode.isTwoSided());
        Color3f color = new Color3f();
        color.set(sensorNode.getColor());
        color.scale((float) (1 / (Math.PI * sensorNode.getRadius() * sensorNode.getRadius())));
        computeByteBuffer.write(color);
        computeByteBuffer.writeFloat(sensorNode.getExponent());
    }

    public int getGroupIndex() {
        return groupIndex;
    }
}
