package xbird.util.concurrent.reference;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public interface ReferentFinalizer<K, V> {

    public void finalize(K key, V reclaimed);
}
