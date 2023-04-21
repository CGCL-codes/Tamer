package popel;

public class PopTestBase {

    private int aPrivateBase;

    static final String aPackageBase = "drei";

    public volatile float aPublicBase;

    private transient char aTransiBase;

    public PopTestBase() {
        aPrivateBase = 10;
        aPublicBase = 30.5f;
        aTransiBase = 'd';
    }
}
