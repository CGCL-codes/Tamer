package newsatort.object;

import newsatort.event.IEvent;

public interface IObservable {

    public void addObserver(IObserver observer);

    public void removeAllObservers();

    public void removeObserver(IObserver observer);

    public void notifyObservers(IEvent event);
}
