package no.eirikb.bomberman.server;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.eirikb.bomberman.applet.game.Bomb;
import no.eirikb.bomberman.client.Client;
import no.eirikb.bomberman.shared.DB;
import no.eirikb.bomberman.shared.Game;
import no.eirikb.bomberman.applet.game.Pos;
import no.eirikb.bomberman.applet.game.PowerUp;
import no.eirikb.bomberman.applet.game.Way;
import no.eirikb.bomberman.shared.Map;
import no.eirikb.bomberman.shared.message.Message;
import no.eirikb.bomberman.shared.User;
import no.eirikb.bomberman.shared.clientcommand.AddPowerUpEvent;
import no.eirikb.bomberman.shared.clientcommand.BombCreateEvent;
import no.eirikb.bomberman.shared.clientcommand.BombExplodeEvent;
import no.eirikb.bomberman.shared.clientcommand.ClientEvent;
import no.eirikb.bomberman.shared.clientcommand.KillUserEvent;
import no.eirikb.bomberman.shared.clientcommand.PowerUpSpeedEvent;
import no.eirikb.bomberman.shared.clientcommand.StartWalkEvent;
import no.eirikb.bomberman.shared.clientcommand.StopWalkEvent;
import no.eirikb.bomberman.shared.img.ImageDB;
import no.eirikb.bomberman.shared.message.GameMessage;
import no.eirikb.bomberman.shared.message.PrivateMessage;
import no.eirikb.bomberman.shared.message.PublicMessage;

/**
 * I have a bad feeling this file will grow way beond normal code length :(
 * @author eirikb
 */
public class BombermanServer extends UnicastRemoteObject implements Server {

    private DB db;

    private Hashtable<String, ServerUser> users;

    private Hashtable<String, Client> clients;

    private Hashtable<String, Client> notInGame;

    private GameHandler gameHandler;

    private BombHandler bombHandler;

    private final int PINGRATE = 3;

    private final int SLEEPTIME = 50;

    private final int MAXLATENCY = 200;

    public static void main(String[] args) {
        try {
            BombermanServer server = new BombermanServer();
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("BOMBERMAN", server);
            System.out.println("Press enter to terminate");
            System.in.read();
            reg.unbind("BOMBERMAN");
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(BombermanServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(BombermanServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }

    public BombermanServer() throws RemoteException {
        try {
            FileHandler handler = new FileHandler("BombermanServer.log");
            Logger.getLogger(BombermanServer.class.getName()).addHandler(handler);
        } catch (IOException ex) {
            Logger.getLogger(BombermanServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(BombermanServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        db = DB.load();
        users = new Hashtable<String, ServerUser>();
        clients = new Hashtable<String, Client>();
        notInGame = new Hashtable<String, Client>();
        gameHandler = new GameHandler();
        bombHandler = new BombHandler(this, gameHandler);
        bombHandler.start();
        ping();
    }

    /**
     * Method for pingin each client
     * Pings by PINGRATE (in seconds)
     */
    private void ping() {
        new Thread() {

            public void run() {
                while (true) {
                    String[] ns = clients.keySet().toArray(new String[0]);
                    Client[] cs = clients.values().toArray(new Client[0]);
                    for (int i = 0; i < ns.length; i++) {
                        try {
                            cs[i].ping();
                            User u = users.get(ns[i]).getUser();
                        } catch (RemoteException ex) {
                            Logger.getLogger(BombermanServer.class.getName()).log(Level.WARNING, "Client did not respond to ping!", ex);
                            removeUser(ns[i]);
                        }
                    }
                    try {
                        Thread.sleep(PINGRATE * 1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BombermanServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }

    /**
     * Remove a user from the userver
     * @param nick Nickname of the user to remove
     */
    public void removeUser(String nick) {
        System.out.println("Remove user!" + nick);
        if (users.get(nick) != null) {
            gameHandler.leaveGame(users.get(nick).getUser());
            users.remove(nick);
            clients.remove(nick);
            notInGame.remove(nick);
            chat(new PublicMessage(null, nick + " leaves the game."));
        } else {
            System.out.println("REMOVEUSER: Unknown user " + nick);
        }
    }

    public void sendClientEvent(User user, ClientEvent event) {
        String[] ns = clients.keySet().toArray(new String[0]);
        Client[] cs = clients.values().toArray(new Client[0]);
        for (int i = 0; i < ns.length; i++) {
            if (!ns[i].equals(user.getNick())) {
                try {
                    cs[i].clientEvent(event);
                } catch (RemoteException ex) {
                    Logger.getLogger(BombermanServer.class.getName()).log(Level.WARNING, "SENDUSERUPDATE, client did not resond, removing", ex);
                    removeUser(ns[i]);
                }
            }
        }
    }

    public void sendClientEvent(ClientEvent event) {
        String[] ns = clients.keySet().toArray(new String[0]);
        Client[] cs = clients.values().toArray(new Client[0]);
        for (int i = 0; i < ns.length; i++) {
            try {
                cs[i].clientEvent(event);
            } catch (RemoteException ex) {
                Logger.getLogger(BombermanServer.class.getName()).log(Level.WARNING, "SENDUSERUPDATE, client did not resond, removing", ex);
                removeUser(ns[i]);
            }
        }
    }

    /**
     * Send a chat message to all clients
     * @param message Message to send to clients
     */
    public synchronized void chat(Message message) {
        System.out.println("CHAT! " + clients.size() + " clients!");
        String[] ns = clients.keySet().toArray(new String[0]);
        Client[] cs = clients.values().toArray(new Client[0]);
        for (int i = 0; i < ns.length; i++) {
            try {
                cs[i].chat(message);
            } catch (RemoteException ex) {
                Logger.getLogger(BombermanServer.class.getName()).log(Level.WARNING, "CHAT, client did not resond, removing", ex);
                removeUser(ns[i]);
            }
        }
    }

    /**
     * Validate walk! Does NOT work atm!
     * @param sUser
     * @param pos2
     * @return
     */
    private boolean validateWalk(ServerUser sUser, Pos pos2) {
        User user = sUser.getUser();
        long time = System.currentTimeMillis() - sUser.getTime();
        double walkLength = Math.sqrt(Math.pow(pos2.getX() - user.getPos().getX(), 2) + Math.pow(pos2.getY() - user.getPos().getY(), 2));
        System.out.println("walkl " + walkLength);
        double possibleWalk = (time / SLEEPTIME) * user.getSpeed();
        System.out.println("possiblew " + possibleWalk);
        double errorWalk = (MAXLATENCY / SLEEPTIME) * user.getSpeed();
        System.out.println("errorw " + errorWalk);
        sUser.setTime(System.currentTimeMillis());
        return true;
    }

    private User getUser(Double hash, String nick) {
        ServerUser sUser = users.get(nick);
        if (sUser != null && sUser.getHash().equals(hash)) {
            return sUser.getUser();
        } else {
            return null;
        }
    }

    private ServerUser getServerUser(Double hash, String nick) {
        ServerUser sUser = users.get(nick);
        if (sUser != null && sUser.getHash().equals(hash)) {
            return sUser;
        } else {
            return null;
        }
    }

    /**
     * NOT WORKING!
     * @param bomb
     */
    public void explodeBomb(Bomb bomb) {
        User user = users.get(bomb.getUser().getNick()).getUser();
        user.setBombs(user.getBombs() + 1);
        sendClientEvent(new BombExplodeEvent(bomb));
        Game game = gameHandler.getGame(bomb.getUser().getGameName());
        int x = bomb.getX() * game.getSize();
        int y = bomb.getY() * game.getSize();
        int alter = 1;
        int c = 10;
        for (int i = 0; i <= bomb.getSize() + 1; i += bomb.getSize() + 1) {
            boolean xb, yb;
            xb = yb = true;
            for (int j = bomb.getX() - bomb.getSize() + i; j < bomb.getX() + i; j++) {
                x += alter;
                y += alter;
                if (xb && game.getMap(x, bomb.getY()) == Map.BRICK && Math.random() >= 0.4) {
                    sendClientEvent(new AddPowerUpEvent(new PowerUp(x, bomb.getY())));
                    xb = false;
                }
                if (yb && game.getMap(bomb.getX(), y) == Map.BRICK && Math.random() >= 0.4) {
                    sendClientEvent(new AddPowerUpEvent(new PowerUp(bomb.getX(), y)));
                    yb = false;
                }
            }
            alter = -1;
        }
    }

    public void addPowerUp(PowerUp powerUp) {
        sendClientEvent(new AddPowerUpEvent(powerUp));
    }

    public ImageDB getImageDB() throws RemoteException {
        return db.getIdb();
    }

    public synchronized Double registerUser(Client client, User user) throws RemoteException {
        if (users.get(user.getNick()) != null) {
            return null;
        }
        chat(new PublicMessage(null, user.getNick() + " joins the game."));
        clients.put(user.getNick(), client);
        notInGame.put(user.getNick(), client);
        Double hash = Math.random();
        users.put(user.getNick(), new ServerUser(user, hash));
        return hash;
    }

    public boolean chat(Double hash, Message message) throws RemoteException {
        System.out.println("CHAT: " + message);
        if (message.getFrom() != null && getUser(hash, message.getFrom()) != null) {
            if (message instanceof PublicMessage) {
                chat(message);
                return true;
            } else if (message instanceof GameMessage) {
                return false;
            } else if (message instanceof PrivateMessage) {
                PrivateMessage priv = (PrivateMessage) message;
                Client client;
                if ((client = clients.get(priv.getTo())) != null) {
                    client.chat(message);
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    public synchronized boolean createGame(Double hash, String nick, Game game) throws RemoteException {
        System.out.println("CREATEGAME: " + nick + " " + game.getName());
        User user;
        if ((user = getUser(hash, nick)) != null && gameHandler.createGame(game, user)) {
            notInGame.remove(nick);
            String[] ns = notInGame.keySet().toArray(new String[0]);
            Client[] cs = notInGame.values().toArray(new Client[0]);
            for (int i = 0; i < ns.length; i++) {
                try {
                    cs[i].addGame(game);
                } catch (RemoteException ex) {
                    Logger.getLogger(BombermanServer.class.getName()).log(Level.WARNING, "CHAT, client did not resond, removing", ex);
                    removeUser(ns[i]);
                }
            }
            return true;
        } else {
            System.out.println("!CREATEGAME: Unknown user, bad hash or game taken");
            return false;
        }
    }

    public synchronized boolean joinGame(Double hash, String nick, String gameName) throws RemoteException {
        System.out.println("JOINGAME: " + nick + " " + gameName);
        User user;
        Game game;
        if ((user = getUser(hash, nick)) != null && (game = gameHandler.joinGame(gameName, user)) != null) {
            notInGame.remove(nick);
            User[] us = game.getUsers().values().toArray(new User[0]);
            for (int i = 0; i < us.length; i++) {
                try {
                    clients.get(us[i].getNick()).updateGame(game);
                } catch (RemoteException ex) {
                    Logger.getLogger(BombermanServer.class.getName()).log(Level.WARNING, "CHAT, client did not resond, removing", ex);
                    removeUser(us[i].getNick());
                }
            }
            String[] ns = notInGame.keySet().toArray(new String[0]);
            Client[] cs = notInGame.values().toArray(new Client[0]);
            for (int i = 0; i < ns.length; i++) {
                try {
                    cs[i].addGame(game);
                } catch (RemoteException ex) {
                    Logger.getLogger(BombermanServer.class.getName()).log(Level.WARNING, "CHAT, client did not resond, removing", ex);
                    removeUser(ns[i]);
                }
            }
            return true;
        } else {
            System.out.println("!JOINGAME: Unknown user, bad hash or unknown game");
            return false;
        }
    }

    public synchronized void leaveGame(Double hash, String nick) throws RemoteException {
        System.out.println("LEAVEGAME: " + nick);
        User user;
        Game game;
        if ((user = getUser(hash, nick)) != null && (game = gameHandler.leaveGame(user)) != null) {
            if (game.getUsers().size() <= 0) {
                String[] ns = notInGame.keySet().toArray(new String[0]);
                Client[] cs = notInGame.values().toArray(new Client[0]);
                for (int i = 0; i < ns.length; i++) {
                    try {
                        cs[i].removeGame(game);
                    } catch (RemoteException ex) {
                        Logger.getLogger(BombermanServer.class.getName()).log(Level.WARNING, "CHAT, client did not resond, removing", ex);
                        removeUser(ns[i]);
                    }
                }
            } else {
                User[] us = game.getUsers().values().toArray(new User[0]);
                for (int i = 0; i < us.length; i++) {
                    try {
                        clients.get(us[i].getNick()).updateGame(game);
                    } catch (RemoteException ex) {
                        Logger.getLogger(BombermanServer.class.getName()).log(Level.WARNING, "CHAT, client did not resond, removing", ex);
                        removeUser(us[i].getNick());
                    }
                }
                String[] ns = notInGame.keySet().toArray(new String[0]);
                Client[] cs = notInGame.values().toArray(new Client[0]);
                for (int i = 0; i < ns.length; i++) {
                    try {
                        cs[i].updateGame(game);
                    } catch (RemoteException ex) {
                        Logger.getLogger(BombermanServer.class.getName()).log(Level.WARNING, "CHAT, client did not resond, removing", ex);
                        removeUser(ns[i]);
                    }
                }
            }
        } else {
            System.out.println("!LEAVEGAME: Unkown user " + nick + " or bad hash");
        }
    }

    public void setReady(Double hash, String nick, boolean ready) throws RemoteException {
        System.out.println("SETREADY: " + nick + " " + ready);
        User user;
        Game game;
        if ((user = getUser(hash, nick)) != null && (game = gameHandler.setReady(user, ready)) != null) {
            if (game.isReady()) {
                User[] us = game.getUsers().values().toArray(new User[0]);
                for (int i = 0; i < us.length; i++) {
                    try {
                        users.get(us[i].getNick()).setTime(System.currentTimeMillis());
                        clients.get(us[i].getNick()).startGame(game);
                    } catch (RemoteException ex) {
                        Logger.getLogger(BombermanServer.class.getName()).log(Level.WARNING, "CHAT, client did not resond, removing", ex);
                        removeUser(us[i].getNick());
                    }
                }
            } else {
                User[] us = game.getUsers().values().toArray(new User[0]);
                for (int i = 0; i < us.length; i++) {
                    try {
                        clients.get(us[i].getNick()).updateGame(game);
                    } catch (RemoteException ex) {
                        Logger.getLogger(BombermanServer.class.getName()).log(Level.WARNING, "CHAT, client did not resond, removing", ex);
                        removeUser(us[i].getNick());
                    }
                }
            }
        } else {
            System.out.println("!SETREADY: Unkown user " + nick + ", bad hash" + " or unkown game");
        }
    }

    public Game[] getGameList() throws RemoteException {
        return gameHandler.getGameList();
    }

    public void startWalk(Double hash, String nick, Way way, Pos pos) throws RemoteException {
        System.out.println("startwalk");
        User user;
        if ((user = getUser(hash, nick)) != null) {
            ServerUser sUser = getServerUser(hash, nick);
            sUser.setTime(System.currentTimeMillis());
            StartWalkEvent event = new StartWalkEvent(nick, way, pos);
            event.execute(user);
            sendClientEvent(user, event);
        }
    }

    public void stopWalk(Double hash, String nick, Pos pos) throws RemoteException {
        System.out.println("stopwalk");
        User user;
        if ((user = getUser(hash, nick)) != null) {
            ServerUser sUser = getServerUser(hash, nick);
            if (validateWalk(sUser, pos)) {
                StopWalkEvent event = new StopWalkEvent(nick, pos);
                event.execute(user);
                sendClientEvent(user, event);
            } else {
                Logger.getLogger(BombermanServer.class.getName()).log(Level.WARNING, "VALIDATEWALK! " + nick);
            }
        }
    }

    public synchronized void disconnect(Double hash, String nick) throws RemoteException {
        if (getUser(hash, nick) != null) {
            removeUser(nick);
        } else {
            System.out.println("DISCONNECT: Unknown user or errorous hash " + nick);
        }
    }

    public void setBomb(Double hash, String nick, Bomb bomb) throws RemoteException {
        User user;
        if ((user = getUser(hash, nick)) != null) {
            if (user.getBombs() > 0) {
                user.setBombs(user.getBombs() - 1);
                bomb.setUser(user);
                bomb.setSize(user.getBombSize());
                bomb.setTime(user.getBombTime());
                bombHandler.addBomb(bomb);
                sendClientEvent(user, new BombCreateEvent(bomb));
            }
        }
    }

    @Override
    public void powerUp(Double hash, String nick, PowerUp powerUp) throws RemoteException {
        User user;
        if ((user = getUser(hash, nick)) != null) {
            sendClientEvent(user, new PowerUpSpeedEvent(user, 1, powerUp));
        }
    }
}
