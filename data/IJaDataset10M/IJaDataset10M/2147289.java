package com.prefabware.jmodel.javadoc;

import java.util.List;
import com.prefabware.commons.StringUtil;
import com.prefabware.jmodel.code.JCodable;
import com.prefabware.jmodel.code.JCodeOptions;

public class JMultiLineComment implements JCodable {

    public static final String LF = "\n";

    public static final String BEGIN_BLOCK = "/**" + LF;

    public static final String END_BLOCK = " */" + LF;

    public static final String BEGIN_LINE = " * ";

    protected StringBuffer text;

    public JMultiLineComment() {
        super();
        this.text = new StringBuffer();
    }

    public boolean isEmpty() {
        return this.text.length() == 0;
    }

    protected StringBuffer getText() {
        return text;
    }

    /**
	 * breaks the given String into lines of the given length respecting word
	 * boundaries than appends it to {@link #text} prepends a linefeed
	 * 
	 * @param append
	 * @return
	 */
    public StringBuffer appendText(String append) {
        if (append == null || append.isEmpty()) {
            return this.text;
        }
        List<String> lines = StringUtil.splitAtLineFeed(append);
        for (String line : lines) {
            this.text = this.text.append(BEGIN_LINE).append(line).append(LF);
        }
        return this.text;
    }

    @Override
    public String asCode(JCodeOptions options) {
        if (this.isEmpty()) {
            return "";
        }
        StringBuffer doc = new StringBuffer();
        doc.append(BEGIN_BLOCK);
        doc.append(this.text);
        doc.append(END_BLOCK);
        return doc.toString();
    }
}
