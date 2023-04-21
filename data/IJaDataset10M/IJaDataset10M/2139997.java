package com.wpl.ui.samples.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wpl.ui.events.EventHandler;
import com.wpl.ui.factory.annotations.UiLayout;
import com.wpl.ui.factory.annotations.UiScrollable;
import com.wpl.ui.factory.annotations.UiText;
import com.wpl.ui.factory.annotations.constraints.UiBorderLayoutConstraint;
import com.wpl.ui.factory.enums.BorderLayoutConstraint;

/**
 * 
 * @since 1.0
 */
@UiLayout(BorderLayout.class)
public class ExternalPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1384552017351526875L;

    @SuppressWarnings("unused")
    private static Logger LOGGER = LoggerFactory.getLogger(ExternalPanel.class);

    @UiBorderLayoutConstraint(BorderLayoutConstraint.CENTER)
    @UiScrollable
    JTextArea content;

    @UiLayout(FlowLayout.class)
    class CommandPanel extends JPanel {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        @UiText("click me!")
        JButton button;

        @UiText("Exit")
        JButton exit;
    }

    @UiBorderLayoutConstraint(BorderLayoutConstraint.SOUTH)
    CommandPanel commandPanel;

    final EventHandler<CustomEventArgs> customEvent = new EventHandler<CustomEventArgs>();

    final EventHandler<CustomEventArgs> exitEvent = new EventHandler<CustomEventArgs>();

    void onButton_actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "click me! Clicked");
        content.append("click me! Clicked\n");
        customEvent.invoke(new CustomEventArgs(this, "Click Me"));
    }

    void onExit_actionPerformed(ActionEvent e) {
        exitEvent.invoke(new CustomEventArgs(this, "Exit"));
    }
}
