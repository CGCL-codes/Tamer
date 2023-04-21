package sto.orz.test.beans;

import org.apache.commons.lang.RandomStringUtils;
import sto.orz.commons.*;
import sto.orz.rptp.model.*;

public class BeanFactory {

    public static Datasource getSampleDatasource() {
        Datasource datasource = new Datasource();
        datasource.setId(DbUtils.getUuid());
        datasource.setDriver("HSQL Server");
        datasource.setName("TestDatasource");
        datasource.setUrl("jdbc:hsqldb:hsql://localhost/rptp");
        datasource.setUsername("sa");
        return datasource;
    }

    public static Dataset getSampleDataset() {
        Dataset ds = new Dataset();
        ds.setId(DbUtils.getUuid());
        ds.setName("dataset for test");
        ds.setQuery("select * from table");
        ds.setDatasource(getSampleDatasource());
        return ds;
    }
}
