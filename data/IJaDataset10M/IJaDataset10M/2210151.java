package com.idna.trace.domain;

import java.io.Serializable;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.collections.Bag;
import com.idna.trace.utils.parameters.consumer.ParametersConsumer;

public class Response<Content> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ParametersConsumer parsedParameters;

    private Content content;

    private Bag stats;

    private ResponseMetaData metaData;

    private final String requestSources;

    public Response(ParametersConsumer parsedParameters, Content content, Bag stats, ResponseMetaData responseMetaData) {
        this.parsedParameters = parsedParameters;
        this.content = content;
        this.stats = stats;
        this.metaData = responseMetaData;
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry<String, String> source : parsedParameters.getCommonParameters().getSourceNamesMap().entrySet()) {
            stringBuilder.append(source.getKey());
            stringBuilder.append("-");
        }
        if (parsedParameters.getCommonParameters().getSourceNamesMap().size() > 0) stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        this.requestSources = stringBuilder.toString();
    }

    public String getRequestedSources() {
        return requestSources;
    }

    public ParametersConsumer getParsedParameters() {
        return parsedParameters;
    }

    public void setParsedParameters(ParametersConsumer parsedParameters) {
        this.parsedParameters = parsedParameters;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Bag getStats() {
        return stats;
    }

    public void setStats(Bag stats) {
        this.stats = stats;
    }

    public ResponseMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(ResponseMetaData metaData) {
        this.metaData = metaData;
    }
}
