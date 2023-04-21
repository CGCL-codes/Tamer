package net.sf.hsdd.telnet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.StringTokenizer;
import net.sf.hsdd.conf.usr.AuthInfo;
import net.sf.hsdd.net.DownloadNotFound;
import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.terminal.ColorHelper;
import net.wimpi.telnetd.net.Connection;

public abstract class AbstractDownloadCommand extends AbstractTelnetCommand {

    public AbstractDownloadCommand(String name, String cmdHelp, String longHelp) {
        super(name, cmdHelp, longHelp);
    }

    public void run(String line, BasicTerminalIO tio, Connection connection, AuthInfo authInfo) throws IOException, ShutdownException, StopException {
        StringTokenizer tokens = new StringTokenizer(line);
        int tokenCount = tokens.countTokens();
        if (tokenCount >= 2) {
            tokens.nextToken();
            for (int i = 1; i < tokenCount; i++) {
                String token = tokens.nextToken();
                if (token.indexOf('-') < 0) {
                    String strID = trimAndRemoveQuotes(token);
                    try {
                        int id = Integer.parseInt(strID);
                        try {
                            process(tio, id);
                        } catch (DownloadNotFound e) {
                            tio.write(ColorHelper.colorizeText(MessageFormat.format("Download by specified ID({0}) not found\n", String.valueOf(id)), ColorHelper.RED));
                        }
                    } catch (NumberFormatException ex) {
                        tio.write(ColorHelper.colorizeText(MessageFormat.format("Download ID({0}) must be a integer\n", strID), ColorHelper.RED));
                    }
                } else {
                    StringTokenizer range = new StringTokenizer(token, "-");
                    if (range.countTokens() < 2) {
                        tio.write(ColorHelper.colorizeText(MessageFormat.format("\"{0}\" is wrong range\n", token), ColorHelper.RED));
                    } else {
                        String strNumBegin = range.nextToken();
                        String strNumEnd = range.nextToken();
                        int beginNumber = -1;
                        int endNumber = -1;
                        try {
                            beginNumber = Integer.parseInt(strNumBegin);
                            endNumber = Integer.parseInt(strNumEnd);
                            if (beginNumber < 0 || endNumber < 0 || beginNumber > endNumber) {
                                tio.write(ColorHelper.colorizeText("Wrong range specified\n", ColorHelper.RED));
                            } else if (endNumber - beginNumber > 1000) {
                                tio.write(ColorHelper.colorizeText("Range size must be less or equal 1000\n", ColorHelper.RED));
                            } else {
                                for (int j = beginNumber; j <= endNumber; j++) {
                                    try {
                                        process(tio, j);
                                    } catch (DownloadNotFound ex) {
                                    }
                                }
                            }
                        } catch (NumberFormatException ex) {
                            tio.write(ColorHelper.colorizeText("Wrong number format specified in range\n", ColorHelper.RED));
                        }
                    }
                }
            }
        } else tio.write(ColorHelper.colorizeText("Download ID(s) must be specified\n", ColorHelper.RED));
        tio.flush();
    }

    protected abstract void process(BasicTerminalIO tio, int id) throws DownloadNotFound;
}
