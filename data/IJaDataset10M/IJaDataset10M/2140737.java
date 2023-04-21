package org.metaphile.directory.app3;

import java.util.HashMap;
import java.util.Map;
import org.metaphile.TypeUtils;
import org.metaphile.directory.IfdDirectory;
import org.metaphile.tag.TypedTag;
import org.metaphile.tag.app3.KodakEffectsTag;

/**
 * 
 * @author stuart
 * @since 0.1.1
 */
public class EffectsDirectory implements IfdDirectory {

    private Boolean isBigEndian = true;

    private Map<TypedTag, byte[]> values = new HashMap<TypedTag, byte[]>();

    public void setIsBigEndian(Boolean isBigEndian) {
        this.isBigEndian = isBigEndian;
    }

    public Boolean ignoreIfdAlignment() {
        return false;
    }

    public Boolean getIsBigEndian() {
        return isBigEndian;
    }

    public String getDirectoryName() {
        return "Kodak Effects";
    }

    public Integer getDirectoryIdentifier() {
        return 0xc36e;
    }

    public TypedTag getTagByIdentifier(Integer identifier) {
        return KodakEffectsTag.getTagByIdentifier(identifier);
    }

    public Map<TypedTag, byte[]> getValues() {
        return values;
    }

    public Integer[] getDigitalEffectsVersion() {
        return TypeUtils.bytesToUnsignedIntegerArray(values.get(KodakEffectsTag.DIGITAL_EFFECTS_VERSION), 1, isBigEndian);
    }

    public String getDigitalEffectsName() {
        return TypeUtils.bytesToString(values.get(KodakEffectsTag.DIGITAL_EFFECTS_NAME));
    }

    public Integer getDigitalEffectsType() {
        return TypeUtils.bytesToUnsignedInteger(values.get(KodakEffectsTag.DIGITAL_EFFECTS_TYPE), isBigEndian);
    }
}
