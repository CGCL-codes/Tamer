package net.sf.japaki.kit;

import net.sf.japaki.beans.Property;
import net.sf.japaki.text.Parser;

/**
 * Bean to store parser properties before creating the parser.
 * @version 1.0
 * @author Ralph Wagner
 */
public interface ParserMold<V> {

    /**
     * Creates a parser for the specified property and pattern.
     * @param property property of the created parser
     * @param parameter the set of parameters for the parser.
     * @return new parser
     * @throws NullPointerException if parameter is {@code null} or
     *  if property or some parameter is {@code null} and this mold does not
     *  allow it.
     * @throws IllegalArgumentException if the pattern cannot be interpreted
     *  by this mold.
     */
    <B> Parser<B> getParser(Property<B, V> property, Parameter parameter);

    /**
     * Returns the target type of the parsers created by this mold.
     * @return the target type of the parsers created by this mold
     */
    Class<V> getTargetType();

    /**
     * Parameters given to a mold to specify the characteristics of a parser
     * or its underlying format.
     */
    public static class Parameter {

        private String pattern;

        private Integer size;

        /**
         * Returns the pattern string for format based parsers.
         * @return the pattern
         */
        public String getPattern() {
            return pattern;
        }

        /**
         * Returns the size parameter.
         * @return the size parameter
         */
        public Integer getSize() {
            return size;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public void setSize(Integer size) {
            this.size = size;
        }
    }
}
