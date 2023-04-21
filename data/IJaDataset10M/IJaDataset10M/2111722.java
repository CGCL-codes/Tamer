package transport.model;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author rem
 */
public class Flag extends Observation {

    public int area = AREA_NONE;

    int position = -1;

    public int[] location = new int[] { POSITION_NONE, POSITION_NONE };

    public String[] text;

    public List<String> params;

    public Flag(String[] item, List<String> parameters) {
        text = item;
        params = new LinkedList<String>();
        params.addAll(parameters);
        if (item.length > 1) {
            if (item[1].equals("c")) {
                location[0] = POSITION_CENTRE;
                if (item.length > 2) {
                    if (item[2].equals("t")) {
                        location[1] = POSITION_TOP;
                    } else {
                        location[1] = POSITION_BOTTOM;
                    }
                }
            } else if (item[1].equals("p")) {
                area = AREA_PENALTY_BOX;
                if (item[2].equals("l")) {
                    location[0] = POSITION_LEFT;
                } else {
                    location[0] = POSITION_RIGHT;
                }
                if (item[3].equals("t")) {
                    location[1] = POSITION_TOP;
                } else if (item[3].equals("c")) {
                    location[1] = POSITION_CENTRE;
                } else {
                    location[1] = POSITION_BOTTOM;
                }
            } else if (item[1].equals("g")) {
                area = AREA_GOAL;
                if (item[2].equals("l")) {
                    location[0] = POSITION_LEFT;
                } else {
                    location[0] = POSITION_RIGHT;
                }
                if (item[3].equals("t")) {
                    location[1] = POSITION_TOP;
                } else {
                    location[1] = POSITION_BOTTOM;
                }
            } else if (item[1].equals("r")) {
                location[0] = POSITION_RIGHT;
                if (item[2].equals("t")) {
                    location[1] = POSITION_TOP;
                    if (item.length > 3) position = Integer.parseInt(item[3]);
                } else if (item[2].equals("b")) {
                    location[1] = POSITION_BOTTOM;
                    if (item.length > 3) position = Integer.parseInt(item[3]);
                } else {
                    location[1] = POSITION_NONE;
                    position = Integer.parseInt(item[2]);
                }
            } else if (item[1].equals("l")) {
                location[0] = POSITION_LEFT;
                if (item[2].equals("t")) {
                    location[1] = POSITION_TOP;
                    if (item.length > 3) position = Integer.parseInt(item[3]);
                } else if (item[2].equals("b")) {
                    location[1] = POSITION_BOTTOM;
                    if (item.length > 3) position = Integer.parseInt(item[3]);
                } else {
                    location[1] = POSITION_NONE;
                    position = Integer.parseInt(item[2]);
                }
            } else if (item[1].equals("t")) {
                location[0] = POSITION_TOP;
                if (item[2].equals("l")) {
                    location[1] = POSITION_LEFT;
                    if (item.length > 3) position = Integer.parseInt(item[3]);
                } else if (item[2].equals("r")) {
                    location[1] = POSITION_RIGHT;
                    if (item.length > 3) position = Integer.parseInt(item[3]);
                } else {
                    location[1] = POSITION_NONE;
                    position = Integer.parseInt(item[2]);
                }
            } else if (item[1].equals("b")) {
                location[0] = POSITION_BOTTOM;
                if (item[2].equals("l")) {
                    location[1] = POSITION_LEFT;
                    if (item.length > 3) position = Integer.parseInt(item[3]);
                } else if (item[2].equals("r")) {
                    location[1] = POSITION_RIGHT;
                    if (item.length > 3) position = Integer.parseInt(item[3]);
                } else {
                    location[1] = POSITION_NONE;
                    position = Integer.parseInt(item[2]);
                }
            } else {
                String output = "";
                for (String val : item) {
                    output += val + " ";
                }
                System.out.println("NOT UNDERSTOOD: " + output);
            }
        }
        setParameters(parameters);
    }

    @Override
    public String toString() {
        return "Flag" + super.toString() + "{" + areaStrings[area] + " [" + locationStrings[location[0] - 10] + "," + locationStrings[location[1] - 10] + "] " + position + "}";
    }

    public String getLocation(int index) {
        return locationStrings[location[index] - 10];
    }

    public Position getFixedLocation() {
        Position loc = new Position();
        if (area == AREA_NONE) {
            if ((location[0] == POSITION_TOP) || (location[0] == POSITION_BOTTOM)) {
                if (location[0] == POSITION_TOP) {
                    loc.y = 39.0;
                } else if (location[0] == POSITION_BOTTOM) {
                    loc.y = -39.0;
                }
                loc.x = position;
                if (location[1] == POSITION_LEFT) {
                    loc.x = -loc.x;
                }
            } else if ((location[0] == POSITION_LEFT) || (location[0] == POSITION_RIGHT)) {
                if (position > -1) {
                    if (location[0] == POSITION_LEFT) {
                        loc.x = -57.5;
                    } else if (location[0] == POSITION_RIGHT) {
                        loc.x = 57.5;
                    }
                    loc.y = position;
                    if (location[1] == POSITION_TOP) {
                        loc.y = -loc.y;
                    }
                } else {
                    if (location[0] == POSITION_LEFT) {
                        loc.x = -52.5;
                    } else if (location[0] == POSITION_RIGHT) {
                        loc.x = 52.5;
                    }
                    if (location[1] == POSITION_TOP) {
                        loc.y = -34;
                    } else if (location[1] == POSITION_BOTTOM) {
                        loc.y = 34;
                    }
                }
            } else if (location[0] == POSITION_CENTRE) {
                if (location[1] == POSITION_TOP) {
                    loc.y = -34;
                } else if (location[1] == POSITION_BOTTOM) {
                    loc.y = 34;
                }
            }
        } else if (area == AREA_PENALTY_BOX) {
            if (location[0] == POSITION_RIGHT) {
                loc.x = 35.0;
            } else if (location[0] == POSITION_LEFT) {
                loc.x = -35.0;
            }
            if (location[1] == POSITION_TOP) {
                loc.y = -20.0;
            } else if (location[1] == POSITION_BOTTOM) {
                loc.y = 20.0;
            } else if (location[1] == POSITION_CENTRE) {
                loc.y = 0.0;
            }
        } else if (area == AREA_GOAL) {
            if (location[0] == POSITION_RIGHT) {
                loc.x = 52.5;
            } else if (location[0] == POSITION_LEFT) {
                loc.x = -52.5;
            }
            if (location[1] == POSITION_TOP) {
                loc.y = -7.5;
            } else if (location[1] == POSITION_BOTTOM) {
                loc.y = 7.5;
            }
        }
        return loc;
    }
}
