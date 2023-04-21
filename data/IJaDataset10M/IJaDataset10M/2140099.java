package com.ibm.tuningfork.infra.stream.expression.builtin;

import java.util.List;
import com.ibm.tuningfork.infra.feed.Feed;
import com.ibm.tuningfork.infra.feed.FeedGroup;
import com.ibm.tuningfork.infra.feed.FeedGroupRegistry;
import com.ibm.tuningfork.infra.feed.StreamManager;
import com.ibm.tuningfork.infra.stream.core.Stream;
import com.ibm.tuningfork.infra.stream.expression.base.Expression;
import com.ibm.tuningfork.infra.stream.expression.base.StreamContext;
import com.ibm.tuningfork.infra.stream.expression.base.StreamExpression;
import com.ibm.tuningfork.infra.stream.expression.base.StringExpression;
import com.ibm.tuningfork.infra.stream.expression.functions.StreamFunctionDefinition;
import com.ibm.tuningfork.infra.stream.expression.literals.StreamLiteral;
import com.ibm.tuningfork.infra.stream.expression.literals.StringLiteral;
import com.ibm.tuningfork.infra.stream.expression.syntax.ForkTalkSymbolTable;
import com.ibm.tuningfork.infra.stream.expression.types.ExpressionType;

/**
 * The findStream built-in function.  Finds a Stream using standard Feed and FeedGroup methods.  Assumes that the appropriate FeedGroup
 *   is either supplied as an argument or can be found in the symbol table.  The resolution occurs at translation time, not runtime
 *   (otherwise the type of the stream would not be known).   Furthermore, resolution occurs as late as possible during translation
 *   (function applications at depth > 1 are done lazily by returning recursive instances of this class).
 */
public class FindStream extends StreamFunctionDefinition {

    /** The name of the function */
    public static final String FUNCTION_NAME = "findStream";

    /** The symbol to look up to get the default feed group */
    public static final String DEFAULT_FEED_GROUP_SYMBOL = "localFeedGroupName";

    /** The string that might be supplied as an argument to designate the cross-feed streams of a FeedGroup */
    public static final String CROSS_FEED_NAME = "Cross-Feed Streams";

    /** Because this function is used implicitly by the language syntax, we keep an instance of it publically available by symbol */
    private static FindStream INSTANCE;

    /** The signature of the function:  up to three strings (one required) */
    private static final ExpressionType[] PARAMETER_TYPES = { ExpressionType.STRING, ExpressionType.STRING, ExpressionType.STRING };

    /** The names of the parameters (for display) */
    private static final String[] PARAMETER_NAMES = { "streamName", "feedName", "feedGroupName" };

    /** The name of the stream (only used by recursive lazy application instances) */
    private StringExpression streamName;

    /** The name of the feed (only used by recursive lazy application instances) */
    private StringExpression feedName;

    /** The name of the feed group (only used by recursive lazy application instances) */
    private StringExpression feedGroupName;

    /** Nullary constructor required by built-in functions initialization */
    public FindStream() {
        super(StreamLiteral.MISSING.getType(), FUNCTION_NAME, PARAMETER_TYPES, PARAMETER_NAMES);
    }

    /**
     * Internal constructor used by lazy application
     * @param streamName the stream name expression
     * @param feedName the feed name expression
     * @param feedGroupName the feed group name expression
     */
    private FindStream(StringExpression streamName, StringExpression feedName, StringExpression feedGroupName) {
        this();
        this.streamName = streamName;
        this.feedName = feedName;
        this.feedGroupName = feedGroupName;
    }

    public StreamExpression apply(Expression[] arguments, ForkTalkSymbolTable symbolTable, int depth) {
        StringExpression streamName = (StringExpression) arguments[0];
        StringExpression feedName = StringLiteral.MISSING;
        if (arguments.length > 1) {
            feedName = (StringExpression) arguments[1];
        }
        StringExpression feedGroupName;
        if (arguments.length > 2) {
            feedGroupName = (StringExpression) arguments[2];
        } else {
            Expression defaultSymbol = symbolTable.resolveSymbol(DEFAULT_FEED_GROUP_SYMBOL);
            if (defaultSymbol instanceof StringExpression) {
                feedGroupName = (StringExpression) defaultSymbol;
            } else {
                throw new IllegalArgumentException("No default feed group defined");
            }
        }
        if (depth > 1) {
            return new FindStream(streamName, feedName, feedGroupName);
        }
        return doFinding(streamName, feedName, feedGroupName);
    }

    /**
     * Override this to ensure a value if not previously resolved
     * @see com.ibm.tuningfork.infra.stream.expression.functions.StreamFunctionDefinition#getStreamValue(com.ibm.tuningfork.infra.stream.expression.base.StreamContext)
     */
    public Stream getStreamValue(StreamContext context) {
        if (streamName != null) {
            return doFinding(streamName, feedName, feedGroupName).getStreamValue(context);
        }
        return super.getStreamValue(context);
    }

    public int minimumArity() {
        return 1;
    }

    public StreamExpression resolve(Expression[] arguments, int depth) {
        StringExpression newStreamName = streamName.resolve(arguments, depth);
        StringExpression newFeedName = feedName.resolve(arguments, depth);
        StringExpression newFeedGroupName = feedGroupName.resolve(arguments, depth);
        if (depth > 1) {
            if (newStreamName != streamName || newFeedName != feedName || newFeedGroupName != feedGroupName) {
                return new FindStream(newStreamName, newFeedName, newFeedGroupName);
            }
            return this;
        }
        return doFinding(newStreamName, newFeedName, newFeedGroupName);
    }

    public String toString() {
        if (streamName == null) {
            return BuiltInSymbols.toString(FUNCTION_NAME, PARAMETER_TYPES, PARAMETER_NAMES);
        }
        if (feedName == StringLiteral.MISSING) {
            return FUNCTION_NAME + "(" + streamName + ")";
        }
        return FUNCTION_NAME + "(" + streamName + ", " + feedName + ", " + feedGroupName + ")";
    }

    /**
     * Do the actual evaluation of names and finding of streams.  This is done at translation time but as late as possible using
     *   lazy application to delay the finding operation until the final expression is being built
     * @param streamName expression giving the name of the stream
     * @param feedName expression giving the name of the feed
     * @param feedGroupName expression giving the name of the feed group
     * @return
     */
    private StreamExpression doFinding(StringExpression streamName, StringExpression feedName, StringExpression feedGroupName) {
        FeedGroup group = FeedGroupRegistry.findFeedGroup(feedGroupName.getStringValue(StreamContext.TRANSLATION_TIME_CONTEXT));
        if (group == null) {
            throw new IllegalArgumentException("Unable to find FeedGroup with id " + feedGroupName);
        }
        String managerName = feedName.getStringValue(StreamContext.TRANSLATION_TIME_CONTEXT);
        String name = streamName.getStringValue(StreamContext.TRANSLATION_TIME_CONTEXT);
        Stream theStream = null;
        if (managerName == null) {
            theStream = group.getStreamByName(name);
        } else {
            StreamManager manager = null;
            if (CROSS_FEED_NAME.equalsIgnoreCase(managerName)) {
                manager = group.getStreamManager();
            } else {
                List<Feed> feeds = group.getFeeds();
                for (Feed feed : feeds) {
                    if (feed.getName().equalsIgnoreCase(managerName)) {
                        manager = feed.getStreamManager();
                        break;
                    }
                }
            }
            if (manager == null) {
                throw new IllegalArgumentException("Unable to find StreamManager " + managerName);
            }
            theStream = manager.getStreamByName(name);
        }
        if (theStream == null) {
            throw new IllegalArgumentException("Unable to resolve stream " + name);
        }
        return new StreamLiteral(theStream);
    }

    /**
     * @return an instance of this function for use by the syntax
     */
    public static FindStream getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FindStream();
        }
        return INSTANCE;
    }
}
