package steering;

import java.io.InputStream;
import java.io.OutputStream;
import westworld.InvalidStateException;
import westworld.messaging.Message;
import com.jme.math.Vector2f;

public abstract class BaseGameEntity {

    private static final int DEFAULT_ENTITY_TYPE = -1;

    private int id;

    private int entityType;

    private boolean tag;

    private static int nextValidId;

    private int nextValidId() {
        return nextValidId++;
    }

    Vector2f pos;

    Vector2f scale;

    float boundingRadius;

    public BaseGameEntity() {
        id = nextValidId();
        boundingRadius = 0.0f;
        pos = new Vector2f();
        scale = new Vector2f(1.0f, 1.0f);
        entityType = DEFAULT_ENTITY_TYPE;
        tag = false;
    }

    public BaseGameEntity(int entityType) {
        id = nextValidId();
        boundingRadius = 0.0f;
        pos = new Vector2f();
        scale = new Vector2f(1.0f, 1.0f);
        this.entityType = entityType;
        tag = false;
    }

    public BaseGameEntity(int entityType, Vector2f pos, float radius) {
        this.pos = pos;
        boundingRadius = radius;
        id = nextValidId();
        scale = new Vector2f(1.0f, 1.0f);
        this.entityType = entityType;
        tag = false;
    }

    public BaseGameEntity(int entityType, int forceId) {
        id = forceId;
        boundingRadius = 0.0f;
        pos = new Vector2f();
        scale = new Vector2f(1.0f, 1.0f);
        this.entityType = entityType;
        tag = false;
    }

    public abstract void update() throws InvalidStateException;

    public abstract void render();

    public abstract void write(OutputStream os);

    public abstract void read(InputStream is);

    public boolean handleMessage(Message message) throws InvalidStateException {
        return false;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public Vector2f getPos() {
        return pos;
    }

    public void setPos(Vector2f pos) {
        this.pos = pos;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setScale(Vector2f scale) {
        boundingRadius *= Math.max(scale.x, scale.y) / Math.max(this.scale.x, this.scale.y);
        this.scale = scale;
    }

    public void setScale(float scale) {
        boundingRadius += scale / Math.max(this.scale.x, this.scale.y);
        this.scale = new Vector2f(scale, scale);
    }

    public float getBoundingRadius() {
        return boundingRadius;
    }

    public void setBoundingRadius(float boundingRadius) {
        this.boundingRadius = boundingRadius;
    }

    public int getId() {
        return id;
    }

    public void tag() {
        tag = true;
    }

    public void unTag() {
        tag = false;
    }

    public boolean isTagged() {
        return tag;
    }
}
