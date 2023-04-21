package de.grogra.gpuflux.scene.volume;

import java.io.IOException;
import de.grogra.gpuflux.jocl.compute.ComputeByteBuffer;
import de.grogra.vecmath.BoundingBox3d;
import de.grogra.vecmath.geom.Sphere;
import de.grogra.vecmath.geom.Variables;

/**
 * 
 * @author Dietger van Antwerpen
 *
 */
public class FluxSphere extends FluxPrimitive {

    private Sphere v;

    public FluxSphere(Sphere v) {
        assert v != null;
        this.v = v;
    }

    @Override
    public void getExtent(BoundingBox3d bb, Variables temp) {
        assert bb != null;
        assert temp != null;
        v.getExtent(bb.getMin(), bb.getMax(), temp);
    }

    @Override
    public void serialize(ComputeByteBuffer out) throws IOException {
        serialize(out, PRIM_SPHERE, v);
    }
}
