package com.shudes.game;

import java.util.*;
import junit.framework.*;
import com.shudes.pt.builder.*;
import com.shudes.pt.container.*;
import com.shudes.pt.db.*;
import com.shudes.pt.pojo.*;
import com.shudes.util.*;

public class GameTest extends TestCase {

    public GameTest(String name) {
        super(name);
    }

    public void testGame() {
        final Long ID = new Long(9091);
        PTSession session;
        GamePlayerBuilder playerBuilder;
        List<Game> games;
        SessionContext context;
        context = new SessionContext();
        session = new PTSessionBuilder(context).buildById(ID);
        games = new GameBuilder(new SessionContext()).forSession(session);
        playerBuilder = new GamePlayerBuilder(context);
        Game game = games.iterator().next();
        GamePlayerContainer container = playerBuilder.forGame(game);
        System.out.println(Dumper.INSTANCE.dumpSideways(container.getHerosAction()));
    }
}
