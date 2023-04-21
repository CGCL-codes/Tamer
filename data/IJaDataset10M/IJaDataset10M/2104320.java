package de.sopra.model.execution;

import de.sopra.controller.Visitor;
import de.sopra.exceptions.ReferencesOverflowException;
import de.sopra.model.Priority;
import de.sopra.model.Time;
import de.sopra.model.Visitable;
import de.sopra.model.preparation.TestCase;
import de.sopra.model.preparation.TestElement;
import de.sopra.model.preparation.TestSequence;
import de.sopra.model.userdata.User;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Saves data for each test execution that was begun. Is contained in the
 * {@link TestExecutionContainer}.
 * 
 * @author Jens M&uuml;ller
 * 
 */
public class TestExecution implements ReadableTestExecution, Visitable {

    /**
     * The upper-level <code>TestSequence</code> that was executed.
     */
    private ExecutedTestSequence executedTestSequence;

    /**
     * Format used for the date in the string representation of an execution.
     */
    private final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH':'mm");

    /**
     * The Comment for the <code>TestExecution</code>
     */
    private String comment = "";

    /**
     * The Date on which the execution was started.
     */
    private Calendar startingDate;

    /**
     * The time on which the execution was completed.
     */
    private Time finishingTime;

    /**
     * The {@link Priority} from which on up the
     * <code>{@link TestCase test cases}</code> should be executed.
     */
    private Priority priority;

    /**
     * Notes concerning the System Under Test.
     */
    private String noteSUT = "";

    /**
     * Whether the <code>TestExecution</code> was cancelled.
     */
    private boolean cancelled;

    private User author;

    /**
     * Final comment for the <code>TestExecution</code>.
     */
    private String finalComment = "";

    /**
     * {@inheritDoc}
     */
    public boolean getCancelled() {
        return cancelled;
    }

    /**
     * {@inheritDoc}
     */
    public String getComment() {
        return comment;
    }

    /**
     * {@inheritDoc}
     */
    public String getFinalComment() {
        return finalComment;
    }

    /**
     * {@inheritDoc}
     */
    public Priority getLowestPriority() {
        return priority;
    }

    /**
     * {@inheritDoc}
     */
    public String getNoteSUT() {
        return noteSUT;
    }

    /**
     * {@inheritDoc}
     */
    public Time getFinishingTime() {
        return finishingTime;
    }

    /**
     * {@inheritDoc}
     */
    public Calendar getStartingDate() {
        return startingDate;
    }

    /**
     * @param date
     *            The starting date on which the execution was begun.
     */
    public void setStartingDate(Calendar date) {
        startingDate = date;
    }

    /**
     * @param newValue
     *            The lowest priority of which test sequences are executed in
     *            this execution.
     */
    public void setLowestPriority(Priority newValue) {
        priority = newValue;
    }

    /**
     * @param comment
     *            A general comment for the whole execution.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @param note
     *            A note to describe the system under test.
     */
    public void setNoteSUT(String note) {
        this.noteSUT = note;
    }

    /**
     * @param cancelled
     *            true, if the test execution has been cancelled; false, if the
     *            test execution has completely been executed
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * @param comment
     *            A final comment at the end of the execution (even if it was
     *            cancelled).
     */
    public void setFinalComment(String comment) {
        finalComment = comment;
    }

    /**
     * @param finishingTime
     *            The finishing time of the execution.
     */
    public void setFinishingTime(Time finishingTime) {
        this.finishingTime = finishingTime;
    }

    /**
     * @param topLevelSequence
     *            The {@link TestSequence} that has been executed.
     */
    public void setExecutedTestSequence(ExecutedTestSequence topLevelSequence) {
        executedTestSequence = topLevelSequence;
    }

    /**
     * {@inheritDoc}
     */
    public void accept(Visitor visitor, Object o) {
        visitor.visit(this, o);
    }

    /**
     * {@inheritDoc}
     */
    public ExecutedTestSequence getExecutedTestSequence() {
        return executedTestSequence;
    }

    /**
     * @return the author of the <code>TestExecution</code>
     * @see ReadableTestExecution#getAuthor()
     */
    public User getAuthor() {
        return this.author;
    }

    /**
     * Assigns a new author to the <code>TestExecution</code> and changes the
     * references of the old and new author accordingly.
     * 
     * @param author
     *            the new author of the <code>TestExecution</code>
     * @throws ReferencesOverflowException
     *             if the new author is author of too many documents
     */
    public void setAuthor(User author) throws ReferencesOverflowException {
        if (this.author != null) {
            this.author.decrementReferences();
        }
        if (author != null) {
            author.incrementReferences();
        }
        this.author = author;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder retString = new StringBuilder();
        if (executedTestSequence != null) retString.append(executedTestSequence.toString() + "  ");
        if (startingDate != null) {
            retString.append(DateFormat.getDateInstance().format(startingDate.getTime()) + " " + TIME_FORMAT.format(startingDate.getTime()) + " ");
        }
        if (author != null) {
            retString.append(author.toString());
        }
        return retString.toString();
    }

    /**
     * @see de.sopra.model.execution.ReadableTestExecution#getFiltered(short)
     */
    public TestExecution getFiltered(short filter) {
        TestExecution retEx = new TestExecution();
        retEx.author = getAuthor();
        retEx.setCancelled(getCancelled());
        retEx.setFinalComment(getComment());
        retEx.setFinishingTime(getFinishingTime());
        retEx.setNoteSUT(getNoteSUT());
        retEx.setStartingDate(getStartingDate());
        retEx.setExecutedTestSequence(filterSequenceTree(getExecutedTestSequence(), filter));
        return retEx;
    }

    /**
     * Walks through the tree of test sequences, copies all seqeunces, but into
     * them only cases matching the filter.
     * 
     * @param element
     *            The sequence to filter.
     * @param filter
     *            One of the constants defines in ExecutedTestCase.
     * @return A copy of the <code>{@link ExecutedTestSequence}</code>
     *         containing only test cases that match the filter.
     */
    private ExecutedTestSequence filterSequenceTree(ExecutedTestSequence element, short filter) {
        ExecutedTestSequence retSeq = new ExecutedTestSequence(element);
        for (TestElement subElement : element) {
            if (subElement instanceof ExecutedTestCase) {
                short elementSuccess = ((ExecutedTestCase) subElement).getSuccess();
                if ((elementSuccess & filter) != 0) {
                    retSeq.add(subElement);
                }
            } else if (subElement instanceof ExecutedTestSequence) {
                retSeq.add(filterSequenceTree((ExecutedTestSequence) subElement, filter));
            }
        }
        return retSeq;
    }
}
