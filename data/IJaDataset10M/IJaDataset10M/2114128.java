package preprocessing.methods.DataReduction.Drop;

import preprocessing.methods.DataReduction.common.NDInstance;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Trida popisujici jednu instanci pro vypocet DROP3, rozsiruje moznosti NDInstance
 *
 * @author Helena Krkoskova
 */
class DROP3Instance extends NDInstance {

    /**
     * seznam sousednich prvku - (K+1 nejblizsich prvku)
     */
    private List<DROP3Instance> neighbourList;

    /**
     * seznam asociovanych prvku (prvku, ktere maji v seznamuN)
     */
    private Collection<DROP3Instance> associateList = new HashSet<DROP3Instance>();

    /**
     * vzdalenost k nejblizsimu souperi
     */
    private double opponentDistance;

    /**
     * Konstruktor
     *
     * @param instance
     */
    public DROP3Instance(NDInstance instance) {
        super(instance.getVectorData(), instance.getCorrectCategoryClass(), instance.getIndexInStore());
    }

    public List<DROP3Instance> getNeighbourList() {
        return neighbourList;
    }

    public Collection<DROP3Instance> getAssociateList() {
        return associateList;
    }

    public void setNeighbourList(List<DROP3Instance> neighbourList) {
        this.neighbourList = neighbourList;
    }

    /**
     * Nastavi vzdalenost k souperi
     *
     * @param distance hodnota vzdalenosti k souperi
     */
    public void setOpponentDistance(double distance) {
        this.opponentDistance = distance;
    }

    /**
     * Vraci nastavenou hodnotu vzdalenosti k nejblizsimu souperi
     *
     * @return hodnota vzdalenosti (eukl. vzdalenost na druhou)
     */
    public double getOpponentDistance() {
        return opponentDistance;
    }
}
