package spellcast.enchantment;

import spellcast.beings.IBeing;
import spellcast.damage.DamageType;
import spellcast.damage.NullDamageType;
import spellcast.model.Id;

/**
 * The default implementation of <code>Enchantment</code>.
 *
 * @author Barrie Treloar
 * @version $Revision: 121 $
 *
 * @see Enchantment
 */
public class EnchantmentImpl implements Enchantment {

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 1L;

    /** The <code>Id</code> of this <code>Enchantment</code>. */
    private Id id = Id.createId();

    /** The name of this <code>Enchantment</code>. */
    private String name = "";

    /** The description of this <code>Enchantment</code>. */
    private String description = "";

    /** Indicates whether this <code>Enchantment</code> is permanent. */
    private boolean permanent;

    /**
     * The <code>DamageTypes</code> that this <code>Enchantment</code>
     * provides resistance for.
     */
    private DamageType resistance = new NullDamageType();

    /** The number of turns an Enchantment lasts for. */
    private int duration;

    /** The caster <code>IBeing</code> of this <code>Enchantment</code>. */
    private IBeing caster;

    /** The target <code>IBeing</code> of this <code>Enchantment</code>. */
    private IBeing target;

    /**
     * Provided for serialization, do not use as a constructor.
     */
    protected EnchantmentImpl() {
    }

    /**
     * Create the <code>Enchantment</code> cast by theCaster at theTarget.
     *
     * @param theName the name of this <code>Enchantment</code>.
     * @param theCaster the <code>IBeing</code> casting the
     *        <code>Enchantment</code>.
     * @param theTarget the <code>IBeing</code> that is the target of the
     *        <code>Enchantment</code>.
     */
    public EnchantmentImpl(final String theName, final IBeing theCaster, final IBeing theTarget) {
        setName(theName);
        setCaster(theCaster);
        setTarget(theTarget);
    }

    /**
     * {@inheritDoc}
     */
    public final Id getId() {
        return id;
    }

    /**
     * Set the <code>Id</code> of this <code>Enchantment</code>.
     *
     * @param theId the <code>Id</code> of this <code>Enchantment</code>.
     */
    public final void setId(final Id theId) {
        id = theId;
    }

    /**
     * Determine is this object equals the specified object.
     *
     * @param o the object to test equality against.
     *
     * @return true if this <code>Enchantment</code> equals the specified
     *         object.
     */
    public final boolean equals(final Object o) {
        boolean result = false;
        if (o instanceof EnchantmentImpl) {
            EnchantmentImpl other = (EnchantmentImpl) o;
            result = getId().equals(other.getId());
        }
        return result;
    }

    /**
     * The hashCode of this object as per <code>Object.hashCode</code>.
     *
     * @return the hashCode of this object.
     */
    public final int hashCode() {
        int hashCode = 0;
        hashCode ^= getId().hashCode();
        return hashCode;
    }

    /**
     * {@inheritDoc}
     */
    public final String getName() {
        return name;
    }

    /**
     * Set the name of this <code>Enchantment</code>.
     *
     * @param theName the name of this <code>Enchantment</code>.
     */
    public final void setName(final String theName) {
        name = theName;
    }

    /**
     * {@inheritDoc}
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Set the description of this <code>Enchantment</code>.
     *
     * @param theDescription the description of this <code>Enchantment</code>.
     */
    public final void setDescription(final String theDescription) {
        description = theDescription;
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isPermanent() {
        return permanent;
    }

    /**
     * Sets whether this <code>Enchantment</code> is permanent.
     *
     * @param isPermanent whether this <code>Enchantment</code> is permanent.
     */
    public final void setPermanent(final boolean isPermanent) {
        permanent = isPermanent;
        duration = 0;
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isResistant(final DamageType damageType) {
        return damageType.equals(resistance);
    }

    /**
     * Gets the resistance of this <code>Enchantment</code>. If no resistances
     * then <code>NullDamageType</code> is returned.
     *
     * @return the <code>DamageType</code> that this <code>Enchantment</code>
     *         is resistant to.  If no resistances then
     *         <code>NullDamageType</code> is returned.
     */
    public final DamageType getResistance() {
        return resistance;
    }

    /**
     * Sets the resistance of this <code>Enchantment</code> to the specified
     * <code>DamageType</code>.
     *
     * @param damageType the <code>DamageType</code> this
     *        <code>Enchantment</code> will be resistant to.
     */
    public final void setResistance(final DamageType damageType) {
        resistance = damageType;
    }

    /**
     * Return the duration left on this <code>Enchantment</code> before it
     * expires.  If the <code>Enchantment</code> is permanent then this value
     * has no meaning.
     *
     * @return the duration left on this <code>Enchantment</code>.
     */
    public final int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of this <code>Enchantment</code>.
     *
     * @param theDuration the duration of the <code>Enchantment</code>.
     */
    public final void setDuration(final int theDuration) {
        duration = theDuration;
    }

    /**
     * {@inheritDoc}
     */
    public final boolean turnComplete() {
        boolean wornOff = false;
        if (!isPermanent()) {
            duration--;
            if (duration <= 0) {
                wornOff = true;
            }
        }
        return wornOff;
    }

    /**
     * {@inheritDoc}
     */
    public final IBeing getCaster() {
        return caster;
    }

    /**
     * Sets the casting <code>Wizard</code> of this <code>Enchantment</code>.
     *
     * @param theCaster the casting <code>IBeing</code>.
     */
    public final void setCaster(final IBeing theCaster) {
        caster = theCaster;
    }

    /**
     * {@inheritDoc}
     */
    public final IBeing getTarget() {
        return target;
    }

    /**
     * Sets the target <code>IBeing</code> of this <code>Enchantment</code>.
     *
     * @param theTarget the target <code>IBeing</code> of this
     *        <code>Enchantment</code>.
     */
    public final void setTarget(final IBeing theTarget) {
        target = theTarget;
    }
}
