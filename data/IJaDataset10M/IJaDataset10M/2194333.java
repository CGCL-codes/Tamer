package com.google.i18n.pseudolocalization.message;

/**
 * A {@link MessageFragment} which reprsents a placeholder, a value to be filled
 * in at runtime.
 */
public interface Placeholder extends MessageFragment {

    /**
   * Get the textual representation of this placeholder as it would appear
   * in the reassembled message.  For example, MessageFormat-style placeholders
   * might return {0}.
   *
   * @return textual representation
   */
    String getTextRepresentation();
}
