package net.sourceforge.glsof.common.lsof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.io.BufferedReader;
import java.io.IOException;

public class LsofOutputParser extends Observable {

    private static final Map<Character, Integer> IDENTIFIERS_LIST;

    static {
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        map.put('c', 0);
        map.put('p', 1);
        map.put('g', 2);
        map.put('R', 3);
        map.put('u', 4);
        map.put('L', 4);
        map.put('f', 5);
        map.put('a', 5);
        map.put('l', 5);
        map.put('t', 6);
        map.put('d', 7);
        map.put('D', 7);
        map.put('o', 8);
        map.put('s', 8);
        map.put('k', 9);
        map.put('i', 10);
        map.put('P', 10);
        map.put('S', 11);
        map.put('n', 11);
        IDENTIFIERS_LIST = Collections.unmodifiableMap(map);
    }

    private boolean _running;

    public LsofOutputParser(final Observer observer) {
        addObserver(observer);
    }

    public void parse(final BufferedReader bufferedReader) {
        String[] buffer = new String[] { "", "", "", "", "", "", "", "", "", "", "", "" };
        boolean parsedFD = false;
        String tmpFD = "";
        try {
            String line;
            while (_running && (line = bufferedReader.readLine()) != null) {
                final char id = line.charAt(0);
                if (id == 'p') {
                    if (parsedFD) {
                        buffer[5] = tmpFD;
                        notify(buffer);
                    }
                    buffer = new String[] { "", line.substring(1), "", "", "", "", "", "", "", "", "", "" };
                    parsedFD = false;
                    continue;
                }
                if (id == 'f') {
                    if (parsedFD) {
                        buffer[5] = tmpFD;
                        notify(buffer);
                        buffer = new String[] { buffer[0], buffer[1], buffer[2], buffer[3], buffer[4], "", "", "", "", "", "", "" };
                    }
                    tmpFD = line.substring(1);
                    parsedFD = true;
                    continue;
                }
                if (id == 'T') {
                    buffer[11] += " (" + line.substring(1) + ")";
                    continue;
                }
                final int index = columnIdentifier(id);
                if (index != -1) {
                    if (index == 5) {
                        tmpFD += line.substring(1);
                    } else {
                        buffer[index] = line.substring(1);
                    }
                }
            }
            buffer[5] = tmpFD;
            if (parsedFD) {
                notify(buffer);
            }
            notify(null);
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notify(final String[] buffer) {
        setChanged();
        notifyObservers(buffer);
    }

    public int columnIdentifier(final char id) {
        Integer i = IDENTIFIERS_LIST.get(id);
        return i == null ? -1 : i;
    }

    public boolean isRunning() {
        return _running;
    }

    public void setRunning(final boolean running) {
        _running = running;
    }
}
