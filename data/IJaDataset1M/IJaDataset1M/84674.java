package com.jeremygross;

import java.util.ArrayList;
import java.util.Iterator;

public class Game {

    private ArrayList<Player> players;

    private Stack stack;

    private Board board;

    private int nPlayers;

    public Game(int nPlayers) {
        super();
        this.nPlayers = nPlayers;
        this.players = new ArrayList<Player>();
        for (int i = 0; i < nPlayers; i++) {
            Player player = new Player("player" + i, i);
            players.add(player);
        }
        this.stack = new Stack();
        this.board = new Board();
    }

    public void init() {
        this.stack.initTiles();
    }

    public int getnPlayers() {
        return nPlayers;
    }

    private Stack getStack() {
        return stack;
    }

    private Board getBoard() {
        return board;
    }

    private void setStack(Stack stack) {
        this.stack = stack;
    }

    private void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<Player> run() {
        java.util.Random rand = new java.util.Random();
        int currentPlayerIdx = rand.nextInt(nPlayers);
        Iterator<Player> playersIter = players.iterator();
        while (playersIter.hasNext()) {
            Player player = playersIter.next();
            player.initTiles(this.getStack());
        }
        return this.run(currentPlayerIdx);
    }

    public ArrayList<Player> run(int currentPlayerIdx) {
        int firstPlayerNoPlayIdx = -1;
        Player winningPlayer = null;
        while (winningPlayer == null && (firstPlayerNoPlayIdx == -1 || firstPlayerNoPlayIdx != currentPlayerIdx)) {
            Player player = players.get(currentPlayerIdx);
            Tile playedTile = null;
            while (playedTile == null && stack.getNbTiles() > 0) {
                playedTile = player.playATile(this.getBoard());
                if (playedTile == null) {
                    player.getTileFromStack(this.stack);
                }
            }
            if (playedTile != null) {
                firstPlayerNoPlayIdx = -1;
            } else if (firstPlayerNoPlayIdx == -1) {
                firstPlayerNoPlayIdx = currentPlayerIdx;
            }
            currentPlayerIdx++;
            if (currentPlayerIdx >= nPlayers) {
                currentPlayerIdx = 0;
            }
            winningPlayer = this.winningPlayer();
        }
        if (Main.print()) {
            if (winningPlayer != null) {
                System.out.println("==================");
                System.out.println("No one can play");
            }
            System.out.println("==================");
            System.out.println("Players tiles score :");
        }
        int minScore = Integer.MAX_VALUE;
        Iterator<Player> playersIter = players.iterator();
        while (playersIter.hasNext()) {
            Player player = playersIter.next();
            int score = player.getScore();
            minScore = score < minScore ? score : minScore;
            if (Main.print()) System.out.println(player.getName() + " : " + score);
        }
        if (Main.print()) {
            System.out.println("==================");
            System.out.println("Winner players :");
        }
        ArrayList<Player> winningPlayers = new ArrayList<Player>();
        playersIter = players.iterator();
        while (playersIter.hasNext()) {
            Player player = playersIter.next();
            int score = player.getScore();
            if (score == minScore) {
                winningPlayers.add(player);
                if (Main.print()) System.out.println(player.getName());
            }
        }
        return winningPlayers;
    }

    private Player winningPlayer() {
        Iterator<Player> iterator = players.iterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            if (player.getNbTiles() == 0) {
                return player;
            }
        }
        return null;
    }
}
