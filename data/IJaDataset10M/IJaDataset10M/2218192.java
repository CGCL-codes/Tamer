package de.jrpgcore.rpg.items;

import de.jrpgcore.rpg.foundation.*;
import de.jrpgcore.rpg.foundation.WearableKinds;
import de.jrpgcore.rpg.utils.DiceCup;
import de.jrpgcore.rpg.utils.Die;
import java.util.HashMap;

/**
 * @author wolfenlord
 * @since Oct 16, 2010 2:02:31 AM
 */
public class Ring extends WearableItem implements Wearable, Protector {

    public Ring() {
        super();
    }

    public Ring(String typus) {
        super(typus, WearableKinds.ring);
    }

    public <S> boolean matchesBodyPart(S part) {
        return part == BodyParts.leftRing || part == BodyParts.rightRing;
    }

    public boolean isTimedOut() {
        return false;
    }

    public HashMap<ProtectionKind, DiceCup> getProtection() {
        return new HashMap() {

            {
                put(ProtectionKind.mental, new DiceCup() {

                    {
                        add(2, Die.D6);
                    }
                });
            }
        };
    }
}
