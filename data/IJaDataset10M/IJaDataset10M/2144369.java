package net.sf.babble;

import java.util.ArrayList;
import java.util.List;

/**
 * This class models the extra info parameters associated with a
 * <code>ChannelMode</code>.
 * @see ChannelMode
 * @version $Id: ChannelModeInfo.java 222 2004-07-08 08:12:51Z speakmon $
 * @author Ben Speakmon
 */
public final class ChannelModeInfo {

    /**
     * Holds value of property action.
     */
    private ModeAction action;

    /**
     * Holds value of property mode.
     */
    private ChannelMode mode;

    /**
     * Holds value of property parameter.
     */
    private String parameter;

    /**
     * Creates a new instance of ChannelModeInfo.
     */
    public ChannelModeInfo() {
    }

    private ChannelModeInfo(ChannelModeInfo info) {
        this.action = info.action;
        this.mode = info.mode;
        this.parameter = info.parameter;
    }

    /**
     * Returns the action for this instance.
     * @return the action
     */
    public ModeAction getAction() {
        return this.action;
    }

    /**
     * Sets the action for this instance.
     * @param action the action
     */
    public void setAction(ModeAction action) {
        this.action = action;
    }

    /**
     * Returns the <code>ChannelMode</code> for this instance.
     * @return the <code>ChannelMode</code> for this instance
     */
    public ChannelMode getMode() {
        return this.mode;
    }

    /**
     * Sets the <code>ChannelMode</code> for this instance.
     * @param mode the <code>ChannelMode</code> for this instance
     */
    public void setMode(ChannelMode mode) {
        this.mode = mode;
    }

    /**
     * Returns the parameter string for this instance.
     * @return the parameter string for this instance
     */
    public String getParameter() {
        return this.parameter;
    }

    /**
     * Sets the parameter string for this instance.
     * @param parameter new parameter string for this instance
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * Returns a string representation of this object.
     *
     * The format of the returned string is not guaranteed, but the following form
     * may be regarded as typical:
     *
     * Action=&lt;action&gt; ChannelMode=&lt;ChannelMode&gt; Parameter=&lt;parameter&gt;
     * @return a <code>String</code> representation of this object
     */
    public String toString() {
        StringBuilder buf = new StringBuilder(64);
        buf.append("Action=");
        buf.append(action);
        buf.append(" Mode=");
        buf.append(mode);
        buf.append(" Parameter=");
        buf.append(parameter);
        return buf.toString();
    }

    /**
     * Parses any <code>ChannelModeInfo</code> objects from the specified
     * tokens and returns them in an array.
     * @param tokens the tokens to search for modes
     * @param start the index in which to start looking for modes in the specified
     * tokens
     * @return any found <code>ChannelModeInfo</code> objects in an array
     */
    protected static ChannelModeInfo[] parseModes(String[] tokens, int start) {
        List<ChannelModeInfo> modeInfoList = new ArrayList<ChannelModeInfo>();
        for (int i = start; i < tokens.length; ) {
            ChannelModeInfo modeInfo = new ChannelModeInfo();
            int parmIndex = i + 1;
            for (int j = 0; j < tokens[i].length(); j++) {
                while (j < tokens[i].length() && tokens[i].charAt(j) == '+') {
                    modeInfo.action = ModeAction.ADD;
                    j++;
                }
                while (j < tokens[i].length() && tokens[i].charAt(j) == '-') {
                    modeInfo.action = ModeAction.REMOVE;
                    j++;
                }
                if (j == 0) {
                    throw new RuntimeException();
                } else if (j < tokens[i].length()) {
                    switch(tokens[i].charAt(j)) {
                        case 'o':
                        case 'v':
                        case 'b':
                        case 'e':
                        case 'I':
                        case 'k':
                        case 'O':
                            modeInfo.mode = ChannelMode.charToChannelMode(tokens[i].charAt(j));
                            modeInfo.parameter = tokens[parmIndex++];
                            break;
                        case 'l':
                            modeInfo.mode = ChannelMode.charToChannelMode(tokens[i].charAt(j));
                            if (modeInfo.action == ModeAction.ADD) {
                                modeInfo.parameter = tokens[parmIndex++];
                            } else {
                                modeInfo.parameter = "";
                            }
                            break;
                        default:
                            modeInfo.mode = ChannelMode.charToChannelMode(tokens[i].charAt(j));
                            modeInfo.parameter = "";
                            break;
                    }
                }
                modeInfoList.add(new ChannelModeInfo(modeInfo));
            }
            i = parmIndex;
        }
        return modeInfoList.toArray(new ChannelModeInfo[0]);
    }
}
