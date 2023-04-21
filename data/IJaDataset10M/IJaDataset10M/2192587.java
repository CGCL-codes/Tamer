package playground.dsantani.parking;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import org.matsim.api.core.v01.Coord;
import org.matsim.core.utils.geometry.CoordImpl;
import playground.dsantani.utils.CoordUtils;

public abstract class ParkingDataFactory {

    public ParkingDataFactory() {
    }

    public abstract ParkingData createParkingData(String inputLine) throws ParseException;

    public abstract void createParkingData(File inputFile) throws IOException, ParseException;

    public abstract List<? extends ParkingData> getParkingData();

    public abstract ParkingData getParkingData(int parkingId);

    public List<? extends ParkingData> getParkingDataInWGS84() {
        List<? extends ParkingData> parkingDataInWGS84 = this.getParkingData();
        for (ParkingData parkingData : parkingDataInWGS84) {
            Coord coord = CoordUtils.convertToWGS84(new CoordImpl(parkingData.getX(), parkingData.getY()));
            parkingData.setX(coord.getX());
            parkingData.setY(coord.getY());
        }
        return parkingDataInWGS84;
    }
}
