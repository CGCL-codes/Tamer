package org.cart.igd.game;

import org.cart.igd.entity.Entity;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;
import org.cart.igd.states.*;
import org.cart.igd.gui.*;

public class TerrainEntity extends Entity {

    public int terrainId = -1;

    public int user[];

    public boolean used = false;

    public boolean fake = false;

    InGameState igs;

    public TerrainEntity(Vector3f pos, float fD, float bsr, OBJModel model, int terrainId, int user[], InGameState igs) {
        super(pos, fD, bsr, model);
        this.terrainId = terrainId;
        this.user = user;
        this.igs = igs;
    }

    public TerrainEntity(Vector3f pos, float fD, float bsr, OBJModel model, int terrainId, int user, InGameState igs) {
        super(pos, fD, bsr, model);
        this.terrainId = terrainId;
        this.user = new int[] { user };
        this.igs = igs;
    }

    public TerrainEntity(Vector3f pos, float fD, float bsr, OBJModel model, int terrainId, int user, InGameState igs, boolean fake) {
        super(pos, fD, bsr, model);
        this.terrainId = terrainId;
        this.user = new int[] { user };
        this.igs = igs;
        this.fake = fake;
    }

    public void save() {
        if (fake) {
            for (Entity e : igs.interactiveEntities) {
                if (e instanceof Animal) {
                    Animal a = ((Animal) e);
                    if (a.animalId == terrainId && a.state != 4 && a.state != 5) {
                        igs.inventory.PSYCH_FOUND_FAKE_SOLUTION = 1;
                        break;
                    }
                }
            }
        } else {
            if (used) return;
            boolean match = false;
            for (int i = 0; i < user.length; i++) {
                if (user[i] == igs.inventory.currentCursor) {
                    match = true;
                    break;
                }
            }
            if (!match) return;
            for (int i = 0; i < igs.interactiveEntities.size(); i++) {
                Entity e = igs.interactiveEntities.get(i);
                if (e instanceof Animal) {
                    if (((Animal) e).animalId == terrainId) {
                        if (((Animal) e).state == Inventory.READY_TO_SAVE) {
                            ((Animal) e).state = Inventory.JUST_SAVED;
                            ((Dialogue) igs.gui.get(1)).createDialogue((Animal) e, igs);
                            igs.changeGuiState(1);
                            used = true;
                        }
                    }
                }
            }
        }
    }
}
