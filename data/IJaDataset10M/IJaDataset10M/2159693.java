package fileloaders.extractors;

import fileloaders.fieldpositions.YahooQueryPairsFields;

public class YahooQueryPairsExtractor implements LineFieldExtractor, YahooQueryPairsFields {

    @Override
    public String getQuery(String[] t) {
        return new String(t[this._eval_query]);
    }

    @Override
    public String getReformulation(String[] t) {
        return new String(t[this._eval_query_reformulation]);
    }

    @Override
    public String getAge(String[] t) {
        return new String(t[this._eval_bucket]);
    }
}
