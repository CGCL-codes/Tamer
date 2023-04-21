package glengineer.agents.settings;

public class GapSettings extends Settings {

    public Sizes sizes;

    public GapSettings() {
        sizes = new Sizes();
    }

    public GapSettings(Sizes sizes) {
        this.sizes = sizes;
    }

    public GapSettings(int size) {
        sizes = new Sizes(size, size, size);
    }

    public GapSettings(int min, int pref, int max) {
        sizes = new Sizes(min, pref, max);
    }
}
