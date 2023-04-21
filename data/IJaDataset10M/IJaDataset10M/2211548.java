package com.qtc.badminton.entity;

import java.util.Date;

/**
 * @author: cong.wang
 * @version: 1.0, 2010/4/21
 * @modify:
 * @description: This is the <code>WinningRate</code> entity.
 */
public class WinningRate implements Comparable<WinningRate> {

    private Date rankDate;

    private Integer rank;

    private String name;

    private String id;

    private Integer totalMatch;

    private Integer totalWin;

    private Integer totalLost;

    private Float totalWinningRate;

    private String updateId;

    private Date updateDate;

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTotalMatch() {
        return totalMatch;
    }

    public void setTotalMatch(Integer totalMatch) {
        this.totalMatch = totalMatch;
    }

    public Integer getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(Integer totalWin) {
        this.totalWin = totalWin;
    }

    public Integer getTotalLost() {
        return totalLost;
    }

    public void setTotalLost(Integer totalLost) {
        this.totalLost = totalLost;
    }

    public Float getTotalWinningRate() {
        return totalWinningRate;
    }

    public void setTotalWinningRate(Float totalWinningRate) {
        this.totalWinningRate = totalWinningRate;
    }

    public Date getRankDate() {
        return rankDate;
    }

    public void setRankDate(Date rankDate) {
        this.rankDate = rankDate;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int compareTo(WinningRate another) {
        if (this.totalWinningRate < another.totalWinningRate) {
            return 1;
        } else if (this.totalWinningRate.equals(another.totalWinningRate)) {
            return 0;
        } else {
            return -1;
        }
    }
}
