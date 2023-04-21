package com.cinformatique.web.delicious;

import java.io.Serializable;
import java.util.Properties;

public class ListMountPagesImpl implements ListMountPages, Serializable {

    private static Properties props;

    public Properties getAllListMountPages() {
        return props;
    }

    public void setAllListMountPages(Properties props) {
        this.props = props;
    }
}
