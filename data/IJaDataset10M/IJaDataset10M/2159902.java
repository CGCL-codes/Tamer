package org.statefive.feedstate.feed.factory;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.statefive.feedstate.feed.CommonMessageFormat;
import org.statefive.feedstate.feed.ConstructionException;
import org.statefive.feedstate.feed.column.BadTypeException;
import org.statefive.feedstate.feed.column.ColumnGroup;
import org.statefive.feedstate.feed.column.Types;

/**
 * Factory for creating feed data items from Java regular expressions.
 * 
 * <p>
 * The property {@link #REGEX} is used to store the Java regular expression that
 * will be applied every time {@link #build(Object)} is called.
 * 
 * <p>
 * Please refer to the user manual for more information.
 * 
 * @author rmeeking
 */
public class RegexFeedDataFactory extends AbstractFeedDataFactory {

    /** The java regular expression used to construct feed data. */
    public static final String REGEX = "regex.expression";

    /** Pattern compiled from the regular expression used to contruct feed items. */
    private Pattern pattern;

    /**
   * Constructs a new factory from the specified properties.
   * 
   * @param properties
   *          properties defining how feed items will be constructed.
   * 
   * @throws IllegalArgumentException
   *           if the properties {@link #REGEX}is not defined.
   * 
   * @throws ConstructionException
   *           if the header/column information could not be created.
   */
    public RegexFeedDataFactory(Properties properties) throws ConstructionException {
        super(properties, properties.getProperty(RegexFeedDataFactory.REGEX), new String[] { REGEX });
        pattern = Pattern.compile(properties.getProperty(RegexFeedDataFactory.REGEX));
    }

    /**
   * Builds feed data out of the specified string.
   * 
   * <p>TODO this crashes and burns if {@code null} is passed in; should we
   * just consider ingnoring 
   * 
   * @param rawFeedData
   *          a string that will be parsed using the specified regular
   *          expression defined when constructing this factory. If the
   *          data is not of type {@code java.lang.String}, the object
   *          is turned into a string using the {@link Object#toString()}
   *          method. Callers should ensure that the {@code rawFeedData}
   *          is not {@code null}.
   * 
   * @return a feed data item representing the regular expression.
   * 
   * @throws ConstructionException
   *           if the feed data could not be built. This can happen when the
   *           string being parsed using the specified regular expression is
   *           invalid, or if attempting to parse a {@code null} object.
   */
    @Override
    public CommonMessageFormat build(Object rawFeedData) throws ConstructionException {
        CommonMessageFormat feedData = new CommonMessageFormat(ColumnGroup.getColumnGroup(getColumnGroupIdentifier()));
        feedData.setFeedData(rawFeedData);
        String msg = null;
        if (rawFeedData instanceof String) {
            msg = (String) rawFeedData;
        } else {
            msg = rawFeedData.toString();
        }
        Matcher matcher = pattern.matcher(msg.toString());
        ColumnGroup columns = ColumnGroup.getColumnGroup(getColumnGroupIdentifier());
        if (matcher.matches()) {
            for (int i = 1; i < matcher.groupCount() + 1; i++) {
                try {
                    feedData.setField(i - 1, Types.getType(columns.getColumns()[i - 1].getType(), matcher.group(i)));
                } catch (BadTypeException btex) {
                    throw new ConstructionException(btex.getMessage(), btex);
                }
            }
        } else {
            throw new ConstructionException("Could not construct feed data from " + "regular expression:\n\t" + props.getProperty(REGEX) + "\nfrom data:\n\t'" + rawFeedData + "'");
        }
        return feedData;
    }

    /**
   * @deprecated To be removed.
   */
    public void parseFieldHeaders() throws ConstructionException {
    }
}
