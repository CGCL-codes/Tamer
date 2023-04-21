package gameserver.controllers.instances;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import gameserver.ai.AI;
import gameserver.ai.events.Event;
import gameserver.configs.main.CustomConfig;
import gameserver.controllers.NpcController;
import gameserver.dataholders.DataManager;
import gameserver.dataholders.WorldMapsData;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.gameobjects.stats.PlayerLifeStats;
import gameserver.model.templates.WorldMapTemplate;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.InstanceService;
import gameserver.services.TeleportService;
import gameserver.services.instance.AcademySoloInstanceService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import gameserver.world.WorldMapInstance;
import gameserver.world.WorldMapType;

public class AcademySoloController extends NpcController {

    private float[] tlpX = { 363.55927F, 1803.2788F, 1334.719F, 1276.8867F, 363.85471F, 1757.5605F };

    private float[] tlpY = { 1663.2026F, 307.96448F, 1740.7979F, 238.06654F, 349.2952F, 1271.9827F };

    private float[] tlpZ = { 96.0F, 469.95001F, 316.42487F, 405.38031F, 96.091133F, 389.11746F };

    private byte[] tlpHead = { 62, 60, 65, 61, 59, 15 };

    public void onDialogRequest(Player player) {
        getOwner().getAi().handleEvent(Event.TALK);
        Npc localNpc = getOwner();
        int i = localNpc.getObjectId().intValue();
        switch(getOwner().getNpcId()) {
            case 205668:
            case 205669:
            case 205670:
            case 205671:
            case 205672:
            case 205673:
            case 205674:
            case 205675:
            case 205676:
            case 205677:
            case 205678:
            case 205679:
            case 205682:
            case 730461:
            case 730462:
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(i, 1011));
                return;
        }
    }

    public void onDialogSelect(int paramInt1, Player player, int paramInt2) {
        Npc localNpc = getOwner();
        int i = localNpc.getObjectId().intValue();
        Object localObject;
        AionObject localAionObject;
        switch(getOwner().getNpcId()) {
            case 730461:
            case 730462:
                switch(paramInt1) {
                    case 10000:
                        if (player.getLevel() < 50) {
                            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
                            return;
                        }
                        if (player.getPlayerGroup() != null) {
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(i, 27));
                            return;
                        }
                        int j = WorldMapType.IDARENA_SOLO.getId();
                        localObject = InstanceService.getRegisteredInstance(j, player.getObjectId().intValue());
                        if (localObject != null) {
                            transfer(player, (WorldMapInstance) localObject);
                            return;
                        }
                        port(player, j);
                        return;
                }
                return;
            case 205668:
            case 205669:
            case 205670:
            case 205671:
            case 205672:
            case 205673:
                switch(paramInt1) {
                    case 10000:
                        if (player.getAcademySoloStage() == 0) {
                            player.setAcademySoloStage(player.getAcademySoloStage() + 1);
                            player.setAcademySoloRound(0);
                        }
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
                        PacketSendUtility.broadcastPacket(localNpc, new SM_SYSTEM_MESSAGE(getCurrentDialog(player.getAcademySoloStage()), true, localNpc.getObjectId().intValue(), new Object[0]), 30);
                        localAionObject = World.getInstance().findAionObject(i);
                        if ((localAionObject instanceof Npc)) {
                            localObject = (Npc) localAionObject;
                            if (localObject != null) ((Npc) localObject).getController().onDespawn(true);
                        }
                        AcademySoloInstanceService.getInstance().nextSpawn(player);
                        return;
                }
                return;
            case 205674:
            case 205675:
            case 205676:
            case 205677:
            case 205678:
            case 205679:
                switch(paramInt1) {
                    case 10000:
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
                        player.setAcademySoloStage(player.getAcademySoloStage() + 1);
                        player.setAcademySoloRound(0);
                        localAionObject = World.getInstance().findAionObject(i);
                        if ((localAionObject instanceof Npc)) {
                            localObject = (Npc) localAionObject;
                            if (localObject != null) ((Npc) localObject).getController().onDespawn(true);
                        }
                        if (getOwner().getNpcId() == 205679) {
                            AcademySoloInstanceService.getInstance().doReward(player);
                            return;
                        }
                        teleportPlayer(player);
                        changeStage(player);
                        AcademySoloInstanceService.getInstance().spawnStartShigo(player);
                        return;
                }
                return;
            case 205682:
                switch(paramInt1) {
                    case 10000:
                        if (player.getInventory().getItemCountByItemId(186000124) < 1L) {
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(i, 1097));
                            return;
                        }
                        revivePlayer(player);
                        return;
                }
                return;
        }
    }

    private void teleportPlayer(Player player) {
        TeleportService.teleportTo(player, WorldMapType.IDARENA_SOLO.getId(), player.getInstanceId(), this.tlpX[(player.getAcademySoloStage() - 1)], this.tlpY[(player.getAcademySoloStage() - 1)], this.tlpZ[(player.getAcademySoloStage() - 1)], this.tlpHead[(player.getAcademySoloStage() - 1)], 0);
    }

    private void revivePlayer(Player player) {
        player.getLifeStats().setCurrentHpPercent(100);
        player.getLifeStats().setCurrentMpPercent(100);
        player.getInventory().removeFromBagByItemId(186000124, 1L);
        TeleportService.teleportTo(player, WorldMapType.IDARENA_SOLO.getId(), player.getInstanceId(), this.tlpX[(player.getAcademySoloStage() - 1)], this.tlpY[(player.getAcademySoloStage() - 1)], this.tlpZ[(player.getAcademySoloStage() - 1)], this.tlpHead[(player.getAcademySoloStage() - 1)], 0);
    }

    private void changeStage(Player player) {
    }

    private int getCurrentDialog(int paramInt) {
        switch(paramInt) {
            case 1:
                return 1111470;
            case 2:
                return 1111471;
            case 3:
                return 1111472;
            case 4:
                return 1111473;
            case 5:
                return 1111474;
            case 6:
                return 1111475;
        }
        return 0;
    }

    private static void setInstanceCooldown(Player player, int paramInt1, int paramInt2) {
        int i = DataManager.WORLD_MAPS_DATA.getTemplate(paramInt1).getInstanceMapId();
        if (player.getInstanceCD(i) == null) {
            int j = DataManager.WORLD_MAPS_DATA.getTemplate(paramInt1).getCooldown();
            Timestamp localTimestamp = new Timestamp(Calendar.getInstance().getTimeInMillis() + j * 60000);
            player.addInstanceCD(i, localTimestamp, paramInt2, 0);
        }
    }

    private void port(Player player, int paramInt) {
        WorldMapInstance localWorldMapInstance = null;
        localWorldMapInstance = InstanceService.getNextAvailableInstance(paramInt);
        InstanceService.registerPlayerWithInstance(localWorldMapInstance, player);
        setInstanceCooldown(player, paramInt, localWorldMapInstance.getInstanceId());
        transfer(player, localWorldMapInstance);
    }

    private void transfer(Player player, WorldMapInstance worldmapinstance) {
        TeleportService.teleportTo(player, WorldMapType.IDARENA_SOLO.getId(), worldmapinstance.getInstanceId(), this.tlpX[player.getAcademySoloStage()], this.tlpY[player.getAcademySoloStage()], this.tlpZ[player.getAcademySoloStage()], this.tlpHead[player.getAcademySoloStage()], 3000);
    }
}
