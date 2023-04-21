package org.pluginbuilder.core.internal.subversion;

import org.eclipse.core.resources.IProject;
import org.eclipse.team.core.RepositoryProvider;
import org.pluginbuilder.core.Activator;
import org.pluginbuilder.core.internal.BuildConfig;

public class SubversionAdapterFactory {

    public static SubversionAdapter createAdapter(IProject project, BuildConfig config) {
        SubversionAdapter result = null;
        RepositoryProvider provider = RepositoryProvider.getProvider(project);
        if (provider != null && provider.getClass().getName().endsWith("team.svn.core.SVNTeamProvider")) {
            Activator.getLogger().fine("Found subversive provider for " + project.getName());
            result = new SubversiveAdapter(provider);
        } else if (provider != null && provider.getClass().getName().equals("org.tigris.subversion.subclipse.core.SVNTeamProvider")) {
            result = new SubclipseAdapter(project, provider.getClass().getClassLoader());
            Activator.getLogger().fine("Found subclipse provider for " + project.getName());
        } else {
            result = createConfiguredAdapter(project, config);
            Activator.getLogger().info("Could not find subversive/subclipse provider for " + project.getName());
        }
        return result;
    }

    public static ConfiguredSubversionAdapter createConfiguredAdapter(IProject project, BuildConfig config) {
        return new ConfiguredSubversionAdapter(config.getSvnUrl(), project.getName());
    }
}
