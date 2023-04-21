package xreal.server.game;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import xreal.Angle3f;
import xreal.CVars;
import xreal.ConsoleColorStrings;
import xreal.Engine;
import xreal.PlayerStateAccess;
import xreal.UserCommand;
import xreal.UserInfo;
import xreal.common.Config;
import xreal.common.ConfigStrings;
import xreal.common.GameType;
import xreal.common.PlayerController;
import xreal.common.PlayerMove;
import xreal.common.PlayerMovementFlags;
import xreal.common.PlayerMovementType;
import xreal.common.PlayerStatsType;
import xreal.common.Team;
import xreal.server.Server;
import com.bulletphysics.collision.broadphase.CollisionFilterGroups;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.GhostPairCallback;
import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CapsuleShapeZ;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.collision.shapes.CylinderShapeZ;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.linearmath.Transform;

/**
 * Represents, uses and writes to a native gclient_t
 * 
 * @author Robert Beckebans
 */
public class Player extends GameEntity implements ClientListener, PlayerStateAccess {

    UserInfo _userInfo = new UserInfo();

    private ClientPersistant _pers = new ClientPersistant();

    private ClientSession _sess = new ClientSession();

    private PairCachingGhostObject _ghostObject;

    private ConvexShape _collisionShape;

    private PlayerController _playerController;

    private int _lastCmdTime;

    private boolean _noClip;

    private boolean _isBot;

    /**
	 * Send a command to the client which will be interpreted by the client game module
	 * 
	 * @param string
	 */
    public static native synchronized void sendServerCommand(int clientNum, String command);

    private static native synchronized String getUserInfo(int clientNum);

    private static native synchronized void setUserInfo(int clientNum, String s);

    /**
	 * @return Newest user command.
	 */
    private static native synchronized UserCommand getUserCommand(int clientNum);

    Player(int clientNum, boolean firstTime, boolean isBot) throws GameException {
        super(clientNum);
        _noClip = true;
        _isBot = isBot;
        String userinfo = getUserInfo(clientNum);
        if (userinfo.length() == 0) userinfo = "\\name\\badinfo";
        clientUserInfoChanged(userinfo);
        String ip = _userInfo.get("ip");
        if (!isBot && ip != null && !ip.equals("localhost")) {
            String userPassword = _userInfo.get("password");
            String requiredPassword = CVars.g_password.getString();
            if (requiredPassword != null && requiredPassword.length() > 0 && !requiredPassword.equals(userPassword)) {
                throw new GameException("Invalid password");
            }
        }
        _pers.connected = ClientConnectionState.CONNECTING;
        _ghostObject = new PairCachingGhostObject();
        Game.getBroadphase().getOverlappingPairCache().setInternalGhostPairCallback(new GhostPairCallback());
        _collisionShape = new CylinderShapeZ(new Vector3f(CVars.pm_bodyWidth.getValue() / 2, CVars.pm_bodyWidth.getValue() / 2, CVars.pm_normalHeight.getValue() / 2));
        _ghostObject.setCollisionShape(_collisionShape);
        _ghostObject.setCollisionFlags(CollisionFlags.CHARACTER_OBJECT);
        _playerController = new PlayerController(_ghostObject, _collisionShape, Game.getDynamicsWorld());
        Game.getDynamicsWorld().addCollisionObject(_ghostObject, CollisionFilterGroups.CHARACTER_FILTER, (short) (CollisionFilterGroups.STATIC_FILTER | CollisionFilterGroups.DEFAULT_FILTER));
        Game.getDynamicsWorld().addAction(_playerController);
        Game.getPlayers().add(this);
    }

    @Override
    public void clientBegin() {
        Engine.print("xreal.server.game.Player.clientBegin(clientNum = " + getEntityState_number() + ")\n");
        _pers.connected = ClientConnectionState.CONNECTED;
        _pers.enterTime = Game.getLevelTime();
        _pers.cmd = getUserCommand(getEntityState_number());
        Angle3f viewAngles = getPlayerState_viewAngles();
        setPlayerState_deltaPitch(Angle3f.toShort(viewAngles.x));
        setPlayerState_deltaYaw(Angle3f.toShort(viewAngles.y));
        setPlayerState_deltaRoll(Angle3f.toShort(viewAngles.z));
        spawn();
    }

    private boolean cheatsOk() {
        if (!CVars.sv_cheats.getBoolean()) {
            sendServerCommand(getEntityState_number(), "print \"Cheats are not enabled on this server.\n\"");
            return false;
        }
        return true;
    }

    @Override
    public void clientCommand() {
        String[] args = Engine.getConsoleArgs();
        String cmd = args[0];
        if (cmd.equals("say")) {
            Server.broadcastServerCommand("chat \"" + _pers.netname + ": " + ConsoleColorStrings.GREEN + Engine.concatConsoleArgs(1) + "\n\"");
        } else if (cmd.equals("shootbox")) {
            Vector3f forward = new Vector3f();
            getPlayerState_viewAngles().getVectors(forward, null, null);
            Vector3f newOrigin = new Vector3f();
            newOrigin.scaleAdd(CVars.pm_bodyWidth.getValue() / 2 + 5, forward, getPlayerState_origin());
            GameEntity ent = new TestBox(newOrigin, forward);
        } else if (cmd.equals("shootboxes")) {
            Vector3f forward = new Vector3f();
            Vector3f right = new Vector3f();
            Vector3f up = new Vector3f();
            getPlayerState_viewAngles().getVectors(forward, right, up);
            Vector3f origin = getPlayerState_origin();
            Vector3f newOrigin = new Vector3f();
            for (int i = -48; i < 48; i += 12) {
                newOrigin.scaleAdd(i, right, origin);
                newOrigin.scaleAdd(CVars.pm_bodyWidth.getValue() / 2 + 5, forward, newOrigin);
                GameEntity ent = new TestBox(newOrigin, forward);
            }
        } else if (cmd.equals("shootcylinder")) {
            Vector3f forward = new Vector3f();
            getPlayerState_viewAngles().getVectors(forward, null, null);
            Vector3f newOrigin = new Vector3f();
            newOrigin.scaleAdd(CVars.pm_bodyWidth.getValue() / 2 + 5, forward, getPlayerState_origin());
            GameEntity ent = new TestCylinder(newOrigin, forward);
        } else if (cmd.equals("noclip")) {
            if (!cheatsOk()) {
                return;
            }
            String msg;
            if (_noClip) {
                msg = "noclip OFF\n";
            } else {
                msg = "noclip ON\n";
            }
            _noClip = !_noClip;
            sendServerCommand(getEntityState_number(), "print \"" + msg + "\"");
        } else {
            sendServerCommand(getEntityState_number(), "print \"unknown cmd " + cmd + "\n\"");
        }
    }

    @Override
    public void clientDisconnect() {
        Engine.print("xreal.server.game.Player.clientDisconnect(clientNum = " + getEntityState_number() + ")\n");
    }

    @Override
    public void clientThink(UserCommand ucmd) {
        _pers.cmd = ucmd;
        _lastCmdTime = Game.getLevelTime();
        if (!CVars.g_synchronousClients.getBoolean()) {
            clientThinkReal();
        }
    }

    @Override
    public void runThink() {
        if (_isBot) {
            return;
        }
        clientThinkSynchronous();
    }

    private void clientThinkSynchronous() {
        if (CVars.g_synchronousClients.getBoolean()) {
            if (_pers.connected != ClientConnectionState.CONNECTED) {
                return;
            }
            _pers.cmd.serverTime = Game.getLevelTime();
            clientThinkReal();
        }
    }

    private void clientThinkReal() {
        if (_pers.connected != ClientConnectionState.CONNECTED) {
            return;
        }
        UserCommand ucmd = _pers.cmd;
        if (ucmd.serverTime > Game.getLevelTime() + 200) {
            ucmd.serverTime = Game.getLevelTime() + 200;
            Engine.print("serverTime <<<<<\n");
        }
        if (ucmd.serverTime < Game.getLevelTime() - 1000) {
            ucmd.serverTime = Game.getLevelTime() - 1000;
            Engine.print("serverTime >>>>>\n");
        }
        int msec = ucmd.serverTime - getPlayerState_commandTime();
        if (msec < 1 && _sess.spectatorState != SpectatorState.FOLLOW) {
            return;
        }
        if (msec > 200) {
            msec = 200;
        }
        if (_sess.sessionTeam == Team.SPECTATOR) {
            if (_sess.spectatorState == SpectatorState.SCOREBOARD) {
                return;
            }
            spectatorThink(ucmd);
            return;
        }
        if (_noClip) {
            setPlayerState_pm_type(PlayerMovementType.NOCLIP);
        } else {
            setPlayerState_pm_type(PlayerMovementType.NORMAL);
        }
        setPlayerState_speed(CVars.pm_runSpeed.getInteger());
        PlayerMove pm = new PlayerMove(this, ucmd, false, 0, CVars.pm_debugServer.getInteger(), 0, 0, true, false, 0);
        if (CVars.g_synchronousClients.getBoolean()) {
            _playerController.setPlayerMove(pm);
        } else {
            _playerController.movePlayer(pm);
        }
    }

    private void spectatorThink(UserCommand ucmd) {
        PlayerMove pm = new PlayerMove(this, ucmd, false, 0, CVars.pm_debugServer.getInteger(), 0, 0, true, false, 0);
        {
            setPlayerState_pm_type(PlayerMovementType.SPECTATOR);
            setPlayerState_speed(700);
            if (_noClip) {
                setPlayerState_pm_type(PlayerMovementType.NOCLIP);
            }
            _playerController.movePlayer(pm);
        }
    }

    /**
	 * Called from Player() when the player first connects and
	 * directly by the server system when the player updates a userinfo variable.
	 * 
	 * The game can override any of the settings and call Player.setUserinfo
	 * if desired.
	 * 
	 * @param userinfo
	 *            the userinfo string, formatted as:
	 *            "\keyword\value\keyword\value\....\keyword\value"
	 */
    @Override
    public void clientUserInfoChanged(String userinfo) {
        Engine.print("xreal.server.game.Player.clientUserInfoChanged(clientNum = " + getEntityState_number() + ")\n");
        if (userinfo == null) return;
        _userInfo.read(userinfo);
        String ip = _userInfo.get("ip");
        if (ip.equals("localhost")) {
            _pers.localClient = true;
        }
        String oldname = _pers.netname;
        String name = _userInfo.get("name");
        _pers.netname = name;
        if (_sess.sessionTeam == Team.SPECTATOR) {
            if (_sess.spectatorState == SpectatorState.SCOREBOARD) {
                _pers.netname = "scoreboard";
            }
        }
        if (_pers.connected == ClientConnectionState.CONNECTED) {
            if (!_pers.netname.equals(oldname)) {
                Server.broadcastServerCommand("print \"" + oldname + ConsoleColorStrings.WHITE + " renamed to " + _pers.netname + "\n\"");
            }
        }
        String model = _userInfo.get("model");
        Team team = _sess.sessionTeam;
        GameType gt = GameType.values()[CVars.g_gametype.getInteger()];
        if ((gt == GameType.TEAM || gt == GameType.CTF || gt == GameType.ONEFLAG || gt == GameType.OBELISK || gt == GameType.HARVESTER)) {
        }
        String teamTask = _userInfo.get("teamtask");
        boolean teamLeader = _sess.teamLeader;
        String c1 = _userInfo.get("color1");
        String c2 = _userInfo.get("color2");
        UserInfo uinfo = new UserInfo();
        uinfo.put("n", _pers.netname);
        uinfo.put("t", team.toString());
        uinfo.put("model", model);
        uinfo.put("hmodel", "");
        uinfo.put("g_redteam", "");
        uinfo.put("g_redteam", "");
        uinfo.put("c1", c1);
        uinfo.put("c2", c2);
        uinfo.put("hc", _pers.maxHealth);
        uinfo.put("w", _sess.wins);
        uinfo.put("l", _sess.losses);
        uinfo.put("tt", teamTask);
        uinfo.put("tl", teamLeader);
        Server.setConfigString(ConfigStrings.PLAYERS + getEntityState_number(), uinfo.toString());
    }

    /**
	 * Called every time a client is placed fresh in the world:
	 * 
	 * After the first ClientBegin, and after each respawn
	 * Initializes all non-persistant parts of playerState
	 */
    private void spawn() {
        SpawnPoint spawnPoint = null;
        {
            spawnPoint = selectSpectatorSpawnPoint();
        }
        if (spawnPoint != null) {
            Vector3f spawnOrigin = spawnPoint.getEntityState_origin();
            setOrigin(spawnOrigin);
            setPlayerState_origin(spawnOrigin);
            Angle3f spawnAngles = spawnPoint.getEntityState_angles();
            setViewAngles(spawnAngles);
            Transform startTransform = new Transform();
            startTransform.setIdentity();
            startTransform.origin.set(spawnOrigin);
            Quat4f q = new Quat4f();
            spawnAngles.get(q);
            startTransform.setRotation(q);
            _ghostObject.setWorldTransform(startTransform);
        }
        addPlayerState_pm_flags(PlayerMovementFlags.RESPAWNED);
    }

    private void setViewAngles(Angle3f angles) {
        setPlayerState_deltaPitch((short) (Angle3f.toShort(angles.x) - _pers.cmd.pitch));
        setPlayerState_deltaYaw((short) (Angle3f.toShort(angles.y) - _pers.cmd.yaw));
        setPlayerState_deltaRoll((short) (Angle3f.toShort(angles.z) - _pers.cmd.roll));
        setPlayerState_viewAngles(angles);
        setEntityState_angles(angles);
    }

    /**
	 * This is also used for spectator spawns.
	 */
    private SpawnPoint findIntermissionPoint() {
        GameEntity ent = Game.findEntity(this, "info_player_intermission");
        if (ent == null) {
            ent = Game.findEntity(this, "info_player_start");
            if (ent != null) {
                return (SpawnPoint) ent;
            } else {
                return selectSpawnPoint(new Vector3f());
            }
        } else {
            return (SpawnPoint) ent;
        }
    }

    private SpawnPoint selectSpectatorSpawnPoint() {
        return findIntermissionPoint();
    }

    /**
	 * Chooses a player start, deathmatch start, etc
	 * 
	 * @param avoidPoint
	 * @return
	 */
    private SpawnPoint selectSpawnPoint(Vector3f avoidPoint) {
        return null;
    }

    private boolean spotWouldTelefrag(SpawnPoint spot) {
        return false;
    }

    private static native synchronized int getPlayerState_commandTime(int clientNum);

    private static native synchronized void setPlayerState_commandTime(int clientNum, int commandTime);

    private static native synchronized int getPlayerState_pm_type(int clientNum);

    private static native synchronized void setPlayerState_pm_type(int clientNum, int pm_type);

    private static native synchronized int getPlayerState_pm_flags(int clientNum);

    private static native synchronized void setPlayerState_pm_flags(int clientNum, int pm_flags);

    private static native synchronized int getPlayerState_pm_time(int clientNum);

    private static native synchronized void setPlayerState_pm_time(int clientNum, int pm_time);

    private static native synchronized int getPlayerState_bobCycle(int clientNum);

    private static native synchronized void setPlayerState_bobCycle(int clientNum, int bobCycle);

    private static native synchronized Vector3f getPlayerState_origin(int clientNum);

    private static native synchronized void setPlayerState_origin(int clientNum, float x, float y, float z);

    private static native synchronized Vector3f getPlayerState_velocity(int clientNum);

    private static native synchronized void setPlayerState_velocity(int clientNum, float x, float y, float z);

    private static native synchronized int getPlayerState_weaponTime(int clientNum);

    private static native synchronized void setPlayerState_weaponTime(int clientNum, int weaponTime);

    private static native synchronized int getPlayerState_gravity(int clientNum);

    private static native synchronized void setPlayerState_gravity(int clientNum, int gravity);

    private static native synchronized int getPlayerState_speed(int clientNum);

    private static native synchronized void setPlayerState_speed(int clientNum, int speed);

    private static native synchronized short getPlayerState_deltaPitch(int clientNum);

    private static native synchronized void setPlayerState_deltaPitch(int clientNum, short deltaPitch);

    private static native synchronized short getPlayerState_deltaYaw(int clientNum);

    private static native synchronized void setPlayerState_deltaYaw(int clientNum, short deltaYaw);

    private static native synchronized short getPlayerState_deltaRoll(int clientNum);

    private static native synchronized void setPlayerState_deltaRoll(int clientNum, short deltaRoll);

    private static native synchronized int getPlayerState_groundEntityNum(int clientNum);

    private static native synchronized void setPlayerState_groundEntityNum(int clientNum, int groundEntityNum);

    private static native synchronized int getPlayerState_legsTimer(int clientNum);

    private static native synchronized void setPlayerState_legsTimer(int clientNum, int legsTimer);

    private static native synchronized int getPlayerState_legsAnim(int clientNum);

    private static native synchronized void setPlayerState_legsAnim(int clientNum, int legsAnim);

    private static native synchronized int getPlayerState_torsoTimer(int clientNum);

    private static native synchronized void setPlayerState_torsoTimer(int clientNum, int torsoTimer);

    private static native synchronized int getPlayerState_torsoAnim(int clientNum);

    private static native synchronized void setPlayerState_torsoAnim(int clientNum, int torsoAnim);

    private static native synchronized int getPlayerState_movementDir(int clientNum);

    private static native synchronized void setPlayerState_movementDir(int clientNum, int movementDir);

    private static native synchronized Vector3f getPlayerState_grapplePoint(int clientNum);

    private static native synchronized void setPlayerState_grapplePoint(int clientNum, Vector3f grapplePoint);

    private static native synchronized int getPlayerState_eFlags(int clientNum);

    private static native synchronized void setPlayerState_eFlags(int clientNum, int flags);

    private static native synchronized int getPlayerState_eventSequence(int clientNum);

    private static native synchronized void setPlayerState_eventSequence(int clientNum, int eventSequence);

    private static native synchronized int getPlayerState_externalEvent(int clientNum);

    private static native synchronized void setPlayerState_externalEvent(int clientNum, int externalEvent);

    private static native synchronized int getPlayerState_externalEventParm(int clientNum);

    private static native synchronized void setPlayerState_externalEventParm(int clientNum, int externalEventParm);

    private static native synchronized int getPlayerState_externalEventTime(int clientNum);

    private static native synchronized void setPlayerState_externalEventTime(int clientNum, int externalEventTime);

    private static native synchronized int getPlayerState_weapon(int clientNum);

    private static native synchronized void setPlayerState_weapon(int clientNum, int weapon);

    private static native synchronized int getPlayerState_weaponState(int clientNum);

    private static native synchronized void setPlayerState_weaponState(int clientNum, int weaponState);

    private static native synchronized Angle3f getPlayerState_viewAngles(int clientNum);

    private static native synchronized void setPlayerState_viewAngles(int clientNum, float pitch, float yaw, float roll);

    private static native synchronized int getPlayerState_viewHeight(int clientNum);

    private static native synchronized void setPlayerState_viewHeight(int clientNum, int viewHeight);

    private static native synchronized int getPlayerState_damageEvent(int clientNum);

    private static native synchronized void setPlayerState_damageEvent(int clientNum, int damageEvent);

    private static native synchronized int getPlayerState_damageYaw(int clientNum);

    private static native synchronized void setPlayerState_damageYaw(int clientNum, int damageYaw);

    private static native synchronized int getPlayerState_damagePitch(int clientNum);

    private static native synchronized void setPlayerState_damagePitch(int clientNum, int damagePitch);

    private static native synchronized int getPlayerState_damageCount(int clientNum);

    private static native synchronized void setPlayerState_damageCount(int clientNum, int damageCount);

    private static native synchronized int getPlayerState_generic1(int clientNum);

    private static native synchronized void setPlayerState_generic1(int clientNum, int generic1);

    private static native synchronized int getPlayerState_loopSound(int clientNum);

    private static native synchronized void setPlayerState_loopSound(int clientNum, int loopSound);

    private static native synchronized int getPlayerState_jumppad_ent(int clientNum);

    private static native synchronized void setPlayerState_jumppad_ent(int clientNum, int jumppad_ent);

    private static native synchronized int getPlayerState_ping(int clientNum);

    private static native synchronized void setPlayerState_ping(int clientNum, int ping);

    private static native synchronized int getPlayerState_stat(int clientNum, int stat);

    private static native synchronized void setPlayerState_stat(int clientNum, int stat, int value);

    @Override
    public int getPlayerState_bobCycle() {
        return 0;
    }

    @Override
    public int getPlayerState_clientNum() {
        return 0;
    }

    @Override
    public int getPlayerState_commandTime() {
        return getPlayerState_commandTime(getEntityState_number());
    }

    @Override
    public int getPlayerState_damageCount() {
        return 0;
    }

    @Override
    public int getPlayerState_damageEvent() {
        return 0;
    }

    @Override
    public int getPlayerState_damagePitch() {
        return 0;
    }

    @Override
    public int getPlayerState_damageYaw() {
        return 0;
    }

    @Override
    public short getPlayerState_deltaPitch() {
        return getPlayerState_deltaPitch(getEntityState_number());
    }

    @Override
    public short getPlayerState_deltaRoll() {
        return getPlayerState_deltaRoll(getEntityState_number());
    }

    @Override
    public short getPlayerState_deltaYaw() {
        return getPlayerState_deltaYaw(getEntityState_number());
    }

    @Override
    public int getPlayerState_eFlags() {
        return 0;
    }

    @Override
    public int getPlayerState_eventSequence() {
        return 0;
    }

    @Override
    public int getPlayerState_externalEvent() {
        return 0;
    }

    @Override
    public int getPlayerState_externalEventParm() {
        return 0;
    }

    @Override
    public int getPlayerState_externalEventTime() {
        return 0;
    }

    @Override
    public int getPlayerState_generic1() {
        return 0;
    }

    @Override
    public Vector3f getPlayerState_grapplePoint() {
        return null;
    }

    @Override
    public int getPlayerState_gravity() {
        return 0;
    }

    @Override
    public int getPlayerState_groundEntityNum() {
        return getPlayerState_groundEntityNum(getEntityState_number());
    }

    @Override
    public int getPlayerState_jumppad_ent() {
        return 0;
    }

    @Override
    public int getPlayerState_legsAnim() {
        return 0;
    }

    @Override
    public int getPlayerState_legsTimer() {
        return 0;
    }

    @Override
    public int getPlayerState_loopSound() {
        return 0;
    }

    @Override
    public int getPlayerState_movementDir() {
        return 0;
    }

    @Override
    public Vector3f getPlayerState_origin() {
        return getPlayerState_origin(getEntityState_number());
    }

    @Override
    public int getPlayerState_ping() {
        return 0;
    }

    @Override
    public int getPlayerState_pm_flags() {
        return getPlayerState_pm_flags(getEntityState_number());
    }

    @Override
    public int getPlayerState_pm_time() {
        return getPlayerState_pm_time(getEntityState_number());
    }

    @Override
    public PlayerMovementType getPlayerState_pm_type() {
        return PlayerMovementType.values()[getPlayerState_pm_type(getEntityState_number())];
    }

    @Override
    public int getPlayerState_speed() {
        return getPlayerState_speed(getEntityState_number());
    }

    @Override
    public int getPlayerState_torsoAnim() {
        return 0;
    }

    @Override
    public int getPlayerState_torsoTimer() {
        return 0;
    }

    @Override
    public Vector3f getPlayerState_velocity() {
        return getPlayerState_velocity(getEntityState_number());
    }

    @Override
    public Angle3f getPlayerState_viewAngles() {
        return getPlayerState_viewAngles(getEntityState_number());
    }

    @Override
    public int getPlayerState_viewHeight() {
        return 0;
    }

    @Override
    public int getPlayerState_weapon() {
        return 0;
    }

    @Override
    public int getPlayerState_weaponState() {
        return 0;
    }

    @Override
    public int getPlayerState_weaponTime() {
        return 0;
    }

    @Override
    public void setPlayerState_bobCycle(int bobCycle) {
    }

    @Override
    public void setPlayerState_commandTime(int time) {
        setPlayerState_commandTime(getEntityState_number(), time);
    }

    @Override
    public void setPlayerState_damageCount(int damageCount) {
    }

    @Override
    public void setPlayerState_damageEvent(int damageEvent) {
    }

    @Override
    public void setPlayerState_damagePitch(int damagePitch) {
    }

    @Override
    public void setPlayerState_damageYaw(int damageYaw) {
    }

    @Override
    public void setPlayerState_deltaPitch(short deltaPitch) {
        setPlayerState_deltaPitch(getEntityState_number(), deltaPitch);
    }

    @Override
    public void setPlayerState_deltaRoll(short deltaRoll) {
        setPlayerState_deltaRoll(getEntityState_number(), deltaRoll);
    }

    @Override
    public void setPlayerState_deltaYaw(short deltaYaw) {
        setPlayerState_deltaYaw(getEntityState_number(), deltaYaw);
    }

    @Override
    public void setPlayerState_eFlags(int flags) {
    }

    @Override
    public void setPlayerState_eventSequence(int eventSequence) {
    }

    @Override
    public void setPlayerState_externalEvent(int externalEvent) {
    }

    @Override
    public void setPlayerState_externalEventParm(int externalEventParm) {
    }

    @Override
    public void setPlayerState_externalEventTime(int externalEventTime) {
    }

    @Override
    public void setPlayerState_generic1(int generic1) {
    }

    @Override
    public void setPlayerState_grapplePoint(Vector3f grapplePoint) {
    }

    @Override
    public void setPlayerState_gravity(int gravity) {
    }

    @Override
    public void setPlayerState_groundEntityNum(int groundEntityNum) {
        setPlayerState_groundEntityNum(getEntityState_number(), groundEntityNum);
    }

    @Override
    public void setPlayerState_jumppad_ent(int jumppad_ent) {
    }

    @Override
    public void setPlayerState_legsAnim(int legsAnim) {
    }

    @Override
    public void setPlayerState_legsTimer(int legsTimer) {
    }

    @Override
    public void setPlayerState_loopSound(int loopSound) {
    }

    @Override
    public void setPlayerState_movementDir(int movementDir) {
    }

    @Override
    public void setPlayerState_origin(Vector3f origin) {
        setPlayerState_origin(getEntityState_number(), origin.x, origin.y, origin.z);
    }

    @Override
    public void setPlayerState_ping(int ping) {
    }

    @Override
    public void setPlayerState_pm_flags(int flags) {
        setPlayerState_pm_flags(getEntityState_number(), flags);
    }

    @Override
    public void setPlayerState_pm_time(int time) {
        setPlayerState_pm_time(getEntityState_number(), time);
    }

    @Override
    public void setPlayerState_pm_type(PlayerMovementType type) {
        setPlayerState_pm_type(getEntityState_number(), type.ordinal());
    }

    @Override
    public void setPlayerState_speed(int speed) {
        setPlayerState_speed(getEntityState_number(), speed);
    }

    @Override
    public void setPlayerState_torsoAnim(int torsoAnim) {
    }

    @Override
    public void setPlayerState_torsoTimer(int torsoTimer) {
    }

    @Override
    public void setPlayerState_velocity(Vector3f velocity) {
        setPlayerState_velocity(getEntityState_number(), velocity.x, velocity.y, velocity.z);
    }

    @Override
    public void setPlayerState_viewAngles(Angle3f viewAngles) {
        setPlayerState_viewAngles(getEntityState_number(), viewAngles.x, viewAngles.y, viewAngles.z);
    }

    @Override
    public void setPlayerState_viewAngles(float pitch, float yaw, float roll) {
        setPlayerState_viewAngles(getEntityState_number(), pitch, yaw, roll);
    }

    @Override
    public void setPlayerState_viewHeight(int viewHeight) {
    }

    @Override
    public void setPlayerState_weapon(int weapon) {
    }

    @Override
    public void setPlayerState_weaponState(int weaponState) {
    }

    @Override
    public void setPlayerState_weaponTime(int weaponTime) {
    }

    @Override
    public void addPlayerState_pm_flags(int pm_flags) {
        setPlayerState_pm_flags(getPlayerState_pm_flags() | pm_flags);
    }

    @Override
    public void delPlayerState_pm_flags(int pm_flags) {
        setPlayerState_pm_flags(getPlayerState_pm_flags() & ~pm_flags);
    }

    @Override
    public boolean hasPlayerState_pm_flags(int pm_flags) {
        return (getPlayerState_pm_flags() & pm_flags) != 0;
    }

    @Override
    public int getPlayerState_stat(PlayerStatsType stat) {
        return getPlayerState_stat(getEntityState_number(), stat.ordinal());
    }

    @Override
    public void setPlayerState_stat(PlayerStatsType stat, int value) {
        setPlayerState_stat(getEntityState_number(), stat.ordinal(), value);
    }
}
