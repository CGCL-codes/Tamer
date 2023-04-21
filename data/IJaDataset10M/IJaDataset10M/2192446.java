package net.narusas.daumaccess;

import java.io.IOException;

public class Model {

    public Accounts accounts;

    public ProxyPool proxyPool;

    public Tasks tasks;

    public Prefs pref;

    public void setup(String path) throws IOException {
        if (path.endsWith("\\") == false) {
            path = path + "\\";
        }
        accounts = new Accounts(path);
        proxyPool = new ProxyPool();
        proxyPool.start();
        tasks = new Tasks(path + "tasks.txt");
        pref = new Prefs();
    }
}
