package de.mpicbg.buchholz.phenofam.server.stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import de.mpicbg.buchholz.phenofam.client.InputDataEntity;

public class RankedInputDataEntity {

    private InputDataEntity entity;

    private double rank;

    protected RankedInputDataEntity(InputDataEntity entity, double rank) {
        this.entity = entity;
        this.rank = rank;
    }

    public InputDataEntity getEntity() {
        return entity;
    }

    public double getRank() {
        return rank;
    }

    public static List<RankedInputDataEntity> createRankedInputDataEntityList(List<InputDataEntity> data) {
        Collections.sort(data, new Comparator<InputDataEntity>() {

            public int compare(InputDataEntity o1, InputDataEntity o2) {
                return (int) Math.signum(o1.getValue() - o2.getValue());
            }
        });
        List<RankedInputDataEntity> ranked = new ArrayList<RankedInputDataEntity>(data.size());
        double r = 1;
        for (InputDataEntity e : data) {
            ranked.add(new RankedInputDataEntity(e, r));
            r++;
        }
        fixRankedInputDataEntities(ranked);
        return ranked;
    }

    private static void fixRankedInputDataEntities(List<RankedInputDataEntity> rankedInputDataEntities) {
        Iterator<RankedInputDataEntity> iter = rankedInputDataEntities.iterator();
        List<RankedInputDataEntity> toFix = new LinkedList<RankedInputDataEntity>();
        RankedInputDataEntity previous = iter.next();
        while (iter.hasNext()) {
            RankedInputDataEntity pair = iter.next();
            if (pair.entity.getValue().equals(previous.entity.getValue())) {
                if (toFix.isEmpty()) toFix.add(previous);
                toFix.add(pair);
            } else if (!toFix.isEmpty()) {
                averageRankedInputDataEntities(toFix);
                toFix.clear();
            }
            previous = pair;
        }
        if (!toFix.isEmpty()) averageRankedInputDataEntities(toFix);
    }

    private static void averageRankedInputDataEntities(List<RankedInputDataEntity> rankedInputDataEntities) {
        double rank = rankedInputDataEntities.get(0).rank + ((double) rankedInputDataEntities.size() - 1.0) / 2;
        for (RankedInputDataEntity p : rankedInputDataEntities) p.rank = rank;
    }
}
