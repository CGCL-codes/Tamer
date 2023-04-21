package uk.ac.warwick.dcs.cokefolk.ui;

import uk.ac.warwick.dcs.cokefolk.util.Config;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class ConsoleInteraction implements Interaction {

    private static final Logger LOG = Logger.getLogger(ConsoleInteraction.class.getName());

    private final Config config;

    /**
   * If this configuration option is set then sensitive information such as passwords are read
   * straight from standard input without any attempt to protect their secrecy. If the program
   * is run with standard input not redirected then this is a security vulnerability. However,
   * if the program is run with input redirected to come from a file or a pipe, for example,
   * then this option could be useful so that the program can run without interaction from the
   * user. However, appropriate steps should be taken at the operating system level to protect
   * the sensitive information, e.g. an access control list. If no console is available, for
   * example if the program is run as a scheduled task, then this option is implied
   * automatically so it should rarely need to be set pernamently as a configuration option.
   */
    public static final String UNSECURED_INPUT = "io.unsecured";

    public ConsoleInteraction(Config config) {
        this.config = config;
        config.provideDefault(UNSECURED_INPUT, false);
    }

    public synchronized String input(String caption, String prompt) {
        System.out.print(prompt);
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        try {
            String result = stdIn.readLine();
            if (result == null) result = "";
            return result;
        } catch (IOException e) {
            System.err.println("An I/O error has occurred on stdin.");
            return "";
        }
    }

    public synchronized char[] secureInput(String caption, String prompt) {
        if (config.getBoolPref(UNSECURED_INPUT) || System.console() == null) {
            return input(caption, prompt).toCharArray();
        } else {
            return readLineSecure(prompt + ":");
        }
    }

    public synchronized void output(String out) {
        System.out.println(out);
    }

    public synchronized void clear() {
        System.out.println(((char) 27) + "[2J");
        for (int i = 0; i < 25; i++) System.out.println("\n");
    }

    public synchronized void error(String err) {
        System.err.println(err);
    }

    public void activate() {
        return;
    }

    /**
   * Reads and returns some sensitive piece of information (e.g. a password) from the console
   * (i.e. System.in and System.out) in a secure manner.
   * <p>
   * For top security, all console input is masked out while the user types in the password.
   * Once the user presses enter, the password is read via a call to
   * {@link #readLineSecure(PushbackInputStream)}, using a PushbackInputStream that wraps
   * System.in. This method never returns null.
   * @see <a href="http://java.sun.com/features/2002/09/pword_mask.html">Console masking in
   * console</a>
   */
    private static final char[] readLineSecure(String prompt) {
        char[] result = System.console().readPassword();
        if (result != null) return result;
        try {
            System.out.println();
            StreamMasker masker = new StreamMasker(System.out, prompt);
            masker.start();
            PushbackInputStream pin = new PushbackInputStream(System.in);
            int b = pin.read();
            masker.stopMasking();
            try {
                masker.join();
            } catch (InterruptedException e) {
                LOG.warning(e.toString());
            }
            if (b == -1) return new char[0];
            if (System.out.checkError()) throw new IOException("an I/O error occurred on standard output.");
            pin.unread(b);
            return readLineSecure(pin);
        } catch (IOException e) {
            System.err.println(e);
            return new char[0];
        }
    }

    /**
   * Reads chars from in until an end-of-line sequence (EOL) or end-of-file (EOF) is
   * encountered, and then returns the data as a char[].
   * <p>
   * The EOL sequence may be any of the standard formats: '\n' (Unix), '\r' (Mac), "\r\n"
   * (Windows). The EOL sequence is always completely read off the stream but is never included
   * in the result. <i>Note:</i> this means that the result will never contain the chars '\n'
   * or '\r'. In order to guarantee reading thru but not beyond the EOL sequence for all
   * formats (Unix, Mac, Windows), this method requires that a PushbackInputStream and not a
   * more general InputStream be supplied.
   * <p>
   * The code is secure: no Strings are used, only char arrays, and all such arrays other than
   * the result are guaranteed to be blanked out after last use to ensure privacy. Thus, this
   * method is suitable for reading in sensitive information such as passwords.
   * <p>
   * This method never returns null; if no data before the EOL or EOF is read, a zero-length
   * char[] is returned.
   * @throws NullPointerException
   * if
   * @code{in} is null
   * @throws IOException
   * if an I/O problem occurs
   * @see <a
   * href="http://java.sun.com/j2se/1.4.2/docs/guide/security/jce/JCERefGuide.html#PBEEx">Console
   * based encryption code examples from JCE documentation</a>
   */
    private static final char[] readLineSecure(PushbackInputStream in) throws IOException {
        char[] buffer = null;
        try {
            buffer = new char[128];
            int offset = 0;
            loop: while (true) {
                int c = in.read();
                switch(c) {
                    case -1:
                    case '\n':
                        break loop;
                    case '\r':
                        int c2 = in.read();
                        if ((c2 != '\n') && (c2 != -1)) in.unread(c2);
                        break loop;
                    default:
                        buffer = checkBuffer(buffer, offset);
                        buffer[offset++] = (char) c;
                        break;
                }
            }
            char[] result = new char[offset];
            System.arraycopy(buffer, 0, result, 0, offset);
            return result;
        } finally {
            if (buffer != null) Arrays.fill(buffer, ' ');
        }
    }

    /**
   * Checks if buffer is sufficiently large to store an element at an index == offset. If it
   * is, then buffer is simply returned. If it is not, then a new char[] of more than
   * sufficient size is created and initialized with buffer's current elements and returned;
   * the original supplied buffer is guaranteed to be blanked out upon method return in this
   * case.
   * @throws NullPointerException
   * if
   * @code{buffer} is null.
   * @throws IllegalArgumentException
   * if
   * @code{offset}&lt;0
   */
    private static final char[] checkBuffer(char[] buffer, int offset) throws IllegalArgumentException {
        if (offset < 0) throw new IllegalArgumentException("offset = " + offset + " is < 0");
        if (offset < buffer.length) {
            return buffer;
        } else {
            try {
                char[] bufferNew = new char[offset + 128];
                System.arraycopy(buffer, 0, bufferNew, 0, buffer.length);
                return bufferNew;
            } finally {
                Arrays.fill(buffer, ' ');
            }
        }
    }

    /**
   * Masks an InputStream by overwriting blank chars to the PrintStream corresponding to its
   * output. A typical application is for password input masking.
   * <p>
   * @see <a href="http://java.sun.com/features/2002/09/pword_mask.html">Console masking in
   * console</a>
   */
    private static class StreamMasker extends Thread {

        private static final String BLANKS = padString("", ' ', 10, false);

        private final PrintStream out;

        private final String promptOverwrite;

        private volatile boolean doMasking;

        /**
     * Constructor.
     * @throws NullPointerException
     * if
     * @code{out} is null.
     * @throws IllegalArgumentException
     * if prompt contains carriage return or line feed characters.
     */
        public StreamMasker(PrintStream out, String prompt) throws IllegalArgumentException {
            if (out == null) throw new NullPointerException("out");
            if (prompt == null) prompt = "";
            if (prompt.contains("\r")) throw new IllegalArgumentException("prompt contains a carriage return character.");
            if (prompt.contains("\n")) throw new IllegalArgumentException("prompt contains a line feed character.");
            this.out = out;
            String setCursorToStart = padString("", '\b', prompt.length() + BLANKS.length(), false);
            this.promptOverwrite = setCursorToStart + prompt + BLANKS + setCursorToStart + prompt;
        }

        /** Pad a string S with a size of N with char C on the left (true) or on the right(false). */
        public static String padString(String s, char c, int n, boolean paddingLeft) {
            StringBuilder str = new StringBuilder(s);
            int strLength = str.length();
            if (n > 0 && n > strLength) {
                if (paddingLeft) {
                    for (int i = 0; i < n - strLength; i++) str.insert(0, c);
                } else {
                    for (int i = strLength + 1; i <= n; i++) str.append(c);
                }
            }
            return str.toString();
        }

        /**
     * Constantly overwrites out with prompt. This effectively masks any chars coming on out,
     * as long as the masking occurs fast enough.
     * <p>
     * To help ensure that masking occurs when system is in heavy use, the thread will have its
     * priority boosted. Interrupting the calling thread will eventually result in an exit from
     * this method, and the interrupted status of the calling thread will be set to true.
     * @throws RuntimeException
     * if an error in the masking process is detected
     */
        @Override
        public void run() throws RuntimeException {
            this.setPriority(Thread.MAX_PRIORITY);
            doMasking = true;
            while (doMasking) {
                out.print(promptOverwrite);
                if (out.checkError()) throw new RuntimeException("an I/O error occurred on the output stream.");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ie) {
                    interrupt();
                    return;
                }
            }
            yield();
            out.print('\r');
            for (int i = 0; i < promptOverwrite.length(); i++) out.print(' ');
            out.print('\r');
        }

        /** Signals any thread executing run to stop masking and exit run. */
        public void stopMasking() {
            doMasking = false;
        }
    }
}
