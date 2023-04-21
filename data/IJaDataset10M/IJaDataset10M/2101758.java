package model;

import java.util.Iterator;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class SystemBoundary extends Boundary {

    private Point3d point;

    private Vector3d normal;

    private String face;

    public SystemBoundary(String id, String face) {
        super(id);
        this.face = face;
    }

    public Point3d getPoint() {
        return point;
    }

    private void setRight(Point3d p) {
        if (point.x != 0) {
            p.x = (point.x);
        } else if (point.y != 0) {
            p.y = (point.y);
        } else if (point.z != 0) {
            p.z = (point.z);
        }
    }

    public Point3d pointOfIntersection(Vector3d oldV, Vector3d newV) {
        Vector3d v1 = new Vector3d();
        Point3d p1 = new Point3d(oldV);
        Point3d p2 = new Point3d(newV);
        v1.sub(point, p1);
        double d1 = normal.dot(v1);
        Vector3d v2 = new Vector3d();
        v2.sub(p2, p1);
        double d2 = normal.dot(v2);
        double u = d1 / d2;
        if ((u > 0 && u < 1) || d2 == 0.0) {
            p2.sub(p1);
            p2.scale(u);
            Point3d p = new Point3d();
            p.add(p1, p2);
            setRight(p);
            return p;
        }
        System.err.println("Serious fault - entity outside System Volume!");
        System.err.println("Discontinuing Simulation.");
        System.err.println("doReflective it's crossing:");
        System.err.println("point on plane " + point);
        System.err.println("End_point " + newV);
        System.exit(1);
        return null;
    }

    public Vector3d getNormal(Point3d p) {
        return normal;
    }

    public char testInteractionType(Cluster c, EDiffusionSpace diffusionSpace) {
        if (this.face.equalsIgnoreCase("MEMBRANE")) {
            return 'r';
        }
        float r = simSys.getRandomizer().nextFloat();
        Iterator<Float> iProb = boundaryProbability.iterator();
        Iterator<EBoundaryType> iId = boundaryId.iterator();
        while (iProb.hasNext()) {
            Float p = (Float) iProb.next();
            EBoundaryType s = (EBoundaryType) iId.next();
            if (r <= p) {
                switch(s) {
                    case OPEN:
                        System.err.println("Open boundary not allowed for System Volume.");
                        System.exit(1);
                        break;
                    case REFLECTIVE:
                        return 'r';
                    case PERIODIC:
                        return 'p';
                    case ABSORBING:
                        return 'a';
                    default:
                        break;
                }
                break;
            }
        }
        return 'n';
    }

    public char resolve(Character cr, Cluster c, EDiffusionSpace diffusionSpace) {
        switch(cr) {
            case 'o':
                System.err.println("Open boundary not allowed for System Volume.");
                System.exit(1);
                break;
            case 'r':
                doReflective(c, diffusionSpace);
                return 'r';
            case 'p':
                doPeriodic(c, diffusionSpace);
                return 'p';
            case 'a':
                doAbsorbing(c);
                return 'a';
            default:
                break;
        }
        return 'n';
    }

    @Override
    protected void doReflective(Cluster c, EDiffusionSpace diffusionSpace) {
        Vector3d o = c.getCurrentCentreOfMass();
        Vector3d newV = c.getNewCentreOfMass();
        Vector3d displacement = new Vector3d();
        displacement.sub(newV, o);
        double mag_displ = displacement.length();
        Point3d poi = pointOfIntersection(o, newV);
        double xd = poi.x - o.x;
        double yd = poi.y - o.y;
        double zd = poi.z - o.z;
        double distanceOriginIntersect = Math.sqrt(xd * xd + yd * yd + zd * zd);
        double fraction = distanceOriginIntersect / mag_displ;
        Vector3d n = getNormal(new Point3d());
        double dotDisplacementN = displacement.dot(n);
        dotDisplacementN = -2 * dotDisplacementN;
        Vector3d v = new Vector3d();
        v.scale(dotDisplacementN, n);
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

    private void doPeriodic(Cluster c, EDiffusionSpace diffusionSpace) {
        float halfSimulationSize = 0.5f * simSys.getSimulationSize();
        Vector3d o = c.getCurrentCentreOfMass();
        Vector3d newV = c.getNewCentreOfMass();
        Point3d poi = pointOfIntersection(o, newV);
        if (point.x > 0) {
            double x = (newV.x % halfSimulationSize);
            int times = (int) (newV.x / halfSimulationSize);
            if ((times % 2) == 0) {
                newV.x = x;
            } else {
                newV.x = x - halfSimulationSize;
            }
            poi.x = poi.x * (-1);
        } else if (point.x < 0) {
            double x = (newV.x % halfSimulationSize);
            int times = (int) (newV.x / halfSimulationSize);
            if ((times % 2) == 0) {
                newV.x = x;
            } else {
                newV.x = x + halfSimulationSize;
            }
            poi.x = poi.x * (-1);
        }
        if (point.y > 0) {
            double y = (newV.y % halfSimulationSize);
            int times = (int) (newV.y / halfSimulationSize);
            if ((times % 2) == 0) {
                newV.y = y;
            } else {
                newV.y = y - halfSimulationSize;
            }
            poi.y = poi.y * (-1);
        } else if (point.y < 0) {
            double y = (newV.y % halfSimulationSize);
            int times = (int) (newV.x / halfSimulationSize);
            if ((times % 2) == 0) {
                newV.y = y;
            } else {
                newV.y = y + halfSimulationSize;
            }
            poi.y = poi.y * (-1);
        }
        if (point.z > 0) {
            double z = (newV.z % halfSimulationSize);
            int times = (int) (newV.z / halfSimulationSize);
            if ((times % 2) == 0) {
                newV.z = z;
            } else {
                newV.z = z - halfSimulationSize;
            }
            poi.z = poi.z * (-1);
        } else if (point.z < 0) {
            double z = (newV.z % halfSimulationSize);
            int times = (int) (newV.z / halfSimulationSize);
            if ((times % 2) == 0) {
                newV.z = z;
            } else {
                newV.z = z + halfSimulationSize;
            }
            poi.z = poi.z * (-1);
        }
        c.setCurrentCentreOfMass(poi);
        c.setNewCentreOfMass(newV);
        simSys.checkThis(c, diffusionSpace);
    }

    public void setSystem(SimulatedSystem simSys, float simulationSize) {
        this.simSys = simSys;
        if (face.equalsIgnoreCase("ZMAX")) {
            this.point = new Point3d(0, 0, (simulationSize * 0.5f));
            Point3d zmin = new Point3d(0, 0, (simulationSize * -0.5f));
            this.normal = new Vector3d(zmin);
        } else if (face.equalsIgnoreCase("ZMIN")) {
            Point3d zmax = new Point3d(0, 0, (simulationSize * 0.5f));
            this.point = new Point3d(0, 0, (simulationSize * -0.5f));
            this.normal = new Vector3d(zmax);
        } else if (face.equalsIgnoreCase("XMAX")) {
            this.point = new Point3d((simulationSize * 0.5f), 0, 0);
            Point3d xmin = new Point3d((simulationSize * -0.5f), 0, 0);
            this.normal = new Vector3d(xmin);
        } else if (face.equalsIgnoreCase("XMIN")) {
            Point3d xmax = new Point3d((simulationSize * 0.5f), 0, 0);
            this.point = new Point3d((simulationSize * -0.5f), 0, 0);
            this.normal = new Vector3d(xmax);
        } else if (face.equalsIgnoreCase("YMAX")) {
            this.point = new Point3d(0, (simulationSize * 0.5f), 0);
            Point3d ymin = new Point3d(0, (simulationSize * -0.5f), 0);
            this.normal = new Vector3d(ymin);
        } else if (face.equalsIgnoreCase("YMIN")) {
            Point3d ymax = new Point3d(0, (simulationSize * 0.5f), 0);
            this.point = new Point3d(0, (simulationSize * -0.5f), 0);
            this.normal = new Vector3d(ymax);
        } else if (face.equalsIgnoreCase("MEMBRANE")) {
            Point3d ymax = new Point3d(0, simSys.getMembranePosition() * 1.2f, 0);
            this.point = new Point3d(0, simSys.getMembranePosition(), 0);
            this.normal = new Vector3d(ymax);
        } else {
            System.err.println("Incorrect system boundary definition.");
            System.err.println("Check boundary ids.");
            System.exit(-1);
        }
        normal.normalize();
    }

    public void setFace(String s) {
        this.face = s;
    }
}
