package pcgen.gui.tabs.ability;

import java.awt.FlowLayout;
import java.math.BigDecimal;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pcgen.core.AbilityCategory;
import pcgen.core.PlayerCharacter;
import pcgen.gui.utils.Utility;
import pcgen.util.BigDecimalHelper;
import pcgen.util.PropertyFactory;

/**
 * @author boomer70 <boomer70@yahoo.com>
 * 
 * @since 5.11.1
 */
public class AbilityPoolPanel extends JPanel {

    private PlayerCharacter thePC;

    private AbilityCategory theCategory;

    private JTextField theNumAbilitiesField = new JTextField();

    /**
	 * Construct the panel and add all the components.
	 * 
	 * @param aPC The PC
	 * @param aCategory The <tt>AbilityCategory</tt> this panel represents.
	 */
    public AbilityPoolPanel(final PlayerCharacter aPC, final AbilityCategory aCategory) {
        super();
        thePC = aPC;
        theCategory = aCategory;
        setLayout(new FlowLayout());
        final JLabel abilitiesRemainingLabel = new JLabel();
        abilitiesRemainingLabel.setText(PropertyFactory.getFormattedString("InfoAbility.Remaining.Label", theCategory));
        add(abilitiesRemainingLabel);
        theNumAbilitiesField.setInputVerifier(new InputVerifier() {

            @Override
            public boolean verify(final JComponent input) {
                final String text = ((JTextField) input).getText();
                if (text.length() > 0) {
                    try {
                        if (theCategory.allowFractionalPool() == false) {
                            Integer.parseInt(text);
                            return true;
                        }
                        Double.parseDouble(text);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean shouldYieldFocus(final JComponent input) {
                final boolean valueOk = super.shouldYieldFocus(input);
                if (!valueOk) {
                    getToolkit().beep();
                } else {
                    if (theNumAbilitiesField.getText().length() > 0) {
                        final BigDecimal expectedValue = thePC.getAvailableAbilityPool(theCategory);
                        final BigDecimal newValue = new BigDecimal(theNumAbilitiesField.getText());
                        thePC.adjustAbilities(theCategory, newValue.subtract(expectedValue));
                    } else {
                        showRemainingAbilityPoints();
                    }
                }
                return valueOk;
            }
        });
        showRemainingAbilityPoints();
        theNumAbilitiesField.setColumns(3);
        theNumAbilitiesField.setEditable(aCategory.allowPoolMod());
        Utility.setDescription(theNumAbilitiesField, PropertyFactory.getFormattedString("InfoAbility.Pool.Description", theCategory.getDisplayName()));
        add(theNumAbilitiesField);
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
	 * Displays the current number of remaining points in the ability pool.
	 */
    public void showRemainingAbilityPoints() {
        theNumAbilitiesField.setText(BigDecimalHelper.trimBigDecimal(thePC.getAvailableAbilityPool(theCategory)).toString());
    }
}
