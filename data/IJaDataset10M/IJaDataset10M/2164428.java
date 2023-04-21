package org.mmtk.harness.lang.type;

import org.mmtk.harness.lang.runtime.StringValue;
import org.mmtk.harness.lang.runtime.Value;

/**
 * The builtin <code>string</code> type
 */
public class StringType extends AbstractType {

    StringType() {
        super("string");
    }

    /**
   * @see org.mmtk.harness.lang.type.Type#initialValue()
   */
    @Override
    public Value initialValue() {
        return new StringValue("");
    }
}
