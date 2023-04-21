package com.botarena.server.model;

import java.util.Date;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Bot extends Entity {

    private static final int DEFAULT_RANK = 1000;

    @Persistent
    private String name;

    @Persistent
    private Text sourceCode;

    @Persistent
    private String compilationResults;

    @Persistent
    private Blob binary;

    @Persistent
    private Key author;

    @Persistent
    private String parameters;

    @Persistent
    private Key contest;

    @Persistent
    private boolean contestant;

    @Persistent
    private int wins;

    @Persistent
    private int losses;

    @Persistent
    private int draws;

    @Persistent
    private int rank;

    @Persistent
    private Date lastFight;

    public Bot() {
        wins = 0;
        losses = 0;
        draws = 0;
        rank = DEFAULT_RANK;
        lastFight = new Date();
    }

    public Bot(String name, String sourceCode, String parameters, boolean contestant) {
        this();
        this.name = name;
        this.sourceCode = new Text(sourceCode);
        this.contestant = contestant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Key getAuthor() {
        return author;
    }

    public void setAuthor(Key author) {
        this.author = author;
    }

    public String getSourceCode() {
        return sourceCode.getValue();
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = new Text(sourceCode);
    }

    public String getCompilationResults() {
        return compilationResults;
    }

    public void setCompilationResults(String compilationResults) {
        this.compilationResults = compilationResults;
    }

    public Blob getBinary() {
        return binary;
    }

    public void setBinary(Blob binary) {
        this.binary = binary;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public boolean isContestant() {
        return contestant;
    }

    public void setContestant(boolean contestant) {
        this.contestant = contestant;
    }

    public Key getContest() {
        return contest;
    }

    public void setContest(Key contest) {
        this.contest = contest;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setLastFight(Date lastFight) {
        this.lastFight = lastFight;
    }

    public Date getLastFight() {
        return lastFight;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }
}
