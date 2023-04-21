package org.chessworks.chess.model;

import java.util.Date;

public class LiveGame {

    public synchronized int getBoardNumber() {
        return assignedBoard;
    }

    public synchronized void setBoardNumber(int assignedBoard) {
        this.assignedBoard = assignedBoard;
    }

    public synchronized User getBlackPlayer() {
        return blackPlayer;
    }

    public synchronized void setBlackPlayer(User blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public synchronized Date getStartTime() {
        return startTime;
    }

    public synchronized void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public synchronized User getWhitePlayer() {
        return whitePlayer;
    }

    public synchronized void setWhitePlayer(User whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    private int assignedBoard;

    private User blackPlayer;

    private Date startTime;

    private User whitePlayer;
}
