package mirrormonkey.state.tools.listeners;

import java.util.concurrent.Callable;
import mirrormonkey.core.EntityLifecycleListener;
import mirrormonkey.framework.entity.StaticEntityData;
import mirrormonkey.framework.entity.SyncEntity;
import mirrormonkey.state.SingleClientTestProto;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

public class MakeVisibleListener implements EntityLifecycleListener {

    /**
	 * @param singleClientTestProto
	 */
    public MakeVisibleListener() {
    }

    public Material createMaterial(ColorRGBA color) {
        Material m = new Material(SingleClientTestProto.clientApplication.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", color);
        return m;
    }

    @Override
    public void entityInitialized(SyncEntity entity) {
        if (Geometry.class.isInstance(entity)) {
            final Geometry g = (Geometry) entity;
            if (g.getMaterial() == null) {
                Material m = createMaterial(ColorRGBA.Blue);
                g.setMaterial(m);
            }
            SingleClientTestProto.clientApplication.enqueue(new Callable<Object>() {

                @Override
                public Object call() {
                    SingleClientTestProto.clientApplication.getRootNode().attachChild(g);
                    return null;
                }
            });
        }
    }

    @Override
    public void entityReplaced(SyncEntity replacedEntity, StaticEntityData replacedStaticData, SyncEntity replacingEntity) {
        entityRemoved(replacedEntity);
    }

    @Override
    public void entityReplacing(SyncEntity replacedEntity, StaticEntityData replacedStaticData, SyncEntity replacingEntity) {
        entityInitialized(replacingEntity);
    }

    @Override
    public void entityRemoved(SyncEntity entity) {
        if (Geometry.class.isInstance(entity)) {
            Geometry g = (Geometry) entity;
            SingleClientTestProto.clientApplication.getRootNode().detachChild(g);
        }
    }
}
