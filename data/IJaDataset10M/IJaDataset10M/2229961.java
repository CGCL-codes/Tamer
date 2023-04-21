package jorgan;

import jorgan.spi.SetupRegistry;

public class Setup {

    public static void main(String[] args) {
        SetupRegistry.setup();
        App.main(args);
    }
}
