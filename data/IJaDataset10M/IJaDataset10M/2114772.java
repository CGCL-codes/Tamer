package org.openaion.gameserver.model.siege;

import org.openaion.gameserver.controllers.FortressGateController;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.templates.VisibleObjectTemplate;
import org.openaion.gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author Sylar
 *
 */
public class FortressGate extends Npc {

    private int fortressId;

    private FortressGateArtifact gateArtifact;

    private int spawnStaticId;

    public FortressGate(int objId, FortressGateController controller, SpawnTemplate spawn, VisibleObjectTemplate objectTemplate, int fortressId, int staticId) {
        super(objId, controller, spawn, objectTemplate);
        this.fortressId = fortressId;
        this.spawnStaticId = staticId;
    }

    public int getFortressId() {
        return fortressId;
    }

    public void setArtifact(FortressGateArtifact artifact) {
        gateArtifact = artifact;
        gateArtifact.setRelatedGate(this);
    }

    public int getStaticId() {
        return spawnStaticId;
    }
}
