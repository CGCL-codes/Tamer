package com.cerner.system.rest.internal.arguments;

import java.util.List;

/**
 * Abstract base class for all artifacts that check the validity of method or
 * constructor arguments.
 * 
 * @author Alex Horn
 */
abstract class AbstractValidator implements Validator {

    /**
   * Construct an error message that can be used to construct a string for an
   * {@link IllegalArgumentException}. The argument name list must have at least
   * one item in it.
   * 
   * @param names non-empty list of names for method arguments whose values did
   *          not comply with the rules
   * @return human-readable CSV of the argument names
   */
    String concat(List<String> names) {
        final StringBuilder builder = new StringBuilder(8 + 16 * names.size());
        builder.append("Argument");
        if (names.size() > 1) {
            builder.append("s");
            for (int i = 0; i < names.size() - 2; i++) {
                builder.append(" '");
                builder.append(names.get(i));
                builder.append("',");
            }
            builder.append(" '");
            builder.append(names.get(names.size() - 2));
            builder.append("' and");
        }
        builder.append(" '");
        builder.append(names.get(names.size() - 1));
        builder.append("'");
        return builder.toString();
    }
}
