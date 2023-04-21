package lamao.soh.core;

/**
 * Defines collision task for scene.
 * @author lamao
 *
 */
public class SHCollisionTask {

    /** Type of source entity */
    public String sourceType = null;

    /** Type of destination entity */
    public String destType = null;

    /** Bounding or triangle collision detection */
    public boolean checkTris = false;

    public SHCollisionTask(String sourceType, String destType, boolean checkTris) {
        this.checkTris = checkTris;
        this.destType = destType;
        this.sourceType = sourceType;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj == this) {
            result = true;
        } else if (obj instanceof SHCollisionTask) {
            SHCollisionTask task = (SHCollisionTask) obj;
            result = sourceType != null && sourceType.equals(task.sourceType) && destType != null && destType.equals(task.destType);
        }
        return result;
    }

    @Override
    public String toString() {
        return "<" + sourceType + ", " + destType + ", " + checkTris + ">";
    }
}
