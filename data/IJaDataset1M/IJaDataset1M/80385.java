package engine.tiros;

import engine.*;
import engine.personagens.Combatente;
import engine.personagens.Entidade;
import engine.personagens.Personagem;
import java.util.Iterator;

public class TiroInimigo extends Tiros {

    public TiroInimigo(String name, Bloco bloco, Mapa mapa, Personagem source) {
        super(name, bloco, source.isRight());
        this.mapa = mapa;
        this.sourcePers = source;
    }

    @Override
    public void move() {
        setRelY(getY());
        if (this.isleft()) {
            setX(getX() - movX);
        } else {
            setX(getX() + movX);
        }
        setY(getY() - movY);
        this.setRelX(this.getX() - mapa.getX());
        if (this.getRelX() > constantes.ConstantesPainelAcao.WIDTH || this.getRelX() < 0) setAlive(false);
        colision();
    }

    @Override
    public synchronized boolean colision() {
        Iterator<Entidade> it = mapa.getEntityIterator();
        boolean colision = false;
        while (it.hasNext()) {
            Entidade entidade = it.next();
            if (this.intersects(entidade)) {
                if (entidade == mapa.getPlayer()) {
                    this.colisionAction(entidade);
                    colision = true;
                }
            }
        }
        return colision;
    }

    @Override
    public void colisionAction(Entidade entidade) {
        if (entidade instanceof Combatente) {
            Combatente comb = (Combatente) entidade;
            comb.damage(10, sourcePers);
            this.setAlive(false);
        }
    }

    protected Mapa mapa = null;

    Personagem sourcePers = null;
}
