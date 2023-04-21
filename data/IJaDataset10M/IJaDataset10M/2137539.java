package fuku.eb4j;

/**
 * 検索インタフェース。
 *
 * @author Hisaya FUKUMOTO
 */
public interface Searcher {

    /**
     * 次の検索結果を返します。
     *
     * @return 検索結果 (次の検索結果がない場合null)
     * @exception EBException 検索中にエラーが発生した場合
     */
    Result getNextResult() throws EBException;

    /**
     * 利用したInputStreamをすべて閉じる。
     * 現在のSeacherの利用を終了するには必ず呼び出す必要がある。
     */
    void close();
}
