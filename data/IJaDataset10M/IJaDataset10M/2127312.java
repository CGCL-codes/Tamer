package it.ame.permflow.IO;

import it.ame.permflow.packets.BatteryLevelPacket;
import java.util.ArrayList;

public class OpticalEncapsulator extends StreamEncapsulator {

    @Override
    void generateMessages() {
        ArrayList<Byte> buffer = getBuffer();
        while (buffer.size() >= 5) {
            int val = (buffer.get(1) - 48) * 10;
            val += buffer.get(2) - 48;
            BatteryLevelPacket blp = new BatteryLevelPacket(val);
            blp.timestamp = System.currentTimeMillis();
            PacketDispatcher.deliverMessage(blp);
            for (int i = 0; i < 5; ++i) buffer.remove(0);
        }
    }
}
