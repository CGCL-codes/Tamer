package xreal;

import javax.vecmath.Vector3f;
import xreal.common.PlayerMovementType;
import xreal.common.PlayerStatsType;

public interface PlayerStateAccess {

    public int getPlayerState_commandTime();

    public void setPlayerState_commandTime(int commandTime);

    public PlayerMovementType getPlayerState_pm_type();

    public void setPlayerState_pm_type(PlayerMovementType pm_type);

    public int getPlayerState_pm_flags();

    public void setPlayerState_pm_flags(int pm_flags);

    public void addPlayerState_pm_flags(int pm_flags);

    public void delPlayerState_pm_flags(int pm_flags);

    public boolean hasPlayerState_pm_flags(int pm_flags);

    public int getPlayerState_pm_time();

    public void setPlayerState_pm_time(int pm_time);

    public int getPlayerState_bobCycle();

    public void setPlayerState_bobCycle(int bobCycle);

    public Vector3f getPlayerState_origin();

    public void setPlayerState_origin(Vector3f origin);

    public Vector3f getPlayerState_velocity();

    public void setPlayerState_velocity(Vector3f velocity);

    public int getPlayerState_weaponTime();

    public void setPlayerState_weaponTime(int weaponTime);

    public int getPlayerState_gravity();

    public void setPlayerState_gravity(int gravity);

    public int getPlayerState_speed();

    public void setPlayerState_speed(int speed);

    public short getPlayerState_deltaPitch();

    public void setPlayerState_deltaPitch(short deltaPitch);

    public short getPlayerState_deltaYaw();

    public void setPlayerState_deltaYaw(short deltaYaw);

    public short getPlayerState_deltaRoll();

    public void setPlayerState_deltaRoll(short deltaRoll);

    public int getPlayerState_groundEntityNum();

    public void setPlayerState_groundEntityNum(int groundEntityNum);

    public int getPlayerState_legsTimer();

    public void setPlayerState_legsTimer(int legsTimer);

    public int getPlayerState_legsAnim();

    public void setPlayerState_legsAnim(int legsAnim);

    public int getPlayerState_torsoTimer();

    public void setPlayerState_torsoTimer(int torsoTimer);

    public int getPlayerState_torsoAnim();

    public void setPlayerState_torsoAnim(int torsoAnim);

    public int getPlayerState_movementDir();

    public void setPlayerState_movementDir(int movementDir);

    public Vector3f getPlayerState_grapplePoint();

    public void setPlayerState_grapplePoint(Vector3f grapplePoint);

    public int getPlayerState_eFlags();

    public void setPlayerState_eFlags(int flags);

    public int getPlayerState_eventSequence();

    public void setPlayerState_eventSequence(int eventSequence);

    public int getPlayerState_externalEvent();

    public void setPlayerState_externalEvent(int externalEvent);

    public int getPlayerState_externalEventParm();

    public void setPlayerState_externalEventParm(int externalEventParm);

    public int getPlayerState_externalEventTime();

    public void setPlayerState_externalEventTime(int externalEventTime);

    public int getPlayerState_clientNum();

    public int getPlayerState_weapon();

    public void setPlayerState_weapon(int weapon);

    public int getPlayerState_weaponState();

    public void setPlayerState_weaponState(int weaponState);

    public Angle3f getPlayerState_viewAngles();

    public void setPlayerState_viewAngles(Angle3f viewAngles);

    public void setPlayerState_viewAngles(float pitch, float yaw, float roll);

    public int getPlayerState_viewHeight();

    public void setPlayerState_viewHeight(int viewHeight);

    public int getPlayerState_damageEvent();

    public void setPlayerState_damageEvent(int damageEvent);

    public int getPlayerState_damageYaw();

    public void setPlayerState_damageYaw(int damageYaw);

    public int getPlayerState_damagePitch();

    public void setPlayerState_damagePitch(int damagePitch);

    public int getPlayerState_damageCount();

    public void setPlayerState_damageCount(int damageCount);

    public int getPlayerState_generic1();

    public void setPlayerState_generic1(int generic1);

    public int getPlayerState_loopSound();

    public void setPlayerState_loopSound(int loopSound);

    public int getPlayerState_jumppad_ent();

    public void setPlayerState_jumppad_ent(int jumppad_ent);

    public int getPlayerState_ping();

    public void setPlayerState_ping(int ping);

    public int getPlayerState_stat(PlayerStatsType stat);

    public void setPlayerState_stat(PlayerStatsType stat, int value);
}
