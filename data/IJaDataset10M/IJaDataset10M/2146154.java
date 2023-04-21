package net.sf.mailand.smtp.command;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import net.sf.mailand.MailUtil;
import net.sf.mailand.ParseException;
import net.sf.mailand.ProtocolReader;

public class QuitCommand extends AbstractCommand {

    public static final String TYPE = "QUIT";

    public static final QuitCommand QUIT = new QuitCommand();

    private static final String QUIT_STRING = TYPE + MailUtil.CRLF;

    private QuitCommand() {
    }

    public String getType() {
        return TYPE;
    }

    public void serialize(OutputStream out) throws IOException {
        Writer writer = new OutputStreamWriter(out, MailUtil.ENCODING);
        writer.write(QUIT_STRING);
        writer.flush();
    }

    @Override
    QuitCommand parse(ProtocolReader reader) throws IOException, ParseException {
        reader.readLine(0);
        return QUIT;
    }

    public static void register(Map<String, AbstractCommand> commands) {
        commands.put(TYPE, QUIT);
    }
}
