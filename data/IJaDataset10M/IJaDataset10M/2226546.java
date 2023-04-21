package AlphaMiner;

import java.util.ArrayList;

/**
 *
 * @author XY
 */
public class StartEvents {

    private ArrayList<String> startevents = new ArrayList<String>();

    private AlphaLog log;

    public StartEvents(AlphaLog log) {
        this.log = log;
        for (int i = 0; i < log.getTraces().size(); i++) {
            if (!isInAlphabet(log.getTraces().get(i).getEvents().get(0).getName())) {
                this.startevents.add(log.getTraces().get(i).getEvents().get(0).getName());
            }
        }
    }

    public ArrayList<String> getStartEvents() {
        return startevents;
    }

    public boolean isInAlphabet(String newWord) {
        for (String word : startevents) {
            if (word.equals(newWord)) {
                return true;
            }
        }
        return false;
    }
}
