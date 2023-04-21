package bitWave.physics.impl.forces;

import bitWave.linAlg.LinAlgFactory;
import bitWave.linAlg.Vec4;
import bitWave.physics.Atmosphere;
import bitWave.physics.CelestialBody;
import bitWave.physics.RigidBodyComponent;
import bitWave.physics.impl.ForceImpl;
import bitWave.physics.impl.util.Drag;

public class Force_Drag_Atmospheric extends ForceImpl {

    protected final CelestialBody m_source;

    protected Atmosphere m_atmosphere;

    public Force_Drag_Atmospheric(final String name, final RigidBodyComponent target, final CelestialBody source) {
        super(name, target);
        this.m_source = source;
    }

    @Override
    public void computeForceAndTorque(double x, Vec4 f, Vec4 t) {
        Atmosphere atmos = this.m_source.getAtmosphere();
        if (atmos != null) {
            LinAlgFactory laf = this.m_source.getPosition().getFactory();
            Vec4 d = laf.subtractVectors(this.m_source.getPosition(), this.m_target.getBody().getPosition());
            double dl = d.normalize();
            double density = atmos.getDensityAtAltitude(dl - this.m_source.getSurfaceHeight());
            double area = 6;
            double cd = 1.1;
            Vec4 vRel = laf.subtractVectors(this.m_target.getBody().getVelocity(), this.m_source.getVelocity());
            double vRell = vRel.normalize();
            double fa = Drag.calcSpecificAerodynamicDrag(cd, density, area, vRell);
            d.normalize();
            f.assign(laf.scaleVector(vRel, -fa));
            t.setNull();
        }
    }
}
