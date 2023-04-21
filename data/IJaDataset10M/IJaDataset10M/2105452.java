package org.encog.bot.dataunit;

/**
 * A data unit that holds code.
 *
 * @author jheaton
 *
 */
public class CodeDataUnit extends DataUnit {

    /**
	 * The code for this data unit.
	 */
    private String code;

    /**
	 * @return THe code for this data unit.
	 */
    public final String getCode() {
        return this.code;
    }

    /**
	 * Set the code to the specified string.
	 *
	 * @param str
	 *            The new code.
	 */
    public final void setCode(final String str) {
        this.code = str;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final String toString() {
        return this.code;
    }
}
