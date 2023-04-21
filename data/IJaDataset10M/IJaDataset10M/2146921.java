package org.activision.io.scripts.interfaces;

import org.activision.model.player.Player;
import org.activision.io.scripts.Scripts;
import org.activision.io.scripts.interfaceScript;

public class i149 extends interfaceScript {

    @Override
    public void actionButton(Player p, int packetId, int buttonId, int buttonId2, int buttonId3) {
        if (packetId == 24 || packetId == 79) Scripts.invokeItemScript((short) buttonId3).option1(p, buttonId3, 149, buttonId2); else if (packetId == 52) Scripts.invokeItemScript((short) buttonId3).drop(p, buttonId3, 149, buttonId2);
    }
}
