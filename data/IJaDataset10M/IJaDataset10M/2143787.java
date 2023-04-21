package me.fantasy2;

/**
 * @author Cloudee
 * 
 */
public class DelayedSmallScript extends SmallScript {

    private final int delay;

    private final SmallScript target;

    /**
	 * @param delay
	 */
    public DelayedSmallScript(int delay, SmallScript target) {
        super();
        this.delay = delay;
        this.target = target;
    }

    @Override
    public boolean process() {
        if (time >= delay) {
            smallHost.addScript(target);
            return false;
        }
        return true;
    }
}
