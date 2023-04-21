package jp.seraph.sample.collection;

public interface Equivalence<_Left, _Right> {

    /**
     * aLeftとaRightを比較し、両者の関係が条件を満たすかどうかを調べて返します。
     *
     * @param aLeft
     * @param aRight
     * @return 等価とみなせる場合true
     */
    public boolean apply(_Left aLeft, _Right aRight);
}
