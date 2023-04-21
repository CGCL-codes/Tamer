package org.openaion.gameserver.controllers;

import java.util.List;
import org.openaion.gameserver.controllers.attack.AttackStatus;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.TaskId;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Kisk;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.player.RequestResponseHandler;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.services.KiskService;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;
import org.openaion.gameserver.world.World;

/**
 * @author Sarynth
 *
 */
public class KiskController extends NpcController {

    @Override
    public void onAttack(Creature creature, int skillId, TYPE type, int damage, int logId, AttackStatus status, boolean notifyAttackedObservers, boolean sendPacket) {
        Kisk kisk = (Kisk) this.getOwner();
        if (kisk.getLifeStats().isFullyRestoredHp()) {
            List<Player> members = kisk.getCurrentMemberList();
            for (Player member : members) {
                PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_BINDSTONE_IS_ATTACKED);
            }
        }
        super.onAttack(creature, skillId, type, damage, logId, status, notifyAttackedObservers, sendPacket);
    }

    @Override
    public void onDespawn(boolean forced) {
        final Kisk kisk = (Kisk) this.getOwner();
        kisk.broadcastPacket(SM_SYSTEM_MESSAGE.STR_BINDSTONE_IS_REMOVED);
        removeKisk(kisk);
    }

    @Override
    public void onDie(Creature lastAttacker) {
        final Kisk kisk = (Kisk) this.getOwner();
        PacketSendUtility.broadcastPacket(kisk, new SM_EMOTION(kisk, EmotionType.DIE, 0, 0));
        kisk.broadcastPacket(SM_SYSTEM_MESSAGE.STR_BINDSTONE_IS_DESTROYED);
        removeKisk(kisk);
    }

    private void removeKisk(final Kisk kisk) {
        KiskService.removeKisk(kisk);
        addTask(TaskId.DECAY, ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                if (kisk != null && kisk.isSpawned()) World.getInstance().despawn(kisk);
            }
        }, 3 * 1000));
    }

    @Override
    public void onDialogRequest(Player player) {
        final Kisk kisk = (Kisk) this.getOwner();
        if (player.getKisk() == kisk) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_BINDSTONE_ALREADY_REGISTERED);
            return;
        }
        if (kisk.canBind(player)) {
            RequestResponseHandler responseHandler = new RequestResponseHandler(kisk) {

                @Override
                public void acceptRequest(Creature requester, Player responder) {
                    Kisk kisk = (Kisk) requester;
                    if (!kisk.canBind(responder)) {
                        PacketSendUtility.sendPacket(responder, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_HAVE_NO_AUTHORITY);
                        return;
                    }
                    KiskService.onBind(kisk, responder);
                }

                @Override
                public void denyRequest(Creature requester, Player responder) {
                }
            };
            boolean requested = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_BIND_TO_KISK, responseHandler);
            if (requested) {
                PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_BIND_TO_KISK, player.getObjectId()));
            }
        } else if (kisk.getCurrentMemberCount() >= kisk.getMaxMembers()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_FULL);
        } else {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_HAVE_NO_AUTHORITY);
        }
    }
}
