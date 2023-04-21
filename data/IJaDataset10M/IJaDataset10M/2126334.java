package com.michael.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.michael.CacheService;
import com.michael.common.BaseController;
import com.michael.model.Area;
import com.michael.model.Category;
import com.michael.model.Cids;
import com.michael.model.ParamModel;
import com.michael.model.Result;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.TaobaokeItem;
import com.taobao.api.domain.TaobaokeShop;
import com.taobao.api.request.TaobaokeItemsGetRequest;
import com.taobao.api.request.TaobaokeShopsGetRequest;
import com.taobao.api.response.TaobaokeShopsGetResponse;

/**
 * 首页
 * 
 * @author Administrator
 * 
 */
@Controller
public class ShopController extends BaseController {

    @Autowired
    private Category category;

    @Autowired
    private Area area;

    @Autowired
    private Cids cids;

    @Autowired
    private CacheService cacheService;

    @RequestMapping("/index.jhtml")
    public String index(HttpServletRequest request, Model model, ParamModel paramModel) throws Exception {
        model.addAttribute("category", category);
        TaobaokeItemsGetRequest req = new TaobaokeItemsGetRequest();
        req.setNick(nickname);
        req.setFields("title,pic_url,price,click_url");
        req.setPageNo(1L);
        model.addAttribute("activities", cids.getActivities());
        req.setKeyword("1折");
        req.setPageSize(5L);
        model.addAttribute("hd", cacheService.getTaobaokeItems(req).getObject());
        Map<String, List<TaobaokeItem>> items = new LinkedHashMap<String, List<TaobaokeItem>>();
        Map<String, String> cmap = category.getCategory();
        for (Iterator<String> iterator = cmap.keySet().iterator(); iterator.hasNext(); ) {
            String cid = iterator.next();
            String name = cmap.get(cid);
            req.setKeyword("5折 " + name);
            req.setPageSize(3L);
            List<TaobaokeItem> list = cacheService.getTaobaokeItems(req).getObject();
            req.setKeyword("8折 " + name);
            req.setPageSize(3L);
            if (list == null) {
                list = new ArrayList<TaobaokeItem>();
            }
            List itemList = cacheService.getTaobaokeItems(req).getObject();
            if (itemList != null) list.addAll(itemList);
            items.put(cid, list);
        }
        model.addAttribute("items", items);
        TaobaokeShopsGetRequest req2 = new TaobaokeShopsGetRequest();
        req2.setFields("click_url,shop_title,seller_credit");
        req2.setNick(nickname);
        req2.setKeyword(DZ);
        req2.setStartCredit("1crown");
        req2.setEndCredit("5goldencrown");
        req2.setStartCommissionrate("100");
        req2.setEndCommissionrate("3000");
        req2.setStartTotalaction("1");
        req2.setPageNo(1L);
        req2.setPageSize(7L);
        model.addAttribute("shops", cacheService.getTaobaokeShops(req2).getObject());
        req.setFields("title,click_url,pic_url,price,volume");
        req.setKeyword(DZ);
        req.setPageSize(12L);
        req.setSort("commissionNum_desc");
        model.addAttribute("rx", cacheService.getTaobaokeItems(req).getObject());
        return "index";
    }

    @RequestMapping("/q.jhtml")
    public String search(HttpServletRequest request, Model model, ParamModel paramModel) throws Exception {
        model.addAttribute("category", category);
        TaobaokeItemsGetRequest req = new TaobaokeItemsGetRequest();
        req.setNick(nickname);
        req.setFields("title,pic_url,price,click_url");
        req.setSort("commissionRate_desc");
        String name = paramModel.getKeywords();
        req.setKeyword(DZ + name);
        if (paramModel.getType() == 2) {
            req.setFields("shop_title,click_url");
            TaobaokeShopsGetRequest req2 = new TaobaokeShopsGetRequest();
            req2.setStartCredit("5heart");
            req2.setEndCredit("5goldencrown");
            req2.setPageNo(paramModel.getPage());
            req2.setPageSize(21L);
            req2.setNick(nickname);
            Result result = cacheService.getTaobaokeShops(req2);
            paramModel.setDataCount(result.getCount());
            model.addAttribute("items", result.getObject());
        } else {
            req.setPageNo(paramModel.getPage());
            req.setPageSize(21L);
            Result result = cacheService.getTaobaokeItems(req);
            paramModel.setDataCount(result.getCount());
            model.addAttribute("items", result.getObject());
        }
        model.addAttribute(PAGE, paramModel);
        paramModel.setPerPageSize(21);
        paramModel.calculate();
        req.setFields("title,pic_url,price,click_url,seller_credit_score");
        req.setPageNo(1L);
        req.setPageSize(8L);
        req.setSort("commissionNum_desc");
        req.setStartCredit("1crown");
        req.setEndCredit("5goldencrown");
        model.addAttribute("rm", cacheService.getTaobaokeItems(req).getObject());
        return "search";
    }

    @RequestMapping(value = "/shop.jhtml", params = "cid")
    public String second(HttpServletRequest request, Model model, HttpSession session, ParamModel param) throws Exception {
        model.addAttribute("category", category);
        model.addAttribute("area", area.getAreas());
        String cname = null;
        cname = category.getCategory().get(param.getCid());
        if (param.getCid3() != null) cname = category.getCategory3().get(param.getCid2()).get(param.getCid3()); else if (param.getCid2() != null) cname = category.getCategory2().get(param.getCid()).get(param.getCid2());
        String nz = request.getParameter("nzhe");
        String dzKeywords = null;
        if (nz == null || nz.equals("")) {
            dzKeywords = DZ;
        } else {
            dzKeywords = nz + "折 ";
        }
        TaobaokeItemsGetRequest req = new TaobaokeItemsGetRequest();
        req.setNick(nickname);
        req.setFields("title,pic_url,price,click_url");
        req.setSort("commissionRate_desc");
        String cid = (String) cids.getCidMap().get(cname);
        if (cid != null) {
            logger.info("cid=" + cid);
            req.setCid(Long.parseLong(cid));
            if (param.getKeywords() != null) {
                req.setKeyword(dzKeywords + param.getKeywords());
            } else {
                req.setKeyword(dzKeywords);
            }
        } else {
            if (param.getKeywords() == null) req.setKeyword(dzKeywords + cname); else req.setKeyword(dzKeywords + cname + " " + param.getKeywords());
        }
        int by = param.getBy();
        switch(by) {
            case 1:
                req.setSort("price_asc");
                break;
            case 2:
                req.setSort("price_desc");
                break;
            case 3:
                req.setSort("commissionNum_desc");
                break;
            case 4:
                req.setSort("commissionNum_asc");
                break;
            case 5:
                req.setSort("commissionRate_desc");
                break;
            default:
                break;
        }
        if (param.getArea() != null && !param.getArea().equals("")) {
            req.setArea(param.getArea());
        }
        int sprice = param.getSprice();
        int eprice = param.getEprice();
        if (sprice > 0) {
            req.setStartPrice(String.valueOf(sprice));
            if (eprice > sprice) req.setEndPrice(String.valueOf(eprice)); else req.setEndPrice("99999");
        }
        req.setPageNo(param.getPage());
        req.setPageSize(21L);
        Result result = cacheService.getTaobaokeItems(req);
        param.setDataCount(result.getCount());
        param.calculate();
        model.addAttribute("items", result.getObject()).addAttribute(PAGE, param);
        req.setFields("title,pic_url,price,click_url,volume,seller_credit_score");
        req.setPageNo(1L);
        req.setPageSize(8L);
        req.setSort("commissionNum_desc");
        req.setStartCredit("1crown");
        req.setEndCredit("5goldencrown");
        result = cacheService.getTaobaokeItems(req);
        model.addAttribute("rm", result.getObject());
        return "second";
    }

    @RequestMapping("/huodong.jhtml")
    public String ms(HttpServletRequest request, Model model, ParamModel paramModel) throws Exception {
        model.addAttribute("category", category);
        model.addAttribute("items", cids.getActivities());
        return "morems";
    }

    @RequestMapping("/1zhe.jhtml")
    public String hd(HttpServletRequest request, Model model, ParamModel param) throws Exception {
        model.addAttribute("category", category);
        model.addAttribute("cids", cids.getZheCidMap());
        TaobaokeItemsGetRequest req = new TaobaokeItemsGetRequest();
        req.setNick(nickname);
        req.setFields("title,pic_url,price,click_url");
        req.setPageNo(param.getPage());
        req.setSort("commissionRate_desc");
        if (param.getCid2() == null) req.setCid(Long.parseLong((String) cids.getZheCidMap().keySet().iterator().next())); else req.setCid(Long.parseLong(param.getCid2()));
        req.setKeyword("1折");
        req.setPageSize(28L);
        Result result = cacheService.getTaobaokeItems(req);
        param.setDataCount(result.getCount());
        param.setPerPageSize(28);
        param.calculate();
        model.addAttribute("items", result.getObject()).addAttribute(PAGE, param);
        return "more1zhe";
    }

    @RequestMapping("/huangguan_remai.jhtml")
    public String hgrm(HttpServletRequest request, Model model, ParamModel param) throws Exception {
        model.addAttribute("category", category);
        TaobaokeItemsGetRequest req = new TaobaokeItemsGetRequest();
        req.setNick(nickname);
        req.setFields("title,pic_url,price,click_url,seller_credit_score");
        String cname = null;
        if (param.getKeywords() != null) cname = param.getKeywords(); else {
            cname = category.getCategory().get(param.getCid());
            if (param.getCid3() != null) cname = category.getCategory3().get(param.getCid2()).get(param.getCid3()); else if (param.getCid2() != null) cname = category.getCategory2().get(param.getCid()).get(param.getCid2());
        }
        req.setSort("commissionRate_desc");
        String cid = (String) cids.getCidMap().get(cname);
        if (cid != null) {
            logger.info("cid=" + cid);
            req.setCid(Long.parseLong(cid));
            req.setKeyword(DZ);
        } else {
            req.setKeyword(DZ + cname);
        }
        req.setPageNo(param.getPage());
        req.setStartCredit("1crown");
        req.setEndCredit("5goldencrown");
        req.setPageSize(30L);
        Result result = cacheService.getTaobaokeItems(req);
        param.setDataCount(result.getCount());
        param.setPerPageSize(30);
        param.calculate();
        model.addAttribute("items", result.getObject()).addAttribute(PAGE, param);
        return "more_hgrm";
    }

    @RequestMapping("/huangguan_shop.jhtml")
    public String huangguanShop(HttpServletRequest request, Model model, ParamModel param) throws Exception {
        model.addAttribute("category", category);
        model.addAttribute("cids", cids.getShopCidMap());
        TaobaokeShopsGetRequest req2 = new TaobaokeShopsGetRequest();
        req2.setNick(nickname);
        req2.setFields("click_url,shop_title,seller_credit");
        if (param.getCid2() == null) req2.setCid(Long.parseLong((String) cids.getShopCidMap().keySet().iterator().next())); else req2.setCid(Long.parseLong(param.getCid2()));
        req2.setStartCredit("1crown");
        req2.setEndCredit("5goldencrown");
        req2.setPageNo(param.getPage());
        req2.setPageSize(21L);
        Result result = cacheService.getTaobaokeShops(req2);
        param.setDataCount(result.getCount());
        param.setPerPageSize(21);
        param.calculate();
        model.addAttribute("shops", result.getObject()).addAttribute(PAGE, param);
        return "huangguan_shop";
    }

    public static void main(String[] args) throws Exception {
        TaobaoClient client = new DefaultTaobaoClient(CacheService.url, CacheService.appkey, CacheService.secret);
        TaobaokeShopsGetRequest req2 = new TaobaokeShopsGetRequest();
        req2.setFields("click_url,shop_title,seller_credit");
        req2.setNick(nickname);
        req2.setCid(1056L);
        req2.setPageNo(1L);
        req2.setPageSize(6L);
        TaobaokeShopsGetResponse response2 = client.execute(req2);
        List<TaobaokeShop> ss = response2.getTaobaokeShops();
        for (TaobaokeShop taobaokeShop : ss) {
            System.out.println(taobaokeShop.getShopTitle());
        }
    }
}
