package prisms.lang.types;

import prisms.lang.EvaluationException;
import prisms.lang.ParsedItem;

/** Represents a block of statements */
public class ParsedStatementBlock extends ParsedItem {

    private ParsedItem[] theContents;

    /**
	 * Default constructor. Used when {@link #setup(prisms.lang.PrismsParser, ParsedItem, prisms.lang.ParseMatch)} will
	 * be called later
	 */
    public ParsedStatementBlock() {
    }

    /**
	 * Pre-setup constructor. Used so that {@link #setup(prisms.lang.PrismsParser, ParsedItem, prisms.lang.ParseMatch)}
	 * does not need to be called subsequently.
	 * 
	 * @param parser The parser that parsed this structure's contents
	 * @param parent The parent structure
	 * @param match The match that this structure is to identify with
	 * @param contents The content statements of the block
	 * @see ParsedItem#setup(prisms.lang.PrismsParser, ParsedItem, prisms.lang.ParseMatch)
	 */
    public ParsedStatementBlock(prisms.lang.PrismsParser parser, ParsedItem parent, prisms.lang.ParseMatch match, ParsedItem... contents) {
        try {
            super.setup(parser, parent, match);
        } catch (prisms.lang.ParseException e) {
            throw new IllegalStateException("ParseException should not come from super class", e);
        }
        theContents = contents;
    }

    @Override
    public void setup(prisms.lang.PrismsParser parser, ParsedItem parent, prisms.lang.ParseMatch match) throws prisms.lang.ParseException {
        super.setup(parser, parent, match);
        java.util.ArrayList<ParsedItem> contents = new java.util.ArrayList<ParsedItem>();
        for (prisms.lang.ParseMatch m : match.getParsed()) if ("content".equals(m.config.get("storeAs"))) contents.add(parser.parseStructures(this, m)[0]);
        theContents = contents.toArray(new ParsedItem[contents.size()]);
    }

    @Override
    public prisms.lang.EvaluationResult evaluate(prisms.lang.EvaluationEnvironment env, boolean asType, boolean withValues) throws EvaluationException {
        prisms.lang.EvaluationEnvironment scoped = env.scope(true);
        for (ParsedItem content : theContents) {
            prisms.lang.EvaluationResult res = executeJavaStatement(content, scoped, withValues);
            if (res != null) return res;
        }
        return null;
    }

    /**
	 * Executes a statement in a java context. Only statements that would be permitted as java statements are accepted.
	 * Others will throw an exception
	 * 
	 * @param content The statement to execute
	 * @param env The environment to execute the statement in
	 * @param withValues Whether to evaluate values or just validate the statement
	 * @return A result if the statement was a control statement, null otherwise
	 * @throws EvaluationException If the statement is not a valid java statement, or if the execution of the statement
	 *         throws an exception
	 */
    public static prisms.lang.EvaluationResult executeJavaStatement(ParsedItem content, prisms.lang.EvaluationEnvironment env, boolean withValues) throws EvaluationException {
        if (content instanceof ParsedAssignmentOperator) {
        } else if (content instanceof ParsedDeclaration) {
        } else if (content instanceof ParsedMethod) {
            ParsedMethod method = (ParsedMethod) content;
            if (!method.isMethod()) throw new EvaluationException("Content expressions must be declarations, assignments or method calls", content.getParent(), content.getMatch().index);
        } else if (content instanceof ParsedConstructor) {
        } else if (content instanceof ParsedLoop) {
        } else if (content instanceof ParsedEnhancedForLoop) {
        } else if (content instanceof ParsedIfStatement) {
        } else if (content instanceof ParsedKeyword) {
            String word = ((ParsedKeyword) content).getName();
            if (word.equals("continue")) return new prisms.lang.EvaluationResult(prisms.lang.EvaluationResult.ControlType.CONTINUE, null, content);
            if (word.equals("break")) return new prisms.lang.EvaluationResult(prisms.lang.EvaluationResult.ControlType.BREAK, null, content);
        } else if (content instanceof ParsedReturn) {
            if (withValues) return content.evaluate(env, false, withValues);
        } else if (content instanceof ParsedThrow) {
        } else throw new EvaluationException("Content expressions must be declarations, assignments or method calls", content.getParent(), content.getMatch().index);
        content.evaluate(env, false, withValues);
        return null;
    }

    /** @return The contents of this statement block */
    public ParsedItem[] getContents() {
        return theContents;
    }

    @Override
    public ParsedItem[] getDependents() {
        return theContents;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("{\n");
        for (ParsedItem content : theContents) ret.append(content).append('\n');
        ret.append("}\n");
        return ret.toString();
    }
}
