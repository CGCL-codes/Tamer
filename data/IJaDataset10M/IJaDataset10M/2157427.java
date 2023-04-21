package com.zhiyun.admin.action;

import java.io.File;
import java.util.List;
import com.zhiyun.admin.common.BaseActionSupport;
import com.zhiyun.admin.common.Page;
import com.zhiyun.admin.common.bo.ItemQueryBo;
import com.zhiyun.admin.service.*;
import com.zhiyun.admin.vo.*;

public class ItemAction extends BaseActionSupport {

    private static final long serialVersionUID = -6094943481765647800L;

    private IEbShopService shopService;

    private IEbItemService itemService;

    private File items;

    private String itemsFileName;

    private String itemsContentType;

    private List<EbShop> shopList;

    private EbItem item;

    private EbShop shop;

    private ItemQueryBo queryBo;

    private String msg;

    private Page page;

    public File getItems() {
        return items;
    }

    public void setItems(File items) {
        this.items = items;
    }

    public String getItemsFileName() {
        return itemsFileName;
    }

    public void setItemsFileName(String itemsFileName) {
        this.itemsFileName = itemsFileName;
    }

    public String getItemsContentType() {
        return itemsContentType;
    }

    public void setItemsContentType(String itemsContentType) {
        this.itemsContentType = itemsContentType;
    }

    public List<EbShop> getShopList() {
        return shopList;
    }

    public void setShopList(List<EbShop> shopList) {
        this.shopList = shopList;
    }

    public EbItem getItem() {
        return item;
    }

    public EbShop getShop() {
        return shop;
    }

    public void setShop(EbShop shop) {
        this.shop = shop;
    }

    public void setItem(EbItem item) {
        this.item = item;
    }

    public ItemQueryBo getQueryBo() {
        return queryBo;
    }

    public void setQueryBo(ItemQueryBo queryBo) {
        this.queryBo = queryBo;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public void setShopService(IEbShopService shopService) {
        this.shopService = shopService;
    }

    public void setItemService(IEbItemService itemService) {
        this.itemService = itemService;
    }

    public String addItem() {
        try {
            shopList = shopService.getAllShops();
        } catch (Exception e) {
            e.printStackTrace();
            msg = "数据库异常!";
        }
        return SUCCESS;
    }

    public String importItems() {
        try {
            shopList = shopService.getAllShops();
            if (items == null) {
                msg = "没有找到上传文件!";
            } else {
                msg = itemService.saveImportItems(items, shop);
                if (msg == null) {
                    msg = "导入成功!";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "发生异常, 无效的文件类型!";
        }
        return SUCCESS;
    }

    public String preEditItem() {
        try {
            shopList = shopService.getAllShops();
            item = itemService.getItemDetail(item.getId());
        } catch (Exception e) {
            e.printStackTrace();
            msg = "数据库异常!";
        }
        return SUCCESS;
    }

    public String saveEditItem() {
        try {
            msg = itemService.updateItem(item, items, itemsFileName);
            shopList = shopService.getAllShops();
            item = itemService.getItemDetail(item.getId());
            items = null;
            if (msg == null) {
                msg = "更新成功!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "图片上传发生异常!";
        }
        return SUCCESS;
    }

    public String itemSearch() {
        try {
            shopList = shopService.getAllShops();
            if (queryBo == null) {
                queryBo = new ItemQueryBo();
            }
            page = itemService.searchItems(queryBo, page);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "数据库异常!";
        }
        return SUCCESS;
    }
}
