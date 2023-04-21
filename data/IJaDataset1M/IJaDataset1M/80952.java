package yore.boardgames.c4;

import yore.ai.*;
import java.util.*;
import java.io.*;

public class C4Game extends Game {

    public static void main(String[] args) {
        Map cache = null;
        try {
            FileInputStream istream = new FileInputStream("/home/mcmillen/dld/c4-players");
            ObjectInputStream p = new ObjectInputStream(istream);
            cache = (Map) p.readObject();
            istream.close();
        } catch (Exception e) {
            System.out.println("Could not load data for LearningPlayer.\n");
        }
        if (cache == null) cache = new HashMap();
        cache = Collections.synchronizedMap(cache);
        p1 = new LearningPlayer(new C4Heuristic2(), 6, true, 0.05, 0.025);
        p1.setName("Learning Player 1");
        p1.setSymbol('X');
        ((LearningPlayer) p1).setCache(cache);
        p2 = new LearningPlayer(new C4Heuristic2(), 6, true, 0.1, 0.05);
        p2.setName("Learning Player 2");
        p2.setSymbol('O');
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        int gameNo = 0;
        while (true) {
            gameNo++;
            LinkedList players;
            players = new LinkedList();
            players.add(p1);
            players.add(p2);
            System.out.println("Game #" + gameNo);
            new C4Game(players, true).start();
        }
    }

    private static Player p1, p2;

    private static Thread shutdownHook = new Thread() {

        public void run() {
            System.out.println("Running shutdown hook...");
            p1.printRecord();
            p2.printRecord();
            synchronized (((AlphaBetaPlayer) p1).getCache()) {
                try {
                    FileOutputStream ostream = new FileOutputStream("/home/mcmillen/dld/c4-players");
                    ObjectOutputStream p = new ObjectOutputStream(ostream);
                    Map cache = ((AlphaBetaPlayer) p1).getCache();
                    p.writeObject(cache);
                    p.flush();
                    ostream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Shutdown hook finished.");
        }
    };

    public C4Game(LinkedList players) {
        super(players);
    }

    public C4Game(LinkedList players, boolean printEachMove) {
        super(players, printEachMove);
    }

    public GameState getInitialState(LinkedList players) {
        return new C4GameState(players);
    }

    public String getMoveDescription(Player mover, Object move, GameState gameState) {
        Integer i = (Integer) move;
        return (mover.getName() + " placed an " + mover.getSymbol() + " in column " + (i.intValue() + 1) + ".");
    }
}
