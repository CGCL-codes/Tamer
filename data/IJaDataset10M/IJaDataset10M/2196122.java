package de.schwarzrot.vdr.data.processing;

import java.io.BufferedReader;
import java.util.Date;
import de.schwarzrot.app.Client;
import de.schwarzrot.app.support.AbstractResponseHandler;
import de.schwarzrot.data.access.support.EqualConditionElement;
import de.schwarzrot.data.access.support.LowerBoundConditionElement;
import de.schwarzrot.data.access.support.UpperBoundConditionElement;
import de.schwarzrot.data.transaction.TORead;
import de.schwarzrot.data.transaction.TORemove;
import de.schwarzrot.data.transaction.TOSave;
import de.schwarzrot.data.transaction.Transaction;
import de.schwarzrot.vdr.data.domain.ChannelInfo;
import de.schwarzrot.vdr.data.domain.EpgEvent;
import de.schwarzrot.vdr.data.domain.Timer;

public class VdrTimerImporter extends AbstractResponseHandler {

    public VdrTimerImporter(Client client) {
        super(client, "GTIM", null);
    }

    @Override
    protected void cleanup(long maxLifeTime) {
        Date expired = new Date(new Date().getTime() - maxLifeTime * 1000);
        Transaction ta = getTransactionFactory().createTransaction();
        ta.add(new TORemove<Timer>(Timer.class, new UpperBoundConditionElement("end", expired)));
        ta.execute();
    }

    @Override
    protected void processLine(String line, BufferedReader br) {
        if (line.equals(ENDSIG)) return;
        String[] parts = line.split(":");
        ChannelInfo chn = null;
        EpgEvent evt = null;
        Timer timer = null;
        Transaction ta = null;
        getLogger().info("timer line looks like: >" + line);
        if (parts.length > 7) {
            chn = ChannelInfo.valueOf(parts[1]);
            ta = getTransactionFactory().createTransaction();
            TORead<ChannelInfo> toc = new TORead<ChannelInfo>(ChannelInfo.class);
            toc.addCondition(new EqualConditionElement("source", chn.getSource()));
            toc.addCondition(new EqualConditionElement("netId", chn.getNetId()));
            toc.addCondition(new EqualConditionElement("tsId", chn.getTsId()));
            toc.addCondition(new EqualConditionElement("serviceId", chn.getServiceId()));
            toc.addCondition(new EqualConditionElement("radioId", chn.getRadioId()));
            ta.add(toc);
            ta.setRollbackOnly();
            ta.execute();
            if (toc.getResult() == null || toc.getResult().size() < 1) return;
            chn = toc.getResult().get(0);
            Date begin = Timer.createDate(parts[2], parts[3]);
            Date end = Timer.createDate(parts[2], parts[3], parts[4]);
            TORead<EpgEvent> toe = new TORead<EpgEvent>(EpgEvent.class);
            toe.addCondition(new EqualConditionElement("channel", chn));
            toe.addCondition(new LowerBoundConditionElement("begin", begin));
            toe.setMaxRows(2);
            toe.addOrder("begin");
            ta = getTransactionFactory().createTransaction();
            ta.add(toe);
            ta.setRollbackOnly();
            ta.execute();
            if (toe.getResult() == null || toe.getResult().size() < 1) return;
            evt = toe.getResult().get(0);
            timer = new Timer();
            timer.setEvent(evt);
            timer.setBegin(begin);
            timer.setEnd(end);
            timer.setStatus(Integer.valueOf(parts[0]));
            timer.setPriority(Integer.valueOf(parts[5]));
            timer.setLifeTime(Integer.valueOf(parts[6]));
            if (parts.length > 8) timer.setXInfo(parts[8]);
            ta = getTransactionFactory().createTransaction();
            ta.add(new TOSave<Timer>(timer));
            ta.execute();
        }
    }
}
