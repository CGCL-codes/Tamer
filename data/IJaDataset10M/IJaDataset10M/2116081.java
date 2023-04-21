package remote.control;

/** Low-level mote status type codes. */
public interface MoteStatusType {

    int UNKNOWN = 0;

    int UNAVAILABLE = 1;

    int STOPPED = 2;

    int RUNNING = 3;

    int PROGRAMMING = 4;
}
