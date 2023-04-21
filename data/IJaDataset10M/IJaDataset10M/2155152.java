package net.krecan.spring.osgi.dao;

import java.util.Date;
import net.krecan.spring.osgi.Data;
import net.krecan.spring.osgi.DataLoader;

public class DefaultDataLoader implements DataLoader {

    public Data loadData() {
        return new Data("Hallo " + new Date());
    }
}
