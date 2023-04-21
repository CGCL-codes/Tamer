package objects.production;

/**
 * Author: serhiy
 * Created on Sep 4, 2006, 11:52:32 AM
 */
public class LinearResearch implements IResearch {

    @Override
    public double research(double level, double effort) {
        return level + effort;
    }
}
