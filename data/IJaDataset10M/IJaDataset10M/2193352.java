package geometry.base;

import java.awt.Graphics;

public interface DrawAble {

    /**
     * Da jedes Objekt für sich selbst verantwortlich ist, sich selbst
     * zu zeichnen, ist diese Methode gedacht.
     * 
     * @param g Graphic Handler
     */
    public void Draw(Graphics g);
}
