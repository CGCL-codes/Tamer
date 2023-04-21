package br.ufpe.cin.stp.global.pattern.observer;

import java.util.List;
import java.util.Vector;

/**
 * @author Marcello Alves de Sales Junior <BR>
 * email: <a href=mailto:masj2@cin.ufpe.br>masj2@cin.ufpe.br</a> <BR>
 * @created 07/07/2004 20:55:19
 */
public class ObservableObject {

    private List observers;

    public ObservableObject() {
        this.observers = new Vector();
    }

    public void changeThisObjectValueAndAutomaticallyCallTheObservers(int newValue) {
        for (int i = 0; i < observers.size(); i++) {
            ((Observer) observers.get(i)).check(newValue);
        }
    }

    public void addInterestedObserver(Observer interestedObserver) {
        this.observers.add(interestedObserver);
    }

    public static void main(String[] args) {
        InterestedAgent ag = new InterestedAgent();
        InterestedPerson pe = new InterestedPerson();
        ObservableObject object = new ObservableObject();
        object.addInterestedObserver(ag);
        object.addInterestedObserver(pe);
        object.changeThisObjectValueAndAutomaticallyCallTheObservers(5);
    }
}
