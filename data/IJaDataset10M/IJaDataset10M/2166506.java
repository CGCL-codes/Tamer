package net.sf.buildbox.devmodel.fs.api;

import net.sf.buildbox.devmodel.VcsLocation;

/**
 * This interface is used to extend objects implementing {@link net.sf.buildbox.devmodel.fs.api.ParsedVersion}.
 * Buildrobot needs it to explicitly store some info (currently only VcsLocation) with the instance, as that cannot be later parsed from the working copy.
 */
public interface ParsedModule {

    void setVcsLocation(VcsLocation explicitVcsLocation);
}
