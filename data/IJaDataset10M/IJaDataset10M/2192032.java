package org.dfdaemon.il2.core.console.parser;

import org.dfdaemon.il2.core.command.CommandException;
import org.dfdaemon.il2.core.command.CommandParser;
import org.dfdaemon.il2.core.command.reply.NoReply;
import org.dfdaemon.il2.core.command.request.KickCmd;
import org.dfdaemon.il2.core.util.StringUtils;
import java.io.IOException;
import java.io.Writer;

/**
 * @author aka50
 */
public class KickCmdParser implements CommandParser<NoReply, KickCmd> {

    public void validate(KickCmd cmd) throws IllegalArgumentException {
        if (cmd.callsign == null || cmd.callsign.length() == 0) throw new IllegalArgumentException("playername should be non-null nonzero length string");
    }

    public void writeCommand(KickCmd cmd, Writer writer) throws IOException {
        writer.write("kick \"" + StringUtils.native2ascii(cmd.callsign, true) + "\"\n");
    }

    public NoReply parseInput(KickCmd cmd, String[] lines) {
        for (String line : lines) {
            if (line.indexOf("ERROR") == 0) {
                throw new CommandException(line);
            }
        }
        return NoReply.instance();
    }

    public Class<KickCmd> getCmdClass() {
        return KickCmd.class;
    }
}
