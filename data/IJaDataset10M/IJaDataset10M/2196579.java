package geovista.readers.example;

import geovista.common.data.DataSetForApps;
import geovista.readers.shapefile.ShapeFileDataReader;

/**
 * Reads shapefiles from included resources
 * 
 * Object[0] = names of variables 0bject[1] = data (double[], int[], or
 * String[]) 0bject[1] = data (double[], int[], or String[]) ... Object[n-1] =
 * the shapefile data
 * 
 * also see DBaseFile, ShapeFile
 * 
 */
public class GeoDataUSCounties extends GeoDataClassResource {

    @Override
    protected DataSetForApps makeDataSetForApps() {
        return ShapeFileDataReader.makeDataSetForApps(this.getClass(), "USCounties");
    }
}
