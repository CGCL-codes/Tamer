package bagaturchess.search.impl.alg.impl3.iter;

import bagaturchess.search.api.internal.ISearchMoveList;
import bagaturchess.search.api.internal.ISearchMoveListFactory;
import bagaturchess.search.impl.env.SearchEnv;

public class SearchMoveListFactory3 implements ISearchMoveListFactory {

    protected static OrderingStatistics orderingStatistics = new OrderingStatistics();

    public SearchMoveListFactory3() {
    }

    @Override
    public ISearchMoveList createListAll(SearchEnv env) {
        return new ListAll3(env, orderingStatistics);
    }

    @Override
    public ISearchMoveList createListCaptures(SearchEnv env) {
        return null;
    }
}
