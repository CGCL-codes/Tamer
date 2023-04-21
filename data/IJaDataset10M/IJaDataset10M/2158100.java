package gameserver.model.siege;

import gameserver.controllers.FortressGateController;
import gameserver.model.gameobjects.Npc;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;

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
