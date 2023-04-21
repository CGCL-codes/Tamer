package campaigneditor.display;

import campaigneditor.CampaignNodeFactory.CampaignNodeType;
import campaigneditor.*;

/**
 *
 * @author Daniel Aleksandrow
 */
public class CampaignNodeDisplayFactory {

    /** Creates a new instance of CampaignNodeDisplayFactory */
    public static CampaignNodeDisplay getCampaignNodeDisplay(AbstractCampaignNode node, Campaign campaign) {
        String name = node.getClass().getSimpleName();
        Enum[] nodetypes = CampaignNodeFactory.CampaignNodeType.values();
        Enum e = null;
        for (int i = 0; i < nodetypes.length; i++) {
            if (name.equals(nodetypes[i].toString())) {
                e = nodetypes[i];
                break;
            }
        }
        switch((CampaignNodeType) e) {
            default:
                System.out.print("Creating Display ...");
                return new CampaignNodeDisplay(node, campaign);
        }
    }
}
