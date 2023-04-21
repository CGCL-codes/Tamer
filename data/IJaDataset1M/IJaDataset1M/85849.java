package transport.model;

import java.util.List;

/**
 *
 * @author rem
 */
public class CoachGoal {

    public String side;

    public Position position;

    public CoachGoal(String[] items, List<String> parameters) {
        side = items[1];
        position = new Position(Double.parseDouble(parameters.get(0)), Double.parseDouble(parameters.get(1)));
    }

    @Override
    public String toString() {
        return "";
    }
}
