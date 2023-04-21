package santa.jpaint.plugin;

public interface Plugin {

    String getName();

    String getInfo();

    void onLoaded();
}
