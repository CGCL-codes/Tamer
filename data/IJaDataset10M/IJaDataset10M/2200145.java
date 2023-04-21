package jelb.messaging;

import java.util.Vector;
import jelb.common.Bag;
import jelb.features.BagsHandling;
import jelb.netio.Message;
import jelb.netio.Protocol;

public class GetBagsList implements IMessage {

    Message raw;

    public GetBagsList() {
    }

    @Override
    public byte getType() {
        return Protocol.GET_BAGS_LIST;
    }

    @Override
    public void init(Message rawMessage) {
        this.raw = rawMessage;
    }

    public Iterable<Bag> getBags() {
        int bags_no = this.raw.getUint8(0).toInt();
        if (bags_no > BagsHandling.NUM_BAGS) {
            return null;
        }
        Vector<Bag> bags = new Vector<Bag>();
        int pBag;
        for (int i = 0; i < bags_no; i++) {
            pBag = i * 5 + 1;
            int bag_id = this.raw.getUint8(pBag + 4).toInt();
            if (bag_id >= BagsHandling.NUM_BAGS) {
                continue;
            }
            bags.add(new Bag(bag_id, this.raw.getUint16(pBag).toInt(), this.raw.getUint16(pBag + 2).toInt()));
        }
        return bags;
    }
}
