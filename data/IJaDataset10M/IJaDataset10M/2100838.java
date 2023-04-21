package jtmsmon.javadb;

import jtmslib.challenge.TMChallenge;
import jtmslib.challenge.TMRaceRecord;
import jtmslib.player.TMPlayer;
import jtmslib.player.TMPlayerRanking;
import jtmsmon.db.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 *
 * @author th
 */
public abstract class JavaDBGeneric implements jtmsmon.db.JTMSDB {

    /**
   * Method description
   *
   *
   * @throws SQLException
   */
    public void close() throws SQLException {
        try {
            ConnectionPool.shutdownDriver();
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    /**
   * Method description
   *
   *
   * @param id
   *
   * @return
   *
   * @throws SQLException
   */
    public TMChallenge getChallenge(int id) throws SQLException {
        TMChallenge challenge = new TMChallenge();
        challenge.setId(id);
        Statement statement = readOnlyConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from challenge where id=" + id);
        if (resultSet.next()) {
            challenge.setUid(resultSet.getString(2));
            challenge.setName(resultSet.getString(3));
            challenge.setAuthor(resultSet.getString(4));
            challenge.setFileName(resultSet.getString(5));
            challenge.setEnvironment(resultSet.getString(6));
            challenge.setMood(resultSet.getString(7));
            challenge.setAuthorTime(resultSet.getDouble(8));
            challenge.setGoldTime(resultSet.getDouble(9));
            challenge.setSilverTime(resultSet.getDouble(10));
            challenge.setBronzeTime(resultSet.getDouble(11));
            challenge.setCopperPrice(resultSet.getInt(12));
            challenge.setLapRace(resultSet.getInt(13) == 1);
            challenge.setFirstRaceTime(resultSet.getLong(14));
            challenge.setLatestRaceTime(resultSet.getLong(15));
            resultSet.close();
            statement.close();
        }
        resultSet.close();
        statement.close();
        return challenge;
    }

    /**
   * Method description
   *
   *
   * @param uid
   *
   * @return
   *
   * @throws SQLException
   */
    public TMChallenge getChallenge(String uid) throws SQLException {
        TMChallenge challenge = new TMChallenge();
        challenge.setUid(uid);
        Statement statement = readOnlyConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from challenge where challengeuid='" + uid + "'");
        if (resultSet.next()) {
            challenge.setId(resultSet.getInt(1));
            challenge.setName(resultSet.getString(3));
            challenge.setAuthor(resultSet.getString(4));
            challenge.setFileName(resultSet.getString(5));
            challenge.setEnvironment(resultSet.getString(6));
            challenge.setMood(resultSet.getString(7));
            challenge.setAuthorTime(resultSet.getDouble(8));
            challenge.setGoldTime(resultSet.getDouble(9));
            challenge.setSilverTime(resultSet.getDouble(10));
            challenge.setBronzeTime(resultSet.getDouble(11));
            challenge.setCopperPrice(resultSet.getInt(12));
            challenge.setLapRace(resultSet.getInt(13) == 1);
            challenge.setFirstRaceTime(resultSet.getLong(14));
            challenge.setLatestRaceTime(resultSet.getLong(15));
        }
        resultSet.close();
        statement.close();
        return challenge;
    }

    /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   *
   * @throws SQLException
   */
    public TMChallenge getChallengeByName(String name) throws SQLException {
        return null;
    }

    /**
   * Method description
   *
   *
   * @return
   *
   * @throws SQLException
   */
    public ArrayList<TMChallenge> getChallenges() throws SQLException {
        ArrayList<TMChallenge> challenges = new ArrayList<TMChallenge>();
        Statement statement = readOnlyConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from challenge order by firstracetime desc");
        while (resultSet.next()) {
            TMChallenge challenge = new TMChallenge();
            challenge.setId(resultSet.getInt(1));
            challenge.setUid(resultSet.getString(2));
            challenge.setName(resultSet.getString(3));
            challenge.setAuthor(resultSet.getString(4));
            challenge.setFileName(resultSet.getString(5));
            challenge.setEnvironment(resultSet.getString(6));
            challenge.setMood(resultSet.getString(7));
            challenge.setAuthorTime(resultSet.getDouble(8));
            challenge.setGoldTime(resultSet.getDouble(9));
            challenge.setSilverTime(resultSet.getDouble(10));
            challenge.setBronzeTime(resultSet.getDouble(11));
            challenge.setCopperPrice(resultSet.getInt(12));
            challenge.setLapRace(resultSet.getInt(13) == 1);
            challenge.setFirstRaceTime(resultSet.getLong(14));
            challenge.setLatestRaceTime(resultSet.getLong(15));
            challenges.add(challenge);
        }
        resultSet.close();
        statement.close();
        return challenges;
    }

    /**
   * Method description
   *
   *
   * @param id
   *
   * @return
   *
   * @throws SQLException
   */
    public TMPlayer getPlayer(int id) throws SQLException {
        return getPlayerFromDB("select * from player where id=" + id);
    }

    /**
   * Method description
   *
   *
   * @param loginName
   *
   * @return
   *
   * @throws SQLException
   */
    public TMPlayer getPlayer(String loginName) throws SQLException {
        return getPlayerFromDB("select * from player where loginname='" + loginName + "'");
    }

    /**
   * Method description
   *
   *
   * @return
   *
   * @throws SQLException
   */
    public ArrayList<Integer> getPlayerIds() throws SQLException {
        ArrayList<Integer> playerIds = new ArrayList<Integer>();
        Statement statement = readOnlyConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("select id from player");
        while (resultSet.next()) {
            playerIds.add(resultSet.getInt(1));
        }
        resultSet.close();
        statement.close();
        return playerIds;
    }

    /**
   * Method description
   *
   *
   * @return
   *
   * @throws SQLException
   */
    public ArrayList<TMPlayer> getPlayers() throws SQLException {
        ArrayList<TMPlayer> players = new ArrayList<TMPlayer>();
        Statement statement = readOnlyConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from player order by id");
        while (resultSet.next()) {
            TMPlayer player = new TMPlayer();
            setPlayerFromResultSet(player, resultSet);
            players.add(player);
        }
        resultSet.close();
        statement.close();
        return players;
    }

    /**
   * Method description
   *
   *
   * @param queryRecord
   *
   * @return
   *
   * @throws SQLException
   */
    public TMRaceRecord getRaceRecord(TMRaceRecord queryRecord) throws SQLException {
        TMRaceRecord record = null;
        Statement statement = readOnlyConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from racerecords where playerid=" + queryRecord.getPlayerDbId() + " and challengeid=" + queryRecord.getChallengeId());
        if (resultSet.next()) {
            record = new TMRaceRecord();
            setRaceRecordFromResultSet(record, resultSet);
        }
        resultSet.close();
        statement.close();
        return record;
    }

    /**
   * Method description
   *
   *
   * @return
   *
   * @throws SQLException
   */
    public ArrayList<TMRaceRecord> getRaceRecords() throws SQLException {
        ArrayList<TMRaceRecord> records = new ArrayList<TMRaceRecord>();
        Statement statement = readOnlyConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from racerecords");
        while (resultSet.next()) {
            TMRaceRecord record = new TMRaceRecord();
            setRaceRecordFromResultSet(record, resultSet);
            records.add(record);
        }
        resultSet.close();
        statement.close();
        return records;
    }

    /**
   * Method description
   * @param playerDbId 
   * @param archiveSuffix 
   * @return 
   * @throws SQLException 
   */
    public ArrayList<TMRaceRecord> getRaceRecordsForPlayer(int playerDbId, String archiveSuffix) throws SQLException {
        ArrayList<TMRaceRecord> raceRecords = new ArrayList<TMRaceRecord>();
        Statement statement = readOnlyConnection.createStatement();
        String tableName = "racerecords" + ((archiveSuffix != null) ? archiveSuffix : "");
        ResultSet resultSet = statement.executeQuery("select * from " + tableName + " where playerid=" + playerDbId + "order by rank");
        while (resultSet.next()) {
            if (playerDbId == resultSet.getInt(1)) {
                TMRaceRecord record = new TMRaceRecord();
                record.setPlayerDbId(playerDbId);
                record.setChallengeId(resultSet.getInt(2));
                record.setTimestamp(resultSet.getLong(3));
                record.setTime(resultSet.getDouble(4));
                record.setRank(resultSet.getInt(5));
                record.setScore(resultSet.getDouble(6));
                record.setLadderScore(resultSet.getDouble(7));
                record.setFinishedLaps(resultSet.getInt(8));
                raceRecords.add(record);
            }
        }
        resultSet.close();
        statement.close();
        return raceRecords;
    }

    public ArrayList<TMRaceRecord> getRaceRecordsForPlayer(int playerDbId) throws SQLException {
        return getRaceRecordsForPlayer(playerDbId, null);
    }

    /**
   * Method description
   *
   *
   * @param limit
   *
   * @return
   *
   * @throws SQLException
   */
    public ArrayList<TMPlayerRanking> getRankingByJtmsScore(int limit) throws SQLException {
        ArrayList<TMPlayerRanking> ranking = new ArrayList<TMPlayerRanking>();
        Statement statement = readOnlyConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("select id, loginName, nickName, nation, score from player where score > 0 order by score desc");
        while (resultSet.next() && (limit != 0)) {
            int playerId = resultSet.getInt(1);
            String loginName = resultSet.getString(2);
            String nickName = resultSet.getString(3);
            String nation = resultSet.getString(4);
            long score = resultSet.getLong(5);
            ranking.add(new TMPlayerRanking(loginName, nickName, playerId, nation, score));
            limit--;
        }
        resultSet.close();
        statement.close();
        return ranking;
    }

    /**
   * Method description
   *
   *
   * @param challengeId
   * @param players
   * @param limit
   * @param archiveSuffix
   *
   * @return
   *
   * @throws SQLException
   */
    public ArrayList<TMPlayerRanking> getRankingForChallenge(int challengeId, ArrayList<TMPlayer> players, int limit, String archiveSuffix) throws SQLException {
        ArrayList<TMPlayerRanking> ranking = new ArrayList<TMPlayerRanking>();
        Statement statement = readOnlyConnection.createStatement();
        String raceRecordTable = "racerecords" + ((archiveSuffix != null) ? archiveSuffix : "");
        String query = "select a.loginName, a.nickName, a.id, a.nation, b.bestTime, b.racetimestamp, b.rank" + " from player a, " + raceRecordTable + " b where a.id = b.playerId and b.challengeid=" + challengeId;
        if (players != null) {
            String values = "";
            TMPlayer player = null;
            for (int i = 0; i < players.size(); i++) {
                player = players.get(i);
                if ((player != null) && (player.getPlayerDbId() > 0)) {
                    values += player.getPlayerDbId() + (((i + 1) < players.size()) ? "," : "");
                }
            }
            if (values.length() > 0) {
                query += " and a.id in (" + values + ")";
            }
        }
        query += " order by b.besttime, b.racetimestamp";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next() && (limit != 0)) {
            String loginName = resultSet.getString(1);
            String nickName = resultSet.getString(2);
            int playerId = resultSet.getInt(3);
            String nation = resultSet.getString(4);
            double bestTime = resultSet.getDouble(5);
            long timestamp = resultSet.getLong(6);
            int rank = resultSet.getInt(7);
            ranking.add(new TMPlayerRanking(loginName, nickName, playerId, nation, bestTime, timestamp, rank));
            limit--;
        }
        resultSet.close();
        statement.close();
        return ranking;
    }

    /**
   * Method description
   *
   *
   * @param challengeId
   * @param limit
   * @param archiveSuffix
   *
   * @return
   *
   * @throws SQLException
   */
    public ArrayList<TMPlayerRanking> getRankingForChallenge(int challengeId, int limit, String archiveSuffix) throws SQLException {
        return getRankingForChallenge(challengeId, null, limit, archiveSuffix);
    }

    /**
   * Method description
   *
   *
   * @param challengeId
   * @param limit
   *
   * @return
   *
   * @throws SQLException
   */
    public ArrayList<TMPlayerRanking> getRankingForChallenge(int challengeId, int limit) throws SQLException {
        return getRankingForChallenge(challengeId, null, limit, null);
    }

    /**
   * Method description
   *
   *
   * @param challengeId
   *
   * @return
   *
   * @throws SQLException
   */
    public Hashtable getRatingForChallenge(int challengeId) throws SQLException {
        Hashtable ht = new Hashtable();
        Statement statement = readOnlyConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("select rating, count(*) from challengerating" + " where challengeid=" + challengeId + " group by  rating");
        while (resultSet.next()) {
            int rating = resultSet.getInt("rating");
            if (rating == 0) {
                ht.put(rating, resultSet.getInt(2));
            }
            if (rating == 1) {
                ht.put(rating, resultSet.getInt(2));
            }
        }
        return ht;
    }

    /**
   * Method description
   *
   *
   * @param players
   *
   */
    public void importPlayers(ArrayList<TMPlayer> players) {
        int counter = 0;
        TMPlayer p = null;
        String command = null;
        try {
            Connection dbConnection = ConnectionPool.getConnection();
            dbConnection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            Statement statement = dbConnection.createStatement();
            statement.executeUpdate("delete from player");
            for (TMPlayer player : players) {
                p = player;
                String columns = "id,loginname,firstlogin,latestlogin";
                String values = player.getPlayerDbId() + ", '" + player.getLoginName() + "'";
                values += "," + player.getFirstLogin() + "," + player.getLatestLogin();
                if (player.getNickName() != null) {
                    columns += ",nickname";
                    String nickname = player.getNickName();
                    nickname = nickname.replaceAll("'", "''");
                    values += ",'" + nickname + "'";
                }
                if (player.getNation() != null) {
                    columns += ",nation";
                    values += ",'" + player.getNation() + "'";
                }
                if (player.getPlayerTmId() > 0) {
                    columns += ",playerid";
                    values += "," + player.getPlayerTmId();
                }
                if (player.getTeamId() > 0) {
                    columns += ",teamid";
                    values += "," + player.getTeamId();
                }
                if (player.getIpAddress() != null) {
                    columns += ",clientipaddress";
                    values += ",'" + player.getIpAddress() + "'";
                }
                if (player.getConnectionType() != null) {
                    columns += ",connectiontype";
                    values += ",'" + player.getConnectionType() + "'";
                }
                columns += ",isspectator";
                values += "," + ((player.isSpectator()) ? 1 : 0);
                columns += ",isinofficialmode";
                values += "," + ((player.isInOfficialMode()) ? 1 : 0);
                columns += ",hasjoinmatch";
                values += "," + ((player.hasJoinMatch()) ? 1 : 0);
                if (player.getLadderRanking() > 0.0) {
                    columns += ",ladderranking";
                    values += "," + player.getLadderRanking();
                }
                command = "insert into player ( " + columns + " ) values ( " + values + " )";
                statement.executeUpdate(command);
                counter++;
            }
            dbConnection.commit();
            statement.close();
            ConnectionPool.closeConnection(dbConnection);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(counter + " player is bad");
            System.err.println("player = >>" + p + "<<");
            System.err.println("command = " + command);
            System.exit(0);
        }
        System.out.println(counter + " players imported");
    }

    /**
   * Method description
   *
   *
   * @param raceRecords
   *
   * @throws SQLException
   */
    public void importRaceRecords(ArrayList<TMRaceRecord> raceRecords) throws SQLException {
        Connection dbConnection = ConnectionPool.getConnection();
        dbConnection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        int counter = 0;
        Statement statement = dbConnection.createStatement();
        statement.executeUpdate("delete from racerecords");
        for (TMRaceRecord record : raceRecords) {
            String columns = "playerid,challengeid,racetimestamp,besttime";
            String values = record.getPlayerDbId() + "," + record.getChallengeId() + "," + record.getTimestamp() + "," + record.getTime();
            columns += ",rank,score,ladderscore,finishedlaps";
            values += "," + record.getRank();
            values += "," + record.getScore();
            values += "," + record.getLadderScore();
            values += "," + record.getFinishedLaps();
            String command = "insert into racerecords ( " + columns + " ) values ( " + values + " )";
            statement.executeUpdate(command);
            counter++;
        }
        dbConnection.commit();
        System.out.println(counter + " race records imported");
        statement.close();
        ConnectionPool.closeConnection(dbConnection);
    }

    /**
   * Method description
   *
   *
   * @param create
   *
   * @throws Exception
   */
    public abstract void init(boolean create) throws Exception;

    /**
   * Method description
   *
   *
   * @param record
   * @param resultSet
   *
   * @throws SQLException
   */
    public void setRaceRecordFromResultSet(TMRaceRecord record, ResultSet resultSet) throws SQLException {
        record.setPlayerDbId(resultSet.getInt(1));
        record.setChallengeId(resultSet.getInt(2));
        record.setTimestamp(resultSet.getLong(3));
        record.setTime(resultSet.getDouble(4));
        record.setRank(resultSet.getInt(5));
        record.setScore(resultSet.getDouble(6));
        record.setLadderScore(resultSet.getDouble(7));
        record.setFinishedLaps(resultSet.getInt(8));
    }

    /**
   * Method description
   *
   *
   * @param challenge
   *
   * @return
   *
   * @throws SQLException
   */
    public TMChallenge updateChallenge(TMChallenge challenge) throws SQLException {
        Connection dbConnection = ConnectionPool.getConnection();
        dbConnection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        try {
            Statement statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery("select * from challenge where challengeuid = '" + challenge.getUid() + "'");
            if (resultSet.next()) {
                challenge.setId(resultSet.getInt("id"));
                resultSet.updateString("challengename", challenge.getName());
                if (challenge.getAuthor() != null) {
                    resultSet.updateString("author", challenge.getAuthor());
                    resultSet.updateInt("laprace", ((challenge.isLapRace()) ? 1 : 0));
                }
                if (challenge.getLatestRaceTime() > 0l) {
                    resultSet.updateLong("latestracetime", challenge.getLatestRaceTime());
                }
                if (challenge.getGoldTime() > 0.0) {
                    resultSet.updateDouble("goldtime", challenge.getGoldTime());
                }
                if (challenge.getSilverTime() > 0.0) {
                    resultSet.updateDouble("silvertime", challenge.getSilverTime());
                }
                if (challenge.getBronzeTime() > 0.0) {
                    resultSet.updateDouble("bronzetime", challenge.getBronzeTime());
                }
                if (challenge.getAuthorTime() > 0.0) {
                    resultSet.updateDouble("authortime", challenge.getAuthorTime());
                }
                resultSet.updateRow();
            } else {
                System.out.println("Insert new challenge: Name=" + challenge.getName() + ", UID=" + challenge.getUid());
                resultSet.moveToInsertRow();
                resultSet.updateString("challengeuid", challenge.getUid());
                resultSet.updateString("challengename", challenge.getName());
                long timestamp = System.currentTimeMillis();
                resultSet.updateLong("firstracetime", timestamp);
                resultSet.updateLong("latestracetime", timestamp);
                if (challenge.getFileName() != null) {
                    resultSet.updateString("filename", challenge.getFileName());
                }
                if (challenge.getAuthor() != null) {
                    resultSet.updateString("author", challenge.getAuthor());
                }
                if (challenge.getEnvironment() != null) {
                    resultSet.updateString("environment", challenge.getEnvironment());
                }
                if (challenge.getMood() != null) {
                    resultSet.updateString("mood", challenge.getMood());
                }
                if (challenge.getAuthorTime() > 0.0) {
                    resultSet.updateDouble("authortime", challenge.getAuthorTime());
                }
                if (challenge.getGoldTime() > 0.0) {
                    resultSet.updateDouble("goldtime", challenge.getGoldTime());
                }
                if (challenge.getSilverTime() > 0.0) {
                    resultSet.updateDouble("silvertime", challenge.getSilverTime());
                }
                if (challenge.getBronzeTime() > 0.0) {
                    resultSet.updateDouble("bronzetime", challenge.getBronzeTime());
                }
                if (challenge.getCopperPrice() > 0) {
                    resultSet.updateInt("copperprice", challenge.getCopperPrice());
                }
                resultSet.updateInt("laprace", ((challenge.isLapRace()) ? 1 : 0));
                resultSet.insertRow();
            }
            resultSet.close();
            statement.close();
        } finally {
            ConnectionPool.closeConnection(dbConnection);
        }
        return getChallenge(challenge.getUid());
    }

    /**
   * Method description
   *
   *
   * @param playerDbId
   * @param challengeDbId
   * @param rating
   *
   * @throws SQLException
   */
    public void updateChallengeRating(int playerDbId, int challengeDbId, int rating) throws SQLException {
        Connection dbConnection = ConnectionPool.getConnection();
        dbConnection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        try {
            Statement statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery("select * from ChallengeRating where playerId=" + playerDbId + " and challengeId=" + challengeDbId);
            if (resultSet.next()) {
                System.out.println("Rating found");
                resultSet.updateInt("rating", rating);
                resultSet.updateRow();
            } else {
                System.out.println("Insert new rating");
                resultSet.moveToInsertRow();
                resultSet.updateInt("playerId", playerDbId);
                resultSet.updateInt("challengeId", challengeDbId);
                resultSet.updateInt("rating", rating);
                resultSet.insertRow();
            }
            dbConnection.commit();
            resultSet.close();
            statement.close();
        } finally {
            ConnectionPool.closeConnection(dbConnection);
        }
    }

    /**
   * Method description
   *
   *
   * @param player
   *
   * @return
   *
   * @throws SQLException
   */
    public TMPlayer updatePlayer(TMPlayer player) throws SQLException {
        Connection dbConnection = ConnectionPool.getConnection();
        dbConnection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        try {
            Statement statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery("select * from player where loginname = '" + player.getLoginName() + "'");
            if (resultSet.next()) {
                player.setPlayerDbId(resultSet.getInt(1));
                resultSet.updateString("nickname", player.getNickName());
                if ((player.getNation() != null) && (player.getNation().length() > 0)) {
                    resultSet.updateString("nation", player.getNation());
                }
                if (player.getLatestLogin() > 0) {
                    resultSet.updateLong("latestlogin", player.getLatestLogin());
                }
                if (player.getPlayerTmId() > 0) {
                    resultSet.updateInt("playerid", player.getPlayerTmId());
                }
                if (player.getTeamId() > 0) {
                    resultSet.updateInt("teamid", player.getTeamId());
                }
                if (player.getIpAddress() != null) {
                    resultSet.updateString("clientipaddress", player.getIpAddress());
                }
                if (player.getConnectionType() != null) {
                    resultSet.updateString("connectiontype", player.getConnectionType());
                }
                resultSet.updateInt("isspectator", ((player.isSpectator()) ? 1 : 0));
                resultSet.updateInt("isinofficialmode", ((player.isInOfficialMode()) ? 1 : 0));
                resultSet.updateInt("hasjoinmatch", ((player.hasJoinMatch()) ? 1 : 0));
                if (player.getLadderRanking() > 0.0) {
                    resultSet.updateDouble("ladderranking", player.getLadderRanking());
                }
                if (player.getJtmsScore() > 0) {
                    resultSet.updateLong("score", player.getJtmsScore());
                }
                if ((player.getPassword() != null) && (player.getPassword().length() > 5)) {
                    resultSet.updateString("password", player.getPassword());
                }
                resultSet.updateRow();
            } else {
                System.out.println("Insert new player " + player.getLoginName());
                resultSet.moveToInsertRow();
                resultSet.updateString("loginname", player.getLoginName());
                long timestamp = System.currentTimeMillis();
                resultSet.updateLong("firstlogin", timestamp);
                resultSet.updateLong("latestlogin", timestamp);
                if (player.getNickName() != null) {
                    resultSet.updateString("nickname", player.getNickName());
                }
                if (player.getNation() != null) {
                    resultSet.updateString("nation", player.getNation());
                }
                if (player.getPlayerTmId() > 0) {
                    resultSet.updateInt("playerid", player.getPlayerTmId());
                }
                if (player.getTeamId() > 0) {
                    resultSet.updateInt("teamid", player.getTeamId());
                }
                if (player.getIpAddress() != null) {
                    resultSet.updateString("clientipaddress", player.getIpAddress());
                }
                if (player.getConnectionType() != null) {
                    resultSet.updateString("connectiontype", player.getConnectionType());
                }
                resultSet.updateInt("isspectator", ((player.isSpectator()) ? 1 : 0));
                resultSet.updateInt("isinofficialmode", ((player.isInOfficialMode()) ? 1 : 0));
                resultSet.updateInt("hasjoinmatch", ((player.hasJoinMatch()) ? 1 : 0));
                if (player.getLadderRanking() > 0.0) {
                    resultSet.updateDouble("ladderranking", player.getLadderRanking());
                }
                resultSet.insertRow();
            }
            dbConnection.commit();
            resultSet.close();
            statement.close();
        } finally {
            ConnectionPool.closeConnection(dbConnection);
        }
        return getPlayer(player.getLoginName());
    }

    /**
   * Method description
   *
   *
   * @param playerScores
   *
   * @throws SQLException
   */
    public void updatePlayerScores(HashMap<Integer, Long> playerScores) throws SQLException {
        Connection dbConnection = ConnectionPool.getConnection();
        dbConnection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        try {
            PreparedStatement statement = dbConnection.prepareStatement("select id, score from player for update", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                long score = playerScores.get(id);
                if (score > 0) {
                    resultSet.updateLong(2, score);
                    resultSet.updateRow();
                }
            }
            dbConnection.commit();
            resultSet.close();
            statement.close();
        } finally {
            try {
                ConnectionPool.closeConnection(dbConnection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
   * Method description
   *
   *
   * @param record
   *
   * @return
   *
   * @throws SQLException
   */
    public TMRaceRecord updateRaceRecord(TMRaceRecord record) throws SQLException {
        Connection dbConnection = ConnectionPool.getConnection();
        dbConnection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from racerecords where playerid=" + record.getPlayerDbId() + " and challengeid=" + record.getChallengeId());
            if (resultSet.next()) {
                System.out.println("Record found.");
                String command = "update racerecords set ";
                command += "besttime=" + record.getTime();
                command += ",racetimestamp=" + record.getTimestamp();
                command += ",score=" + record.getScore();
                command += ",ladderscore=" + record.getLadderScore();
                command += ",finishedlaps=" + record.getFinishedLaps();
                command += " where playerid=" + record.getPlayerDbId() + " and challengeid=" + record.getChallengeId();
                statement.executeUpdate(command);
            } else {
                System.out.println("Insert new record ");
                String columns = "playerid,challengeid,racetimestamp,besttime";
                String values = record.getPlayerDbId() + "," + record.getChallengeId() + "," + record.getTimestamp() + "," + record.getTime();
                columns += ",score,ladderscore,finishedlaps";
                values += "," + record.getScore();
                values += "," + record.getLadderScore();
                values += "," + record.getFinishedLaps();
                String command = "insert into racerecords ( " + columns + " ) values ( " + values + " )";
                statement.executeUpdate(command);
            }
            dbConnection.commit();
            resultSet.close();
            statement.close();
        } finally {
            ConnectionPool.closeConnection(dbConnection);
        }
        return getRaceRecord(record);
    }

    /**
   * Method description
   *
   *
   * @param challengeUid
   *
   * @throws SQLException
   */
    public void updateRanksForChallenge(String challengeUid) throws SQLException {
        updateRanksForChallenge(getChallenge(challengeUid).getId());
    }

    /**
   * Method description
   *
   *
   * @param challengeDbId
   *
   * @throws SQLException
   */
    public void updateRanksForChallenge(int challengeDbId) throws SQLException {
        HashMap<Integer, Integer> ranks = new HashMap<Integer, Integer>(10000);
        {
            int rank = 1;
            Statement statement = readOnlyConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("select playerid from racerecords where challengeid = " + challengeDbId + " order by besttime, racetimestamp");
            while (resultSet.next()) {
                ranks.put(resultSet.getInt(1), rank++);
            }
            resultSet.close();
            statement.close();
        }
        Connection dbConnection = ConnectionPool.getConnection();
        dbConnection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        try {
            PreparedStatement statement = dbConnection.prepareStatement("select playerid, rank from racerecords where challengeid = " + challengeDbId + " for update", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                resultSet.updateInt(2, ranks.get(resultSet.getInt(1)));
                resultSet.updateRow();
            }
            dbConnection.commit();
            resultSet.close();
            statement.close();
        } finally {
            ConnectionPool.closeConnection(dbConnection);
        }
    }

    /**
   * Method description
   *
   *
   * @param query
   *
   * @return
   *
   * @throws SQLException
   */
    private TMPlayer getPlayerFromDB(String query) throws SQLException {
        TMPlayer player = null;
        Statement statement = readOnlyConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            player = new TMPlayer();
            setPlayerFromResultSet(player, resultSet);
        }
        resultSet.close();
        statement.close();
        return player;
    }

    /**
   * Method description
   *
   *
   * @param player
   * @param resultSet
   *
   * @throws SQLException
   */
    private void setPlayerFromResultSet(TMPlayer player, ResultSet resultSet) throws SQLException {
        player.setPlayerDbId(resultSet.getInt(1));
        player.setLoginName(resultSet.getString(2));
        player.setNickName(resultSet.getString(3));
        player.setPlayerTmId(resultSet.getInt(4));
        player.setTeamId(resultSet.getInt(5));
        player.setFirstLogin(resultSet.getLong(6));
        player.setLatestLogin(resultSet.getLong(7));
        player.setIpAddress(resultSet.getString(8));
        player.setConnectionType(resultSet.getString(9));
        player.setIsSpectator(resultSet.getInt(10) == 1);
        player.setIsInOfficialMode(resultSet.getInt(11) == 1);
        player.setJoinMatch(resultSet.getInt(12) == 1);
        player.setLadderRanking(resultSet.getDouble(13));
        player.setNation(resultSet.getString(14));
        player.setPassword(resultSet.getString(15));
        player.setStatus(resultSet.getString(16));
        player.setRoles(resultSet.getString(17));
        player.setJtmsScore(resultSet.getLong(18));
    }

    /** Field description */
    protected Connection readOnlyConnection;

    /** Field description */
    private String dbPath;
}
