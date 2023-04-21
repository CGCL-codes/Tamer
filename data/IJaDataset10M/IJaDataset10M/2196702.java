package com.oat.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.oat.Version;

/**
 * Type: OATPanel<br/>
 * Date: 21/12/2006<br/>
 * <br/>
 * Description: Information about OAT
 * <br/>
 * @author Jason Brownlee
 * 
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 */
public class OATPanel extends JPanel {

    public static final String ABOUT_TEXT = Version.NAME_LONG + " (" + Version.NAME_SHORT + ") v" + Version.VERSION + "\n" + Version.WEB + "\n" + "Copyright (C) 2006, 2008  Jason Brownlee\n" + "\n" + "OAT is free software; you can redistribute it and/or modify it under the terms\n" + "of the GNU Lesser General Public License as published by the Free Software \n" + "Foundation; either version 3 of the License, or (at your option) any \n" + "later version.\n" + "\n" + "OAT is distributed in the hope that it will be useful, but WITHOUT ANY \n" + "WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS \n" + "FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for\n" + "more details.\n" + "\n" + "You should have received a copy of the GNU Lesser General Public License \n" + "along with this program.  If not, see <http://www.gnu.org/licenses/>.\n" + "\n" + "Jason Brownlee\n" + "Project Lead";

    public OATPanel() {
        prepareGUI();
    }

    protected void prepareGUI() {
        setName(Version.NAME_SHORT);
        setLayout(new BorderLayout());
        JTextArea jta = new JTextArea();
        jta.setEditable(false);
        jta.setLineWrap(true);
        jta.setWrapStyleWord(true);
        jta.setText(ABOUT_TEXT);
        add(new JScrollPane(jta));
    }
}
