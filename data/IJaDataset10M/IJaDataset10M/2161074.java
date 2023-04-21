package org.jsynthlib.synthdrivers.RolandD10;

import org.jsynthlib.core.Patch;

/**
 * This class handles bit manipulation for the pratial mute checkboxes which
 * operates on the same underlying data byte. The model is used by the
 * PartMuteParamModel for getting and setting bits and the PartMuteSender
 * for retrieving data to send.
 *
 * @author Roger Westerlund
 */
class PartMuteDataModel extends D10ParamModel {

    public PartMuteDataModel(Patch patch, int offset) {
        super(patch, offset);
    }

    public int get(int bit) {
        return get() & (1 << bit);
    }

    public void set(int bit, int value) {
        if (value != 0) {
            super.set(super.get() | (1 << bit));
        } else {
            super.set(super.get() & ~(1 << bit));
        }
    }

    public int getData() {
        return super.get();
    }
}
