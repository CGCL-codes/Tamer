package net.todd.games.boardgame;

import javax.media.j3d.Canvas3D;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class UniverseFactory {

    private final Canvas3D canvas3D;

    public UniverseFactory(Canvas3D canvas3D) {
        this.canvas3D = canvas3D;
    }

    public IUniverse createUniverse() {
        SimpleUniverse universe = new SimpleUniverse(canvas3D);
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.getViewer().getView().setMinimumFrameCycleTime(5);
        return new UniverseAdapter(universe);
    }
}
