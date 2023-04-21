package com.united_chat.sex_game.gui;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.Map;
import java.util.Iterator;
import com.united_chat.sex_game.commons.Utils;
import com.united_chat.sex_game.objects.Player;

/**
 * This abstract class provides the player editing screens.
 * <br />
 * Created on: 26th of May, 2003<br />
 * Last Modified: $Date: 2003/05/28 12:05:27 $
 * @author <a href="mailto:andrew@united-chat.com">Andrew Hughes</a>
 * @author <a href="mailto:brett@united-chat.com">Brett Royles</a>
 * @version $Revision: 1.4 $<br />
 * <br />
 * Version History:
 * <code>
 * <br />$Log: PlayerData.java,v $
 * <br />Revision 1.4  2003/05/28 12:05:27  andy2002
 * <br />Added Player and Task objects and associated test cases.
 * <br />
 * <br />Revision 1.3  2003/05/27 14:38:03  andy2002
 * <br />Added level dialogs and Preferences choice dialog.
 * <br />
 * <br />Revision 1.2  2003/05/26 22:52:51  andy2002
 * <br />Added Edit Players and Edit Tasks
 * <br />
 * <br />Revision 1.1  2003/05/26 16:43:37  andy2002
 * <br />Implemented and abstracted Add Player dialog.
 * <br />
 * </code>
 * <br />
 * <strong>Copyright (c) 2003 Andrew Hughes, Brett Royles</strong>
 * <br />
 * <p>This file is part of Sex Game.</p>
 * <p>
 * Sex Game is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * </p>
 * <p>
 * Sex Game is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Sex Game; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * </p>
 */
public abstract class PlayerData extends JDialog {

    private JButton ok, cancel;

    private JLabel nameLabel, sexLabel, orientationLabel, levelLabel;

    private JTextField name;

    private ButtonGroup sex, orientation;

    private JSpinner level;

    private ActionListener okListener, cancelListener;

    public PlayerData(JDialog parent, String title) {
        super(parent, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());
        setupEventHandlers();
        setupLabel(nameLabel, "Name:", 0, 0);
        setupName();
        setupLabel(sexLabel, "Sex:", 0, 1);
        setupSex();
        setupLabel(orientationLabel, "Orientation:", 0, 2);
        setupOrientation();
        setupLabel(levelLabel, "Initial Clothing Level:", 0, 3);
        setupLevel();
        setupButton(ok, "OK", okListener, 0, 4);
        setupButton(cancel, "Cancel", cancelListener, 2, 4);
        pack();
        show();
    }

    private void setupButton(JButton button, String label, ActionListener listener, int x, int y) {
        button = new JButton(label);
        button.addActionListener(listener);
        Utils.addComponent(button, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.CENTER, 100, 100, getContentPane());
    }

    private void setupEventHandlers() {
        okListener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        };
        cancelListener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        };
    }

    private void setupLabel(JLabel label, String text, int x, int y) {
        label = new JLabel(text);
        Utils.addComponent(label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST, 100, 100, getContentPane());
    }

    private void setupName() {
        name = new JTextField();
        Utils.addComponent(name, 1, 0, 3, 1, GridBagConstraints.HORIZONTAL, 100, 100, getContentPane());
    }

    private void setupSex() {
        sex = new ButtonGroup();
        generateRadioButtons(Player.SEX_MAP, sex, 1);
    }

    private void setupOrientation() {
        orientation = new ButtonGroup();
        generateRadioButtons(Player.ORIENTATION_MAP, orientation, 2);
    }

    private void setupRadioButton(JRadioButton button, String label, int x, int y, ButtonGroup group) {
        button = new JRadioButton(label, false);
        group.add(button);
        Utils.addComponent(button, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.CENTER, 100, 100, getContentPane());
    }

    private void setupLevel() {
        level = new JSpinner();
        Utils.addComponent(level, 1, 3, 1, 1, GridBagConstraints.NONE, GridBagConstraints.CENTER, 100, 100, getContentPane());
    }

    private void generateRadioButtons(Map optionMap, ButtonGroup group, int y) {
        int counter;
        Iterator iterator;
        counter = 1;
        iterator = optionMap.keySet().iterator();
        while (iterator.hasNext()) {
            setupRadioButton(new JRadioButton(), (String) iterator.next(), counter, y, group);
            ++counter;
        }
    }
}
