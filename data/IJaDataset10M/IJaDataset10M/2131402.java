package fr.lig.sigma.astral.query;

import fr.lig.sigma.astral.query.QueryNode;
import java.util.Map;

/**
 *
 */
public interface GraphNotifier {

    void sendGraph(Map<String, QueryNode> queries);

    void sendNotice(QueryNode node);
}
