package ch.simas.nbtostring.builder;

/**
 * A factory class to create the appropriate builder type.
 * @author Simon
 */
public class ToStringBuilderFactory {

    private ToStringBuilderFactory() {
    }

    /**
     * Creates the builder specified by the type. By default a builder using simple string concatenation is returned.
     * @param sbt the builder type to create
     * @return the builder according to {@code sbt} or a string concatenator
     */
    public static ToStringBuilder createToStringBuilder(ToStringBuilderType sbt) {
        if (sbt == ToStringBuilderType.STRINGBUILDER) {
            return new StringBuilderToStringBuilder(sbt);
        } else if (sbt == ToStringBuilderType.STRINGBUFFER) {
            return new StringBufferToStringBuilder(sbt);
        }
        return new StringToStringBuilder(sbt);
    }
}
