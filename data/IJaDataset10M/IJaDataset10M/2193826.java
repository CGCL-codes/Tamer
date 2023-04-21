package shieh.pnn.wm;

import java.util.*;
import shieh.pnn.core.Item;
import shieh.pnn.core.ItemSet;
import shieh.pnn.core.Param;

public class DataSet extends Vector<StimulusList> {

    public static final String LISTLEN_CURVE = "List length Curve";

    public static final String SERIALPOS_CURVE = "Serial position curve";

    public static final String SERIALITEM_CURVE = "Item accuracy curve";

    public static final String TRANSPOSITION_CURVE = "Transposition gradients";

    public static final String OVER_LENGTH = "Over Length Recalls";

    public static final String CONFUSION_MATRIX = "Confusion Matrix";

    private static final long serialVersionUID = 2577556782565663228L;

    public double accPos[][] = null;

    public double accItem[][] = null;

    public double accLen[] = null;

    public double outinPos[][][] = null;

    public int cntLen[] = null;

    public double[][] confusion = null;

    public double overLen = 0;

    public int digitSpan = 0;

    public int corr = 0, total = 0;

    public int nMaxLen = 0;

    public ItemSet itemSet;

    public static int[] range(int start, int end) {
        int[] range = new int[end - start + 1];
        for (int i = start; i <= end; i++) range[i - start] = i;
        return range;
    }

    public static int[] list(int... lenList) {
        return lenList;
    }

    public DataSet(ItemSet itemSet, DataType dt) {
        this(itemSet, dt, 1, null);
    }

    public DataSet(ItemSet itemSet, DataType dt, int numberPerLen) {
        this(itemSet, dt, numberPerLen, null);
    }

    public DataSet(ItemSet itemSet, DataType dt, int numberPerLen, int len) {
        this(itemSet, dt, numberPerLen, list(len));
    }

    public DataSet(ItemSet itemSet, DataType dt, int numberPerLen, int[] lengths, Integer... params) {
        this.itemSet = itemSet;
        if (dt == DataType.TEST) {
            GenerateTestData();
            return;
        } else if (dt == DataType.TEST2) {
            GenerateTestData2();
            return;
        } else if (dt == DataType.TEST_PROBED_MEM) {
            GenerateSingleMemTestData();
            return;
        } else if (dt == DataType.TEST_SINGLE_SEQUENCE) {
            GenerateSingleSequenceData();
            return;
        }
        addData(dt, numberPerLen, lengths, params);
        Collections.sort(this);
    }

    /**
	 * Generates random lists for working memory tests
	 * @param dt				data type
	 * @param nParams		flexible length. 
	 *                  nParams[0]: number of trials 
	 *                  nParams[1]: start length
	 *                  nParams[2]: end length (if present)
	 */
    public void addData(DataType dt, int numPerLen, int[] lengths, Integer... params) {
        StimulusList list = null;
        Vector<Item> items = null;
        Vector<Item> nonconfus = null;
        Vector<Item> confus = null;
        WordSet letters = null;
        int nLen = 0;
        for (int i = 0; i < numPerLen; i++) {
            switch(dt) {
                case TEST_SINGLE_ITEM:
                    for (int j = 0; j < itemSet.size(); j++) {
                        list = StimulusList.constantPause(itemSet.getList(j), Param.presentRate);
                        add(list);
                    }
                    break;
                case RANDOM_NO_REPEAT:
                    for (int j : lengths) {
                        nLen = j;
                        items = itemSet.getListNoRepeat(nLen);
                        list = StimulusList.constantPause(items, Param.presentRate);
                        add(list);
                    }
                    nMaxLen = nLen;
                    break;
                case RANDOM_WITH_REPEAT:
                    for (int j : lengths) {
                        nLen = j;
                        items = itemSet.getListWithRepeat(nLen);
                        list = StimulusList.constantPause(items, Param.presentRate);
                        add(list);
                    }
                    nMaxLen = nLen;
                    break;
                case RANDOM_6_GROUPING_NO_REPEAT:
                    items = itemSet.getListNoRepeat(6);
                    list = StimulusList.temporalGrouping(items, Param.presentRate, Param.groupPause, 3);
                    add(list);
                    nMaxLen = 6;
                    break;
                case RANDOM_GROUPING_NO_REPEAT:
                    nMaxLen = lengths[0];
                    items = itemSet.getListNoRepeat(nMaxLen);
                    list = StimulusList.chunkMarking(items, Param.presentRate, params[0], params[1]);
                    add(list);
                    break;
                case RANDOM_GROUPING_WITH_REPEAT:
                    nMaxLen = lengths[0];
                    items = itemSet.getListWithRepeat(nMaxLen);
                    list = StimulusList.chunkMarking(items, Param.presentRate, params[0], params[1]);
                    add(list);
                    break;
                case RANDOM_3_4_GROUPING_WITH_REPEAT:
                    items = itemSet.getListWithRepeat(7);
                    list = StimulusList.chunkMarking(items, Param.presentRate, 3, 7);
                    add(list);
                    nMaxLen = 7;
                    break;
                case SSSS:
                    letters = (WordSet) itemSet;
                    nMaxLen = nLen = lengths[0];
                    items = letters.confusableItems.getListNoRepeat(nLen);
                    list = StimulusList.constantPause(items, Param.presentRate);
                    add(list);
                    break;
                case DDDD:
                    letters = (WordSet) itemSet;
                    nMaxLen = nLen = lengths[0];
                    items = letters.nonconfusableItems.getListNoRepeat(nLen);
                    list = StimulusList.constantPause(items, Param.presentRate);
                    add(list);
                    break;
                case SDSD:
                    letters = (WordSet) itemSet;
                    nMaxLen = nLen = lengths[0];
                    nonconfus = letters.nonconfusableItems.getListNoRepeat(nLen / 2);
                    confus = letters.confusableItems.getListNoRepeat(nLen / 2);
                    items = alternateItems(confus, nonconfus);
                    list = StimulusList.constantPause(items, Param.presentRate);
                    add(list);
                    break;
                case DSDS:
                    letters = (WordSet) itemSet;
                    nMaxLen = nLen = lengths[0];
                    nonconfus = letters.nonconfusableItems.getListNoRepeat(nLen / 2);
                    confus = letters.confusableItems.getListNoRepeat(nLen / 2);
                    items = alternateItems(nonconfus, confus);
                    list = StimulusList.constantPause(items, Param.presentRate);
                    add(list);
                    break;
                case DDSS:
                    letters = (WordSet) itemSet;
                    nMaxLen = nLen = lengths[0];
                    items = letters.nonconfusableItems.getListNoRepeat(nLen / 2);
                    items.addAll(letters.confusableItems.getListNoRepeat(nLen / 2));
                    list = StimulusList.constantPause(items, Param.presentRate);
                    add(list);
                    break;
                case SSDD:
                    letters = (WordSet) itemSet;
                    nMaxLen = nLen = lengths[0];
                    items = letters.confusableItems.getListNoRepeat(nLen / 2);
                    items.addAll(letters.nonconfusableItems.getListNoRepeat(nLen / 2));
                    list = StimulusList.constantPause(items, Param.presentRate);
                    add(list);
                    break;
                case DSMIX:
                    letters = (WordSet) itemSet;
                    nMaxLen = nLen = lengths[0];
                    ItemSet mix = new ItemSet();
                    mix.addAll(letters.confusableItems);
                    mix.addAll(letters.nonconfusableItems);
                    items = mix.getListNoRepeat(nLen);
                    list = StimulusList.constantPause(items, Param.presentRate);
                    add(list);
                    break;
            }
        }
    }

    private Vector<Item> alternateItems(Vector<Item> seq1, Vector<Item> seq2) {
        Vector<Item> items = new Vector<Item>();
        Iterator<Item> iter1 = seq1.iterator();
        Iterator<Item> iter2 = seq2.iterator();
        while (iter1.hasNext() && iter2.hasNext()) {
            items.add(iter1.next());
            items.add(iter2.next());
        }
        while (iter1.hasNext()) items.add(iter1.next());
        while (iter2.hasNext()) items.add(iter2.next());
        return items;
    }

    private void GenerateTestData() {
        Vector<Item> items = new Vector<Item>();
        StimulusList list = new StimulusList();
        items = itemSet.getList(1, 2, 2);
        list = StimulusList.constantPause(items, Param.presentRate);
        add(list);
        items = itemSet.getList(1, 2, 2, 2, 4, 5);
        list = StimulusList.constantPause(items, Param.presentRate);
        add(list);
        items = itemSet.getList(1, 2, 2, 2, 4, 5);
        list = StimulusList.constantPause(items, Param.presentRate);
        add(list);
        items = itemSet.getList(1, 2, 1, 2, 2, 5);
        list = StimulusList.constantPause(items, Param.presentRate);
        add(list);
        items = itemSet.getList(2, 3, 3, 2);
        list = StimulusList.constantPause(items, Param.presentRate);
        add(list);
        items = itemSet.getList(2, 2, 3, 2, 2, 5);
        list = StimulusList.constantPause(items, Param.presentRate);
        add(list);
        items = itemSet.getList(1, 2, 2, 3, 3, 4, 2, 2, 5);
        list = StimulusList.constantPause(items, Param.presentRate);
        add(list);
        nMaxLen = 10;
    }

    private void GenerateTestData2() {
        Vector<Item> items = new Vector<Item>();
        StimulusList list = new StimulusList();
        items = itemSet.getList(1, 2, 3, 4, 5);
        list = StimulusList.constantPause(items, Param.presentRate);
        add(list);
        items = itemSet.getList(5, 4, 3, 2, 1);
        list = StimulusList.constantPause(items, Param.presentRate);
        add(list);
        nMaxLen = 10;
    }

    private void GenerateSingleMemTestData() {
        Vector<Item> items = new Vector<Item>();
        StimulusList list = new StimulusList();
        items = itemSet.getList(1, 10, 2, 3, 4, 5);
        list = StimulusList.constantPause(items, Param.presentRate);
        add(list);
        items = itemSet.getList(5, 4, 10, 3, 2, 1);
        list = StimulusList.constantPause(items, Param.presentRate);
        add(list);
        nMaxLen = 10;
    }

    private void GenerateSingleSequenceData() {
        Vector<Item> items = new Vector<Item>();
        StimulusList list = new StimulusList();
        items = itemSet.getList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        list = StimulusList.constantPause(items, Param.tick);
        add(list);
        nMaxLen = 9;
    }

    public double calcScores() {
        int len;
        accPos = new double[nMaxLen][nMaxLen];
        accItem = new double[nMaxLen][nMaxLen];
        accLen = new double[nMaxLen];
        outinPos = new double[nMaxLen][nMaxLen][nMaxLen];
        confusion = new double[itemSet.size()][itemSet.size()];
        cntLen = new int[nMaxLen];
        corr = 0;
        total = size();
        digitSpan = 0;
        int consecError = 0, lastCorrectLen = 0;
        for (StimulusList slist : this) {
            len = slist.trueLen() - 1;
            cntLen[len]++;
            for (int i = 0; i <= len; i++) {
                if (slist.corrPos[i]) accPos[len][i]++;
                if (slist.corrItem[i]) accItem[len][i]++;
            }
            if (slist.correct) {
                accLen[len]++;
                corr++;
                consecError = 0;
                lastCorrectLen = len + 1;
            } else {
                if (++consecError >= 2 && digitSpan == 0) digitSpan = lastCorrectLen;
            }
            for (int i = 0; i <= len; i++) for (int j = 0; j <= len; j++) outinPos[len][j][i] += (double) slist.outinPos[j][i] / total;
            if (slist.overLength) overLen++;
            if (slist.confusedItem != null && slist.confusedBy != null) {
                int recallItemID = slist.confusedBy.ID;
                int originalItemID = slist.confusedItem.ID;
                confusion[recallItemID][originalItemID]++;
            }
        }
        for (len = 0; len < nMaxLen; len++) {
            if (cntLen[len] != 0) {
                accLen[len] /= cntLen[len];
                for (int j = 0; j < nMaxLen; j++) {
                    accPos[len][j] /= cntLen[len];
                    accItem[len][j] /= cntLen[len];
                }
            }
        }
        corr /= total;
        overLen /= total;
        for (Item recall : itemSet) for (Item original : itemSet) {
            confusion[recall.ID][original.ID] /= total;
        }
        return corr;
    }

    public void printScores() {
        System.out.println("\nDIGIT SPAN\n");
        System.out.println("N = " + digitSpan + "\n");
        System.out.println("\nOVER LENGTH\n");
        System.out.printf("Over-length (at least by 2 items): %.2f%%\n", overLen * 100);
        System.out.println("\nSERIAL POSITION CURVES BY LENGTH (ALL ERRORS)");
        int len, i, j;
        for (len = 0; len < nMaxLen; len++) System.out.printf("\t%d", len + 1);
        System.out.println();
        for (len = 0; len < nMaxLen; len++) if (cntLen[len] > 0) {
            System.out.print(len + 1);
            for (j = 0; j <= len; j++) System.out.printf("\t%.2f", accPos[len][j] * 100);
            System.out.println();
        }
        System.out.println("\nACCURACY BY LENGTH");
        for (len = 0; len < nMaxLen; len++) if (cntLen[len] > 0) System.out.printf("%d\t", len + 1);
        System.out.println();
        for (len = 0; len < nMaxLen; len++) if (cntLen[len] > 0) System.out.printf("%.2f\t", accLen[len] * 100);
        System.out.println();
        System.out.println("\nSERIAL POSITION CURVES BY LENGTH (ITEM ERRORS)");
        for (len = 0; len < nMaxLen; len++) System.out.printf("\t%d", len + 1);
        System.out.println();
        for (len = 0; len < nMaxLen; len++) if (cntLen[len] > 0) {
            System.out.print(len + 1);
            for (j = 0; j <= len; j++) System.out.printf("\t%.2f", accItem[len][j] * 100);
            System.out.println();
        }
        System.out.println("\nTRANSPOSITION GRADIENTS BY LENGTH");
        for (len = 0; len < nMaxLen; len++) {
            if (cntLen[len] > 0) {
                System.out.printf("\nLength %d\n", len + 1);
                for (i = 0; i <= len; i++) {
                    System.out.printf("Output position %d", i + 1);
                    for (j = 0; j <= len; j++) System.out.printf("\t%.2f", 100 * outinPos[len][i][j]);
                    System.out.printf("\n");
                }
            }
        }
    }

    /**
	 * Get a curve from the data 
	 * @param key     A key to the curve, e.g., LISTLEN_CURVE
	 * @param params  Optional parameters, depending on the curve. Typically, params[0] is the length
	 * @return the curve data (if two dimensional, they are combined in one dimension) 
	 */
    public double[] getCurve(String key, Object... params) {
        if (key.equals(LISTLEN_CURVE)) return accLen; else if (key.equals(SERIALPOS_CURVE)) {
            int len = (Integer) params[0];
            return accPos[len - 1];
        } else if (key.equals(TRANSPOSITION_CURVE)) {
            int len = (Integer) params[0];
            double[] combined_curve = new double[len * len];
            for (int i = 0; i < len; i++) for (int j = 0; j < len; j++) combined_curve[i * len + j] = outinPos[len - 1][i][j];
            return combined_curve;
        } else if (key.equals(SERIALITEM_CURVE)) {
            int len = (Integer) params[0];
            return accItem[len - 1];
        } else if (key.equals(OVER_LENGTH)) return new double[] { overLen }; else if (key.equals(CONFUSION_MATRIX)) {
            double[] confusion_curve = new double[confusion.length * confusion[0].length];
            for (int i = 0; i < confusion.length; i++) for (int j = 0; j < confusion[i].length; j++) confusion_curve[i * confusion[0].length + j] = confusion[i][j];
            return confusion_curve;
        }
        return null;
    }
}
