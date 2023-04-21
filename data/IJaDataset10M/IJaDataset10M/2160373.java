package Game;

import java.awt.event.KeyEvent;

public class PlayerArkanoid extends GameObject {

    private static final long serialVersionUID = 1L;

    private boolean move_left = false;

    private boolean move_right = false;

    private float speed = 10;

    public PlayerArkanoid(float x, float y, int w, int h) {
        super(x, y, w, h);
        Load("/Imagens/barra.png");
    }

    public void Update(InputManager input) {
        if (input.ChecarTecla(KeyEvent.VK_LEFT)) move_left = true; else move_left = false;
        if (input.ChecarTecla(KeyEvent.VK_RIGHT)) move_right = true; else move_right = false;
        if (move_left) this.x -= speed;
        if (move_right) this.x += speed;
        if (this.x < 0) this.x = 0;
        if (this.x >= 800 - w) {
            this.x = 800 - w;
        }
    }
}
