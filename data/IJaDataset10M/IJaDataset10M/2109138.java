package openwar.battle;

import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.UpdateControl;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import openwar.battle.formations.BoxFormation;
import openwar.battle.formations.CircleFormation;
import openwar.battle.formations.Formation;

/**
 *
 * @author kehl
 */
public class Unit {

    public enum Status {

        Idle, Walk, Run, Attack, Fight, Retreat, Rout
    }

    ;

    public String refName;

    public String owner;

    public UnitMeleeStats meleeStats;

    public UnitRangeStats rangeStats;

    public boolean selected, run, invertFormation, previewFormation;

    public float morale = 100f, stamina = 100f;

    public ArrayList<Soldier> soldiers;

    public BattleAppState battle;

    public Vector2f currPos, goalPos, goalDir, currDir, oldGoalDir;

    public Status status;

    public Formation formation;

    public Unit attackedUnit;

    public Node cone;

    public Unit(BattleAppState b, openwar.DB.Unit U, String player) {
        battle = b;
        status = Unit.Status.Idle;
        owner = player;
        currPos = new Vector2f(0, 0);
        goalPos = new Vector2f(0, 0);
        currDir = new Vector2f(0, 1);
        goalDir = new Vector2f(0, 1);
        oldGoalDir = new Vector2f(0, 1);
        soldiers = new ArrayList<Soldier>();
        for (int i = 0; i < U.count; i++) {
            soldiers.add(new Soldier(this));
        }
        formation = new BoxFormation(this);
    }

    public void createData() {
        cone = new Node("");
        Spatial dome = (Spatial) new Geometry("", new Dome(Vector3f.ZERO, 2, 12, 1, false));
        dome.setLocalScale(1, 5, 1);
        cone.attachChild(dome);
        cone.attachChild((Spatial) new Geometry("", new Sphere(12, 12, 1)));
        cone.setMaterial(new Material(battle.game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md"));
        Quaternion q = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Z);
        Quaternion l = new Quaternion().fromAngleAxis(FastMath.atan2(currDir.y, currDir.x), Vector3f.UNIT_Y);
        cone.setLocalRotation(l.multLocal(q));
        for (Soldier s : soldiers) {
            s.createData();
            battle.sceneNode.attachChild(s.node);
        }
    }

    public void update(float tpf) {
        currPos.x = currPos.y = currDir.x = currDir.y = 0;
        boolean allIdle = true;
        for (final Soldier s : soldiers) {
            s.update(tpf);
            allIdle &= (s.status == Soldier.Status.Idle);
            currPos.addLocal(s.currPos);
            currDir.addLocal(s.currDir);
            if (s.status == Soldier.Status.Dead) {
                battle.sceneNode.getControl(UpdateControl.class).enqueue(new Callable() {

                    @Override
                    public Object call() throws Exception {
                        s.node.detachChild(s.selectionQuad);
                        battle.game.bulletState.getPhysicsSpace().remove(s.collControl);
                        battle.game.bulletState.getPhysicsSpace().remove(s.meleeControl);
                        if (rangeStats != null) {
                            battle.game.bulletState.getPhysicsSpace().remove(s.rangeControl);
                        }
                        soldiers.remove(s);
                        return null;
                    }
                });
            }
        }
        currPos.divideLocal(soldiers.size());
        currDir.normalizeLocal();
        switch(status) {
            case Idle:
                stamina += tpf * 0.01f;
                break;
            case Walk:
                stamina -= tpf * 0.1f;
                if (allIdle) {
                    status = Unit.Status.Idle;
                    System.out.println("Unit is idle");
                }
                break;
            case Run:
                stamina -= tpf * 0.5f;
                if (allIdle) {
                    status = Unit.Status.Idle;
                    System.out.println("Unit is idle");
                }
                break;
        }
        stamina = battle.ensureMinMax(0f, 100f, stamina);
        if (!selected) {
            return;
        }
        cone.setLocalTranslation(currPos.x, battle.terrain.terrainQuad.getHeight(currPos) + 5, currPos.y);
        Quaternion q = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Z);
        Quaternion l = new Quaternion().fromAngleAxis(FastMath.atan2(goalDir.y, goalDir.x), Vector3f.UNIT_Y);
        cone.setLocalRotation(l.multLocal(q));
    }

    public void setPosition(float x, float z, float dx, float dz) {
        currPos.x = x;
        currPos.y = z;
        currDir.x = dx;
        currDir.y = dz;
        currDir.normalizeLocal();
        goalPos = currPos.clone();
        goalDir = currDir.clone();
        formation.doFormation(false, true, false);
    }

    public void setFormation(Formation form, boolean warp) {
        formation = form;
        formation.doFormation(run, warp, invertFormation);
    }

    public void setGoal(float x, float z, float dx, float dz, boolean run) {
        oldGoalDir = goalDir.clone();
        goalDir.x = -dx;
        goalDir.y = dz;
        goalDir.normalizeLocal();
        setGoal(x, z, run);
    }

    public void setGoal(float x, float z, boolean run) {
        goalPos.x = x;
        goalPos.y = z;
        if (run) {
            status = Status.Run;
            System.out.println("Unit is running");
        } else {
            status = Status.Walk;
            System.out.println("Unit is walking");
        }
        invertFormation ^= goalDir.smallestAngleBetween(oldGoalDir) > FastMath.HALF_PI;
        formation.doFormation(run, false, invertFormation);
        this.run = run;
    }

    public void toggleSelection(boolean select) {
        if (selected == select) {
            return;
        }
        selected = select;
        if (selected) {
            battle.sceneNode.attachChild(cone);
        } else {
            battle.sceneNode.detachChild(cone);
        }
        for (Soldier s : soldiers) {
            s.select(select);
        }
    }

    public void togglePreviewFormation(boolean select) {
        previewFormation = select;
        if (previewFormation) {
            for (Soldier s : soldiers) {
                battle.sceneNode.attachChild(s.previewQuad);
            }
        } else {
            for (Soldier s : soldiers) {
                battle.sceneNode.detachChild(s.previewQuad);
            }
        }
    }

    public void previewFormation(Vector3f start, Vector3f end, boolean accept) {
        formation.previewFormation(start.x, start.z, end.x, end.z, accept);
    }
}
