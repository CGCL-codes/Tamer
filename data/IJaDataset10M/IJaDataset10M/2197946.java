package gameserver.controllers.instances;

import java.util.Timer;
import java.util.TimerTask;
import gameserver.ai.events.Event;
import gameserver.controllers.NpcController;
import gameserver.dataholders.DataManager;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.NpcTemplate;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.services.DropService;
import gameserver.services.EmpyreanCrucibleService;
import gameserver.services.InstanceService;
import gameserver.services.TeleportService;
import gameserver.spawnengine.SpawnEngine;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

public class EmpyreanCrucibleController extends NpcController {

    @Override
    public void onDie(Creature lastAttacker) {
        super.onDie(lastAttacker);
        Npc owner = getOwner();
        Player player;
        player = (Player) lastAttacker;
    }

    Npc npc = getOwner();

    int itemId;

    @Override
    public void onDialogRequest(final Player player) {
        getOwner().getAi().handleEvent(Event.TALK);
        NpcTemplate npctemplate = DataManager.NPC_DATA.getNpcTemplate(getOwner().getNpcId());
        if (npctemplate.getNameId() == 354983 || npctemplate.getNameId() == 354984 || npctemplate.getNameId() == 354985 || npctemplate.getNameId() == 354986 || npctemplate.getNameId() == 354987 || npctemplate.getNameId() == 354988 || npctemplate.getNameId() == 354989 || npctemplate.getNameId() == 354648 || npctemplate.getNameId() == 354649 || npctemplate.getNameId() == 354650 || npctemplate.getNameId() == 354651 || npctemplate.getNameId() == 354652 || npctemplate.getNameId() == 354653 || npctemplate.getNameId() == 354654 || npctemplate.getNameId() == 354655 || npctemplate.getNameId() == 354656 || npctemplate.getNameId() == 354657 || npctemplate.getNameId() == 354658 || npctemplate.getNameId() == 354659 || npctemplate.getNameId() == 354660 || npctemplate.getNameId() == 354661 || npctemplate.getNameId() == 355036 || npctemplate.getNameId() == 355037 || npctemplate.getNameId() == 355038 || npctemplate.getNameId() == 355039 || npctemplate.getNameId() == 355040 || npctemplate.getNameId() == 355041) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
            return;
        }
    }

    @Override
    public void onDialogSelect(int dialogId, final Player player, int questId) {
        Npc npc = getOwner();
        if (dialogId == 10000 && (npc.getNpcId() == 799573 || npc.getNpcId() == 205426 || npc.getNpcId() == 205427 || npc.getNpcId() == 205428 || npc.getNpcId() == 205429 || npc.getNpcId() == 205430 || npc.getNpcId() == 205431)) {
            if (player.getCommonData().getRace().getRaceId() == 0) itemId = 186000124; else itemId = 186000125;
            if ((player.getInventory().getItemCountByItemId(itemId) != 0)) {
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
                player.getInventory().removeFromBagByItemId(itemId, 1);
                if (player.isInGroup()) {
                    player.getPlayerGroup().getEmpyreanCrucible().returnStage(player);
                }
                return;
            } else PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1097));
            return;
        }
        if (dialogId == 10000 && (npc.getNpcId() == 799567)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.getPlayerGroup().getEmpyreanCrucible() == null) {
                        EmpyreanCrucibleService ec = new EmpyreanCrucibleService();
                        player.getPlayerGroup().setEmpyreanCrucible(ec);
                        ec.startEvent(player.getPlayerGroup());
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 799568)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStart();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 799569)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStart();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 799570)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStage();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 799571)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStage();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 799572)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStage();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 205331)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStart();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 205332)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStart();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 205333)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStart();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 205334)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStart();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 205335)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStart();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 205336)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStart();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 205337)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStart();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 205338)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStage();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 205339)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStage();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 205340)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStage();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 205341)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStage();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 205342)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStage();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 205343)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().changeStage();
                    }
                }
            }, 1000);
            delete();
            return;
        } else if (dialogId == 10000 && (npc.getNpcId() == 205344)) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 0));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (player.isInGroup()) {
                        player.getPlayerGroup().getEmpyreanCrucible().doReward();
                    }
                }
            }, 1000);
            delete();
            return;
        }
    }
}
