package com.c4j.analyser;

/**
 * This is the implementation of the IProblem interface.
 */
public class Problem implements IProblem {

    /** Stores the kind of the problem. */
    private final Type type;

    /** Stores the title of the problem. */
    private final String title;

    /** Stores the detailed message of the problem. */
    private final String message;

    /**
     * Constructs a new problem.
     *
     * @param type
     *         The kind of the problem.
     * @param title
     *         The title of the problem.
     * @param message
     *         The detailed message of the problem.
     */
    public Problem(final Type type, final String title, final String message) {
        this.type = type;
        this.title = title;
        this.message = message;
    }

    @Override
    public final Type getType() {
        return type;
    }

    @Override
    public final String getTitle() {
        return title;
    }

    @Override
    public final String getMessage() {
        return message;
    }
}
