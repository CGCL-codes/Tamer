package net.sourceforge.javautil.developer.ui.command.set.release;

import java.io.IOException;
import net.sourceforge.javautil.classloader.resolver.impl.ClassPackageImpl;
import net.sourceforge.javautil.developer.common.ide.IIDEProject;
import net.sourceforge.javautil.developer.common.project.release.IProjectRelease;
import net.sourceforge.javautil.developer.common.project.release.sourceforge.SourceForgeReleaseSystem;
import net.sourceforge.javautil.developer.ui.command.set.DeveloperCommandContext;
import net.sourceforge.javautil.network.ssh.SecureShell;
import net.sourceforge.javautil.ui.cli.annotation.CLIConvert;
import net.sourceforge.javautil.ui.command.annotation.Argument;
import net.sourceforge.javautil.ui.command.annotation.Command;
import net.sourceforge.javautil.ui.command.annotation.UICommandContext;

/**
 * Commands for creating {@link IProjectRelease}'s.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class ReleaseCommandSet {

    private DeveloperCommandContext context;

    private ClassPackageReleaseFactory factory = new ClassPackageReleaseFactory();

    private SourceForgeReleaseSystem sourceforge = new SourceForgeReleaseSystem(factory, new SecureShell());

    @UICommandContext
    public DeveloperCommandContext getContext() {
        return context;
    }

    public void setContext(DeveloperCommandContext context) {
        this.context = context;
    }

    @CLIConvert
    public IIDEProject getProject(String name) {
        return context.getIdeContext().getWorkspace().getProject(name);
    }

    @Command(value = "Release a project to sourceforge.net", arguments = { @Argument(name = "project", value = "The project to release"), @Argument(name = "version", value = "The version release") })
    public void sourceforge(IIDEProject project, String version) throws IOException {
        sourceforge.release(project, ClassPackageImpl.decode(version), true);
    }
}
