package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public abstract class Boundary {

    private String id;

    protected List<Float> boundaryProbability = new ArrayList<Float>();

    protected List<EBoundaryType> boundaryId = new ArrayList<EBoundaryType>();

    protected SimulatedSystem simSys;

    private String domainOfOrigin;

    private String finalDomain;

    protected void doReflective(Cluster c, EDiffusionSpace diffusionSpace) {
        Vector3d o = c.getCurrentCentreOfMass();
        Vector3d finalP = c.getNewCentreOfMass();
        Vector3d displacement = new Vector3d();
        displacement.sub(finalP, o);
        double mag_displ = displacement.length();
        Point3d poi = pointOfIntersection(o, finalP);
        if (poi == null) {
            return;
        }
        Vector3d normal = getNormal(poi);
        double xd = poi.x - o.x;
        double yd = poi.y - o.y;
        double zd = poi.z - o.z;
        double distanceOriginIntersect = Math.sqrt(xd * xd + yd * yd + zd * zd);
        double fraction = distanceOriginIntersect / mag_displ;
        double dotDisplacementN = displacement.dot(normal);
        dotDisplacementN = -2 * dotDisplacementN;
        Vector3d v = new Vector3d();
        v.scale(dotDisplacementN, normal);
        v.add(displacement);
        v.scale((1 - fraction));
        Vector3d finalPosition = new Vector3d();
        Vector3d d = (Vector3d) displacement.clone();
        d.scale(fraction);
        finalPosition.add(o, d);
        finalPosition.add(v);
        c.setCurrentCentreOfMass(poi);
        c.setNewCentreOfMass(finalPosition);
        simSys.checkThis(c, diffusionSpace);
    }

    protected abstract Vector3d getNormal(Point3d poi);

    protected abstract Point3d pointOfIntersection(Vector3d o, Vector3d newV);

    public String getDomainOfOrigin() {
        return domainOfOrigin;
    }

    public void setDomainOfOrigin(String domainOfOrigin) {
        this.domainOfOrigin = domainOfOrigin;
    }

    public Boundary(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void putBoundaryProbability(String attribute, float value) {
        boundaryId.add(EBoundaryType.getType(attribute));
        float total = value;
        for (Float p : boundaryProbability) {
            total += p;
            if (total > 1) {
                System.err.println("Boundary probabilities add up to more than 1.");
                System.exit(1);
            }
        }
        boundaryProbability.add(total);
    }

    public char testInteractionType(Cluster c, EDiffusionSpace diffusionSpace) {
        float r = simSys.getRandomizer().nextFloat();
        Iterator<Float> iProb = boundaryProbability.iterator();
        Iterator<EBoundaryType> iId = boundaryId.iterator();
        while (iProb.hasNext()) {
            Float p = (Float) iProb.next();
            EBoundaryType s = (EBoundaryType) iId.next();
            if (r <= p) {
                switch(s) {
                    case OPEN:
                        return 'o';
                    case REFLECTIVE:
                        return 'r';
                    case PERIODIC:
                        return 'p';
                    case ABSORBING:
                        return 'a';
                    default:
                        return 'n';
                }
            }
        }
        return 'n';
    }

    public char resolve(Character cr, Cluster c, EDiffusionSpace diffusionSpace) {
        switch(cr) {
            case 'o':
                return 'o';
            case 'r':
                doReflective(c, diffusionSpace);
                return 'r';
            case 'p':
                doPeriodic();
                return 'p';
            case 'a':
                doAbsorbing(c);
                return 'a';
            default:
                return 'n';
        }
    }

    protected void doPeriodic() {
        System.err.println("Periodic non-system boundary not yet implemented!");
        System.exit(1);
    }

    protected void doAbsorbing(Cluster c) {
        simSys.anihilate(c);
    }

    public void setSystem(SimulatedSystem simSys) {
        this.simSys = simSys;
    }

    public String getFinalDomain() {
        return finalDomain;
    }

    public void setFinalDomain(String finalDomain) {
        this.finalDomain = finalDomain;
    }

    public void printDebug() {
        System.err.println("Id: " + id);
        System.err.println("Domain of Origin: " + domainOfOrigin);
        System.err.println("Final Domain: " + finalDomain);
        for (EBoundaryType string : boundaryId) {
            System.err.println(string);
        }
        for (Float f : boundaryProbability) {
            System.err.println(f);
        }
    }

    void setId(String s) {
        this.id = s;
    }
}
