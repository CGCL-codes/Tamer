package puppy.eval;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map.Entry;
import puppy.eval.logloaders.QueryPairsLoader;
import util.evaluation.StatsGenerator;
import util.ranker.RankingService;
import algorithms.models.QueryTagsModel;
import fileloaders.extractors.AOLQueryPairsExtractor;
import fileloaders.extractors.LineFieldExtractor;
import fileloaders.extractors.YahooQueryPairsExtractor;
import fileloaders.filters.AOLQueryPairFilter;
import fileloaders.filters.TextLineFilter;
import fileloaders.filters.YahooQueryPairFilter;

public class RunSuggestions {

    private Hashtable<String, Hashtable<String, ArrayList<String>>> gold = new Hashtable<String, Hashtable<String, ArrayList<String>>>();

    private QueryPairsLoader loader = null;

    private TextLineFilter filter = null;

    private LineFieldExtractor extractor = null;

    private HashSet<String> previous_queries = new HashSet<String>();

    public RunSuggestions(String path_logs, String logs_type, HashSet<String> valid_ages, boolean onlyClicks, int duration, boolean onlyManual) {
        if (logs_type.equals("aol")) {
            filter = new AOLQueryPairFilter(valid_ages, onlyClicks, duration);
            extractor = new AOLQueryPairsExtractor();
        } else {
            filter = new YahooQueryPairFilter(valid_ages, onlyClicks, duration, onlyManual);
            extractor = new YahooQueryPairsExtractor();
        }
        loader = new QueryPairsLoader(path_logs, filter, extractor);
        gold = loader.loadQueryPairs();
    }

    public void rankQueries(RankingService service, int limit) {
        Enumeration<String> types = gold.keys();
        while (types.hasMoreElements()) {
            Hashtable<String, ArrayList<String>> temporal = gold.get(types.nextElement());
            Enumeration<String> queries = temporal.keys();
            while (queries.hasMoreElements()) {
                String query = queries.nextElement();
                if (previous_queries.contains(query)) {
                    continue;
                }
                previous_queries.add(query);
                ArrayList<Entry<String, Float>> ranked = service.rankQuery(query, limit);
                int rank = 1;
                int index = 0;
                while (ranked != null && index < ranked.size() && rank <= limit) {
                    Entry<String, Float> suggestion = ranked.get(index);
                    index++;
                    String suggested = suggestion.getKey();
                    String temp = query + "\t" + suggested + "\t" + rank + "\n";
                    rank++;
                }
            }
        }
    }

    public void rankQueries(RankingService service, QueryTagsModel model, int limit) {
        Enumeration<String> types = gold.keys();
        while (types.hasMoreElements()) {
            Hashtable<String, ArrayList<String>> temporal = gold.get(types.nextElement());
            Enumeration<String> queries = temporal.keys();
            while (queries.hasMoreElements()) {
                String query = queries.nextElement();
                if (previous_queries.contains(query)) {
                    continue;
                }
                previous_queries.add(query);
                Hashtable<String, Float> q = null;
                ArrayList<Entry<String, Float>> ranked = null;
                if (model != null && model.getQueryHash() != null && model.getQueryHash().containsKey(query)) {
                    q = model.getQueryHash().get(query);
                    ranked = service.rankQuery(query, q, limit);
                } else {
                    ranked = service.rankQuery(query, limit);
                }
                int rank = 1;
                int index = 0;
                while (ranked != null && index < ranked.size() && rank <= limit) {
                    Entry<String, Float> suggestion = ranked.get(index);
                    index++;
                    String suggested = suggestion.getKey();
                    String temp = query + "\t" + suggested + "\t" + rank + "\n";
                    System.out.println(query + "\t" + suggestion.getKey() + "\t" + rank);
                    rank++;
                }
            }
        }
    }

    public Hashtable<String, Hashtable<String, ArrayList<String>>> getGoldStandard() {
        return gold;
    }

    public static void main(String[] args) throws IOException {
        String prefix_data = "/Users/sergioduarte/projects/data/";
        String evaluation_path = prefix_data + "results/aol_query_reformulations/all_long_click_kids.txt";
        String in_path = prefix_data + "results/query_and_tags_sorted_aol/query_and_tags_sorted_aol.txt";
        String path = prefix_data + "results/query_suggestions_google";
    }
}
