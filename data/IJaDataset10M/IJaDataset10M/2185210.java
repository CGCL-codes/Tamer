package fr.fg.server.data;

import fr.fg.server.data.base.AchievementBase;

public class Achievement extends AchievementBase {

    public static final int TYPE_FLEETS_DESTOYED = 0, TYPE_FLEETS_LOST = 1, TYPE_TRADED_RESOURCES = 2, TYPE_BUILT_SHIPS = 3, TYPE_HYPERJUMPS = 4, TYPE_STOLEN_RESOURCES = 5, TYPE_TRAININGS = 6, TYPE_PICKED_DOODADS = 7, TYPE_MINED_RESOURCES = 8, TYPE_BUILT_WARDS = 9, TYPE_SWAPS = 10, TYPE_EMP = 11, TYPE_CRITICAL_HITS = 12, TYPE_DODGES = 13, TYPE_BLACKHOLES_LOSSES = 14, TYPE_RETREATS = 15, TYPE_ALIENS = 16, TYPE_SPACE_STATION_FUNDS = 17, TYPE_MISSIONS = 18, TYPE_ACHIEVEMENTS = 19;

    public static final long[][] REQUIRED_SCORE = { { 20, 100, 500, 2500, 15000 }, { 15, 75, 380, 2000, 12000 }, { 1000000, 10000000, 100000000, 1000000000, 10000000000l }, { 10000, 100000, 100000, 1000000, 10000000 }, { 50, 250, 1250, 6500, 35000 }, { 50000, 250000, 1250000, 6500000, 35000000 }, { 15, 75, 380, 2000, 12000 }, { 50, 250, 1250, 6500, 35000 }, { 100000, 1000000, 10000000, 100000000, 1000000000 }, { 20, 100, 500, 2500, 15000 }, { 20, 100, 500, 2500, 15000 }, { 20, 100, 500, 2500, 15000 }, { 15, 75, 380, 2000, 12000 }, { 15, 75, 380, 2000, 12000 }, { 5000, 25000, 125000, 650000, 3500000 }, { 15, 75, 380, 2000, 12000 }, { 15, 75, 380, 2000, 12000 }, { 500000, 2500000, 12500000, 62500000, 320000000 }, { 5, 20, 80, 320, 1280 }, { 10, 30, 50, 70, 90 } };

    public Achievement() {
    }

    public Achievement(int idPlayer, int type, long score) {
        setIdPlayer(idPlayer);
        setType(type);
        setScore(score);
    }

    public void incrementScore() {
        setScore(getScore() + 1);
    }

    public void addScore(long value) {
        setScore(getScore() + value);
    }

    public Player getPlayer() {
        return DataAccess.getPlayerById(getIdPlayer());
    }

    public int getLevel() {
        long score = getScore();
        for (int i = 0; i < 5; i++) if (score < REQUIRED_SCORE[getType()][i]) return i;
        return 5;
    }
}
