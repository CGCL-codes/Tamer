package machine;

import java.awt.Point;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import main.Constants;
import exceptions.IllegalParameterException;
import exceptions.ParsingException;
import exceptions.UnknownTapeException;
import exceptions.UnknownTransitionException;

/**
 * A transition saves all information that is necessary to change between
 * states.
 * 
 * @author Sven Schneider
 * @since 0.1.8
 */
public class Transition {

    /**
	 * Inside the expression used for creating a text for a transitions
	 * text-anchor these and only these operations can occur.
	 */
    private enum cmdType {

        /**
		 * symbolizes a head move of one of the transitions operations, used for
		 * anchortext-expr
		 */
        CMD_H, /**
		 * symbolizes the read symbol of one of the transitions operations, used
		 * for anchortext-expr
		 */
        CMD_SR, /**
		 * symbolizes the write symbol of one of the transitions operations,
		 * used for anchortext-expr
		 */
        CMD_SW, /**
		 * symbolizes the tape description of one of the transitions operations,
		 * used for anchortext-expr
		 */
        CMD_TD, /**
		 * symbolizes the tape number of one of the transitions operations, used
		 * for anchortext-expr
		 */
        CMD_TN
    }

    /**
	 * the expression shown at the textanchor, null if default value should be
	 * used
	 */
    private String anchorExpr = null;

    /**
	 * [TransitionDots] the dots the transition will follow from the start state
	 * to the end state
	 */
    private Vector<TransitionDot> dots = new Vector<TransitionDot>();

    /** The SelectableOval the transition ends in */
    private SelectableOval end;

    /**
	 * if end is a Submachine this variable stores the name of the state from
	 * that submachine this transitions targets
	 */
    private String endName = null;

    /**
	 * normalized transition dots are moved when their attached selectableOvals
	 * get dragged
	 */
    private boolean isNormalized = true;

    /** the operations that need to be done in case this transition is used */
    private Vector<TransitionOpns> opns = new Vector<TransitionOpns>();

    /** The SelectableOval the transition starts in */
    private SelectableOval start;

    /**
	 * if start is a Submachine this variable stores the name of the state from
	 * that submachine this transition starts in
	 */
    private String startName = null;

    /** this variable stores where the textAnchor is placed */
    private TransitionDot textAnchor;

    /** this is the turingmachine this transition belongs to */
    private Turingmachine tm = null;

    /**
	 * The constructor sets all important variables.
	 * 
	 * @param pStart
	 *            the SelectableOval the transition starts in
	 * @param pStartname
	 *            if pStart is a Submachine this variable stores the name of the
	 *            state from that submachine this transitions starts in
	 * @param pEnd
	 *            the SelectableOval the transition ends in
	 * @param pEndname
	 *            if pEnd is a Submachine this variable stores the name of the
	 *            state from that submachine this transitions targets
	 * @param pDots
	 *            [TransitionDots] the dots the transition will follow from the
	 *            start state to the end state
	 * @param pOpns
	 *            the operations that need to be done in case this transition is
	 *            used
	 * @param pTextAnchor
	 *            the position of the textAnchor
	 * @param pTm
	 *            the turingmachine this transition belongs to
	 * @see #addOpns(TransitionOpns)
	 * @throws IllegalParameterException
	 *             thrown for two different reasons:<br>
	 *             1: pStart/pEnd/pTM were null<br>
	 *             2: internal errors.
	 * @throws UnknownTapeException
	 *             see #addOpns
	 * @throws UnknownTransitionException
	 *             see #addOpns
	 */
    protected Transition(SelectableOval pStart, @SuppressWarnings("unused") String pStartname, SelectableOval pEnd, @SuppressWarnings("unused") String pEndname, Vector<Point> pDots, Vector<TransitionOpns> pOpns, Point pTextAnchor, Turingmachine pTm) throws IllegalParameterException, UnknownTapeException, UnknownTransitionException {
        tm = pTm;
        if (pStart == null || pEnd == null || pTm == null) throw new IllegalParameterException(Transition.class.getCanonicalName() + " / null-parameter / cannnot create transition");
        start = pStart;
        end = pEnd;
        if (pTextAnchor != null) textAnchor = new TransitionDot(new Point(pTextAnchor), this);
        if (pDots != null && pDots.size() != 0) {
            for (Point it : pDots) addDot(it);
            if (textAnchor == null) setTextAnchor(getNormalAnchor());
        } else normalize();
        if (pOpns != null) {
            for (TransitionOpns it : pOpns) {
                addOpns(new TransitionOpns(it.getHm(), it.getReadSymb(), it.getWriteSymb(), it.getTape()));
            }
        }
    }

    /**
	 * A dot is supposed to be inserted.
	 * <p>
	 * If the dot is to close to one of the others, the dot is not inserted.
	 * 
	 * @param param
	 *            the position of the new dot
	 * @return true if the dot was inserted<br>
	 *         false if the dot was not inserted
	 * @see TransitionDot#TransitionDot(Point, Transition)
	 * @throws IllegalParameterException
	 *             see TransitionDot#TransitionDot
	 */
    private boolean addDot(Point param) throws IllegalParameterException {
        for (TransitionDot it : dots) if (it.getPosition().distance(param) < Constants.MINIMUM_DOT_DIST) return false;
        isNormalized = false;
        return dots.add(new TransitionDot(param, this));
    }

    /**
	 * Adds an operation to the transition.
	 * <p>
	 * If there is already an operation for the same tape, the old operation is
	 * removed and the new inserted.
	 * 
	 * @param param
	 *            the TransitionOperation to be inserted
	 * @throws IllegalParameterException
	 *             the symbol mentioned by param is not available
	 * @throws UnknownTapeException
	 *             the tape mentioned by param is not available
	 * @throws UnknownTransitionException
	 *             <code>this</code> does not belong to the machine (internal
	 *             error)
	 */
    protected void addOpns(TransitionOpns param) throws IllegalParameterException, UnknownTapeException, UnknownTransitionException {
        if (!tm.isTape(param.getTape())) throw new UnknownTapeException(Transition.class.getCanonicalName() + " / tape does not belong to the given machine / not adding this operation");
        if (!tm.isSymbol(param.getReadSymb()) && !param.getReadSymb().isBlank() && !param.getReadSymb().isEpsilon() || !tm.isSymbol(param.getWriteSymb()) && !param.getWriteSymb().isBlank() && !param.getWriteSymb().isEpsilon()) throw new IllegalParameterException(Transition.class.getCanonicalName() + " / symbol does not belong to the given machine / not adding this operation");
        for (TransitionOpns it : opns) {
            if (param.getTape() == it.getTape()) {
                tm.removeTransitionOpn(this, it.getTape(), it.getHm(), it.getReadSymb(), it.getWriteSymb());
                break;
            }
        }
        opns.add(param);
    }

    /**
	 * To the done parameter the element cmd frm the tape numb is added.
	 * 
	 * @param done
	 *            the result
	 * @param numb
	 *            the tape number of the operation to choose from
	 * @param cmd
	 *            the element to add to done from the operation with the
	 *            tapenumber numb
	 * @see Transition.cmdType
	 */
    private void addTransText(StringBuffer done, int numb, cmdType cmd) {
        for (TransitionOpns it : opns) {
            if (it.getTape().getTapeNumber() == numb) {
                switch(cmd) {
                    case CMD_TD:
                        done.append(it.getTape().getDescription());
                        break;
                    case CMD_TN:
                        done.append(it.getTape().getTapeNumber());
                        break;
                    case CMD_SR:
                        done.append(it.getReadSymb());
                        break;
                    case CMD_SW:
                        done.append(it.getWriteSymb());
                        break;
                    case CMD_H:
                        done.append(it.getHm().shortToString());
                        break;
                }
                break;
            }
        }
    }

    /**
	 * Checks if this operation can be applied to the turingmachine.
	 * <p>
	 * a transition cannot be used if one or more of the operation of that
	 * transition cannot be applied to the tapes.<br>
	 * This is the case if the symbol of the operation cannot be read from the
	 * tape.
	 * 
	 * @return true if this transition ca be used<br>
	 *         false if this transition cannot be used.
	 */
    public boolean canUseThisTransistion() {
        return true;
    }

    /**
	 * It is checked if the expression can be parsed without any error.
	 * 
	 * @param text
	 *            the expression to be checked
	 * @see #getTransText(String)
	 * @throws ParsingException
	 *             see getTransText
	 */
    public void checkExpr(String text) throws ParsingException {
        getTransText(text);
    }

    /**
	 * The todo-buffer begins with an integer number, this number is returned.
	 * 
	 * @param done
	 *            the number is added to the end of this buffer
	 * @param todo
	 *            the number is removed from the beginning of this buffer
	 * @return the number that was at the beginning of todo
	 */
    private int extractNumber(StringBuffer done, StringBuffer todo) {
        String numb = "";
        while (todo.charAt(0) == '0' || todo.charAt(0) == '1' || todo.charAt(0) == '2' || todo.charAt(0) == '3' || todo.charAt(0) == '4' || todo.charAt(0) == '5' || todo.charAt(0) == '6' || todo.charAt(0) == '7' || todo.charAt(0) == '8' || todo.charAt(0) == '9') {
            numb += todo.charAt(0);
            if (done != null) done.append(todo.charAt(0));
            todo.deleteCharAt(0);
        }
        return Integer.parseInt(numb);
    }

    /**
	 * Getter.
	 * 
	 * @return the anchorExpression of this transition<br>
	 *         null if the default expression is supposed to be used
	 */
    public String getAnchorExpr() {
        return anchorExpr;
    }

    /**
	 * A vector containing all dots of the transition.
	 * 
	 * @return A vector containing all dots of the transition
	 */
    public List<TransitionDot> getDots() {
        return Collections.unmodifiableList(dots);
    }

    /**
	 * Getter.
	 * 
	 * @return the SelectableOval the transition targets
	 */
    public SelectableOval getEnd() {
        return end;
    }

    /**
	 * Getter.
	 * 
	 * @return the statename the transition targets, if the transition targets a
	 *         Submachine
	 */
    public String getEndName() {
        return endName;
    }

    /**
	 * Returns the location the textAnchor would have if the transition would be
	 * normalized.
	 * 
	 * @return the location the textAnchor would have if the transition would be
	 *         normalized
	 * @see TransitionDot#TransitionDot(Point, Transition)
	 * @throws IllegalParameterException
	 *             see TransitionDot#TransitionDot
	 */
    private TransitionDot getNormalAnchor() throws IllegalParameterException {
        Point p = new Point(dots.elementAt(dots.size() / 2).getPosition());
        return new TransitionDot(new Point(p.x + 10, p.y), this);
    }

    /**
	 * A vector containing all opns of the transition.
	 * 
	 * @return A vector containing all opns of the transition
	 */
    public Collection<TransitionOpns> getOpns() {
        return Collections.unmodifiableCollection(opns);
    }

    /**
	 * Getter.
	 * 
	 * @return the SelectableOval the transition starts in
	 */
    public SelectableOval getStart() {
        return start;
    }

    /**
	 * Getter.
	 * 
	 * @return the statename the transition starts in, if the transition starts
	 *         in a Submachine
	 */
    public String getStartName() {
        return startName;
    }

    /**
	 * Getter.
	 * 
	 * @return the current location of the textanchor
	 */
    public TransitionDot getTextAnchor() {
        return textAnchor;
    }

    /**
	 * Getter.
	 * 
	 * @return the parsed and evaluated string to be displayed at the anchor
	 * @see #getTransText(String)
	 * @throws ParsingException
	 *             see #getTransText
	 */
    public Vector<String> getTransText() throws ParsingException {
        return getTransText(anchorExpr);
    }

    /**
	 * Evaluates the given anchor-expression.
	 * 
	 * @param str
	 *            the expression to be evaluated
	 * @return the list of string to be displayed
	 * @throws ParsingException
	 *             an unknown command was found
	 */
    private Vector<String> getTransText(String str) throws ParsingException {
        Vector<Integer> excludedNumbers = new Vector<Integer>();
        StringBuffer done = new StringBuffer();
        StringBuffer todo = new StringBuffer((str == null) ? Constants.DEFAULT_ANCHOR_EXPR : str);
        while (todo.length() > 0) {
            if (todo.charAt(0) != '%') {
                done.append(todo.charAt(0));
                todo.deleteCharAt(0);
                continue;
            }
            todo.deleteCharAt(0);
            if (todo.indexOf("ex") == 0) {
                done.append("%ex");
                todo.delete(0, 2);
                int numb = extractNumber(done, todo);
                excludedNumbers.add(new Integer(numb));
            } else if (todo.indexOf("in") == 0) {
                done.append("%in");
                todo.delete(0, 2);
                int numb = extractNumber(done, todo);
                excludedNumbers.remove(new Integer(numb));
            } else if (todo.indexOf("td") == 0) {
                todo.delete(0, 2);
                addTransText(done, extractNumber(done, todo), cmdType.CMD_TD);
            } else if (todo.indexOf("tn") == 0) {
                todo.delete(0, 2);
                addTransText(done, extractNumber(done, todo), cmdType.CMD_TN);
            } else if (todo.indexOf("sr") == 0) {
                todo.delete(0, 2);
                addTransText(done, extractNumber(done, todo), cmdType.CMD_SR);
            } else if (todo.indexOf("sw") == 0) {
                todo.delete(0, 2);
                addTransText(done, extractNumber(done, todo), cmdType.CMD_SW);
            } else if (todo.indexOf("h") == 0) {
                todo.deleteCharAt(0);
                addTransText(done, extractNumber(done, todo), cmdType.CMD_H);
            } else if (todo.charAt(0) == '%') {
                done.append("%");
                readToDoublePercent(done, todo);
            } else {
                done.append("%" + todo.charAt(0));
                todo.deleteCharAt(0);
                readToDoublePercent(done, todo);
            }
        }
        excludedNumbers.clear();
        todo = done;
        done = new StringBuffer();
        while (todo.length() > 0) {
            if (todo.charAt(0) != '%') {
                done.append(todo.charAt(0));
                todo.deleteCharAt(0);
                continue;
            }
            todo.deleteCharAt(0);
            if (todo.indexOf("ex") == 0) {
                done.append("%ex");
                todo.delete(0, 2);
                excludedNumbers.add(new Integer(extractNumber(done, todo)));
            } else if (todo.indexOf("in") == 0) {
                done.append("%in");
                todo.delete(0, 2);
                excludedNumbers.remove(new Integer(extractNumber(done, todo)));
            } else {
                StringBuffer iCom = new StringBuffer();
                readToDoublePercent(iCom, todo);
                iCom.reverse();
                iCom.delete(0, 2);
                iCom.reverse();
                for (Tape it : tm.getTapes()) {
                    if (hasOpenForTape(it) && !excludedNumbers.contains(it.getTapeNumber())) {
                        StringBuffer cCom = new StringBuffer(iCom);
                        while (cCom.length() > 0) {
                            if (cCom.charAt(0) != '%') {
                                if (!excludedNumbers.contains(it.getTapeNumber())) done.append(cCom.charAt(0));
                                cCom.deleteCharAt(0);
                            } else {
                                cCom.deleteCharAt(0);
                                if (cCom.indexOf("ex") == 0) {
                                    cCom.delete(0, 2);
                                    excludedNumbers.add(new Integer(extractNumber(null, cCom)));
                                } else if (cCom.indexOf("in") == 0) {
                                    cCom.delete(0, 2);
                                    excludedNumbers.remove(new Integer(extractNumber(null, cCom)));
                                }
                                if (cCom.indexOf("tdi") == 0) {
                                    cCom.delete(0, 3);
                                    if (excludedNumbers.contains(it.getTapeNumber())) continue;
                                    addTransText(done, it.getTapeNumber(), cmdType.CMD_TD);
                                } else if (cCom.indexOf("tni") == 0) {
                                    cCom.delete(0, 3);
                                    if (excludedNumbers.contains(it.getTapeNumber())) continue;
                                    addTransText(done, it.getTapeNumber(), cmdType.CMD_TN);
                                } else if (cCom.indexOf("sri") == 0) {
                                    cCom.delete(0, 3);
                                    if (excludedNumbers.contains(it.getTapeNumber())) continue;
                                    addTransText(done, it.getTapeNumber(), cmdType.CMD_SR);
                                } else if (cCom.indexOf("swi") == 0) {
                                    cCom.delete(0, 3);
                                    if (excludedNumbers.contains(it.getTapeNumber())) continue;
                                    addTransText(done, it.getTapeNumber(), cmdType.CMD_SW);
                                } else if (cCom.indexOf("hi") == 0) {
                                    cCom.delete(0, 2);
                                    if (excludedNumbers.contains(it.getTapeNumber())) continue;
                                    addTransText(done, it.getTapeNumber(), cmdType.CMD_H);
                                }
                            }
                        }
                    }
                }
            }
        }
        todo = done;
        done = new StringBuffer();
        while (todo.length() > 0) {
            if (todo.charAt(0) != '%') {
                done.append(todo.charAt(0));
                todo.deleteCharAt(0);
                continue;
            }
            todo.deleteCharAt(0);
            if (todo.indexOf("ex") == 0 || todo.indexOf("in") == 0) {
                todo.delete(0, 2);
            } else throw new ParsingException(Transition.class.getCanonicalName() + " / unknown command in expression \"" + todo + "\" / aborting parsing");
        }
        todo = done;
        Vector<String> res = new Vector<String>();
        String cur = "";
        while (todo.length() > 0) {
            if (todo.indexOf("\\n") == 0) {
                todo.delete(0, 2);
                res.add(cur);
                cur = "";
            } else {
                cur += todo.charAt(0);
                todo.deleteCharAt(0);
            }
        }
        if (cur.length() > 0) res.add(cur);
        return res;
    }

    /**
	 * Checks if this transition has an operation for the passed tape.
	 * 
	 * @param tape
	 *            the tape to search for operation in this transition
	 * @return true if this transition has an operation regarding the tape<br>
	 *         false if this transition has no operation for the given tape
	 */
    private boolean hasOpenForTape(Tape tape) {
        for (TransitionOpns it : opns) if (tape == it.getTape()) return true;
        return false;
    }

    /**
	 * Inserts a dot between the passed dot and is precessor.
	 * 
	 * @param dot
	 *            the dot which will be the successor of the new dot
	 * @return the created and inserted dot
	 * @see TransitionDot#TransitionDot(Point, Transition)
	 * @throws IllegalParameterException
	 *             the passed dot is not part of this transition<br>
	 *             or see TransitionDot#TransitionDot (internal error)
	 */
    protected TransitionDot insertDotAt(TransitionDot dot) throws IllegalParameterException {
        int idx = -1;
        TransitionDot old = null;
        TransitionDot cur = new TransitionDot(getStart().getPosition(), this);
        TransitionDot newDot = null;
        for (TransitionDot it : dots) {
            idx++;
            old = cur;
            cur = it;
            if (cur == dot) break;
        }
        if (cur != dot) throw new IllegalParameterException(Transition.class.getCanonicalName() + " / passed point not in this transition / cannnot insert dot");
        newDot = new TransitionDot(new Point((old.getPosition().x + cur.getPosition().x) / 2, (old.getPosition().y + cur.getPosition().y) / 2), this);
        dots.add(idx, newDot);
        isNormalized = false;
        return newDot;
    }

    /**
	 * Inserts a dot at a given position in the list of dots.
	 * 
	 * @param dot
	 *            the dot to be inserted
	 * @param idx
	 *            the position in the list of dots where the dot is supposed to
	 *            be inserted
	 * @throws IllegalParameterException
	 *             the passed dot is already inserted
	 */
    protected void insertDotAt(TransitionDot dot, int idx) throws IllegalParameterException {
        if (dots.contains(dot)) throw new IllegalParameterException(Transition.class.getCanonicalName() + " / passed dot is already inserted / not inserting dot");
        dots.add(idx, dot);
        isNormalized = false;
    }

    /**
	 * Getter.
	 * 
	 * @return wether or not the transition counts as normalized
	 */
    public boolean isNormalized() {
        return isNormalized;
    }

    /**
	 * Checks if start and end selectableOvals are identical.
	 * 
	 * @return true if both objects are the same <br>
	 *         false if the differ
	 */
    public boolean isSelfLoop() {
        return start.equals(end);
    }

    /**
	 * The transition gets normalized, this means the number and location of
	 * dots are resetted.
	 * 
	 * @see TransitionDot#TransitionDot(Point, Transition)
	 * @see #addDot(Point)
	 * @see #setTextAnchor(TransitionDot)
	 * @throws IllegalParameterException
	 *             see TransitionDot#TransitionDot<br>
	 *             see #addDot<br>
	 *             see #setTextAnchor
	 * 
	 */
    protected void normalize() throws IllegalParameterException {
        dots = new Vector<TransitionDot>();
        textAnchor = null;
        if (start != end) {
            if (textAnchor == null) textAnchor = new TransitionDot(new Point((start.getPosition().x + end.getPosition().x) / 2, (start.getPosition().y + end.getPosition().y) / 2), this);
            addDot(textAnchor.getPosition());
        } else {
            addDot(new Point(start.getPosition().x, start.getPosition().y - 70));
            addDot(new Point(start.getPosition().x + 20, start.getPosition().y - 40));
        }
        setTextAnchor(getNormalAnchor());
        isNormalized = true;
    }

    /**
	 * Everything from todo is read until two percent chars are read.
	 * <p>
	 * The read chars are appended to done.
	 * 
	 * @param todo
	 *            read from here
	 * @param done
	 *            write to this buffer
	 */
    private void readToDoublePercent(StringBuffer done, StringBuffer todo) {
        boolean first = false;
        while (todo.length() > 0) {
            char c = todo.charAt(0);
            done.append(c);
            todo.deleteCharAt(0);
            if (c == '%') {
                if (first == true) break;
                first = true;
            } else first = false;
        }
    }

    /**
	 * Removes a given dot from the list of dots.
	 * <p>
	 * This method should only be called if the number of dots remaining does
	 * not imply a normalisation.
	 * 
	 * @param dot
	 *            the dot to be removed
	 */
    protected void removeDot(TransitionDot dot) {
        dots.remove(dot);
    }

    /**
	 * Removes a passed operation from this transitions list of operations.
	 * 
	 * @param t
	 *            the tape the operation works on
	 * @param hm
	 *            the HeadMove the operation does
	 * @param read
	 *            the symbol read by the transition
	 * @param write
	 *            the symbol the operation writes.
	 */
    protected TransitionOpns removeOpn(Tape t, HeadMove hm, Symbol read, Symbol write) {
        for (TransitionOpns it : opns) {
            if (it.getTape() == t && it.getHm().equals(hm) && (read != null && it.getReadSymb() == read || it.getReadSymb().isBlank() && read.isBlank() || it.getReadSymb().isEpsilon() && read.isEpsilon()) && (write != null && it.getWriteSymb() == write || it.getWriteSymb().isBlank() && write.isBlank() || it.getWriteSymb().isEpsilon() && write.isEpsilon())) {
                opns.remove(it);
                return it;
            }
        }
        return null;
    }

    /**
	 * Setter.
	 * 
	 * @param expr
	 *            the new expression for the textanchor
	 */
    protected void setAnchorExp(String expr) {
        anchorExpr = expr;
    }

    /**
	 * Setter.
	 * 
	 * @param pTextAnchor
	 *            the new position for the textanchor
	 * @see TransitionDot#TransitionDot(Point, Transition)
	 * @throws IllegalParameterException
	 *             see TransitionDot#TransitionDot
	 */
    protected void setTextAnchor(TransitionDot pTextAnchor) throws IllegalParameterException {
        if (pTextAnchor == null) return;
        textAnchor = new TransitionDot(pTextAnchor.getPosition(), this);
    }

    /**
	 * Notifies the transition that it is no longer normalized.
	 */
    protected void setUnNormalized() {
        isNormalized = false;
    }

    /**
	 * Returns a string representation of the transition.
	 * 
	 * @return a string representation of the transition
	 */
    @Override
    public String toString() {
        return start.getName() + "," + end.getName() + "[" + opns.size() + "]";
    }
}
