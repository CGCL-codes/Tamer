package yuba.util;

/**
 * description 
 *
 * @author hoge1000
 *
 */
public class TimeAna {

    long started = System.currentTimeMillis();

    String label;

    /**
     * 
     */
    public TimeAna(String l) {
        label = l;
    }

    public void result() {
        System.out.println("TIME" + label + ": " + (System.currentTimeMillis() - started));
    }
}
