package ao.droid;

import java.util.Collections;
import java.util.Vector;

public class ChannelList {

    private Vector<Channel> channels = new Vector<Channel>();

    public void add(Channel c) {
        channels.add(c);
    }

    public void rem(String name) {
        channels.remove(find(name));
    }

    public void rem(byte[] b) {
        channels.remove(find(b));
    }

    public void clear() {
        channels.clear();
    }

    public Channel get(int i) {
        return channels.get(i);
    }

    public Channel get(String name) {
        if (find(name) != -1) {
            return channels.get(find(name));
        } else {
            return null;
        }
    }

    public Channel get(byte[] b) {
        if (find(b) != -1) {
            return channels.get(find(b));
        } else {
            return null;
        }
    }

    public int find(String name) {
        for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).getName().compareTo(name) == 0) return i;
        }
        return -1;
    }

    public int find(byte[] b) {
        for (int i = 0; i < channels.size(); i++) {
            if (compare(channels.get(i).getID(), b)) return i;
        }
        return -1;
    }

    public boolean contains(Channel c) {
        return find(c.getID()) != -1;
    }

    public boolean contains(byte[] b) {
        return find(b) != -1;
    }

    public boolean contains(String name) {
        return find(name) != -1;
    }

    private boolean compare(byte[] b1, byte[] b2) {
        if (b1.length != b2.length) return false;
        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i]) return false;
        }
        return true;
    }

    public void remove(int i) {
        channels.remove(i);
    }

    public int size() {
        return channels.size();
    }

    public void sort() {
        Collections.sort(channels);
    }
}
