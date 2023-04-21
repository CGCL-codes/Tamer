package org.apache.isis.extensions.bdd.common;

import java.util.ArrayList;
import java.util.List;
import org.apache.isis.extensions.bdd.common.util.Strings;

public abstract class CellBinding {

    private boolean found = false;

    private int column = -1;

    private StoryCell headCell = null;

    private StoryCell currentCell;

    private final String name;

    private final List<String> headTexts;

    private final boolean autoCreate;

    private final boolean ditto;

    private final boolean optional;

    private boolean dittoed;

    protected CellBinding(final String name, final boolean autoCreate, final boolean ditto, final boolean optional, final String[] headTexts) {
        this.name = name;
        this.autoCreate = autoCreate;
        this.ditto = ditto;
        this.optional = optional;
        if (headTexts.length == 0) {
            throw new IllegalArgumentException("Require at least one heading text");
        }
        final List<String> headTextList = new ArrayList<String>();
        for (final String headText : headTexts) {
            headTextList.add(Strings.camel(headText).toLowerCase());
        }
        this.headTexts = headTextList;
    }

    /**
     * Whether the head cell was found.
     */
    public boolean isFound() {
        return found;
    }

    /**
     * The column that is found, if any.
     */
    public int getColumn() {
        return column;
    }

    /**
     * The head cell with the text, if any.
     */
    public StoryCell getHeadCell() {
        return headCell;
    }

    /**
     * Indicate that the head cell has been found.
     */
    public void foundHeadColumn(final int column, final StoryCell headCell) {
        this.found = true;
        set(column, headCell);
    }

    /**
     * Indicate that the head column.
     */
    public void setHeadColumn(final int column) {
        this.found = true;
        set(column, null);
    }

    /**
     * Indicate that the head cell was not found and has been created.
     */
    public void create(final int column, final StoryCell headCell) {
        set(column, headCell);
    }

    private void set(final int column, final StoryCell headCell) {
        this.column = column;
        this.headCell = headCell;
    }

    public boolean matches(final String candidateText) {
        final String candidateTextCamelLower = Strings.camel(candidateText).toLowerCase();
        for (final String headText : getHeadTexts()) {
            if (headText.equalsIgnoreCase(candidateTextCamelLower)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Holds onto a current (body) cell.
     */
    public StoryCell getCurrentCell() {
        return currentCell;
    }

    /**
     * @see #getCurrentCell()
     */
    public void setCurrentCell(final StoryCell cell) {
        this.currentCell = cell;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getName() {
        return name;
    }

    /**
	 * Captures the current, but checking that the column in which the
	 * {@link StoryCell value} has been provided corresponds to the
	 * {@link #getColumn() column} of this binding.
	 * 
	 * @see #captureCurrent(StoryCell)
	 */
    public void captureCurrent(final StoryCell cell, final int column) {
        if (column != getColumn()) {
            return;
        }
        captureCurrent(cell);
    }

    /**
	 * Captures the current {@link StoryCell value} for this binding. 
	 * 
	 * <p>
	 * For implementations where we already know that the value provided is for this
	 * particular binding.
	 */
    public void captureCurrent(final StoryCell cell) {
        final StoryCell previousCell = getCurrentCell();
        setCurrentCell(cell);
        boolean shouldDitto = Strings.emptyString(cell.getText());
        boolean canDitto = isDitto() && previousCell != null;
        if (shouldDitto && canDitto) {
            ditto(previousCell);
            dittoed = true;
        } else {
            dittoed = false;
        }
    }

    /**
	 * Whether the most recent call to {@link #captureCurrent(StoryCell)} resulted in a ditto.
	 */
    public boolean isDittoed() {
        return dittoed;
    }

    private void ditto(final StoryCell previousCell) {
        copy(previousCell, getCurrentCell());
    }

    protected abstract void copy(final StoryCell from, StoryCell to);

    @Override
    public String toString() {
        return found ? ("found, current=" + getCurrentCell().getText()) : "not found";
    }

    public boolean isAutoCreate() {
        return autoCreate;
    }

    public List<String> getHeadTexts() {
        return headTexts;
    }

    public boolean isDitto() {
        return ditto;
    }
}
