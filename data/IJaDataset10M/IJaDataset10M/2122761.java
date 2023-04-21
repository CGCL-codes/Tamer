package utils;

/**
 *
 * @author quique
 */
public interface ITimer {

    public void timerStarted(Timer timer);

    public void timerStopped(Timer timer);

    public void timerDeath(Timer timer);

    public void timerInterval(Timer timer);
}
