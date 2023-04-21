package src.ptm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import src.ptm.PTM.PTMObjects;

/**
 * <p>Handles a time function:</p>
 * <p><code>{{#time: yyyy-MM-dd hh:mm:ss}}</code></p>
 * <p>See <a href="http://download.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a>
 * regarding the accepted formatting options.</p>
 */
public class PTMFunctionTime extends PTMFunctionNode {

    public static final Pattern startPattern = Pattern.compile("^\\{\\{\\s*#time\\s*:");

    private static final List<PTMObjects> allowedChildnodes;

    static {
        allowedChildnodes = new ArrayList<PTMObjects>();
        allowedChildnodes.add(PTMObjects.Argument);
    }

    /**
	 * <p>Inserts the current timestamp formatted according to the given pattern.</p>
	 */
    public PTMFunctionTime(StringBuffer content, int beginIndex, PTMNode parent, PTMRootNode root) throws ObjectNotApplicableException {
        super(content, beginIndex, parent, root);
        Matcher m = startPattern.matcher(PTMObjectFactory.getIndicator(content, beginIndex));
        if (m.find()) {
            endIndex = beginIndex + m.end();
        } else {
            throw new ObjectNotApplicableException("Lost position of the start pattern!");
        }
        if (content.charAt(endIndex) == '|') {
            PTMArgumentNode obj = new PTMArgumentNode(content, beginIndex, parent, root, true);
            childTree.add(obj);
        }
        boolean functionEndReached = false;
        PTMObject obj;
        do {
            try {
                obj = PTMObjectFactory.buildObject(content, endIndex, this, root, PTMFunctionNode.abort, allowedChildnodes);
                if (obj != null) {
                    childTree.add(obj);
                    endIndex = obj.endIndex;
                }
            } catch (EndOfExpressionReachedException e) {
                functionEndReached = true;
                break;
            }
        } while (obj != null);
        if (!functionEndReached) {
            throw new ObjectNotApplicableException("End of the #time expression could not be found.");
        }
        try {
            if (PTMFunctionNode.endExpression.equals(content.substring(endIndex, endIndex + PTMFunctionNode.endExpression.length()))) {
                functionEndReached = true;
                endIndex += PTMFunctionNode.endExpression.length();
            }
        } catch (StringIndexOutOfBoundsException e) {
        }
        if (!functionEndReached) {
            throw new ObjectNotApplicableException("End of the #time expression could not be located.");
        }
        if (childTree.size() < 1) {
            throw new ObjectNotApplicableException("Not enough arguments for the #time function. Usage: {{#time: format}}");
        }
        assert endIndex > this.beginIndex;
    }

    public String evaluate() throws RecursionException {
        String format = childTree.get(0).evaluate();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String result = formatter.format(new Date());
        return result;
    }
}
