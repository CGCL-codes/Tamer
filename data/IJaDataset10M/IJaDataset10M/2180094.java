package net.sf.japi.tools.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import net.sf.japi.io.args.ArgParser;
import net.sf.japi.io.args.BasicCommand;
import net.sf.japi.io.args.Option;
import net.sf.japi.io.args.OptionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A very simple and primitive mail command.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @since 0.1
 */
public class Mail extends BasicCommand {

    /** The default welcome name. */
    private static final String DEFAULT_HELO_NAME = "JapiMail";

    /** The default port. */
    private static final int DEFAULT_PORT = 25;

    /** The sender. */
    private String from;

    /** The welcome name. */
    private String heloName = DEFAULT_HELO_NAME;

    /** The smtp server to communicate with. */
    private String server;

    /** The port. */
    private int port = DEFAULT_PORT;

    /** Whether or not to print debug messages. */
    private boolean debug;

    /** Whether or not to use simple smtp instead of enhanced smtp. */
    private boolean simple;

    /** The msg to send.
     * Maybe <code>null</code> which means the message will be read from stdin.
     */
    @Nullable
    private String msg;

    /** Main program.
     * @param args Command line arguments (try --help)
     */
    public static void main(final String... args) {
        ArgParser.simpleParseAndRun(new Mail(), args);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    public int run(@NotNull final List<String> args) throws IOException {
        final PingPongSession session = new PingPongSession(new Socket(server, port));
        session.setDebug(debug);
        try {
            session.waitFor("220");
            session.sendAndWait((simple ? "HELO" : "EHLO") + " " + heloName, "250");
            session.sendAndWait("MAIL from: " + from, "250");
            for (final String arg : args) {
                session.sendAndWait("RCPT to: " + arg, "250");
            }
            session.sendAndWait("DATA", "354");
            if (msg == null) {
                @SuppressWarnings({ "IOResourceOpenedButNotSafelyClosed" }) final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String line;
                while ((line = in.readLine()) != null && !".".equals(line)) {
                    session.send(line);
                }
            } else {
                session.send(msg);
            }
            session.sendAndWait(".", "250");
            session.sendAndWait("QUIT", "221");
        } finally {
            session.close();
        }
        return 0;
    }

    /** Sets the server.
     * @param server Server.
     */
    @Option(type = OptionType.REQUIRED, value = { "s", "server" })
    public void setServer(@NotNull final String server) {
        this.server = server;
    }

    /** Sets the port.
     * @param port Port.
     */
    @Option({ "p", "port" })
    public void setPort(@Nullable final Integer port) {
        this.port = port != null ? port : DEFAULT_PORT;
    }

    /** Sets the sender.
     * @param from The sender.
     */
    @Option(type = OptionType.REQUIRED, value = { "f", "from" })
    public void setFrom(@NotNull final String from) {
        this.from = from;
    }

    /** Turns on debugging. */
    @Option({ "d", "debug" })
    public void setDebug() {
        debug = true;
    }

    /** Turns off enhanced smtp. */
    @Option({ "simple" })
    public void setSimple() {
        simple = true;
    }

    /** Sets the name to use for HELO / EHLO.
     * @param heloName Name to use for HELO / EHLO.
     */
    @Option({ "heloName" })
    public void setHeloName(@NotNull final String heloName) {
        this.heloName = heloName;
    }

    /** Sets the message to send.
     * @param msg Message to send (without trailing "." line).
     */
    public void setMsg(@Nullable final String msg) {
        this.msg = msg;
    }

    /** Send mail.
     * @param server Server address.
     * @param port Server port.
     * @param from Sender address.
     * @param msg Message to send (without trailing "." line).
     * @param rcpts Recipient addresses.
     * @throws IOException in case of I/O problems.
     */
    public static void sendMail(@NotNull final String server, final int port, @NotNull final String from, @NotNull final String msg, @NotNull final String... rcpts) throws IOException {
        final Mail mail = new Mail();
        mail.setServer(server);
        mail.setPort(port);
        mail.setFrom(from);
        mail.setMsg(msg);
        mail.run(Arrays.asList(rcpts));
    }
}
