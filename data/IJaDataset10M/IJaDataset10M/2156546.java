package net.sf.brightside.qualifications.metamodel;

import java.util.List;

public interface Group {

    List<TeamAppearance> getTeams();

    void setTeams(List<TeamAppearance> teams);

    List<Game> getGames();

    Competition getCompetition();

    void setCompetition(Competition competition);

    String getName();

    void setName(String name);

    Long getId();
}
