package megatennis.business;

/**
 * This is class of business layer which represents the ball in the tennis.
 * @author Ivan Klim
 */
public class Ball {

    /**
     * Default value of dx
     */
    public static final int defaultDx = 1;

    /**
     * Default value of dy
     */
    public static final int defaultDy = 2;

    /**
     * Default value of r
     */
    public static final int defaultR = 4;

    private Long id;

    /**
     * Current position of the ball X-direction 
     */
    private int x;

    /**
     * Current position of the ball Y-direction 
     */
    private int y;

    /**
     * Radius of the ball
     */
    private int r;

    /**
     * Elementary component of movement X-direction. It defines X-direction speed. 
     */
    private int dx;

    /**
     * Elementary component of movement Y-direction. It defines Y-direction speed. 
     */
    private int dy;

    private float acceleration;

    Ball() {
    }

    public Ball(int x0, int y0, int r, int dx, int dy, float acceleration) {
        if (x0 < 0 || y0 < 0 || r < 0) {
            throw new IllegalArgumentException("Wrong argument");
        } else {
            this.x = x0;
            this.y = y0;
            this.r = r;
            this.dx = dx;
            this.dy = dy;
            this.acceleration = acceleration;
        }
    }

    public Ball(int x0, int y0) {
        if (x0 < 0 || y0 < 0) {
            throw new IllegalArgumentException("Wrong argument");
        } else {
            this.x = x0;
            this.y = y0;
            this.r = defaultR;
            this.dx = defaultDx;
            this.dy = defaultDy;
        }
    }

    public Long getId() {
        return id;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    public int getR() {
        return this.r;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    Long getIdPersistent() {
        return id;
    }

    void setIdPersistent(Long id) {
        this.id = id;
    }

    int getDxPersistent() {
        return dx;
    }

    void setDxPersistent(int dx) {
        this.dx = dx;
    }

    public int getDyPersistent() {
        return dy;
    }

    public void setDyPersistent(int dy) {
        this.dy = dy;
    }

    public int getRPersistent() {
        return r;
    }

    public void setRPersistent(int r) {
        this.r = r;
    }

    public int getXPersistent() {
        return x;
    }

    public void setXPersistent(int x) {
        this.x = x;
    }

    public int getYPersistent() {
        return y;
    }

    public void setYPersistent(int y) {
        this.y = y;
    }
}
