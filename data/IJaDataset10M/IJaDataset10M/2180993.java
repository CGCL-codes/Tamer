package org.gromurph.javascore.gui;

import javax.swing.*;
import org.gromurph.javascore.model.Division;
import org.gromurph.javascore.model.Regatta;
import org.gromurph.javascore.model.SubDivision;
import org.gromurph.util.UtilJfcTestCase;

/**
 * Covering unit tests for the PanelRace class
 */
public class PanelSubDivisionTests extends UtilJfcTestCase {

    public PanelSubDivisionTests(String name) {
        super(name);
    }

    public void testMainPanel() {
        assertEquals("Number of windows is incorrect", 0, getOpenWindowCount());
        Regatta reg = new Regatta();
        reg.addDivision(new Division("Div1"));
        reg.addDivision(new Division("Div2"));
        PanelSubDivision.setTestRegatta(reg);
        SubDivision subdiv = new SubDivision();
        showPanel(subdiv);
        JDialog panel = getTestDialog();
        JTextField field = (JTextField) findComponent(JTextField.class, "fTextName", panel);
        assertNotNull("Cant find fTextName", field);
        JComponent alternateFocusField = (JComponent) findComponent(JCheckBox.class, "fCheckMonopoly", panel);
        assertNotNull("alternateFocusField is null", alternateFocusField);
        String newtext = "A";
        sendStringAndChangeFocus(field, newtext, alternateFocusField);
        assertEquals("Subdiv name didnt react to lost focus", newtext, subdiv.getName());
        newtext = "B";
        sendStringAndEnter(field, newtext);
        assertEquals("Subdiv name didnt react to enter", newtext, subdiv.getName());
        field = (JTextField) findComponent(JTextField.class, "fTextAddon", panel);
        assertNotNull("Cant find fTextAddon", field);
        alternateFocusField = (JComponent) findComponent(JCheckBox.class, "fCheckMonopoly", panel);
        assertNotNull("alternateFocusField is null", alternateFocusField);
        newtext = "2.0";
        sendStringAndChangeFocus(field, newtext, alternateFocusField);
        assertEquals("Subdiv addon didnt react to lost focus", newtext, Double.toString(subdiv.getRaceAddon()));
        newtext = "3.0";
        sendStringAndEnter(field, newtext);
        assertEquals("Subdiv name didnt react to enter", newtext, Double.toString(subdiv.getRaceAddon()));
        JCheckBox check = (JCheckBox) findComponent(JCheckBox.class, "fCheckMonopoly", panel);
        assertNotNull(check);
        assertEquals(false, subdiv.isMonopoly());
        check.doClick();
        awtSleep();
        assertEquals(true, subdiv.isMonopoly());
        check.doClick();
        awtSleep();
        assertEquals(false, subdiv.isMonopoly());
        check = (JCheckBox) findComponent(JCheckBox.class, "fCheckScoreSeparately", panel);
        assertNotNull(check);
        assertEquals(false, subdiv.isScoreSeparately());
        check.doClick();
        awtSleep();
        assertEquals(true, subdiv.isScoreSeparately());
        check.doClick();
        awtSleep();
        assertEquals(false, subdiv.isScoreSeparately());
        JRadioButton buttonQual = (JRadioButton) findComponent(JRadioButton.class, "fRadioQualifying", panel);
        assertNotNull("buttonQual is null", buttonQual);
        JRadioButton buttonFinal = (JRadioButton) findComponent(JRadioButton.class, "fRadioFinal", panel);
        assertNotNull("buttonFinal is null", buttonFinal);
        assertTrue("final should be selected", buttonFinal.isSelected());
        assertTrue("subdiv should be final", subdiv.isFinalGroup());
        try {
            clickOn(buttonQual);
            assertTrue("qual should be selected", buttonQual.isSelected());
            assertTrue("subdiv should be qual", subdiv.isQualifyingGroup());
            clickOn(buttonFinal);
            assertTrue("final should be selected", buttonFinal.isSelected());
            assertTrue("subdiv should be final", subdiv.isFinalGroup());
        } catch (Exception e) {
            fail("unexpected exception: " + e.toString());
        }
        JButton okButton = (JButton) findComponentByName(JButton.class, "fButtonExit", panel);
        clickOn(okButton);
        assertEquals("Number of windows is incorrect", 0, getOpenWindowCount());
    }
}
