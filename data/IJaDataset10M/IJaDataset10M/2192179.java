package ise.ui;

import ise.utilities.Timer;
import processing.core.PApplet;

/**
 *  TODO: DOCUMENT ME!
 *
 * @author Andy Bursavich
 * @version 0.1
  */
public class ProgressBar extends Component {

    float progress;

    int MILLIS = 6000;

    /**
   * Creates a new ProgressBar object.
   *
   * @param p DOCUMENT ME!
   * @param x DOCUMENT ME!
   * @param y DOCUMENT ME!
   * @param width DOCUMENT ME!
   * @param height DOCUMENT ME!
   */
    public ProgressBar(PApplet p, float x, float y, float width, float height) {
        super(p);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
   * TODO: DOCUMENT ME!
   *
   * @param timer DOCUMENT ME!
   */
    public void update(Timer timer) {
        progress = (timer.getMillisecondsActive() % MILLIS) / (float) (MILLIS - 1);
    }

    /**
   * TODO: DOCUMENT ME!
   */
    @Override
    protected void drawComponent() {
        float border = 2.0f;
        float innerWidth = width - border - border;
        float innerHeight = height - border - border;
        p.noStroke();
        p.fill(255);
        p.rect(0, 0, width, height);
        p.fill(50);
        p.rect(border, border, innerWidth, innerHeight);
        p.fill(0, 0xCC, 0);
        p.rect(border, border, innerWidth * progress, innerHeight);
    }
}
