package net.alcuria.ball;

public class EventHandler {

    static final int WORLD_1_SWITCH_A = 1;

    public static void start(int eventID) {
        switch(eventID) {
            case WORLD_1_SWITCH_A:
                for (int i = 50; i <= 52; i++) {
                    Assets.map.collisionMap[i][29] = 15;
                    Assets.map.lowerMap[i][29] = 156;
                    Assets.map.lowerMap[i][28] = 148;
                }
                break;
        }
    }
}
