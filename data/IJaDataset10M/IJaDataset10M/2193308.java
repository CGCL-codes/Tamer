package networking;

import logic.common.player.Player;
import logic.nodes.lod.blocks.LeafBlock;
import main.InitGame;
import com.captiveimagination.jgn.synchronization.GraphicalController;
import com.captiveimagination.jgn.synchronization.message.Synchronize3DMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeRemoveMessage;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

public class GraphicSyncController implements GraphicalController<Spatial> {

    public static final float FULL_PROXIMITY = 1f;

    public static final float LOW_PROXIMITY = 0.5f;

    public static final float LOWEST_PROXIMITY = 0.001f;

    public GraphicSyncController() {
    }

    @Override
    public void applySynchronizationMessage(SynchronizeMessage message, Spatial spatial) {
        Synchronize3DMessage m = (Synchronize3DMessage) message;
        spatial.getLocalTranslation().x = m.getPositionX();
        spatial.getLocalTranslation().y = m.getPositionY();
        spatial.getLocalTranslation().z = m.getPositionZ();
        spatial.getLocalRotation().x = m.getRotationX();
        spatial.getLocalRotation().y = m.getRotationY();
        spatial.getLocalRotation().z = m.getRotationZ();
        spatial.getLocalRotation().w = m.getRotationW();
    }

    @Override
    public SynchronizeMessage createSynchronizationMessage(Spatial spatial) {
        Synchronize3DMessage message = new Synchronize3DMessage();
        message.setPositionX(spatial.getLocalTranslation().x);
        message.setPositionY(spatial.getLocalTranslation().y);
        message.setPositionZ(spatial.getLocalTranslation().z);
        message.setRotationX(spatial.getLocalRotation().x);
        message.setRotationY(spatial.getLocalRotation().y);
        message.setRotationZ(spatial.getLocalRotation().z);
        message.setRotationW(spatial.getLocalRotation().w);
        return message;
    }

    @Override
    public float proximity(Spatial spatial, short playerId) {
        return getProxmity(spatial, playerId);
    }

    public static float getProxmity(Spatial spatial, short playerId) {
        Player player = InitGame.get().getNetworkState().getPlayerFromShort(playerId);
        if (player != null && spatial == player.getHunter()) return FULL_PROXIMITY;
        if (spatial.getParent() == null) return LOWEST_PROXIMITY;
        Vector3f spatialLoc = spatial.getLocalTranslation();
        if (player == null) return LOWEST_PROXIMITY;
        if (!player.isAlive()) return LOW_PROXIMITY;
        float distance = player.getHunter().getLocalTranslation().distance(spatialLoc);
        if (LeafBlock.UPDATE_DIST <= distance) return LOWEST_PROXIMITY;
        return 1f - (distance / LeafBlock.VIEW_DIST);
    }

    @Override
    public boolean validateCreate(SynchronizeCreateMessage message) {
        return true;
    }

    @Override
    public boolean validateMessage(SynchronizeMessage message, Spatial object) {
        return true;
    }

    @Override
    public boolean validateRemove(SynchronizeRemoveMessage message) {
        return true;
    }
}
