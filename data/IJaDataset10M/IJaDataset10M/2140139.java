package org.jmlspecs.jml4.compiler;

import java.util.Stack;

/**
 * <p>This is a helper class for the scanner. It helps manage which state the scanner is in relative to JML annotations and nested Java comments within JML annotations.</p>
 * <p>From the perspective of this class, the scanner is in one of the following states: (1) Not within a JML annotation. (2) Within a JML annotation but not within a nested Java comment. (3) Within a Java comment nested within a JML annotation. and (although this is beyond what is normally permitted by JML syntax) (4) Within a JML annotation nested within a JML annotation.</p>
 * <p>We  allow (4) for now because it is a convenient means of supporting JML4-only syntax while allowing the overall source to remain JML2 compatible. E.g.</p>
 * <blockquote>
 * <pre>//@ public int array_of_int[/*#@non_null*\/];</pre>
 * </blockquote>
 * Case 3 can take various forms such as:
 * <blockquote>
 * <pre>//@ // Java comment within JML annotation. 
 * //@ /* Java comment within JML annotation. *\/ 
 * /*@ // Java comment within JML annotation.
 *   @...
 *   @ *\/</pre>
 * </blockquote>
 */
public class JmlScannerAnnotationState {

    public static final int NOT_IN = 0;

    public static final int SINGLE_LINE = 1;

    public static final int MULTI_LINE = 2;

    public static final int MAX_JML_ANNOTATION_NESTIN_LEVEL = 2;

    State top = new State();

    Stack savedState = new Stack();

    private int outerJmlAnnotationState = NOT_IN;

    private int jmlAnnotationState = NOT_IN;

    private int nestedJavaCommentInJmlAnnotationState = NOT_IN;

    public int jmlAnnotationState() {
        return this.jmlAnnotationState;
    }

    /**
	 * @return true iff we are in a JML annotation but not within a nested Java comment.
	 */
    public boolean processJml() {
        return this.jmlAnnotationState != NOT_IN && this.nestedJavaCommentInJmlAnnotationState == NOT_IN;
    }

    public int nestedJavaCommentInJmlAnnotationState() {
        return this.nestedJavaCommentInJmlAnnotationState;
    }

    public void startJmlAnnotation(boolean isSingleLine) {
        this.outerJmlAnnotationState = this.jmlAnnotationState;
        this.jmlAnnotationState = isSingleLine ? SINGLE_LINE : MULTI_LINE;
    }

    public void resetJmlAnnotationState() {
        this.outerJmlAnnotationState = NOT_IN;
        this.jmlAnnotationState = NOT_IN;
        resetNestedJavaCommentInJmlCommentState();
    }

    public void startNestedJavaCommentInJmlAnnotation(boolean isSingleLine) {
        this.nestedJavaCommentInJmlAnnotationState = isSingleLine ? SINGLE_LINE : MULTI_LINE;
    }

    public void resetNestedJavaCommentInJmlCommentState() {
        this.nestedJavaCommentInJmlAnnotationState = NOT_IN;
    }

    public boolean endOfSingleLineComment() {
        if (this.nestedJavaCommentInJmlAnnotationState == SINGLE_LINE) {
            resetNestedJavaCommentInJmlCommentState();
            this.endOfSingleLineComment();
            return true;
        } else if (this.jmlAnnotationState == SINGLE_LINE) {
            this.jmlAnnotationState = this.outerJmlAnnotationState;
            this.outerJmlAnnotationState = NOT_IN;
            return true;
        }
        return false;
    }

    public boolean endOfMultiLineComment() {
        if (this.nestedJavaCommentInJmlAnnotationState == MULTI_LINE) {
            resetNestedJavaCommentInJmlCommentState();
            return true;
        } else if (this.jmlAnnotationState == MULTI_LINE) {
            this.jmlAnnotationState = this.outerJmlAnnotationState;
            this.outerJmlAnnotationState = NOT_IN;
            return true;
        }
        return false;
    }

    public boolean maxAnnotationNestingReached() {
        return this.jmlAnnotationNestingLevel() >= MAX_JML_ANNOTATION_NESTIN_LEVEL;
    }

    public int jmlAnnotationNestingLevel() {
        if (this.jmlAnnotationState == NOT_IN) {
            return 0;
        } else if (this.outerJmlAnnotationState == NOT_IN) return 1; else return 2;
    }

    public boolean canNestJmlAnnotation(boolean isSingleLineComment) {
        return !this.maxAnnotationNestingReached() && (isSingleLineComment ? this.jmlAnnotationState != SINGLE_LINE : this.jmlAnnotationState != MULTI_LINE);
    }

    public String toString() {
        return "[JML Annotation: " + stateToString(this.jmlAnnotationState) + ", " + "Nest Java comment: " + stateToString(this.nestedJavaCommentInJmlAnnotationState) + "]";
    }

    public String stateToString(int state) {
        switch(state) {
            case NOT_IN:
                return "NOT_IN";
            case SINGLE_LINE:
                return "SINGLE_LINE";
            case MULTI_LINE:
                return "MULTI_LINE";
            default:
                return "UNKNOWN STATE";
        }
    }
}

class State {

    public int state;

    public boolean isJmlAnnotation;

    public State() {
        this.state = JmlScannerAnnotationState.NOT_IN;
        this.isJmlAnnotation = true;
    }
}
