package hottargui.net;

import java.net.*;
import java.rmi.*;
import hottargui.framework.*;

public abstract class GameInitializer {

    public static void bind(String color, Game localGame, GameRepository localRepository) throws MalformedURLException, RemoteException {
        PlayerColor pc = stringToColor(color);
        if (pc == PlayerColor.None) {
            throw new IllegalArgumentException("Only Red, green, blue and yellow allowed");
        }
        Naming.rebind("//localhost/" + color.toUpperCase() + "HotTarguiGame", localGame);
        Naming.rebind("//localhost/" + color.toUpperCase() + "HotTarguiGameRepository", localRepository);
    }

    public static void unbind(String color) throws RemoteException, MalformedURLException, NotBoundException {
        PlayerColor pc = stringToColor(color);
        if (pc == PlayerColor.None) {
            throw new IllegalArgumentException("Only Red, green, blue and yellow allowed");
        }
        Naming.unbind("//localhost/" + color.toUpperCase() + "HotTarguiGame");
        Naming.unbind("//localhost/" + color.toUpperCase() + "HotTarguiGameRepository");
    }

    public static Game connect(String color, Game localGame) throws MalformedURLException, RemoteException {
        PlayerColor pc = stringToColor(color);
        GameNetworkDecorator gs = null;
        Pair[] g = new Pair[3];
        int i = 0;
        if (pc != PlayerColor.Red) {
            g[i++] = getRemote("RED");
        }
        if (pc != PlayerColor.Green) {
            g[i++] = getRemote("GREEN");
        }
        if (pc != PlayerColor.Blue) {
            g[i++] = getRemote("BLUE");
        }
        if (pc != PlayerColor.Yellow) {
            g[i++] = getRemote("YELLOW");
        }
        gs = new GameNetworkDecorator(localGame);
        for (i = 0; i < 3; ++i) {
            gs.addGame(g[i].game, g[i].gameRepository);
        }
        return gs;
    }

    private static class Pair {

        public Pair(Game g, GameRepository gr) {
            game = g;
            gameRepository = gr;
        }

        public Game game;

        public GameRepository gameRepository;
    }

    private static Pair getRemote(String color) throws MalformedURLException {
        do {
            try {
                Game g = (Game) Naming.lookup("//localhost/" + color + "HotTarguiGame");
                GameRepository gr = (GameRepository) Naming.lookup("//localhost/" + color + "HotTarguiGameRepository");
                return new Pair(g, gr);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    private static PlayerColor stringToColor(String color) {
        if (color.toUpperCase().equals("RED")) {
            return PlayerColor.Red;
        } else if (color.toUpperCase().equals("GREEN")) {
            return PlayerColor.Green;
        } else if (color.toUpperCase().equals("BLUE")) {
            return PlayerColor.Blue;
        } else if (color.toUpperCase().equals("YELLOW")) {
            return PlayerColor.Yellow;
        }
        return PlayerColor.None;
    }
}
