package net.kelissa.divizor.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.kelissa.divizor.model.CellModel;
import net.kelissa.divizor.model.ClassWeight;
import net.kelissa.divizor.model.DataModel;
import net.kelissa.divizor.model.DivizorModel;
import net.kelissa.divizor.model.StatCell;
import net.kelissa.divizor.model.StatDivizor;
import net.kelissa.divizor.model.CellModel.Clazz;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class MathUtil {

    protected static Random random;

    public static Random getRandom() {
        if (random == null) random = new Random();
        return random;
    }

    public static double getDefaultRandom() {
        Random random = getRandom();
        return (random.nextDouble() * 2) - 1;
    }

    public static double getX2Random() {
        double nextDouble = getRandom().nextDouble();
        return nextDouble * nextDouble;
    }

    public static double getX2DefaultRandom() {
        double nextDouble = getRandom().nextDouble();
        double sign = getRandom().nextDouble();
        if (sign < 0.5) return -1 * nextDouble * nextDouble;
        return nextDouble * nextDouble;
    }

    public static double getX3Random() {
        double nextDouble = getRandom().nextDouble();
        return nextDouble * nextDouble * nextDouble;
    }

    public static double[] getRandVersor(int dimensions) {
        return getRandVector(dimensions, 1.);
    }

    public static double[] getRandVector(int dimensions, double norm) {
        Random random = getRandom();
        double[] v = new double[dimensions];
        for (int i = 0; i < v.length; i++) {
            v[i] = (random.nextDouble() * 2) - 1;
        }
        normalize(v, norm);
        return v;
    }

    public static double[] getRandStdDev(List<DataModel> dataList) {
        int length = dataList.get(0).getPosition().length;
        InnerVectorStatistics stat = new InnerVectorStatistics(length);
        for (DataModel dataModel : dataList) {
            stat.add(dataModel.getPosition());
        }
        double[] mean = stat.getMean();
        double[] stdDev = stat.getStdDev();
        for (int i = 0; i < length; i++) {
            stdDev[i] = 2 * stdDev[i] * getDefaultRandom();
        }
        return sum(mean, stdDev);
    }

    public static double scalarProduct(double[] v1, double[] v2) {
        double res = 0d;
        for (int i = 0; i < v1.length; i++) {
            res += v1[i] * v2[i];
        }
        return res;
    }

    public static double[] diff(double[] v1, double[] v2) {
        double[] res = new double[v1.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = v1[i] - v2[i];
        }
        return res;
    }

    public static double[] sum(double[] v1, double[] v2) {
        double[] res = new double[v1.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = v1[i] + v2[i];
        }
        return res;
    }

    public static double distance(double[] v1, double[] v2) {
        double[] distV = diff(v1, v2);
        return norm(distV);
    }

    public static double norm(double[] v) {
        double res = 0.;
        for (int i = 0; i < v.length; i++) {
            res += v[i] * v[i];
        }
        return Math.sqrt(res);
    }

    public static void normalize(double[] v) {
        normalize(v, 1.);
    }

    public static void normalize(double[] v, double newNorm) {
        double norm = norm(v);
        for (int i = 0; i < v.length; i++) {
            v[i] = v[i] * newNorm / norm;
        }
    }

    public static int neighbor(Map<Integer, CellModel> cells, DataModel data) {
        return neighbor(cells, data.getPosition());
    }

    public static int neighbor(Map<Integer, CellModel> cells, double[] v) {
        Integer res = null;
        double bestDist = 0d;
        double tmpDist = 0d;
        for (Entry<Integer, CellModel> entry : cells.entrySet()) {
            if (res == null) {
                res = entry.getKey();
                bestDist = distance(entry.getValue().getPosition(), v);
            }
            tmpDist = distance(entry.getValue().getPosition(), v);
            if (tmpDist < bestDist) {
                res = entry.getKey();
                bestDist = tmpDist;
            }
        }
        if (res == null) {
            System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            System.out.println("cells" + cells.size());
            System.out.println("cells" + v);
        }
        return res;
    }

    public static Clazz calcClass(CellModel cell, DataModel data) {
        return calcClass(cell, data.getPosition());
    }

    public static double calcComponent(double[] direction, double[] v) {
        double[] versor = direction.clone();
        normalize(versor);
        return scalarProduct(v, versor);
    }

    public static Clazz calcClass(CellModel cell, double[] v) {
        double[] position = diff(v, cell.getPosition());
        double[] divizor = cell.getDivizor();
        double component = calcComponent(divizor, position);
        double[] versor = divizor.clone();
        normalize(versor, component);
        double[] perp = diff(position, versor);
        ;
        double norm = norm(perp);
        if ((component < (cell.getCurvature() * norm * norm))) {
            return CellModel.Clazz.DOWN;
        } else {
            return CellModel.Clazz.UP;
        }
    }

    public static void recalcClass(Map<Integer, CellModel> cells, List<DataModel> dataList, ClassWeight weigths) {
        Map<Integer, List<DataModel>> groups = calcGroups(cells, dataList);
        for (Entry<Integer, CellModel> entry : cells.entrySet()) {
            CellModel value = entry.getValue();
            int id = value.getId();
            List<DataModel> list = groups.get(id);
            if (list.size() > 0) recalcClass(value, list, weigths);
        }
    }

    public static Map<Integer, List<DataModel>> calcGroups(DivizorModel divizor) {
        return calcGroups(divizor.getCells(), divizor.getData());
    }

    public static Map<Integer, List<DataModel>> calcGroups(Map<Integer, CellModel> cells, List<DataModel> dataList) {
        Map<Integer, List<DataModel>> groups = new HashMap<Integer, List<DataModel>>();
        for (Entry<Integer, CellModel> entry : cells.entrySet()) {
            groups.put(entry.getKey(), new ArrayList<DataModel>());
        }
        for (DataModel dataModel : dataList) {
            int neighbor = neighbor(cells, dataModel);
            groups.get(neighbor).add(dataModel);
        }
        return groups;
    }

    public static void recalcClass(CellModel cell, List<DataModel> list, ClassWeight weigths) {
        String clazzUp = null;
        String clazzDown = null;
        double tmp = 0d;
        Map<String, Double> mapUp = new HashMap<String, Double>();
        Map<String, Double> mapDown = new HashMap<String, Double>();
        for (DataModel dataModel : list) {
            String clazzLabel = dataModel.getClazzLabel();
            Clazz clazz = calcClass(cell, dataModel);
            if (clazz.equals(CellModel.Clazz.DOWN)) {
                tmp = mapDown.containsKey(clazzLabel) ? mapDown.get(clazzLabel) : 0d;
                mapDown.put(clazzLabel, tmp + weigths.getWeigth(clazzLabel));
            } else {
                tmp = mapUp.containsKey(clazzLabel) ? mapUp.get(clazzLabel) : 0d;
                mapUp.put(clazzLabel, tmp + weigths.getWeigth(clazzLabel));
            }
        }
        clazzUp = calcBest(mapUp);
        clazzDown = calcBest(mapDown);
        if (clazzUp == null) clazzUp = clazzDown;
        if (clazzDown == null) clazzDown = clazzUp;
        if (clazzUp == null && clazzDown == null) {
            clazzUp = Const.POSITIVE;
            clazzDown = Const.POSITIVE;
        }
        cell.setClassLabelDown(clazzDown);
        cell.setClassLabelUp(clazzUp);
    }

    private static String calcBest(Map<String, Double> map) {
        String clazz = null;
        double best = 0d;
        for (Entry<String, Double> entry : map.entrySet()) {
            if (clazz == null) {
                clazz = entry.getKey();
                best = entry.getValue();
            }
            if (entry.getValue() > best) {
                clazz = entry.getKey();
                best = entry.getValue();
            }
        }
        return clazz;
    }

    public static StatDivizor calcStat(DivizorModel divizor) {
        return calcStat(divizor.getCells(), divizor.getData());
    }

    public static StatDivizor calcStat(Map<Integer, CellModel> cells, List<DataModel> dataList) {
        StatDivizor stat = new StatDivizor();
        Map<Integer, List<DataModel>> groups = calcGroups(cells, dataList);
        for (Entry<Integer, CellModel> entry : cells.entrySet()) {
            CellModel cell = entry.getValue();
            int id = cell.getId();
            List<DataModel> list = groups.get(id);
            StatCell statCell = calcStat(cell, list, new ClassWeight());
            stat.addStat(statCell);
        }
        return stat;
    }

    public static StatCell calcStat(CellModel cell, List<DataModel> dataList, ClassWeight classWeight) {
        StatCell res = new StatCell();
        res.setId(cell.getId());
        int count = 0;
        int countUp = 0;
        int countDown = 0;
        double fitness = 0.;
        double total = 0.;
        for (DataModel dataModel : dataList) {
            Clazz clazz = calcClass(cell, dataModel);
            if (clazz.equals(CellModel.Clazz.UP)) countUp += 1;
            if (clazz.equals(CellModel.Clazz.DOWN)) countDown += 1;
            count = countUp < countDown ? countUp : countDown;
            if (cell.getClazzLabel(clazz).equals(dataModel.getClazzLabel())) fitness += classWeight.getWeigth(dataModel.getClazzLabel());
            total += classWeight.getWeigth(dataModel.getClazzLabel());
        }
        double capacity = sigmoid(dataList.size());
        res.setCapacity(capacity);
        if (total > 0) res.setFitness(fitness / total); else res.setFitness(0.);
        if (cell.isDivided()) {
            res.setBalance(sigmoid(count));
        } else {
            res.setBalance(capacity);
        }
        return res;
    }

    private static double sigmoid(int count) {
        double res = 0.;
        switch(count) {
            case 0:
                res = 0.0;
                break;
            case 1:
                res = 0.1;
                break;
            case 2:
                res = 0.2;
                break;
            case 3:
                res = 0.7;
                break;
            case 4:
                res = 0.8;
                break;
            default:
                res = realSigmoid(count);
                break;
        }
        return res;
    }

    private static double realSigmoid(int count) {
        double res = 1 / (1 + Math.pow(2., -(count - Const.SIGMOID_CENTER) * Const.SIGMOID_WIDTH));
        return res;
    }

    public double[] calcMean(List<DataModel> dataList) {
        InnerVectorStatistics stat = new InnerVectorStatistics(dataList.get(0).getPosition().length);
        for (DataModel dataModel : dataList) {
            stat.add(dataModel.getPosition());
        }
        return stat.getMean();
    }

    public double[] calcStdDev(List<DataModel> dataList) {
        int length = dataList.get(0).getPosition().length;
        InnerVectorStatistics stat = new InnerVectorStatistics(length);
        for (DataModel dataModel : dataList) {
            stat.add(dataModel.getPosition());
        }
        return stat.getStdDev();
    }
}
