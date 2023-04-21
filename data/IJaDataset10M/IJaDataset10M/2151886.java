package mbis.service.reservation;

/**
 * User: tmichalec
 * Date: Nov 3, 2008
 * Time: 3:39:54 PM
 */
public class NotEnoughLessonCapacityException extends Exception {

    private int capacity;

    private int used;

    private int needed;

    public NotEnoughLessonCapacityException() {
        super();
    }

    public NotEnoughLessonCapacityException(int capacity, int used, int needed) {
        super();
        this.capacity = capacity;
        this.used = used;
        this.needed = needed;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getNeeded() {
        return needed;
    }

    public void setNeeded(int needed) {
        this.needed = needed;
    }
}
