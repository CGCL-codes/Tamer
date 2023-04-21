package org.dolmen.container.behaviors;

/**
 *
 * @since 0.0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public interface Startable {

    public void start() throws Exception;

    public void stop() throws Exception;

    public boolean suspend() throws Exception;

    public boolean resume() throws Exception;

    public int getState();

    public String getStateAsString();
}
