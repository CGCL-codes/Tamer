package com.coyousoft.wangyu.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Map;
import org.sysolar.sun.mvc.support.Json;
import org.sysolar.sun.mvc.support.RequestWrapper;

/**
 * USER_URL_CAT : 用户网址类别
 */
public final class UserUrlCat {

    public static final int CREATE_TYPE_USER = 1;

    public static final int CREATE_TYPE_DEF = 2;

    public static final int SHOW_TYPE_PRIVATE = 1;

    public static final int SHOW_TYPE_FRIENDLY = 2;

    public static final int SHOW_TYPE_PUBLIC = 3;

    public static final int COL_NUM_0 = 0;

    public static final int COL_NUM_1 = 1;

    public static final int COL_NUM_2 = 2;

    private Integer catId;

    private Integer userId;

    private String catName;

    private Integer catCreateType;

    private Integer catShowType;

    private Integer catColNum;

    private Integer catOrder;

    private Integer urlCount;

    private Date catCdate;

    private Date catUdate;

    private WangyuUser wangyuUser;

    private List<UserUrl> userUrlList;

    /**
     * 设定默认值
     */
    public UserUrlCat setDefault() {
        Date date = new Date();
        if (null == catCreateType) {
            catCreateType = 1;
        }
        if (null == catShowType) {
            catShowType = 3;
        }
        if (null == catColNum) {
            catColNum = 0;
        }
        if (null == catOrder) {
            catOrder = 0;
        }
        if (null == urlCount) {
            urlCount = 0;
        }
        if (null == catCdate) {
            catCdate = date;
        }
        return this;
    }

    public UserUrlCat add(UserUrl userUrl) {
        if (null == userUrlList) {
            userUrlList = new ArrayList<UserUrl>(30);
        }
        userUrlList.add(userUrl);
        return this;
    }

    public UserUrlCat setUserUrlList(List<UserUrl> userUrlList) {
        this.userUrlList = userUrlList;
        return this;
    }

    public List<UserUrl> getUserUrlList() {
        return userUrlList;
    }

    public UserUrlCat setCatId(Integer catId) {
        this.catId = catId;
        return this;
    }

    public UserUrlCat setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public UserUrlCat setCatName(String catName) {
        this.catName = catName;
        return this;
    }

    public UserUrlCat setCatCreateType(Integer catCreateType) {
        this.catCreateType = catCreateType;
        return this;
    }

    public UserUrlCat setCatShowType(Integer catShowType) {
        this.catShowType = catShowType;
        return this;
    }

    public UserUrlCat setCatColNum(Integer catColNum) {
        this.catColNum = catColNum;
        return this;
    }

    public UserUrlCat setCatOrder(Integer catOrder) {
        this.catOrder = catOrder;
        return this;
    }

    public UserUrlCat setUrlCount(Integer urlCount) {
        this.urlCount = urlCount;
        return this;
    }

    public UserUrlCat setCatCdate(Date catCdate) {
        this.catCdate = catCdate;
        return this;
    }

    public UserUrlCat setCatUdate(Date catUdate) {
        this.catUdate = catUdate;
        return this;
    }

    public UserUrlCat setWangyuUser(WangyuUser wangyuUser) {
        this.wangyuUser = wangyuUser;
        return this;
    }

    public Integer getCatId() {
        return catId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getCatName() {
        return catName;
    }

    public Integer getCatCreateType() {
        return catCreateType;
    }

    public Integer getCatShowType() {
        return catShowType;
    }

    public Integer getCatColNum() {
        return catColNum;
    }

    public Integer getCatOrder() {
        return catOrder;
    }

    public Integer getUrlCount() {
        return urlCount;
    }

    public Date getCatCdate() {
        return catCdate;
    }

    public Date getCatUdate() {
        return catUdate;
    }

    public WangyuUser getWangyuUser() {
        return wangyuUser;
    }

    /**
     * 用查询结果 Map 给实体属性赋值，包含对父表实体的创建、赋值。
     */
    public UserUrlCat fill(Map<String, Object> row, boolean fillWangyuUser) {
        if (fillWangyuUser) {
            wangyuUser = new WangyuUser().fill(row);
        }
        return this.fill(row);
    }

    /**
     * 用查询结果 Map 给实体属性赋值。
     */
    public UserUrlCat fill(Map<String, Object> row) {
        Object obj = null;
        if (null != (obj = row.get("CAT_ID"))) {
            catId = ((Number) obj).intValue();
        }
        if (null != (obj = row.get("USER_ID"))) {
            userId = ((Number) obj).intValue();
        }
        if (null != (obj = row.get("CAT_NAME"))) {
            catName = (String) obj;
        }
        if (null != (obj = row.get("CAT_CREATE_TYPE"))) {
            catCreateType = ((Number) obj).intValue();
        }
        if (null != (obj = row.get("CAT_SHOW_TYPE"))) {
            catShowType = ((Number) obj).intValue();
        }
        if (null != (obj = row.get("CAT_COL_NUM"))) {
            catColNum = ((Number) obj).intValue();
        }
        if (null != (obj = row.get("CAT_ORDER"))) {
            catOrder = ((Number) obj).intValue();
        }
        if (null != (obj = row.get("URL_COUNT"))) {
            urlCount = ((Number) obj).intValue();
        }
        if (null != (obj = row.get("CAT_CDATE"))) {
            catCdate = (Date) obj;
        }
        if (null != (obj = row.get("CAT_UDATE"))) {
            catUdate = (Date) obj;
        }
        return this;
    }

    /**
     * 用 request 参数给实体属性赋值。
     */
    public UserUrlCat fill(RequestWrapper wrapper) throws Exception {
        catId = wrapper.getInteger("catId");
        userId = wrapper.getInteger("userId");
        catName = wrapper.getString("catName");
        catCreateType = wrapper.getInteger("catCreateType");
        catShowType = wrapper.getInteger("catShowType");
        catColNum = wrapper.getInteger("catColNum");
        catOrder = wrapper.getInteger("catOrder");
        urlCount = wrapper.getInteger("urlCount");
        catCdate = wrapper.getDate("catCdate");
        catUdate = wrapper.getDate("catUdate");
        return this;
    }

    /**
     * 用 json 格式字符串给实体属性赋值。
     */
    public UserUrlCat fillJson(String json) {
        return this.fillJson(Json.toJavaMap(json));
    }

    /**
     * 用 json 格式字符串转换后的 Map 给实体属性赋值。
     */
    @SuppressWarnings("unchecked")
    public UserUrlCat fillJson(Map<String, Object> json) {
        Object value = null;
        if (null != (value = json.get("catId"))) {
            catId = Integer.valueOf((String) value);
        }
        if (null != (value = json.get("userId"))) {
            userId = Integer.valueOf((String) value);
        }
        if (null != (value = json.get("catName"))) {
            catName = (String) value;
        }
        if (null != (value = json.get("catCreateType"))) {
            catCreateType = Integer.valueOf((String) value);
        }
        if (null != (value = json.get("catShowType"))) {
            catShowType = Integer.valueOf((String) value);
        }
        if (null != (value = json.get("catColNum"))) {
            catColNum = Integer.valueOf((String) value);
        }
        if (null != (value = json.get("catOrder"))) {
            catOrder = Integer.valueOf((String) value);
        }
        if (null != (value = json.get("urlCount"))) {
            urlCount = Integer.valueOf((String) value);
        }
        if (null != (value = json.get("catCdate"))) {
            catCdate = new Date(Long.valueOf((String) value));
        }
        if (null != (value = json.get("catUdate"))) {
            catUdate = new Date(Long.valueOf((String) value));
        }
        if (null != (value = json.get("wangyuUser"))) {
            wangyuUser = new WangyuUser().fillJson((Map<String, Object>) value);
        }
        if (null != (value = json.get("userUrlList"))) {
            for (Object obj : (List<Object>) value) {
                this.add(new UserUrl().fill((Map<String, Object>) obj));
            }
        }
        return this;
    }

    /**
     * 深度克隆实体。
     */
    public UserUrlCat clone() {
        UserUrlCat e = new UserUrlCat();
        e.catId = this.catId;
        e.userId = this.userId;
        e.catName = this.catName;
        e.catCreateType = this.catCreateType;
        e.catShowType = this.catShowType;
        e.catColNum = this.catColNum;
        e.catOrder = this.catOrder;
        e.urlCount = this.urlCount;
        e.catCdate = this.catCdate;
        e.catUdate = this.catUdate;
        if (null != this.wangyuUser) {
            e.wangyuUser = this.wangyuUser.clone();
        }
        if (null != this.userUrlList) {
            for (UserUrl userUrl : this.userUrlList) {
                e.add(userUrl.clone());
            }
        }
        return e;
    }

    /**
     * 返回 json 格式字符串。
     */
    public String toString() {
        StringBuilder buffer = new StringBuilder(512).append("{");
        if (null != catId) {
            buffer.append("catId:").append(catId).append(",");
        }
        if (null != userId) {
            buffer.append("userId:").append(userId).append(",");
        }
        if (null != catName) {
            buffer.append("catName:'").append(catName).append("',");
        }
        if (null != catCreateType) {
            buffer.append("catCreateType:").append(catCreateType).append(",");
        }
        if (null != catShowType) {
            buffer.append("catShowType:").append(catShowType).append(",");
        }
        if (null != catColNum) {
            buffer.append("catColNum:").append(catColNum).append(",");
        }
        if (null != catOrder) {
            buffer.append("catOrder:").append(catOrder).append(",");
        }
        if (null != urlCount) {
            buffer.append("urlCount:").append(urlCount).append(",");
        }
        if (null != catCdate) {
            buffer.append("catCdate:").append(catCdate.getTime()).append(",");
        }
        if (null != catUdate) {
            buffer.append("catUdate:").append(catUdate.getTime()).append(",");
        }
        if (null != wangyuUser) {
            buffer.append("wangyuUser:").append(wangyuUser).append(",");
        }
        if (null != userUrlList) {
            buffer.append("userUrlList:").append(Json.encode(userUrlList)).append(",");
        }
        if (buffer.length() > 1) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        return buffer.append("}").toString();
    }

    /**
     * 返回实体 bean 全部字段值组成的数组（即包含null值），字段值顺序同insert语句里的参数顺序。
     */
    public Object[] toArray() {
        return new Object[] { this.getCatId(), this.getUserId(), this.getCatName(), this.getCatCreateType(), this.getCatShowType(), this.getCatColNum(), this.getCatOrder(), this.getUrlCount(), this.getCatCdate(), this.getCatUdate() };
    }
}
