package org.skycastle.gameobjects.creating.design;

import org.skycastle.core.old.GameObject;
import org.skycastle.gameobjects.communication.forum.Forum;
import org.skycastle.gameobjects.creating.library.Library;
import org.skycastle.gameobjects.economy.license.License;

/**
 * Represents non-physical, (typically character created) design in the world, such as blueprints, texts, maps, musical
 * scores, paintings, sculptures, etc.
 * <p/>
 * Based on this {@link Design}, it may be possible to create specific implementations - physical products such as
 * houses or shovels or books, or performances such as music or coreography.
 * <p/>
 * {@link Design}s can be licensed under different {@link License}s, and they have a {@link ChangeHistory} of the edits
 * made to them.
 * <p/>
 * {@link Design}s can also have comments.
 * <p/>
 * {@link Design}s can be collected in {@link Library}-ies.
 */
public interface Design extends GameObject {

    /**
     * @return the license that this Design is available under (in game as well as outside the game).
     */
    License getLicense();

    /**
     * @return a history of the changes done to this Design.
     */
    ChangeHistory getChangeHistory();

    Forum getDiscussionForum();
}
