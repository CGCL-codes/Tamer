package it.unipi.miabot.data;

import it.unipi.miabot.utils.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class contains the user selectable configurations.
 * <p>
 * To add a new field there are 5 steps to follow:
 * <ol>
 * <li> Add a private field with the name.
 * <li> Add a string with the field name in the <code>field_names</code> array
 * inside the constructor.
 * <li> Add a private setter method that parse a string, check basic field
 * proprieties and assign the field.
 * <li> Add an invocation to the setter, at the end of the constructor code,
 * passing the appropriate value from <code>fields</code> mapping.
 * <li> Add a public getter method for the field.
 * </ol>
 * For example, to add a positive integer called <code>NEW_FIELD</code>:
 * <ol>
 * <li> In the class:
 * 
 * <pre>
 * private int NEW_FIELD;
 * </pre>
 * 
 * <li> In constructor:
 * 
 * <pre>
 * String[] field_names = new String[] { &quot;OLD_FIELD 1&quot;, &quot;OLD_FIELD 2&quot;, //..,
 *     &quot;OLD_FIELD n&quot;, &quot;NEW_FIELD&quot; };
 * </pre>
 * 
 * <li> In the class:
 * 
 * <pre>
 * private void setNEW_FIELD(String string) {
 *   if (string == null) {
 *     throw new MiabotConfigurationException(&quot;missing field: NEW_FIELD&quot;);
 *   }
 *   this.NEW_FIELD = Conversions.toPositiveInteger(string);
 * }
 * </pre>
 * 
 * <li> At the end of constructor:
 * 
 * <pre>
 * this.setNEW_FIELD(fields.get(&quot;NEW_FIELD&quot;));
 * </pre>
 * 
 * <li> In the class:
 * 
 * <pre>
 * public int getNEW_FIELD() {
 *   return this.NEW_FIELD;
 * }
 * </pre>
 * 
 * </ol>
 */
public final class MiabotConfiguration {

    private InetAddress HOST;

    private int PORT;

    private int STEPS_FOR_TURN_90_DEGREES;

    private int STEPS_FOR_MOVE_10_CM;

    private int MIN_ELEVATION;

    private int MAX_ELEVATION;

    private int DEFAULT_ELEVATION;

    private int MIN_GRIP;

    private int MAX_GRIP;

    private int DEFAULT_GRIP;

    private CompassHistory COMPASS_HISTORY;

    private BlobColorMap BLOB_COLORMAP;

    private boolean AUTO_WHITE_BALANCE;

    private boolean AUTO_ADJUST;

    private boolean LIGHT_FILTER;

    /**
   * Load a miabot configuraton file.
   * 
   * @param file the file to read.
   */
    public MiabotConfiguration(final File file) {
        final Map<String, String> fields = new TreeMap<String, String>();
        final String[] field_names = new String[] { "HOST", "PORT", "STEPS_FOR_TURN_90_DEGREES", "STEPS_FOR_MOVE_10_CM", "MIN_ELEVATION", "MAX_ELEVATION", "MIN_GRIP", "MAX_GRIP", "COMPASS_HISTORY", "BLOB_COLORMAP", "AUTO_WHITE_BALANCE", "AUTO_ADJUST", "LIGHT_FILTER", "DEFAULT_ELEVATION", "DEFAULT_GRIP" };
        for (final String attr : field_names) {
            fields.put(attr, null);
        }
        List<String> lines;
        try {
            lines = FileLineReader.readLinesFromFile(file.getAbsolutePath());
        } catch (final IOException e) {
            throw new MiabotConfigurationException(e);
        }
        for (final String line : lines) {
            final String tokens[] = line.split("=");
            if (tokens.length != 2) {
                throw new MiabotConfigurationException("Malformed field: " + line);
            }
            final String name = tokens[0].trim().toUpperCase();
            final String value = tokens[1].trim();
            if (fields.containsKey(name)) {
                fields.put(name, value);
            } else {
                throw new MiabotConfigurationException("unknown field " + name + "=" + value);
            }
        }
        this.setHOST(fields.get("HOST"));
        this.setPORT(fields.get("PORT"));
        this.setSTEPS_FOR_TURN_90_DEGREES(fields.get("STEPS_FOR_TURN_90_DEGREES"));
        this.setSTEPS_FOR_MOVE_10_CM(fields.get("STEPS_FOR_MOVE_10_CM"));
        this.setMIN_ELEVATION(fields.get("MIN_ELEVATION"));
        this.setMAX_ELEVATION(fields.get("MAX_ELEVATION"));
        this.setMIN_GRIP(fields.get("MIN_GRIP"));
        this.setMAX_GRIP(fields.get("MAX_GRIP"));
        this.setCOMPASS_HISTORY(fields.get("COMPASS_HISTORY"));
        this.setBLOB_COLORMAP(fields.get("BLOB_COLORMAP"));
        this.setAUTO_WHITE_BALANCE(fields.get("AUTO_WHITE_BALANCE"));
        this.setAUTO_ADJUST(fields.get("AUTO_ADJUST"));
        this.setLIGHT_FILTER(fields.get("LIGHT_FILTER"));
        this.setDEFAULT_ELEVATION(fields.get("DEFAULT_ELEVATION"));
        this.setDEFAULT_GRIP(fields.get("DEFAULT_GRIP"));
    }

    /**
   * Gets the camera auto adjust miabot configuration.
   * 
   * @return the camera auto adjust miabot configuration.
   */
    public boolean getAUTO_ADJUST() {
        return this.AUTO_ADJUST;
    }

    /**
   * Gets the camera auto white balance miabot configuration.
   * 
   * @return the camera auto white balance miabot configuration.
   */
    public boolean getAUTO_WHITE_BALANCE() {
        return this.AUTO_WHITE_BALANCE;
    }

    /**
   * Gets the camera blob color map miabot configuration.
   * 
   * @return the camera blob color map miabot configuration.
   */
    public BlobColorMap getBLOB_COLORMAP() {
        return this.BLOB_COLORMAP;
    }

    /**
   * Gets the compass history miabot configuration.
   * 
   * @return the compass history miabot configuration.
   */
    public CompassHistory getCOMPASS_HISTORY() {
        return this.COMPASS_HISTORY;
    }

    /**
   * Gets the gripper default elevation miabot configuration.
   * 
   * @return the gripper default elevation miabot configuration.
   */
    public int getDEFAULT_ELEVATION() {
        return this.DEFAULT_ELEVATION;
    }

    /**
   * Gets the gripper default grip miabot configuration.
   * 
   * @return the gripper default grip miabot configuration.
   */
    public int getDEFAULT_GRIP() {
        return this.DEFAULT_GRIP;
    }

    /**
   * Gets the ip address miabot configuration.
   * 
   * @return the ip address miabot configuration.
   */
    public InetAddress getHOST() {
        return this.HOST;
    }

    /**
   * Gets the camera light filter miabot configuration.
   * 
   * @return the camera light filter miabot configuration.
   */
    public boolean getLIGHT_FILTER() {
        return this.LIGHT_FILTER;
    }

    /**
   * Gets the gripper max elevation miabot configuration.
   * 
   * @return the gripper max elevation miabot configuration.
   */
    public int getMAX_ELEVATION() {
        return this.MAX_ELEVATION;
    }

    /**
   * Gets the gripper max grip miabot configuration.
   * 
   * @return the gripper max grip miabot configuration.
   */
    public int getMAX_GRIP() {
        return this.MAX_GRIP;
    }

    /**
   * Gets the gripper min elevation miabot configuration.
   * 
   * @return the gripper min elevation miabot configuration.
   */
    public int getMIN_ELEVATION() {
        return this.MIN_ELEVATION;
    }

    /**
   * Gets the gripper min grip miabot configuration.
   * 
   * @return the gripper min grip miabot configuration.
   */
    public int getMIN_GRIP() {
        return this.MIN_GRIP;
    }

    /**
   * Gets the ip port miabot configuration.
   * 
   * @return the ip port miabot configuration.
   */
    public int getPORT() {
        return this.PORT;
    }

    /**
   * Gets the number of steps to move for 10 cm miabot configuration.
   * 
   * @return the number of steps to move for 10 cm miabot configuration.
   */
    public int getSTEPS_FOR_MOVE_10_CM() {
        return this.STEPS_FOR_MOVE_10_CM;
    }

    /**
   * Gets the number of steps to rotate 90 degrees miabot configuration.
   * 
   * @return the number of steps to rotate 90 degrees miabot configuration.
   */
    public int getSTEPS_FOR_TURN_90_DEGREES() {
        return this.STEPS_FOR_TURN_90_DEGREES;
    }

    private void setAUTO_ADJUST(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: AUTO_ADJUST");
        }
        this.AUTO_ADJUST = ParameterUtils.toBoolean(string);
    }

    private void setAUTO_WHITE_BALANCE(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: AUTO_WHITE_BALANCE");
        }
        this.AUTO_WHITE_BALANCE = ParameterUtils.toBoolean(string);
    }

    private void setBLOB_COLORMAP(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: BLOB_COLORMAP");
        }
        this.BLOB_COLORMAP = DataFactory.createBlobColorMap(string);
    }

    private void setCOMPASS_HISTORY(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: COMPASS_HISTORY");
        }
        this.COMPASS_HISTORY = DataFactory.createCompassHistory(string);
    }

    private void setDEFAULT_ELEVATION(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: DEFAULT_ELEVATION");
        }
        this.DEFAULT_ELEVATION = ParameterUtils.toRangedInteger(string, 0, 255);
    }

    private void setDEFAULT_GRIP(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: DEFAULT_GRIP");
        }
        this.DEFAULT_GRIP = ParameterUtils.toRangedInteger(string, 0, 255);
    }

    private void setHOST(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: HOST");
        }
        this.HOST = ParameterUtils.toAddress(string);
    }

    private void setLIGHT_FILTER(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: LIGHT_FILTER");
        }
        this.LIGHT_FILTER = ParameterUtils.toBoolean(string);
    }

    private void setMAX_ELEVATION(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: MAX_ELEVATION");
        }
        this.MAX_ELEVATION = ParameterUtils.toRangedInteger(string, 0, 255);
    }

    private void setMAX_GRIP(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: MAX_GRIP");
        }
        this.MAX_GRIP = ParameterUtils.toRangedInteger(string, 0, 255);
    }

    private void setMIN_ELEVATION(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: MIN_ELEVATION");
        }
        this.MIN_ELEVATION = ParameterUtils.toRangedInteger(string, 0, 255);
    }

    private void setMIN_GRIP(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: MIN_GRIP");
        }
        this.MIN_GRIP = ParameterUtils.toRangedInteger(string, 0, 255);
    }

    private void setPORT(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: PORT");
        }
        this.PORT = ParameterUtils.toPort(string);
    }

    private void setSTEPS_FOR_MOVE_10_CM(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: STEPS_FOR_MOVE_10_CM");
        }
        this.STEPS_FOR_MOVE_10_CM = ParameterUtils.toPositiveInteger(string);
    }

    private void setSTEPS_FOR_TURN_90_DEGREES(final String string) {
        if (string == null) {
            throw new MiabotConfigurationException("missing field: STEPS_FOR_TURN_90_DEGREES");
        }
        this.STEPS_FOR_TURN_90_DEGREES = ParameterUtils.toPositiveInteger(string);
    }
}
