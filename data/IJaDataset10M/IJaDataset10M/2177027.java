package com.wangyu001.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.sysolar.sun.mvc.support.BaseBean;
import org.sysolar.sun.mvc.support.Json;
import org.sysolar.sun.mvc.support.RequestWrapper;

/**
 * 热门收藏里的网址分类，支持二级分类。
 */
public final class HotUrlCat extends BaseBean implements Comparable<HotUrlCat> {

    private Long catId;

    private String catName;

    private Long catOrder;

    private Long topCatId;

    private Long adminId;

    private Date catCdate;

    private Date catUdate;

    private List<HotUrlCat> subCatList = new ArrayList<HotUrlCat>();

    public List<HotUrlCat> getSubCatList() {
        return subCatList;
    }

    public void setSubCatList(List<HotUrlCat> subCatList) {
        this.subCatList = subCatList;
    }

    public HotUrlCat() {
    }

    public HotUrlCat setCatId(Long catId) {
        this.catId = catId;
        return this;
    }

    public Long getCatId() {
        return this.catId;
    }

    public HotUrlCat setCatName(String catName) {
        this.catName = catName;
        return this;
    }

    public String getCatName() {
        return this.catName;
    }

    public HotUrlCat setCatOrder(Long catOrder) {
        this.catOrder = catOrder;
        return this;
    }

    public Long getCatOrder() {
        return this.catOrder;
    }

    public HotUrlCat setTopCatId(Long topCatId) {
        this.topCatId = topCatId;
        return this;
    }

    public Long getTopCatId() {
        return this.topCatId;
    }

    public HotUrlCat setAdminId(Long adminId) {
        this.adminId = adminId;
        return this;
    }

    public Long getAdminId() {
        return this.adminId;
    }

    public HotUrlCat setCatCdate(Date catCdate) {
        this.catCdate = catCdate;
        return this;
    }

    public Date getCatCdate() {
        return this.catCdate;
    }

    public HotUrlCat setCatUdate(Date catUdate) {
        this.catUdate = catUdate;
        return this;
    }

    public Date getCatUdate() {
        return this.catUdate;
    }

    public HotUrlCat fill(Map<String, Object> row) {
        Object obj = null;
        if (null != (obj = row.get("CAT_ID"))) {
            catId = ((Number) obj).longValue();
        }
        if (null != (obj = row.get("CAT_NAME"))) {
            catName = (String) obj;
        }
        if (null != (obj = row.get("CAT_ORDER"))) {
            catOrder = ((Number) obj).longValue();
        }
        if (null != (obj = row.get("TOP_CAT_ID"))) {
            topCatId = ((Number) obj).longValue();
        }
        if (null != (obj = row.get("ADMIN_ID"))) {
            adminId = ((Number) obj).longValue();
        }
        if (null != (obj = row.get("CAT_CDATE"))) {
            catCdate = (Date) obj;
        }
        if (null != (obj = row.get("CAT_UDATE"))) {
            catUdate = (Date) obj;
        }
        return this;
    }

    public HotUrlCat fillJson(Map<String, String> json) {
        String value = null;
        if (null != (value = json.get("catId"))) {
            catId = Long.valueOf(value);
        }
        if (null != (value = json.get("catName"))) {
            catName = value;
        }
        if (null != (value = json.get("catOrder"))) {
            catOrder = Long.valueOf(value);
        }
        if (null != (value = json.get("topCatId"))) {
            topCatId = Long.valueOf(value);
        }
        if (null != (value = json.get("adminId"))) {
            adminId = Long.valueOf(value);
        }
        if (null != (value = json.get("catCdate"))) {
            catCdate = new Date(Long.valueOf(value));
        }
        if (null != (value = json.get("catUdate"))) {
            catUdate = new Date(Long.valueOf(value));
        }
        return this;
    }

    public HotUrlCat fill(RequestWrapper wrapper) throws Exception {
        catId = wrapper.getLong("catId");
        catName = wrapper.getString("catName");
        catOrder = wrapper.getLong("catOrder");
        topCatId = wrapper.getLong("topCatId");
        adminId = wrapper.getLong("adminId");
        catCdate = wrapper.getDate("catCdate");
        catUdate = wrapper.getDate("catUdate");
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(512).append("{");
        if (null != catId) {
            sb.append("catId:").append(catId).append(",");
        }
        if (null != catName) {
            sb.append("catName:'").append(catName).append("',");
        }
        if (null != catOrder) {
            sb.append("catOrder:").append(catOrder).append(",");
        }
        if (null != topCatId) {
            sb.append("topCatId:").append(topCatId).append(",");
        }
        if (null != adminId) {
            sb.append("adminId:").append(adminId).append(",");
        }
        if (null != catCdate) {
            sb.append("catCdate:").append(catCdate.getTime()).append(",");
        }
        if (null != catUdate) {
            sb.append("catUdate:").append(catUdate.getTime()).append(",");
        }
        if (null != subCatList) {
            sb.append("subCatList:").append(Json.toJsArray(subCatList)).append(",");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}").trimToSize();
        return sb.toString();
    }

    public int compareTo(HotUrlCat hotUrlCat) {
        return (int) (this.catOrder - hotUrlCat.getCatOrder());
    }
}
