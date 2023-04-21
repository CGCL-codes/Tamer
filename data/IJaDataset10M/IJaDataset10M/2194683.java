package net.sf.nxqd;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import javax.xml.namespace.QName;

/**
 * Describe class <code>NxqdQueryExpression</code> here.
 *
 * @author <a href="mailto:"></a>
 * @version 1.0
 */
public class NxqdQueryExpression extends NxqdConsumer {

    /**
     * The variable <code>logger</code> is used for logging events.
     *
     */
    private static Logger logger = Logger.getLogger(NxqdQueryExpression.class.getName());

    private String query, queryPlan;

    protected NxqdQueryExpression(NxqdManager manager) throws NxqdException {
        super(manager);
    }

    public String getQuery() {
        return query;
    }

    public String getQueryPlan() {
        return queryPlan;
    }
}
