package net.sf.hattori.populators.domain;

import net.sf.hattori.PopulationTestCase;
import net.sf.hattori.example.graph.building.Building;
import net.sf.hattori.example.graph.building.BuildingDTO;
import net.sf.hattori.example.graph.building.Floor;
import net.sf.hattori.example.graph.building.FloorsDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class DomainObjectCollectionFieldPopulatorTest extends PopulationTestCase {

    public void testAdditionToCollectonWithPriorElements() throws NoSuchFieldException {
        DomainObjectCollectionFieldPopulator populator = new DomainObjectCollectionFieldPopulator();
        Building building = new Building(1L, 5L);
        Floor floor = building.createFloor(1L, 1L);
        floor.setNumber(1L);
        this.getLocator().addToRepository(floor);
        floor = building.createFloor(2L, 2L);
        floor.setNumber(2L);
        this.getLocator().addToRepository(floor);
        this.getLocator().addToRepository(building);
        BuildingDTO buildingDTO = (BuildingDTO) this.getPopulator().populateDTO(building, BuildingDTO.class);
        floor = new Floor(null, 3L, 5L);
        FloorsDTO floorDTO = (FloorsDTO) this.getPopulator().populateDTO(floor, FloorsDTO.class);
        floorDTO.setNumber(55L);
        buildingDTO.getFloorsDTO().add(floorDTO);
        populator.populateField(buildingDTO.getClass().getDeclaredField("floorsDTO"), buildingDTO, building);
        assertTrue(building.getFloors().size() == 3);
        assertTrue(CollectionUtils.countMatches(building.getFloors(), new Predicate() {

            public boolean evaluate(Object arg0) {
                if (((Floor) arg0).getNumber().equals(55L)) return true;
                return false;
            }
        }) > 0);
    }
}
