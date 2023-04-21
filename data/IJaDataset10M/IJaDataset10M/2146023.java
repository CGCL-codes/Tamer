package playground.scnadine.gpsPreprocess;

import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings("serial")
public class GPSFlammFileComparator implements Comparator<GPSFlammDataset>, Serializable {

    public int compare(GPSFlammDataset dataset1, GPSFlammDataset dataset2) {
        return (dataset1.getPersonID() - dataset2.getPersonID()) * 100000000 + dataset1.getStartDate() - dataset2.getStartDate();
    }
}
