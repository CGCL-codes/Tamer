package gameObjects;

import fancyClient.gameStates.GameState;
import gameObjects.Debris.DebrisType;
import java.awt.Polygon;
import java.nio.ByteBuffer;
import java.util.Random;
import shared.network.ObjectIDPacket;

public final class ObjectFactory {

    private ObjectFactory() {
        throw new AssertionError();
    }

    public static PolyRadiusPair createPolygonByType(DebrisType type, int seed, ByteBuffer packet) {
        int minRad = 10, maxRad = 55, minPts = 7, maxPts = 20;
        int radius;
        Polygon ret;
        switch(type) {
            case DEBUG:
                Random generator = new Random(seed);
                int numPoints = (int) Math.floor(generator.nextDouble() * (maxPts - minPts)) + minPts;
                int[] yPolyPoints = new int[numPoints];
                int[] xPolyPoints = new int[numPoints];
                radius = (int) Math.floor(generator.nextDouble() * (maxRad - minRad)) + minRad;
                double crAng = 0, angDiff = Math.toRadians(360.0 / numPoints), radJitter = radius / 3.0, angJitter = angDiff * .9;
                for (int i = 0; i < numPoints; i++) {
                    double tRadius = radius + (generator.nextDouble() * radJitter - radJitter / 2.0);
                    double tAng = crAng + (generator.nextDouble() * angJitter - angJitter / 2.0);
                    int nx = (int) (Math.sin(tAng) * tRadius), ny = (int) (Math.cos(tAng) * tRadius);
                    yPolyPoints[i] = ny;
                    xPolyPoints[i] = nx;
                    crAng += angDiff;
                }
                ret = new Polygon(xPolyPoints, yPolyPoints, numPoints);
                break;
            default:
                throw new IllegalArgumentException("Type " + type + " is not known.");
        }
        return new PolyRadiusPair(ret, (int) (radius * 1.1));
    }

    public static class PolyRadiusPair {

        public Polygon polygon;

        public int radius;

        public PolyRadiusPair(Polygon _polygon, int _radius) {
            polygon = _polygon;
            radius = _radius;
        }
    }

    public static DrawableSimulatedObject createByType(int type, int oid, GameState parent, ByteBuffer packet) {
        DrawableSimulatedObject ret = null;
        ObjectIDPacket.TypeID t = ObjectIDPacket.TypeID.getTypeFromID(type);
        if (t == null) throw new IllegalArgumentException("Type number " + type + " is not known.");
        switch(t) {
            case PLAYER_SHIP:
                ret = new PlayerShip(oid, parent);
                break;
            case DEBUG_BULLET:
                ret = new DebugBullet(oid, parent);
                break;
            case DEBRIS:
                break;
            case SLUG:
                ret = new Slug(oid, parent);
                break;
            case SHURIKEN:
                ret = new Shuriken(oid, parent);
                break;
            case PELLET:
                ret = new Pellet(oid, parent);
                break;
            case BURNER_MINE:
                ret = new BurnerMine(oid, parent);
                break;
            case ENGINEER_MINE:
                ret = new EngineerMine(oid, parent);
                break;
            case SKIRMISHER_MISSILE:
                ret = new SkirmisherMissile(oid, parent);
                break;
            case ASTEROID:
                ret = new Asteroid(oid, parent);
                break;
            case PLANETOID:
                ret = new Planetoid(oid, parent);
                break;
            default:
                throw new IllegalArgumentException("Type " + t.toString() + " is not implemented.");
        }
        return ret.InitializeFromObjectIDPacket(packet);
    }
}
