package com.rapidminer.operator.web.services.twitter;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.io.AbstractExampleSource;
import com.rapidminer.operator.ports.metadata.AttributeMetaData;
import com.rapidminer.operator.ports.metadata.ExampleSetMetaData;
import com.rapidminer.operator.ports.metadata.MDInteger;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.tools.Ontology;

public class SearchTwitterOperator extends AbstractExampleSource {

    public static final String PARAMETER_QUERY = "query";

    public static final String ATTRIBUTE_ID = "Id";

    public static final String ATTRIBUTE_CREATED_AT = "Created-At";

    public static final String ATTRIBUTE_FROM_USER = "From-User";

    public static final String ATTRIBUTE_FROM_USER_ID = "From-User-Id";

    public static final String ATTRIBUTE_TO_USER = "To-User";

    public static final String ATTRIBUTE_TO_USER_ID = "To-User-Id";

    public static final String ATTRIBUTE_LANGUAGE = "Language";

    public static final String ATTRIBUTE_SOURCE = "Source";

    public static final String ATTRIBUTE_GEO_LOCATION_LATITUDE = "Geo-Location-Latitude";

    public static final String ATTRIBUTE_GEO_LOCATION_LONGITUDE = "Geo-Location-Longitude";

    public static final String ATTRIBUTE_TEXT = "Text";

    private static final int MAX_RESULTS_PER_PAGE = 100;

    public SearchTwitterOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public ExampleSetMetaData getGeneratedMetaData() {
        ExampleSetMetaData emd = new ExampleSetMetaData();
        emd.addAttribute(new AttributeMetaData(ATTRIBUTE_ID, Ontology.NUMERICAL, Attributes.ID_NAME));
        AttributeMetaData amd = new AttributeMetaData(ATTRIBUTE_CREATED_AT, Ontology.DATE_TIME);
        emd.addAttribute(amd);
        amd = new AttributeMetaData(ATTRIBUTE_FROM_USER, Ontology.NOMINAL);
        emd.addAttribute(amd);
        amd = new AttributeMetaData(ATTRIBUTE_FROM_USER_ID, Ontology.INTEGER);
        emd.addAttribute(amd);
        amd = new AttributeMetaData(ATTRIBUTE_TO_USER, Ontology.NOMINAL);
        amd.setNumberOfMissingValues(new MDInteger());
        emd.addAttribute(amd);
        amd = new AttributeMetaData(ATTRIBUTE_TO_USER_ID, Ontology.INTEGER);
        amd.setNumberOfMissingValues(new MDInteger());
        emd.addAttribute(amd);
        amd = new AttributeMetaData(ATTRIBUTE_LANGUAGE, Ontology.NOMINAL);
        amd.setNumberOfMissingValues(new MDInteger());
        emd.addAttribute(amd);
        amd = new AttributeMetaData(ATTRIBUTE_SOURCE, Ontology.NOMINAL);
        amd.setNumberOfMissingValues(new MDInteger());
        emd.addAttribute(amd);
        amd = new AttributeMetaData(ATTRIBUTE_TEXT, Ontology.STRING);
        amd.setNumberOfMissingValues(new MDInteger());
        emd.addAttribute(amd);
        amd = new AttributeMetaData(ATTRIBUTE_GEO_LOCATION_LATITUDE, Ontology.NUMERICAL);
        amd.setNumberOfMissingValues(new MDInteger());
        emd.addAttribute(amd);
        amd = new AttributeMetaData(ATTRIBUTE_GEO_LOCATION_LONGITUDE, Ontology.NUMERICAL);
        amd.setNumberOfMissingValues(new MDInteger());
        emd.addAttribute(amd);
        emd.setNumberOfExamples(new MDInteger());
        return emd;
    }

    @Override
    public ExampleSet createExampleSet() throws OperatorException {
        List<Attribute> attributes = new LinkedList<Attribute>();
        Attribute idAttribute = AttributeFactory.createAttribute(ATTRIBUTE_ID, Ontology.INTEGER);
        attributes.add(idAttribute);
        Attribute dateAttribute = AttributeFactory.createAttribute(ATTRIBUTE_CREATED_AT, Ontology.DATE_TIME);
        attributes.add(dateAttribute);
        Attribute fromUserAttribute = AttributeFactory.createAttribute(ATTRIBUTE_FROM_USER, Ontology.NOMINAL);
        attributes.add(fromUserAttribute);
        Attribute fromUserIdAttribute = AttributeFactory.createAttribute(ATTRIBUTE_FROM_USER_ID, Ontology.INTEGER);
        attributes.add(fromUserIdAttribute);
        Attribute toUserAttribute = AttributeFactory.createAttribute(ATTRIBUTE_TO_USER, Ontology.NOMINAL);
        attributes.add(toUserAttribute);
        Attribute toUserIdAttribute = AttributeFactory.createAttribute(ATTRIBUTE_TO_USER_ID, Ontology.INTEGER);
        attributes.add(toUserIdAttribute);
        Attribute languageAttribute = AttributeFactory.createAttribute(ATTRIBUTE_LANGUAGE, Ontology.NOMINAL);
        attributes.add(languageAttribute);
        Attribute sourceAttribute = AttributeFactory.createAttribute(ATTRIBUTE_SOURCE, Ontology.NOMINAL);
        attributes.add(sourceAttribute);
        Attribute textAttribute = AttributeFactory.createAttribute(ATTRIBUTE_TEXT, Ontology.NOMINAL);
        attributes.add(textAttribute);
        Attribute latitudeAttribute = AttributeFactory.createAttribute(ATTRIBUTE_GEO_LOCATION_LATITUDE, Ontology.NUMERICAL);
        attributes.add(latitudeAttribute);
        Attribute longitudeAttribute = AttributeFactory.createAttribute(ATTRIBUTE_GEO_LOCATION_LONGITUDE, Ontology.NUMERICAL);
        attributes.add(longitudeAttribute);
        MemoryExampleTable table = new MemoryExampleTable(attributes);
        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        try {
            Query query = new Query(getParameterAsString(PARAMETER_QUERY));
            query.setRpp(MAX_RESULTS_PER_PAGE);
            int page = 1;
            List<Tweet> tweets;
            do {
                query.setPage(page);
                tweets = twitter.search(query).getTweets();
                for (Tweet tweet : tweets) {
                    double[] data = new double[attributes.size()];
                    data[0] = tweet.getId();
                    Date date = tweet.getCreatedAt();
                    data[1] = date == null ? Double.NaN : date.getTime();
                    String fromUser = tweet.getFromUser();
                    data[2] = fromUser == null ? Double.NaN : fromUserAttribute.getMapping().mapString(fromUser);
                    data[3] = tweet.getFromUserId();
                    String toUser = tweet.getToUser();
                    data[4] = toUser == null ? Double.NaN : toUserAttribute.getMapping().mapString(toUser);
                    data[5] = tweet.getToUserId();
                    String language = tweet.getIsoLanguageCode();
                    data[6] = language == null ? Double.NaN : languageAttribute.getMapping().mapString(language);
                    String source = tweet.getSource();
                    data[7] = source == null ? Double.NaN : sourceAttribute.getMapping().mapString(source);
                    String text = tweet.getText();
                    data[8] = text == null ? Double.NaN : textAttribute.getMapping().mapString(text);
                    GeoLocation geoLocation = tweet.getGeoLocation();
                    if (geoLocation == null) {
                        data[9] = Double.NaN;
                        data[10] = Double.NaN;
                    } else {
                        data[9] = geoLocation.getLatitude();
                        data[10] = geoLocation.getLongitude();
                    }
                    table.addDataRow(new DoubleArrayDataRow(data));
                }
                page++;
            } while (page <= 15 && tweets.size() >= 100);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return table.createExampleSet();
    }

    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = new LinkedList<ParameterType>();
        types.add(new ParameterTypeString(PARAMETER_QUERY, "The term that should be searched.", false, false));
        return types;
    }
}
