package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;

public class AcolyteGame {

    AcolyteGameType gameType;

    List<AcolytePlayer> players;

    Iterator<AcolytePlayer> turnOrder;

    AcolytePlayer activePlayer;

    Iterator<String> phases;

    String phase;

    public void endPhase() {
        if (phases.hasNext()) {
            phase = phases.next();
        } else {
        }
    }

    public void setProperty(AcolytePlayer targetPlayer, String name, String value) {
    }

    public List<AcolyteCard> viewZone(AcolytePlayer viewingPlayer, String zone, AcolytePlayer targetPlayer) {
        return null;
    }

    public void moveCard(AcolytePlayer actingPlayer, AcolyteCard card, String targetZone, AcolytePlayer targetPlayer) {
    }
}
