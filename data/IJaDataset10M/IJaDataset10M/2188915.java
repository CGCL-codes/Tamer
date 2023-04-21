package org.jfge.api.arena;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jfge.api.effect.ArenaEffect;
import org.jfge.spi.graphics.GraphicsFactory;
import org.jfge.spi.graphics.Image;
import org.jfge.spi.render.ArenaRenderer;
import com.google.inject.Inject;
import com.google.inject.Provider;

public final class ArenaFactoryImpl implements ArenaFactory {

    private GraphicsFactory graphicsFactory;

    private Map<String, Provider<ArenaEffect>> arenaEffectMap;

    @Inject
    private ArenaFactoryImpl(GraphicsFactory graphicsFactory, Map<String, Provider<ArenaEffect>> arenaEffectMap) {
        this.graphicsFactory = graphicsFactory;
        this.arenaEffectMap = arenaEffectMap;
    }

    @Override
    public Arena createArena(String file, ArenaRenderer arenaRenderer) throws IOException {
        Image backGround = this.graphicsFactory.createImage(file);
        List<ArenaState> arenaStates = new ArrayList<ArenaState>();
        arenaStates.add(new Intro(arenaRenderer, "Intro", 99, 20, "Running", null, false));
        arenaStates.add(new Running("Running", 20, "Finished", arenaEffectMap.get("FightAnimation") != null ? arenaEffectMap.get("FightAnimation").get() : null, false));
        arenaStates.add(new Finished(arenaRenderer, "Finished", 20, "Intro", null, false));
        return new ArenaImpl("Arena", arenaStates, "Intro", backGround, arenaRenderer);
    }
}
