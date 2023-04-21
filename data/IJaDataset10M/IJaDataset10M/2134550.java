package model;

public class RemoveEntityEvent extends Event {

    private String eType;

    private String id;

    public RemoveEntityEvent(String id, String target) {
        eType = target;
        this.id = id;
    }

    @Override
    public void execute() {
        sys.removeFreeEntitis(eType);
    }
}
