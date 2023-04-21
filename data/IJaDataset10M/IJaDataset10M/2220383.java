package com.fj.torkel.perf;

import com.fj.engine.BaseObject;
import com.fj.engine.Lib;
import com.fj.engine.MapObject;
import com.fj.engine.RPG;
import com.fj.engine.Thing;
import com.fj.torkel.Action;
import com.fj.torkel.Game;
import com.fj.torkel.GameScreen;
import com.fj.torkel.Hero;
import com.fj.torkel.QuestApplet;
import com.fj.torkel.MapHelper;
import com.fj.torkel.NullHandler;
import com.fj.torkel.TyrantTestCase;

public class MoreKilling implements IWork {

    private Thing hero;

    private MapObject map;

    private GameScreen gameScreen;

    public MoreKilling() {
    }

    public void run() {
        boolean originalGetSet = BaseObject.GET_SET_DEBUG;
        try {
            while (hero.x < (map.getWidth() - 2)) {
                gameScreen.tryTick(hero, Action.MOVE_E, false);
            }
        } finally {
            BaseObject.GET_SET_DEBUG = originalGetSet;
        }
    }

    public void setUp() {
        RPG.setRandSeed(0);
        Lib.clear();
        hero = Hero.createHero("bob", "human", "fighter");
        TyrantTestCase.setTestHero(hero);
        NullHandler.installNullMessageHandler();
        Game.setUserinterface(null);
        String mapString = "################################" + "\n" + "#@.............................#" + "\n" + "##.............................#" + "\n" + "#..............................#" + "\n" + "################################";
        map = new MapHelper().createMap(mapString);
        for (int x = hero.x; x < map.getWidth(); x++) {
            if (!map.isBlocked(x, 1)) {
                map.addThing(Lib.create("[IsMonster]"), x, 1);
                map.addThing(Lib.create("[IsItem]"), x, 1);
                map.addThing(Lib.create("menhir"), x, 2);
                map.addThing(Lib.create("[IsMonster]"), x, 3);
                map.addThing(Lib.create("[IsItem]"), x, 3);
            }
        }
        hero.set("IsImmortal", true);
        gameScreen = new GameScreen(new QuestApplet());
        gameScreen.map = map;
    }

    public String getMessage() {
        return "";
    }
}
