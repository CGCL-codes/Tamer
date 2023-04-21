package com.microfly.compiler;

import com.microfly.exception.NpsException;
import com.microfly.util.Utils;
import java.io.Reader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 * a new publishing system
 * Copyright (c) 2007</p>
 * @author jialin
 * @version 1.0
 */
public class JavaWriter {

    public static int TAB_WIDTH = 2;

    public static String SPACES = "                              ";

    private int indent = 0;

    private int virtual_indent = 0;

    PrintWriter writer;

    private int javaLine = 1;

    public JavaWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public void close() throws IOException {
        writer.close();
    }

    public int getJavaLine() {
        return javaLine;
    }

    public void pushIndent() {
        virtual_indent += TAB_WIDTH;
        if (virtual_indent >= 0 && virtual_indent <= SPACES.length()) {
            indent = virtual_indent;
        }
    }

    public void popIndent() {
        virtual_indent -= TAB_WIDTH;
        if (virtual_indent >= 0 && virtual_indent <= SPACES.length()) {
            indent = virtual_indent;
        }
    }

    public void print(Object o) {
        if (o == null) return;
        if (o instanceof Reader) {
            try {
                java.io.Reader r = (Reader) o;
                WordLimitWriter aWriter = new WordLimitWriter(writer);
                com.microfly.util.Utils.GetText(r, aWriter);
                try {
                    r.close();
                } catch (Exception e) {
                }
            } catch (Exception e) {
                com.microfly.util.DefaultLog.error_noexception(e);
            }
            return;
        } else if (o instanceof String) {
            print(Utils.TransferToHtmlEntity((String) o));
            return;
        } else if (o instanceof java.util.Date) {
            print(com.microfly.util.Utils.FormateDate((java.util.Date) o, "yyyy-MM-dd"));
            return;
        }
        print(o.toString());
    }

    public void print(Object o, String format) {
        if (o == null) return;
        if (o instanceof java.util.Date) {
            print(com.microfly.util.Utils.FormateDate((java.util.Date) o, format));
            return;
        }
        if (o instanceof java.lang.Number) {
            print(com.microfly.util.Utils.FormateNumber(o, format));
            return;
        }
        if (o instanceof String) {
            print(Utils.TransferToHtmlEntity((String) o));
            return;
        }
        if (o instanceof java.io.Reader) {
            WordLimitWriter aWriter = new WordLimitWriter(writer);
            if (format.equalsIgnoreCase("0") || format.equalsIgnoreCase("text")) {
                try {
                    java.io.Reader r = (Reader) o;
                    com.microfly.util.Utils.GetFlatText(r, aWriter);
                    try {
                        r.close();
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                    try {
                        com.microfly.util.DefaultLog.error(e);
                    } catch (Exception e2) {
                    }
                }
                return;
            } else {
                try {
                    java.io.Reader r = (Reader) o;
                    com.microfly.util.Utils.GetText(r, aWriter);
                    try {
                        r.close();
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                    try {
                        com.microfly.util.DefaultLog.error(e);
                    } catch (Exception e2) {
                    }
                }
                return;
            }
        }
        print(o.toString());
    }

    public void print(Object o, int wordcount) throws NpsException {
        print(o, wordcount, "");
    }

    public void print(Object o, int wordcount, String append) throws NpsException {
        if (o == null) return;
        if (o instanceof String) {
            String s = o.toString();
            if (wordcount <= 0 || wordcount >= s.length()) print(s); else print(s.substring(0, wordcount));
            return;
        }
        if (o instanceof java.io.Reader) {
            WordLimitWriter aWriter = new WordLimitWriter(writer, wordcount, append);
            try {
                java.io.Reader r = (Reader) o;
                com.microfly.util.Utils.GetFlatText(r, aWriter);
                try {
                    r.close();
                } catch (Exception e) {
                }
            } catch (Exception e) {
                try {
                    com.microfly.util.DefaultLog.error(e);
                } catch (Exception e2) {
                }
            }
            return;
        }
        print(o.toString());
    }

    /**
     * Prints the given string followed by '\n'
     */
    public void println(String s) {
        javaLine++;
        writer.println(s);
    }

    /**
     * Prints a '\n'
     */
    public void println() {
        javaLine++;
        writer.println("");
    }

    /**
     * Prints the current indention
     */
    public void printin() {
        writer.print(SPACES.substring(0, indent));
    }

    /**
     * Prints the current indention, followed by the given string
     */
    public void printin(String s) {
        writer.print(SPACES.substring(0, indent));
        writer.print(s);
    }

    /**
     * Prints the current indention, and then the string, and a '\n'.
     */
    public void printil(String s) {
        javaLine++;
        writer.print(SPACES.substring(0, indent));
        writer.println(s);
    }

    /**
     * Prints the given char.
     *
     * Use println() to print a '\n'.
     */
    public void print(char c) {
        writer.print(c);
    }

    /**
     * Prints the given int.
     */
    public void print(int i) {
        writer.print(i);
    }

    /**
     * Prints the given string.
     *
     * The string must not contain any '\n', otherwise the line count will be
     * off.
     */
    public void print(String s) {
        writer.print(s);
    }

    /**
     * Prints the given string.
     *
     * If the string spans multiple lines, the line count will be adjusted
     * accordingly.
     */
    public void printMultiLn(String s) {
        int index = 0;
        while ((index = s.indexOf('\n', index)) > -1) {
            javaLine++;
            index++;
        }
        writer.print(s);
    }
}
