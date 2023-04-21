package com.shithead.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.shithead.bo.score.PlayerStatistics;

public class SHUtil {

    public static boolean IsEmpty(String string) {
        return string == null || string.trim().length() < 1;
    }

    public static <T> List<T> CloneList(Collection<T> list) {
        List<T> clone = new ArrayList<T>();
        clone.addAll(list);
        return clone;
    }

    public static String GetOrdinal(int value) {
        if (value == 1) {
            return "1st";
        } else if (value == 2) {
            return "2nd";
        } else if (value == 3) {
            return "3rd";
        } else {
            return value + "th";
        }
    }

    public static <KEY, VALUE> void RemoveListFromMap(List<VALUE> cards, Map<KEY, VALUE> map) {
        for (Iterator<VALUE> iterator = cards.iterator(); iterator.hasNext(); ) {
            VALUE card = iterator.next();
            for (Map.Entry<KEY, VALUE> entry : map.entrySet()) {
                if (entry.getValue() == card) {
                    map.remove(entry.getKey());
                    iterator.remove();
                }
            }
        }
    }

    public static <T> void RemoveListFromList(List<T> cards, List<T> list) {
        for (Iterator<T> iterator = cards.iterator(); iterator.hasNext(); ) {
            T card = iterator.next();
            if (list.contains(card)) {
                list.remove(card);
                iterator.remove();
            }
        }
    }

    public static List<PlayerStatistics> OrderStatsByRanking(Collection<PlayerStatistics> stats) {
        List<PlayerStatistics> statList = new ArrayList<PlayerStatistics>();
        statList.addAll(stats);
        Collections.sort(statList, new Comparator<PlayerStatistics>() {

            @Override
            public int compare(PlayerStatistics one, PlayerStatistics two) {
                return one.getAccumulatedScore() - two.getAccumulatedScore();
            }
        });
        return statList;
    }

    public static <T> List<T> GetList(T... cards) {
        List<T> list = new ArrayList<T>();
        for (T card : cards) {
            list.add(card);
        }
        return list;
    }
}
