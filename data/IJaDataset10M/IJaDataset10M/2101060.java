package net.sf.odinms.net.channel.pvp;

import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.MapleBuffStat;
import net.sf.odinms.client.MapleStat;
import net.sf.odinms.client.MapleJob;
import net.sf.odinms.net.world.guild.MapleGuild;
import net.sf.odinms.net.channel.handler.AbstractDealDamageHandler;
import net.sf.odinms.server.life.MapleMonster;
import net.sf.odinms.server.life.MapleLifeFactory;
import net.sf.odinms.server.maps.MapleMap;
import net.sf.odinms.tools.MaplePacketCreator;

public class MaplePvp {

    private static int pvpDamage;

    private static int maxDis;

    private static int maxHeight;

    private static boolean isAoe = false;

    public static boolean isLeft = false;

    public static boolean isRight = false;

    private static boolean isMeleeAttack(AbstractDealDamageHandler.AttackInfo attack) {
        switch(attack.skill) {
            case 1001004:
            case 1001005:
            case 4001334:
            case 4201005:
            case 1111004:
            case 1111003:
            case 1311004:
            case 1311003:
            case 1311002:
            case 1311005:
            case 1311001:
            case 1121008:
            case 1221009:
            case 1121006:
            case 1221007:
            case 1321003:
            case 4221001:
            case 5001002:
            case 5101003:
            case 5101004:
                return true;
        }
        return false;
    }

    private static boolean isRangeAttack(AbstractDealDamageHandler.AttackInfo attack) {
        switch(attack.skill) {
            case 2001004:
            case 2001005:
            case 3001004:
            case 3001005:
            case 4001344:
            case 2101004:
            case 2101005:
            case 2201004:
            case 2301005:
            case 4101005:
            case 2211002:
            case 2211003:
            case 3111006:
            case 3211006:
            case 4111005:
            case 4211002:
            case 2121003:
            case 2221006:
            case 2221003:
            case 2111006:
            case 2211006:
            case 2321007:
            case 3121003:
            case 3121004:
            case 3221003:
            case 3221001:
            case 3221007:
            case 4121003:
            case 4121007:
            case 4221007:
            case 4221003:
            case 4111004:
            case 5001003:
            case 5101002:
            case 5201001:
            case 5201002:
            case 5201004:
            case 5201006:
            case 5211004:
            case 5211005:
                return true;
        }
        return false;
    }

    private static boolean isAoeAttack(AbstractDealDamageHandler.AttackInfo attack) {
        switch(attack.skill) {
            case 2201005:
            case 3101005:
            case 3201005:
            case 1111006:
            case 1111005:
            case 1211002:
            case 1311006:
            case 2111002:
            case 2111003:
            case 2311004:
            case 3111004:
            case 3111003:
            case 3211004:
            case 3211003:
            case 4211004:
            case 1221011:
            case 2121001:
            case 2121007:
            case 2121006:
            case 2221001:
            case 2221007:
            case 2321008:
            case 2321001:
            case 4121004:
            case 4121008:
            case 4221004:
            case 5121001:
            case 5111006:
                return true;
        }
        return false;
    }

    private static void getDirection(AbstractDealDamageHandler.AttackInfo attack) {
        if (isAoe) {
            isRight = true;
            isLeft = true;
        } else if (attack.direction <= 0 && attack.stance <= 0) {
            isRight = false;
            isLeft = true;
        } else {
            isRight = true;
            isLeft = false;
        }
    }

    private static void DamageBalancer(AbstractDealDamageHandler.AttackInfo attack) {
        if (attack.skill == 0) {
            pvpDamage = 100;
            maxDis = 130;
            maxHeight = 35;
        } else if (isMeleeAttack(attack)) {
            maxDis = 130;
            maxHeight = 45;
            isAoe = false;
            if (attack.skill == 4201005) {
                pvpDamage = (int) (Math.floor(Math.random() * (75 - 5) + 5));
            } else if (attack.skill == 1121008) {
                pvpDamage = (int) (Math.floor(Math.random() * (320 - 180) + 180));
                maxHeight = 50;
            } else if (attack.skill == 4221001) {
                pvpDamage = (int) (Math.floor(Math.random() * (200 - 150) + 150));
            } else if (attack.skill == 1121006 || attack.skill == 1221007 || attack.skill == 1321003) {
                pvpDamage = (int) (Math.floor(Math.random() * (200 - 80) + 80));
            } else {
                pvpDamage = (int) (Math.floor(Math.random() * (600 - 250) + 250));
            }
        } else if (isRangeAttack(attack)) {
            maxDis = 300;
            maxHeight = 40;
            isAoe = false;
            if (attack.skill == 4201005) {
                pvpDamage = (int) (Math.floor(Math.random() * (75 - 5) + 5));
            } else if (attack.skill == 4121007) {
                pvpDamage = (int) (Math.floor(Math.random() * (60 - 15) + 15));
            } else if (attack.skill == 4001344 || attack.skill == 2001005) {
                pvpDamage = (int) (Math.floor(Math.random() * (195 - 90) + 90));
            } else if (attack.skill == 4221007) {
                pvpDamage = (int) (Math.floor(Math.random() * (350 - 180) + 180));
            } else if (attack.skill == 3121004 || attack.skill == 3111006 || attack.skill == 3211006) {
                maxDis = 450;
                pvpDamage = (int) (Math.floor(Math.random() * (50 - 20) + 20));
            } else if (attack.skill == 2121003 || attack.skill == 2221003) {
                pvpDamage = (int) (Math.floor(Math.random() * (600 - 300) + 300));
            } else {
                pvpDamage = (int) (Math.floor(Math.random() * (400 - 250) + 250));
            }
        } else if (isAoeAttack(attack)) {
            maxDis = 350;
            maxHeight = 350;
            isAoe = true;
            if (attack.skill == 2121001 || attack.skill == 2221001 || attack.skill == 2321001 || attack.skill == 2121006) {
                maxDis = 175;
                maxHeight = 175;
                pvpDamage = (int) (Math.floor(Math.random() * (350 - 180) + 180));
            } else {
                pvpDamage = (int) (Math.floor(Math.random() * (700 - 300) + 300));
            }
        }
    }

    private static void monsterBomb(MapleCharacter player, MapleCharacter attackedPlayers, MapleMap map, AbstractDealDamageHandler.AttackInfo attack) {
        if (attackedPlayers.getLevel() > player.getLevel() + 25) {
            pvpDamage *= 1.35;
        } else if (attackedPlayers.getLevel() < player.getLevel() - 25) {
            pvpDamage /= 1.35;
        } else if (attackedPlayers.getLevel() > player.getLevel() + 100) {
            pvpDamage *= 1.50;
        } else if (attackedPlayers.getLevel() < player.getLevel() - 100) {
            pvpDamage /= 1.50;
        }
        if (player.getJob().equals(MapleJob.MAGICIAN)) {
            pvpDamage *= 1.20;
        }
        Integer mguard = attackedPlayers.getBuffedValue(MapleBuffStat.MAGIC_GUARD);
        Integer mesoguard = attackedPlayers.getBuffedValue(MapleBuffStat.MESOGUARD);
        if (mguard != null) {
            int mploss = (int) (pvpDamage / .5);
            pvpDamage *= .70;
            if (mploss > attackedPlayers.getMp()) {
                pvpDamage /= .70;
                attackedPlayers.cancelBuffStats(MapleBuffStat.MAGIC_GUARD);
            } else {
                attackedPlayers.setMp(attackedPlayers.getMp() - mploss);
                attackedPlayers.updateSingleStat(MapleStat.MP, attackedPlayers.getMp());
            }
        } else if (mesoguard != null) {
            int mesoloss = (int) (pvpDamage * .75);
            pvpDamage *= .75;
            if (mesoloss > attackedPlayers.getMeso()) {
                pvpDamage /= .75;
                attackedPlayers.cancelBuffStats(MapleBuffStat.MESOGUARD);
            } else {
                attackedPlayers.gainMeso(-mesoloss, false);
            }
        }
        MapleMonster pvpMob = MapleLifeFactory.getMonster(9400711);
        map.spawnMonsterOnGroundBelow(pvpMob, attackedPlayers.getPosition(), false);
        for (int attacks = 0; attacks < attack.numDamage; attacks++) {
            map.broadcastMessage(MaplePacketCreator.damagePlayer(attack.numDamage, pvpMob.getId(), attackedPlayers.getId(), pvpDamage));
            attackedPlayers.addHP(-pvpDamage);
        }
        int attackedDamage = pvpDamage * attack.numDamage;
        attackedPlayers.getClient().getSession().write(MaplePacketCreator.serverNotice(5, player.getName() + " has hit you for " + attackedDamage + " damage!"));
        map.killMonster(pvpMob, player, false);
        if (attackedPlayers.getHp() <= 0 && !attackedPlayers.isAlive()) {
            int expReward = attackedPlayers.getLevel() * 100;
            int gpReward = (int) (Math.floor(Math.random() * (200 - 50) + 50));
            if (player.getPvpKills() * .25 >= player.getPvpDeaths()) {
                expReward *= 20;
            }
            player.gainExp(expReward, true, false);
            if (player.getGuildId() != 0 && player.getGuildId() != attackedPlayers.getGuildId()) {
                try {
                    MapleGuild guild = player.getClient().getChannelServer().getWorldInterface().getGuild(player.getGuildId(), null);
                    guild.gainGP(gpReward);
                } catch (Exception e) {
                }
            }
            player.gainPvpKill();
            player.getClient().getSession().write(MaplePacketCreator.serverNotice(6, "You've killed " + attackedPlayers.getName() + "!! You've gained a pvp kill!"));
            attackedPlayers.gainPvpDeath();
            attackedPlayers.getClient().getSession().write(MaplePacketCreator.serverNotice(6, player.getName() + " has killed you!"));
        }
    }

    public static void doPvP(MapleCharacter player, MapleMap map, AbstractDealDamageHandler.AttackInfo attack) {
        DamageBalancer(attack);
        getDirection(attack);
        for (MapleCharacter attackedPlayers : player.getMap().getNearestPvpChar(player.getPosition(), maxDis, maxHeight, player.getMap().getCharacters())) {
            if (attackedPlayers.isAlive() && (player.getParty() == null || player.getParty() != attackedPlayers.getParty())) {
                monsterBomb(player, attackedPlayers, map, attack);
            }
        }
    }
}
