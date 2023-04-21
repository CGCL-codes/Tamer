package com.es.systems.movement;

import com.artemis.ComponentMapper;
import com.artemis.ComponentType;
import com.artemis.ComponentTypeManager;
import com.artemis.Entity;
import com.es.components.gamelogic.AI;
import com.es.components.gamelogic.Acceleration;
import com.es.components.gamelogic.Control;
import com.es.components.gamelogic.Homing;
import com.es.components.gamelogic.Position;
import com.es.components.gamelogic.Speed;
import com.es.components.references.Target;
import com.es.components.references.Team;
import com.es.components.text.Name;
import com.es.manager.Utility;
import com.es.state.GameWorld;
import org.newdawn.slick.GameContainer;

public class AiInputSystem extends ControlSystem {

    private ComponentMapper<Control> controlMapper;

    private ComponentMapper<Target> targetMapper;

    private ComponentMapper<Position> positionMapper;

    private ComponentMapper<AI> aiMapper;

    private ComponentType positionType;

    private ComponentType teamType;

    private ComponentType speedType;

    public AiInputSystem(int interval, GameContainer container) {
        super(interval, container, Position.class, AI.class, Control.class, Target.class);
    }

    @Override
    public void initialize() {
        controlMapper = new ComponentMapper<Control>(Control.class, world.getEntityManager());
        positionMapper = new ComponentMapper<Position>(Position.class, world.getEntityManager());
        targetMapper = new ComponentMapper<Target>(Target.class, world.getEntityManager());
        aiMapper = new ComponentMapper<AI>(AI.class, world.getEntityManager());
        positionType = ComponentTypeManager.getTypeFor(Position.class);
        speedType = ComponentTypeManager.getTypeFor(Speed.class);
        teamType = ComponentTypeManager.getTypeFor(Team.class);
    }

    @Override
    protected void begin() {
    }

    @Override
    protected void process(Entity entity) {
        Position pos = positionMapper.get(entity);
        AI ai = aiMapper.get(entity);
        Control control = controlMapper.get(entity);
        Target target = targetMapper.get(entity);
        Position targetPos = null;
        if (target.primary != null) {
            targetPos = (Position) target.primary.getComponent(positionType);
            if (targetPos == null) {
                target.primary = null;
            } else if (!withinRadius(pos, targetPos, ai.getChaseRadius())) {
                ai.randomiseChaseRadius();
                target.primary = null;
            }
        }
        if (target.primary == null) {
            Entity closest = Utility.getClosestEnemy((GameWorld) world, entity);
            if (closest != null) {
                targetPos = (Position) closest.getComponent(positionType);
                if (withinRadius(pos, targetPos, ai.getAggroRadius())) {
                    target.primary = closest;
                }
            }
        }
        switch(ai.state) {
            case IDLE:
                changeState(ai, AI.AIState.MOVING);
                break;
            case MOVING:
                if (target.primary == null) {
                    Speed sp = (Speed) entity.getComponent(Speed.TYPE);
                    if (sp != null) {
                        sp.getSpeed().x = 0;
                        sp.getSpeed().y = 0;
                    }
                    changeState(ai, AI.AIState.TURNING);
                } else {
                    if (entity.getComponent(Acceleration.TYPE) == null) {
                        entity.addComponent(new Acceleration(0));
                        entity.refresh();
                    }
                    ((Acceleration) entity.getComponent(Acceleration.TYPE)).acceleration = control.getAcceleration();
                    if (!withinAngle(pos, targetPos, 60) || Utility.getDistanceBetweenSquared(pos.x, pos.y, targetPos.x, targetPos.y) > Math.pow(ai.getMoveRadius(), 2)) {
                        changeState(ai, AI.AIState.TURNING);
                    }
                }
                break;
            case TURNING:
                if (target.primary == null) {
                    changeState(ai, AI.AIState.MOVING);
                    break;
                } else {
                    Homing homing = (Homing) entity.getComponent(Homing.TYPE);
                    if (homing == null) {
                        homing = new Homing(control.getRotation());
                        entity.addComponent(homing);
                        entity.refresh();
                    }
                    homing.turnRate = control.getRotation();
                    homing.leadSpeed = ai.getLeadSpeed();
                    if (targetPos != null) {
                        if (withinAngle(pos, targetPos, ai.getThrustAngleUse()) || Utility.getDistanceBetweenSquared(pos.x, pos.y, targetPos.x, targetPos.y) < Math.pow(ai.getTurnRadius(), 2)) {
                            homing.turnRate = 0;
                            changeState(ai, AI.AIState.MOVING);
                        }
                    }
                }
        }
        if (target.primary != null) {
            targetPos = (Position) target.primary.getComponent(positionType);
            if (targetPos != null) {
                float targetAngle = Utility.getTargetAngle(pos.x, pos.y, targetPos.x, targetPos.y);
                if (withinRadius(pos, targetPos, ai.getPrimaryWeaponRadius())) {
                    smartFireAllWeaponsOnSlot(entity, 0, targetAngle);
                }
                if (withinRadius(pos, targetPos, ai.getSecondaryWeaponRadius())) {
                    smartFireAllWeaponsOnSlot(entity, 1, targetAngle);
                }
                if (withinRadius(pos, targetPos, ai.getTertiaryWeaponRadius())) {
                    smartFireAllWeaponsOnSlot(entity, 2, targetAngle);
                }
            }
        }
    }

    protected boolean withinRadius(Position from, Position to, float radius) {
        return Utility.getDistanceBetweenSquared(from.x, from.y, to.x, to.y) <= Math.pow(radius, 2);
    }

    private void changeState(AI ai, AI.AIState newState) {
        ai.state = newState;
        ai.randomiseChaseMoveRadius();
        ai.randomiseChaseTurnRadius();
        ai.randomiseChaseThrustAngle();
    }

    private boolean withinAngle(Position pos, Position targetPos, float maxAngle) {
        return Math.abs(Utility.getTargetAngle(pos.x, pos.y, targetPos.x, targetPos.y) - pos.rotation) <= maxAngle;
    }

    private void printAiTargetString(Entity entity, Entity closest, String middleString) {
        Name aiName = (Name) entity.getComponent(Name.TYPE);
        if (aiName != null) {
            System.out.print(aiName.name + " ");
        }
        System.out.print(entity);
        System.out.print(" " + middleString + " ");
        Name name = (Name) closest.getComponent(Name.TYPE);
        if (name != null) {
            System.out.print(name.name + " ");
        }
        System.out.println(closest);
    }
}
