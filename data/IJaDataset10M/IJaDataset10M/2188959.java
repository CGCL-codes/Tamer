package ar.com.omnipresence.web.backing;

import ar.com.omnipresence.game.client.UniverseTO;
import java.util.List;

public interface GlobalGame {

    List<UniverseTO> getAllUniverses();

    String getValidUserNamePattern();
}
