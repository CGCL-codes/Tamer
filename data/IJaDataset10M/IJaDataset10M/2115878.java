package net.sf.moviekebab.logic;

import java.awt.Dimension;

/**
 * @deprecated use {@link net.sf.moviekebab.logic.cut.SceneListSkill} instead.
 *
 * @author Laurent Caillette
 */
public interface SceneListSkill {

    void updateSceneList();

    void setThumbnailSize(Dimension dimension);
}
