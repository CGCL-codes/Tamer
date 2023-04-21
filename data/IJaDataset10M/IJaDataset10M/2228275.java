package org.icehockeymanager.ihm.clients.ihmeditor.dummies;

import java.util.Vector;
import org.icehockeymanager.ihm.game.sponsoring.SponsorData;

/**
 * The Class InjuriesDummy.
 */
public class SponsorsDummy {

    /**
   * Gets the dummy injuries.
   * 
   * @return the dummy injuries
   */
    public static SponsorData[] getDummySponsors() {
        Vector<SponsorData> result = new Vector<SponsorData>();
        SponsorData data = null;
        data = new SponsorData("3b World", "MAIN", 1);
        result.add(data);
        data = new SponsorData("Globi International", "MAIN", 1);
        result.add(data);
        data = new SponsorData("Electronic Commercial", "MAIN", 1);
        result.add(data);
        data = new SponsorData("Tagi", "MAIN", 1);
        result.add(data);
        data = new SponsorData("3b TV", "MEDIA_TV", 1);
        result.add(data);
        data = new SponsorData("Globi TV", "MEDIA_TV", 1);
        result.add(data);
        data = new SponsorData("Electronic Commercial TV", "MEDIA_TV", 1);
        result.add(data);
        data = new SponsorData("Tagi Tv", "MEDIA_TV", 1);
        result.add(data);
        data = new SponsorData("3b TV", "MEDIA_RADIO", 1);
        result.add(data);
        data = new SponsorData("Globi TV", "MEDIA_RADIO", 1);
        result.add(data);
        data = new SponsorData("Electronic Commercial TV", "MEDIA_RADIO", 1);
        result.add(data);
        data = new SponsorData("Tagi Tv", "MEDIA_RADIO", 1);
        result.add(data);
        data = new SponsorData("3b TV", "MEDIA_WEB", 1);
        result.add(data);
        data = new SponsorData("Globi TV", "MEDIA_WEB", 1);
        result.add(data);
        data = new SponsorData("Electronic Commercial TV", "MEDIA_WEB", 1);
        result.add(data);
        data = new SponsorData("Tagi Tv", "MEDIA_WEB", 1);
        result.add(data);
        data = new SponsorData("3b TV", "ARENA_BOARD", 1);
        result.add(data);
        data = new SponsorData("Globi TV", "ARENA_BOARD", 1);
        result.add(data);
        data = new SponsorData("Electronic Commercial TV", "ARENA_BOARD", 1);
        result.add(data);
        data = new SponsorData("Tagi Tv", "ARENA_BOARD", 1);
        result.add(data);
        data = new SponsorData("3b TV", "ARENA_ICE", 1);
        result.add(data);
        data = new SponsorData("Globi TV", "ARENA_ICE", 1);
        result.add(data);
        data = new SponsorData("Electronic Commercial TV", "ARENA_ICE", 1);
        result.add(data);
        data = new SponsorData("Tagi Tv", "ARENA_ICE", 1);
        result.add(data);
        data = new SponsorData("3b TV", "TEAM_MAIN", 1);
        result.add(data);
        data = new SponsorData("Globi TV", "TEAM_MAIN", 1);
        result.add(data);
        data = new SponsorData("Electronic Commercial TV", "TEAM_MAIN", 1);
        result.add(data);
        data = new SponsorData("Tagi Tv", "TEAM_MAIN", 1);
        result.add(data);
        data = new SponsorData("3b TV", "TEAM_STICK", 1);
        result.add(data);
        data = new SponsorData("Globi TV", "TEAM_STICK", 1);
        result.add(data);
        data = new SponsorData("Electronic Commercial TV", "TEAM_STICK", 1);
        result.add(data);
        data = new SponsorData("Tagi Tv", "TEAM_STICK", 1);
        result.add(data);
        data = new SponsorData("3b TV", "TEAM_EQUIPMENT", 1);
        result.add(data);
        data = new SponsorData("Globi TV", "TEAM_EQUIPMENT", 1);
        result.add(data);
        data = new SponsorData("Electronic Commercial TV", "TEAM_EQUIPMENT", 1);
        result.add(data);
        data = new SponsorData("Tagi Tv", "TEAM_EQUIPMENT", 1);
        result.add(data);
        return result.toArray(new SponsorData[result.size()]);
    }
}
