package org.tigr.microarray.mev.file;

import java.util.Vector;
import org.tigr.microarray.file.MevFileParser;
import org.tigr.microarray.mev.SpotInformationData;

public class MevParser extends MevFileParser {

    /** Creates new MeVLoader */
    public MevParser() {
        super();
    }

    public SpotInformationData getSpotInformation() {
        if (this.isMevFileLoaded()) {
            String[][] data = this.getDataMatrix();
            Vector headers = this.getColumnHeaders();
            if (headers.size() < 9) return null;
            int numberOfFields = headers.size() - 9;
            String[][] spotData = new String[data.length][numberOfFields];
            for (int i = 0; i < spotData.length; i++) {
                for (int j = 0; j < spotData[i].length; j++) {
                    spotData[i][j] = data[i][j + 9];
                }
            }
            String[] headerStrings = new String[headers.size() - 9];
            for (int i = 0; i < headerStrings.length; i++) {
                headerStrings[i] = (String) (headers.elementAt(i + 9));
            }
            return new SpotInformationData(headerStrings, spotData);
        } else return null;
    }
}
