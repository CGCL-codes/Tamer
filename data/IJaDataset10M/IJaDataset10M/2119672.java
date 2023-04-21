package jelb.messaging;

import java.util.Hashtable;
import java.util.Map;
import jelb.netio.Message;
import jelb.netio.Protocol;
import jelb.netio.Protocol.Stat;

public class SendPartialStat implements IMessage {

    private Map<Stat, Long> changedStats;

    @Override
    public byte getType() {
        return Protocol.SEND_PARTIAL_STAT;
    }

    @Override
    public void init(Message rawMessage) {
        this.changedStats = new Hashtable<Stat, Long>();
        int statInfoSize = 5;
        int statsCount = (rawMessage.getLength().toInt() - 1) / statInfoSize;
        for (int pointer = 0; pointer < statsCount; pointer += statInfoSize) {
            byte statId = rawMessage.getByte(pointer);
            long statValue = rawMessage.getUint32(pointer + 1).toLong();
            try {
                Stat stat = Stat.parseByte(statId);
                this.changedStats.put(stat, statValue);
            } catch (IndexOutOfBoundsException ex) {
            }
        }
    }

    public boolean isChanging(Stat stat) {
        return this.changedStats.containsKey(stat);
    }

    public long getValue(Stat stat) {
        return this.changedStats.get(stat);
    }
}
