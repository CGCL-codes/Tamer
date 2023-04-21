package com.notuvy.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * A UI component that is a status bar to be displayed for a frame.  It can
 * display a single line of text.
 * It automatically integrates into the log4j framework.  To activate the appender,
 * configure it like:
 *
 *    <appender name="StatusBar" class="com.notuvy.gui.StatusBarAppender">
 *      <param name="threshold" value="info"/>
 *      <layout class="org.apache.log4j.PatternLayout">
 *        <param name="ConversionPattern" value="%m%n"/>
 *      </layout>
 *    </appender>
 *
 *  <root>
 *    ...
 *    <appender-ref ref="StatusBar" />
 *  </root>
 *
 * User: murali
 * Date: Nov 9, 2010
 * Time: 3:29:43 PM
 */
public class StatusBar {

    private final JPanel fPanel = new JPanel();

    private final JTextField fTextField = new JTextField("");

    public StatusBar() {
        this(BorderFactory.createRaisedBevelBorder());
    }

    public StatusBar(Border pBorder) {
        getTextField().setEditable(false);
        getPanel().setBorder(pBorder);
        getPanel().setLayout(new BorderLayout());
        getPanel().add(getTextField(), BorderLayout.CENTER);
        StatusBarAppender.STATUSES.add(this);
    }

    public JPanel getPanel() {
        return fPanel;
    }

    private JTextField getTextField() {
        return fTextField;
    }

    public void setText(String pString) {
        getTextField().setText(pString);
    }

    public void clear() {
        setText("");
    }

    public ActionListener wrapClear(final ActionListener pListener) {
        return new ActionListener() {

            public void actionPerformed(ActionEvent pActionEvent) {
                clear();
                pListener.actionPerformed(pActionEvent);
            }
        };
    }
}
