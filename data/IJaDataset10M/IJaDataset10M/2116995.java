package xhack.util;

/**
 *
 */
public interface Logger {

    public static final int SAVING = 0;

    public static final int LOADING = 1;

    public static final int WRITING = 2;

    public static final int CREATING = 3;

    public static final int PAINTING = 4;

    public static final String[] ACTIONS = { "saving", "loading", "writing", "creating", "painting" };

    /**
   * Begin a timed action.
   * @param action int constant representing the action type from {@link #ACTIONS}
   * @param name String target of the action
   */
    public void begin(int action, String name);

    /**
   * End a timed action.  If action was unsuccessful, use {@link #failed}
   * instead.
   */
    public void done();

    /**
   * End a timed action with the given text.
   * @param text String
   */
    public void done(String text);

    /**
   * Flush the output buffer.
   */
    public void flush();

    /**
   * A timed action ended in failure.  If the action succeeded, use
   * {@link #done} instead.
   */
    public void failed();

    /**
   * Begin a major section.
   * @param action int
   * @param name String
   */
    public void beginSection(int action, String name);

    /**
   * End a major section.
   */
    public void doneSection();

    /**
   * Print a blank line.
   */
    public void println();

    /**
   * Print a line of text.
   * @param text String
   */
    public void println(String text);

    /**
   * Print a line of text and a newline
   * @param text String
   */
    public void print(String text);
}
