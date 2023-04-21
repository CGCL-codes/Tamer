package gui;

import engine.Personagem;
import java.awt.Graphics;

/**
 *
 * @author gilvolpe
 */
public abstract class GuiPersonagem {

    public GuiPersonagem(PainelAcao painel, Personagem personagem) {
        this.painel = painel;
        this.personagem = personagem;
    }

    public abstract void draw(Graphics g);

    public Personagem getPersonagem() {
        return this.personagem;
    }

    protected Personagem personagem;

    protected PainelAcao painel;
}
