package org.moyoman.client.reference.admin;

import org.moyoman.client.reference.GameEngineSwingWorker;
import org.moyoman.client.reference.util.IGUIConstants;
import javax.swing.JFrame;

/**
 * Defines methods required by Moyoman <code>AdminManager</code> owners.
 *
 * @author Jeffrey M. Thompson
 * @version v0.03
 *
 * @since v0.03
 */
public interface IMoyomanAdminOwner extends IGUIConstants {

    /**
     * Return the game engine.
     *
     * @since v0.03
     */
    GameEngineSwingWorker getEngine();

    /**
     * Return this application's frame.
     *
     * @since v0.03
     */
    JFrame getFrame();
}
