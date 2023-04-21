package pt.utl.ist.lucene.treceval.handlers.topics.output.impl;

import pt.utl.ist.lucene.treceval.handlers.topics.output.Topic;
import pt.utl.ist.lucene.treceval.handlers.topics.output.OutputFormat;

/**
 * @author Jorge Machado
 * @date 26/Jan/2010
 * @time 23:34:17
 * @email machadofisher@gmail.com
 */
public class SimpleTopic implements Topic {

    private String id;

    private String query;

    private OutputFormat outputFormat;

    public SimpleTopic(String id) {
        this.id = id;
    }

    public SimpleTopic(String id, OutputFormat outputFormat) {
        this.id = id;
        this.outputFormat = outputFormat;
    }

    public SimpleTopic(String id, String query, OutputFormat outputFormat) {
        this.id = id;
        this.query = query;
        this.outputFormat = outputFormat;
    }

    public String getIdentifier() {
        return id;
    }

    public String getQuery() {
        return query;
    }

    public OutputFormat getOutputFormat() {
        return outputFormat;
    }
}
