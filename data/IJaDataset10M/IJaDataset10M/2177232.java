package com.lettuce.core.clause;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import com.lettuce.core.Phrases;
import com.lettuce.core.phrase.Keyword;
import com.lettuce.core.phrase.Phrase;

/**
 * <code>ClauseException</code> might be thrown while composing a
 * <code>lettuce query phrase sequence</code>.
 * 
 * @author Zhigang Xie
 * @version 1.0, 12/30/09
 * @since JDK 1.5
 * @see Phrases
 */
public class ClauseException extends RuntimeException {

    private static final long serialVersionUID = 5321282527929387218L;

    private Phrases phrases;

    private int position = 0;

    /**
	 * Constructs a <code>ClauseException</code> with a
	 * <code>lettuce query phrase sequence</code>.
	 * 
	 * @param phrases
	 *            lettuce query phrase sequence.
	 */
    public ClauseException(Phrases phrases) {
        this.phrases = phrases;
        this.position = phrases.getCurrentIndex();
    }

    /**
	 * Gets the lettuce query phrase sequence where causing this exception.
	 * 
	 * @return
	 */
    public Phrases getPhrases() {
        return phrases;
    }

    @Override
    public String getMessage() {
        if (phrases != null) {
            StringBuffer sb = new StringBuffer("LettuceQuery syntax error: ");
            List<Object> list = buildList(phrases, position + 1);
            for (int index = 0; index < list.size(); index++) {
                Object obj = list.get(index);
                if (obj instanceof Phrase) {
                    Phrase phrase = (Phrase) obj;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    phrase.sqlRepresent(new PrintStream(baos));
                    sb.append(baos.toString());
                } else if (obj instanceof Clause) {
                    Clause clause = (Clause) obj;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    clause.sqlRepresent(new PrintStream(baos));
                    sb.append(baos.toString());
                } else sb.append(obj.toString());
                Object objNext = null;
                if (index + 1 < list.size()) objNext = list.get(index + 1);
                if (!(Keyword.L_PARENTHESIS.equals(obj) || Keyword.R_PARENTHESIS.equals(obj) || Keyword.L_PARENTHESIS.equals(objNext) || Keyword.R_PARENTHESIS.equals(objNext) || objNext == null)) sb.append(',');
                sb.append(' ');
            }
            sb.append("***[SYNTAX ERROR]***");
            return sb.toString();
        }
        return "[null]";
    }

    /**
	 * @param list
	 * @return
	 */
    private List<Object> buildList(Phrases context, int toPosition) {
        List<Object> list = new ArrayList<Object>();
        Object[] objs = context.toArray();
        for (int index = 0; index < toPosition && index < objs.length; index++) {
            Object obj = objs[index];
            if (obj instanceof Phrases) {
                list.add(Keyword.L_PARENTHESIS);
                Phrases phrases = (Phrases) obj;
                list.addAll(buildList(phrases, phrases.size()));
                list.add(Keyword.R_PARENTHESIS);
            } else list.add(obj);
        }
        if (context.getParent() != null) {
            List<Object> newList = buildList(context.getParent(), context.getNestPosition());
            newList.addAll(list);
            return newList;
        } else return list;
    }
}
