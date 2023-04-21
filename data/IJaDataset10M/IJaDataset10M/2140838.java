package emulator.hardware.clock;

/** Synchronization object for components. 
 *  components synchronize on the system clock by "consuming ticks". */
public interface ClockHandle {

    /** Consume one tick of the system clock. */
    void tick();

    /** Consume x ticks of the system clock. */
    void tick(int x);

    /** Gets elapsed ticks */
    long getTicks();
}
