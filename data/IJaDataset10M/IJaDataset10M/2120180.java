package com.krobothsoftware.psn.parser;

import java.util.List;
import org.htmlcleaner.TagNode;
import com.krobothsoftware.psn.PlaystationNetwork;
import com.krobothsoftware.psn.model.Trophy;
import com.krobothsoftware.psn.model.TrophyData;
import com.krobothsoftware.psn.model.list.TrophyList;

/**
 * The Class TrophyWebUKHandler.
 * 
 * @author Kyle Kroboth
 */
public class TrophyWebUKHandler extends WebHandler {

    private TrophyList<TrophyData> trophyList;

    /**
	 * Instantiates a new trophy web uk handler.
	 * 
	 @param PlaystationNetwork
	 *            psn instance
	 */
    public TrophyWebUKHandler(PlaystationNetwork psn) {
        super(psn);
    }

    /**
	 * Gets the trophies.
	 * 
	 * @return the trophies
	 */
    public TrophyList<TrophyData> getTrophies() {
        return trophyList;
    }

    @Override
    void parse(TagNode rootTagNode) {
        String psnId = rootTagNode.findElementByAttValue("class", "psnId", true, false).getText().toString();
        String gameId = rootTagNode.findElementByAttValue("class", "gameLogoImage", true, false).findElementByName("IMG", false).getAttributeByName("src");
        gameId = gameId.substring(gameId.indexOf("trophy/np/") + 10, gameId.indexOf("_00_"));
        gameId += "_00";
        @SuppressWarnings("unchecked") List<TagNode> trophyDetails = rootTagNode.findElementByAttValue("class", "psnTrophyTable psnTrophyTitleTable", true, false).findElementByName("tbody", false).getElementListByName("td", true);
        int bronze = Integer.parseInt(trophyDetails.get(1).getText().toString());
        int silver = Integer.parseInt(trophyDetails.get(2).getText().toString());
        int gold = Integer.parseInt(trophyDetails.get(3).getText().toString());
        int platinum = Integer.parseInt(trophyDetails.get(4).getText().toString());
        TagNode gameListingContainer = rootTagNode.findElementByAttValue("class", "gamelevelListingContainer", true, false);
        @SuppressWarnings("unchecked") List<TagNode> trophyItems = gameListingContainer.getElementListByAttValue("class", "gameLevelListItem", true, false);
        psn.addProgressLength(trophyItems.size());
        int trophyId = 0;
        for (TagNode trophyItem : trophyItems) {
            trophyId++;
            boolean trophyHidden = false;
            String trophyDate = null;
            String trophyImage;
            String trophyTitle;
            String trophyDesc;
            int trophyType = 0;
            trophyImage = trophyItem.findElementByAttValue("class", "gameLevelImage", false, false).findElementByName("IMG", false).getAttributeByName("src");
            if (trophyImage.contains("/common/icon_trophy_padlock")) {
                trophyImage = "http://webassets.scea.com/playstation/img/trophy_locksmall.png";
            } else {
                trophyImage = "http://trophy01.np.community.playstation.net/trophy/np/" + trophyImage.substring(trophyImage.indexOf("/trophy/np/") + 11);
            }
            TagNode gameLevelInfo = trophyItem.findElementByAttValue("class", "gameLevelInfo", false, false);
            String trophyTypeField = gameLevelInfo.findElementByName("IMG", true).getAttributeByName("alt");
            if (trophyTypeField.equalsIgnoreCase("Platinum")) {
                trophyType = Trophy.PLATINUM;
            } else if (trophyTypeField.equalsIgnoreCase("Gold")) {
                trophyType = Trophy.GOLD;
            } else if (trophyTypeField.equalsIgnoreCase("Silver")) {
                trophyType = Trophy.SILVER;
            } else if (trophyTypeField.equalsIgnoreCase("Bronze")) {
                trophyType = Trophy.BRONZE;
            } else if (trophyTypeField.equalsIgnoreCase("Hidden")) {
                trophyType = Trophy.HIDDEN;
                trophyHidden = true;
            }
            @SuppressWarnings("unchecked") List<TagNode> levelDetails = trophyItem.findElementByAttValue("class", "gameLevelDetails", true, false).getElementListByName("p", false);
            if (levelDetails.size() == 3) {
                trophyTitle = levelDetails.get(0).getText().toString();
                trophyDate = levelDetails.get(1).getText().toString();
                trophyDate = trophyDate.substring(9);
                trophyDesc = levelDetails.get(2).getText().toString();
            } else {
                trophyTitle = levelDetails.get(0).getText().toString();
                trophyDesc = levelDetails.get(1).getText().toString();
            }
            if (trophyList == null) trophyList = new TrophyList<TrophyData>(gameId, platinum, gold, silver, bronze);
            trophyList.add(TrophyData.create(psn, psnId, trophyId, gameId, trophyTitle, trophyImage, trophyDesc, trophyDate, trophyType, trophyHidden));
            psn.updateProgress("Parsing trophy " + trophyTitle);
        }
        return;
    }
}
