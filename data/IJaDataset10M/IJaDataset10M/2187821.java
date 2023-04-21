package mirrormonkey.state.tools.identity.entities;

import mirrormonkey.core.annotations.ClientSideConstructor;
import mirrormonkey.core.annotations.EntityHierarchy;
import mirrormonkey.framework.annotations.IdentityAware;
import mirrormonkey.framework.entity.EntityData;
import mirrormonkey.framework.entity.SyncEntity;
import mirrormonkey.state.AnnotationPresets.ServerToClient;
import mirrormonkey.state.AnnotationPresets.SpecialServerToClient;
import mirrormonkey.state.annotations.TrackValue;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

@EntityHierarchy
public class IdAwareTestBox extends Geometry implements SyncEntity {

    public EntityData data;

    public int setterCalls;

    @IdentityAware
    @TrackValue(true)
    @SpecialServerToClient
    public IdAwareTestBox otherBox;

    @IdentityAware
    @TrackValue(true)
    @SpecialServerToClient
    public IdAwareTestBox trackedOtherBox;

    @ClientSideConstructor
    public IdAwareTestBox() {
        super("Box", new Box(Vector3f.ZERO, 0.5f, 0.5f, 0.5f));
        reset();
    }

    @Override
    @ServerToClient
    public void setLocalTranslation(Vector3f localTranslation) {
        super.setLocalTranslation(localTranslation);
    }

    @Override
    public Vector3f getLocalTranslation() {
        return super.getLocalTranslation();
    }

    public void setOtherBox(IdAwareTestBox otherBox) {
        if (otherBox == null) {
            setLocalTranslation(Vector3f.ZERO);
        } else {
            setLocalTranslation(otherBox.getLocalTranslation().clone().add(new Vector3f(0f, 1.5f, 0f)));
        }
        this.otherBox = otherBox;
    }

    public void setTrackedOtherBox(IdAwareTestBox trackedOtherBox) {
        if (trackedOtherBox == null) {
            setLocalTranslation(Vector3f.ZERO);
        } else {
            setLocalTranslation(trackedOtherBox.getLocalTranslation().add(new Vector3f(0f, -1.5f, 0f)));
        }
        setterCalls++;
        this.trackedOtherBox = trackedOtherBox;
    }

    public void reset() {
        setterCalls = 0;
    }

    @Override
    public void setData(EntityData data) {
        this.data = data;
    }

    @Override
    public EntityData getData() {
        return data;
    }
}
