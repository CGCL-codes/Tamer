package RMIServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import Middleware.IPlayer;
import Middleware.IRoom;
import Middleware.ICard;
import Middleware.Card;

public class Room extends UnicastRemoteObject implements IRoom {

    private static final long serialVersionUID = 8122748828955256369L;

    private final int upperLimitPlayers = 5;

    private Registry registry;

    private Server server;

    private ArrayList<IPlayer> players;

    private IPlayer dealer;

    private int currentPlayer;

    private int timestamp;

    private int maxPlayers;

    private int numberOfPlayers;

    private int ready;

    private int numberOfDecks;

    private Random generator;

    private boolean endOfRound = true;

    private String RMIName;

    public Room(int p, int nd, Server s, String RMIName) throws RemoteException {
        this.RMIName = RMIName;
        numberOfPlayers = 0;
        if (p < upperLimitPlayers) {
            maxPlayers = p;
        } else {
            maxPlayers = upperLimitPlayers;
        }
        players = new ArrayList<IPlayer>();
        dealer = new Player(new User("Dealer", "Dealer", "Dealer", 123));
        players.add(dealer);
        ready = 0;
        generator = new Random();
        server = s;
        numberOfDecks = nd;
        timestamp = 1;
        registry = LocateRegistry.getRegistry();
        registry.rebind(RMIName, this);
        System.out.println(RMIName + " listening...");
    }

    public int getPoints(IPlayer p) {
        return points(p.getCards());
    }

    public ArrayList<IPlayer> getPlayers() {
        return players;
    }

    private boolean hasBlackJack(IPlayer p) {
        return (points(p.getCards()) == 21 && p.getCards().size() == 2);
    }

    private void cleanup() {
        for (IPlayer player : players) {
            if (player.getType() == "SPLIT") {
                players.remove(player);
                cleanup();
                return;
            } else {
                player.clearCards();
                player.setIsWinner(false);
                player.setShowCards(false);
            }
        }
    }

    public void establishWinners() {
        while (getPoints(dealer) < 17) {
            dealer.addCard(generateCard());
        }
        int points, dealer_points = getPoints(dealer);
        boolean dealer_has_blackjack = hasBlackJack(dealer);
        double win = 0;
        System.out.println("Dealer has " + dealer_points + " points");
        for (IPlayer player : players) {
            if (player != dealer) {
                win = 0;
                points = getPoints(player);
                System.out.println(player.getUsername() + " has " + points + " points");
                if (points <= 21) {
                    if (hasBlackJack(player)) {
                        if (dealer_has_blackjack) {
                            win += player.getBet();
                        } else {
                            win += player.getBet() * 2.5;
                        }
                    } else {
                        if (!dealer_has_blackjack) {
                            if (points > dealer_points) {
                                win += 2 * player.getBet();
                            }
                            if (points == dealer_points) {
                                win += player.getBet();
                            }
                        }
                    }
                }
                if (win > 0) {
                    System.out.println(player.getUsername() + " wins " + win);
                    try {
                        server.addWinnings(player.getUsername(), win);
                    } catch (RemoteException re) {
                        System.out.println("Error giving money to " + player.getUsername());
                    }
                    player.setIsWinner(true);
                } else {
                    System.out.println("DEALER wins ");
                    player.setIsWinner(false);
                }
                player.setShowCards(true);
            }
        }
        dealer.setShowCards(true);
        endOfRound = true;
        update();
    }

    private ICard generateCard() {
        char v = 0, c = 0;
        int value = generator.nextInt(numberOfDecks * 52) % 52;
        if (value / 4 < 8) v = (char) (value / 4 + 2 + '0'); else switch(value / 4) {
            case 8:
                v = 't';
                break;
            case 9:
                v = 'j';
                break;
            case 10:
                v = 'q';
            case 11:
                v = 'k';
            case 12:
                v = 'a';
        }
        switch(value % 4) {
            case 0:
                c = 'c';
                break;
            case 1:
                c = 'd';
                break;
            case 2:
                c = 'h';
                break;
            case 3:
                c = 's';
                break;
        }
        return new Card(v, c);
    }

    public boolean bet(String username, String password, int amount) throws RemoteException {
        IPlayer player = server.getPlayer(username, password);
        if (player == null) {
            return false;
        }
        if (server.bet(player.getUsername(), amount)) {
            player.setBet(amount);
            ready++;
            if (ready == numberOfPlayers) {
                cleanup();
                endOfRound = false;
                ready = 0;
                for (IPlayer p : players) {
                    p.addCard(generateCard());
                    p.addCard(generateCard());
                }
                currentPlayer = 1;
            }
            update();
            return true;
        } else {
            return false;
        }
    }

    private void update() {
        timestamp++;
    }

    private int points(ArrayList<ICard> cards) {
        int p = 0;
        for (ICard c : cards) {
            switch(c.getValue()) {
                case '2':
                    p += 2;
                    break;
                case '3':
                    p += 3;
                    break;
                case '4':
                    p += 4;
                    break;
                case '5':
                    p += 5;
                    break;
                case '6':
                    p += 6;
                    break;
                case '7':
                    p += 7;
                    break;
                case '8':
                    p += 8;
                    break;
                case '9':
                    p += 9;
                    break;
                case 'T':
                    p += 10;
                    break;
                case 'J':
                    p += 10;
                    break;
                case 'Q':
                    p += 10;
                    break;
                case 'K':
                    p += 10;
                    break;
                case 'A':
                    p += 11;
                    break;
            }
        }
        if (p == 22 && cards.size() == 2) {
            p = 21;
        }
        if (p > 21) {
            for (ICard c : cards) {
                if (c.getValue() == 'A') {
                    p -= 10;
                    if (p <= 21) {
                        break;
                    }
                }
            }
        }
        return p;
    }

    public void hitMe(String username, String password) throws RemoteException {
        IPlayer player = server.getPlayer(username, password);
        if (player == null) {
            return;
        }
        if (player.getUsername().equals(players.get(currentPlayer).getUsername())) {
            player.addCard(generateCard());
            System.out.println("AM adaugat o carte si are in total " + points(player.getCards()));
            if (points(player.getCards()) > 21) {
                currentPlayer++;
            }
            if (currentPlayer > numberOfPlayers) {
                establishWinners();
            }
            update();
        }
    }

    public void enough(String username, String password) throws RemoteException {
        IPlayer player = server.getPlayer(username, password);
        if (player == null) {
            return;
        }
        if (player.getUsername().equals(players.get(currentPlayer).getUsername())) {
            currentPlayer++;
            if (currentPlayer > numberOfPlayers) {
                establishWinners();
            }
            update();
        }
    }

    public void betDouble(String username, String password) throws RemoteException {
        IPlayer player = server.getPlayer(username, password);
        if (player == null) {
            return;
        }
        if (player.getUsername().equals(players.get(currentPlayer).getUsername())) {
            int amount = player.getBet();
            if (server.bet(player.getUsername(), amount)) {
                player.setBet(2 * amount);
                player.addCard(generateCard());
                currentPlayer++;
                if (currentPlayer > numberOfPlayers) {
                    establishWinners();
                }
            }
            update();
        }
    }

    public IPlayer split(String username, String password) throws RemoteException {
        IPlayer player = server.getPlayer(username, password);
        if (player == null) {
            return null;
        }
        if (player.getUsername().equals(players.get(currentPlayer).getUsername())) {
            ArrayList<ICard> cards = player.getCards();
            if (cards.size() == 2 && cards.get(0).getValue() == cards.get(1).getValue()) {
                IPlayer sp = player.getUserClone();
                sp.setType("SPLIT");
                player.clearCards();
                player.addCard(cards.get(0));
                sp.addCard(cards.get(1));
                players.add(currentPlayer, sp);
                return sp;
            }
            update();
        }
        return null;
    }

    public boolean enter(String username, String password) throws RemoteException {
        IPlayer player = server.getPlayer(username, password);
        if (player == null) {
            return false;
        }
        if (hasRoom()) {
            System.out.println(this.RMIName + ": Am adaugat un nou jucator: " + player.getUsername());
            for (IPlayer p : players) {
                if (p.getUsername().equals(player.getUsername())) {
                    return true;
                }
            }
            numberOfPlayers++;
            players.add(player);
            update();
            return true;
        } else {
            return false;
        }
    }

    public boolean hasRoom() throws RemoteException {
        return numberOfPlayers < maxPlayers;
    }

    public void leave(String username, String password) throws RemoteException {
        IPlayer player = server.getPlayer(username, password);
        if (player == null) {
            return;
        }
        for (IPlayer p : players) {
            if (p.getUsername().equals(player.getUsername())) {
                System.out.println(this.RMIName + ": Am sters un jucator: " + p.getUsername());
                numberOfPlayers--;
                players.remove(p);
                break;
            }
        }
        System.out.println("AM ramas cu " + numberOfPlayers);
        if (numberOfPlayers == 0) {
            try {
                registry.unbind(RMIName);
                server.removeRoom(this);
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
        update();
    }

    public boolean IsRoundInProgress() throws RemoteException {
        return !endOfRound;
    }

    public String getRMIName() throws RemoteException {
        return this.RMIName;
    }

    public String getDetails() throws RemoteException {
        return "" + this.numberOfPlayers + " out of " + this.maxPlayers + " players";
    }

    public int getTimestamp() throws RemoteException {
        return this.timestamp;
    }

    public ArrayList<IPlayer> getPlayers(String username, String password) throws RemoteException {
        IPlayer player = server.getPlayer(username, password);
        if (player == null) {
            return null;
        }
        ArrayList<IPlayer> res = new ArrayList<IPlayer>();
        res.add(dealer);
        String next_player_username = "";
        if (currentPlayer < players.size()) {
            next_player_username = players.get(currentPlayer).getUsername();
        }
        for (IPlayer p : players) {
            if (p.getUsername().equals(player.getUsername())) {
                if (p.getUsername().equals(next_player_username)) {
                    p.setMyTurn(true);
                } else {
                    p.setMyTurn(false);
                }
                res.add(p);
            }
        }
        for (IPlayer p : players) {
            if (p != dealer && !p.getUsername().equals(player.getUsername())) {
                if (p.getUsername().equals(next_player_username)) {
                    p.setMyTurn(true);
                } else {
                    p.setMyTurn(false);
                }
                res.add(p);
            }
        }
        return res;
    }
}
