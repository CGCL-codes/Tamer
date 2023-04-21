package montecarlo;

public class RotateAction implements Action {

    public final int value;

    public RotateAction(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Rotation(" + value + ")";
    }
}
