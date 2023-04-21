package gameserver.network.aion.clientpackets;

import gameserver.configs.GeneralConfig;
import gameserver.controllers.MoveController;
import gameserver.controllers.movement.MovementType;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_MOVE;
import gameserver.task.impl.GroupUpdater;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.stats.StatFunctions;
import gameserver.world.World;
import org.apache.log4j.Logger;

/**
 * Packet about player movement.
 */
public class CM_MOVE extends AionClientPacket {

    /**
	* logger for this class
	*/
    private static final Logger log = Logger.getLogger(CM_MOVE.class);

    private MovementType type;

    private byte heading;

    private byte movementType;

    private float x, y, z, x2, y2, z2;

    private byte glideFlag;

    /**
	* Constructs new instance of <tt>CM_MOVE </tt> packet
	* 
	* @param opcode
	*/
    public CM_MOVE(int opcode) {
        super(opcode);
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected void readImpl() {
        Player player = getConnection().getActivePlayer();
        if (!player.isSpawned()) return;
        x = readF();
        y = readF();
        z = readF();
        heading = (byte) readC();
        movementType = (byte) readC();
        type = MovementType.getMovementTypeById(movementType);
        switch(type) {
            case MOVEMENT_START_MOUSE:
            case MOVEMENT_START_KEYBOARD:
                x2 = readF();
                y2 = readF();
                z2 = readF();
                break;
            case MOVEMENT_GLIDE_DOWN:
            case MOVEMENT_GLIDE_START_MOUSE:
                x2 = readF();
                y2 = readF();
                z2 = readF();
            case MOVEMENT_GLIDE_UP:
            case VALIDATE_GLIDE_MOUSE:
                glideFlag = (byte) readC();
                break;
            default:
                break;
        }
    }

    /**
	* {@inheritDoc}
	*/
    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        World world = World.getInstance();
        if (type == null || player == null) return;
        float playerZ = player.getZ();
        switch(type) {
            case MOVEMENT_START_MOUSE:
            case MOVEMENT_START_KEYBOARD:
            case MOVEMENT_MOVIN_ELEVATOR:
            case MOVEMENT_ON_ELEVATOR:
            case MOVEMENT_STAYIN_ELEVATOR:
                world.updatePosition(player, x, y, z, heading);
                player.getMoveController().setNewDirection(x2, y2, z2);
                player.getController().onStartMove();
                player.getFlyController().onStopGliding();
                PacketSendUtility.broadcastPacket(player, new SM_MOVE(player.getObjectId(), x, y, z, x2, y2, z2, heading, type), false);
                break;
            case MOVEMENT_GLIDE_START_MOUSE:
                player.getMoveController().setNewDirection(x2, y2, z2);
            case MOVEMENT_GLIDE_DOWN:
                world.updatePosition(player, x, y, z, heading);
                player.getController().onMove();
                PacketSendUtility.broadcastPacket(player, new SM_MOVE(player.getObjectId(), x, y, z, x2, y2, z2, heading, glideFlag, type), false);
                player.getFlyController().switchToGliding();
                break;
            case MOVEMENT_GLIDE_UP:
                world.updatePosition(player, x, y, z, heading);
                player.getController().onMove();
                PacketSendUtility.broadcastPacket(player, new SM_MOVE(player.getObjectId(), x, y, z, heading, glideFlag, type), false);
                player.getFlyController().switchToGliding();
                break;
            case VALIDATE_GLIDE_MOUSE:
                world.updatePosition(player, x, y, z, heading);
                player.getController().onMove();
                player.getFlyController().switchToGliding();
                float glideSpeed = player.getGameStats().getCurrentStat(StatEnum.SPEED);
                double angle = Math.toRadians(heading * 3);
                x2 = (float) (glideSpeed * Math.cos(angle));
                y2 = (float) (glideSpeed * Math.sin(angle));
                PacketSendUtility.broadcastPacket(player, new SM_MOVE(player.getObjectId(), x, y, z, x2, y2, z2, heading, glideFlag, MovementType.MOVEMENT_GLIDE_DOWN), false);
                break;
            case VALIDATE_MOUSE:
            case VALIDATE_KEYBOARD:
                player.getController().onMove();
                player.getFlyController().onStopGliding();
                world.updatePosition(player, x, y, z, heading);
                MoveController mc = player.getMoveController();
                PacketSendUtility.broadcastPacket(player, new SM_MOVE(player.getObjectId(), x, y, z, mc.getTargetX(), mc.getTargetY(), mc.getTargetZ(), heading, (type == MovementType.VALIDATE_MOUSE) ? MovementType.MOVEMENT_START_MOUSE : MovementType.MOVEMENT_START_KEYBOARD), false);
                break;
            case MOVEMENT_STOP:
                PacketSendUtility.broadcastPacket(player, new SM_MOVE(player.getObjectId(), x, y, z, heading, type), false);
                world.updatePosition(player, x, y, z, heading);
                player.getController().onStopMove();
                player.getFlyController().onStopGliding();
                break;
            case UNKNOWN:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown movement type: ").append(movementType);
                sb.append("Coordinates: X=").append(x);
                sb.append(" Y=").append(y);
                sb.append(" Z=").append(z);
                sb.append(" player=").append(player.getName());
                log.warn(sb.toString());
                break;
            default:
                break;
        }
        if (player.isInGroup() || player.isInAlliance()) {
            GroupUpdater.getInstance().startTask(player);
        }
        float distance = playerZ - z;
        if (GeneralConfig.ACTIVE_FALL_DAMAGE && player.isInState(CreatureState.ACTIVE) && !player.isInState(CreatureState.FLYING) && !player.isInState(CreatureState.GLIDING) && (type == MovementType.MOVEMENT_STOP || distance >= GeneralConfig.MAXIMUM_DISTANCE_MIDAIR)) {
            if (StatFunctions.calculateFallDamage(player, distance)) {
                return;
            }
        }
        if (type != MovementType.MOVEMENT_STOP && player.isProtectionActive()) {
            player.getController().stopProtectionActiveTask();
        }
    }
}
