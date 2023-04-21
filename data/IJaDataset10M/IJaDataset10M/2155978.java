package org.gjt.universe.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import javax.swing.event.*;
import org.gjt.universe.GalaxySpiral;
import org.gjt.universe.GalaxyGlobular;
import org.gjt.universe.GalaxyElliptical;
import org.gjt.universe.GalaxyStd;
import org.gjt.universe.GameScale;
import org.gjt.universe.GameOptions;
import org.gjt.universe.GameEngine;
import org.gjt.universe.GalaxyType;
import org.gjt.universe.Log;

/** This class implements a panel which allows selection
  * of the number, type, and size range of galaxies which
  * should be used in the game.
  * 
  * @see GUIGameOptionPanels
  *
  * @author Allan Noordvyk (noordvyk@home.com)
  *
  * @version $Id: GUIGalaxyOptionsPanel.java,v 1.4 2001/05/08 05:52:00 sstarkey Exp $
  */
public class GUIGalaxyOptionsPanel extends JPanel implements ListSelectionListener {

    private JList myListView;

    private JTextArea myBackStoryArea;

    private JPanel mySwapPanel;

    private JCheckBox myGlobularGalaxyCheck, myEllipticalGalaxyCheck, mySpiralGalaxyCheck, myGlieseGalaxyCheck;

    private JTextField myMinGalaxyCountField, myMaxGalaxyCountField, myMinGalaxyDiameterField, myMaxGalaxyDiameterField, myMinDensityField, myMaxDensityField;

    private boolean myContentsCreated = false;

    private GameScale myGameScale;

    /**
         * This method populates the panel with its components.
         */
    public void createContents() {
        JScrollPane scrollPane;
        Container contentPane = this;
        if (myContentsCreated) {
            return;
        }
        myContentsCreated = true;
        GridBagLayout gridBag = new GridBagLayout();
        contentPane.setLayout(gridBag);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(1, 1, 1, 1);
        JLabel aLabel = new JLabel("Please select the desired scale of the game.");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 18;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gridBag.setConstraints(aLabel, gbc);
        contentPane.add(aLabel);
        GameOptions gameOptions = GameEngine.getGameOptions();
        myListView = new JList(GameScale.ALL_SCALES);
        myListView.setSelectedValue(gameOptions.getGameScale(), true);
        myListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myListView.addListSelectionListener(this);
        scrollPane = new JScrollPane(myListView);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 8;
        gbc.gridheight = 8;
        gbc.weightx = 0.2;
        gbc.weighty = 1.0;
        gridBag.setConstraints(scrollPane, gbc);
        contentPane.add(scrollPane);
        myBackStoryArea = new JTextArea("");
        myBackStoryArea.setEditable(false);
        myBackStoryArea.setLineWrap(true);
        myBackStoryArea.setWrapStyleWord(true);
        myBackStoryArea.setBackground(contentPane.getBackground());
        Font origFont = myBackStoryArea.getFont();
        Font newFont = origFont.deriveFont((float) Math.ceil(origFont.getSize() * 1.15));
        myBackStoryArea.setFont(newFont);
        scrollPane = new JScrollPane(myBackStoryArea);
        gbc.gridx = 8;
        gbc.gridy = 1;
        gbc.gridwidth = 10;
        gbc.gridheight = 4;
        gbc.weightx = 0.8;
        gbc.weighty = 0.5;
        gridBag.setConstraints(scrollPane, gbc);
        contentPane.add(scrollPane);
        mySwapPanel = new JPanel();
        gbc.gridx = 8;
        gbc.gridy = 5;
        gbc.gridwidth = 10;
        gbc.gridheight = 4;
        gbc.weightx = 0.8;
        gbc.weighty = 0.5;
        gridBag.setConstraints(mySwapPanel, gbc);
        contentPane.add(mySwapPanel);
        myGlobularGalaxyCheck = new JCheckBox(GalaxyType.GLOBULAR.getDescription(), true);
        myEllipticalGalaxyCheck = new JCheckBox(GalaxyType.ELLIPTICAL.getDescription(), true);
        mySpiralGalaxyCheck = new JCheckBox(GalaxyType.SPIRAL.getDescription(), true);
        myGlieseGalaxyCheck = new JCheckBox(GalaxyType.GLIESE.getDescription(), false);
        JPanel galaxyTypePanel = new JPanel();
        GridLayout aGrid = new GridLayout(5, 1);
        galaxyTypePanel.setLayout(aGrid);
        galaxyTypePanel.add(new JLabel("Possible Galaxy Types:"));
        galaxyTypePanel.add(myGlobularGalaxyCheck);
        galaxyTypePanel.add(myEllipticalGalaxyCheck);
        galaxyTypePanel.add(mySpiralGalaxyCheck);
        galaxyTypePanel.add(myGlieseGalaxyCheck);
        mySwapPanel.add(galaxyTypePanel);
        JPanel numericFieldPanel = new JPanel();
        numericFieldPanel.setLayout(aGrid);
        mySwapPanel.add(numericFieldPanel);
        gridBag = new GridBagLayout();
        numericFieldPanel.setLayout(gridBag);
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.weightx = 0.2;
        gbc.weighty = 1.0;
        myMinGalaxyCountField = new JTextField("2", 3);
        myMaxGalaxyCountField = new JTextField("5", 3);
        myMinGalaxyDiameterField = new JTextField("10", 3);
        myMaxGalaxyDiameterField = new JTextField("60", 3);
        myMinDensityField = new JTextField("0.1", 3);
        myMaxDensityField = new JTextField("1.0", 3);
        for (int i = 0; i < 3; i++) {
            String s1, s2;
            JTextField minField, maxField;
            switch(i) {
                case 0:
                    s1 = "# of Galaxies:";
                    s2 = "";
                    minField = myMinGalaxyCountField;
                    maxField = myMaxGalaxyCountField;
                    break;
                case 1:
                    s1 = "Galaxy Diameters:";
                    s2 = "parsecs";
                    minField = myMinGalaxyDiameterField;
                    maxField = myMaxGalaxyDiameterField;
                    break;
                case 2:
                default:
                    s1 = "Galaxy Densities:";
                    s2 = "systems/parsec";
                    minField = myMinDensityField;
                    maxField = myMaxDensityField;
                    break;
            }
            aLabel = new JLabel(s1, JLabel.RIGHT);
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.gridwidth = 3;
            gbc.gridheight = 1;
            gridBag.setConstraints(aLabel, gbc);
            numericFieldPanel.add(aLabel);
            gbc.gridx = 3;
            gbc.gridy = i;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gridBag.setConstraints(minField, gbc);
            numericFieldPanel.add(minField);
            aLabel = new JLabel("...");
            gbc.gridx = 4;
            gbc.gridy = i;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gridBag.setConstraints(aLabel, gbc);
            numericFieldPanel.add(aLabel);
            gbc.gridx = 5;
            gbc.gridy = i;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gridBag.setConstraints(maxField, gbc);
            numericFieldPanel.add(maxField);
            if (s2 != "") {
                aLabel = new JLabel(s2);
                gbc.gridx = 6;
                gbc.gridy = i;
                gbc.gridwidth = 2;
                gbc.gridheight = 1;
                gridBag.setConstraints(aLabel, gbc);
                numericFieldPanel.add(aLabel);
            }
            minField.setHorizontalAlignment(JTextField.RIGHT);
            maxField.setHorizontalAlignment(JTextField.RIGHT);
        }
        this.loadOptions();
    }

    public void loadOptions() {
        GameOptions gameOptions = GameEngine.getGameOptions();
        myGameScale = gameOptions.getGameScale();
        if (!myContentsCreated) {
            return;
        }
        this.updateContents();
    }

    public void saveOptions() {
        Vector galaxyTypeVector;
        if (!myContentsCreated) {
            return;
        }
        GameOptions gameOptions = GameEngine.getGameOptions();
        gameOptions.setGameScale(myGameScale);
        Vector gTypeVector = new Vector();
        if (myGlobularGalaxyCheck.isSelected()) gTypeVector.addElement(GalaxyType.GLOBULAR);
        if (myEllipticalGalaxyCheck.isSelected()) gTypeVector.addElement(GalaxyType.ELLIPTICAL);
        if (mySpiralGalaxyCheck.isSelected()) gTypeVector.addElement(GalaxyType.SPIRAL);
        if (myGlieseGalaxyCheck.isSelected()) gTypeVector.addElement(GalaxyType.GLIESE);
        if (gTypeVector.size() > 0) {
            int n = gTypeVector.size();
            GalaxyType[] gTypeArray = new GalaxyType[n];
            for (int i = 0; i < n; i++) {
                gTypeArray[i] = (GalaxyType) gTypeVector.elementAt(i);
            }
            GameScale.CUSTOM.setGalaxyTypes(gTypeArray);
        }
        GameScale.CUSTOM.setGalaxyCountRange(new Integer(myMinGalaxyCountField.getText()).intValue(), new Integer(myMaxGalaxyCountField.getText()).intValue());
        GameScale.CUSTOM.setDiameterRange(new Float(myMinGalaxyDiameterField.getText()).floatValue(), new Float(myMaxGalaxyDiameterField.getText()).floatValue());
        GameScale.CUSTOM.setDensityRange(new Float(myMinDensityField.getText()).floatValue(), new Float(myMaxDensityField.getText()).floatValue());
    }

    public void updateContents() {
        String backStory;
        if (myGameScale == null) {
            Log.debug("No scale curently selected.");
            backStory = "";
        } else {
            backStory = myGameScale.getBackStory();
            if (backStory == null) {
                backStory = "*No Back Story!*";
            }
        }
        if (myGameScale == GameScale.CUSTOM) {
            myMinGalaxyCountField.setText(Integer.toString(myGameScale.getMinGalaxyCount()));
            myMaxGalaxyCountField.setText(Integer.toString(myGameScale.getMaxGalaxyCount()));
            myMinGalaxyDiameterField.setText(Float.toString(myGameScale.getMinDiameter()));
            myMaxGalaxyDiameterField.setText(Float.toString(myGameScale.getMaxDiameter()));
            myMinDensityField.setText(Float.toString(myGameScale.getMinDensity()));
            myMaxDensityField.setText(Float.toString(myGameScale.getMaxDensity()));
            myGlobularGalaxyCheck.setSelected(false);
            myEllipticalGalaxyCheck.setSelected(false);
            mySpiralGalaxyCheck.setSelected(false);
            myGlieseGalaxyCheck.setSelected(false);
            GalaxyType[] gTypeArray = myGameScale.getGalaxyTypes();
            for (int i = 0; i < gTypeArray.length; i++) {
                if (gTypeArray[i] == GalaxyType.GLOBULAR) {
                    myGlobularGalaxyCheck.setSelected(true);
                    continue;
                }
                if (gTypeArray[i] == GalaxyType.ELLIPTICAL) {
                    myEllipticalGalaxyCheck.setSelected(true);
                    continue;
                }
                if (gTypeArray[i] == GalaxyType.SPIRAL) {
                    mySpiralGalaxyCheck.setSelected(true);
                    continue;
                }
                if (gTypeArray[i] == GalaxyType.GLIESE) {
                    myGlieseGalaxyCheck.setSelected(true);
                    continue;
                }
            }
            mySwapPanel.setVisible(true);
        } else {
            mySwapPanel.setVisible(false);
        }
        myBackStoryArea.setText(backStory);
    }

    public void valueChanged(ListSelectionEvent evt) {
        myGameScale = (GameScale) myListView.getSelectedValue();
        this.updateContents();
    }
}
