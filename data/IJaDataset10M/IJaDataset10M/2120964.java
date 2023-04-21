package collections.googleCollections;

import java.util.Map;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * Author: Sergiy Doroshenko
 * Date: Feb 22, 2010
 * Time: 4:15:49 PM
 */
public class FilterMap {

    public static void main(String[] args) {
        ImmutableMap<Integer, String> map = new ImmutableMap.Builder<Integer, String>().put(10, "Ten").put(20, "Twenty").put(30, "Thirty").build();
        Map<Integer, String> filtered = Maps.filterKeys(map, Predicates.or(Predicates.equalTo(10), Predicates.equalTo(30)));
        System.out.println(filtered);
    }
}
