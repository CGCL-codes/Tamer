package windu.sms.thread;

/**
 *
 * @author Cakrabuana
 */
public class GlobalVar {

    private int sign;

    private boolean available = false;

    public synchronized int getSign() {
        while (available == false) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        available = false;
        notifyAll();
        System.out.println("consume");
        return sign;
    }

    public synchronized void setSign(int sign) {
        while (available == true) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        System.out.println("produce");
        this.sign = sign;
        available = true;
        notifyAll();
    }
}
