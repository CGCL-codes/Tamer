package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TaobaokeVirtualcardGetResponse;
import com.taobao.api.ApiRuleException;

/**
 * TOP API: taobao.taobaoke.virtualcard.get request
 * 
 * @author auto create
 * @since 1.0, 2011-10-24 16:00:43
 */
public class TaobaokeVirtualcardGetRequest implements TaobaoRequest<TaobaokeVirtualcardGetResponse> {

    private TaobaoHashMap udfParams;

    private Long timestamp;

    /** 
	* 电话卡地区目前只有如下地区支持:
浙江
				上海
				北京
				广东
				江苏
				山东
				福建
				辽宁
				河南
				四川
				湖北
				天津
				湖南
				河北
				重庆
				云南
				新疆
				西藏
				青海
				宁夏
				内蒙
				江西
				吉林
				黑龙江
				海南
				贵州
				广西
				甘肃
				安徽
				陕西
				山西
	 */
    private String area;

    /** 
	* 电话卡或游戏点卡标记.
电话卡：phoneCard，游戏卡：gameCard

注意:如果是电话卡查询,则 area,operator,price,card_type都是必须的参数. 
如果是游戏卡查询,则game_name,price,card_type是必须参数
	 */
    private String bizType;

    /** 
	* 电话充值卡类型.
卖家代充：autoship，自动发货：autopost，10分钟代充：10minship
	 */
    private String cardType;

    /** 
	* 需返回的字段列表.可选值:num_iid,title,nick,pic_url,price,click_url,commission,ommission_rate,commission_num,commission_volume,shop_click_url,seller_credit_score,item_location,volume ;字段之间用","分隔
	 */
    private String fields;

    /** 
	* 游戏名,目前只支持如下游戏:
魔兽世界
				永恒之塔
				奇迹世界
				征途
				热血传奇
				传奇世界
				梦幻西游
				大话西游
				大唐豪侠
				天龙八部
				完美世界
				魔域
				诛仙
				热血江湖
				问道
				劲舞团
				完美世界国际版
				剑侠世界
				武林外传
				剑情网络版
				剑侠情缘
				封神榜
				QQ幻想
				泡泡堂
				冒险岛
				水浒Q传
				彩虹岛
				街头篮球
				跑跑卡丁车
				三国群英传
				惊天动地
				超级舞者
				梦幻国度
				天堂
				风云
				卓越之剑
				华夏
				联众世界
				浩方
				春秋Q传
				刀剑英雄
				新英雄年代
				信长之野望
				热舞派对
				赤壁
				大话西游外传
				SD敢达
				穿越火线
				QQ自由幻想
				QQ三国
				QQ华夏
				传奇外传
				封神榜
				地下城与勇士
	 */
    private String gameName;

    /** 
	* 淘宝用户昵称，注：指的是淘宝的会员登录名.如果昵称错误,那么客户就收不到佣金.每个淘宝昵称都对应于一个pid，在这里输入要结算佣金的淘宝昵称，当推广的商品成功后，佣金会打入此输入的淘宝昵称的账户。具体的信息可以登入阿里妈妈的网站查看
	 */
    private String nick;

    /** 
	* 电话卡运营商.
1:移动 2:联通 3:电信
	 */
    private String operator;

    /** 
	* 自定义输入串.格式:英文和数字组成;长度不能大于12个字符,区分不同的推广渠道,如:bbs,表示bbs为推广渠道;blog,表示blog为推广渠道.
	 */
    private String outerCode;

    /** 
	* 结果页数.1~99
	 */
    private Long pageNo;

    /** 
	* 每页返回结果数.最大每页40
	 */
    private Long pageSize;

    /** 
	* 淘客id
	 */
    private String pid;

    /** 
	* 充值卡面值.目前只支持如下面值:
  1,
        10, 
        20, 
        30, 
        50, 
        100,
        200,
        300,
        500, 
        1000
	 */
    private String price;

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return this.area;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizType() {
        return this.bizType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardType() {
        return this.cardType;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getFields() {
        return this.fields;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return this.gameName;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return this.nick;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOuterCode(String outerCode) {
        this.outerCode = outerCode;
    }

    public String getOuterCode() {
        return this.outerCode;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageNo() {
        return this.pageNo;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageSize() {
        return this.pageSize;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPid() {
        return this.pid;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return this.price;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getApiMethodName() {
        return "taobao.taobaoke.virtualcard.get";
    }

    public Map<String, String> getTextParams() {
        TaobaoHashMap txtParams = new TaobaoHashMap();
        txtParams.put("area", this.area);
        txtParams.put("biz_type", this.bizType);
        txtParams.put("card_type", this.cardType);
        txtParams.put("fields", this.fields);
        txtParams.put("game_name", this.gameName);
        txtParams.put("nick", this.nick);
        txtParams.put("operator", this.operator);
        txtParams.put("outer_code", this.outerCode);
        txtParams.put("page_no", this.pageNo);
        txtParams.put("page_size", this.pageSize);
        txtParams.put("pid", this.pid);
        txtParams.put("price", this.price);
        if (udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        return txtParams;
    }

    public void putOtherTextParam(String key, String value) {
        if (this.udfParams == null) {
            this.udfParams = new TaobaoHashMap();
        }
        this.udfParams.put(key, value);
    }

    public Class<TaobaokeVirtualcardGetResponse> getResponseClass() {
        return TaobaokeVirtualcardGetResponse.class;
    }

    public void check() throws ApiRuleException {
        RequestCheckUtils.checkNotEmpty(bizType, "bizType");
        RequestCheckUtils.checkNotEmpty(cardType, "cardType");
        RequestCheckUtils.checkNotEmpty(fields, "fields");
    }
}
