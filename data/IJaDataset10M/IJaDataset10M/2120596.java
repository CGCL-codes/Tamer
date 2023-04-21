package uk.gov.dti.og.fox;

import uk.gov.dti.og.fox.ex.ExInternal;
import uk.gov.dti.og.fox.track.Track;

public abstract class ConstantType extends Track {

    private boolean mIsSet = false;

    private String mDesc;

    private void setting() {
        if (mIsSet) {
            throw new ExInternal("Cannot set ConstantType more than once");
        }
        mIsSet = true;
    }

    public boolean isSet() {
        return mIsSet;
    }

    public ConstantType set(String pDescription) {
        mDesc = pDescription;
        setting();
        return this;
    }

    public final String toString() {
        return mDesc + " " + super.toString();
    }

    public abstract static class IntType extends ConstantType {

        private int mint;

        public final ConstantType set(int pIntValue, String pDescription) {
            set(pDescription);
            mint = pIntValue;
            return this;
        }

        public final int getIntValue() {
            if (!isSet()) {
                throw new ExInternal("ConstantType has not been set");
            }
            return mint;
        }
    }

    public abstract static class StringType extends ConstantType {

        private String mString;

        public final ConstantType set(String pString, String pDescription) {
            super.set(pDescription);
            mString = pString;
            return this;
        }

        public final ConstantType set(String pString) {
            super.set(pString);
            mString = pString;
            return this;
        }

        public final String getString() {
            if (!isSet()) {
                throw new ExInternal("Type has not been set");
            }
            return mString;
        }
    }
}
