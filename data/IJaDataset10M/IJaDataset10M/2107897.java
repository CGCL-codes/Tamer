package net.davidlauzon.assemblandroid.asyncTask;

import java.util.ArrayList;
import android.os.AsyncTask;

public abstract class AssemblaAsyncTask<E1, E2, E3> extends AsyncTask<E1, E2, E3> implements IAsynctaskObservable {

    private ArrayList<IAsynctaskObserver> _observers = new ArrayList<IAsynctaskObserver>();

    protected Exception _exception = null;

    protected int _parsingSeconds;

    protected int _loadingSeconds;

    protected int _count;

    public void addObserver(IAsynctaskObserver observer) {
        _observers.add(observer);
    }

    public void removeObserver(IAsynctaskObserver observer) {
        _observers.remove(observer);
    }

    public void notifyObservers() {
        for (IAsynctaskObserver observer : _observers) {
            if (_exception == null) {
                observer.onUpdate();
            } else {
                observer.onUpdateFailed(_exception);
            }
        }
    }

    public void clearObservers() {
        _observers.clear();
    }
}
