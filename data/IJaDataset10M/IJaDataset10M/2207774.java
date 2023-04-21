package Game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class GradiusOpening extends Scene {

    private Background background;

    public GradiusOpening() {
        background = new Background(0, 0, 800, 600);
        background.Load("/imagens/GradiusOpening.png");
    }

    @Override
    public void update(InputManager input) {
        if (input.ChecarTecla(KeyEvent.VK_ENTER)) SceneManager.MudarCena();
    }

    @Override
    public void draw(Graphics2D g2d) {
        background.Draw(g2d);
    }
}
