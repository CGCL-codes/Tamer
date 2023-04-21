package panoramic.geometry;

public class PanoVertex2 {

    private double m_x, m_y;

    public PanoVertex2() {
    }

    public PanoVertex2(double x, double y) {
        m_x = x;
        m_y = y;
    }

    public void setX(double x) {
        m_x = x;
    }

    public void setY(double y) {
        m_y = y;
    }

    public void setPos(double x, double y) {
        m_x = x;
        m_y = y;
    }

    public double getX() {
        return m_x;
    }

    public double getY() {
        return m_y;
    }
}
