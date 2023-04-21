package cham.open.pattern.observer;

/**
 *
 * @author Chaminda Amarasinghe <chaminda.amarasinghe@headstrong.com>
 */
public interface Subject {

    void remove(Observer observer1);

    void register(Observer observer1);

    int getSizeOfObservers();

    void notifyObservers();

    void setValForObservers(int val);
}
