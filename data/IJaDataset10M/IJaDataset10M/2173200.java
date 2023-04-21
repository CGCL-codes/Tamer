package gothwag.mapgeneration.util;

import java.util.Random;
import java.awt.Point;

/**
 * <p>Title: gothwag</p>
 * <p>Description: </p>
 * <p>Copyright: no copyright, GPL</p>
 * <p>Company: gothwag organization</p>
 * @author chenyu
 * @version 0.0.1
 */
public class DirectionControl {

    Random m_random;

    int m_x_increment;

    int m_y_increment;

    double m_x_possibility;

    /**
   * m_x_count, m_y_count variables are used for statistic check
   */
    int m_x_count;

    int m_y_count;

    public DirectionControl(double i_direction) {
        System.out.println("i_direction: " + i_direction);
        m_random = new Random();
        int t_array_size = 0;
        int t_location;
        double t_x;
        double t_y;
        t_y = Math.sin(i_direction);
        t_x = Math.cos(i_direction);
        m_x_increment = t_x > 0 ? 1 : -1;
        m_y_increment = t_y > 0 ? 1 : -1;
        m_x_possibility = t_x / (Math.abs(t_y) + Math.abs(t_x));
        m_x_count = 0;
        m_y_count = 0;
    }

    public Point nextStep() {
        double t = m_random.nextInt(10000) / 10000.0;
        if (t > m_x_possibility) {
            m_y_count++;
            return new Point(0, m_y_increment);
        } else {
            m_x_count++;
            return new Point(m_x_increment, 0);
        }
    }

    public void getXPercentage() {
        System.out.println("************** get X percentage *************");
        System.out.println("x percentage: " + m_x_count / (1.0 * (m_y_count + m_x_count)));
    }

    public static void main(String[] args) {
        DirectionControl t_control = new DirectionControl(Math.PI / 2);
        for (int i = 0; i < 10; i++) {
            System.out.println(t_control.nextStep());
        }
    }
}
