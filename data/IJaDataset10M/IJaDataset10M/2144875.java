package net.mlw.fball.loader.yahoo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import net.mlw.fball.bo.Player;
import net.mlw.fball.bo.SeasonStats;
import net.mlw.fball.event.ProgressEvent;
import net.mlw.fball.event.StatusEvent;
import net.mlw.fball.loader.LoaderContextHolder;
import net.mlw.util.NetUtils;
import net.mlw.util.ParsingUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.8 $ $Date: 2004/06/04 15:37:48 $
 */
public class SeasonStatsLoader extends LoaderContextHolder {

    /** Commons Logger. **/
    private static final Log LOGGER = LogFactory.getLog(PlayerLoader.class);

    /**
    * @see net.mlw.fball.loader.Loader#doLoad()
    */
    public void doLoad() throws Exception {
        List players = playerDao.findByMap(null);
        int count = 0;
        int totalCount = players.size();
        eventListener.onEvent(new ProgressEvent(totalCount));
        for (Iterator iter = players.iterator(); iter.hasNext(); ) {
            try {
                Player player = (Player) iter.next();
                eventListener.onEvent(new StatusEvent(player.toString()));
                String yahooPlayerId = player.getProvider("yahoo");
                File file = new File("/stats/players/" + yahooPlayerId);
                file.mkdirs();
                file = new File("/stats/players/" + yahooPlayerId + "/career");
                NetUtils.copyFile(new URL(location.replaceAll(":yahooPlayerId", yahooPlayerId)), file);
                LineNumberReader reader = new LineNumberReader(new FileReader(file));
                String currentPosition = null;
                while (reader.ready() && currentPosition == null) {
                    String line = reader.readLine();
                    currentPosition = getPosition(line);
                }
                if ("TE".equals(currentPosition)) {
                    while (reader.ready()) {
                        String line = reader.readLine();
                        if (line.indexOf("<td class=\"yspscores\" align=\"left\">") >= 0) {
                            String[] values = ParsingUtils.getTdContentsAsArray(line);
                            SeasonStats stats = seasonStatsDao.findById(player, new Integer(values[0]));
                            stats.setPosition("TE");
                            stats.setTeam(teamDao.findByName("%" + values[1] + "%"));
                            stats.setGamesPlayed(new Integer(values[2]));
                            stats.setReceivingRecptions(new Integer(values[4]));
                            stats.setReceivingYards(new Integer(values[5]));
                            stats.setReceivingLong(new Integer(values[8]));
                            stats.setReceivingFirstdowns(new Integer(values[10]));
                            stats.setReceivingTouchdown(new Integer(values[11]));
                            stats.setRushingAttempts(new Integer(values[13]));
                            stats.setRushingYards(new Integer(values[14]));
                            stats.setRushingTouchdowns(new Integer(values[17]));
                            stats.setFumbles(new Integer(values[19]));
                            stats.setFunblesLost(new Integer(values[20]));
                            seasonStatsDao.save(stats);
                        }
                    }
                }
                if ("RB".equals(currentPosition)) {
                    while (reader.ready()) {
                        String line = reader.readLine();
                        if (line.indexOf("<td class=\"yspscores\" align=\"left\">") >= 0) {
                            String[] values = ParsingUtils.getTdContentsAsArray(line);
                            SeasonStats stats = seasonStatsDao.findById(player, new Integer(values[0]));
                            stats.setPosition("RB");
                            stats.setTeam(teamDao.findByName("%" + values[1] + "%"));
                            stats.setGamesPlayed(new Integer(values[2]));
                            stats.setRushingAttempts(new Integer(values[4]));
                            stats.setRushingYards(new Integer(values[5]));
                            stats.setRushingTouchdowns(new Integer(values[8]));
                            stats.setReceivingRecptions(new Integer(values[10]));
                            stats.setReceivingYards(new Integer(values[11]));
                            stats.setReceivingLong(new Integer(values[14]));
                            stats.setReceivingFirstdowns(new Integer(values[16]));
                            stats.setReceivingTouchdown(new Integer(values[17]));
                            stats.setFumbles(new Integer(values[19]));
                            stats.setFunblesLost(new Integer(values[20]));
                            seasonStatsDao.save(stats);
                        }
                    }
                } else if ("QB".equals(currentPosition)) {
                    while (reader.ready()) {
                        String line = reader.readLine();
                        if (line.indexOf("<td class=\"yspscores\" align=\"left\">") >= 0) {
                            String[] values = ParsingUtils.getTdContentsAsArray(line);
                            SeasonStats stats = seasonStatsDao.findById(player, new Integer(values[0]));
                            stats.setPosition("QB");
                            stats.setTeam(teamDao.findByName("%" + values[1] + "%"));
                            stats.setGamesPlayed(new Integer(values[2]));
                            stats.setQuarterBackRating(new Double(values[4]));
                            stats.setPassingCompletion(new Integer(values[5]));
                            stats.setPassingAttempts(new Integer(values[6]));
                            stats.setPassingYards(new Integer(values[8]));
                            stats.setPassingTouchdowns(new Integer(values[11]));
                            stats.setPassingInterception(new Integer(values[12]));
                            stats.setRushingAttempts(new Integer(values[14]));
                            stats.setRushingYards(new Integer(values[15]));
                            stats.setRushingTouchdowns(new Integer(values[18]));
                            stats.setPassingSacked(new Integer(values[20]));
                            stats.setPassingSackedYards(new Integer(values[21]));
                            stats.setFumbles(new Integer(values[23]));
                            stats.setFunblesLost(new Integer(values[24]));
                            seasonStatsDao.save(stats);
                        }
                    }
                } else if ("WR".equals(currentPosition)) {
                    while (reader.ready()) {
                        String line = reader.readLine();
                        if (line.indexOf("<td class=\"yspscores\" align=\"left\">") >= 0) {
                            String[] values = ParsingUtils.getTdContentsAsArray(line);
                            SeasonStats stats = seasonStatsDao.findById(player, new Integer(values[0]));
                            stats.setPosition("WR");
                            stats.setTeam(teamDao.findByName("%" + values[1] + "%"));
                            stats.setGamesPlayed(new Integer(values[2]));
                            stats.setReceivingRecptions(new Integer(values[4]));
                            stats.setReceivingYards(new Integer(values[5]));
                            stats.setReceivingLong(new Integer(values[8]));
                            stats.setReceivingFirstdowns(new Integer(values[10]));
                            stats.setReceivingTouchdown(new Integer(values[11]));
                            stats.setKickReturnReturns(new Integer(values[13]));
                            stats.setKickReturnYards(new Integer(values[14]));
                            stats.setKickReturnLong(new Integer(values[16]));
                            stats.setPuntReturnReturns(new Integer(values[18]));
                            stats.setPuntReturnYards(new Integer(values[19]));
                            stats.setPuntReturnLong(new Integer(values[21]));
                            stats.setFumbles(new Integer(values[23]));
                            stats.setFunblesLost(new Integer(values[24]));
                            seasonStatsDao.save(stats);
                        }
                    }
                } else if ("K".equals(currentPosition)) {
                    while (reader.ready()) {
                        String line = reader.readLine();
                        if (line.indexOf("<td class=\"yspscores\" align=\"left\">") >= 0) {
                            String[] values = ParsingUtils.getTdContentsAsArray(line);
                            SeasonStats stats = seasonStatsDao.findById(player, new Integer(values[0]));
                            stats.setPosition("K");
                            stats.setTeam(teamDao.findByName("%" + values[1] + "%"));
                            stats.setGamesPlayed(new Integer(values[2]));
                            stats.setFieldGoalRange0To19(new Integer(values[4]));
                            stats.setFieldGoalRange20To29(new Integer(values[5]));
                            stats.setFieldGoalRange30To39(new Integer(values[6]));
                            stats.setFieldGoalRange40To49(new Integer(values[7]));
                            stats.setFieldGoalRange50Plus(new Integer(values[8]));
                            stats.setFieldGoalMade(new Integer(values[9]));
                            stats.setFieldGoalAttempt(new Integer(values[10]));
                            stats.setFieldGoalLong(new Integer(values[12]));
                            stats.setExtraPointAttempt(new Integer(values[14]));
                            stats.setExtraPointMade(new Integer(values[15]));
                            seasonStatsDao.save(stats);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            eventListener.onEvent(new ProgressEvent(totalCount, ++count));
        }
    }

    private String getPosition(String line) {
        if (line.indexOf("Linebacker") > 0) {
            return "";
        } else if (line.indexOf("Wide Receiver") > 0) {
            return "WR";
        } else if (line.indexOf("Cornerback") > 0) {
            return "";
        } else if (line.indexOf("Center") > 0) {
            return "";
        } else if (line.indexOf("Running back") > 0) {
            return "RB";
        } else if (line.indexOf("Defensive tackle") > 0) {
            return "";
        } else if (line.indexOf("Cornerback") > 0) {
            return "";
        } else if (line.indexOf("Quarterback") > 0) {
            return "QB";
        } else if (line.indexOf("Tight end ") > 0) {
            return "TE";
        } else if (line.indexOf("Kicker") > 0) {
            return "K";
        } else {
            return null;
        }
    }
}
