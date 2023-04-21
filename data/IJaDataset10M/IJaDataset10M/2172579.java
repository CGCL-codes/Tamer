package org.state4j.publishing;

import java.util.List;

public interface Publisher {

    public void attach(Observer observer);

    public void detach(Observer observer);

    public void publish(Publishable event);

    public void attach(List<Observer> arg0);
}
