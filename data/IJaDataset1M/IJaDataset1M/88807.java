package com.dukesoftware.viewlon3.gui.infopanel.searchpanel;

import com.dukesoftware.viewlon3.data.common.Const;
import com.dukesoftware.viewlon3.data.common.DataController;
import com.dukesoftware.viewlon3.data.common.DataManagerCore;
import com.dukesoftware.viewlon3.data.internal.ClassObjectArray;
import com.dukesoftware.viewlon3.gui.infopanel.InfoPanel;

/**
 * 検索モードのための抽象クラスです。
 * StrategyパターンのAbstractStrategy役になります。
 * 
 * 
 *
 *
 */
public abstract class SearchMode implements Const {

    protected final DataController<DataManagerCore> d_con;

    protected final InfoPanel infoPanel;

    public SearchMode(DataController d_con, InfoPanel infoPanel) {
        this.d_con = d_con;
        this.infoPanel = infoPanel;
    }

    /**
	 * 検索を実行するための抽象メソッドです。
	 * @param text 検索する文字列
	 */
    public abstract void search(String text);
}
