package me.w70.bot.scripting;

import me.w70.bot.Bot;

public class RandomChecker extends Thread {

    public void check() {
        try {
            for (Random r : Bot.scriptLoader.getLoadedRandoms().values()) {
                try {
                    r.execute();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }
}
