package com.googlecode.mvnmigrate;

import org.apache.ibatis.migration.commands.VersionCommand;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal which execute the ibatis migration version command.
 *
 * @version $Id: VersionCommandMojo.java 154 2010-07-24 12:52:25Z marco.speranza79 $
 * @goal version
 */
public class VersionCommandMojo extends AbstractCommandMojo<VersionCommand> {

    /**
     * Version string.
     *
     * @parameter expression="${migration.version}"
     * @required
     */
    protected String version;

    /**
     * Creates a instance of  version command mojo.
     */
    public VersionCommandMojo() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    VersionCommand createCommandClass() {
        return new VersionCommand(repository, environment, force);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isSkip()) {
            return;
        }
        init();
        getCommand().execute(this.version);
    }
}
