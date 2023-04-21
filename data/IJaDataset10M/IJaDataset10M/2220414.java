package rtjdds.rtps;

public class InstanceHandle extends DDS.InstanceHandle {

    public InstanceHandle(int handle) {
        super(handle);
    }

    public InstanceHandle() {
        super(0);
    }

    public int getHandle() {
        return super.value;
    }

    public void setHandle(int handle) {
        super.value = handle;
    }
}
