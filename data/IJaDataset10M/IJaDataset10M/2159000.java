package tstoa;

public class PlayerStats extends CharacterStats {

    private GameLog gameLog = GameLog.getSoleInstance();

    public PlayerStats() {
        statsData = new Object[9][2];
        statsData[0][0] = "Level";
        statsData[0][1] = (Integer) 1;
        statsData[1][0] = "Max HP";
        statsData[1][1] = (Integer) 100;
        statsData[2][0] = "Current HP";
        statsData[2][1] = (Integer) 100;
        statsData[3][0] = "Strength";
        statsData[3][1] = (Integer) 5;
        statsData[4][0] = "Attack";
        statsData[4][1] = (Integer) 1;
        statsData[5][0] = "Toughness";
        statsData[5][1] = (Integer) 5;
        statsData[6][0] = "Defence";
        statsData[6][1] = (Integer) 1;
        statsData[7][0] = "Current XP";
        statsData[7][1] = (Integer) 0;
        statsData[8][0] = "Level Up XP";
        statsData[8][1] = (Integer) 100;
    }

    public int getStat(PlayerStatsType type) {
        switch(type) {
            case LEVEL:
                return ((Integer) statsData[0][1]).intValue();
            case MAX_HP:
                return ((Integer) statsData[1][1]).intValue();
            case CURRENT_HP:
                return ((Integer) statsData[2][1]).intValue();
            case STRENGTH:
                return ((Integer) statsData[3][1]).intValue();
            case ATTACK:
                return ((Integer) statsData[4][1]).intValue();
            case TOUGHNESS:
                return ((Integer) statsData[5][1]).intValue();
            case DEFENCE:
                return ((Integer) statsData[6][1]).intValue();
            case CURRENT_XP:
                return ((Integer) statsData[7][1]).intValue();
            case LEVEL_UP_XP:
                return ((Integer) statsData[8][1]).intValue();
            default:
                return 0;
        }
    }

    public void setStat(PlayerStatsType type, int newStat) {
        switch(type) {
            case LEVEL:
                statsData[0][1] = new Integer(newStat);
                break;
            case MAX_HP:
                statsData[1][1] = new Integer(newStat);
                break;
            case CURRENT_HP:
                statsData[2][1] = new Integer(newStat);
                break;
            case STRENGTH:
                statsData[3][1] = new Integer(newStat);
                break;
            case ATTACK:
                statsData[4][1] = new Integer(newStat);
                break;
            case TOUGHNESS:
                statsData[5][1] = new Integer(newStat);
                break;
            case DEFENCE:
                statsData[6][1] = new Integer(newStat);
                break;
            case CURRENT_XP:
                statsData[7][1] = new Integer(newStat);
                break;
            case LEVEL_UP_XP:
                statsData[8][1] = new Integer(newStat);
                break;
            default:
                break;
        }
        if (((Integer) statsData[2][1]).intValue() > ((Integer) statsData[1][1]).intValue()) statsData[2][1] = statsData[1][1];
        if (((Integer) statsData[2][1]).intValue() < 0) statsData[2][1] = 0;
        while (((Integer) statsData[7][1]).intValue() >= ((Integer) statsData[8][1]).intValue()) levelUp();
        fireTableDataChanged();
    }

    private void levelUp() {
        int level = (Integer) ((Integer) statsData[0][1]).intValue();
        int maxHP = (Integer) ((Integer) statsData[1][1]).intValue();
        int str = (Integer) ((Integer) statsData[3][1]).intValue();
        int tgh = (Integer) ((Integer) statsData[5][1]).intValue();
        int levelUpXP = (Integer) ((Integer) statsData[8][1]).intValue();
        statsData[0][1] = level + 1;
        statsData[1][1] = maxHP + 50;
        statsData[2][1] = maxHP + 50;
        statsData[3][1] = str + 5;
        statsData[5][1] = tgh + 5;
        statsData[8][1] = levelUpXP + (level * 100);
        String message = "DING! You have gained a level.";
        gameLog.append(message);
    }
}
