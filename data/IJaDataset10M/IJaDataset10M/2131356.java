package fluids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.linear.LVect3d;

/**
 *
 * @author Calvin Ashmore
 */
public class Particle {

    public class ParticleSpring {

        private Particle other;

        private double restLength;

        private ParticleSpring() {
        }

        public Particle getOther() {
            return other;
        }

        public double getRestLength() {
            return restLength;
        }

        public void setRestLength(double restLength) {
            this.restLength = restLength;
        }
    }

    private LVect3d previousPosition = new LVect3d();

    private LVect3d position = new LVect3d();

    private LVect3d velocity = new LVect3d();

    int index;

    private List<ParticleSpring> springs = new ArrayList<ParticleSpring>();

    private Map<Particle, ParticleSpring> springMap = new HashMap<Particle, ParticleSpring>();

    public int getIndex() {
        return index;
    }

    public double distanceTo(LVect3d pos) {
        return position.sub(pos).magnitude();
    }

    public double distance(Particle p) {
        return distanceTo(p.position);
    }

    public LVect3d getPreviousPosition() {
        return previousPosition;
    }

    public LVect3d getPosition() {
        return position;
    }

    public LVect3d getVelocity() {
        return velocity;
    }

    public List<ParticleSpring> getSprings() {
        return springs;
    }

    public ParticleSpring getSpring(Particle neighbor) {
        return springMap.get(neighbor);
    }

    public ParticleSpring addSpring(Particle neighbor, double restLength) {
        ParticleSpring spring = new ParticleSpring();
        spring.other = neighbor;
        spring.restLength = restLength;
        springs.add(spring);
        springMap.put(neighbor, spring);
        return spring;
    }

    public void removeSpring(ParticleSpring spring) {
        springs.remove(spring);
        springMap.remove(spring.other);
    }
}
