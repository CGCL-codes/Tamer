package de.volkerraum.pokerbot.tournamentengine;

import java.util.List;
import de.volkerraum.pokerbot.model.AllPlayerData;
import de.volkerraum.pokerbot.tableengine.TableStatistics;
import de.volkerraum.pokerbot.tournamentlog.model.TableLog;

public interface Tablecontrol {

    public List<Long> getPlayersAlive();

    public void terminateTable();

    public void init(TournamentEngine tournament, String configDir, long tableId, List<AllPlayerData> players, TableLog tableLog, TableStatistics statistics);

    public void startTable();

    public List<PlayerTableResult> getPlayerDiedOrder();
}
