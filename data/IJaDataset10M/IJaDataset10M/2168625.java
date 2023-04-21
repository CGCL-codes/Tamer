package org.chessworks.uscl.services.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.chessworks.common.service.BasicLifecycle;
import org.chessworks.uscl.model.Game;
import org.chessworks.uscl.model.Player;
import org.chessworks.uscl.model.Team;
import org.chessworks.uscl.services.InvalidPlayerException;
import org.chessworks.uscl.services.InvalidTeamException;
import org.chessworks.uscl.services.TournamentService;

public class SimpleTournamentService extends BasicLifecycle implements TournamentService {

    /** Map players to games */
    private final Map<Player, Game> playerBoards = new LinkedHashMap<Player, Game>();

    /** Map board numbers to games */
    private final Map<Integer, Game> boards = new TreeMap<Integer, Game>();

    /** Map codes to teams */
    private final Map<String, Team> teams;

    /** A read-only wrapper for returning all teams. */
    private final Collection<Team> allTeams;

    /** Map handles to players */
    private final Map<String, Player> players;

    /** A read-only wrapper for returning all players. */
    private final Collection<Player> allPlayers;

    public SimpleTournamentService() {
        teams = new TreeMap<String, Team>();
        allTeams = Collections.unmodifiableCollection(teams.values());
        players = new TreeMap<String, Player>();
        allPlayers = Collections.unmodifiableCollection(players.values());
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#clearSchedule()
	 */
    @Override
    public void clearSchedule() {
        playerBoards.clear();
        boards.clear();
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#scheduleGame(int, Player, Player)
	 */
    @Override
    public Game scheduleGame(int board, Player white, Player black) {
        Game game = new Game(board, white, black);
        game.whitePlayer = white;
        game.blackPlayer = black;
        game.boardNumber = board;
        return scheduleGame(game);
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#scheduleGame(Game)
	 */
    @Override
    public Game scheduleGame(Game game) {
        Player white = game.whitePlayer;
        Player black = game.blackPlayer;
        playerBoards.put(white, game);
        playerBoards.put(black, game);
        boards.put(game.boardNumber, game);
        return game;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#cancelGame(Game)
	 */
    @Override
    public Game cancelGame(Game game) {
        if (game == null) return null;
        if (!boards.containsValue(game)) return null;
        Player white = game.whitePlayer;
        Player black = game.blackPlayer;
        playerBoards.remove(white);
        playerBoards.remove(black);
        boards.remove(game.boardNumber);
        return game;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#cancelGame(Player)
	 */
    @Override
    public Game cancelGame(Player player) {
        Game game = playerBoards.get(player);
        cancelGame(game);
        return game;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#cancelGame(int)
	 */
    @Override
    public Game cancelGame(int board) {
        Game game = boards.get(board);
        cancelGame(game);
        return game;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#findPlayerGame(Player)
	 */
    @Override
    public Game findPlayerGame(Player player) {
        if (player == null) return null;
        Game game = playerBoards.get(player);
        return game;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#findGame(int)
	 */
    @Override
    public Game findGame(int gameNumber) {
        Game game = boards.get(gameNumber);
        return game;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#findAllGames()
	 */
    @Override
    public Collection<Game> findAllGames() {
        return Collections.unmodifiableCollection(boards.values());
    }

    /**
	 * {@inheritDoc}
	 * @throws InvalidTeamException
	 * @throws InvalidPlayerException
	 *
	 * @see org.chessworks.uscl.services.TournamentService#findOrCreatePlayer(java.lang.String)
	 */
    @Override
    public Player findOrCreatePlayer(String handle) throws InvalidPlayerException, InvalidTeamException {
        Player p = players.get(handle.toLowerCase());
        if (p == null) {
            p = createPlayer(handle);
        }
        return p;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#findPlayer(java.lang.String)
	 */
    @Override
    public Player findPlayer(String handle) {
        Player p = players.get(handle.toLowerCase());
        return p;
    }

    /**
	 * {@inheritDoc}
	 * @throws InvalidTeamException
	 *
	 * @see org.chessworks.uscl.services.TournamentService#findOrCreateTeam(java.lang.String)
	 */
    @Override
    public Team findOrCreateTeam(String handle) throws InvalidTeamException {
        Team t = teams.get(handle.toUpperCase());
        if (t == null) {
            t = createTeam(handle);
        }
        return t;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#findTeam(java.lang.String)
	 */
    @Override
    public Team findTeam(String teamCode) {
        Team t = teams.get(teamCode.toUpperCase());
        return t;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#findAllPlayers()
	 */
    @Override
    public Collection<Player> findAllPlayers() {
        return allPlayers;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#findAllTeams()
	 */
    @Override
    public Collection<Team> findAllTeams() {
        return allTeams;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#createPlayer(java.lang.String)
	 */
    @Override
    public Player createPlayer(String handle) throws InvalidPlayerException, InvalidTeamException {
        String teamCode = teamCode(handle);
        Team team = teams.get(teamCode.toUpperCase());
        if (team == null) {
            throw new InvalidTeamException("Unknown team: {0}", teamCode);
        }
        return createPlayer(handle, team);
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#createPlayer(java.lang.String, org.chessworks.uscl.model.Team)
	 */
    @Override
    public Player createPlayer(String handle, Team team) throws InvalidPlayerException {
        Player p = players.get(handle.toLowerCase());
        if (p != null) {
            throw new InvalidPlayerException("Player with the handle \"{0}\" already exists", handle);
        }
        p = new Player(handle, team);
        team.getPlayers().add(p);
        players.put(handle.toLowerCase(), p);
        return p;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#createTeam(java.lang.String)
	 */
    @Override
    public Team createTeam(String teamCode) throws InvalidTeamException {
        Team t = teams.get(teamCode.toUpperCase());
        if (t != null) {
            throw new InvalidTeamException("Team with the handle \"{0}\" already exists", teamCode);
        }
        if (teamCode == null || teamCode.length() < 2 || teamCode.length() > 3) {
            throw new InvalidTeamException("Teams must have a 2 or 3-letter team code");
        }
        t = new Team(teamCode);
        teams.put(teamCode.toUpperCase(), t);
        return t;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#removePlayer(Player)
	 */
    @Override
    public boolean removePlayer(Player player) {
        playerBoards.remove(player);
        String key = player.getHandle().toLowerCase();
        player = players.remove(key);
        if (player == null) return false;
        Team team = player.getTeam();
        team.getPlayers().remove(player);
        return true;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#removeTeam(Team)
	 */
    @Override
    public int removeTeam(Team team) {
        String key = team.getTeamCode().toUpperCase();
        team = teams.remove(key);
        if (team == null) return -1;
        int count = 0;
        ArrayList<Player> list = new ArrayList<Player>(team.getPlayers());
        for (Player p : list) {
            boolean done = removePlayer(p);
            if (done) count++;
        }
        return count;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#updatePlayer(Player)
	 */
    @Override
    public void updatePlayer(Player player) {
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#updateTeam(Team)
	 */
    @Override
    public void updateTeam(Team team) {
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#flush()
	 */
    @Override
    public void flush() {
    }

    protected void reset() {
        this.playerBoards.clear();
        this.players.clear();
        this.teams.clear();
    }

    public static String teamCode(String handle) throws InvalidPlayerException {
        int i = handle.lastIndexOf('-');
        if (i < 0) {
            throw new InvalidPlayerException("Player handle \"{0}\" must end with a valid team code", handle);
        }
        String teamCode = handle.substring(i + 1);
        return teamCode;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#findOnlinePlayers()
	 */
    @Override
    public Set<Player> findOnlinePlayers() {
        Set<Player> players = new LinkedHashSet<Player>();
        for (Player p : this.players.values()) {
            if (p.isOnline()) {
                players.add(p);
            }
        }
        players = Collections.unmodifiableSet(players);
        return players;
    }

    /**
	 * {@inheritDoc}
	 *
	 * @see org.chessworks.uscl.services.TournamentService#findScheduledPlayers()
	 */
    @Override
    public Collection<Player> findScheduledPlayers() {
        Collection<Player> players = this.playerBoards.keySet();
        List<Player> list = new ArrayList<Player>(players);
        Collections.sort(list);
        list = Collections.unmodifiableList(list);
        return list;
    }
}
