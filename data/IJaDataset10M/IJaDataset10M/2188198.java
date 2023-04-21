package org.jbfilter.impl;

class _CaseSensitiveDelegate {

    private boolean caseSensitive = false;

    private Boolean caseSensitiveCleanValue = Boolean.FALSE;

    /**
	 * Indicates if the matching must be case-sensitive or not.
	 * By default matching is NOT case-sensitive.
	 * @return according to contract
	 */
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
	 * See getter.
	 * @param caseSensitive
	 */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    /**
	 * The clean value. Set it to {@code null} to ignore while clearing.
	 * @param caseSensitiveCleanValue
	 */
    public void setCaseSensitiveCleanValue(Boolean caseSensitiveCleanValue) {
        this.caseSensitiveCleanValue = caseSensitiveCleanValue;
    }

    /**
	 * Defaults to {@link Boolean#FALSE}.
	 * @see #setCaseSensitiveCleanValue(Boolean)
	 */
    public Boolean getCaseSensitiveCleanValue() {
        return caseSensitiveCleanValue;
    }
}
