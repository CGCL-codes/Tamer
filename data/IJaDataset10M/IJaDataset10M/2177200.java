package mirrormonkey.rpc.tools.result.entities;

import mirrormonkey.rpc.tools.entities.SyncBox;
import mirrormonkey.rpc.tools.result.specs.PositionRequestable;
import com.jme3.math.Vector3f;

public class PositionRequestEntity extends SyncBox implements PositionRequestable {

    public PositionRequestEntity(Vector3f pos) {
        super(pos);
    }

    @Override
    public Vector3f getLocalTranslation() {
        return super.getLocalTranslation();
    }

    @Override
    public Object returnNull() {
        return null;
    }

    @Override
    public void throwSomeError(String expectedMessage) {
        throw new RuntimeException(expectedMessage);
    }

    @Override
    public void justReturn() {
    }

    @Override
    public PositionRequestEntity returnThis() {
        return this;
    }

    @Override
    public PositionRequestable returnThisByInterface() {
        return this;
    }

    @Override
    public PositionRequestEntity returnUnknownEntity() {
        return new PositionRequestEntity(Vector3f.ZERO);
    }

    @Override
    public PositionRequestEntity returnIdAwareNull() {
        return null;
    }
}
