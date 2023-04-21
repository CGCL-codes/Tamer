package simple.framework.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.EventObject;
import javax.swing.event.EventListenerList;

public class SerialConfigParser {

    protected EventListenerList listenerList = new EventListenerList();

    public void addConfigItemListener(ConfigItemListener l) {
        listenerList.add(ConfigItemListener.class, l);
    }

    private void fireConfigItemParsed(String item) {
        Object[] listeners = this.listenerList.getListenerList();
        EventObject e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ConfigItemListener.class) {
                if (e == null) {
                    e = new EventObject(item);
                }
                ((ConfigItemListener) listeners[i + 1]).configItemParsed(e);
            }
        }
    }

    public void parse(BufferedReader reader) {
        try {
            StringBuilder item = new StringBuilder();
            char[] buff = new char[256];
            int n;
            boolean commented = false;
            while ((n = reader.read(buff)) >= 0) {
                int i = 0;
                while (i < n) {
                    if (commented) {
                        while (i < n && buff[i] != '\n') i++;
                        commented = (i >= n);
                        i++;
                        continue;
                    }
                    int k = i;
                    while (k < n && buff[k] != '\n' && buff[k] != '#') k++;
                    if (k == n) {
                        item.append(buff, i, k - i);
                        i = n;
                        continue;
                    }
                    commented = (buff[k] == '#');
                    int j = k - 1;
                    while (j >= i && Character.isWhitespace(buff[j])) j--;
                    if (j >= i && buff[j] == '\\') {
                        item.append(buff, i, j - i);
                        i = k + 1;
                        continue;
                    }
                    item.append(buff, i, k - i);
                    if (item.length() > 0) {
                        String s = item.toString().trim();
                        if (s.length() > 0) fireConfigItemParsed(s);
                    }
                    item.setLength(0);
                    i = k + 1;
                }
            }
            if (item.length() > 0) {
                String s = item.toString().trim();
                if (s.length() > 0) fireConfigItemParsed(s);
            }
        } catch (IOException error) {
            throw new RuntimeException(error);
        }
    }

    public void removeListDataListener(ConfigItemListener l) {
        listenerList.remove(ConfigItemListener.class, l);
    }
}
