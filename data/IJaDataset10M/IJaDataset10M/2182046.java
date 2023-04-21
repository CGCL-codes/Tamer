package ch.tarnet.library.swing;

public class AbstractApplicationExceptionText extends AbstractApplication {

    @Override
    protected void createGUI() {
        throw new IllegalArgumentException("bla bla");
    }

    public static void main(String[] args) {
        new AbstractApplicationExceptionText();
    }
}
