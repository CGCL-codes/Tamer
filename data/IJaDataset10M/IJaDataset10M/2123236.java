package lastfmbaseball.console;

import java.io.*;
import java.util.*;
import lastfmbaseball.core.*;

/**
 * @author DanBress
 * 
 */
public class ConsoleProgram {

    public static String BaseRunnersToString(List<BaseRunner> baseRunners) {
        String result = "";
        if (baseRunners.size() > 0) {
            for (int i = 0; i < baseRunners.size(); i++) {
                result += baseRunners.get(i).toString() + ",";
            }
        } else {
            result = "No one on.";
        }
        return result;
    }

    public static String boxScore(Game g) {
        final int teamPad = 30;
        final int inningPad = 4;
        String result = "";
        result += String.format("%-" + teamPad + "s", "Team Name");
        List<Inning> innings = g.getInnings();
        for (int i = 0; i < innings.size(); i++) {
            Inning inning = innings.get(i);
            result += String.format("%" + inningPad + "s", inning.getInningNumber());
        }
        result += String.format("%" + inningPad + "s", "R");
        result += String.format("%" + inningPad + "s", "H");
        result += String.format("%" + inningPad + "s", "LOB");
        result += "\r\n";
        result += formatHalfBoxScore(g, g.getTeam(HomeOrVisitor.Visitor), InningType.Top);
        result += formatHalfBoxScore(g, g.getTeam(HomeOrVisitor.Home), InningType.Bottom);
        return result;
    }

    public static TeamInGame createTeamInGame1() {
        Player dan = new Player("Dan", "Http://dan");
        Player lucas = new Player("Lucas", "Http://Lucas");
        Player dre = new Player("Dre", "Http://dry");
        Player jj = new Player("JJ", "http://JJ");
        Player lee = new Player("Lee", "http://lee");
        Player amber = new Player("Amber", "http://amber");
        List<Player> allBalls = new ArrayList<Player>();
        allBalls.add(dan);
        allBalls.add(lucas);
        allBalls.add(dre);
        allBalls.add(jj);
        allBalls.add(lee);
        allBalls.add(amber);
        Team tabledBalls = new Team("Tabled Balls", new ArrayList<Player>());
        PlayerInGame danIngame = new PlayerInGame(dan, PlayerInGameState.InGamePitching);
        PlayerInGame lucasIngame = new PlayerInGame(lucas, PlayerInGameState.InGameNotPitching);
        PlayerInGame dreIngame = new PlayerInGame(dre, PlayerInGameState.InGameNotPitching);
        PlayerInGame jjIngame = new PlayerInGame(jj, PlayerInGameState.InGameNotPitching);
        PlayerInGame leeIngame = new PlayerInGame(lee, PlayerInGameState.InGameNotPitching);
        PlayerInGame amberInGame = new PlayerInGame(amber, PlayerInGameState.Available);
        List<PlayerInGame> ballsIngame = new ArrayList<PlayerInGame>();
        List<PlayerInGame> lineup = new ArrayList<PlayerInGame>();
        ballsIngame.add(danIngame);
        ballsIngame.add(lucasIngame);
        ballsIngame.add(dreIngame);
        ballsIngame.add(jjIngame);
        ballsIngame.add(leeIngame);
        ballsIngame.add(amberInGame);
        lineup.add(lucasIngame);
        lineup.add(dreIngame);
        lineup.add(jjIngame);
        lineup.add(leeIngame);
        TeamInGame tabledBallsIngame = new TeamInGame(tabledBalls, ballsIngame, lineup);
        return tabledBallsIngame;
    }

    public static TeamInGame createTeamInGame2() {
        Player dan = new Player("Matt", "Http://dan");
        Player lucas = new Player("Cakes", "Http://Lucas");
        Player dre = new Player("Oliver", "Http://dry");
        Player jj = new Player("Fatman", "http://JJ");
        Player lee = new Player("Todd", "http://lee");
        List<Player> allBalls = new ArrayList<Player>();
        allBalls.add(dan);
        allBalls.add(lucas);
        allBalls.add(dre);
        allBalls.add(jj);
        allBalls.add(lee);
        Team tabledBalls = new Team("Crotch Adjustments", new ArrayList<Player>());
        PlayerInGame danIngame = new PlayerInGame(dan, PlayerInGameState.InGamePitching);
        PlayerInGame lucasIngame = new PlayerInGame(lucas, PlayerInGameState.InGameNotPitching);
        PlayerInGame dreIngame = new PlayerInGame(dre, PlayerInGameState.InGameNotPitching);
        PlayerInGame jjIngame = new PlayerInGame(jj, PlayerInGameState.InGameNotPitching);
        PlayerInGame leeIngame = new PlayerInGame(lee, PlayerInGameState.InGameNotPitching);
        List<PlayerInGame> ballsIngame = new ArrayList<PlayerInGame>();
        List<PlayerInGame> lineup = new ArrayList<PlayerInGame>();
        ballsIngame.add(danIngame);
        ballsIngame.add(lucasIngame);
        ballsIngame.add(dreIngame);
        ballsIngame.add(jjIngame);
        ballsIngame.add(leeIngame);
        lineup.add(lucasIngame);
        lineup.add(dreIngame);
        lineup.add(jjIngame);
        lineup.add(leeIngame);
        TeamInGame tabledBallsIngame = new TeamInGame(tabledBalls, ballsIngame, lineup);
        return tabledBallsIngame;
    }

    public static String formatHalfBoxScore(Game g, TeamInGame team, InningType inningType) {
        final int inningPad = 4;
        String result = "";
        List<Inning> innings = g.getInnings();
        result += String.format("%-" + 30 + "s", team.getTeam());
        int runsScored = 0;
        int hits = 0;
        int leftOnBase = 0;
        for (int i = 0; i < innings.size(); i++) {
            Inning inning = innings.get(i);
            HalfInning half = inning.getHalf(inningType);
            if (half != null) {
                result += String.format("%" + inningPad + "s", half.getRunsScored());
                runsScored += half.getRunsScored();
                hits += half.getHits();
                leftOnBase += half.getLeftOnBase();
            } else {
                result += String.format("%" + inningPad + "s", "X");
            }
        }
        result += String.format("%" + inningPad + "s", runsScored);
        result += String.format("%" + inningPad + "s", hits);
        result += String.format("%" + inningPad + "s", leftOnBase);
        result += "\r\n";
        return result;
    }

    public static void handleAtBat(Game game, HalfInning halfInning, int pitcherScore, int batterScore) {
        PlayerInGame pitcher = game.getPitcher(halfInning);
        PlayerInGame batter = game.getBatter(halfInning);
        Umpire umpire = null;
        List<BaseRunner> baseRunners = game.getBaseRunners(halfInning);
        System.out.println(game.getTeam(HomeOrVisitor.Visitor).getTeam() + " " + game.getScore(HomeOrVisitor.Visitor));
        System.out.println("at");
        System.out.println(game.getTeam(HomeOrVisitor.Home).getTeam() + " " + game.getScore(HomeOrVisitor.Home));
        System.out.println();
        System.out.println(halfInning);
        System.out.println(BaseRunnersToString(baseRunners));
        System.out.println(halfInning.getNumberOfOuts() + " out");
        System.out.println();
        System.out.println("Pitching: " + pitcher.getPlayer());
        System.out.println("Batting: " + batter.getPlayer());
        System.out.println("Pitcher score: " + pitcherScore);
        System.out.println("Batter score: " + batterScore);
        System.out.println();
        RulesEngine r = new RulesEngine();
        AtBat ab = r.handleBat(pitcher, batter, umpire, pitcherScore, batterScore, baseRunners);
        halfInning.addAtBat(ab);
        System.out.println(ab);
    }

    public static void main(String[] args) {
        TeamInGame balls = createTeamInGame1();
        TeamInGame adjustments = createTeamInGame2();
        System.out.println(balls);
        System.out.println();
        System.out.println(adjustments);
        Game g = new Game(balls, adjustments);
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader input = new BufferedReader(isr);
        boolean gameOver = false;
        while (!gameOver) {
            try {
                System.out.println();
                System.out.println("ph) pinch hitter");
                System.out.println("rp) relief pitcher");
                System.out.println("enter to handle at bat");
                String command = input.readLine();
                command = command.toUpperCase();
                if (command.equals("PH")) {
                    System.out.println("Team:");
                    List<TeamInGame> teamList = new ArrayList<TeamInGame>();
                    teamList.add(g.getTeam(HomeOrVisitor.Home));
                    teamList.add(g.getTeam(HomeOrVisitor.Visitor));
                    for (int i = 0; i < teamList.size(); i++) {
                        System.out.println(i + " " + teamList.get(i).getTeam());
                    }
                    int teamNumber = -1;
                    String inputString = input.readLine();
                    teamNumber = Integer.parseInt(inputString);
                    System.out.println("Pinch Hitter:");
                    List<PlayerInGame> pinchHitters = new ArrayList<PlayerInGame>();
                    TeamInGame team = teamList.get(teamNumber);
                    for (int i = 0; i < team.getAllPlayersInGame().size(); i++) {
                        PlayerInGame player = team.getAllPlayersInGame().get(i);
                        if (player.getState() == PlayerInGameState.Available) {
                            pinchHitters.add(player);
                        }
                    }
                    if (pinchHitters.size() > 0) {
                        for (int i = 0; i < pinchHitters.size(); i++) {
                            System.out.println(i + " " + pinchHitters.get(i));
                        }
                        inputString = input.readLine();
                        int pinchHitNumber = Integer.parseInt(inputString);
                        PlayerInGame pinchHitter = pinchHitters.get(pinchHitNumber);
                        System.out.println("Sub for:");
                        for (int i = 0; i < team.getBattingLineup().size(); i++) {
                            System.out.println(i + " " + team.getBattingLineup().get(i));
                        }
                        inputString = input.readLine();
                        int subNumber = Integer.parseInt(inputString);
                        PlayerInGame subOut = team.getBattingLineup().get(subNumber);
                        team.pinchHit(pinchHitter, subOut);
                        System.out.println("Pinch Hitting: " + pinchHitter + " for " + subOut);
                    } else {
                        System.out.println("No Pinch Hitters available");
                    }
                } else if (command.equals("RP")) {
                    System.out.println("Team:");
                    List<TeamInGame> teamList = new ArrayList<TeamInGame>();
                    teamList.add(g.getTeam(HomeOrVisitor.Home));
                    teamList.add(g.getTeam(HomeOrVisitor.Visitor));
                    for (int i = 0; i < teamList.size(); i++) {
                        System.out.println(i + " " + teamList.get(i).getTeam());
                    }
                    int teamNumber = -1;
                    String inputString = input.readLine();
                    teamNumber = Integer.parseInt(inputString);
                    TeamInGame team = teamList.get(teamNumber);
                    System.out.println("Relief Pitcher");
                    List<PlayerInGame> reliefPitchers = new ArrayList<PlayerInGame>();
                    for (int i = 0; i < team.getAllPlayersInGame().size(); i++) {
                        PlayerInGame player = team.getAllPlayersInGame().get(i);
                        if (player.getState() == PlayerInGameState.Available) {
                            reliefPitchers.add(player);
                        }
                    }
                    if (reliefPitchers.size() > 0) {
                        for (int i = 0; i < reliefPitchers.size(); i++) {
                            System.out.println(i + " " + reliefPitchers.get(i));
                        }
                        inputString = input.readLine();
                        int pitcherNumber = Integer.parseInt(inputString);
                        PlayerInGame pitcher = reliefPitchers.get(pitcherNumber);
                        team.changePitcher(pitcher);
                        System.out.println(pitcher + " in to pitch for: " + team);
                    } else {
                        System.out.println("No pitchers available");
                    }
                } else {
                    Inning inning = g.getCurrentInning();
                    HalfInning halfInning = null;
                    if (inning != null) {
                        halfInning = g.getCurrentHalfInning(inning);
                    }
                    if (halfInning != null) {
                        Random r = new Random();
                        int pitcherScore = r.nextInt(11);
                        int batterScore = r.nextInt(11);
                        handleAtBat(g, halfInning, pitcherScore, batterScore);
                    } else {
                        System.out.println();
                        gameOver = true;
                        System.out.println(boxScore(g));
                    }
                }
            } catch (IOException ex) {
                System.err.println("Caught an exception" + ex);
            }
        }
    }
}
