package kits.info;

import javax.sound.midi.InvalidMidiDataException;
import kits.TdKit;
import kits.TdSubPart;
import exceptions.VdrumException;

/**
 * @author egolan
 */
public abstract class TdInfo {

    private final String name;

    private final int startNameIndex;

    private final int maxNameLength;

    private final int numberOfSubParts;

    private final int maxNumberOfKits;

    private final int msbAddressIndex;

    private final int kitSize;

    private final int kitIdIndex;

    private final int subPartIndex;

    private final int msbAddressValue;

    TdInfo(String name, int startNameIndex, int maxNameLength, int numberOfSubParts, int maxNumberOfKits, int msbAddressIndex, int msbAddressValue, int kitSize, int kitIdIndex, int subPartIndex) {
        this.name = name;
        this.startNameIndex = startNameIndex;
        this.maxNameLength = maxNameLength;
        this.numberOfSubParts = numberOfSubParts;
        this.maxNumberOfKits = maxNumberOfKits;
        this.msbAddressIndex = msbAddressIndex;
        this.msbAddressValue = msbAddressValue;
        this.kitSize = kitSize;
        this.kitIdIndex = kitIdIndex;
        this.subPartIndex = subPartIndex;
    }

    public final int getStartNameIndex() {
        return this.startNameIndex;
    }

    public final int getMaxLengthName() {
        return this.maxNameLength;
    }

    public final int getNumberOfSubParts() {
        return numberOfSubParts;
    }

    public final int getMaxNumberOfKits() {
        return maxNumberOfKits;
    }

    public final int getKitSize() {
        return kitSize;
    }

    public final int getMsbAddressIndex() {
        return msbAddressIndex;
    }

    public final String getNameToDisplay() {
        return name;
    }

    public final int getKitIdIndex() {
        return kitIdIndex;
    }

    public final int getSubPartIndex() {
        return subPartIndex;
    }

    public final int getMsbAddressValue() {
        return msbAddressValue;
    }

    @Override
    public final boolean equals(Object obj) {
        return getNameToDisplay().equals(((TdInfo) obj).getNameToDisplay());
    }

    @Override
    public final int hashCode() {
        return getNameToDisplay().hashCode();
    }

    @Override
    public final String toString() {
        return getNameToDisplay();
    }

    public final TdKit getKit(byte[] kitBytes) throws InvalidMidiDataException, VdrumException {
        return new TdKit(this, kitBytes);
    }

    public final TdKit getNewKit(TdSubPart[] newSubParts) {
        return new TdKit(this, newSubParts);
    }

    public final TdSubPart getNewSubPart(TdSubPart subPart, Integer newId) throws InvalidMidiDataException {
        return new TdSubPart(subPart, newId, this);
    }
}
