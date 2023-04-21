package singlton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import cummingsm.interfaces.Item;
import cummingsm.interfaces.LivableLocation;
import cummingsm.items.Food;
import cummingsm.items.HealthPack;

public class RoomSearcher {

    private static RoomSearcher _lazyHolder = null;

    private static Object _lazyObject = new Object();

    private List<Class> _classItemList = new ArrayList<Class>();

    public void add(Class itemClass) {
        _classItemList.add(itemClass);
    }

    private HashMap<LivableLocation, Double> _searchingValues;

    private RoomSearcher() {
        _searchingValues = new HashMap<LivableLocation, Double>();
        _classItemList.add(Food.class);
        _classItemList.add(HealthPack.class);
    }

    public static RoomSearcher getInstance() {
        if (_lazyHolder == null) {
            synchronized (_lazyObject) {
                if (_lazyHolder == null) {
                    _lazyHolder = new RoomSearcher();
                }
            }
        }
        return _lazyHolder;
    }

    public Item searchRoom(LivableLocation ll) {
        Double randomNumber = getPercentageAndReduce(ll);
        boolean itemFound = false;
        Random generator = new Random();
        Double randomNum = generator.nextDouble();
        if (randomNum < (randomNumber * 100)) {
            itemFound = true;
        }
        if (itemFound) {
            return getRandomItem();
        }
        return null;
    }

    private Item getRandomItem() {
        int upperBound = _classItemList.size();
        Random generator = new Random();
        int randomNum = generator.nextInt(upperBound - 1);
        Class itemClass = _classItemList.get(randomNum);
        Item toReturn = null;
        try {
            toReturn = (Item) itemClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    private Double getPercentageAndReduce(LivableLocation ll) {
        if (!_searchingValues.containsKey(ll)) {
            _searchingValues.put(ll, 100.0);
        }
        Double percentage = _searchingValues.get(ll);
        _searchingValues.put(ll, percentage - .5);
        return percentage;
    }
}
