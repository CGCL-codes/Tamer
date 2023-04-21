package example.chat;

import java.util.TimerTask;

/**
 * <p>
 * 检查玩家任务
 * </p>
 * <br>
 * @author 胡玮@ritsky
 *
 */
public class CheckPlayerTask extends TimerTask {

    public static final long OVERTIME = 1 * 60 * 1000;

    private Player player;

    public CheckPlayerTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        synchronized (player) {
            if (System.currentTimeMillis() - player.lastAccessTime > OVERTIME) {
                player.isOnline(false);
                this.cancel();
                Player[] friends = player.getFriends();
                OutputMessage outMsg = MessageFactory.createOnlineStatusNotify(player.getPlayerId(), false);
                for (int i = 0; i < friends.length; i++) {
                    friends[i].putMessage(outMsg);
                    friends[i].flush();
                }
            }
        }
    }
}
