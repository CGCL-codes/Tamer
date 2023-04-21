package net.sf.buildbox.devmodel.fs.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import net.sf.buildbox.buildrobot.model.VcsLocation;
import net.sf.buildbox.devmodel.VersionId;
import net.sf.buildbox.devmodel.fs.api.ModuleLocator;
import net.sf.buildbox.devmodel.util.DevModelUtil;
import net.sf.buildbox.devmodel.util.FsModelUtil;
import net.sf.buildbox.util.Iterables;

/**
 * This implementation requires all modules registered in modules.txt file.
 */
public final class ModuleRegistry implements ModuleLocator {

    private ModuleDirsDaoLiveImpl modules;

    public ModuleRegistry(File modulesTxt) {
        this.modules = new ModuleDirsDaoLiveImpl(modulesTxt);
    }

    public VersionId getVersionId(VcsLocation vcsLocation) {
        try {
            final File basedir = FsModelUtil.asFile(vcsLocation);
            return modules.getModuleId(basedir);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Iterable<String> findMatchingGroupIds(final String groupId) {
        final Iterable<String> candidates = Iterables.distinct(new Iterables.Deref<VersionId, String>(allVersionIds()) {

            public String get(VersionId versionId) {
                return versionId.getGroupId();
            }
        });
        if (groupId == null) {
            return candidates;
        }
        return DevModelUtil.findMatchingGroups(candidates, groupId);
    }

    public Iterable<VcsLocation> findMatchingModules(VersionId versionTemplate) {
        return modules.findMatchingModules(versionTemplate);
    }

    public Set<VcsLocation> findModules(VcsLocation locationPrefix) {
        try {
            return modules.listModules(locationPrefix);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public VersionId saveModuleEntry(VcsLocation vcsLocation, VersionId versionId) {
        try {
            return modules.setModuleEntry(vcsLocation, versionId);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Iterable<String> allArtifacts(String groupId) {
        final Collection<String> artifactIds = new LinkedList<String>();
        for (VersionId versionId : this.allVersionIds()) {
            if (!versionId.getGroupId().equals(groupId)) continue;
            artifactIds.add(versionId.getArtifactId());
        }
        return artifactIds;
    }

    public Iterable<VersionId> allVersions(final String groupId, final String artifactId) {
        final Collection<VersionId> versionIds = new LinkedList<VersionId>();
        for (VersionId versionId : this.allVersionIds()) {
            if (!versionId.getGroupId().equals(groupId)) continue;
            if (!versionId.getArtifactId().equals(artifactId)) continue;
            versionIds.add(versionId);
        }
        return versionIds;
    }

    private Iterable<VersionId> allVersionIds() {
        return Iterables.distinct(modules.getModuleIds());
    }
}
