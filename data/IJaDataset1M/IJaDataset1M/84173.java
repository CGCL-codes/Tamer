package com.realtime.crossfire.jxclient.settings;

import com.realtime.crossfire.jxclient.server.crossfire.CrossfireDrawextinfoListener;
import com.realtime.crossfire.jxclient.server.crossfire.CrossfireServerConnection;
import com.realtime.crossfire.jxclient.server.crossfire.MessageTypes;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * Manages macro expansion in command strings.
 * @author Andreas Kirschbaum
 */
public class Macros {

    /**
     * The "reply_to" macro name.
     */
    @NotNull
    private static final String REPLY_TO = "reply_to";

    /**
     * The {@link Pattern} matching macro names.
     */
    @NotNull
    private final Pattern macroPattern = Pattern.compile("<<([a-z_]+)>>");

    /**
     * The macro expansions. Maps macro name to macro expansion.
     */
    @NotNull
    private final Map<String, String> expansions = new HashMap<String, String>();

    /**
     * The {@link CrossfireDrawextinfoListener} for tracking tells.
     */
    @NotNull
    private final CrossfireDrawextinfoListener crossfireDrawextinfoListener = new CrossfireDrawextinfoListener() {

        @Override
        public void commandDrawextinfoReceived(final int color, final int type, final int subtype, @NotNull final String message) {
            switch(type) {
                case MessageTypes.MSG_TYPE_BOOK:
                case MessageTypes.MSG_TYPE_CARD:
                case MessageTypes.MSG_TYPE_PAPER:
                case MessageTypes.MSG_TYPE_SIGN:
                case MessageTypes.MSG_TYPE_MONUMENT:
                case MessageTypes.MSG_TYPE_DIALOG:
                case MessageTypes.MSG_TYPE_MOTD:
                case MessageTypes.MSG_TYPE_ADMIN:
                case MessageTypes.MSG_TYPE_SHOP:
                case MessageTypes.MSG_TYPE_COMMAND:
                case MessageTypes.MSG_TYPE_ATTRIBUTE:
                case MessageTypes.MSG_TYPE_SKILL:
                case MessageTypes.MSG_TYPE_APPLY:
                case MessageTypes.MSG_TYPE_ATTACK:
                    break;
                case MessageTypes.MSG_TYPE_COMMUNICATION:
                    if (subtype == MessageTypes.MSG_TYPE_COMMUNICATION_TELL) {
                        final int index = message.indexOf(" tells you:");
                        if (index != -1) {
                            final String name = message.substring(0, index);
                            expansions.put(REPLY_TO, name);
                        }
                    }
                    break;
                case MessageTypes.MSG_TYPE_SPELL:
                case MessageTypes.MSG_TYPE_ITEM:
                case MessageTypes.MSG_TYPE_MISC:
                case MessageTypes.MSG_TYPE_VICTIM:
                default:
                    break;
            }
        }

        @Override
        public void setDebugMode(final boolean printMessageTypes) {
        }
    };

    /**
     * Creates a new instance.
     * @param crossfireServerConnection the crossfire server connection to
     * track
     */
    public Macros(@NotNull final CrossfireServerConnection crossfireServerConnection) {
        expansions.put(REPLY_TO, "");
        crossfireServerConnection.addCrossfireDrawextinfoListener(crossfireDrawextinfoListener);
    }

    /**
     * Expands all macro references.
     * @param string the string to expand
     * @return the expanded string
     */
    @NotNull
    public String expandMacros(@NotNull final String string) {
        StringBuilder result = null;
        int index = 0;
        final Matcher macroMatcher = macroPattern.matcher(string);
        while (macroMatcher.find()) {
            if (result == null) {
                result = new StringBuilder();
            }
            final String name = macroMatcher.group(1);
            String expansion = expansions.get(name);
            if (expansion == null) {
                expansion = macroMatcher.group();
            }
            result.append(string.substring(index, macroMatcher.start()));
            result.append(expansion);
            index = macroMatcher.end();
        }
        if (result != null) {
            result.append(string.substring(index, string.length()));
        }
        return result == null ? string : result.toString();
    }
}
