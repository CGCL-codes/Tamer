package cb.recommender.base.recommender.support.contentBased;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import cb.recommender.base.datamodel.CBDataModel;
import cb.recommender.base.recommender.support.contentBased.AttributesParser.AttributeType;
import cb.recommender.base.recommender.support.contentBased.xmlClasses.Filter;
import cb.recommender.base.recommender.support.contentBased.xmlClasses.FilterRules;
import cb.recommender.base.recommender.support.contentBased.xmlClasses.GroupFilters;
import com.uplexis.idealize.base.exceptions.IdealizeInputException;

public class FilterRulesParser {

    /**
	 * Object that represent the data. Used to check the rules.
	 */
    private CBDataModel dataModel;

    /**
	 * Object used to represent the configuration rules used to recommend and defined at a xml file.
	 */
    private FilterRules filterRules;

    /**
	 * Possible FilterTypes.
	 */
    private enum FilterType {

        equals, notequals, in, notin, greater, greaterequals, lesser, lesserequals, between, threshold
    }

    ;

    /**
	 * HashMap to access the defined FilterTypes.
	 */
    private HashMap<String, FilterType> filterTypes;

    /**
	 * Possible Relation values.
	 */
    private enum Relation {

        or, and
    }

    /**
	 * HashMap to access the defined Relation values.
	 */
    private HashMap<String, Relation> relation;

    /**
	 * Object user to parse the attributes.
	 */
    private AttributesParser attributesParser;

    /**
	 * Constructor of a FilterRulesParser.
	 * @param dataModel
	 * @param xmlFile
	 * @throws IdealizeInputException
	 */
    public FilterRulesParser(CBDataModel dataModel, FilterRules filterRules, AttributesParser attributesParser) throws IdealizeInputException {
        super();
        this.dataModel = dataModel;
        this.filterRules = filterRules;
        this.attributesParser = attributesParser;
    }

    /**
	 * Generate a BooleanQuery to apply the filter rules.
	 * @param referenceItemID
	 * @return BooleanQuery object to apply the filter rules.
	 * @throws IdealizeInputException
	 */
    public BooleanQuery getBooleanQueryFilter(long referenceItemID) throws IdealizeInputException {
        int referenceDocID = dataModel.getDocIDOf(referenceItemID);
        Document referenceDoc = dataModel.get(referenceDocID);
        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("id", String.valueOf(referenceItemID))), BooleanClause.Occur.MUST_NOT);
        if (filterRules != null) {
            Iterator<Filter> filterIt = filterRules.getFilter().iterator();
            while (filterIt.hasNext()) {
                parseFilter(query, filterIt.next(), referenceDoc);
            }
            Iterator<GroupFilters> groupFilterIt = filterRules.getGroupFilters().iterator();
            while (groupFilterIt.hasNext()) {
                parseGroupFilter(query, groupFilterIt.next(), referenceDoc);
            }
        } else {
            query.add(NumericRangeQuery.newIntRange("id", 0, 9, true, true), BooleanClause.Occur.MUST);
        }
        return query;
    }

    /**
	 * Parse the defined filter rules to a BooleanQuery object.
	 * @param query
	 * @param filter
	 * @param referenceDoc
	 * @throws IdealizeInputException
	 */
    private void parseFilter(BooleanQuery query, Filter filter, Document referenceDoc) throws IdealizeInputException {
        if (filter == null) return;
        BigInteger attributeId = filter.getAttributeId();
        String attributeName = attributesParser.getAttributeName(attributeId);
        String attributeValue = referenceDoc.get(attributesParser.getAttributeName(attributeId));
        AttributeType atributeType = attributesParser.getAttributeType(attributeId);
        if (attributeValue == null || attributeValue.isEmpty() || attributeName == null || attributeName.isEmpty()) {
            throw new IdealizeInputException(String.format("FilterRulesParser.parseFilter: Invalid attribute id %s in xml file.", attributeId));
        }
        StringTokenizer values = new StringTokenizer(filter.getValue(), ";");
        FilterType filterType = getFilterTypeOf(filter.getType());
        Query addQuery;
        boolean equals = false;
        switch(filterType) {
            case equals:
                addQuery = new TermQuery(new Term(attributeName, attributeValue));
                query.add(addQuery, BooleanClause.Occur.MUST);
                break;
            case notequals:
                addQuery = new TermQuery(new Term(attributeName, attributeValue));
                query.add(addQuery, BooleanClause.Occur.MUST_NOT);
                break;
            case in:
                if (filter.getValue().contains(attributeValue)) {
                    BooleanQuery queryIn = new BooleanQuery();
                    while (values.hasMoreTokens()) {
                        addQuery = new TermQuery(new Term(attributeName, values.nextToken()));
                        queryIn.add(addQuery, BooleanClause.Occur.SHOULD);
                    }
                    query.add(queryIn, BooleanClause.Occur.MUST);
                }
                break;
            case notin:
                if (!filter.getValue().contains(attributeValue)) {
                    BooleanQuery queryNotIn = new BooleanQuery();
                    while (values.hasMoreTokens()) {
                        addQuery = new TermQuery(new Term(attributeName, values.nextToken()));
                        queryNotIn.add(addQuery, BooleanClause.Occur.SHOULD);
                    }
                    query.add(queryNotIn, BooleanClause.Occur.MUST_NOT);
                }
                break;
            case greaterequals:
                equals = true;
            case greater:
                switch(atributeType) {
                    case string:
                        addQuery = new TermRangeQuery(attributeName, attributeValue, null, equals, false);
                        query.add(addQuery, BooleanClause.Occur.MUST);
                        break;
                    case integer:
                        addQuery = NumericRangeQuery.newIntRange(attributeName, Integer.parseInt(attributeValue), null, equals, false);
                        query.add(addQuery, BooleanClause.Occur.MUST);
                        break;
                    case decimal:
                        addQuery = NumericRangeQuery.newDoubleRange(attributeName, Double.parseDouble(attributeValue), null, equals, false);
                        query.add(addQuery, BooleanClause.Occur.MUST);
                        break;
                    case datetime:
                    default:
                        throw new IdealizeInputException(String.format("FilterRulesParser.parseFilter: Invalid attribute type '%s' for the greater(-equals) filter-rule in xml file.", filter.getType()));
                }
                break;
            case lesserequals:
                equals = true;
            case lesser:
                switch(atributeType) {
                    case string:
                        addQuery = new TermRangeQuery(attributeName, null, attributeValue, false, equals);
                        query.add(addQuery, BooleanClause.Occur.MUST);
                        break;
                    case integer:
                        addQuery = NumericRangeQuery.newIntRange(attributeName, Integer.parseInt(attributeValue), null, false, equals);
                        query.add(addQuery, BooleanClause.Occur.MUST);
                        break;
                    case decimal:
                        addQuery = NumericRangeQuery.newDoubleRange(attributeName, Double.parseDouble(attributeValue), null, false, equals);
                        query.add(addQuery, BooleanClause.Occur.MUST);
                        break;
                    case datetime:
                    default:
                        throw new IdealizeInputException(String.format("FilterRulesParser.parseFilter: Invalid attribute type '%s' for the lesser(-equals) filter-rule in xml file.", filter.getType()));
                }
                break;
            case between:
                if (values.countTokens() != 2) {
                    throw new IdealizeInputException(String.format("FilterRulesParser.parseFilter: Invalid filter values %s in xml file.", filter.getValue()));
                }
                String b1 = values.nextToken(), b2 = values.nextToken();
                switch(atributeType) {
                    case string:
                        addQuery = new TermRangeQuery(attributeName, b1, b2, true, true);
                        query.add(addQuery, BooleanClause.Occur.MUST);
                        break;
                    case integer:
                        addQuery = NumericRangeQuery.newIntRange(attributeName, Integer.parseInt(b1), Integer.parseInt(b2), true, true);
                        query.add(addQuery, BooleanClause.Occur.MUST);
                        break;
                    case decimal:
                        addQuery = NumericRangeQuery.newDoubleRange(attributeName, Double.parseDouble(b1), Double.parseDouble(b2), true, true);
                        query.add(addQuery, BooleanClause.Occur.MUST);
                        break;
                    case datetime:
                    default:
                        throw new IdealizeInputException(String.format("FilterRulesParser.parseFilter: Invalid attribute type '%s' for the lesser(-equals) filter-rule in xml file.", filter.getType()));
                }
                break;
            case threshold:
                String t1, t2;
                if (values.countTokens() == 1) {
                    t1 = t2 = values.nextToken();
                } else if (values.countTokens() == 2) {
                    t1 = values.nextToken();
                    t2 = values.nextToken();
                } else {
                    throw new IdealizeInputException(String.format("FilterRulesParser.parseFilter: Invalid filter values %s xml file.", filter.getValue()));
                }
                switch(atributeType) {
                    case integer:
                        int attIntValue = Integer.parseInt(attributeValue);
                        int minInt = attIntValue - Integer.parseInt(t1);
                        int maxInt = attIntValue + Integer.parseInt(t2);
                        addQuery = NumericRangeQuery.newIntRange(attributeName, minInt, maxInt, true, true);
                        query.add(addQuery, BooleanClause.Occur.MUST);
                        break;
                    case decimal:
                        double attDblValue = Double.parseDouble(attributeValue);
                        double minDbl = attDblValue - Double.parseDouble(t1);
                        double maxDbl = attDblValue + Double.parseDouble(t2);
                        addQuery = NumericRangeQuery.newDoubleRange(attributeName, minDbl, maxDbl, true, true);
                        query.add(addQuery, BooleanClause.Occur.MUST);
                        break;
                    case datetime:
                    default:
                        throw new IdealizeInputException(String.format("FilterRulesParser.parseFilter: Invalid attribute type '%s' for the threshold filter-rule in xml file.", filter.getType()));
                }
                break;
            default:
                throw new IdealizeInputException(String.format("FilterRulesParser.parseFilter: Invalid filter type '%s' in xml file.", filter.getType()));
        }
    }

    /**
	 * Parse the defined group-filter rules to a BooleanQuery object.
	 * @param query
	 * @param filterRules
	 * @param referenceDoc
	 * @throws IdealizeInputException
	 */
    private void parseGroupFilter(BooleanQuery query, GroupFilters groupFilterRules, Document referenceDoc) throws IdealizeInputException {
        BooleanQuery groupQuery = new BooleanQuery();
        Iterator<Filter> filterIt = groupFilterRules.getFilter().iterator();
        while (filterIt.hasNext()) {
            parseFilter(groupQuery, filterIt.next(), referenceDoc);
        }
        Relation relation = getRelationOf(groupFilterRules.getRelation());
        BooleanClause.Occur occur = null;
        switch(relation) {
            case or:
                occur = BooleanClause.Occur.MUST;
                break;
            case and:
                occur = BooleanClause.Occur.MUST_NOT;
                break;
        }
        query.add(groupQuery, occur);
    }

    /**
	 * Map the string type defined at xml file to corresponding enum FilterType value.
	 * @param type
	 * @return Corresponding FilterType Value of a input string.
	 * @throws IdealizeInputException
	 */
    private FilterType getFilterTypeOf(String type) throws IdealizeInputException {
        if (filterTypes == null) {
            filterTypes = new HashMap<String, FilterType>();
            filterTypes.put("equals", FilterType.equals);
            filterTypes.put("not-equals", FilterType.notequals);
            filterTypes.put("in", FilterType.in);
            filterTypes.put("not-in", FilterType.notin);
            filterTypes.put("greater", FilterType.greater);
            filterTypes.put("greater-equals", FilterType.greaterequals);
            filterTypes.put("lesser", FilterType.lesser);
            filterTypes.put("lesser-equals", FilterType.lesserequals);
            filterTypes.put("between", FilterType.between);
            filterTypes.put("threshold", FilterType.threshold);
        }
        FilterType result = filterTypes.get(type);
        if (result == null) throw new IdealizeInputException(String.format("FilterRulesParser.getFilterTypeOf: Invalid filter type '%s' in xml file.", type));
        return result;
    }

    /**
	 * Map the string relation defined at xml file to corresponding enum Relation value.
	 * @param type
	 * @return Corresponding Relation Value of a input string.
	 * @throws IdealizeInputException
	 */
    private Relation getRelationOf(String rel) throws IdealizeInputException {
        if (relation == null) {
            relation = new HashMap<String, Relation>();
            relation.put("or", Relation.or);
            relation.put("and", Relation.and);
        }
        Relation result = relation.get(rel);
        if (result == null) throw new IdealizeInputException(String.format("FilterRulesParser.getRelationOf: Invalid group-filter relation '%s' in xml file.", rel));
        return result;
    }
}
