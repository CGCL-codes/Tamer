package net.sf.buildbox.devmodel.fs.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import net.sf.buildbox.devmodel.VersionId;
import net.sf.buildbox.devmodel.fs.api.MultiLocator;
import net.sf.buildbox.devmodel.fs.api.VersionLocator;
import net.sf.buildbox.util.Iterables;

public final class DefaultMultiLocator implements MultiLocator {

    private final Collection<VersionLocator> locators = new ArrayList<VersionLocator>();

    /**
     * Creates empty locator container.
     *
     * @see #setLocators(java.util.Collection)
     */
    public DefaultMultiLocator() {
    }

    public File locate(VersionId versionId) {
        for (VersionLocator versionLocator : locators) {
            final File location = versionLocator.computeLocation(versionId);
            if (location != null) {
                return location;
            }
        }
        if (locators.size() == 0) {
            throw new IllegalStateException("no locators available");
        }
        return null;
    }

    public Iterable<VersionId> findMatchingVersionIds(final VersionId versionTemplate) {
        return Iterables.concat(new Iterables.Deref<VersionLocator, Iterable<VersionId>>(locators) {

            public Iterable<VersionId> get(VersionLocator locator) {
                return locator.findMatchingVersionIds(versionTemplate);
            }
        });
    }

    public void setLocators(Collection<VersionLocator> locators) {
        this.locators.clear();
        this.locators.addAll(locators);
    }
}
