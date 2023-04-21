package org.colombbus.tangara.ide.controller.impl;

import java.io.Writer;
import javax.swing.SwingUtilities;
import org.apache.commons.lang.Validate;
import org.colombbus.tangara.ide.controller.DisplayConsole;
import org.colombbus.tangara.ide.controller.MessageNature;

class DisplayConsoleWriter extends Writer {

    private DisplayConsole displayConsole;

    private MessageNature messageNature = MessageNature.MESSAGE;

    public void setDisplayConsole(DisplayConsole displayConsole) {
        Validate.notNull(displayConsole, "displayConsole argument is null");
        this.displayConsole = displayConsole;
    }

    public void setMessageNature(MessageNature messageNature) {
        Validate.notNull(messageNature, "messageNature argument is null");
        this.messageNature = messageNature;
    }

    @Override
    public void write(char[] cbuf, int off, int len) {
        Validate.notNull(displayConsole, "displayConsole argument is null");
        Validate.notNull(messageNature, "messageNature argument is null");
        final String message = new String(cbuf, off, len);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                displayConsole.write(message, messageNature);
            }
        });
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }
}
