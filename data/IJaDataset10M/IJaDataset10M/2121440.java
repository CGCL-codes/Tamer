package org.eclipse.ui;

/**
 * An interface that descriptor classes may implement in addition to their
 * descriptor interface. This indicates that they may or may not originate from
 * a plugin contribution. This is useful in various activity filtering
 * scenarios.
 * 
 * @since 3.0
 */
public interface IPluginContribution {

    /**
     * @return the local id of the contribution. Must not be <code>null</code>.
     *         This should correspond to the extension-specific identifier for
     *         a given contribution.
     */
    public String getLocalId();

    /**
     * @return the id of the originating plugin. Can be <code>null</code> if
     *         this contribution did not originate from a plugin.
     */
    public String getPluginId();
}
