package sratworld.base.area;

import java.text.ParseException;
import java.util.Observable;
import java.util.Observer;

public class AreaReaderState extends AreaLoaderStateBase {

    private AreaImpl currentArea;

    private boolean hasName = false;

    public void OnEnter() {
        hasName = false;
    }

    public void OnLeave() throws ParseException {
        if (!hasName) {
            throw new ParseException("No name found in [Area]", -1);
        } else if (currentAreaName == null) {
            throw new ParseException("Multiple name lines found in [Area]", -1);
        }
    }

    public boolean isStateEntry(String line) {
        return line.equals("[Area]");
    }

    private class MapObserver implements Observer {

        public void update(Observable from, Object map) {
            currentArea = new AreaImpl(currentAreaName, (Map) map);
            setChanged();
            notifyObservers(currentArea);
            currentArea = null;
        }
    }

    private MapObserver mapObserver = new MapObserver();

    public Observer getMapObserver() {
        return mapObserver;
    }

    String currentAreaName;

    private class PropertyObserver implements Observer {

        public void update(Observable from, Object map) {
            if (hasName) {
                currentAreaName = null;
            } else {
                currentAreaName = ((PropertyPair) map).propertyValue;
                hasName = true;
            }
        }
    }

    private PropertyObserver propertyObserver = new PropertyObserver();

    public Observer getPropertyObserver() {
        return propertyObserver;
    }
}
