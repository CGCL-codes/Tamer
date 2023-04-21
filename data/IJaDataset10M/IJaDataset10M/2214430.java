package org.webball.draw;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.webball.controller.Controller;
import org.webball.draw.Draw;
import org.webball.pitch.Pitch;
import org.webball.pitch.Team;
import org.webball.player.Player;

public class TestDraw extends BasicGame {

    private Pitch pitch;

    private Player checkedPlayer;

    private boolean checked = false;

    Input input;

    Controller control = new Controller();

    public TestDraw(String title) {
        super(title);
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        Draw d = new Draw(1);
        d.draw(g, pitch);
    }

    public void init(GameContainer container) throws SlickException {
        pitch = new Pitch();
        Player[] players1 = new Player[6];
        Player[] players2 = new Player[6];
        for (int i = 0; i < 6; i++) {
            players1[i] = new Player();
            players2[i] = new Player();
        }
        pitch.setTeam(0, new Team(players1));
        pitch.setTeam(1, new Team(players2));
    }

    public void update(GameContainer container, int delta) throws SlickException {
        pitch.update();
        Input input = container.getInput();
        control.action(pitch, input);
    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new TestDraw("Webball"));
        app.setMaximumLogicUpdateInterval(10);
        app.setMinimumLogicUpdateInterval(50);
        app.setVSync(true);
        app.setTargetFrameRate(35);
        app.setDisplayMode(800, 600, false);
        app.start();
    }
}
