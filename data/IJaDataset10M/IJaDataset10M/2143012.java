package phex.rules.condition;

import phex.download.RemoteFile;
import phex.query.Search;
import phex.xml.sax.rules.DCondition;

public interface Condition extends Cloneable {

    public boolean isMatched(Search search, RemoteFile remoteFile);

    /**
     * Validates if this condition is completly edited and ready for storage or 
     * requires further modifications.
     * @return true if complet false otherwise.
     */
    public boolean isComplete();

    public Object clone();

    public DCondition createDCondition();
}
