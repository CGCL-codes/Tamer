package me.guythundar.NoChat;

import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerChatListener extends PlayerListener {

    public NoChat plug;

    public PlayerChatListener(NoChat noChat) {
        plug = noChat;
    }

    public void onPlayerChat(PlayerChatEvent chat) {
        Set<Player> r = chat.getRecipients();
        chat.getRecipients().removeAll(r);
        int s = plug.playerHearingLevels.size();
        for (int i = 0; i < s; i++) {
            String key = plug.playerHearingLevels.keys().nextElement();
            int val = plug.playerHearingLevels.get(key);
            if (val == plug.HEAR_COMMANDS_ONLY || val == plug.HEAR_NONE) r.remove(chat.getPlayer().getServer().getPlayer(key));
        }
        chat.getRecipients().addAll(r);
    }
}
