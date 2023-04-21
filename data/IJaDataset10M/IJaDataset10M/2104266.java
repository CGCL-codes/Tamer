package spellcast.enchantment;

import spellcast.beings.IBeing;
import spellcast.beings.IWizard;

/**
 * The Fear <code>Enchantment</code>.
 *
 * @author Barrie Treloar
 * @version $Revision: 121 $
 */
public class FearEnchantment extends EnchantmentImpl {

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 1L;

    /** The duration for <code>FearEnchantment</code>. */
    private static final int DURATION = 1;

    /** The description for <code>FearEnchantment</code>. */
    private static final String DESCRIPTION = "In the turn following the casting of this spell, the subject cannot " + "perform a C, D, F or S gesture. This obviously has no effect on " + "monsters. If the subject is also the subject of 'amnesia', " + "'confusion', 'charm person', 'charm monster' or 'paralysis', then " + "none of the spells work.";

    /** The name for <code>FearEnchantment</code>. */
    private static final String NAME = "Fear";

    /**
     * Provided for serialization, do not use as a constructor.
     */
    protected FearEnchantment() {
    }

    /**
     * Creates a new <code>FearEnchantment</code> object.
     *
     * @param theCaster the <code>IBeing</code> casting the
     *        <code>Enchantment</code>.
     * @param theTarget the <code>Wizard</code> that is the target of the
     *        <code>Enchantment</code>.
     */
    public FearEnchantment(final IBeing theCaster, final IWizard theTarget) {
        super(NAME, theCaster, theTarget);
        setDuration(DURATION);
        setDescription(DESCRIPTION);
    }
}
