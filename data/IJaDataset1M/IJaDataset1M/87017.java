package websphinx.workbench;

import java.awt.CardLayout;
import java.awt.Choice;
import java.awt.Event;
import java.awt.Panel;

abstract class FeatureChoice extends Choice {

    public FeatureChoice() {
    }

    public abstract Panel getArgs();

    public synchronized void select(int pos) {
        super.select(pos);
        flipArgs();
    }

    public synchronized void select(String item) {
        super.select(item);
        flipArgs();
    }

    public boolean handleEvent(Event event) {
        if (event.id == Event.ACTION_EVENT && event.target == this) {
            flipArgs();
            return true;
        } else return super.handleEvent(event);
    }

    void flipArgs() {
        Panel args = getArgs();
        CardLayout layout = (CardLayout) (args.getLayout());
        layout.show(args, getSelectedItem());
    }
}
