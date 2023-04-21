package kursach2;

import java.util.HashMap;
import javax.xml.xpath.XPathExpressionException;

/**
 * ������� ������. ���� ������ � ��������� ���, ��������� ��� �� XML-����� �
 * ��������� � ���������.
 * 
 * @author Vsevolod
 * 
 */
public class BuildingFactory {

    private HashMap<String, TBuilding> MapBuildings = new HashMap<String, TBuilding>();

    public TBuilding getBuilding(String Name) throws XPathExpressionException {
        TBuilding rez;
        if (MapBuildings.containsKey(Name)) {
            rez = MapBuildings.get(Name);
        } else {
            rez = new TBuilding(Name);
            MapBuildings.put(Name, rez);
        }
        return rez;
    }
}
