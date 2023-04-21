package gr.konstant.transonto.interfaces.dul;

import java.util.Set;
import gr.konstant.transonto.exception.*;

public interface DULObject extends Entity {

    public boolean isAgent() throws BackendException;

    public boolean isDescription() throws BackendException;

    public boolean isSituation() throws BackendException;

    public void hasPart(DULObject part, boolean trans) throws BackendException;

    public Set<? extends DULObject> hasPart(boolean trans) throws BackendException, BadKnowledgeBaseException;

    public DULObject createPart(boolean trans) throws BackendException;
}
