package symore.test;

import java.util.Set;
import symore.notification.ChangeEvent;
import symore.notification.ChangeListener;

/**
 * 
 * @author Frank Bregulla, Manuel Scholz
 *
 */
public class SimpleChangeListener implements ChangeListener {

    private String name;

    public SimpleChangeListener(String name) {
        super();
        this.name = name;
    }

    public void viewChanged(ChangeEvent ce) {
    }
}
