package net.sf.sbcc.colorpanel;

import java.awt.GridBagConstraints;
import junit.framework.TestCase;

/**
 * <br>
 * Modifications:
 * <ul>
 * <!--
 * <li> some text here (2008-mm-dd, Christoph Bimminger)</li>
 * -->
 * </ul>
 * <br>
 * <br>
 * <i>This class is part of the Swing Better Components Collection (SBCC), which is an open source project. 
 * The project is available at <a href="http://sbcc.sourceforge.net" >http://sbcc.sourceforge.net</a> and
 * is distributed under the conditions of the GNU Library or Lesser General Public License (LGPL).</i><br>
 * <br>
 * Filename: JColorArrayPanelTest.java<br>
 * Last modified: 2008-04-19<br>
 * 
 * @author Christoph Bimminger

 
 */
public class JColorArrayPanelTest extends TestCase {

    public void testGridBagConstraint() {
        JColorArrayPanel F_colorArrayPanel = new JColorArrayPanel();
        F_colorArrayPanel.setBreakCount(5);
        GridBagConstraints F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(0);
        assertEquals(0, F_gbc.gridx);
        assertEquals(0, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(1);
        assertEquals(1, F_gbc.gridx);
        assertEquals(0, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(2);
        assertEquals(2, F_gbc.gridx);
        assertEquals(0, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(3);
        assertEquals(3, F_gbc.gridx);
        assertEquals(0, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(4);
        assertEquals(4, F_gbc.gridx);
        assertEquals(0, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(5);
        assertEquals(0, F_gbc.gridx);
        assertEquals(1, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(6);
        assertEquals(1, F_gbc.gridx);
        assertEquals(1, F_gbc.gridy);
        F_colorArrayPanel.setBreakCount(3);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(0);
        assertEquals(0, F_gbc.gridx);
        assertEquals(0, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(1);
        assertEquals(1, F_gbc.gridx);
        assertEquals(0, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(2);
        assertEquals(2, F_gbc.gridx);
        assertEquals(0, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(3);
        assertEquals(0, F_gbc.gridx);
        assertEquals(1, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(4);
        assertEquals(1, F_gbc.gridx);
        assertEquals(1, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(5);
        assertEquals(2, F_gbc.gridx);
        assertEquals(1, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(6);
        assertEquals(0, F_gbc.gridx);
        assertEquals(2, F_gbc.gridy);
        F_colorArrayPanel.setOrientation(JColorArrayPanel.VERTICAL);
        F_colorArrayPanel.setBreakCount(5);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(0);
        assertEquals(0, F_gbc.gridx);
        assertEquals(0, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(1);
        assertEquals(0, F_gbc.gridx);
        assertEquals(1, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(2);
        assertEquals(0, F_gbc.gridx);
        assertEquals(2, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(3);
        assertEquals(0, F_gbc.gridx);
        assertEquals(3, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(4);
        assertEquals(0, F_gbc.gridx);
        assertEquals(4, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(5);
        assertEquals(1, F_gbc.gridx);
        assertEquals(0, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(6);
        assertEquals(1, F_gbc.gridx);
        assertEquals(1, F_gbc.gridy);
        F_colorArrayPanel.setBreakCount(3);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(0);
        assertEquals(0, F_gbc.gridx);
        assertEquals(0, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(1);
        assertEquals(0, F_gbc.gridx);
        assertEquals(1, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(2);
        assertEquals(0, F_gbc.gridx);
        assertEquals(2, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(3);
        assertEquals(1, F_gbc.gridx);
        assertEquals(0, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(4);
        assertEquals(1, F_gbc.gridx);
        assertEquals(1, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(5);
        assertEquals(1, F_gbc.gridx);
        assertEquals(2, F_gbc.gridy);
        F_gbc = F_colorArrayPanel.getNextComponentGridBagConstraints(6);
        assertEquals(2, F_gbc.gridx);
        assertEquals(0, F_gbc.gridy);
    }
}
