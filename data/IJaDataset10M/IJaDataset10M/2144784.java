package org.jfge.games.mk2.game;

import org.jfge.api.game.Game;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

public class MortalKombat2GameModule extends AbstractModule {

    @Override
    protected void configure() {
        MapBinder<String, Game> gameBinder = MapBinder.newMapBinder(binder(), String.class, Game.class);
        gameBinder.addBinding("mortalKombat2").toProvider(MortalKombat2Game.class);
    }
}
