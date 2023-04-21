package jorgan.spi;

import jorgan.UI;
import jorgan.util.PluginUtils.Ordering;

public interface UIProvider extends Ordering {

    public UI getUI();
}
