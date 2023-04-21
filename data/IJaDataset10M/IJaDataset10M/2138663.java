package sk.naive.talker.server;

import sk.naive.talker.*;
import sk.naive.talker.util.Utils;
import sk.naive.talker.command.CommandException;
import sk.naive.talker.props.*;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.regex.*;

/**
 * UserMessageProcessor processes user messages that will became commands (no callback processing is done here).
 * <p>
 * First step is processing corrections, user's macros, global macros (depending on enabled settings).
 *
 *
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.7 $ $Date: 2005/01/25 21:57:05 $
 */
public class UserMessageProcessor {

    private Talker talker;

    private User user;

    private String message;

    public UserMessageProcessor(Talker talker, User user, String message) {
        this.talker = talker;
        this.user = user;
        this.message = message;
    }

    public void process() throws RemoteException, PropertyStoreException, CommandException {
        message = message.trim();
        if (message.length() == 0) {
            return;
        }
        message = processMacros(user, message);
        if (message.length() == 0) {
            return;
        }
        String commandSign = (String) user.get(User.UPROP_COMMAND_SIGN);
        if (commandSign == null || message.startsWith(commandSign)) {
            if (commandSign != null) {
                message = message.substring(commandSign.length());
            }
            talker.getCommandDispatcher().process(message, user);
        } else {
            String defaultCommand = (String) user.get(User.UPROP_DEFAULT_COMMAND);
            if (isCommandMode(defaultCommand)) {
                talker.getCommandDispatcher().process(message, user);
            } else {
                talker.getCommandDispatcher().process(defaultCommand, user, message);
            }
        }
    }

    private boolean isCommandMode(String defaultCommand) {
        return defaultCommand == null || defaultCommand.length() == 0;
    }

    private String processMacros(User user, String message) throws PropertyStoreException {
        String delim = "";
        if (((String) user.get(User.UPROP_MACRO_SPACES_DELIM)).equals(BooleanProperty.TRUE)) {
            delim = " ";
        }
        if (((String) user.get(User.UPROP_CORRECTIONS_ENABLED)).equals(BooleanProperty.TRUE)) {
            message = processCorrections((Map<String, String>) user.get(User.UPROP_CORRECTIONS), message);
        }
        if (((String) user.get(User.UPROP_MACRO_USERS)).equals(BooleanProperty.TRUE)) {
            message = processMacroMap((Map<String, String>) user.get(User.UPROP_MACROS), delim, message);
        }
        if (((String) user.get(User.UPROP_MACRO_GLOBALS)).equals(BooleanProperty.TRUE)) {
            message = processMacroMap((Map<String, String>) talker.get(Consts.TPROP_MACROS), delim, message);
        }
        return message.trim();
    }

    private String processCorrections(Map<String, String> corrections, String message) {
        StringBuilder messageBuffer = new StringBuilder(message);
        for (Map.Entry<String, String> correction : corrections.entrySet()) {
            int ix = 0;
            while ((ix = messageBuffer.indexOf(correction.getKey(), ix)) >= 0) {
                messageBuffer.delete(ix, ix + correction.getKey().length());
                messageBuffer.insert(ix, correction.getValue());
                ix += correction.getValue().length();
            }
        }
        return messageBuffer.toString();
    }

    private String processMacroMap(Map<String, String> macros, String delim, String message) {
        for (Map.Entry<String, String> macro : macros.entrySet()) {
            StringBuilder testedMacroKey = new StringBuilder().append(macro.getKey());
            if (message.length() > testedMacroKey.length()) {
                testedMacroKey.append(delim);
            }
            if (message.startsWith(testedMacroKey.toString())) {
                message = message.substring(testedMacroKey.length()).trim();
                message = processMacro(macro.getKey(), macro.getValue(), message);
                break;
            }
        }
        return message;
    }

    private String processMacro(String paramZero, String macro, String message) {
        int params = analyzeParams(macro);
        StringBuilder messageBuffer = new StringBuilder(macro);
        String[] realParams = Utils.splitWords(message, params + 1);
        int ix = 0;
        while ((ix = messageBuffer.indexOf("$", ix)) >= 0) {
            ix++;
            if (messageBuffer.length() > ix) {
                char c = messageBuffer.charAt(ix);
                if (c == '$') {
                    messageBuffer.deleteCharAt(ix);
                    continue;
                }
                if (c == '*') {
                    ix--;
                    messageBuffer.delete(ix, ix + 2);
                    if (realParams.length > params) {
                        messageBuffer.insert(ix, realParams[params]);
                        ix += realParams[params].length();
                    }
                    continue;
                }
                if (c >= '0' && c <= '9') {
                    ix--;
                    messageBuffer.delete(ix, ix + 2);
                    int parIndex = (c - '0');
                    if (parIndex == 0) {
                        messageBuffer.insert(ix, paramZero);
                        ix += paramZero.length();
                    } else if (realParams.length >= parIndex) {
                        messageBuffer.insert(ix, realParams[parIndex - 1]);
                        ix += realParams[parIndex - 1].length();
                    }
                    continue;
                }
            }
        }
        message = messageBuffer.toString();
        return message;
    }

    private static final Pattern paramPattern = Pattern.compile("\\$([0-9*$])");

    private int analyzeParams(String macroValue) {
        int parMax = 0;
        Matcher matcher = paramPattern.matcher(macroValue);
        while (matcher.find()) {
            String parSpec = matcher.group(1);
            if (parSpec.charAt(0) == '*' || parSpec.charAt(0) == '$') {
                continue;
            }
            int parIndex = Integer.parseInt(parSpec);
            if (parIndex > parMax) {
                parMax = parIndex;
            }
        }
        return parMax;
    }
}
