package bagaturchess.search.impl.rootsearch.parallel;

import java.util.Stack;
import bagaturchess.bitboard.api.IBitBoard;
import bagaturchess.bitboard.api.IBoardConfig;
import bagaturchess.bitboard.impl.Board;
import bagaturchess.search.api.internal.ISearch;
import bagaturchess.search.api.internal.ISearchMediator;
import bagaturchess.search.impl.alg.SearchMTD;
import bagaturchess.search.impl.env.SearchEnv;
import bagaturchess.search.impl.env.SharedData;

public class SearchersPool {

    private Stack<ISearch> pool = new Stack<ISearch>();

    private int searchersCount = 0;

    private IBoardConfig boardConfig;

    public SearchersPool(IBoardConfig _boardConfig) {
        boardConfig = _boardConfig;
    }

    public ISearch getSearcher(IBitBoard bitboardForSetup, SharedData sharedData) {
        if (searchersCount <= 0) {
            throw new IllegalAccessError("searchersCount=" + searchersCount);
        }
        ISearch result = pool.pop();
        result.setup(bitboardForSetup);
        return result;
    }

    public void releaseSearcher(ISearch searcher) {
        pool.push(searcher);
    }

    void newGame(IBitBoard bitboardForSetup, SharedData sharedData, int threadsCount) {
        searchersCount = threadsCount;
        pool.clear();
        for (int i = 0; i < threadsCount; i++) {
            IBitBoard searcher_bitboard = new Board(bitboardForSetup.toEPD(), boardConfig);
            SearchEnv searchEnv = new SearchEnv(searcher_bitboard, sharedData);
            ISearch searcher = new SearchMTD(searchEnv);
            releaseSearcher(searcher);
        }
    }

    public void dumpSearchers(ISearchMediator mediator) {
        for (int i = 0; i < pool.size(); i++) {
            mediator.dump(pool.get(i).toString());
        }
    }

    void newSearch() {
        for (int i = 0; i < pool.size(); i++) {
            pool.get(i).newSearch();
        }
    }

    void waitSearchersToStop() {
        while (true) {
            synchronized (pool) {
                if (pool.size() == searchersCount) {
                    return;
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
