package gene.core.util;

/**
 *
 * @author vlm
 */
public class Camera {

    short x_y_w_h[] = { 0, 0, 0, 0 };

    public Camera(int x, int y, int w, int h, int limitX, int limitY) {
        this.x_y_w_h[0] += x;
        this.x_y_w_h[1] += y;
        this.x_y_w_h[2] += w;
        this.x_y_w_h[3] += h;
    }

    public boolean Collids() {
        return true;
    }

    public boolean active() {
        return true;
    }

    public void move(int x, int y) {
        this.x_y_w_h[0] += x;
        this.x_y_w_h[1] += y;
    }

    public void setX(int x) {
        this.x_y_w_h[0] = (short) x;
    }

    public void setY(int y) {
        this.x_y_w_h[1] = (short) y;
    }

    public void setH(int h) {
        this.x_y_w_h[2] = (short) h;
    }

    public void setW(int w) {
        this.x_y_w_h[3] = (short) w;
    }

    public int getX() {
        return this.x_y_w_h[0];
    }

    public int getY() {
        return this.x_y_w_h[1];
    }

    public int getW() {
        return this.x_y_w_h[2];
    }

    public int getH() {
        return this.x_y_w_h[3];
    }
}
