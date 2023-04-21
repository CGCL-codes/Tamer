package nz.ac.massey.softwarec.group3.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * SessionListenerInterface - Interface for the SessionListener class.
 * @version 1.0 Release
 * @since 1.0
 * @authors Natalie Eustace | Wanting Huang | Paul Smith | Craig Spence
 */
public interface SessionListenerInterface extends HttpSessionListener {

    @Override
    void sessionCreated(final HttpSessionEvent sessionEvent);

    @Override
    void sessionDestroyed(final HttpSessionEvent sessionEvent);
}
