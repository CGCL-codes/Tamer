package Game;

import java.awt.Graphics2D;
import Game.ManagerSound.Type;

public class Level02Arkanoid extends Scene {

    Background background;

    ArkanoidBall ball;

    PlayerArkanoid playerArkanoid;

    int Iniciar;

    final int NUM_COL = 15;

    final int NUM_LINE = 3;

    public Level02Arkanoid() {
        background = new Background(0, 0, 800, 600);
        background.Load("/Imagens/background.png");
        ball = new ArkanoidBall(390, 530, 20, 20, 6, 10);
        playerArkanoid = new PlayerArkanoid(350, 550, 100, 10);
        RectManager.Setup(NUM_COL, NUM_LINE, 2);
        SceneManager.currentScene = SceneManager.CENA.SCN_Level01Arkanoid;
    }

    @Override
    public void update(InputManager input) {
        if (Iniciar < 30) {
            Iniciar++;
        } else {
            RectManager.Update(ball);
            playerArkanoid.Update(input);
            if (playerArkanoid.Collision(ball)) {
                ball.speedy *= -1;
                ManagerSound.getIntance().Play(Type.COLLISION);
            }
            if (RectManager.recs.size() <= 0) {
                SceneManager.MudarCena();
            }
            ball.Update();
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        background.Draw(g2d);
        RectManager.Draw(g2d);
        ball.Draw(g2d);
        playerArkanoid.Draw(g2d);
    }
}
