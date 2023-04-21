package viewer.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 * @param <T>
 *            the type of things.
 * @param <D>
 *            the type of data.
 */
public abstract class ImageProvider<T, D> {

    Object lock = new Object();

    List<Message> toProvideList = new ArrayList<Message>();

    Set<T> toProvideSet = new HashSet<T>();

    Set<T> providing = new HashSet<T>();

    Set<LoadListener<T, D>> listeners = new HashSet<LoadListener<T, D>>();

    private int nThreads;

    /**
	 * This is the method that this ImageProvider will use to get an Image. Implement this in
	 * order to create a special purpose provider.
	 * 
	 * @param thing
	 *            the thing that shall be loaded.
	 * @return the loaded data.
	 */
    public abstract D load(T thing);

    /**
	 * Provide the data for this thing. Return to the caller immediately. Notify the listeners
	 * as soon as loading is done.
	 * 
	 * @param thing
	 *            the thing to provide data for.
	 */
    public void provide(T thing) {
        synchronized (lock) {
            if (toProvideSet.contains(thing) || providing.contains(thing)) return;
            toProvideList.add(new Message(MessageType.Data, thing));
            toProvideSet.add(thing);
            lock.notify();
        }
    }

    /**
	 * Stop producing new elements.
	 */
    public void stopRunning() {
        synchronized (lock) {
            for (int i = 0; i < nThreads; i++) {
                toProvideList.add(new Message(MessageType.Kill, null));
            }
            lock.notifyAll();
        }
    }

    /**
	 * Add a listener to be notified on load / fail events.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
    public void addLoadListener(LoadListener<T, D> listener) {
        listeners.add(listener);
    }

    /**
	 * Remove a listener from the set of listeners.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
    public void removeLoadListener(LoadListener<T, D> listener) {
        listeners.remove(listener);
    }

    void notifyLoaded(T thing, D data) {
        for (LoadListener<T, D> listener : listeners) {
            listener.loaded(thing, data);
        }
    }

    void notifyFailed(T thing) {
        for (LoadListener<T, D> listener : listeners) {
            listener.loadFailed(thing);
        }
    }

    /**
	 * Create a new ImageProvider.
	 * 
	 * @param nThreads
	 *            the number of threads to use for loading.
	 */
    public ImageProvider(int nThreads) {
        this.nThreads = nThreads;
        for (int i = 0; i < nThreads; i++) {
            LoadThread loadThread = new LoadThread();
            Thread thread = new Thread(loadThread);
            thread.start();
        }
    }

    class LoadThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                T provide = null;
                synchronized (lock) {
                    if (toProvideList.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                        }
                    } else {
                        Message message = toProvideList.remove(toProvideList.size() - 1);
                        if (message.type == MessageType.Kill) {
                            System.out.println("stopped!!!!");
                            return;
                        }
                        provide = message.data;
                        toProvideSet.remove(provide);
                        providing.add(provide);
                    }
                }
                if (provide == null) continue;
                D loaded = load(provide);
                synchronized (lock) {
                    if (loaded == null) {
                        notifyFailed(provide);
                    } else {
                        notifyLoaded(provide, loaded);
                    }
                    providing.remove(provide);
                }
            }
        }
    }

    private class Message {

        MessageType type;

        T data;

        public Message(MessageType type, T data) {
            this.type = type;
            this.data = data;
        }
    }

    private enum MessageType {

        Data, Kill
    }
}
