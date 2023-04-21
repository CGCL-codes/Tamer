package net.sf.mailand.smtp.command;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import net.sf.mailand.MailUtil;
import net.sf.mailand.ParseException;
import net.sf.mailand.ProtocolReader;

public class MailCommand extends AbstractCommand {

    public static final String TYPE = "MAIL";

    private static final String FROM = "FROM:";

    private Path reversePath;

    private MailCommand() {
    }

    public MailCommand(Path reversePath) {
        if (reversePath == null) throw new IllegalArgumentException();
        this.reversePath = reversePath;
    }

    public Path reversePath() {
        return reversePath;
    }

    public String getType() {
        return TYPE;
    }

    public void serialize(OutputStream out) throws IOException {
        Writer writer = new OutputStreamWriter(out, MailUtil.ENCODING);
        writer.write(getType());
        writer.write(' ');
        writer.write(FROM);
        writer.write(reversePath().toString());
        writer.write(MailUtil.CRLF);
        writer.flush();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof MailCommand) {
            MailCommand command = (MailCommand) obj;
            return reversePath().equals(command.reversePath());
        } else return false;
    }

    @Override
    public String toString() {
        return getType() + ' ' + FROM + reversePath.toString();
    }

    @Override
    MailCommand parse(ProtocolReader reader) throws IOException, ParseException {
        reader.readSP();
        if (!FROM.equals(reader.read(FROM.length()))) {
            throw new ParseException("Expected: " + '"' + FROM + '"' + '!');
        }
        Path reversePath = new Path(reader.readLine(Path.MAX_LENGTH));
        return new MailCommand(reversePath);
    }

    public static void register(Map<String, AbstractCommand> commands) {
        commands.put(TYPE, new MailCommand());
    }
}
