package com.kongur.network.erp.ao.ic.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kongur.network.erp.ao.ic.TaobaoPublishAO;
import com.kongur.network.erp.exception.ic.IcException;
import com.kongur.network.erp.request.ic.ItemCatRequest;
import com.kongur.network.erp.request.ic.taobao.TaobaoItemOptRequest;
import com.kongur.network.erp.request.ic.taobao.TaobaoItemRequest;
import com.kongur.network.erp.request.ic.taobao.TaobaoSkuRequest;
import com.kongur.network.erp.result.ic.ItemCatResult;
import com.kongur.network.erp.result.ic.ItemPromotionsResult;
import com.kongur.network.erp.result.ic.ItemResult;
import com.kongur.network.erp.result.ic.ItemSkuResult;
import com.kongur.network.erp.result.ic.ItemTemplatesResult;
import com.kongur.network.erp.service.ic.IcService;
import com.kongur.network.erp.service.ic.ItemCatService;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.domain.ItemProp;
import com.taobao.api.domain.ItemTemplate;
import com.taobao.api.domain.PromotionDisplayTop;
import com.taobao.api.domain.Sku;

/**
 * @author gaojf
 * @version $Id: TaobaoPublishAOImpl.java,v 0.1 2012-3-16 ����10:26:12 gaojf Exp $
 */
@Service("taobaoPublishAO")
public class TaobaoPublishAOImpl implements TaobaoPublishAO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ItemCatService itemCatService;

    @Autowired
    private IcService icService;

    /**
     * ��ȡ��׼��Ʒ��Ŀ���� 
    */
    public List<ItemProp> getItemProps(ItemCatRequest req) {
        ItemCatResult res = null;
        try {
            res = itemCatService.getItemProps(req);
        } catch (IcException e) {
            logger.error("getItemProps errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("getItemProps errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getProps();
    }

    /**
     * ��ȡ��̨�����ҷ�����Ʒ�ı�׼��Ʒ��Ŀ 
     */
    public List<ItemCat> getItemCats(ItemCatRequest req) {
        ItemCatResult res = null;
        try {
            res = itemCatService.getItemCats(req);
        } catch (IcException e) {
            logger.error("getItemCats errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("getItemProps errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getCats();
    }

    @Override
    public Item add(TaobaoItemOptRequest req) {
        ItemResult res = null;
        try {
            res = icService.add(req);
        } catch (IcException e) {
            logger.error("add errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("add errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getItem();
    }

    @Override
    public Item delete(TaobaoItemRequest req) {
        ItemResult res = null;
        try {
            res = icService.delete(req);
        } catch (IcException e) {
            logger.error("delete errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("delete errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getItem();
    }

    @Override
    public Item get(TaobaoItemRequest req) {
        ItemResult res = null;
        try {
            res = icService.get(req);
        } catch (IcException e) {
            logger.error("get errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("get errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getItem();
    }

    @Override
    public List<Item> itemsCustomGet(TaobaoItemRequest req) {
        ItemResult res = null;
        try {
            res = icService.itemsCustomGet(req);
        } catch (IcException e) {
            logger.error("itemsCustomGet errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("itemsCustomGet errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getItems();
    }

    @Override
    public List<Item> itemsGet(TaobaoItemOptRequest req) {
        ItemResult res = null;
        try {
            res = icService.itemsGet(req);
        } catch (IcException e) {
            logger.error("itemsGet errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("itemsGet errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getItems();
    }

    @Override
    public Item priceUpdate(TaobaoItemOptRequest req) {
        ItemResult res = null;
        try {
            res = icService.priceUpdate(req);
        } catch (IcException e) {
            logger.error("priceUpdate errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("priceUpdate errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getItem();
    }

    @Override
    public Item quantityUpdate(TaobaoSkuRequest req) {
        ItemResult res = null;
        try {
            res = icService.quantityUpdate(req);
        } catch (IcException e) {
            logger.error("quantityUpdate errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("quantityUpdate errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getItem();
    }

    @Override
    public Sku skuAdd(TaobaoSkuRequest req) {
        ItemSkuResult res = null;
        try {
            res = icService.skuAdd(req);
        } catch (IcException e) {
            logger.error("skuAdd errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("skuAdd errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getSku();
    }

    @Override
    public Sku skuDelete(TaobaoSkuRequest req) {
        ItemSkuResult res = null;
        try {
            res = icService.skuDelete(req);
        } catch (IcException e) {
            logger.error("skuDelete errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("skuDelete errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getSku();
    }

    @Override
    public Sku skuGet(TaobaoSkuRequest req) {
        ItemSkuResult res = null;
        try {
            res = icService.skuGet(req);
        } catch (IcException e) {
            logger.error("skuGet errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("skuGet errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getSku();
    }

    @Override
    public Sku skuPriceUpdate(TaobaoSkuRequest req) {
        ItemSkuResult res = null;
        try {
            res = icService.skuPriceUpdate(req);
        } catch (IcException e) {
            logger.error("skuPriceUpdate errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("skuPriceUpdate errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getSku();
    }

    @Override
    public Sku skuUpdate(TaobaoSkuRequest req) {
        ItemSkuResult res = null;
        try {
            res = icService.skuUpdate(req);
        } catch (IcException e) {
            logger.error("skuUpdate errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("skuUpdate errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getSku();
    }

    @Override
    public List<Sku> skusCustomGet(TaobaoSkuRequest req) {
        ItemSkuResult res = null;
        try {
            res = icService.skusCustomGet(req);
        } catch (IcException e) {
            logger.error("skusCustomGet errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("skusCustomGet errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getSkus();
    }

    @Override
    public List<Sku> skusGet(TaobaoSkuRequest req) {
        ItemSkuResult res = null;
        try {
            res = icService.skusGet(req);
        } catch (IcException e) {
            logger.error("skusGet errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("skusGet errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getSkus();
    }

    @Override
    public List<ItemTemplate> templatesGet(TaobaoItemRequest req) {
        ItemTemplatesResult res = null;
        try {
            res = icService.templatesGet(req);
        } catch (IcException e) {
            logger.error("templatesGet errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("templatesGet errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getItemTemplates();
    }

    @Override
    public PromotionDisplayTop umpPromotionGet(TaobaoItemRequest req) {
        ItemPromotionsResult res = null;
        try {
            res = icService.umpPromotionGet(req);
        } catch (IcException e) {
            logger.error("umpPromotionGet errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("umpPromotionGet errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getPromotions();
    }

    @Override
    public Item update(TaobaoItemOptRequest req) {
        ItemResult res = null;
        try {
            res = icService.update(req);
        } catch (IcException e) {
            logger.error("update errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("update errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getItem();
    }

    @Override
    public Item updateDelisting(TaobaoItemRequest req) {
        ItemResult res = null;
        try {
            res = icService.updateDelisting(req);
        } catch (IcException e) {
            logger.error("updateDelisting errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("updateDelisting errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getItem();
    }

    @Override
    public Item updateListing(TaobaoItemRequest req) {
        ItemResult res = null;
        try {
            res = icService.updateListing(req);
        } catch (IcException e) {
            logger.error("updateListing errror:", e);
            return null;
        }
        if (res.hasError()) {
            logger.error("updateListing errror:ErrorCode=" + res.getErrorCode() + ";ErrorInfo=" + res.getErrorInfo() + ";req=" + req);
            return null;
        }
        return res.getItem();
    }
}
