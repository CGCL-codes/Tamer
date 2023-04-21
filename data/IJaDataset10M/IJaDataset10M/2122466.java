package edu.harvard.fas.rregan.requel.project.command;

import edu.harvard.fas.rregan.requel.annotation.command.EditPositionCommand;
import edu.harvard.fas.rregan.requel.project.ProjectOrDomain;

/**
 * Create or edit a position on a LexicalIssue for adding an actor to the
 * project.
 * 
 * @author ron
 */
public interface EditAddActorToProjectPositionCommand extends EditPositionCommand {

    /**
	 * @param projectOrDomain -
	 *            the project or domain to add the actor to.
	 */
    public void setProjectOrDomain(ProjectOrDomain projectOrDomain);
}
