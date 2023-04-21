package net.sf.traser.client.scanner;

import java.util.logging.Logger;
import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import net.sf.traser.common.ConfigManager;
import org.apache.axiom.om.OMElement;

/**
 *
 * @author Marcell Szathmari
 */
public class SimpleEventQueueReader extends AbstractIdentificator {

    /**
     * This field is used to LOG messages.
     */
    private static final Logger LOG = Logger.getLogger("net.sf.traser.reader.impl.SimpleEventQueueReader");

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(OMElement config, ConfigManager manager) {
        super.init(config, manager);
        int lChar = '\t';
        int tChar = '\t';
        try {
            String leadingChar = config.getFirstChildWithName(new QName("leadingCharacter")).getText();
            lChar = leadingChar.charAt(0);
            LOG.finer("Using leading delimiter: '" + lChar + "'");
        } catch (Exception ex) {
            LOG.log(Level.INFO, "Leading delimiter character not specified, using default value: '" + lChar + "'.");
        }
        try {
            String trailingChar = config.getFirstChildWithName(new QName("trailingCharacter")).getText();
            tChar = trailingChar.charAt(0);
            LOG.finer("Using leading delimiter: '" + tChar + "'");
        } catch (Exception ex) {
            LOG.log(Level.INFO, "Trailing delimiter character not specified, using default value: '" + tChar + "'.");
        }
        eq = new SimpleEventQueueReaderEQ(lChar, tChar);
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(eq);
    }

    /**
     * The instance of the EventQueue reader implementation.
     */
    private SimpleEventQueueReaderEQ eq;

    /**
     *
     */
    private class SimpleEventQueueReaderEQ extends EventQueue {

        /**
         * Flag telling whether we are actually reading a tag.
         */
        private boolean inTag = false;

        /**
         * Starting delimiter character.
         */
        private final int lDelimiter;

        /**
         * Ending delimiter character.
         */
        private final int tDelimiter;

        /**
         * Next delimiter we are seeking.
         */
        private int aDelimiter;

        /**
         * Data so far read from the tag.
         */
        private String tagData;

        /** 
         * Creates a new instance of EventQueueReader.
         * @param lDelim the L delimiter.
         * @param tDelim the T delimiter
         */
        public SimpleEventQueueReaderEQ(int lDelim, int tDelim) {
            lDelimiter = lDelim;
            tDelimiter = tDelim;
            aDelimiter = this.lDelimiter;
            tagData = "";
        }

        /**
         * Overrides the dispatchEvent method of EventQueue. This method checks
         * events and if the starting delimiter character is typed in it starts
         * recording tag data till it detects the typing of the ending delimiter
         * character. If the event is not a key event or it not within the
         * boundaries of a tag, this method lets the original queue handle it.
         * @param aWTEvent the next event to check.
         */
        @Override
        @SuppressWarnings(value = "unchecked")
        protected void dispatchEvent(AWTEvent aWTEvent) {
            if (aWTEvent instanceof KeyEvent) {
                KeyEvent evt = (KeyEvent) aWTEvent;
                if (evt.paramString().startsWith("KEY_PRESSED")) {
                    LOG.finer("Key pressed, delimiter keycode comparison: " + evt.getKeyCode() + " ?? " + this.aDelimiter);
                    if (evt.getKeyCode() == this.aDelimiter) {
                        LOG.finer("\t-- Read delimiter");
                        if (this.inTag) {
                            LOG.finer("\t-- Ending tag, preparing read data for callback");
                            notifyListeners(tagData);
                            this.inTag = false;
                            this.aDelimiter = this.lDelimiter;
                        } else {
                            LOG.finer("\t-- Starting tag");
                            this.inTag = true;
                            this.aDelimiter = this.tDelimiter;
                            tagData = "";
                        }
                    } else {
                        LOG.finer("\t-- Read other character");
                        if (this.inTag) {
                            LOG.finer("\t-- Tag data character: '" + evt.getKeyCode() + " (" + evt.getKeyChar() + ")\'");
                            tagData += evt.getKeyChar();
                        } else {
                            LOG.finer("\t-- Invoking super dispatcher");
                            super.dispatchEvent(aWTEvent);
                        }
                    }
                }
            } else {
                LOG.finer("Not KeyEvent, invoking super dispatcher");
                super.dispatchEvent(aWTEvent);
            }
        }
    }
}
