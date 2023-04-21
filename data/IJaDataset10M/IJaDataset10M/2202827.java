package networking.messages.weaponMessages;

import networking.messages.SimpleMessage;

public class WeaponRemoveMessage extends SimpleMessage {

    private int weaponIndex;

    public WeaponRemoveMessage() {
        super();
    }

    public WeaponRemoveMessage(int weaponIndex) {
        this();
        this.weaponIndex = weaponIndex;
    }

    public int getWeaponIndex() {
        return weaponIndex;
    }
}
