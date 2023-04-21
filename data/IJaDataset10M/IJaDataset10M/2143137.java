package it.unibz.izock.client.models;

import it.unibz.izock.Cardable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class GameBoardModel extends Observable {

    private Map<String, Cardable> _cards;

    private List<String> _seats;

    private Boolean _shouldIMove;

    public GameBoardModel() {
        _cards = new HashMap<String, Cardable>();
        _seats = new ArrayList<String>();
        _shouldIMove = false;
    }

    public void init() throws RemoteException {
        setSeats(Remote.game().getSeating());
    }

    public void addObserver(Observer o) {
        super.addObserver(o);
        o.update(this, null);
    }

    public void setCards(Map<String, Cardable> cards) {
        try {
            synchronized (_cards) {
                _cards.clear();
                _cards.putAll(cards);
            }
            setChanged();
            notifyObservers();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public Map<String, Cardable> getBoard() {
        synchronized (_cards) {
            return _cards;
        }
    }

    public List<String> getSeating() throws RemoteException {
        synchronized (_seats) {
            return _seats;
        }
    }

    public void setSeats(List<String> seating) {
        synchronized (_seats) {
            _seats.clear();
            _seats.addAll(seating);
        }
        setChanged();
        notifyObservers();
    }

    public void setToPerformMove(boolean move) {
        synchronized (_shouldIMove) {
            _shouldIMove = move;
        }
    }

    public boolean shouldIPerformAMove() {
        synchronized (_shouldIMove) {
            return _shouldIMove;
        }
    }
}
