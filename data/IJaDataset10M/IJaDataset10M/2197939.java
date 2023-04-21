package badkaaui.input;

/**
 *
 * @author Shashank Tulsyan
 */
public abstract class Input<E> {

    public enum TYPE {

        TENSOR, ASCII_ALPHABETS, ALPHA_NUMERIC, UNICODE, KEY_INPUT, MOUSE_INPUT, AUDIO, INPUT_STREAM, OTHER
    }

    public abstract int[] getDimensions();

    @Override
    public abstract String toString();

    public abstract TYPE getType();

    public abstract E getData();

    public boolean wasValueObtainedByReference() {
        return true;
    }
}
