package xreal.client;

import java.util.Arrays;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import xreal.Angle3f;
import xreal.PlayerStateAccess;
import xreal.TrajectoryType;
import xreal.common.Config;
import xreal.common.EntityType;
import xreal.common.PlayerMovementFlags;
import xreal.common.PlayerMovementType;
import xreal.common.PlayerStatsType;

/**
 * playerState_t is the information needed by both the client and server to predict player motion and actions- Nothing
 * outside of the PlayerController should modify these, or some degree of prediction error will occur.
 * 
 * You can't add anything to this without modifying the code in msg.c.
 * 
 * playerState_t is a full superset of entityState_t as it is used by players, so if a playerState_t is transmitted, the
 * entityState_t can be fully derived from it.
 * 
 * @author Robert Beckebans
 */
public class PlayerState extends EntityState implements Cloneable, PlayerStateAccess {

    static final int MAX_STATS = 16;

    static final int MAX_PERSISTANT = 16;

    static final int MAX_POWERUPS = 16;

    static final int MAX_WEAPONS = 16;

    public static final int MAX_PS_EVENTS = 2;

    /** cmd->serverTime of last executed command */
    public int commandTime;

    public PlayerMovementType pm_type;

    public int pm_flags;

    public int pm_time;

    /** for view bobbing and footstep generation */
    public int bobCycle;

    public Vector3f origin;

    public Vector3f velocity;

    public int weaponTime;

    public int gravity;

    public int speed;

    /**
	 * add to command angles to get view direction changed by spawns, rotating objects, and teleporters
	 */
    public int deltaPitch;

    public int deltaYaw;

    public int deltaRoll;

    /** ENTITYNUM_NONE = in air */
    public int groundEntityNum;

    /** don't change low priority animations until this runs out */
    public int legsTimer;

    /** mask off ANIM_TOGGLEBIT */
    public int legsAnim;

    /** don't change low priority animations until this runs out */
    public int torsoTimer;

    /** mask off ANIM_TOGGLEBIT */
    public int torsoAnim;

    /**
	 * a number 0 to 7 that represents the relative angle of movement to the view angle (axial and diagonals) when at
	 * rest, the value will remain unchanged used to twist the legs during strafing
	 */
    public int movementDir;

    /** location of grapple to pull towards if PMF_GRAPPLE_PULL */
    private Vector3f grapplePoint;

    /** pmove generated events */
    public int eventSequence;

    private int events[];

    private int eventParms[];

    /** events set on player from another source */
    public int externalEvent;

    public int externalEventParm;

    public int externalEventTime;

    /** ranges from 0 to MAX_CLIENTS-1 */
    private int clientNum;

    /** copied to entityState_t->weapon */
    public int weapon;

    public int weaponState;

    /** for fixed views */
    public Angle3f viewAngles;

    public int viewHeight;

    /** when it changes, latch the other parms */
    public int damageEvent;

    public int damageYaw;

    public int damagePitch;

    public int damageCount;

    /** MAX_STATS many */
    private int stats[];

    /** MAX_PERSISTANT many stats that aren't cleared on death */
    private int persistant[];

    /** MAX_POWERUPS many level.time that the powerup runs out */
    private int powerups[];

    /** MAX_WEAPONS */
    private int ammo[];

    public int generic1;

    public int loopSound;

    /** jumppad entity hit this frame */
    public int jumppad_ent;

    public int ping;

    public int pmove_framecount;

    public int jumppad_frame;

    public int entityEventSequence;

    public PlayerState(int commandTime, int pmType, int pmFlags, int pmTime, int bobCycle, Vector3f origin, Vector3f velocity, int weaponTime, int gravity, int speed, int deltaPitch, int deltaYaw, int deltaRoll, int groundEntityNum, int legsTimer, int legsAnim, int torsoTimer, int torsoAnim, int movementDir, Vector3f grapplePoint, int eFlags, int eventSequence, int[] events, int[] eventParms, int externalEvent, int externalEventParm, int externalEventTime, int clientNum, int weapon, int weaponState, Angle3f viewAngles, int viewHeight, int damageEvent, int damageYaw, int damagePitch, int damageCount, int[] stats, int[] persistant, int[] powerups, int[] ammo, int generic1, int loopSound, int jumppadEnt, int ping, int pmoveFramecount, int jumppadFrame, int entityEventSequence) {
        super(clientNum);
        this.commandTime = commandTime;
        pm_type = PlayerMovementType.values()[pmType];
        pm_flags = pmFlags;
        pm_time = pmTime;
        this.bobCycle = bobCycle;
        this.origin = origin;
        this.velocity = velocity;
        this.weaponTime = weaponTime;
        this.gravity = gravity;
        this.speed = speed;
        this.deltaPitch = deltaPitch;
        this.deltaYaw = deltaYaw;
        this.deltaRoll = deltaRoll;
        this.groundEntityNum = groundEntityNum;
        this.legsTimer = legsTimer;
        this.legsAnim = legsAnim;
        this.torsoTimer = torsoTimer;
        this.torsoAnim = torsoAnim;
        this.movementDir = movementDir;
        this.grapplePoint = grapplePoint;
        this.eFlags = eFlags;
        this.eventSequence = eventSequence;
        this.events = events;
        this.eventParms = eventParms;
        this.externalEvent = externalEvent;
        this.externalEventParm = externalEventParm;
        this.externalEventTime = externalEventTime;
        this.clientNum = clientNum;
        this.weapon = weapon;
        this.weaponState = weaponState;
        this.viewAngles = viewAngles;
        this.viewHeight = viewHeight;
        this.damageEvent = damageEvent;
        this.damageYaw = damageYaw;
        this.damagePitch = damagePitch;
        this.damageCount = damageCount;
        this.stats = stats;
        this.persistant = persistant;
        this.powerups = powerups;
        this.ammo = ammo;
        this.generic1 = generic1;
        this.loopSound = loopSound;
        jumppad_ent = jumppadEnt;
        this.ping = ping;
        pmove_framecount = pmoveFramecount;
        jumppad_frame = jumppadFrame;
        this.entityEventSequence = entityEventSequence;
    }

    /**
	 * This is done after each set of usercmd_t on the server, and after local prediction on the client
	 * 
	 * @param snap
	 * @return
	 */
    public EntityState createEntityState(boolean snap) {
        EntityState s = new EntityState(clientNum);
        if (pm_type == PlayerMovementType.INTERMISSION || pm_type == PlayerMovementType.SPECTATOR) {
            s.eType = EntityType.INVISIBLE;
        } else if (this.stats[PlayerStatsType.HEALTH.ordinal()] <= Config.GIB_HEALTH) {
            s.eType = EntityType.INVISIBLE;
        } else {
            s.eType = EntityType.PLAYER;
        }
        s.pos.trType = TrajectoryType.INTERPOLATE;
        s.pos.trBase = new Vector4f(this.origin);
        if (snap) {
            s.pos.trBase.snap();
        }
        s.origin = new Vector3f(s.pos.trBase.x, s.pos.trBase.y, s.pos.trBase.z);
        s.origin2 = new Vector3f();
        s.pos.trDelta = new Vector4f(this.velocity);
        s.apos.trType = TrajectoryType.INTERPOLATE;
        s.apos.trBase = new Vector4f(this.viewAngles);
        if (snap) {
            s.apos.trBase.snap();
        }
        s.angles = new Angle3f(s.apos.trBase.x, s.apos.trBase.y, s.apos.trBase.z);
        s.time2 = this.movementDir;
        s.legsAnim = this.legsAnim;
        s.torsoAnim = this.torsoAnim;
        s.clientNum = this.clientNum;
        s.eFlags = this.eFlags;
        if (this.stats[PlayerStatsType.HEALTH.ordinal()] <= 0) {
            s.setEntityFlag_dead(true);
        } else {
            s.setEntityFlag_dead(false);
        }
        if (this.externalEvent > 0) {
            s.event = this.externalEvent;
            s.eventParm = this.externalEventParm;
        } else if (this.entityEventSequence < this.eventSequence) {
            int seq;
            if (this.entityEventSequence < this.eventSequence - MAX_PS_EVENTS) {
                this.entityEventSequence = this.eventSequence - MAX_PS_EVENTS;
            }
            seq = this.entityEventSequence & (MAX_PS_EVENTS - 1);
            s.event = this.events[seq] | ((this.entityEventSequence & 3) << 8);
            s.eventParm = this.eventParms[seq];
            this.entityEventSequence++;
        }
        s.weapon = this.weapon;
        s.groundEntityNum = this.groundEntityNum;
        s.powerups = 0;
        for (int i = 0; i < MAX_POWERUPS; i++) {
            if (this.powerups[i] > 0) {
                s.powerups |= 1 << i;
            }
        }
        s.angles2 = new Angle3f(this.grapplePoint);
        if ((this.pm_flags & PlayerMovementFlags.WALLCLIMBINGCEILING) != 0) s.setEntityFlag_wallClimbCeiling(true);
        s.loopSound = this.loopSound;
        s.generic1 = this.generic1;
        return s;
    }

    @Override
    public String toString() {
        return String.format("PlayerState [\n\tammo=%s,\n\tbobCycle=%s,\n\tclientNum=%s,\n\tcommandTime=%s,\n\tdamageCount=%s,\n\tdamageEvent=%s,\n\tdamagePitch=%s,\n\tdamageYaw=%s,\n\tdeltaPitch=%s,\n\tdeltaRoll=%s,\n\tdeltaYaw=%s,\n\teFlags=%s,\n\tentityEventSequence=%s,\n\teventParms=%s,\n\teventSequence=%s,\n\tevents=%s,\n\texternalEvent=%s,\n\texternalEventParm=%s,\n\texternalEventTime=%s,\n\tgeneric1=%s,\n\tgrapplePoint=%s,\n\tgravity=%s,\n\tgroundEntityNum=%s,\n\tjumppad_ent=%s,\n\tjumppad_frame=%s,\n\tlegsAnim=%s,\n\tlegsTimer=%s,\n\tloopSound=%s,\n\tmovementDir=%s,\n\torigin=%s,\n\tpersistant=%s,\n\tping=%s,\n\tpm_flags=%s,\n\tpm_time=%s,\n\tpm_type=%s,\n\tpmove_framecount=%s,\n\tpowerups=%s,\n\tspeed=%s,\n\tstats=%s,\n\ttorsoAnim=%s,\n\ttorsoTimer=%s,\n\tvelocity=%s,\n\tviewAngles=%s,\n\tviewHeight=%s,\n\tweapon=%s,\n\tweaponState=%s,\n\tweaponTime=%s\t\n]", Arrays.toString(ammo), bobCycle, clientNum, commandTime, damageCount, damageEvent, damagePitch, damageYaw, deltaPitch, deltaRoll, deltaYaw, eFlags, entityEventSequence, Arrays.toString(eventParms), eventSequence, Arrays.toString(events), externalEvent, externalEventParm, externalEventTime, generic1, grapplePoint, gravity, groundEntityNum, jumppad_ent, jumppad_frame, legsAnim, legsTimer, loopSound, movementDir, origin, Arrays.toString(persistant), ping, pm_flags, pm_time, pm_type, pmove_framecount, Arrays.toString(powerups), speed, Arrays.toString(stats), torsoAnim, torsoTimer, velocity, viewAngles, viewHeight, weapon, weaponState, weaponTime);
    }

    public Object clone() {
        PlayerState ps = new PlayerState(commandTime, pm_type.ordinal(), pm_flags, pm_time, bobCycle, new Vector3f(origin), new Vector3f(velocity), weaponTime, gravity, speed, deltaPitch, deltaYaw, deltaRoll, groundEntityNum, legsTimer, legsAnim, torsoTimer, torsoAnim, movementDir, new Vector3f(grapplePoint), eFlags, eventSequence, events.clone(), eventParms.clone(), externalEvent, externalEventParm, externalEventTime, clientNum, weapon, weaponState, new Angle3f(viewAngles), viewHeight, damageEvent, damageYaw, damagePitch, damageCount, stats.clone(), persistant.clone(), powerups.clone(), ammo.clone(), generic1, loopSound, jumppad_ent, ping, pmove_framecount, jumppad_frame, entityEventSequence);
        return ps;
    }

    @Override
    public void addPlayerState_pm_flags(int pmFlags) {
        pm_flags |= pmFlags;
    }

    @Override
    public void delPlayerState_pm_flags(int pmFlags) {
        pm_flags &= ~pm_flags;
    }

    @Override
    public int getPlayerState_bobCycle() {
        return bobCycle;
    }

    @Override
    public int getPlayerState_clientNum() {
        return clientNum;
    }

    @Override
    public int getPlayerState_commandTime() {
        return commandTime;
    }

    @Override
    public int getPlayerState_damageCount() {
        return damageCount;
    }

    @Override
    public int getPlayerState_damageEvent() {
        return damageEvent;
    }

    @Override
    public int getPlayerState_damagePitch() {
        return damagePitch;
    }

    @Override
    public int getPlayerState_damageYaw() {
        return damageYaw;
    }

    @Override
    public short getPlayerState_deltaPitch() {
        return (short) deltaPitch;
    }

    @Override
    public short getPlayerState_deltaRoll() {
        return (short) deltaRoll;
    }

    @Override
    public short getPlayerState_deltaYaw() {
        return (short) deltaYaw;
    }

    @Override
    public int getPlayerState_eFlags() {
        return eFlags;
    }

    @Override
    public int getPlayerState_eventSequence() {
        return eventSequence;
    }

    @Override
    public int getPlayerState_externalEvent() {
        return externalEvent;
    }

    @Override
    public int getPlayerState_externalEventParm() {
        return externalEventParm;
    }

    @Override
    public int getPlayerState_externalEventTime() {
        return externalEventTime;
    }

    @Override
    public int getPlayerState_generic1() {
        return generic1;
    }

    @Override
    public Vector3f getPlayerState_grapplePoint() {
        return new Vector3f(grapplePoint);
    }

    @Override
    public int getPlayerState_gravity() {
        return gravity;
    }

    @Override
    public int getPlayerState_groundEntityNum() {
        return groundEntityNum;
    }

    @Override
    public int getPlayerState_jumppad_ent() {
        return jumppad_ent;
    }

    @Override
    public int getPlayerState_legsAnim() {
        return legsAnim;
    }

    @Override
    public int getPlayerState_legsTimer() {
        return legsTimer;
    }

    @Override
    public int getPlayerState_loopSound() {
        return loopSound;
    }

    @Override
    public int getPlayerState_movementDir() {
        return movementDir;
    }

    @Override
    public Vector3f getPlayerState_origin() {
        return new Vector3f(origin);
    }

    @Override
    public int getPlayerState_ping() {
        return ping;
    }

    @Override
    public int getPlayerState_pm_flags() {
        return pm_flags;
    }

    @Override
    public int getPlayerState_pm_time() {
        return pm_time;
    }

    @Override
    public PlayerMovementType getPlayerState_pm_type() {
        return pm_type;
    }

    @Override
    public int getPlayerState_speed() {
        return speed;
    }

    @Override
    public int getPlayerState_torsoAnim() {
        return torsoAnim;
    }

    @Override
    public int getPlayerState_torsoTimer() {
        return torsoTimer;
    }

    @Override
    public Vector3f getPlayerState_velocity() {
        return new Vector3f(velocity);
    }

    @Override
    public Angle3f getPlayerState_viewAngles() {
        return new Angle3f(viewAngles);
    }

    @Override
    public int getPlayerState_viewHeight() {
        return viewHeight;
    }

    @Override
    public int getPlayerState_weapon() {
        return weapon;
    }

    @Override
    public int getPlayerState_weaponState() {
        return weaponState;
    }

    @Override
    public int getPlayerState_weaponTime() {
        return weaponTime;
    }

    @Override
    public boolean hasPlayerState_pm_flags(int pmFlags) {
        return (pm_flags & pmFlags) != 0;
    }

    @Override
    public void setPlayerState_bobCycle(int bobCycle) {
        this.bobCycle = bobCycle;
    }

    @Override
    public void setPlayerState_commandTime(int commandTime) {
        this.commandTime = commandTime;
    }

    @Override
    public void setPlayerState_damageCount(int damageCount) {
        this.damageCount = damageCount;
    }

    @Override
    public void setPlayerState_damageEvent(int damageEvent) {
        this.damageEvent = damageEvent;
    }

    @Override
    public void setPlayerState_damagePitch(int damagePitch) {
        this.damagePitch = damagePitch;
    }

    @Override
    public void setPlayerState_damageYaw(int damageYaw) {
        this.damageYaw = damageYaw;
    }

    @Override
    public void setPlayerState_deltaPitch(short deltaPitch) {
        this.deltaPitch = deltaPitch;
    }

    @Override
    public void setPlayerState_deltaRoll(short deltaRoll) {
        this.deltaRoll = deltaRoll;
    }

    @Override
    public void setPlayerState_deltaYaw(short deltaYaw) {
        this.deltaYaw = deltaYaw;
    }

    @Override
    public void setPlayerState_eFlags(int flags) {
        this.eFlags = flags;
    }

    @Override
    public void setPlayerState_eventSequence(int eventSequence) {
        this.eventSequence = eventSequence;
    }

    @Override
    public void setPlayerState_externalEvent(int externalEvent) {
        this.externalEvent = externalEvent;
    }

    @Override
    public void setPlayerState_externalEventParm(int externalEventParm) {
        this.externalEventParm = externalEventParm;
    }

    @Override
    public void setPlayerState_externalEventTime(int externalEventTime) {
        this.externalEventTime = externalEventTime;
    }

    @Override
    public void setPlayerState_generic1(int generic1) {
        this.generic1 = generic1;
    }

    @Override
    public void setPlayerState_grapplePoint(Vector3f grapplePoint) {
        this.grapplePoint = grapplePoint;
    }

    @Override
    public void setPlayerState_gravity(int gravity) {
        this.gravity = gravity;
    }

    @Override
    public void setPlayerState_groundEntityNum(int groundEntityNum) {
        this.groundEntityNum = groundEntityNum;
    }

    @Override
    public void setPlayerState_jumppad_ent(int jumppadEnt) {
        this.jumppad_ent = jumppadEnt;
    }

    @Override
    public void setPlayerState_legsAnim(int legsAnim) {
        this.legsAnim = legsAnim;
    }

    @Override
    public void setPlayerState_legsTimer(int legsTimer) {
        this.legsTimer = legsTimer;
    }

    @Override
    public void setPlayerState_loopSound(int loopSound) {
        this.loopSound = loopSound;
    }

    @Override
    public void setPlayerState_movementDir(int movementDir) {
        this.movementDir = movementDir;
    }

    @Override
    public void setPlayerState_origin(Vector3f origin) {
        this.origin = origin;
    }

    @Override
    public void setPlayerState_ping(int ping) {
        this.ping = ping;
    }

    @Override
    public void setPlayerState_pm_flags(int pmFlags) {
        this.pm_flags = pmFlags;
    }

    @Override
    public void setPlayerState_pm_time(int pmTime) {
        this.pm_time = pmTime;
    }

    @Override
    public void setPlayerState_pm_type(PlayerMovementType pmType) {
        this.pm_type = pmType;
    }

    @Override
    public void setPlayerState_speed(int speed) {
        this.speed = speed;
    }

    @Override
    public void setPlayerState_torsoAnim(int torsoAnim) {
        this.torsoAnim = torsoAnim;
    }

    @Override
    public void setPlayerState_torsoTimer(int torsoTimer) {
        this.torsoTimer = torsoTimer;
    }

    @Override
    public void setPlayerState_velocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    @Override
    public void setPlayerState_viewAngles(Angle3f viewAngles) {
        this.viewAngles = viewAngles;
    }

    @Override
    public void setPlayerState_viewAngles(float pitch, float yaw, float roll) {
        viewAngles.x = pitch;
        viewAngles.y = yaw;
        viewAngles.z = roll;
    }

    @Override
    public void setPlayerState_viewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    @Override
    public void setPlayerState_weapon(int weapon) {
        this.weapon = weapon;
    }

    @Override
    public void setPlayerState_weaponState(int weaponState) {
        this.weaponState = weaponState;
    }

    @Override
    public void setPlayerState_weaponTime(int weaponTime) {
        this.weaponTime = weaponTime;
    }

    @Override
    public int getPlayerState_stat(PlayerStatsType stat) {
        return stats[stat.ordinal()];
    }

    @Override
    public void setPlayerState_stat(PlayerStatsType stat, int value) {
        stats[stat.ordinal()] = value;
    }
}
