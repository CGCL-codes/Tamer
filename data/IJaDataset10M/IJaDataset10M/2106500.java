package abc.soot.util;

import soot.tagkit.AttributeValueException;
import soot.tagkit.Tag;

/**
 * @author Sascha Kuzins
 */
public class AroundShadowInfoTag implements Tag {

    public static final String name = "AroundShadowInfoTag";

    public String getName() {
        return name;
    }

    public byte[] getValue() {
        throw new AttributeValueException();
    }

    public AroundShadowInfoTag(abc.weaving.weaver.around.AroundWeaver.ShadowInlineInfo shadowInfo) {
        this.shadowInfo = shadowInfo;
    }

    public final abc.weaving.weaver.around.AroundWeaver.ShadowInlineInfo shadowInfo;
}
