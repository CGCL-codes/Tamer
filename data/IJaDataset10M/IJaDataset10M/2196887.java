package il.ac.biu.cs.grossmm.impl.activeData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import il.ac.biu.cs.grossmm.api.flow.Converter;

/**
 * Filds all shortest conversion paths between all possible pairs of data
 * exchange points
 * 
 */
public class BestConversionChainFinder {

    DataLayer[] dataLayers;

    Map<DataLayer, Integer> indeces;

    /**
	 * Creates the new <tt>BestConversionChainFinder</tt> object based on data
	 * conversion graph
	 * 
	 * @param dataLayers
	 *            data layers
	 */
    BestConversionChainFinder(Collection<DataLayer> dataLayers) {
        int size = dataLayers.size();
        this.dataLayers = new DataLayer[size];
        indeces = new HashMap<DataLayer, Integer>(size);
        int i = 0;
        for (Iterator it = dataLayers.iterator(); it.hasNext(); ) {
            DataLayer dataLayer = (DataLayer) it.next();
            this.dataLayers[i] = dataLayer;
            indeces.put(dataLayer, i);
            i++;
        }
    }

    @SuppressWarnings("unchecked")
    void findBestChains() {
        int n = dataLayers.length;
        int i, j, k;
        double[] priorities = new double[n * n];
        int[] predcessor = new int[n * n];
        for (i = 0; i < n; i++) for (j = 0; j < n; j++) {
            int ij = i * n + j;
            if (i == j) {
                priorities[ij] = 0;
                predcessor[ij] = -1;
            } else {
                double priority = getDirectPriority(i, j);
                priorities[ij] = priority;
                if (priority == 0) predcessor[ij] = -1; else predcessor[ij] = i;
            }
        }
        for (k = 0; k < n; k++) {
            for (i = 0; i < n; i++) {
                for (j = 0; j < n; j++) {
                    int ij = i * n + j, ik = i * n + k, kj = k * n + j;
                    double priority = priorities[ij];
                    double priority2 = priorities[ik] * priorities[kj];
                    if (priority < priority2) {
                        priorities[ij] = priority2;
                        predcessor[ij] = predcessor[kj];
                    }
                }
            }
        }
        Converter[] a = new Converter[n];
        for (j = 0; j < n; j++) {
            Map<DataLayer, ConversionChain> bestChains = new HashMap<DataLayer, ConversionChain>();
            for (i = 0; i < n; i++) {
                if (i == j) continue;
                int index = n;
                k = j;
                while (true) {
                    int k_prev = k;
                    k = predcessor[i * n + k];
                    assert k != k_prev : "Loop!";
                    if (k == -1) break;
                    a[--index] = dataLayers[k].getConverterTo(dataLayers[k_prev]);
                }
                int length = n - index;
                if (length == 0) continue;
                ConversionProcessor[] chain = new ConversionProcessor[length];
                System.arraycopy(a, index, chain, 0, length);
                double priority = priorities[i * n + j];
                ConversionChain cc = new ConversionChain(chain, priority);
                bestChains.put(dataLayers[i], cc);
            }
            dataLayers[j].setConversionChains(bestChains);
        }
    }

    /**
	 * Gets the relative priority of conversion from i-th data layer to j-th
	 * data layer. Returns zero if there is no unconditional converter to
	 * convert between the layers
	 * 
	 * @param i
	 * @param j
	 * @return relative priority of conversion from i-th data layer to k-th data
	 *         layer, or zero if there is no unconditional converter to convert
	 *         between the layers
	 */
    private double getDirectPriority(int i, int j) {
        DataLayer from = dataLayers[i];
        DataLayer to = dataLayers[j];
        Converter c = from.getConverterTo(to);
        if (c == null) return 0;
        return c.getPriority();
    }
}
