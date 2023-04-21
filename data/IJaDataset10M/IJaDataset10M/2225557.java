package tristero.chord;

import java.io.*;
import java.net.*;
import java.util.*;
import java.math.*;
import discordia.*;
import tristero.*;
import tristero.util.*;
import tristero.lookup.*;
import tristero.search.*;
import tristero.ntriple.*;

public class Chord extends Thread implements Add {

    ChordList list;

    ChordLookup lookup;

    public void init() {
        System.out.println("Chord.init()");
        try {
            list = new ChordList(Config.address);
            Config.refStore = list;
            lookup = new ChordLookup(list);
            Config.addHandler("ChordLookup", lookup);
            Config.rdfStore.addAddListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                list.update();
                sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int add(String db, List statement) {
        try {
            List l = (List) statement.get(0);
            String s = (String) l.get(1);
            String p = (String) l.get(1);
            l = (List) statement.get(2);
            String o = (String) l.get(1);
            if (!s.equals(Config.address)) {
                System.out.println("wrong address");
                return 0;
            }
            URI uri = new URI(o);
            o = uri.getSchemeSpecificPart();
            List hosts = lookup.lookup(o);
            String host = (String) hosts.get(0);
            System.out.println("Announcing " + statement + " to " + host);
            if (host.equals(Config.address)) return 0;
            Add add = (Add) XmlRpcProxy.newInstance(Add.class, host + "#search");
            add.add("file:files", statement);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int addAll(String db, List statements) {
        return 0;
    }
}
