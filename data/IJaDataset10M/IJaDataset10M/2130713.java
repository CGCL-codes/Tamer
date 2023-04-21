package flex.messaging.io.amfx.testtypes;

public interface Cheese {

    String getName();

    String getMilkType();

    String getRegion();

    int getMaturation();

    Cheese getParing();

    void setParing(Cheese c);
}
