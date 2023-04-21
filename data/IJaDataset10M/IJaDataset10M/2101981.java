package ch.epfl.lbd.io.writers.test;

import java.io.File;
import org.junit.Assert;
import com.vividsolutions.jts.geom.Geometry;
import ch.epfl.lbd.benchmark.Tester;
import ch.epfl.lbd.io.readers.ShapeFileReader;
import ch.epfl.lbd.io.writers.ShapeFileWriter;

public class TestShpFileWriter extends Tester {

    @Override
    public void run() throws Exception {
        super.run();
        ShapeFileWriter writer = null;
        try {
            writer = new ShapeFileWriter("src/assets/shp/region_out");
        } catch (Exception e) {
            e.printStackTrace();
        }
        writer.open();
        ShapeFileReader reader = null;
        try {
            reader = new ShapeFileReader(new File("src/assets/shp/region.shp"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        reader.open();
        Geometry[] geometries = reader.readAll();
        Assert.assertEquals(3, geometries.length);
        writer.write(geometries);
        reader.close();
        writer.close();
    }
}
