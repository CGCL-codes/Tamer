package de.miethxml.hawron.project;

import de.miethxml.hawron.net.PublishListener;

/**
 *
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 *
 *
 *
 *
 *
 *
 *
 */
public interface ProjectListener extends ProcessListener, PublishListener {

    public void startProcessing();

    public void taskStart(Task task);

    public void taskEnd();

    public void endProcessing();
}
