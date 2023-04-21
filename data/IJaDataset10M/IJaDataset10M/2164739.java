package playground.johannes.socialnets;

import java.util.Map;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.io.GraphMLFileHandler;

/**
 * @author illenberger
 *
 */
public class PersonGraphMLFileHandler extends GraphMLFileHandler {

    @Override
    protected ArchetypeVertex createVertex(Map attrs) {
        ArchetypeVertex v = super.createVertex(attrs);
        v.setUserDatum(UserDataKeys.ID, (String) attrs.get(UserDataKeys.ID), UserDataKeys.COPY_ACT);
        v.setUserDatum(UserDataKeys.X_COORD, Double.parseDouble((String) attrs.get(UserDataKeys.X_COORD)), UserDataKeys.COPY_ACT);
        v.setUserDatum(UserDataKeys.Y_COORD, Double.parseDouble((String) attrs.get(UserDataKeys.Y_COORD)), UserDataKeys.COPY_ACT);
        return v;
    }
}
