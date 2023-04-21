package org.fao.fenix.web.client.util.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.google.gwt.user.client.ui.ListBox;

public class CategoryList {

    private Map categoryMap;

    public CategoryList() {
        categoryMap = new HashMap();
        initializeCategoryMap();
    }

    private Map getCategoryList() {
        return categoryMap;
    }

    private void setCategoryList(Map categoryMap) {
        this.categoryMap = categoryMap;
    }

    public String getCategoryLabel(String key) {
        String label = (String) categoryMap.get(key);
        return label;
    }

    private void initializeCategoryMap() {
        categoryMap.put("020", "Food Availability");
        categoryMap.put("021", "Food Access");
        categoryMap.put("022", "Food Utilization");
        categoryMap.put("023", "Early Warning");
        categoryMap.put("001", "Agriculture and Farming");
        categoryMap.put("002", "Biologic and Ecologic Information");
        categoryMap.put("003", "Administrative and Political Boundaries");
        categoryMap.put("004", "Atmospheric and Climatic Data");
        categoryMap.put("005", "Business and Economic Information");
        categoryMap.put("006", "Elevation and Derived Products");
        categoryMap.put("007", "Environmental Monitoring and Modelling");
        categoryMap.put("008", "Geologic and Geophysical Information");
        categoryMap.put("009", "Human Health and Disease");
        categoryMap.put("010", "Imagery and Aerial Photographs");
        categoryMap.put("011", "Military Bases, Structures and Activities");
        categoryMap.put("012", "Inland Water Resources and Characteristics");
        categoryMap.put("013", "Geodetic Networks and Control Points");
        categoryMap.put("014", "Ocean and Estuarine Resources and Characteristics");
        categoryMap.put("015", "Cadastral and Legal Land Descriptions");
        categoryMap.put("016", "Society, Cultural and Demographic Information");
        categoryMap.put("017", "Facilities, Buildings and Structures");
        categoryMap.put("018", "Transportation Networks and Models");
        categoryMap.put("018", "Utility Distribution Networks");
        categoryMap.put("", "");
        setCategoryList(categoryMap);
    }

    public ListBox getCategoriesListBox() {
        ListBox list = new ListBox();
        Map map = getCategoryList();
        list.setWidth("95%");
        list.setName("categories");
        list.addItem("", "");
        Iterator keyValuePairs = map.entrySet().iterator();
        for (int i = 0; i < map.size(); i++) {
            Map.Entry entry = (Entry) keyValuePairs.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            list.addItem(value, key);
        }
        return list;
    }
}
