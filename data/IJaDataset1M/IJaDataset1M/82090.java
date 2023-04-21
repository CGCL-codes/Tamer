package net.todd.games.boardgame;

import javax.media.j3d.Bounds;

public interface ICameraGenerator {

    void createCamera(IUniverse su, Bounds bounds);
}
