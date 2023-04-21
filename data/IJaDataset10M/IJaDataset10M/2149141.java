package org.freehold.jukebox.util;

/**
 * ANSI color escape sequence support.
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 1995-1998
 * @version $Id: ANSI.java,v 1.1 2000-12-15 23:51:28 vtt Exp $
 */
public class ANSI {

    /**
     * Esc code (0x1b).
     */
    private static String esc = "";

    /**
     * Color sequence.
     *
     * Responsible for rendering a required color.
     */
    protected String seq;

    /**
     * Create the ANSI sequence.
     *
     * The sequence is a parameter prepended by the <b>Esc</b> code
     * (<code>0x1b</code>).
     *
     * @param seq Sequence without the leading <b>Esc</b> character.
     */
    public ANSI(String seq) {
        this.seq = esc + seq;
    }

    public String toString() {
        return seq;
    }

    /**
     * Dark red.
     */
    public static final ANSI red = new ANSI("[0;40;31m");

    /**
     * Dark green.
     */
    public static final ANSI green = new ANSI("[0;40;32m");

    /**
     * Dark yellow, almost brown.
     */
    public static final ANSI yellow = new ANSI("[0;40;33m");

    /**
     * Dark blue.
     */
    public static final ANSI blue = new ANSI("[0;40;34m");

    /**
     * Dark purple.
     */
    public static final ANSI purple = new ANSI("[0;40;35m");

    /**
     * Dark cyan.
     */
    public static final ANSI cyan = new ANSI("[0;40;36m");

    /**
     * Dark white, or just gray.
     */
    public static final ANSI gray = new ANSI("[0;40;37m");

    /**
     * Reverse bright red.
     */
    public static final ANSI reverse_red = new ANSI("[7;40;31m");

    /**
     * Bright red.
     */
    public static final ANSI bright_red = new ANSI("[1;40;31m");

    /**
     * Bright green.
     */
    public static final ANSI bright_green = new ANSI("[1;40;32m");

    /**
     * Reverse bright yellow.
     */
    public static final ANSI reverse_yellow = new ANSI("[7;40;33m");

    /**
     * Bright yellow.
     */
    public static final ANSI bright_yellow = new ANSI("[1;40;33m");

    /**
     * Bright blue.
     */
    public static final ANSI bright_blue = new ANSI("[1;40;34m");

    /**
     * Bright purple.
     */
    public static final ANSI bright_purple = new ANSI("[1;40;35m");

    /**
     * Bright cyan.
     */
    public static final ANSI bright_cyan = new ANSI("[1;40;36m");

    /**
     * White.
     */
    public static final ANSI white = new ANSI("[1;40;37m");

    /**
     * Reverse white.
     */
    public static final ANSI reverse_white = new ANSI("[7;40;37m");
}
