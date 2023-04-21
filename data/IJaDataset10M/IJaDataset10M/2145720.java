package iaik.pkcs.pkcs11;

import iaik.pkcs.pkcs11.wrapper.CK_SLOT_INFO;
import iaik.pkcs.pkcs11.wrapper.Constants;
import iaik.pkcs.pkcs11.wrapper.Functions;

/**
 * Objects of this class represet slots that can accpet tokens. The application
 * can get a token object, if there is one present, by calling getToken.
 * This may look like this:
 * <pre><code>
 *   Token token = slot.getToken();
 *
 *   // to ensure that there is a token present in the slot
 *   if (token != null) {
 *     // ... work with the token
 *   }
 * </code></pre>
 *
 * @see iaik.pkcs.pkcs11.SlotInfo
 * @see iaik.pkcs.pkcs11.Token
 * @author <a href="mailto:Karl.Scheibelhofer@iaik.at"> Karl Scheibelhofer </a>
 * @version 1.0
 * @invariants (module_ <> null)
 */
public class Slot {

    /**
   * The module that created this slot object.
   */
    protected Module module_;

    /**
   * The indentifier of the slot.
   */
    protected long slotID_;

    /**
   * The constructor that takes a reference to the module and the slot ID.
   *
   * @param module The reference to the module of this slot.
   * @param slotID The identifier of the slot.
   * @preconditions (pkcs11Module <> null)
   * @postconditions
   */
    protected Slot(Module module, long slotID) {
        if (module == null) {
            throw new NullPointerException("Argument \"module\" must not be null.");
        }
        module_ = module;
        slotID_ = slotID;
    }

    /**
   * Compares the slot ID and the module_ of this object with the slot ID and
   * module_ of the other object. Returns only true, if both are equal.
   *
   * @param otherObject The other Slot object.
   * @return True, if other is an instance of Slot and the slot ID and module_
   *         of both objects are equal. False, otherwise.
   * @preconditions
   * @postconditions
   */
    public boolean equals(Object otherObject) {
        boolean equal = false;
        if (otherObject instanceof Slot) {
            Slot other = (Slot) otherObject;
            equal = (this == other) || ((this.slotID_ == other.slotID_) && this.module_.equals(other.module_));
        }
        return equal;
    }

    /**
   * Get the module that created this Slot object.
   *
   * @return The module of this slot.
   * @preconditions
   * @postconditions
   */
    public Module getModule() {
        return module_;
    }

    /**
   * Get the ID of this slot. This is the ID returned by the PKCS#11
   * module.
   *
   * @return The ID of this slot.
   * @preconditions
   * @postconditions
   */
    public long getSlotID() {
        return slotID_;
    }

    /**
   * Get information about this slot object.
   *
   * @return An object that contains informatin about this slot.
   * @exception TokenException If reading the information fails.
   * @preconditions
   * @postconditions (result <> null)
   */
    public SlotInfo getSlotInfo() throws TokenException {
        CK_SLOT_INFO ckSlotInfo = module_.getPKCS11Module().C_GetSlotInfo(slotID_);
        return new SlotInfo(ckSlotInfo);
    }

    /**
   * Get an object for handling the token that is currently present in this
   * slot, or null, if there is no token present.
   *
   * @return The object for accessing the token. Or null, if none is present
   *         in this slot.
   * @exception TokenException If determining if a token is present fails.
   * @preconditions
   * @postconditions
   */
    public Token getToken() throws TokenException {
        Token token = null;
        if (getSlotInfo().isTokenPresent()) {
            token = new Token(this);
        }
        return token;
    }

    /**
   * The overriding of this method should ensure that the objects of this class
   * work correctly in a hashtable.
   *
   * @return The hash code of this object. Gained from the slot ID.
   * @preconditions
   * @postconditions
   */
    public int hashCode() {
        return (int) slotID_;
    }

    /**
   * Returns the string representation of this object.
   *
   * @return the string representation of this object
   */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Slot ID: ");
        buffer.append("0x");
        buffer.append(Functions.toHexString(slotID_));
        buffer.append(Constants.NEWLINE);
        buffer.append("Module: ");
        buffer.append(module_.toString());
        return buffer.toString();
    }
}
