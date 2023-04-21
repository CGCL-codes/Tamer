package org.antlr.runtime3_3_0;

import java.io.Serializable;

public class CommonToken implements Token, Serializable {

    protected int type;

    protected int line;

    protected int charPositionInLine = -1;

    protected int channel = DEFAULT_CHANNEL;

    protected transient CharStream input;

    /** We need to be able to change the text once in a while.  If
	 *  this is non-null, then getText should return this.  Note that
	 *  start/stop are not affected by changing this.
	  */
    protected String text;

    /** What token number is this from 0..n-1 tokens; < 0 implies invalid index */
    protected int index = -1;

    /** The char position into the input buffer where this token starts */
    protected int start;

    /** The char position into the input buffer where this token stops */
    protected int stop;

    public CommonToken(int type) {
        this.type = type;
    }

    public CommonToken(CharStream input, int type, int channel, int start, int stop) {
        this.input = input;
        this.type = type;
        this.channel = channel;
        this.start = start;
        this.stop = stop;
    }

    public CommonToken(int type, String text) {
        this.type = type;
        this.channel = DEFAULT_CHANNEL;
        this.text = text;
    }

    public CommonToken(Token oldToken) {
        text = oldToken.getText();
        type = oldToken.getType();
        line = oldToken.getLine();
        index = oldToken.getTokenIndex();
        charPositionInLine = oldToken.getCharPositionInLine();
        channel = oldToken.getChannel();
        input = oldToken.getInputStream();
        if (oldToken instanceof CommonToken) {
            start = ((CommonToken) oldToken).start;
            stop = ((CommonToken) oldToken).stop;
        }
    }

    public int getType() {
        return type;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getText() {
        if (text != null) {
            return text;
        }
        if (input == null) {
            return null;
        }
        if (start < input.size() && stop < input.size()) {
            text = input.substring(start, stop);
        } else {
            text = "<EOF>";
        }
        return text;
    }

    /** Override the text for this token.  getText() will return this text
	 *  rather than pulling from the buffer.  Note that this does not mean
	 *  that start/stop indexes are not valid.  It means that that input
	 *  was converted to a new string in the token object.
	 */
    public void setText(String text) {
        this.text = text;
    }

    public int getLine() {
        return line;
    }

    public int getCharPositionInLine() {
        return charPositionInLine;
    }

    public void setCharPositionInLine(int charPositionInLine) {
        this.charPositionInLine = charPositionInLine;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStartIndex() {
        return start;
    }

    public void setStartIndex(int start) {
        this.start = start;
    }

    public int getStopIndex() {
        return stop;
    }

    public void setStopIndex(int stop) {
        this.stop = stop;
    }

    public int getTokenIndex() {
        return index;
    }

    public void setTokenIndex(int index) {
        this.index = index;
    }

    public CharStream getInputStream() {
        return input;
    }

    public void setInputStream(CharStream input) {
        this.input = input;
    }

    public String toString() {
        String channelStr = "";
        if (channel > 0) {
            channelStr = ",channel=" + channel;
        }
        String txt = getText();
        if (txt != null) {
            txt = txt.replaceAll("\n", "\\\\n");
            txt = txt.replaceAll("\r", "\\\\r");
            txt = txt.replaceAll("\t", "\\\\t");
        } else {
            txt = "<no text>";
        }
        return "[@" + getTokenIndex() + "," + start + ":" + stop + "='" + txt + "',<" + type + ">" + channelStr + "," + line + ":" + getCharPositionInLine() + "]";
    }
}
