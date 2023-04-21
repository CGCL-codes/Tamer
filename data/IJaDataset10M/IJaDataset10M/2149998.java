package com.coyousoft.adsys.constant;

import com.coyousoft.adsys.logic.AdvertLogic;
import com.coyousoft.adsys.logic.DataExchangeLogic;
import com.coyousoft.adsys.logic.EmailLogic;
import com.coyousoft.adsys.logic.EmailOpenedLogLogic;
import com.coyousoft.adsys.logic.GoodsCatalogLogic;
import com.coyousoft.adsys.logic.GoodsItemLogic;
import com.coyousoft.adsys.logic.GoodsShopLogic;
import com.coyousoft.adsys.logic.SenderLogic;
import com.coyousoft.adsys.logic.UserMailLogic;
import com.coyousoft.adsys.logicimpl.AdvertLogicImpl;
import com.coyousoft.adsys.logicimpl.DataExchangeLogicImpl;
import com.coyousoft.adsys.logicimpl.EmailLogicImpl;
import com.coyousoft.adsys.logicimpl.EmailOpenedLogLogicImpl;
import com.coyousoft.adsys.logicimpl.GoodsCatalogLogicImpl;
import com.coyousoft.adsys.logicimpl.GoodsItemLogicImpl;
import com.coyousoft.adsys.logicimpl.GoodsShopLogicImpl;
import com.coyousoft.adsys.logicimpl.SenderLogicImpl;
import com.coyousoft.adsys.logicimpl.UserMailLogicImpl;

public interface Logic {

    SenderLogic senderLogic = new SenderLogicImpl();

    AdvertLogic advertLogic = new AdvertLogicImpl();

    UserMailLogic userMailLogic = new UserMailLogicImpl();

    EmailLogic emailLogic = new EmailLogicImpl();

    EmailOpenedLogLogic emailOpenedLogLogic = new EmailOpenedLogLogicImpl();

    DataExchangeLogic dataExchangeLogic = new DataExchangeLogicImpl();

    GoodsCatalogLogic goodsCatalogLogic = new GoodsCatalogLogicImpl();

    GoodsItemLogic goodsItemLogic = new GoodsItemLogicImpl();

    GoodsShopLogic goodsShopLogic = new GoodsShopLogicImpl();
}
