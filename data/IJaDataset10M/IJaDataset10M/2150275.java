package pcgen.gui.tabs.ability;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import pcgen.core.Ability;
import pcgen.core.AbilityCategory;
import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.gui.tabs.components.AddItemPanel;
import pcgen.gui.tabs.components.FilterPanel;
import pcgen.gui.utils.ClickHandler;
import pcgen.gui.utils.JTreeTable;
import pcgen.gui.utils.JTreeTableMouseAdapter;
import pcgen.gui.utils.PObjectNode;
import pcgen.gui.utils.Utility;
import pcgen.util.PropertyFactory;

/**
 * This class creates and manages a panel containing available Abilities for the
 * user to choose from.
 * 
 * <p>The panel consists of a <tt>FilterPanel</tt> at the top, a 
 * <tt>JTreeTable</tt> taking up most of the area and an <tt>AddItemPanel</tt>
 * at the bottom.  Methods are provided to control which components are visible
 * and configure the individual components.
 * 
 * @see pcgen.gui.tabs.components.FilterPanel
 * @see pcgen.gui.tabs.components.AddItemPanel
 * @see pcgen.gui.utils.JTreeTable
 * 
 * @author boomer70 <boomer70@yahoo.com>
 * 
 * @since 5.11.1
 *
 */
public class AvailableAbilityPanel extends AbilitySelectionPanel {

    private static final String AVAILABLE_LABEL = PropertyFactory.getString("in_available") + ": ";

    private AddItemPanel theAddButton;

    private JMenuItem theAddMenu;

    /**
	 * Construct the available ability panel.
	 * 
	 * <p>This method adds a <tt>FilterPanel</tt> and <tt>AddItemPanel</tt> to 
	 * the base table.
	 * 
	 * @param aPC
	 * @param aCategory
	 * 
	 * @see pcgen.gui.tabs.components.FilterPanel
	 * @see pcgen.gui.tabs.components.AddItemPanel
	 */
    public AvailableAbilityPanel(final PlayerCharacter aPC, final AbilityCategory aCategory) {
        super(aPC, aCategory);
        setLayout(new BorderLayout());
        add(new FilterPanel(this, AVAILABLE_LABEL), BorderLayout.NORTH);
        theAddButton = new AddItemPanel();
        theAddButton.addActionListener(new ActionListener() {

            public void actionPerformed(@SuppressWarnings("unused") final ActionEvent evt) {
                addAbility();
            }
        });
        add(theAddButton, BorderLayout.SOUTH);
    }

    /**
	 * Overrided to add a Mouse Listener and Popup menu to the table.
	 * 
	 * @see pcgen.gui.tabs.ability.AbilitySelectionPanel#initComponents()
	 */
    @Override
    protected void initComponents() {
        super.initComponents();
        theTable.addMouseListener(new JTreeTableMouseAdapter(theTable, new AvailableClickHandler(), false));
        theTable.addPopupMenu(new AbilityPopupMenu(theTable));
    }

    private void addAbility() {
        final Object temp = theTable.getTree().getLastSelectedPathComponent();
        final Ability ability = getAbilityFromObject(temp);
        if (ability == null) {
            return;
        }
        for (final IAbilitySelectionListener listener : getListeners()) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    listener.addAbility(ability);
                }
            });
        }
    }

    private void setAddEnabled(final boolean enabled) {
        theAddButton.setEnabled(enabled);
        theAddMenu.setEnabled(enabled);
    }

    private class AbilityPopupMenu extends JPopupMenu {

        private JMenuItem poolFullMenuItem;

        private JMenuItem noQualifyMenuItem;

        private JTreeTable theTreeTable;

        private AbilityPopupMenu(final JTreeTable aTreeTable) {
            theTreeTable = aTreeTable;
            final String menuText = PropertyFactory.getFormattedString("InfoAbility.Menu.Add", getCategory().getDisplayName());
            final String menuTip = PropertyFactory.getFormattedString("InfoAbility.Menu.Add.Tooltip", getCategory().getDisplayName());
            this.add(theAddMenu = Utility.createMenuItem(menuText, new ActionListener() {

                public void actionPerformed(@SuppressWarnings("unused") ActionEvent evt) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            addAbility();
                        }
                    });
                }
            }, null, (char) 0, "shortcut EQUALS", menuTip, "Add16.gif", true));
            final String NO_QUALIFY_MESSAGE = PropertyFactory.getString("InfoAbility.Messages.NotQualified");
            final String POOL_FULL_MESSAGE = PropertyFactory.getString("InfoAbility.Messages.NoPoints");
            noQualifyMenuItem = Utility.createMenuItem(NO_QUALIFY_MESSAGE, null, null, (char) 0, null, null, null, false);
            poolFullMenuItem = Utility.createMenuItem(POOL_FULL_MESSAGE, null, null, (char) 0, null, null, null, false);
        }

        /**
		 * Display the appropriate menu.
		 * 
		 * <p>The menu is modified to let the user know why they can't select
		 * an ability if they are not able to.
		 * 
		 * @see javax.swing.JPopupMenu#show(java.awt.Component, int, int)
		 */
        @Override
        public void show(final Component source, final int x, final int y) {
            final PObjectNode node = (PObjectNode) theTreeTable.getTree().getLastSelectedPathComponent();
            if (node != null && node.getItem() instanceof Ability) {
                final Ability ability = (Ability) ((PObjectNode) theTreeTable.getTree().getLastSelectedPathComponent()).getItem();
                if (getPC().canSelectAbility(ability)) {
                    removeAll();
                    add(theAddMenu);
                } else if (getPC().getAvailableAbilityPool(getCategory()).compareTo(BigDecimal.ZERO) < 0) {
                    removeAll();
                    add(poolFullMenuItem);
                } else {
                    removeAll();
                    add(noQualifyMenuItem);
                }
                super.show(source, x, y);
            } else {
                removeAll();
                super.show(source, x, y);
            }
        }
    }

    private class AvailableClickHandler implements ClickHandler {

        /**
		 * @see pcgen.gui.utils.ClickHandler#singleClickEvent()
		 */
        public void singleClickEvent() {
        }

        /**
		 * Select the Ability.
		 * 
		 * @see pcgen.gui.utils.ClickHandler#doubleClickEvent()
		 */
        public void doubleClickEvent() {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    addAbility();
                }
            });
        }

        /**
		 * @see pcgen.gui.utils.ClickHandler#isSelectable(java.lang.Object)
		 */
        public boolean isSelectable(Object obj) {
            return !(obj instanceof String);
        }
    }

    /**
	 * @see pcgen.gui.tabs.ability.IAbilityListFilter#accept(ViewMode, pcgen.core.Ability)
	 */
    @Override
    public boolean accept(final ViewMode aMode, Ability anAbility) {
        if (super.accept(aMode, anAbility) == false) {
            return false;
        }
        if (aMode == ViewMode.PREREQTREE) {
            return true;
        }
        if (anAbility.isMultiples()) {
            return true;
        } else if (getPC().hasVirtualAbility(getCategory(), anAbility)) {
            return true;
        }
        return !getPC().hasAbility(null, Ability.Nature.ANY, anAbility);
    }

    /**
	 * @see pcgen.gui.tabs.ability.AbilitySelectionPanel#getAbilityList()
	 */
    @Override
    protected List<Ability> getAbilityList() {
        return Globals.getAbilityList(getCategory());
    }

    /**
	 * @see pcgen.gui.tabs.ability.AbilitySelectionPanel#getOptionKey()
	 */
    @Override
    protected String getOptionKey() {
        return "available";
    }

    /**
	 * @see pcgen.gui.tabs.ability.AbilitySelectionPanel#getDefaultViewMode()
	 */
    @Override
    public ViewMode getDefaultViewMode() {
        return ViewMode.PREREQTREE;
    }

    /**
	 * @see pcgen.gui.tabs.ability.AbilitySelectionPanel#abilitySelected(pcgen.core.Ability)
	 */
    @Override
    protected void abilitySelected(final Ability anAbility) {
        super.abilitySelected(anAbility);
        if (anAbility != null) {
            setAddEnabled(getPC().canSelectAbility(anAbility));
        } else {
            setAddEnabled(false);
        }
    }
}
