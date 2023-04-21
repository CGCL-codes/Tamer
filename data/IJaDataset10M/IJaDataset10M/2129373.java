package blue.tools.soundFont;

import org.apache.commons.lang.builder.ToStringBuilder;

public class PresetInfo {

    public String num;

    public String name;

    public String bank;

    public String presetNum;

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
