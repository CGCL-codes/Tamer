package net.etherstorm.jOpenRPG;

import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

/**
 * 
 * @author Slava Pestov
 * @author David Hudson
 * @author $Author: tedberg $
 * @version $Revision: 352 $
 * $Date: 2002-02-01 02:32:11 -0500 (Fri, 01 Feb 2002) $
 */
public class TipOfTheDay extends JDialog {

    /**
	 *
	 */
    public TipOfTheDay(JFrame jFrame) {
        super(jFrame, "Tip of the Day", false);
        setContentPane(new TipPanel());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(jFrame);
        show();
    }

    /**
	 *
	 */
    public void ok() {
        dispose();
    }

    /**
	 *
	 */
    public void cancel() {
        dispose();
    }

    /**
	 *
	 */
    class TipPanel extends JPanel {

        TipPanel() {
            super(new BorderLayout(12, 12));
            setBorder(new EmptyBorder(12, 12, 12, 12));
            JLabel label = new JLabel("Did you know...");
            label.setFont(new Font("SansSerif", Font.PLAIN, 24));
            label.setForeground(UIManager.getColor("Button.foreground"));
            TipPanel.this.add(BorderLayout.NORTH, label);
            tipText = new JEditorPane();
            tipText.setEditable(false);
            tipText.setContentType("text/html");
            nextTip();
            JScrollPane scroller = new JScrollPane(tipText);
            scroller.setPreferredSize(new Dimension(150, 150));
            TipPanel.this.add(BorderLayout.CENTER, scroller);
            ActionHandler actionHandler = new ActionHandler();
            Box buttons = new Box(BoxLayout.X_AXIS);
            showNextTime = new JCheckBox("Show tips on startup", TipShowPref.equalsIgnoreCase(referenceManager.getPreference("tip.show", "true")));
            showNextTime.addActionListener(actionHandler);
            buttons.add(showNextTime);
            buttons.add(Box.createHorizontalStrut(6));
            buttons.add(Box.createGlue());
            nextTip = new JButton("Next Tip");
            nextTip.addActionListener(actionHandler);
            buttons.add(nextTip);
            buttons.add(Box.createHorizontalStrut(6));
            close = new JButton("Close");
            close.addActionListener(actionHandler);
            buttons.add(close);
            TipOfTheDay.this.getRootPane().setDefaultButton(close);
            Dimension dim = nextTip.getPreferredSize();
            dim.width = Math.max(dim.width, close.getPreferredSize().width);
            nextTip.setPreferredSize(dim);
            close.setPreferredSize(dim);
            TipPanel.this.add(BorderLayout.SOUTH, buttons);
        }

        /**
		 *
		 */
        private JCheckBox showNextTime;

        /**
		 *
		 */
        private JButton nextTip, close;

        /**
		 *
		 */
        private JEditorPane tipText;

        /**
		 *
		 */
        private int currentTip = -1;

        /**
		 *
		 */
        private String TipShowPref = new String("true");

        /**
		 *
		 */
        private ReferenceManager referenceManager = ReferenceManager.getInstance();

        /**
		 *
		 */
        private void nextTip() {
            int count = 4;
            int tipToShow = currentTip;
            while (tipToShow == currentTip) tipToShow = Math.abs(new Random().nextInt()) % count;
            try {
                tipText.setPage(TipOfTheDay.class.getResource("/tips/tip" + tipToShow + ".html"));
            } catch (Exception e) {
            }
        }

        /**
		 *
		 */
        class ActionHandler implements ActionListener {

            /**
			 *
			 */
            public void actionPerformed(ActionEvent evt) {
                Object source = evt.getSource();
                if (source == showNextTime) {
                    if (TipShowPref.equalsIgnoreCase(referenceManager.getPreference("tip.show", "true"))) {
                        referenceManager.setPreference("tip.show", "false");
                    } else {
                        referenceManager.setPreference("tip.show", "true");
                    }
                } else if (source == nextTip) nextTip(); else if (source == close) dispose();
            }
        }
    }
}
