package core.model.classes.base;

import java.util.List;

public class BaseGame extends Entity {

    private List<BaseCampaign> campaignList;

    private List<BaseMap> maps;

    public BaseGame() {
    }

    public List<BaseCampaign> getCampaignList() {
        return campaignList;
    }

    public List<BaseMap> getMaps() {
        return maps;
    }

    public void setCampaignList(List<BaseCampaign> campaignList) {
        this.campaignList = campaignList;
    }

    public void setMaps(List<BaseMap> maps) {
        this.maps = maps;
    }
}
