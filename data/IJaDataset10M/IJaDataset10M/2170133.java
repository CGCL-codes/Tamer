package org.km.xplane.fixes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Fix {

    private String _name;

    private HashMap<String, FixPosition> _positions = new HashMap<String, FixPosition>();

    public Fix(String name, FixPosition position) throws IOException {
        _name = name;
        addPosition(position);
    }

    public String getName() {
        return _name;
    }

    public Collection<FixPosition> getPositions() {
        return _positions.values();
    }

    public void addPosition(FixPosition position) throws IOException {
        if (!_positions.containsKey(position.toString())) _positions.put(position.toString(), position); else Config.report("Duplicate FIX (exact; deleted):\t" + _name.trim() + " " + position.toString());
    }

    public void removePosition(FixPosition position) {
        _positions.remove(position.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Iterator<FixPosition> it = getPositions().iterator();
        while (it.hasNext()) {
            builder.append(it.next().toString());
            builder.append(' ');
            builder.append(_name);
            builder.append("\r\n");
        }
        return builder.toString();
    }

    public void cleanUp() throws IOException {
        if (_positions.size() > 1) {
            cleanUpAuto();
        }
    }

    private void cleanUpAuto() throws IOException {
        ArrayList<FixPosition> poss = new ArrayList<FixPosition>(getPositions());
        for (int i = 0; i < poss.size() - 1; i++) for (int j = i + 1; j < poss.size(); j++) {
            FixPosition current = poss.get(j);
            double distance = poss.get(i).getDistanceTo(current);
            if (Math.abs(distance) < Config.autoDistance) {
                if (current.isImported()) {
                    Config.report("Duplicate FIX (dist; deleted): " + _name.trim() + " " + poss.get(i).toString() + " (INFO: distance " + distance + " keeping: " + poss.get(i).toString() + " [imported])");
                    removePosition(poss.get(i));
                    poss.remove(i);
                    i--;
                    break;
                } else {
                    Config.report("Duplicate FIX (dist; deleted): " + _name.trim() + " " + poss.get(j).toString() + " (INFO: distance " + distance + " keeping: " + poss.get(i).toString() + (poss.get(i).isImported() ? " [imported]" : "") + ")");
                    removePosition(current);
                    poss.remove(j);
                    j--;
                }
            }
        }
    }
}
