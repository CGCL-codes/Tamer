package logs;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import mysql.Request;
import server.*;

public class LogAnalyseCOD extends Thread {

    public int portSocket;

    Statement requeteA, requeteB;

    int MULTIPLE_PACKETS_TIMEOUT = 300000;

    int taille = 100024;

    byte buffer[] = new byte[taille];

    int ServerId;

    int MatchId, status;

    int scoreTeamA, scoreTeamB;

    String TeamA, TeamB, map;

    boolean FFA, ModeWarmup, ModeMatch;

    boolean ReadyTeamA, RestartTeamA;

    boolean ReadyTeamB, RestartTeamB;

    boolean sideone, sidetwo;

    boolean savingScoresEndRound, savingScoresDuringRound;

    boolean firstScore;

    boolean toRun;

    Server MonServeur;

    boolean toSleep;

    Request marequeteA, marequeteB;

    public LogAnalyseCOD(int portSocket, Server MonServeur) {
        this.portSocket = portSocket;
        this.MonServeur = MonServeur;
        this.ServerId = MonServeur.getServerId();
        this.toRun = true;
        this.marequeteA = new Request();
        this.marequeteB = new Request();
        System.out.println("construction d'un analyseur de logs DODS");
    }

    public void setToRun(boolean toRun) {
        this.toRun = toRun;
    }

    public String parseResponse(byte[] buf) {
        String retVal = "";
        if (buf[0] != -1 || buf[1] != -1 || buf[2] != -1 || buf[3] != -1) {
            retVal = "ERROR";
        } else {
            int off = 5;
            StringBuffer challenge = new StringBuffer(20);
            while (buf[off] != 0) {
                challenge.append((char) (buf[off++] & 255));
            }
            retVal = challenge.toString();
        }
        return retVal;
    }

    public void displayMatchStatus() {
        System.out.println("Serveur :" + this.ServerId + " MatchId :" + this.MatchId + "");
        if (this.FFA) {
            System.out.println("Serveur en FFA");
        }
        if ((this.ModeWarmup) || (this.ModeMatch)) {
            if (this.ModeWarmup) {
                System.out.println("Serveur en Warmup");
            } else {
                System.out.println("Serveur en Match");
            }
            if (this.sideone) {
                System.out.println("Side One");
            }
            if (this.sidetwo) {
                System.out.println("Side Two");
            }
            System.out.println("TeamA :" + this.TeamA + " TeamB :" + this.TeamB + "");
        }
    }

    public void run() {
        try {
            System.out.println("lancement LogAnalyse COD4 " + portSocket);
            this.MonServeur.Command("say ServeurCOD4 pris en compte par le bot");
            this.FFA = true;
            this.ModeWarmup = false;
            this.ModeMatch = false;
            this.ReadyTeamA = false;
            this.ReadyTeamB = false;
            this.RestartTeamA = false;
            this.RestartTeamB = false;
            this.firstScore = false;
            this.savingScoresEndRound = false;
            this.savingScoresDuringRound = false;
            this.sideone = false;
            this.sidetwo = false;
            this.scoreTeamB = 0;
            this.scoreTeamA = 0;
            this.status = 0;
            this.MatchId = 0;
            this.map = "";
            this.marequeteA.setRequest("SELECT MatchId, Player1Name, Player2Name,Player1Ready,Player2Ready, Action, Status,Map,Player1ScoreSet1,Player2ScoreSet1,Player1ScoreSet2,Player2ScoreSet2 " + "FROM matches WHERE ServerId=" + this.MonServeur.getServerId() + " AND (Status>0 AND Status<5)");
            this.marequeteA.sendRequest();
            if (this.marequeteA.rs.next()) {
                this.FFA = false;
                this.MatchId = this.marequeteA.rs.getInt(1);
                this.TeamA = this.marequeteA.rs.getString(2);
                this.TeamB = this.marequeteA.rs.getString(3);
                this.status = this.marequeteA.rs.getInt(7);
                this.map = this.marequeteA.rs.getString(8);
                this.MonServeur.Command("hostname \"" + this.TeamA + " Vs " + this.TeamB + "\"");
                if ((this.status == 1) || (this.status == 3)) {
                    this.ModeWarmup = true;
                    if (this.marequeteA.rs.getInt(4) == 1) {
                        this.ReadyTeamA = true;
                    }
                    if (this.marequeteA.rs.getInt(5) == 1) {
                        this.ReadyTeamB = true;
                    }
                    if (this.status == 1) {
                        this.sideone = true;
                    } else {
                        this.sidetwo = true;
                    }
                }
                if ((this.status == 2) || (this.status == 4)) {
                    this.ModeMatch = true;
                    if (this.status == 2) {
                        this.scoreTeamA = this.marequeteA.rs.getInt(9);
                        this.scoreTeamB = this.marequeteA.rs.getInt(10);
                        this.sideone = true;
                    } else {
                        this.scoreTeamA = this.marequeteA.rs.getInt(11);
                        this.scoreTeamB = this.marequeteA.rs.getInt(12);
                        this.sidetwo = true;
                    }
                }
            }
            int MULTIPLE_PACKETS_TIMEOUT = 300000;
            int taille = 100024;
            byte buffer[] = new byte[taille];
            this.setName("" + portSocket);
            DatagramSocket socket = new DatagramSocket(portSocket);
            socket.setSoTimeout(MULTIPLE_PACKETS_TIMEOUT);
            DatagramPacket data = new DatagramPacket(buffer, buffer.length);
            while (toRun == true) {
                String reponse = "";
                if ((this.ModeMatch) || (this.ModeWarmup)) {
                    socket.receive(data);
                    reponse = parseResponse(data.getData());
                    System.out.println(this.MonServeur.getServerIP() + ":" + this.MonServeur.getServerPort() + " " + reponse + " " + this.getName());
                }
                if (this.FFA) {
                    this.marequeteA.setRequest("SELECT MatchId,Player1Name,Player2Name,Map FROM matches WHERE ServerId=" + this.MonServeur.getServerId() + " AND Action=1");
                    this.marequeteA.sendRequest();
                    while (this.marequeteA.rs.next()) {
                        this.MatchId = this.marequeteA.rs.getInt(1);
                        this.TeamA = this.marequeteA.rs.getString(2);
                        this.TeamB = this.marequeteA.rs.getString(3);
                        this.map = this.marequeteA.rs.getString(4);
                        System.out.println(this.TeamA + " VS " + this.TeamB);
                        this.MonServeur.ChangeMap(this.map);
                        this.MonServeur.ModeWarmup();
                        this.MonServeur.Command("hostname \"" + this.TeamA + " Vs " + this.TeamB + "\"");
                        this.marequeteB.setRequest("UPDATE matches SET Action=0,Status=1,Player1Ready=0,Player2Ready=0,Player1ScoreSet1=0,Player2ScoreSet1=0,Player1ScoreSet2=0,Player2ScoreSet2=0 WHERE MatchId=" + this.MatchId + "");
                        this.marequeteB.sendRequest();
                        this.status = 1;
                        this.displayMatchStatus();
                        this.FFA = false;
                        this.ModeWarmup = true;
                        this.sideone = true;
                    }
                }
                if (this.ModeWarmup) {
                    if ((reponse.indexOf("Allies") > 0) && (reponse.indexOf("Ready") > 0)) {
                        if (this.status == 1) {
                            System.out.println("L1");
                            this.marequeteA.setRequest("UPDATE matches SET Player1Ready=1 " + "WHERE MatchID=" + this.MatchId + "");
                            this.marequeteA.sendRequest();
                            this.MonServeur.Command("say " + this.TeamA + " Ready");
                            this.ReadyTeamA = true;
                        } else if (this.status == 3) {
                            System.out.println("L3");
                            this.marequeteA.setRequest("UPDATE matches SET Player2Ready=1 " + "WHERE MatchID=" + this.MatchId + "");
                            this.marequeteA.sendRequest();
                            this.MonServeur.Command("say " + this.TeamB + " Ready");
                            this.ReadyTeamB = true;
                        }
                    }
                    if ((reponse.indexOf("Axis") > 0) && (reponse.indexOf("Ready") > 0)) {
                        if (this.status == 1) {
                            System.out.println("X1");
                            this.marequeteA.setRequest("UPDATE matches SET Player2Ready=1 " + "WHERE MatchID=" + this.MatchId + "");
                            this.marequeteA.sendRequest();
                            this.MonServeur.Command("say " + this.TeamB + " Ready");
                            this.ReadyTeamB = true;
                        } else if (this.status == 3) {
                            System.out.println("X3");
                            this.marequeteA.setRequest("UPDATE matches SET Player1Ready=1 " + "WHERE MatchID=" + this.MatchId + "");
                            this.marequeteA.sendRequest();
                            this.MonServeur.Command("say " + this.TeamA + " Ready");
                            this.ReadyTeamA = true;
                        }
                    }
                    if ((reponse.indexOf("Warmup") > 0) && (reponse.indexOf("Ends") > 0)) {
                        this.MonServeur.HFGL();
                        if ((this.status == 1) || (this.status == 3)) {
                            this.ReadyTeamB = false;
                            this.ReadyTeamA = false;
                            this.ModeWarmup = false;
                            this.ModeMatch = true;
                            if (this.status == 1) {
                                this.sideone = true;
                                this.status = 2;
                                System.out.println("Le side 1 entre " + this.TeamA + " et " + this.TeamB + " sur " + this.map + " vient de commencer");
                                this.MonServeur.Command("tv_record maxlan2008_" + this.TeamA + "-" + this.TeamB + "-" + this.map + "-side1-" + new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date()));
                                this.marequeteA.setRequest("UPDATE matches SET Player1Ready=0,Player2Ready=0,Status=2 " + "WHERE MatchID=" + this.MatchId + "");
                                this.marequeteA.sendRequest();
                            } else {
                                this.sideone = false;
                                this.sidetwo = true;
                                this.status = 4;
                                System.out.println("Le side 2 entre " + this.TeamA + " et " + this.TeamB + " sur " + this.map + " vient de commencer");
                                this.MonServeur.Command("tv_record maxlan2008_" + this.TeamA + "-" + this.TeamB + "-" + this.map + "-side2-" + new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date()));
                                this.marequeteA.setRequest("UPDATE matches SET Player1Ready=0,Player2Ready=0,Status=4 " + "WHERE MatchID=" + this.MatchId + "");
                                this.marequeteA.sendRequest();
                            }
                        }
                    }
                }
                if (this.ModeMatch) {
                    if ((!this.savingScoresDuringRound) && (reponse.indexOf("round") > 0) && (reponse.indexOf("win") > 0) && (!this.savingScoresEndRound)) {
                        this.savingScoresDuringRound = true;
                        this.firstScore = true;
                    }
                    if ((this.savingScoresDuringRound) && (reponse.indexOf("team") > 0) && (reponse.indexOf("scores") > 0) && (!this.savingScoresEndRound)) {
                        if (this.firstScore) {
                            String score = reponse.substring(((reponse.indexOf("roundswon")) + 11), reponse.length());
                            this.scoreTeamA = Integer.parseInt(score.substring(0, score.indexOf("\"")));
                            this.MonServeur.Command("say Score Allies : " + this.scoreTeamA);
                            this.firstScore = false;
                        } else {
                            String score = reponse.substring(((reponse.indexOf("roundswon")) + 11), reponse.length());
                            this.scoreTeamB = Integer.parseInt(score.substring(0, score.indexOf("\"")));
                            this.MonServeur.Command("say Score Axis   : " + this.scoreTeamB);
                            this.firstScore = true;
                            if (this.sideone) {
                                this.marequeteA.setRequest("UPDATE matches SET Player1ScoreSet1=" + this.scoreTeamA + ",Player2ScoreSet1=" + this.scoreTeamB + " " + "WHERE MatchID=" + this.MatchId + "");
                                this.marequeteA.sendRequest();
                                Thread.sleep(1000);
                            } else if (this.sidetwo) {
                                this.marequeteA.setRequest("UPDATE matches SET Player1ScoreSet2=" + this.scoreTeamB + ",Player2ScoreSet2=" + this.scoreTeamA + " " + "WHERE MatchID=" + this.MatchId + "");
                                this.marequeteA.sendRequest();
                                Thread.sleep(1000);
                            }
                            this.savingScoresDuringRound = false;
                        }
                    }
                    if ((!this.savingScoresEndRound) && (reponse.indexOf("Game") > 0) && (reponse.indexOf("Over") > 0) && (!this.savingScoresDuringRound)) {
                        this.savingScoresEndRound = true;
                        this.firstScore = true;
                    }
                    if ((this.savingScoresEndRound) && (reponse.indexOf("team") > 0) && (reponse.indexOf("scores") > 0) && (!this.savingScoresDuringRound)) {
                        if (this.firstScore) {
                            String score = reponse.substring(((reponse.indexOf("roundswon")) + 11), reponse.length());
                            this.scoreTeamA = Integer.parseInt(score.substring(0, score.indexOf("\"")));
                            this.MonServeur.Command("say Round Score Allies : " + this.scoreTeamA);
                            this.firstScore = false;
                        } else {
                            String score = reponse.substring(((reponse.indexOf("roundswon")) + 11), reponse.length());
                            this.scoreTeamB = Integer.parseInt(score.substring(0, score.indexOf("\"")));
                            this.MonServeur.Command("say Round Score Axis : " + this.scoreTeamB);
                            this.firstScore = true;
                            this.ModeMatch = false;
                            if (this.sideone) {
                                this.ModeWarmup = true;
                                this.sideone = false;
                                this.sidetwo = true;
                                this.status = 3;
                                this.marequeteA.setRequest("UPDATE matches SET Player1ScoreSet1=" + this.scoreTeamA + ",Player2ScoreSet1=" + this.scoreTeamB + ",Status=3 " + "WHERE MatchID=" + this.MatchId + "");
                                this.marequeteA.sendRequest();
                                Thread.sleep(1000);
                                System.out.println("say Side One Over");
                                this.MonServeur.Command("say Side One Over");
                                System.out.println("say changemap");
                                this.MonServeur.ChangeMap(this.map);
                                System.out.println("warmup");
                                this.MonServeur.ModeWarmup();
                                this.MonServeur.Command("hostname \"" + this.TeamA + " Vs " + this.TeamB + "\"");
                            } else if (this.sidetwo) {
                                this.FFA = true;
                                this.sideone = false;
                                this.sidetwo = false;
                                this.status = 5;
                                this.marequeteA.setRequest("UPDATE matches SET Player1ScoreSet2=" + this.scoreTeamB + ",Player2ScoreSet2=" + this.scoreTeamA + ",Status=5 " + "WHERE MatchID=" + this.MatchId + "");
                                this.marequeteA.sendRequest();
                                Thread.sleep(1000);
                                this.MonServeur.Command("say Side Two Over");
                            }
                            this.savingScoresEndRound = false;
                        }
                    }
                }
            }
            System.out.println("bye bye birdie DODS");
        } catch (Exception e) {
        }
    }
}
