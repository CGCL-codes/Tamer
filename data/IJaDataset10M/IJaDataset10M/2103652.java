package com.botarena.shared;

import java.io.Serializable;

public class BotInfo implements Serializable {

    private static final long serialVersionUID = -1351745977458068757L;

    public static final String NOT_COMPILED_YET = "Not compiled yet";

    public static final String COMPILATION_ERRORS = "Compilation errors";

    public static final String COMPILED_SUCCESSFULLY = "Compiled successfully";

    private String key;

    private String name;

    private String sourceCode;

    private String compilationResults;

    private String parameters;

    private int points;

    private int wins;

    private int losses;

    private int draws;

    private boolean contestant;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getCompilationResults() {
        return compilationResults;
    }

    public void setCompilationResults(String compilationsResults) {
        this.compilationResults = compilationsResults;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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

    public boolean isContestant() {
        return contestant;
    }

    public void setContestant(boolean active) {
        this.contestant = active;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }
}
