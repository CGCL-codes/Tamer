package ie.blackoutscout.common.beans.interfaces;

import ie.blackoutscout.common.beans.implementations.Fixture;
import ie.blackoutscout.common.beans.implementations.Player;
import java.util.Collection;

/**
 * This interface is for managing the properties of the Team object
 */
public interface ITeam extends IBean {

    Integer getBot();

    void setBot(Integer bot);

    String getCountry();

    void setCountry(String country);

    Long getLeagueId();

    void setLeagueId(Long leagueId);

    Long getManager();

    void setManager(Long manager);

    Integer getNationalRank();

    void setNationalRank(Integer nationalRank);

    String getNickName1();

    void setNickName1(String nickName1);

    String getNickName2();

    void setNickName2(String nickName2);

    String getNickName3();

    void setNickName3(String nickName3);

    Double getRankingPoints();

    void setRankingPoints(Double rankingPoints);

    Integer getRegion();

    void setRegion(Integer region);

    Integer getRegionRank();

    void setRegionRank(Integer regionRank);

    String getStadium();

    void setStadium(String stadium);

    Long getTeamId();

    void setTeamId(Long teamId);

    String getTeamName();

    void setTeamName(String teamName);

    Integer getWorldRank();

    void setWorldRank(Integer worldRank);

    Long getBankBalance();

    void setBankBalance(Long bankBalance);

    Integer getDrift();

    void setDrift(Integer intValue);

    Integer getRush();

    void setRush(Integer intValue);

    Integer getManOnMan();

    void setManOnMan(Integer intValue);

    Integer getContentment();

    void setContentment(Integer intValue);

    Long getMembers();

    void setMembers(Long longValue);

    Long getStadiumCapacity();

    void setStadiumCapacity(Long longValue);

    Long getStadiumStanding();

    void setStadiumStanding(Long longValue);

    Long getStadiumUncovered();

    void setStadiumUncovered(Long longValue);

    Long getStadiumCovered();

    void setStadiumCovered(Long longValue);

    Long getStadiumMembers();

    void setStadiumMembers(Long longValue);

    Long getStadiumCorporate();

    void setStadiumCorporate(Long longValue);

    Integer getMajorSponsor();

    void setMajorSponsor(Integer intValue);

    Integer getMinorSponsor();

    void setMinorSponsor(Integer intValue);

    Double getPrevRankingPoints();

    void setPrevRankingPoints(Double doubleValue);

    Integer getPrevRegionRank();

    void setPrevRegionRank(Integer intValue);

    Integer getPrevNationalRank();

    void setPrevNationalRank(Integer intValue);

    Integer getPrevWorldRank();

    void setPrevWorldRank(Integer intValue);

    Integer getScoutingStars();

    void setScoutingStars(Integer intValue);

    Integer getPlural();

    void setPlural(Integer intValue);

    Integer getPluralNickname1();

    void setPluralNickname1(Integer intValue);

    Integer getPluralNickname2();

    void setPluralNickname2(Integer intValue);

    Integer getPluralNickname3();

    void setPluralNickname3(Integer intValue);

    public Collection<Player> getSquad();

    public void setSquad(Collection<Player> squad);
}
