package geovista.jts.io;

import geovista.jts.feature.FeatureCollection;

/**
 * JMLWriter is a {@link JUMPWriter} specialized to write JML.
 *
 * <p>
 * This is a simple class that passes the work off to the {@link GMLWriter} class that
 * knows how to auto-generate a JML compatible {@link GMLOutputTemplate}.
 * </p>
 *
 * <p>
 * DataProperties for the JMLWriter write(DataProperties) interface:<br><br>
 * </p>
 *
 * <p>
 * <table border='1' cellspacing='0' cellpadding='4'>
 *   <tr>
 *     <th>Parameter</th>
 *     <th>Meaning</th>
 *   </tr>
 *   <tr>
 *     <td>OutputFile or DefaultValue</td>
 *     <td>File name for the output JML file</td>
 *   </tr>
 * </table><br>
 * </p>
 */
public class JMLWriter implements JUMPWriter {

    /** Creates new JMLWriter */
    public JMLWriter() {
    }

    /**
     *  Writes the feature collection to the specified file in JML format.
     * @param featureCollection features to write
     * @param dp 'OutputFile' or 'DefaultValue' to specify what file to write.
     */
    public void write(FeatureCollection featureCollection, DriverProperties dp) throws IllegalParametersException, Exception {
        GMLWriter gmlWriter;
        String outputFname;
        outputFname = dp.getProperty("File");
        if (outputFname == null) {
            outputFname = dp.getProperty("DefaultValue");
        }
        if (outputFname == null) {
            throw new IllegalParametersException("call to JMLWriter.write() has DataProperties w/o a OutputFile specified");
        }
        gmlWriter = new GMLWriter();
        gmlWriter.write(featureCollection, dp);
    }
}
