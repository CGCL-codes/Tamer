package net.assimilator.examples.raytrace.render;

class Ray {

    final double HUGE_DIST = 1000000.0;

    Point3 origin;

    Vector3 dir;

    int depth;

    double dist;

    VisibleObject object;

    /** Holds value of property scene. */
    private Scene scene;

    public Ray(Scene scene, Point3 origin, Vector3 dir, int depth) {
        setScene(scene);
        this.origin = origin;
        this.dir = new Vector3(dir);
        this.dir.normalize();
        this.depth = depth;
        this.dist = HUGE_DIST;
    }

    public final int getDepth() {
        return depth;
    }

    public final double getDistance() {
        return dist;
    }

    public final Point3 getOrigin() {
        return origin;
    }

    public final Vector3 getDirection() {
        return dir;
    }

    public final VisibleObject getObject() {
        return object;
    }

    public void hit(Color shade) {
        for (int i = 0; i < scene.getObjectCount(); i++) {
            scene.getObject(i).intersect(this);
        }
        shade(shade);
    }

    public void record(double distance, VisibleObject object) {
        if (distance < dist && distance > 0.001) {
            this.dist = distance;
            this.object = object;
        }
    }

    Point3 getIntersect() {
        Vector3 tmp = new Vector3(dir);
        tmp.scaleBy(dist);
        Point3 intersect = new Point3(origin);
        intersect.add(tmp);
        return intersect;
    }

    void shade(Color shade) {
        if (object == null || depth > scene.getMaxDepth()) {
            shade.init(0, 0, 0);
            return;
        }
        if (object.getOpticalProperty().isLuminous()) {
            shade.init(object.getColor());
            return;
        }
        Shader shader = object.createShader(this);
        shader.color(shade);
    }

    /** Getter for property scene.
     * @return Value of property scene.
 */
    public Scene getScene() {
        return scene;
    }

    /** Setter for property scene.
     * @param scene New value of property scene.
 */
    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
