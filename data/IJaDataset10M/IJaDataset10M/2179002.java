package vm;

public interface MatchLoader {

    public abstract Match getNextMatch();

    public abstract Match getNextNextMatch();

    public abstract Match getNextMatch(String strTeam);

    public abstract String getAllMatchesFromOneTeam(String strTeam);

    public abstract String getNameOfAllTeams();

    public abstract String getMatchesInOneDay(int day);

    public abstract String getTeamsInGroup(String group);
}
