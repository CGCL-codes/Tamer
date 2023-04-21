package com.ourorganization.uzhrelax.infrastructure.dbfeeder;

import com.ourorganization.uzhrelax.domain.Building;
import com.ourorganization.uzhrelax.domain.Location;
import com.ourorganization.uzhrelax.domain.repository.api.LocationRepository;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>Feeds database with real data concerning <strong>locations</strong> and <strong>buildings</strong>,
 * and artificial data concerning <strong>events</strong>, <strong>images</strong>, etc.</p>
 *
 * @author Taras Matyashovsky
 */
public class DBFeeder {

    public static void main(String[] args) {
        final ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("bom.xml");
        LocationRepository locationRepository = (LocationRepository) applicationContext.getBean("locationRepository");
        Location locationDomion = new Location(22.285307, 48.601984);
        Building buildingDomion = new Building();
        buildingDomion.setName("�����");
        buildingDomion.setDescription("���������� ��� ������� ��������������� ������� �������� 60 ��. � � ��������� �� 650 ����. ������ ��������������� � ������� ��������� ����� �Dolby Digital�. �������� �������� ������� � ��������� � ����������� ��������� ������� ������� ������������ �������� �����������. ����������� ���������� ��������� ��� �������� �����, ��������, ��� ��, ������ ��������� ������������.");
        buildingDomion.setAddress("���. ���������, 40");
        buildingDomion.setPhone("(03122) 2-25-43");
        buildingDomion.setUrl("www.domion.info");
        buildingDomion.setType(Building.BuildingType.CINEMA);
        buildingDomion.setLocation(locationDomion);
        locationDomion.setBuilding(buildingDomion);
        locationRepository.add(locationDomion);
        Location locationViper = new Location(22.289385, 48.608132);
        Building buildingViper = new Building();
        buildingViper.setName("VIPER");
        buildingViper.setAddress("���. ���������, 16�");
        buildingViper.setPhone("8 (095) 466-74-86");
        buildingViper.setType(Building.BuildingType.CLUB);
        buildingViper.setLocation(locationViper);
        locationRepository.add(locationViper);
        Location locationEyla = new Location(22.288891, 48.618235);
        Building buildingEyla = new Building();
        buildingEyla.setName("����");
        buildingEyla.setAddress("!!!");
        buildingEyla.setPhone("!!!");
        buildingEyla.setType(Building.BuildingType.CLUB);
        buildingEyla.setLocation(locationEyla);
        locationRepository.add(locationEyla);
        Location locationBelami = new Location(22.292161, 48.619128);
        Building buildingBelami = new Building();
        buildingBelami.setName("Belami");
        buildingBelami.setAddress("���. �����������, 20");
        buildingBelami.setPhone("(0321) 61-71-99");
        buildingBelami.setType(Building.BuildingType.PIZZERIA);
        buildingBelami.setLocation(locationBelami);
        locationRepository.add(locationBelami);
        Location locationTwins = new Location(22.282899, 48.606142);
        Building buildingTwins = new Building();
        buildingTwins.setName("����");
        buildingTwins.setAddress("���. 8 �������, 25");
        buildingTwins.setPhone("(0321) 66-36-77");
        buildingTwins.setType(Building.BuildingType.PIZZERIA);
        buildingTwins.setLocation(locationTwins);
        locationRepository.add(locationTwins);
        Location locationAvangard = new Location(22.278507, 48.624041);
        locationRepository.add(locationAvangard);
        Location locationCastle = new Location(22.305806, 48.620968);
        locationRepository.add(locationCastle);
        Location locationCathedral = new Location(22.302087, 48.622713);
        locationRepository.add(locationCathedral);
    }
}
