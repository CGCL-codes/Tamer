package preprocessing.methods.DataReduction.Hart;

import preprocessing.methods.DataReduction.common.KnnAlgorithm;
import preprocessing.methods.DataReduction.common.NDInstance;
import java.util.*;

/**
 * @author Helena Krkoskova
 */
public class HartsCondensingAlgorithm {

    /**
     * Vlastni algoritmus, probiha v nasledujicich krocich:
     * 1. Vyber nahodny vzorek z trenovaci mnoziny X a vloz jej do prazdne?mnoziny R
     * 2. Pro vsechny vzorky v trenovaci mnozine X proved klasifikaci pomoci metody 1-NN, kde jako trenovaci mnozinu pouzij mnozinu R. Pokud nebude spravne klasifikovan, tak jej vloz do R mnoziny.
     * 3. Pokud nedoslo v predeslem kroku ke zmene R mnoziny a zaroven X mnozina neni prazdna, tak prejdi na bod 4, jinak opakuj od bodu 2
     * 4. R je redukovana trenovaci mnozina
     *
     * @param xSet         trenovaci X mnozina
     * @param k            pocet trid v okoli - K-nn algoritmus
     * @param classesCount pocet trid ke klasifikaci
     * @return R je redukovana trenovaci mnozina
     */
    public static List<NDInstance> run(List<NDInstance> xSet, final int k, final int classesCount) {
        List<NDInstance> rSet = new ArrayList<NDInstance>();
        rSet.add(xSet.get(new Random().nextInt(xSet.size())));
        int rSetPreviousSize = 0;
        final Collection<NDInstance> removeFromXSet = new HashSet<NDInstance>();
        while (rSetPreviousSize != rSet.size()) {
            rSetPreviousSize = rSet.size();
            removeFromXSet.clear();
            for (NDInstance instance : xSet) {
                KnnAlgorithm.findKNearestNeighbour(instance, rSet, k, classesCount);
                if (instance.getCorrectCategoryClass() != instance.getResultCategoryClass()) {
                    instance.setResultCategoryClass(instance.getCorrectCategoryClass());
                    removeFromXSet.add(instance);
                    rSet.add(instance);
                }
            }
            xSet.removeAll(removeFromXSet);
        }
        return rSet;
    }
}
