package yaw.core.va;

public interface IValueOwner {

    IValueAccessor[] getValueAccessors();

    void validate();

    boolean isComplete();

    Object getOwner();
}
