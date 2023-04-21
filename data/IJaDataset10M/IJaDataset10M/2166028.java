package org.aquastarz.score.report;

import java.math.BigDecimal;

public class FiguresLabel implements Comparable {

    String levelName;

    int levelOrder;

    String name;

    int place;

    String team;

    BigDecimal score;

    public FiguresLabel(String levelName, int levelOrder, String name, int place, String team, BigDecimal score) {
        this.levelName = levelName;
        this.levelOrder = levelOrder;
        this.name = name;
        this.place = place;
        this.team = team;
        this.score = score;
    }

    public int getLevelOrder() {
        return levelOrder;
    }

    public void setLevelOrder(int levelOrder) {
        this.levelOrder = levelOrder;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String toString() {
        return levelOrder + "-" + place;
    }

    public int compareTo(Object o) {
        FiguresLabel fl = (FiguresLabel) o;
        int comp = levelOrder - fl.levelOrder;
        if (comp != 0) {
            return comp;
        }
        return place - fl.place;
    }
}
