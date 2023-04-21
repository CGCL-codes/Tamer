package eu.keep.uphec.help;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;
import eu.keep.kernel.CoreEngineModel;

/**
* The About class defines the content and look of the About dialog 
* 
* @author Antonio Ciuffreda 
*/
public class About {

    private static final Logger logger = Logger.getLogger(eu.keep.uphec.help.About.class.getName());

    private JDialog dialog;

    private Container contentPane;

    private JPanel panel_1;

    private JPanel panel_2;

    private String title;

    private String version;

    private String vendor;

    private JLabel message;

    private JButton close_button;

    public About(JFrame mainWindowFrame, CoreEngineModel model) {
        dialog = new JDialog(mainWindowFrame, "About");
        dialog.setBounds((mainWindowFrame.getBounds().x + 200), (mainWindowFrame.getBounds().y + 75), 600, 600);
        dialog.setResizable(false);
        dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
        logger.info("Launching About dialog...");
        contentPane = dialog.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        panel_1 = new JPanel();
        panel_1.setPreferredSize(new Dimension(500, 560));
        panel_1.setBackground(new Color(211, 213, 213));
        panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.LINE_AXIS));
        title = model.getTitle();
        version = model.getVersion();
        vendor = model.getVendor();
        message = new JLabel("<HTML><BR>" + "<B>" + title + "</B><BR>" + "Copyright (c) 2009-2012 Tessella plc. - Version " + version + "<BR>" + "Licensed under the Apache License, Version 2.0 (the 'License'); you may not use this software except in <BR>" + "compliance with the License. You may obtain a copy of the License at:<BR><BR>" + "http://www.apache.org/license/LICENSE-2.0 <BR><BR>" + "Unless required by applicable law or agreed to in writing, software distributed under the License is distributed <BR>" + "on as 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License " + "for the specific language governing emissions and limitations under the License. <BR>" + "For more information about this project, visit:<BR><BR>" + vendor + "<BR><BR>" + "Credits:<BR>" + "-Bram Lohman (Tessella plc., Emulation Framework)<BR>" + "-David Michel (Tessella plc., Emulation Framework)<BR>" + "-Edo Noordermeer (Tessella plc., Emulation Framework)<BR>" + "-Jeffrey van der Hoeven (National Library of the Netherlands, Coordination)<BR><BR><BR>" + "<B>Emulation Framework GUI</B><BR>" + "Copyright 2009-2012 University of Portsmouth. - Version 0.3 <BR><BR> " + "Licensed under the Apache License, Version 2.0 (the 'License');" + "you may not use this file except in compliance <BR> " + "with the License. You may obtain a copy of the License at<BR><BR>" + "http://www.apache.org/licenses/LICENSE-2.0<BR><BR>" + "Unless required by applicable law or agreed to in writing, software distributed under the License is distributed <BR>" + "on as 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License " + "for the specific language governing emissions and limitations under the License. <BR><BR>" + "Credits: Antonio Ciuffreda (University of Portsmouth)</HTML>");
        message.setFont(new Font("new", Font.PLAIN, 11));
        panel_1.add(Box.createRigidArea(new Dimension(15, 0)));
        panel_1.add(message, BorderLayout.CENTER);
        panel_1.add(Box.createRigidArea(new Dimension(15, 0)));
        panel_2 = new JPanel();
        panel_2.setPreferredSize(new Dimension(500, 40));
        panel_2.setBackground(new Color(211, 213, 213));
        panel_2.setLayout(new FlowLayout(FlowLayout.RIGHT, 7, 7));
        close_button = new JButton("Close");
        close_button.setMaximumSize(new Dimension(50, 27));
        close_button.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "close");
        close_button.getActionMap().put("close", close);
        close_button.addActionListener(new CloseButtonAboutListener(dialog));
        panel_2.add(close_button);
        contentPane.add(panel_1);
        contentPane.add(panel_2);
        dialog.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "close");
        dialog.getRootPane().getActionMap().put("close", close);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    Action close = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent e) {
            dialog.dispose();
        }
    };
}
