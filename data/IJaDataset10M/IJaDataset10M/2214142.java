package sgf.value;

import go.GoVertex;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValuePointList implements Value {

    private static final Pattern p = Pattern.compile("([a-zA-Z][a-zA-Z]):([a-zA-Z][a-zA-Z])");

    private ArrayList<GoVertex> vertexList;

    public ValuePointList() {
        vertexList = new ArrayList<GoVertex>();
    }

    public void addPoint(GoVertex v) {
        if (hasPoint(v) == false) {
            vertexList.add(v);
        }
    }

    public void removePoint(GoVertex v) {
        GoVertex vRemove = null;
        for (GoVertex vTmp : vertexList) {
            if (v.equals(vTmp)) {
                vRemove = vTmp;
                break;
            }
        }
        if (vRemove != null) {
            vertexList.remove(vRemove);
        }
    }

    public boolean isEmpty() {
        return vertexList.isEmpty();
    }

    public boolean hasPoint(GoVertex v) {
        for (GoVertex vTmp : vertexList) {
            if (v.equals(vTmp)) {
                return true;
            }
        }
        return false;
    }

    public void setSgfString(String str) {
        Matcher m = p.matcher(str);
        if (m.matches()) {
            GoVertex v0 = new GoVertex(m.group(1));
            GoVertex v1 = new GoVertex(m.group(2));
            int x0 = v0.getX();
            int y0 = v0.getY();
            int x1 = v1.getX();
            int y1 = v1.getY();
            for (int i = x0; i <= x1; ++i) {
                for (int j = y0; j <= y1; ++j) {
                    GoVertex v = new GoVertex(i, j);
                    addPoint(v);
                }
            }
        } else {
            GoVertex v = new GoVertex(str);
            addPoint(v);
        }
    }

    public String getSgfString() {
        return null;
    }

    public String getValueString() {
        StringBuilder str = new StringBuilder();
        for (GoVertex v : vertexList) {
            str.append("[");
            str.append(v.toSgfString());
            str.append("]");
        }
        return str.toString();
    }

    public Collection<GoVertex> getCollection() {
        return vertexList;
    }
}
