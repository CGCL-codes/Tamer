package opala.lexing;

import java.io.Serializable;
import java.net.URL;

public interface ILocation extends Serializable {

    URL getURL();

    int getCharPos();

    int getLinePos();
}
