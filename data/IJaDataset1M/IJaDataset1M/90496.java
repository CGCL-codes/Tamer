package org.knopflerfish.bundle.httpconsole;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * A Command represents an item in teh ConsoleServlet that
 * can be activated by a certain trigger. When triggered, the
 * <tt>run</tt> method is called. For each request, the
 * <tt>HTMLable.toHTML</tt> method is called to produce the
 * output.
 */
public interface Command extends HTMLable {

    /**
   * Command wants full screen when run
   */
    public static int DISPLAY_FULLSCREEN = 0x0001;

    /**
   * Command wants compact listing of bundles when run
   */
    public static int DISPLAY_COMPACTLIST = 0x0002;

    /**
   * Returns a bit-wise combination of the DISPLAY flags
   */
    public int getDisplayFlags();

    /**
   * Called when ConsoleServet har decided the command is triggered.
   * Typically, this happens when <tt>isTrigger</tt> has returned
   * true.
   */
    public StringBuffer run(HttpServletRequest request);

    /**
   * Id of the command. Typically used to identify a submit button.
   */
    public String getId();

    /**
   * Should return true if command thinks the specified request
   * should trigger the <tt>run</tt> method.
   */
    public boolean isTrigger(HttpServletRequest request);

    /**
   * Human-readable name of command.
   */
    public String getName();

    /**
   * Human-readable description of command.
   */
    public String getDescription();

    /**
   * URL string to command icon. Can be null.
   */
    public String getIcon();
}
