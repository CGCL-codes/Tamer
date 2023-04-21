package com.gmvc.client.base;

import java.util.List;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.gmvc.client.meta.FilterItem;

/**
 * View kismi iki yapidan meydana gelir; Browser ve Editor.
 * 
 * <p/>
 * Browser, View tarafinda yer alan ve verileri listeledigimiz kisimdir.
 * Search-Delete islemleri bu formlarda yapilir.
 * 
 * @see IModel
 * 
 * @author mdpinar
 * 
 */
public interface IBrowser<M extends IModel> {

    /**
	 * Formun gosterimini saglar.
	 * 
	 */
    ContentPanel popup();

    /**
	 * Browser uzerinde secili olan Modeli doner
	 * 
	 * @see IModel
	 */
    M getSelectedModel();

    /**
	 * Browser uzerinde secili olan Modelleri doner
	 * 
	 * @see IModel
	 */
    List<M> getSelectedModels();

    /**
	 * Ozel olay dinleyicilerin eklenebilmesi ve mevcut ozelliklerinin
	 * degistirilebilmesi icin ana gridi doner.
	 * 
	 */
    Grid<BeanModel> getGrid();

    /**
	 * Browser daki model listesini tazeler
	 * 
	 * <p/>
	 * Paramete olarak BeanModel almasinin nedeni; Sistemde iki cesit MVC yapisi
	 * kullanildi, MVC ve MiniMVC. MVC yapisina gore dogrudan Modeller ile
	 * calisiliyor fakat MiniMVC ye gore gecici modeller uzerinde calisiliyor.
	 * Her iki yapi ile calisabilmek icin ortak olan BeanModel kullanildi.
	 * 
	 */
    void refresh(ListStore<BeanModel> store);

    /**
	 * Silme isleminden onceki "emin misiniz" sorusu icin gerekli olan
	 * bilgilendirme kismini doner
	 */
    String getDeleteQueryPart();

    /**
	 * Editor ve Browser yapisi tek bir formda yer alacaksa bu metod true doner
	 */
    boolean isSingleForm();

    /**
	 * Tum filtrelerin ustunde degerlendirilmesi gereken master filtre varsa bu metoddan donulecek
	 */
    FilterItem getMasterFilterItem();
}
