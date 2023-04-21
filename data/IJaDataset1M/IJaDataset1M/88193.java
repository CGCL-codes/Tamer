package org.vizzini.example.gin.ui;

import org.vizzini.game.IAgent;
import org.vizzini.util.IProvider;

/**
 * Provides a mouse human agent provider.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.4
 */
public class MouseHumanAgentProvider implements IProvider<IAgent> {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * @see  org.vizzini.util.IProvider#create()
     */
    public MouseHumanAgent create() {
        MouseHumanAgent answer = new MouseHumanAgent();
        return answer;
    }
}
