package ie.blackoutscout.common.beans.implementations;

import ie.blackoutscout.common.beans.interfaces.IFixture;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author John
 */
@Entity
public class Fixture implements IFixture {

    @Id
    @Column(name = "FIXTURE_ID")
    private Long fixtureId;

    private Integer season;

    private String countryIso;

    private Long leagueId;

    private Integer round;

    private Long homeTeamId;

    private Long guestTeamId;

    private String competition;

    private Integer botMatch;

    private Long matchStart;

    private Boolean hasMatchBeenPlayed = Boolean.FALSE;

    private Long brTimeStamp;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "fixtureId")
    private Collection<MatchEvent> commentary = new ArrayList<MatchEvent>();

    @OneToOne
    private ReportersSummary reportersSummary;

    @OneToOne
    private FixtureScout fixtureScoutReport;

    public Long getGuestTeamId() {
        return guestTeamId;
    }

    public void setGuestTeamId(Long guestTeamId) {
        this.guestTeamId = guestTeamId;
    }

    public Integer getBotMatch() {
        return botMatch;
    }

    public void setBotMatch(Integer botMatch) {
        this.botMatch = botMatch;
    }

    public Collection<MatchEvent> getCommentary() {
        return commentary;
    }

    public void setCommentary(Collection<MatchEvent> commentary) {
        this.commentary = commentary;
    }

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    public Long getFixtureId() {
        return fixtureId;
    }

    public void setFixtureId(Long fixtureId) {
        this.fixtureId = fixtureId;
    }

    public Long getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(Long homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public Long getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Long leagueId) {
        this.leagueId = leagueId;
    }

    public Long getMatchStart() {
        return matchStart;
    }

    public void setMatchStart(Long matchStart) {
        this.matchStart = matchStart;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getIdString() {
        return "FIXTURE_ID";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Fixture other = (Fixture) obj;
        if (this.fixtureId != other.fixtureId && (this.fixtureId == null || !this.fixtureId.equals(other.fixtureId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.fixtureId != null ? this.fixtureId.hashCode() : 0);
        return hash;
    }

    public ReportersSummary getReportersSummary() {
        return reportersSummary;
    }

    public void setReportersSummary(ReportersSummary reportersSummary) {
        this.reportersSummary = reportersSummary;
    }

    public FixtureScout getFixtureScoutReport() {
        return fixtureScoutReport;
    }

    public void setFixtureScoutReport(FixtureScout fixtureScoutReport) {
        this.fixtureScoutReport = fixtureScoutReport;
    }

    public Boolean getHasMatchBeenPlayed() {
        return hasMatchBeenPlayed;
    }

    public void setHasMatchBeenPlayed(Boolean hasMatchBeenPlayed) {
        this.hasMatchBeenPlayed = hasMatchBeenPlayed;
    }

    public Long getBrTimeStamp() {
        return brTimeStamp;
    }

    public void setBrTimeStamp(Long brTimeStamp) {
        this.brTimeStamp = brTimeStamp;
    }
}
