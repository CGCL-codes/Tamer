package dnsprobe.controller;

import java.awt.event.ActionListener;
import dnsprobe.data.ApplicationData;
import dnsprobe.gui.View;

public class Controller {

    public interface Main extends ActionListener {

        public void setModel(final ApplicationData model);

        public void setView(final View.Main view);

        public void printLog(final Object... part);

        public void taskDone();
    }
}
