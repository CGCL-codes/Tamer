package javax.media.ding3d;

class NnuIdManager {

    static int nnuId = 0;

    static final int getId() {
        if (nnuId == Integer.MAX_VALUE) {
            nnuId = 0;
        }
        return nnuId++;
    }

    static final int equals(NnuId nnuIdArr[], NnuId key, int start, int end) {
        int mid;
        mid = start + ((end - start) / 2);
        if (nnuIdArr[mid] != null) {
            int test = key.equal(nnuIdArr[mid]);
            if ((test < 0) && (start != mid)) return equals(nnuIdArr, key, start, mid); else if ((test > 0) && (start != mid)) return equals(nnuIdArr, key, mid, end); else if (test == 0) {
                if (key == nnuIdArr[mid]) {
                    return mid;
                }
                int temp = mid - 1;
                while ((temp >= start) && (key.equal(nnuIdArr[temp]) == 0)) {
                    if (key == nnuIdArr[temp]) {
                        return temp;
                    }
                    temp--;
                }
                temp = mid + 1;
                while ((temp < end) && (key.equal(nnuIdArr[temp]) == 0)) {
                    if (key == nnuIdArr[temp]) {
                        return temp;
                    }
                    temp++;
                }
                return -1;
            } else return -1;
        }
        return -2;
    }

    static final boolean equals(NnuId nnuIdArr[], NnuId key, int[] index, int start, int end) {
        int mid;
        mid = start + ((end - start) / 2);
        if (nnuIdArr[mid] != null) {
            int test = key.equal(nnuIdArr[mid]);
            if (start != mid) {
                if (test < 0) {
                    return equals(nnuIdArr, key, index, start, mid);
                } else if (test > 0) {
                    return equals(nnuIdArr, key, index, mid, end);
                }
            } else {
                if (test < 0) {
                    index[0] = mid;
                    return false;
                } else if (test > 0) {
                    index[0] = mid + 1;
                    return false;
                }
            }
            if (key == nnuIdArr[mid]) {
                index[0] = mid;
                return true;
            }
            int temp = mid - 1;
            while ((temp >= start) && (key.equal(nnuIdArr[temp]) == 0)) {
                if (key == nnuIdArr[temp]) {
                    index[0] = temp;
                    return true;
                }
                temp--;
            }
            temp = mid + 1;
            while ((temp < end) && (key.equal(nnuIdArr[temp]) == 0)) {
                if (key == nnuIdArr[temp]) {
                    index[0] = temp;
                    return true;
                }
                temp++;
            }
            index[0] = temp;
            return false;
        }
        index[0] = mid;
        return false;
    }

    static final void sort(NnuId nnuIdArr[]) {
        if (nnuIdArr.length < 20) {
            insertSort(nnuIdArr);
        } else {
            quicksort(nnuIdArr, 0, nnuIdArr.length - 1);
        }
    }

    static final void insertSort(NnuId nnuIdArr[]) {
        for (int i = 0; i < nnuIdArr.length; i++) {
            for (int j = i; j > 0 && (nnuIdArr[j - 1].getId() > nnuIdArr[j].getId()); j--) {
                NnuId temp = nnuIdArr[j];
                nnuIdArr[j] = nnuIdArr[j - 1];
                nnuIdArr[j - 1] = temp;
            }
        }
    }

    static final void quicksort(NnuId nnuIdArr[], int l, int r) {
        int i = l;
        int j = r;
        int k = nnuIdArr[(l + r) / 2].getId();
        do {
            while (nnuIdArr[i].getId() < k) i++;
            while (k < nnuIdArr[j].getId()) j--;
            if (i <= j) {
                NnuId tmp = nnuIdArr[i];
                nnuIdArr[i] = nnuIdArr[j];
                nnuIdArr[j] = tmp;
                i++;
                j--;
            }
        } while (i <= j);
        if (l < j) quicksort(nnuIdArr, l, j);
        if (l < r) quicksort(nnuIdArr, i, r);
    }

    static final NnuId[] delete(NnuId nnuIdArr0[], NnuId nnuIdArr1[]) {
        int i, index, len;
        int curStart = 0, newStart = 0;
        boolean found = false;
        int size = nnuIdArr0.length - nnuIdArr1.length;
        if (size > 0) {
            NnuId newNnuIdArr[] = new NnuId[size];
            for (i = 0; i < nnuIdArr1.length; i++) {
                index = equals(nnuIdArr0, nnuIdArr1[i], 0, nnuIdArr0.length);
                if (index >= 0) {
                    found = true;
                    if ((i < (nnuIdArr1.length - 1)) && nnuIdArr1[i].getId() == nnuIdArr1[i + 1].getId()) {
                        NnuId[] tmpNnuIdArr0 = new NnuId[nnuIdArr0.length - 1];
                        System.arraycopy(nnuIdArr0, 0, tmpNnuIdArr0, 0, index);
                        System.arraycopy(nnuIdArr0, index + 1, tmpNnuIdArr0, index, nnuIdArr0.length - index - 1);
                        nnuIdArr0 = tmpNnuIdArr0;
                    } else {
                        if (index == curStart) {
                            curStart++;
                        } else {
                            len = index - curStart;
                            System.arraycopy(nnuIdArr0, curStart, newNnuIdArr, newStart, len);
                            curStart = index + 1;
                            newStart = newStart + len;
                        }
                    }
                } else {
                    found = false;
                    MasterControl.getCoreLogger().severe("Can't Find matching nnuId.");
                }
            }
            if ((found == true) && (curStart < nnuIdArr0.length)) {
                len = nnuIdArr0.length - curStart;
                System.arraycopy(nnuIdArr0, curStart, newNnuIdArr, newStart, len);
            }
            return newNnuIdArr;
        } else if (size == 0) {
        } else {
            MasterControl.getCoreLogger().severe("Attempt to remove more elements than are present");
        }
        return null;
    }

    static final NnuId[] merge(NnuId nnuIdArr0[], NnuId nnuIdArr1[]) {
        int index[] = new int[1];
        int indexPlus1, blkSize, i, j;
        int size = nnuIdArr0.length + nnuIdArr1.length;
        NnuId newNnuIdArr[] = new NnuId[size];
        System.arraycopy(nnuIdArr0, 0, newNnuIdArr, 0, nnuIdArr0.length);
        for (i = nnuIdArr0.length, j = 0; i < size; i++, j++) {
            equals((NnuId[]) newNnuIdArr, nnuIdArr1[j], index, 0, i);
            if (index[0] == i) {
                newNnuIdArr[i] = nnuIdArr1[j];
            } else {
                indexPlus1 = index[0] + 1;
                blkSize = i - index[0];
                System.arraycopy(newNnuIdArr, index[0], newNnuIdArr, indexPlus1, blkSize);
                newNnuIdArr[index[0]] = nnuIdArr1[j];
            }
        }
        return newNnuIdArr;
    }

    static final void replace(NnuId oldObj, NnuId newObj, NnuId nnuIdArr[]) {
        int[] index = new int[1];
        int lenLess1 = nnuIdArr.length - 1;
        int blkSize;
        index[0] = equals(nnuIdArr, oldObj, 0, nnuIdArr.length);
        if (index[0] == lenLess1) {
            nnuIdArr[index[0]] = null;
        } else if (index[0] >= 0) {
            blkSize = lenLess1 - index[0];
            System.arraycopy(nnuIdArr, index[0] + 1, nnuIdArr, index[0], blkSize);
            nnuIdArr[lenLess1] = null;
        } else {
            MasterControl.getCoreLogger().severe("Can't Find matching nnuId.");
        }
        equals(nnuIdArr, newObj, index, 0, lenLess1);
        if (index[0] == lenLess1) {
            nnuIdArr[index[0]] = newObj;
        } else {
            blkSize = lenLess1 - index[0];
            System.arraycopy(nnuIdArr, index[0], nnuIdArr, index[0] + 1, blkSize);
            nnuIdArr[index[0]] = newObj;
        }
    }

    static final void printIds(NnuId nnuIdArr[]) {
        for (int i = 0; i < nnuIdArr.length; i++) {
            System.err.println("[" + i + "] is " + nnuIdArr[i].getId());
        }
    }
}
