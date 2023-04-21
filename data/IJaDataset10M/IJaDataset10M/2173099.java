package core.model.classes.base;

import java.util.List;

public class BaseCampaign extends Entity {

    private List<BaseQuest> questList;

    public BaseCampaign() {
    }

    public List<BaseQuest> getQuestList() {
        return questList;
    }

    public void setQuestList(List<BaseQuest> questList) {
        this.questList = questList;
    }
}
