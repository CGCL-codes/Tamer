package city.model.cities;

import java.util.Random;
import city.model.architectures.ArchitectureRandomiser;
import city.model.architectures.roads.NormalRoad;
import city.model.architectures.roads.Road;
import city.model.architectures.roads.Road.Direction;
import city.model.citygrid.CityCell;
import city.model.citygrid.CityGrid;

/**
 * Plan First pass: building roads (and other transport infrastructure) Seconds
 * pass: buildings
 * 
 * @author Peter
 * 
 */
public class RandomCityModel extends CityModel {

    public RandomCityModel(int width, int height) throws Exception {
        super(width, height);
        CityGrid cityGrid = getCityGrid();
        Random random = new Random();
        int noOfVerticalMajorRoads = random.nextInt(Math.round(width / 10f)) + 1;
        System.out.println("Number of vertical major roads: " + noOfVerticalMajorRoads);
        for (int r = 0; r < noOfVerticalMajorRoads; r++) {
            int x = random.nextInt(width);
            int y = 0;
            Direction direction = null;
            Direction prevDirection = null;
            Road road = null;
            while (y < height) {
                road = new NormalRoad();
                try {
                    cityGrid.getCell(x, y).setArchitecture(road);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    if (e.getMessage() == null) e.printStackTrace();
                }
                switch(random.nextInt(4)) {
                    case 0:
                    case 1:
                        direction = Direction.SOUTH;
                        break;
                    case 2:
                        direction = Direction.EAST;
                        break;
                    case 3:
                        direction = Direction.WEST;
                        break;
                }
                if (prevDirection == Direction.WEST && direction == Direction.EAST) direction = Direction.WEST;
                if (prevDirection == Direction.EAST && direction == Direction.WEST) direction = Direction.EAST;
                if ((direction == Direction.EAST && x + 1 >= width - 1) || (direction == Direction.WEST && x - 1 <= 0)) direction = Direction.SOUTH;
                switch(direction) {
                    case SOUTH:
                        y++;
                        break;
                    case EAST:
                        x++;
                        break;
                    case WEST:
                        x--;
                        break;
                }
                prevDirection = direction;
            }
        }
        ArchitectureRandomiser randArch = new ArchitectureRandomiser();
        cityGrid.resetIterator();
        for (CityCell cell : cityGrid) {
            if (!cell.isOccupied()) try {
                cell.setArchitecture(randArch.nextArchitecture());
            } catch (Exception e) {
                System.err.println(e.getMessage());
                if (e.getMessage() == null) e.printStackTrace();
            }
        }
    }
}
