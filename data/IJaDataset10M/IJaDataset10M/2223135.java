package pe.task.uniquePacket;

import java.io.Serializable;

public class UniquePacketIDTest implements Serializable {

    public static void main(String[] args) {
        UniquePacketID upid0 = new UniquePacketID(0, 5);
        UniquePacketID upid1 = new UniquePacketID(1, 3);
        UniquePacketID upid2 = new UniquePacketID(0, 5);
        UniquePacketID upid3 = new UniquePacketID(1, 3);
        if (!upid0.equals(upid1)) System.out.println("test passed"); else System.out.println("test failed");
        if (upid0.equals(upid2)) System.out.println("test passed"); else System.out.println("test failed");
        if (!upid1.equals(upid2)) System.out.println("test passed"); else System.out.println("test failed");
        if (upid1.equals(upid3)) System.out.println("test passed"); else System.out.println("test failed");
        upid0.toXml();
        upid1.toXml();
        upid2.toXml();
        upid3.toXml();
        System.out.println("I'm Satisfied ;)");
    }
}
