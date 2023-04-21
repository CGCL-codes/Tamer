package de.capacis.jzeemap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import org.apache.log4j.Logger;

public class SceneLoader {

    private static final Logger logger = Logger.getLogger(SceneLoader.class);

    public Scene load(final File file) throws IOException {
        logger.info("loading scene from " + file);
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            final Scene scene = new Scene();
            final Calendar cal = Calendar.getInstance();
            cal.clear();
            String line = null;
            while ((line = in.readLine()) != null) {
                logger.debug("reading " + line);
                final String[] col = line.split("\\s+");
                final double x = Double.parseDouble(col[0]);
                final double y = Double.parseDouble(col[1]);
                final double mag = Double.parseDouble(col[5]);
                final double z = Double.parseDouble(col[6]);
                cal.set(Calendar.YEAR, Integer.parseInt(col[2]));
                cal.set(Calendar.MONTH, Integer.parseInt(col[3]) - 1);
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(col[4]));
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(col[7]));
                cal.set(Calendar.MINUTE, Integer.parseInt(col[8]));
                final SceneObject sceneObject = new SceneObject(x, y, z, new Timestamp(cal.getTimeInMillis()), mag);
                scene.addSceneObject(sceneObject);
            }
            logger.info("loading finished successfully");
            return scene;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
