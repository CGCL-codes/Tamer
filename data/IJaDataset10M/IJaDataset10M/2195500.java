package newGameLogic;

import java.util.ArrayList;

public class MoveEvent implements Subject {

    String description;

    ArrayList<Observer> observers = new ArrayList<Observer>();

    public MoveEvent(String description) {
        this.description = description;
    }

    @Override
    public void registerObserver(Observer thisObserver) {
        observers.add(thisObserver);
    }

    @Override
    public void removeObserver(Observer thisObserver) {
        observers.remove(thisObserver);
    }

    @Override
    public void notifyObservers() {
        for (Observer curObserver : observers) {
            curObserver.update();
        }
    }

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }
}
