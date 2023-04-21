package com.esri.gpt.catalog.lucene;

import com.esri.gpt.catalog.discovery.Discoverable;
import com.esri.gpt.catalog.discovery.DiscoveryException;
import com.esri.gpt.catalog.discovery.LogicalClause;
import com.esri.gpt.catalog.discovery.PropertyComparisonType;
import com.esri.gpt.catalog.discovery.PropertyMeaning;
import com.esri.gpt.catalog.discovery.PropertyMeanings;
import com.esri.gpt.catalog.discovery.PropertyValueType;
import com.esri.gpt.catalog.discovery.SpatialClause;
import com.esri.gpt.framework.geometry.Envelope;
import com.esri.gpt.framework.util.DateProxy;
import com.esri.gpt.framework.util.Val;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PrefixFilter;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;

/**package*/
class QueryProvider {

    /** The Logger. */
    private static Logger LOGGER = Logger.getLogger(QueryProvider.class.getName());

    /** fields */
    private String[] fields;

    /** use constant score query */
    private boolean useConstantScoreQuery;

    /** lucene query adapter */
    private LuceneQueryAdapter luceneQueryAdapter;

    /** property meanings */
    private PropertyMeanings meanings;

    /**
   * Creates instance of the provider.
   * @param meanings meaning
   */
    public QueryProvider(String[] fields, boolean useConstantScoreQuery, LuceneQueryAdapter luceneQueryAdapter, PropertyMeanings meanings) {
        if (luceneQueryAdapter == null) {
            throw new IllegalArgumentException("null luceneQueryAdapter.");
        }
        if (meanings == null) {
            throw new IllegalArgumentException("null meanings.");
        }
        this.fields = fields;
        this.useConstantScoreQuery = useConstantScoreQuery;
        this.luceneQueryAdapter = luceneQueryAdapter;
        this.meanings = meanings;
    }

    /**
   * Gets a simple query.
   * @param field field name
   * @param queryText query text
   * @param slop slop
   * @return query or <code>null</code> if query for the particular field is unavailable
   * @throws ParseException if error creating query
   */
    protected Query getFieldQuery(String field, String queryText, int slop) throws ParseException {
        Query q = null;
        PropertyMeaning meaning = resolveMeaning(field);
        if (meaning != null) {
            PropertyComparisonType type = meaning.getComparisonType();
            if (type == PropertyComparisonType.KEYWORD) {
                q = new TermQuery(new Term(field, Val.chkStr(queryText).toLowerCase()));
            }
            if (type == PropertyComparisonType.VALUE && meaning.getValueType() == PropertyValueType.STRING) {
                q = new TermQuery(new Term(field, queryText));
            }
            if (type == PropertyComparisonType.VALUE && meaning.getValueType() == PropertyValueType.LONG) {
                try {
                    LongField lgField = new LongField(field);
                    queryText = lgField.makeValueToQuery(queryText, false, false);
                    q = new TermQuery(new Term(field, queryText));
                } catch (DiscoveryException ex) {
                    throw new ParseException("Error parsing expression: " + ex.getMessage());
                }
            }
            if (type == PropertyComparisonType.VALUE && meaning.getValueType() == PropertyValueType.DOUBLE) {
                try {
                    DoubleField lgField = new DoubleField(field, DoubleField.DEFAULT_PRECISION);
                    queryText = lgField.makeValueToQuery(queryText, false, false);
                    q = new TermQuery(new Term(field, queryText));
                } catch (DiscoveryException ex) {
                    throw new ParseException("Error parsing expression: " + ex.getMessage());
                }
                q = new TermQuery(new Term(field, queryText));
            }
            if (type == PropertyComparisonType.VALUE && meaning.getValueType() == PropertyValueType.TIMESTAMP) {
                try {
                    if (isFullDate(queryText)) {
                        TimestampField tsField = new TimestampField(field);
                        queryText = tsField.makeValueToQuery(queryText, true, false);
                        q = new TermQuery(new Term(field, queryText));
                    } else {
                        q = (new TimestampField(field)).makeRangeQuery(queryText, queryText, true, true);
                    }
                } catch (DiscoveryException ex) {
                    throw new ParseException("Error parsing expression: " + ex.getMessage());
                }
            }
            if (type == PropertyComparisonType.VALUE && meaning.getValueType() == PropertyValueType.GEOMETRY) {
                try {
                    Locator locator = Locator.newInstance();
                    Locator.Candidate bestCandidate = locator.findBestCandidate(locator.find(queryText));
                    if (bestCandidate != null) {
                        double dif = 0.1;
                        BooleanQuery rootQuery = new BooleanQuery();
                        SpatialClause spatialClause = createSpatialClause(meaning, true);
                        spatialClause.getBoundingEnvelope().setMinX(bestCandidate.getLocation()[0] - dif);
                        spatialClause.getBoundingEnvelope().setMinY(bestCandidate.getLocation()[1] - dif);
                        spatialClause.getBoundingEnvelope().setMaxX(bestCandidate.getLocation()[0] + dif);
                        spatialClause.getBoundingEnvelope().setMaxY(bestCandidate.getLocation()[1] + dif);
                        SpatialClauseAdapter spatialClauseAdapter = new SpatialClauseAdapter(getLuceneQueryAdapter());
                        spatialClauseAdapter.adaptSpatialClause(rootQuery, new LogicalClause.LogicalAnd(), spatialClause);
                        q = rootQuery;
                    }
                } catch (Exception ex) {
                    throw new ParseException("Error parsing expression: " + ex.getMessage());
                }
            }
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("QueryProvider.getFieldQuery(" + field + "," + queryText + "," + slop + ") -> " + q);
        }
        return q;
    }

    /**
   * Gets prefix query.
   * @param field field name
   * @param termStr term
   * @return query or <code>null</code> if query for the particular field is unavailable
   * @throws ParseException if error creating query
   */
    protected Query getPrefixQuery(String field, String termStr) throws ParseException {
        Query q = null;
        PropertyMeaning meaning = resolveMeaning(field);
        if (meaning != null) {
            PropertyComparisonType type = meaning.getComparisonType();
            if (type == PropertyComparisonType.KEYWORD) {
                q = newPrefixQuery(field, Val.chkStr(termStr).toLowerCase());
            } else if (type == PropertyComparisonType.TERMS) {
                q = newPrefixQuery(field, Val.chkStr(termStr).toLowerCase());
            } else if (type == PropertyComparisonType.VALUE) {
                q = newPrefixQuery(field, Val.chkStr(termStr));
            } else {
                q = newPrefixQuery(field, Val.chkStr(termStr));
            }
        } else if (field != null) {
            q = newPrefixQuery(field, Val.chkStr(termStr));
        }
        if (q == null) {
            List clauses = new ArrayList();
            for (int i = 0; i < getFields().length; i++) {
                clauses.add(new BooleanClause(getPrefixQuery(getFields()[i], termStr), BooleanClause.Occur.SHOULD));
            }
            q = newBooleanQuery(clauses, true);
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("QueryProvider.getPrefixQuery(" + field + "," + termStr + ") -> " + q);
        }
        return q;
    }

    /**
   * Gets range query.
   * @param field field name
   * @param part1 first part of the range
   * @param part2 second part of the range
   * @param inclusive <code>true</code> for inclusive search
   * @return query or <code>null</code> if query for the particular field is unavailable
   * @throws ParseException if error creating query
   */
    protected Query getRangeQuery(String field, String part1, String part2, boolean inclusive) throws ParseException {
        Query q = null;
        PropertyMeaning meaning = resolveMeaning(field);
        if (meaning != null) {
            PropertyComparisonType type = meaning.getComparisonType();
            if (type == PropertyComparisonType.KEYWORD) {
                q = newRangeQuery(field, Val.chkStr(part1).toLowerCase(), Val.chkStr(part2).toLowerCase(), inclusive);
            }
            if (type == PropertyComparisonType.VALUE && meaning.getValueType() == PropertyValueType.STRING) {
                q = newRangeQuery(field, part1, part2, inclusive);
            }
            if (type == PropertyComparisonType.VALUE && meaning.getValueType() == PropertyValueType.GEOMETRY) {
                try {
                    BooleanQuery rootQuery = new BooleanQuery();
                    SpatialClause spatialClause = createSpatialClause(meaning, inclusive);
                    parseEnvelope(spatialClause.getBoundingEnvelope(), part1, part2);
                    SpatialClauseAdapter spatialClauseAdapter = new SpatialClauseAdapter(getLuceneQueryAdapter());
                    spatialClauseAdapter.adaptSpatialClause(rootQuery, new LogicalClause.LogicalAnd(), spatialClause);
                    q = rootQuery;
                } catch (DiscoveryException ex) {
                    throw new ParseException("Error parsing expression: " + ex.getMessage());
                }
            }
            if (type == PropertyComparisonType.VALUE && meaning.getValueType() == PropertyValueType.TIMESTAMP) {
                try {
                    q = (new TimestampField(field)).makeRangeQuery(part1, part2, inclusive, inclusive);
                } catch (DiscoveryException ex) {
                    throw new ParseException("Error parsing expression: " + ex.getMessage());
                }
            }
            if (type == PropertyComparisonType.VALUE && meaning.getValueType() == PropertyValueType.LONG) {
                try {
                    q = (new LongField(field)).makeRangeQuery(part1, part2, inclusive, inclusive);
                } catch (DiscoveryException ex) {
                    throw new ParseException("Error parsing expression: " + ex.getMessage());
                }
            }
            if (type == PropertyComparisonType.VALUE && meaning.getValueType() == PropertyValueType.DOUBLE) {
                try {
                    q = (new LongField(field)).makeRangeQuery(part1, part2, inclusive, inclusive);
                } catch (DiscoveryException ex) {
                    throw new ParseException("Error parsing expression: " + ex.getMessage());
                }
            }
        } else if (field != null) {
            q = newRangeQuery(field, part1, part2, inclusive);
        }
        if (q == null) {
            List clauses = new ArrayList();
            for (int i = 0; i < getFields().length; i++) {
                clauses.add(new BooleanClause(getRangeQuery(getFields()[i], part1, part2, inclusive), BooleanClause.Occur.SHOULD));
            }
            q = newBooleanQuery(clauses, true);
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("QueryProvider.getRangeQuery(" + field + "," + part1 + "," + part2 + "," + inclusive + ") -> " + q);
        }
        return q;
    }

    /**
   * Gets widcard query.
   * @param field field name
   * @param termStr term
   * @return query or <code>null</code> if query for the particular field is unavailable
   * @throws ParseException if error creating query
   */
    protected Query getWildcardQuery(String field, String termStr) throws ParseException {
        Query q = null;
        PropertyMeaning meaning = resolveMeaning(field);
        if (meaning != null) {
            PropertyComparisonType type = meaning.getComparisonType();
            if (type == PropertyComparisonType.KEYWORD) {
                q = new WildcardQuery(new Term(field, Val.chkStr(termStr).toLowerCase()));
            }
            if (type == PropertyComparisonType.VALUE) {
                q = new WildcardQuery(new Term(field, termStr));
            }
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("QueryProvider.getWildcardQuery(" + field + "," + termStr + ") -> " + q);
        }
        return q;
    }

    /**
   * Gets fuzzy query.
   * @param field field name
   * @param termStr term
   * @param minSimilarity minimal similarity
   * @return query or <code>null</code> if query for the particular field is unavailable
   * @throws ParseException if error creating query
   */
    protected Query getFuzzyQuery(String field, String termStr, float minSimilarity) throws ParseException {
        Query q = null;
        PropertyMeaning meaning = resolveMeaning(field);
        if (meaning != null) {
            PropertyComparisonType type = meaning.getComparisonType();
            if (type == PropertyComparisonType.KEYWORD) {
                q = new FuzzyQuery(new Term(field, Val.chkStr(termStr).toLowerCase()));
            }
            if (type == PropertyComparisonType.VALUE) {
                q = new FuzzyQuery(new Term(field, termStr));
            }
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("QueryProvider.getFuzzyQuery(" + field + "," + termStr + "," + minSimilarity + ") -> " + q);
        }
        return q;
    }

    /**
   * Creates new prefix query. Depending on {@link getUseConstantScoreQuery()}
   * it's either {@link org.apache.lucene.search.ConstantScoreQuery} with
   * {@link org.apache.lucene.search.RangeFilter} or just
   * {@link org.apache.lucene.search.RangeQuery}.
   * @param term term
   * @return prefix query
   */
    private Query newRangeQuery(String fieldName, String lowerTerm, String upperTerm, boolean inclusive) {
        return new TermRangeQuery(fieldName, lowerTerm, upperTerm, inclusive, inclusive);
    }

    /**
   * Creates new prefix query. Depending on {@link getUseConstantScoreQuery()}
   * it's either {@link org.apache.lucene.search.ConstantScoreQuery} with
   * {@link org.apache.lucene.search.PrefixFilter} or just
   * {@link org.apache.lucene.search.PrefixQuery}.
   * @param term term
   * @return prefix query
   */
    private Query newPrefixQuery(String fieldName, String term) {
        return this.getUseConstantScoreQuery() ? new ConstantScoreQuery(new PrefixFilter(new Term(fieldName, term))) : new PrefixQuery(new Term(fieldName, term));
    }

    /**
   * Gets boolean query. Identical to the method {@link org.apache.lucene.queryParser.MultiFieldQueryParser#getBooleanQuery}
   * @param clauses list of clauses
   * @param disableCoord disables {@link org.apache.lucene.search.Similarity#coord(int,int)} in scoring
   * @return
   * @throws ParseException
   */
    private Query newBooleanQuery(List clauses, boolean disableCoord) throws ParseException {
        if (clauses.size() == 0) {
            return null;
        }
        BooleanQuery query = new BooleanQuery(disableCoord);
        for (int i = 0; i < clauses.size(); i++) {
            query.add((BooleanClause) clauses.get(i));
        }
        return query;
    }

    /**
   * Checks if provided date is a full date stored in the index. Full date is
   * a date of milliseconds resolution.
   * @param queryText possibly a full date
   * @return <code>true</code> if this is a full date.
   */
    private boolean isFullDate(String queryText) {
        try {
            queryText = Val.chkStr(queryText);
            long lngDate = DateTools.stringToTime(queryText);
            return queryText.matches("[0-9]+") && queryText.length() >= DateTools.timeToString(lngDate, DateTools.Resolution.MILLISECOND).length();
        } catch (java.text.ParseException ex) {
            return false;
        }
    }

    /**
   * Makes the value to query.
   * <br/>The value to query is derived from timestampToIndexableString().
   * @param value to input query value
   * @param isLowerBoundary true if this is a lower boundary of a range query
   * @param isUpperBoundary true if this is a upper boundary of a range query
   * @param inclusive <code>true</code> to make inclusive query
   * @return the value to query
   * @throws DiscoveryException if the supplied value cannot be converted
   */
    private String makeValueToQuery(String value, boolean isLowerBoundary, boolean isUpperBoundary, boolean inclusive) {
        DateProxy proxy = new DateProxy();
        proxy.setDate(value);
        if (!proxy.getIsValid()) {
            throw new IllegalArgumentException("Invalid Timestamp: " + value + ", use for yyyy-mm-dd hh:mm:ss.fff");
        }
        Timestamp tsValue = null;
        if (isLowerBoundary) {
            tsValue = inclusive ? proxy.asFromTimestamp() : proxy.asFromTimestampExcl();
        } else if (isUpperBoundary) {
            tsValue = inclusive ? proxy.asToTimestamp() : proxy.asToTimestampExcl();
        } else {
            tsValue = inclusive ? proxy.asFromTimestamp() : proxy.asFromTimestampExcl();
        }
        if (tsValue == null) {
            return null;
        }
        if (isLowerBoundary) {
            LOGGER.finer("Lower boundary timestamp to query: " + tsValue);
        } else if (isUpperBoundary) {
            LOGGER.finer("Upper boundary timestamp to query: " + tsValue);
        } else {
            LOGGER.finer("Timestamp to query: " + tsValue);
        }
        return TimestampField.timestampToIndexableString(tsValue);
    }

    /**
   * Parses corner.
   * @param cornerDef corner definition
   * @param extremeValue array of two extreme values for that corner
   * @return array of coordinates of the corner
   * @throws ParseException if parsing fails
   */
    private double[] parseCorner(String cornerDef, double[] extremeValue) throws ParseException {
        double[] corner = new double[] { extremeValue[0], extremeValue[1] };
        String[] sCoords = Val.chkStr(cornerDef).split(",");
        if (sCoords.length == 2) {
            for (int i = 0; i < 2; i++) {
                if (sCoords[i].trim().equals("*")) {
                } else {
                    try {
                        corner[i] = Double.parseDouble(sCoords[i].trim());
                    } catch (NumberFormatException ex) {
                        throw new ParseException("Invalid envelope corner definition: " + cornerDef);
                    }
                }
            }
        } else if (sCoords.length == 1) {
            if (sCoords[0].trim().equals("*")) {
            } else {
                throw new ParseException("Invalid envelope corner definition: " + cornerDef);
            }
        } else {
            throw new ParseException("Invalid envelope corner definition: " + cornerDef);
        }
        return corner;
    }

    /**
   * Parses envelope.
   * @param envelope envelope to store information
   * @param part1 left part of the range query
   * @param part2 right part of the range query
   * @throws ParseException if parsing envelope fails
   */
    private void parseEnvelope(Envelope envelope, String part1, String part2) throws ParseException {
        double[] loverLeftCorner = parseCorner(part1, new double[] { -180, -90 });
        double[] upperRightCorner = parseCorner(part2, new double[] { +180, +90 });
        envelope.setMinX(loverLeftCorner[0]);
        envelope.setMinY(loverLeftCorner[1]);
        envelope.setMaxX(upperRightCorner[0]);
        envelope.setMaxY(upperRightCorner[1]);
    }

    /**
   * Creates spatial clause.
   * @param meaning property meaning
   * @param inclusive <code>true</code> to have inclusive clause
   * @return clause
   */
    private SpatialClause createSpatialClause(PropertyMeaning meaning, boolean inclusive) {
        SpatialClause spatialClause = inclusive ? new SpatialClause.GeometryBBOXIntersects() : new SpatialClause.GeometryIsWithin();
        spatialClause.setSrsName("4326");
        Discoverable target = new Discoverable(meaning.getName());
        target.setMeaning(meaning);
        target.setStoreable(new GeometryProperty(meaning.getName()));
        spatialClause.setTarget(target);
        return spatialClause;
    }

    /**
   * Resolves meaning.
   * @param fieldName field name
   * @return meaning proxy
   */
    private PropertyMeaning resolveMeaning(String fieldName) {
        PropertyMeaning meaning = getMeanings().get(fieldName);
        if (meaning == null) {
            Discoverable discoverable = getMeanings().getAllAliased().get(fieldName);
            if (discoverable != null) {
                meaning = discoverable.getMeaning();
            }
        }
        return meaning;
    }

    /**
   * @return the fields
   */
    protected String[] getFields() {
        return fields;
    }

    /**
   * @return the useConstantScoreQuery
   */
    protected boolean getUseConstantScoreQuery() {
        return useConstantScoreQuery;
    }

    /**
   * @return the luceneQueryAdapter
   */
    protected LuceneQueryAdapter getLuceneQueryAdapter() {
        return luceneQueryAdapter;
    }

    /**
   * @return the meanings
   */
    protected PropertyMeanings getMeanings() {
        return meanings;
    }
}
