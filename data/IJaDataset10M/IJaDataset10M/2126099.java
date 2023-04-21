package engine.tiros;

import engine.*;
import engine.personagens.Entidade;

public class Missil extends BaseTiro {

    public Missil(String name, Bloco bloco) {
        super(name, bloco);
    }

    @Override
    public void move() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean colision() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void colisionAction(Entidade entidade) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
