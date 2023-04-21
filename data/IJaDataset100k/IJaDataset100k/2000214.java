package main;

import framework.AbstractGame;
import framework.Person;
import impl.business.MatrixBiz;
import impl.game.grid.GridGame;
import impl.persons.*;
import org.apache.log4j.Logger;
import java.sql.*;

/**
 * Created by IntelliJ IDEA.
 * User: adodonov
 * Date: 15.04.2008
 * Time: 14:48:19
 * To change this template use File | Settings | File Templates.
 */
public class Statistic {

    static Logger LOG = Logger.getLogger(Main.class);

    private static final int TURNS_COUNT = 10000;

    public static String comb2string(int[] Combination) {
        String combString = "";
        for (int i = 0; i < 10; i++) {
            combString = combString + new Integer(Combination[i]).toString();
        }
        return combString;
    }

    public static void main(String[] args) throws SQLException {
        GridGame game = new GridGame(3, 3, GridGame.Q8);
        MatrixBiz biz = new MatrixBiz(10, 8, 3, 0);
        int combinationNum = 0;
        int insertedEnvId;
        int resultIdx = 0;
        Connection conn = null;
        PreparedStatement psInsert = null;
        Statement s = null;
        ResultSet rs = null;
        String sql;
        conn = DriverManager.getConnection("jdbc:derby:db;");
        s = conn.createStatement();
        System.out.println(conn);
        sql = "DELETE FROM results";
        s.executeUpdate(sql);
        sql = "DELETE FROM combinations";
        s.executeUpdate(sql);
        sql = "SELECT env_id FROM environment WHERE " + "matrix = '" + biz.toString() + "' " + "AND totalturnnumber =" + TURNS_COUNT + " " + "AND gametype = '" + game.getClass().getSimpleName() + "'";
        rs = s.executeQuery(sql);
        if (!rs.next()) {
            s = conn.createStatement();
            psInsert = conn.prepareStatement("INSERT INTO environment " + "   (totalturnnumber, matrix, gametype) " + "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            psInsert.clearParameters();
            psInsert.setInt(1, TURNS_COUNT);
            psInsert.setString(2, biz.toString());
            psInsert.setString(3, game.getClass().getSimpleName());
            int rowCount = psInsert.executeUpdate();
            System.out.println(rowCount);
            rs = s.executeQuery("select IDENTITY_VAL_LOCAL() from environment");
            rs.next();
        }
        insertedEnvId = rs.getInt(1);
        int[][] Combinations = new int[24310][10];
        int totalGamersNum = 8;
        int[] totalSum = new int[10];
        int[] idx = new int[10];
        int[] Combination = new int[10];
        int combinationIdx = 0;
        long estimate;
        long t0, t;
        t0 = System.currentTimeMillis();
        for (idx[0] = 0; idx[0] <= totalGamersNum; idx[0]++) {
            Combination[0] = idx[0];
            totalSum[0] = idx[0];
            for (idx[1] = 0; idx[1] <= (totalGamersNum - totalSum[0]); idx[1]++) {
                Combination[1] = idx[1];
                totalSum[1] = totalSum[0] + idx[1];
                for (idx[2] = 0; idx[2] <= (totalGamersNum - totalSum[1]); idx[2]++) {
                    Combination[2] = idx[2];
                    totalSum[2] = totalSum[1] + idx[2];
                    for (idx[3] = 0; idx[3] <= (totalGamersNum - totalSum[2]); idx[3]++) {
                        Combination[3] = idx[3];
                        totalSum[3] = totalSum[2] + idx[3];
                        for (idx[4] = 0; idx[4] <= (totalGamersNum - totalSum[3]); idx[4]++) {
                            Combination[4] = idx[4];
                            totalSum[4] = totalSum[3] + idx[4];
                            for (idx[5] = 0; idx[5] <= (totalGamersNum - totalSum[4]); idx[5]++) {
                                Combination[5] = idx[5];
                                totalSum[5] = totalSum[4] + idx[5];
                                for (idx[6] = 0; idx[6] <= (totalGamersNum - totalSum[5]); idx[6]++) {
                                    Combination[6] = idx[6];
                                    totalSum[6] = totalSum[5] + idx[6];
                                    for (idx[7] = 0; idx[7] <= (totalGamersNum - totalSum[6]); idx[7]++) {
                                        Combination[7] = idx[7];
                                        totalSum[7] = totalSum[6] + idx[7];
                                        for (idx[8] = 0; idx[8] <= (totalGamersNum - totalSum[7]); idx[8]++) {
                                            Combination[8] = idx[8];
                                            totalSum[8] = totalSum[7] + idx[8];
                                            idx[9] = totalGamersNum - totalSum[8];
                                            Combination[9] = idx[9];
                                            totalSum[9] = totalSum[8] + idx[9];
                                            for (int strategyIdx = 0; strategyIdx < 10; strategyIdx++) {
                                                Person[] players = new Person[9];
                                                Person testedStrategy = getStrategy(strategyIdx, game);
                                                players[0] = testedStrategy;
                                                int playerNumber = 1;
                                                for (int c = 0; c < Combination.length; c++) {
                                                    for (int j = 0; j < Combination[c]; j++) {
                                                        players[playerNumber] = getStrategy(c, game);
                                                        playerNumber++;
                                                    }
                                                }
                                                game.init(players, biz);
                                                game.turn(TURNS_COUNT);
                                                int[] finalScores = game.getScores();
                                                if (psInsert != null) {
                                                    psInsert.clearParameters();
                                                }
                                                psInsert = conn.prepareStatement("INSERT INTO results " + "   (env_id, combination_id, str_id, score, result_id) " + "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                                                psInsert.setInt(1, insertedEnvId);
                                                psInsert.setInt(2, combinationIdx);
                                                psInsert.setInt(3, strategyIdx);
                                                psInsert.setInt(4, finalScores[0]);
                                                psInsert.setInt(5, resultIdx);
                                                psInsert.executeUpdate();
                                                resultIdx++;
                                            }
                                            for (int cellIdx = 0; cellIdx < Combination.length; cellIdx++) {
                                                if (psInsert != null) {
                                                    psInsert.clearParameters();
                                                }
                                                psInsert = conn.prepareStatement("INSERT INTO combinations " + "   (env_id, str_id, str_quantity, combination_id) " + "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                                                psInsert.setInt(1, insertedEnvId);
                                                psInsert.setInt(2, cellIdx);
                                                psInsert.setInt(3, Combination[cellIdx]);
                                                psInsert.setInt(4, combinationIdx);
                                                psInsert.executeUpdate();
                                            }
                                            t = System.currentTimeMillis();
                                            estimate = (24310 - combinationIdx) * ((t - t0) / (combinationIdx + 1));
                                            estimate = (estimate / 1000) / 60;
                                            System.out.println(combinationIdx + ": [" + comb2string(Combination) + "]" + " Estimate:" + estimate + " min");
                                            combinationIdx++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static Person getStrategy(int i, AbstractGame game) {
        switch(i) {
            case 0:
                return new Egoist();
            case 1:
                return new Altruist();
            case 2:
                return new Stupid();
            case 3:
                return new TitForTat();
            case 4:
                return new Trigger();
            case 5:
                return new CrazyTrigger();
            case 6:
                return new AntiTitForTat();
            case 7:
                return new WaitForGood();
            case 8:
                return new FTrigger(100);
            case 9:
                return new Jealous(game);
            default:
                return new Stupid();
        }
    }
}
