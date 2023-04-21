package VoltIJ.tools.isosurface;

/**
 *
 * @author jc
 */
public class IsoSurfFactory {

    public static IsoSurfProcessor getSurfProcessor(int algorithm) {
        switch(algorithm) {
            case 1:
                return new BasicSurfProcessor();
            case 2:
                return new Surf3DProcessor();
            default:
                return new SurfProcessor();
        }
    }
}
