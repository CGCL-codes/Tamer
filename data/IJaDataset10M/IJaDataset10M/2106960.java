package org.ambiance.azureus.commands.seeding;

import java.util.Calendar;
import org.ambiance.chain.AmbianceCommand;
import org.apache.commons.chain.Context;
import org.gudy.azureus2.core3.download.DownloadManager;

public class ShareLimitCommand extends AmbianceCommand {

    public boolean execute(Context ctx) throws Exception {
        DownloadManager manager = (DownloadManager) ctx.get("manager");
        float shareLimitRation = Float.parseFloat((String) ctx.get("share.limit.ration"));
        long shareLimitTime = Long.parseLong((String) ctx.get("share.limit.time"));
        if (manager.getStats().getShareRatio() > shareLimitRation) return CHAIN_STOP;
        if (manager.getNbPeers() == 0 && (Calendar.getInstance().getTimeInMillis() - manager.getCreationTime()) > shareLimitTime) return CHAIN_STOP;
        return CHAIN_CONTINUE;
    }
}
