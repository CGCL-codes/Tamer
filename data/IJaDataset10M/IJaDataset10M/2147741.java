package pcgen.gui.tabs.ability;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import pcgen.core.Ability;
import pcgen.core.PlayerCharacter;
import pcgen.core.utils.CoreUtility;
import pcgen.gui.utils.JLabelPane;
import pcgen.gui.utils.Utility;
import pcgen.util.PropertyFactory;
import static pcgen.gui.HTMLUtils.*;

/**
 * @author boomer70 <boomer70@yahoo.com>
 * 
 * @since 5.11.1
 */
public class AbilityInfoPanel extends JPanel {

    private PlayerCharacter thePC;

    private Ability theAbility = null;

    private JLabelPane theInfoLabel = new JLabelPane();

    /**
	 * Constructs a panel for displaying information about an ability.
	 * 
	 * @param aPC The PC this ability could be associated with. (Used to check
	 * requirements)
	 * @param aTitle The title to display for this component.
	 */
    public AbilityInfoPanel(final PlayerCharacter aPC, final String aTitle) {
        super();
        thePC = aPC;
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c = new GridBagConstraints();
        setLayout(gridbag);
        final JScrollPane infoScroll = new JScrollPane();
        Utility.buildConstraints(c, 0, 0, 1, 1, 1, 1);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        gridbag.setConstraints(infoScroll, c);
        final TitledBorder title1 = BorderFactory.createTitledBorder(aTitle);
        title1.setTitleJustification(TitledBorder.CENTER);
        infoScroll.setBorder(title1);
        theInfoLabel.setBackground(getBackground());
        infoScroll.setViewportView(theInfoLabel);
        Utility.setDescription(infoScroll, PropertyFactory.getString("in_infoScrollTip"));
        add(infoScroll);
    }

    /**
	 * Sets the PlayerCharacter this panel is displaying information for.
	 * 
	 * @param aPC The PlayerCharacter to set.
	 */
    public void setPC(final PlayerCharacter aPC) {
        thePC = aPC;
    }

    /**
	 * Sets the <tt>Ability</tt> information will be displayed about.
	 * 
	 * @param anAbility The <tt>Ability</tt> to display
	 */
    public void setAbility(final Ability anAbility) {
        theAbility = anAbility;
        theInfoLabel.setText(getDisplayString());
    }

    private String getDisplayString() {
        if (theAbility == null) {
            return HTML + END_HTML;
        }
        final StringBuffer sb = new StringBuffer();
        sb.append(HTML).append(BOLD);
        sb.append(theAbility.piSubString());
        sb.append(END_BOLD).append(TWO_SPACES);
        sb.append(BOLD);
        sb.append(PropertyFactory.getString("in_type")).append(':');
        sb.append(END_BOLD);
        sb.append(CoreUtility.join(theAbility.getTypeList(true), '.'));
        final String costStr = theAbility.getCostString();
        if (!costStr.equals("1")) {
            sb.append(' ').append(BOLD);
            sb.append(PropertyFactory.getString("Ability.Info.Cost")).append(':');
            sb.append(END_BOLD);
            sb.append(costStr);
        }
        if (theAbility.isMultiples()) {
            sb.append(TWO_SPACES).append(PropertyFactory.getString("Ability.Info.Multiple"));
        }
        if (theAbility.isStacks()) {
            sb.append(TWO_SPACES).append(PropertyFactory.getString("Ability.Info.Stacks"));
        }
        final String cString = theAbility.preReqHTMLStrings(thePC, false);
        if (cString.length() > 0) {
            sb.append(TWO_SPACES);
            sb.append(BOLD);
            sb.append(PropertyFactory.getString("in_requirements")).append(':');
            sb.append(END_BOLD);
            sb.append(cString);
        }
        sb.append(TWO_SPACES);
        sb.append(BOLD);
        sb.append(PropertyFactory.getString("in_descrip")).append(':');
        sb.append(END_BOLD);
        sb.append(theAbility.piDescSubString(thePC));
        sb.append(TWO_SPACES);
        sb.append(BOLD);
        sb.append(PropertyFactory.getString("in_sourceLabel")).append(':');
        sb.append(END_BOLD);
        sb.append(theAbility.getDefaultSourceString());
        sb.append(END_HTML);
        return sb.toString();
    }
}
