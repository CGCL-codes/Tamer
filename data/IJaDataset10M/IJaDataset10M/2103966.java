package cn.poco.bean;

import java.io.Serializable;

/**
 * @author jack 保存餐厅简略信息
 */
public class Restaurant implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 餐厅总数 **/
	private int total;
	/** 餐厅ID **/
	private Integer id;
	/** 餐厅名 **/
	private String title;
	/** 没作用，暂不处理 **/
	private String card;
	/****/
	private String cardInfor;
	/** 地址 **/
	private String address;
	/** 是否加盟餐厅（0,1） **/
	private Integer vouch;
	/** 平均消费 **/
	private String average;
	/** 经纬度 **/
	private String location;
	/** 餐厅所属区域 **/
	private String area;
	/** 餐厅星级 **/
	private String star;
	/** 菜式风味 **/
	private String dish;
	/** 推荐菜式 **/
	private String maindish;
	/** 联系电话 **/
	private String tel;
	/** 餐厅介绍 **/
	private String intro;
	/** POCO卡折扣 **/
	private int discount;
	/** 特别推荐 (餐厅推荐信息) **/
	private String recomment;
	/** 餐厅食评数 **/
	private Integer comment;
	/** 该餐厅美食作品数目 **/
	private Integer blog;
	/** 餐厅距离 **/
	private String distance;
	/** 是否百事加盟 **/
	private Integer pepsi;

	private String seat;
	private String park;
	private String opening;
	private String traffic;

	/** 定义check标记 */
	private boolean visibility;
	private boolean checked;
	
	
	
	public boolean isVisibility() {
		return visibility;
	}

	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getCardInfor() {
		return cardInfor;
	}

	public void setCardInfor(String cardInfor) {
		this.cardInfor = cardInfor;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getVouch() {
		return vouch;
	}

	public void setVouch(Integer vouch) {
		this.vouch = vouch;
	}

	public String getAverage() {
		return average;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getDish() {
		return dish;
	}

	public void setDish(String dish) {
		this.dish = dish;
	}

	public String getMaindish() {
		return maindish;
	}

	public void setMaindish(String maindish) {
		this.maindish = maindish;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public String getRecomment() {
		return recomment;
	}

	public void setRecomment(String recomment) {
		this.recomment = recomment;
	}

	public Integer getComment() {
		return comment;
	}

	public void setComment(Integer comment) {
		this.comment = comment;
	}

	public Integer getBlog() {
		return blog;
	}

	public void setBlog(Integer blog) {
		this.blog = blog;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public Integer getPepsi() {
		return pepsi;
	}

	public void setPepsi(Integer pepsi) {
		this.pepsi = pepsi;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getPark() {
		return park;
	}

	public void setPark(String park) {
		this.park = park;
	}

	public String getOpening() {
		return opening;
	}

	public void setOpening(String opening) {
		this.opening = opening;
	}

	public String getTraffic() {
		return traffic;
	}

	public void setTraffic(String traffic) {
		this.traffic = traffic;
	}

	@Override
	public String toString() {
		return "Restaurant [total=" + total + ", id=" + id + ", title=" + title + ", card=" + card + ",  cardinfor=" + cardInfor + ",address="
				+ address + ", vouch=" + vouch + ", average=" + average + ", location=" + location + ", area=" + area
				+ ", star=" + star + ", dish=" + dish + ", maindish=" + maindish + ", tel=" + tel + ", intro=" + intro
				+ ", discount=" + discount + ", recomment=" + recomment + ", comment=" + comment + ", blog=" + blog
				+ ", distance=" + distance + ", pepsi=" + pepsi + ", seat=" + seat + ", park=" + park + ", opening="
				+ opening + ", traffic=" + traffic + ", checked=" + checked + "]";
	}

}
