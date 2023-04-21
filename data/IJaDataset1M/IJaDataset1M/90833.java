package pub.utils.hitgenerate;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/** Performs an exhaustive search through our searchSpace, following
 * states across transition edges.  We simulate nondeterminism by
 * keeping an explicit stack of things to do next.  As we hit success
 * states, we store these in a List, and then return an iterator to
 * that list.
 */
public class IterativeSearch {

    public static List search(SearchSpace searchSpace, List edges) {
        getLogger().debug("Search initializing with edges=" + edges + " converted to " + Cons.fromList(edges));
        Stack eventStack = new Stack();
        SearchEvent initialEvent = SearchEvent.makeSearchFromStateEvent(searchSpace.initialState(), Cons.fromList(edges));
        eventStack.push(initialEvent);
        return search(searchSpace, eventStack);
    }

    /**
     *
     */
    private static List search(SearchSpace searchSpace, Stack eventStack) {
        getLogger().debug("Search starting");
        SearchState bestSoFar = null;
        List results = new ArrayList();
        while (eventStack.empty() == false) {
            SearchEvent event = (SearchEvent) eventStack.pop();
            getLogger().debug("Current event is: " + event);
            if (event.getType().equals("BEST")) {
                if (bestSoFar != null) {
                    results.add(bestSoFar);
                    bestSoFar = null;
                }
            } else if (event.getType().equals("SEARCH")) {
                SearchState state = event.getState();
                Cons edges = event.getEdges();
                if (searchSpace.isSuccessState(state)) {
                    bestSoFar = state;
                }
                if (edges != null) {
                    eventStack.push(SearchEvent.makeReturnBestEvent());
                    Iterator neighborIter = searchSpace.neighbors(state, edges.first).iterator();
                    while (neighborIter.hasNext()) {
                        SearchEvent newEvent = SearchEvent.makeSearchFromStateEvent((SearchState) neighborIter.next(), edges.rest);
                        eventStack.push(newEvent);
                    }
                }
            } else {
                throw new RuntimeException("Illegal event type");
            }
        }
        getLogger().debug("All done!");
        getLogger().debug(results);
        return results;
    }

    private static org.apache.log4j.Logger getLogger() {
        return pub.utils.Log.getLogger(pub.utils.hitgenerate.IterativeSearch.class);
    }

    /** Variant record that represents a search event.

    A search event is either a search from a node, or a request to get
    the "best" result from a search so far.
    */
    private static class SearchEvent {

        private String type;

        private SearchState state;

        private Cons edges;

        public static String BEST = "BEST";

        public static String SEARCH = "SEARCH";

        static SearchEvent bestEvent;

        static {
            bestEvent = new SearchEvent(BEST);
        }

        private SearchEvent(String type) {
            this.type = type;
        }

        private SearchEvent(String type, SearchState state, Cons edges) {
            this.type = type;
            this.state = state;
            this.edges = edges;
        }

        public static SearchEvent makeReturnBestEvent() {
            return bestEvent;
        }

        public static SearchEvent makeSearchFromStateEvent(SearchState state, Cons edges) {
            return new SearchEvent(SEARCH, state, edges);
        }

        public String toString() {
            if (type.equals("BEST")) {
                return "SearchEvent(BEST)";
            } else {
                return "SearchEvent(SEARCH, " + state + ", " + edges + ")";
            }
        }

        public String getType() {
            return type;
        }

        public SearchState getState() {
            return state;
        }

        public Cons getEdges() {
            return edges;
        }
    }
}
