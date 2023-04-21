package org.mineground.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.mineground.Main;

/**
 * @name ServerListener.java
 * @author Daniel Koenen (2012)
 */
public class ServerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onServerCommand(ServerCommandEvent commandEvent) {
        if (commandEvent.getCommand().equalsIgnoreCase("writedefaultconfig")) {
            if (Main.getInstance().getConfigHandler().writeDefaultConfig()) {
                System.out.println("[LVM] Default config has been saved.");
                return;
            }
            System.out.println("[LVM] Default config has not been saved for an unknown reason.");
        }
    }
}
