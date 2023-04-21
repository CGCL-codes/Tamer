package org.jmule.ui.sacli.command.temp;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.jmule.resource.Messages;
import org.jmule.ui.sacli.controller.Command;
import org.jmule.core.internalCommunications.ComClient;
import org.jmule.core.DownloadManager;
import org.jmule.core.Download;
import org.jmule.core.partialfile.Gap;
import org.jmule.core.partialfile.GapList;

/**
 * Created , 08.11.2002
 * 
 * @author emarant
 * @version $Revision: 1.1.1.1 $
 * <br>Last changed by $Author: jmartinc $ on $Date: 2005/04/22 21:45:37 $
 */
public class ShowDownloadGaplistCommand implements Command {

    public ShowDownloadGaplistCommand(int downloadNumber) {
        this.num = downloadNumber;
    }

    int num;

    /**
	 * @see org.jmule.core.controller.Command#execute(AppContext)
	 */
    public List execute(ComClient comClient) throws Exception {
        ArrayList outStrings = new ArrayList();
        if ((num > 0) && (num <= (DownloadManager.getInstance().getDownloadList().size()))) {
            Download dl = ((Download) DownloadManager.getInstance().getDownloadList().get(num - 1));
            outStrings.add(num + ".: " + dl.toString() + "\n");
            if (dl.isComplete()) {
                return outStrings;
            }
            GapList remaining = (GapList) dl.getPartialFile().getGapList().clone();
            remaining.sort();
            outStrings.add(Messages.getString("remaining parts:") + remaining + "\n");
            GapList untagged = (GapList) dl.getUntaggedGaps().clone();
            untagged.sort();
            Iterator it = untagged.getGaps().iterator();
            while (it.hasNext()) {
                Gap aGap = (Gap) it.next();
                remaining.removeGap(aGap.getStart(), aGap.getEnd());
            }
            outStrings.add(Messages.getString("requested parts:") + remaining + "\n");
        } else {
            outStrings.add(Messages.getString("Invalid download number.\n"));
        }
        return outStrings;
    }
}
