package org.ekstrabilet.game.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import org.ekstrabilet.database.connection.ConnectionManager;
import org.ekstrabilet.database.statements.Statements;
import org.ekstrabilet.game.beans.Game;
import org.ekstrabilet.game.beans.SectorPrice;
import org.ekstrabilet.logic.exceptions.StadiumException;

/**
 * Singleton, represents logic of Game object, is a connector 
 * between Game object and database, implements methods responsible
 * for manipulating that object in database 
 * @author Marcin
 *
 */
public class GameLogic {

    private static GameLogic gameLogic;

    private GameLogic() {
    }

    /**
	 * runs search based on Game object's parameters, results are returned as Game 
	 * objects
	 * @param searchData 
	 * @return list of found Game objects 
	 */
    public List<Game> searchGames(Game searchData) {
        String query = "Select gameID as gID,  G.teamA as ta, G.teamB as tb, G.name as sn, G.city as sc, G.date as d, G.time as t  " + "from (Select * from game natural join stadium where date = '" + searchData.getGameDate() + "' and time >='" + searchData.getGameTime() + "' union Select * from game natural join stadium where date > '" + "" + searchData.getGameDate() + "') G where G.name like '%" + searchData.getStadiumName() + "%' and  G.city like '%" + searchData.getStadiumCity() + "%' " + "and ((G.teamA like '%" + searchData.getTeamA() + "%' and G.teamB like '%" + searchData.getTeamB() + "%') or" + "(G.teamB like '%" + searchData.getTeamA() + "%' and G.teamA like '%" + searchData.getTeamB() + "%')) order by d, t";
        List<Game> searchResult = new LinkedList<Game>();
        Connection conn = ConnectionManager.getManager().getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Game newGame = new Game(rs.getString("ta"), rs.getString("tb"), rs.getString("sn"), rs.getString("sc"), rs.getDate("d"), rs.getTime("t"));
                newGame.setGameID(rs.getInt("gID"));
                searchResult.add(newGame);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    /**
	 * gets games by parameters passed by Game object, results are returned as Game objects
	 * @param game 
	 * @return list of Game objects
	 */
    public List<Game> getGames(Game game) {
        String query = "Select G.gameID as id, G.teamA as ta, G.teamB as tb, G.name as sn, G.city as sc, G.date as d, G.time as t  " + "from (Select * from game natural join stadium where date =" + game.getGameDate() + " and time > '" + game.getGameTime() + "' union Select * from game natural join stadium where date > '" + "" + game.getGameDate() + "') G where G.name like '%" + game.getStadiumName() + "%' and  G.city like '%" + game.getStadiumCity() + "%' " + "and ((G.teamA like '%" + game.getTeamA() + "%' and G.teamB like '%" + game.getTeamB() + "%') or" + "(G.teamB like '%" + game.getTeamA() + "%' and G.teamA like '%" + game.getTeamB() + "%')) order by d, t";
        List<Game> result = new LinkedList<Game>();
        Connection conn = ConnectionManager.getManager().getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                result.add(new Game(rs.getInt("id"), rs.getString("ta"), rs.getString("tb"), rs.getString("sn"), rs.getString("sc"), rs.getDate("d"), rs.getTime("t")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
	 * gets all games from database, results are returned as Game objects
	 * @return list of Game objects
	 */
    public List<Game> getGames() {
        Connection conn = ConnectionManager.getManager().getConnection();
        LinkedList<Game> gameList = null;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(Statements.SELECT_ALL_GAMES);
            gameList = new LinkedList<Game>();
            while (rs.next()) {
                LinkedList<SectorPrice> sectorPrices = new LinkedList<SectorPrice>();
                int gameId = rs.getInt("gameID");
                Game game = new Game(gameId, rs.getString("NAME"), rs.getString("CITY"), rs.getString("teamA"), rs.getString("teamB"), rs.getDate("date"), rs.getTime("time"));
                PreparedStatement stm = conn.prepareStatement(Statements.SELECT_TICKET_PRICE);
                stm.setInt(1, gameId);
                ResultSet res = stm.executeQuery();
                while (res.next()) {
                    SectorPrice sectorPrice = new SectorPrice(res.getInt("sectorID"), res.getInt("price"));
                    sectorPrices.add(sectorPrice);
                }
                game.setSectorPrices(sectorPrices);
                gameList.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return gameList;
    }

    /**
	 * creates new entity in database based on Game object 
	 * @param game 
	 * @return <code>true</code> if adding was successful <code>false<code/> if it failed
	 */
    public Boolean addGame(Game game) {
        String getStadiumId = "Select * from stadium where name = '" + game.getStadiumName() + "' and  city ='" + game.getStadiumCity() + "'";
        Connection conn = ConnectionManager.getManager().getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(getStadiumId);
            rs.next();
            String query = "Insert into game (stadiumID, date, time, teamA, teamB) values (" + rs.getInt("stadiumID") + ", '" + game.getGameDate() + "', '" + game.getGameTime() + "', '" + game.getTeamA() + "', '" + game.getTeamB() + "')";
            st.executeUpdate(query);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
	 * creates new entities in database based on list of Game objects,
	 * operation is a transaction if single step fail it is being rolled back
	 * @param games list of games
	 * @throws StadiumException
	 * @throws SQLException
	 */
    public void addGames(List<Game> games) throws StadiumException, SQLException {
        Connection conn = ConnectionManager.getManager().getConnection();
        conn.setAutoCommit(false);
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            for (Game game : games) {
                stm = conn.prepareStatement(Statements.SELECT_STADIUM);
                stm.setString(1, game.getStadiumName());
                stm.setString(2, game.getStadiumCity());
                rs = stm.executeQuery();
                int stadiumId = -1;
                while (rs.next()) {
                    stadiumId = rs.getInt("stadiumID");
                }
                if (stadiumId == -1) throw new StadiumException("No such stadium");
                stm = conn.prepareStatement(Statements.INSERT_GAME);
                stm.setInt(1, stadiumId);
                stm.setDate(2, game.getGameDate());
                stm.setTime(3, game.getGameTime());
                stm.setString(4, game.getTeamA());
                stm.setString(5, game.getTeamB());
                stm.executeUpdate();
                int gameId = getMaxId();
                List<SectorPrice> sectorPrices = game.getSectorPrices();
                for (SectorPrice price : sectorPrices) {
                    stm = conn.prepareStatement(Statements.INSERT_TICKET_PRICE);
                    stm.setInt(1, gameId);
                    stm.setInt(2, price.getSectorId());
                    stm.setInt(3, price.getPrice());
                    stm.executeUpdate();
                }
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            rs.close();
            stm.close();
        }
        conn.commit();
        conn.setAutoCommit(true);
    }

    /**
	 * update entity in database using parameters passed by Game object 
	 * @param gameID id of game
	 * @param game
	 * @return <code>true</code> if operation was successful <code>false</code> if it failed
	 */
    public Boolean updateGame(Integer gameID, Game game) {
        String getStadiumId = "Select * from stadium where name = '" + game.getStadiumName() + "' and  city ='" + game.getStadiumCity() + "'";
        Connection conn = ConnectionManager.getManager().getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(getStadiumId);
            rs.next();
            String query = "Update game set stadiumID = " + rs.getInt("stadiumID") + ",  date = '" + game.getGameDate() + "', " + "time = '" + game.getGameTime() + "', teamA = '" + game.getTeamA() + "', teamB = '" + game.getTeamB() + "'" + " where gameID = " + gameID;
            st.executeUpdate(query);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
	 * delete game from database
	 * @param gameID game id
	 * @return true if operation was successful false if it failed
	 */
    public Boolean deleteGame(Integer gameID) {
        Connection conn = ConnectionManager.getManager().getConnection();
        try {
            Statement st = conn.createStatement();
            String query = "Delete from game where gameID =" + gameID;
            st.executeUpdate(query);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
	 * removes list of games from database, operation is a transaction
	 * if single step fail its being rolled back
	 * @param games
	 * @throws SQLException
	 */
    public void removeGames(List<Game> games) throws SQLException {
        Connection conn = ConnectionManager.getManager().getConnection();
        PreparedStatement stm = null;
        conn.setAutoCommit(false);
        try {
            for (Game game : games) {
                stm = conn.prepareStatement(Statements.DELETE_GAME);
                stm.setInt(1, game.getGameID());
                stm.executeUpdate();
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            if (stm != null) stm.close();
        }
        conn.commit();
        conn.setAutoCommit(true);
    }

    /**
	 * gets all game ids from database
	 * @return list of game ids
	 */
    public LinkedList<Integer> getGameIDs() {
        String query = "Select gameID from game order by gameID";
        LinkedList<Integer> gList = new LinkedList<Integer>();
        Connection conn = ConnectionManager.getManager().getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                gList.add(rs.getInt("gameID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gList;
    }

    private int getMaxId() {
        Connection conn = ConnectionManager.getManager().getConnection();
        Statement st = null;
        ResultSet rs = null;
        int maxId = -1;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("Select max(gameID) as maxId from game");
            while (rs.next()) {
                maxId = rs.getInt("maxId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxId;
    }

    /**
	 * 
	 * @return instance of GameLogic
	 */
    public static GameLogic getInstance() {
        if (gameLogic == null) {
            gameLogic = new GameLogic();
        }
        return gameLogic;
    }

    /**
	 * Initializes Game object using row
	 * @param row
	 * @return initialized Game object
	 */
    public static Game initGame(Row row) {
        Game g = new Game();
        g.setTeamA(row.getHomeTeam());
        g.setTeamB(row.getVisitorTeam());
        g.setStadiumName(row.getStadium());
        g.setStadiumCity(row.getCity());
        g.setGameDate(row.getDate());
        g.setGameTime(row.getTime());
        return g;
    }
}
