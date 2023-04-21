package de.carne.fs.core.transfer;

/**
 * Interface used to format all kind of <code>long</code> based flag values.
 */
public interface ShortFlagFormatter {

    /**
	 * Get the <code>short</code> mask representing the flags formatted by this instance.
	 * 
	 * @return The flag mask of this formatter.
	 */
    public short mask();

    /**
	 * Format a <code>short</code> flag.
	 * 
	 * @param buffer <code>StringBuilder</code> receiving the formatted <code>short</code> flag.
	 * @param s The masked flag value.
	 * @return The updated <code>StringBuilder</code>
	 */
    public StringBuilder formatFlag(StringBuilder buffer, short s);
}
