package org.querycreator.converter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Luciano Molinari
 *
 */
public class ClauseAndParameters implements Serializable {

    private static final long serialVersionUID = 2831630782420571140L;

    private String clause;

    private List<Object> parameters;

    public ClauseAndParameters(String clause, List<Object> parameters) {
        this.clause = clause;
        this.parameters = parameters;
    }

    public String getClause() {
        return clause;
    }

    public List<Object> getParameters() {
        return Collections.unmodifiableList(parameters);
    }
}
