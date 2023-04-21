package org.cubicunit.internal.ie;

import org.cubicunit.TextArea;
import com.tapsterrock.jiffie.IHTMLTextAreaElement;

public class IeTextArea extends IeTextInput implements TextArea {

    public IeTextArea(IHTMLTextAreaElement element) {
        super(element);
    }
}
