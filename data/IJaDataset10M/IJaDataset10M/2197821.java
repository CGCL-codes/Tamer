package suggestion.client.classes;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Band extends Account implements Serializable {

    private String name;

    private int numOfTrack;

    public Band() {
    }

    public Band(int id, String l, String e, Date sd, String n, int nc) {
        super(id, l, e, sd);
        type = 'B';
        name = n;
        numOfTrack = nc;
    }

    public String getName() {
        return name;
    }

    public int getNumOfTrack() {
        return numOfTrack;
    }
}
