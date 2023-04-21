package objects.production;

import objects.Race;
import java.util.Properties;

/**
 * Author: serhiy
 * Created on Mar 9, 2007, 5:44:13 AM
 */
public interface IProductionFabric {

    void init(int i, Properties props, String name);

    IProductionFabric clone(Race race);

    boolean isMyName(String name);

    IProduction create(String name);

    int getCode();

    IProduction createByCode(int code);

    String getType();
}
