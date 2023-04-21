package name.huzhenbo.java.algorithm.sort;

class MergeSorter implements Sorter {

    /**
     * Complexity: O(nlogn)
     *
     * @param input
     * @return
     */
    public int[] go(int[] input) {
        return _mergeSort(input, 0, input.length - 1);
    }

    private int[] _mergeSort(int[] input, int start, int end) {
        int[] result = new int[end - start + 1];
        if (start == end) {
            result[0] = input[start];
            return result;
        }
        int[] lResult = _mergeSort(input, start, (start + end) / 2);
        int[] rResult = _mergeSort(input, (start + end) / 2 + 1, end);
        for (int i = 0, li = 0, ri = 0; i < result.length; i++) {
            if (ri >= rResult.length) {
                result[i] = lResult[li++];
            } else if (li >= lResult.length) {
                result[i] = rResult[ri++];
            } else {
                result[i] = lResult[li] > rResult[ri] ? rResult[ri++] : lResult[li++];
            }
        }
        return result;
    }
}
