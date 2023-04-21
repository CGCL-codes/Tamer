package werkzeugkasten.gainer.conf;

import java.util.List;
import werkzeugkasten.gainer.AinEventListener;
import werkzeugkasten.gainer.DinEventListener;
import werkzeugkasten.gainer.OnBoardButtonEventListener;

public interface Configuration {

    String portId();

    ConfigType type();

    void addButtonListener(OnBoardButtonEventListener listener);

    void addAinListener(AinEventListener listener);

    void addDinListener(DinEventListener listener);

    void notifyButton(boolean pressed);

    void notifyAin(List<Number> ain);

    void notifyDin(List<Boolean> din);
}
