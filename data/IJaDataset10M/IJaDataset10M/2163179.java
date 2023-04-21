package plugin.charactersheet.gui;

import java.util.ArrayList;
import java.util.List;
import pcgen.core.Constants;
import pcgen.core.Equipment;
import pcgen.core.PlayerCharacter;

/**
 * <code>WeaponListPane</code>.
 * 
 * Confirmed no memory Leaks Dec 10, 2004
 * @author  ddjone3
 * @version	$Revision: 1.15 $
 */
public class WeaponListPane extends javax.swing.JPanel {

    private PlayerCharacter pc;

    private ArrayList weaponList = new ArrayList();

    private int serial = 0;

    /**
	 * Constructor
	 */
    public WeaponListPane() {
        initComponents();
        setLocalColor();
    }

    private void initComponents() {
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
    }

    /**
	 * Set the colour for the local pane and others in the weapon list.
	 */
    public void setColor() {
        setLocalColor();
        for (int i = 0; i < weaponList.size(); i++) {
            WeaponPane weapPane = (WeaponPane) weaponList.get(i);
            weapPane.setColor();
        }
    }

    /**
	 * Set the colour for the local pane.
	 */
    public void setLocalColor() {
        setBackground(CharacterPanel.white);
    }

    /**
	 * Set the Player Character.
	 * 
	 * @param pc Player Character to assocaiate with the pane.
	 */
    public void setPc(PlayerCharacter pc) {
        if (this.pc != pc) {
            this.pc = pc;
            serial = 0;
        }
    }

    /**
	 * Refresh the displayed list of weapons. The method is synchronized as
	 * multiple events may trigger refreshes when swapping characters, and the
	 * list must only be processed by one refresh at a time, otherwise
	 * duplicates may appear in the list.
	 */
    public synchronized void refresh() {
        if (serial < pc.getSerial()) {
            pc.setCalcEquipmentList(pc.getUseTempMods());
            List weaps = pc.getExpandedWeapons(Constants.MERGE_ALL);
            addEqPanes(weaps);
            serial = pc.getSerial();
        }
        revalidate();
        repaint();
    }

    /**
	 * Remove components from the current pane and all those panes in the weapon list.  
	 * Remove the panes from the weapon list.
	 */
    public void clear() {
        removeAll();
        for (int i = weaponList.size() - 1; i >= 0; i--) {
            WeaponPane pane = (WeaponPane) weaponList.get(i);
            pane.clear();
            weaponList.remove(pane);
            pane = null;
        }
        weaponList.clear();
    }

    private void addEqPanes(List weapons) {
        clear();
        int gridBag = 1;
        for (int i = 0; i < weapons.size(); i++) {
            WeaponPane weaponPane = new WeaponPane();
            weaponPane.setWeapon(pc, (Equipment) weapons.get(i));
            weaponList.add(weaponPane);
            weaponPane.refresh();
            add(weaponPane);
            gridBag++;
        }
    }

    /**
	 * Clear the current pane.
	 * 
	 * @see #clear()
	 */
    public void destruct() {
        clear();
    }
}
