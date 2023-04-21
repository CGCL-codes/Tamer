
package cn.nkjobsearch.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class Config
 * 配置信息保存类
 * */
public class Config {
	/**
	 * 是否输出自定义的调试信息
	 */
	public static final boolean DEBUG_INFO = true;

	/** 
	 * 是否输出系统的错误信息
	 */
	public static final boolean DEBUG_ERROR = true;

	/** 
	 * MYSQL连接包的名字
	 * */
	public static final String MYSQL_sDBDriver = "com.mysql.jdbc.Driver";

	/**
	 * MYSQL数据的地址
	 * */
	public static final String MYSQL_sConnStr 
		= "jdbc:mysql://192.168.200.2:3306/job?useUnicode=yes&characterEncoding=utf8";

	/**
	 * MYSQL数据的用户名
	 * */
	public static final String MYSQL_user = "root";

	/**
	 * MYSQL数据的密码
	 * */
	public static final String MYSQL_password = "";

//	/**
//	 * 匹配链接的正则表达式
//	 * */
//	public static final String URL_PATTERN_STRING 
//		= "[\\s]+href\\s*=\\s*(('[^(\'|#)]*')|(\"[^(\"|#)]*\")|([^(\\s|>|\\\\|#)]+))";
	
	/**
	 * ChinaHR职位分类的内部编号
	 * */
	public static final String CHINAHR_CATEGORY[] = { "100", "300", "400",
			"600", "604", "700", "702", "800", "900", "1200", "1700", "1900",
			"2000", "2100", "2500", "2700", "3300", "3400", "3420", "3500",
			"3600", "3800", "1013000", "1014000", "1016001", "1017000",
			"1020000", "1026000", "1033000", "1034000", "1035000", "1036000" };

	/**
	 * ChinaHR职位行业的内部编码
	 * */
	public static final String CHINAHR_INDUSTRY[] = { "100", "300", "400",
			"600", "700", "800", "900", "1000", "1100", "1200", "1300", "1400",
			"1500", "1600", "1700", "1800", "2000", "2100", "2200", "2300",
			"2400", "2600", "2800", "2900", "3100", "3200", "3600", "3700",
			"3900", "1105000", "1106000", "1113000", "1114000", "1115000",
			"1116000", "1118000", "1121000", "1122000", "1123000", "1124000" };

	/**
	 * ChinaHR省份的内部编码
	 * */
	public static final String CHINAHR_PROVINCE[] = { "30000", "31000",
			"32000", "33000", "1000", "2000", "3000", "4000", "5000", "6000",
			"7000", "8000", "9000", "10000", "11000", "12000", "13000",
			"14000", "15000", "16000", "17000", "18000", "19000", "20000",
			"21000", "22000", "23000", "24000", "25000", "26000", "27000",
			"34000", "35000", "36000" };

	/**
	 * 遍历ChinaHR搜索接口，省份的计数器
	 * */
	public static int CHINAHR_SEARCHLIST_P = 0;

	/**
	 * 遍历ChinaHR搜索接口，职位行业的计数器
	 * */
	public static int CHINAHR_SEARCHLIST_I = 0;

	/**
	 * 遍历ChinaHR搜索接口，职位分类的计数器
	 * */
	public static int CHINAHR_SEARCHLIST_C = 0;

	/**
	 * 遍历ChinaHR搜索接口，一个计时器
	 * */
	public static int CHINAHR_SEARCHLIST_TIMER = 0;

	/**
	 * 遍历ChinaHR搜索接口，每个页面允许的最大延时(秒)
	 * */
	public static final int CHINAHR_SEARCHLIST_DELAY_SECONDS = 10;

	/**
	 * ComChinaHRController是否应该退出的标志位
	 * */
	public static boolean CHINAHR_SEARCHLIST_RUNNING = true;

	/**
	 * 取ChinaHR内容页面的计时器
	 * */
	public static int CHINAHR_CONTENT_TIMER = 0;

	/**
	 * 取ChinaHR内容页面的每个页面允许的最大延时(秒)
	 * */
	public static final int CHINAHR_CONTENT_DELAY_SECONDS = 10;

	/**
	 * ComChinaHRController是否应该退出的标志位
	 * */
	public static boolean CHINAHR_CONTENT_RUNNING = true;
	
	/**
	 * 遍历ChinaHR搜索接口时，记录Province, Industry, Category的索引值
	 * */
	public final static String CHINAHR_SEARCHLIST_PIC_CONF_PATH = "conf/ChinaHR_PIC.conf";
	
	/**
	 * 遍历ChinaHR搜索接口时的Log文件的路径
	 * */
	public final static String CHINAHR_SEARCHLIST_LOG_PATH = "log/ChinaHRSearchListLog_"
			+ (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()) + ".ini";
	
	/**
	 * 智联招聘的省(市)名称
	 * */
	public final static String[] ZHILIAN_CITY = { "安徽省", "澳门特别行政区", "北京",
			"福建省", "甘肃省", "广东省", "广西壮族自治区", "贵州省", "海南省", "河北省", "河南省", "黑龙江省",
			"湖北省", "湖南省", "吉林省", "江苏省", "江西省", "辽宁省", "内蒙古自治区", "宁夏回族自治区",
			"青海省", "山东省", "山西省", "陕西省", "上海", "四川省", "台湾省", "天津", "西藏自治区",
			"香港特别行政区", "新疆维吾尔族自治区", "云南省", "浙江省", "重庆" };
	
	/**
	 * 智联招聘的省(市)名称(URL编码后的值)
	 * */
	public final static String[] ZHILIAN_CITY_ENCODE = {
			"%E5%AE%89%E5%BE%BD%E7%9C%81",
			"%E6%BE%B3%E9%97%A8%E7%89%B9%E5%88%AB%E8%A1%8C%E6%94%BF%E5%8C%BA",
			"%E5%8C%97%E4%BA%AC",
			"%E7%A6%8F%E5%BB%BA%E7%9C%81",
			"%E7%94%98%E8%82%83%E7%9C%81",
			"%E5%B9%BF%E4%B8%9C%E7%9C%81",
			"%E5%B9%BF%E8%A5%BF%E5%A3%AE%E6%97%8F%E8%87%AA%E6%B2%BB%E5%8C%BA",
			"%E8%B4%B5%E5%B7%9E%E7%9C%81",
			"%E6%B5%B7%E5%8D%97%E7%9C%81",
			"%E6%B2%B3%E5%8C%97%E7%9C%81",
			"%E6%B2%B3%E5%8D%97%E7%9C%81",
			"%E9%BB%91%E9%BE%99%E6%B1%9F%E7%9C%81",
			"%E6%B9%96%E5%8C%97%E7%9C%81",
			"%E6%B9%96%E5%8D%97%E7%9C%81",
			"%E5%90%89%E6%9E%97%E7%9C%81",
			"%E6%B1%9F%E8%8B%8F%E7%9C%81",
			"%E6%B1%9F%E8%A5%BF%E7%9C%81",
			"%E8%BE%BD%E5%AE%81%E7%9C%81",
			"%E5%86%85%E8%92%99%E5%8F%A4%E8%87%AA%E6%B2%BB%E5%8C%BA",
			"%E5%AE%81%E5%A4%8F%E5%9B%9E%E6%97%8F%E8%87%AA%E6%B2%BB%E5%8C%BA",
			"%E9%9D%92%E6%B5%B7%E7%9C%81",
			"%E5%B1%B1%E4%B8%9C%E7%9C%81",
			"%E5%B1%B1%E8%A5%BF%E7%9C%81",
			"%E9%99%95%E8%A5%BF%E7%9C%81",
			"%E4%B8%8A%E6%B5%B7",
			"%E5%9B%9B%E5%B7%9D%E7%9C%81",
			"%E5%8F%B0%E6%B9%BE%E7%9C%81",
			"%E5%A4%A9%E6%B4%A5",
			"%E8%A5%BF%E8%97%8F%E8%87%AA%E6%B2%BB%E5%8C%BA",
			"%E9%A6%99%E6%B8%AF%E7%89%B9%E5%88%AB%E8%A1%8C%E6%94%BF%E5%8C%BA",
			"%E6%96%B0%E7%96%86%E7%BB%B4%E5%90%BE%E5%B0%94%E6%97%8F%E8%87%AA%E6%B2%BB%E5%8C%BA",
			"%E4%BA%91%E5%8D%97%E7%9C%81", "%E6%B5%99%E6%B1%9F%E7%9C%81",
			"%E9%87%8D%E5%BA%86"};
	/**
	 * 智联招聘的职位类别
	 * */
	public final static String[] ZHILIAN_CATEGORY = { "销售", "市场/市场拓展/公关",
			"商务/采购/贸易", "计算机软、硬件/互联网/IT", "电子/半导体/仪表仪器", "通信技术", "客户服务/技术支持",
			"行政/后勤", "人力资源", "高级管理", "生产/加工/制造", "质控/安检", "工程机械", "技工",
			"财会/审计/统计", "金融/银行/保险/证券/投资", "建筑/房地产/装修/物业", "交通/仓储/物流",
			"普通劳动力/家政服务", "零售业", "教育/培训", "咨询/顾问", "学术/科研", "法律", "美术/设计/创意",
			"编辑/文案/传媒/影视/新闻", "酒店/餐饮/旅游/娱乐", "化工", "能源/矿产/地质勘查", "医疗/护理/保健/美容",
			"生物/制药/医疗器械", "翻译（口译与笔译）", "公务员", "环境科学/环保", "农/林/牧/渔业",
			"兼职/临时/培训生/储备干部", "在校学生", "其他" };

	public final static String[] ZHILIAN_CATEGORY_CODE = { "4010200", "4082000",
			"4083000", "160000", "160100", "5001000", "4000000", "3010000",
			"5002000", "1050000", "121100", "2023405", "5003000", "5004000",
			"2060000", "2070000", "140000", "4010300", "6270000", "5005000",
			"2090000", "2140000", "2010000", "2080000", "2100708", "2120000",
			"4040000", "120500", "130000", "2050000", "121300", "2120500",
			"200100", "2023100", "100000", "1", "5006000", "2" };
	
	/**
	 * 智联招聘的职位类别(URL编码后的值)
	 * */
	public final static String[] ZHILIAN_CATEGORY_ENCODE = {
			"%E9%94%80%E5%94%AE",
			"%E5%B8%82%E5%9C%BA%2F%E5%B8%82%E5%9C%BA%E6%8B%93%E5%B1%95%2F%E5%85%AC%E5%85%B3",
			"%E5%95%86%E5%8A%A1%2F%E9%87%87%E8%B4%AD%2F%E8%B4%B8%E6%98%93",
			"%E8%AE%A1%E7%AE%97%E6%9C%BA%E8%BD%AF%E3%80%81%E7%A1%AC%E4%BB%B6%2F%E4%BA%92%E8%81%94%E7%BD%91%2FIT",
			"%E7%94%B5%E5%AD%90%2F%E5%8D%8A%E5%AF%BC%E4%BD%93%2F%E4%BB%AA%E8%A1%A8%E4%BB%AA%E5%99%A8",
			"%E9%80%9A%E4%BF%A1%E6%8A%80%E6%9C%AF",
			"%E5%AE%A2%E6%88%B7%E6%9C%8D%E5%8A%A1%2F%E6%8A%80%E6%9C%AF%E6%94%AF%E6%8C%81",
			"%E8%A1%8C%E6%94%BF%2F%E5%90%8E%E5%8B%A4",
			"%E4%BA%BA%E5%8A%9B%E8%B5%84%E6%BA%90",
			"%E9%AB%98%E7%BA%A7%E7%AE%A1%E7%90%86",
			"%E7%94%9F%E4%BA%A7%2F%E5%8A%A0%E5%B7%A5%2F%E5%88%B6%E9%80%A0",
			"%E8%B4%A8%E6%8E%A7%2F%E5%AE%89%E6%A3%80",
			"%E5%B7%A5%E7%A8%8B%E6%9C%BA%E6%A2%B0",
			"%E6%8A%80%E5%B7%A5",
			"%E8%B4%A2%E4%BC%9A%2F%E5%AE%A1%E8%AE%A1%2F%E7%BB%9F%E8%AE%A1",
			"%E9%87%91%E8%9E%8D%2F%E9%93%B6%E8%A1%8C%2F%E4%BF%9D%E9%99%A9%2F%E8%AF%81%E5%88%B8%2F%E6%8A%95%E8%B5%84",
			"%E5%BB%BA%E7%AD%91%2F%E6%88%BF%E5%9C%B0%E4%BA%A7%2F%E8%A3%85%E4%BF%AE%2F%E7%89%A9%E4%B8%9A",
			"%E4%BA%A4%E9%80%9A%2F%E4%BB%93%E5%82%A8%2F%E7%89%A9%E6%B5%81",
			"%E6%99%AE%E9%80%9A%E5%8A%B3%E5%8A%A8%E5%8A%9B%2F%E5%AE%B6%E6%94%BF%E6%9C%8D%E5%8A%A1",
			"%E9%9B%B6%E5%94%AE%E4%B8%9A",
			"%E6%95%99%E8%82%B2%2F%E5%9F%B9%E8%AE%AD",
			"%E5%92%A8%E8%AF%A2%2F%E9%A1%BE%E9%97%AE",
			"%E5%AD%A6%E6%9C%AF%2F%E7%A7%91%E7%A0%94",
			"%E6%B3%95%E5%BE%8B",
			"%E7%BE%8E%E6%9C%AF%2F%E8%AE%BE%E8%AE%A1%2F%E5%88%9B%E6%84%8F",
			"%E7%BC%96%E8%BE%91%2F%E6%96%87%E6%A1%88%2F%E4%BC%A0%E5%AA%92%2F%E5%BD%B1%E8%A7%86%2F%E6%96%B0%E9%97%BB",
			"%E9%85%92%E5%BA%97%2F%E9%A4%90%E9%A5%AE%2F%E6%97%85%E6%B8%B8%2F%E5%A8%B1%E4%B9%90",
			"%E5%8C%96%E5%B7%A5",
			"%E8%83%BD%E6%BA%90%2F%E7%9F%BF%E4%BA%A7%2F%E5%9C%B0%E8%B4%A8%E5%8B%98%E6%9F%A5",
			"%E5%8C%BB%E7%96%97%2F%E6%8A%A4%E7%90%86%2F%E4%BF%9D%E5%81%A5%2F%E7%BE%8E%E5%AE%B9",
			"%E7%94%9F%E7%89%A9%2F%E5%88%B6%E8%8D%AF%2F%E5%8C%BB%E7%96%97%E5%99%A8%E6%A2%B0",
			"%E7%BF%BB%E8%AF%91%EF%BC%88%E5%8F%A3%E8%AF%91%E4%B8%8E%E7%AC%94%E8%AF%91%EF%BC%89",
			"%E5%85%AC%E5%8A%A1%E5%91%98",
			"%E7%8E%AF%E5%A2%83%E7%A7%91%E5%AD%A6%2F%E7%8E%AF%E4%BF%9D",
			"%E5%86%9C%2F%E6%9E%97%2F%E7%89%A7%2F%E6%B8%94%E4%B8%9A",
			"%E5%85%BC%E8%81%8C%2F%E4%B8%B4%E6%97%B6%2F%E5%9F%B9%E8%AE%AD%E7%94%9F%2F%E5%82%A8%E5%A4%87%E5%B9%B2%E9%83%A8",
			"%E5%9C%A8%E6%A0%A1%E5%AD%A6%E7%94%9F", "%E5%85%B6%E4%BB%96" };
	

	/**
	 * 遍历ZHILIAN搜索接口，省份的计数器
	 * */
	public static int ZHILIAN_SEARCHLIST_P = 0;

	/**
	 * 遍历ZHILIAN搜索接口，职位分类的计数器
	 * */
	public static int ZHILIAN_SEARCHLIST_C = 0;

	/**
	 * 遍历ZHILIAN搜索接口，一个计时器
	 * */
	public static int ZHILIAN_SEARCHLIST_TIMER = 0;

	/**
	 * 遍历ZHILIAN搜索接口，每个页面允许的最大延时(秒)
	 * */
	public static final int ZHILIAN_SEARCHLIST_DELAY_SECONDS = 10;

	/**
	 * ComZhilianController是否应该退出的标志位
	 * */
	public static boolean ZHILIAN_SEARCHLIST_RUNNING = true;
	
	public final static String ZHILIAN_SEARCHLIST_PIC_CONF_PATH = "conf/Zhilian_PC.conf";
	
	public final static String ZHILIAN_SEARCHILIST_LOG_PATH = "log/ZhilianSearchListLog_"
		+ (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()) + ".ini";

	
	public static boolean ZHILIAN_CONTENT_RUNNING = true;
	
	public static int ZHILIAN_CONTENT_TIMER = 0;
	
	public final static int ZHILIAN_CONTENT_DELAY_SECONDS = 10;
	
	public static int ZHILIAN_CONTENT_COUNT = 0;

	
	/**
	 * 51Job  通过[职位分类][省市代码]和[翻页页码]进行遍历，采集Id
	 * 
	 * 51Job  省份的编码（共34个省份和直辖市）
	 * */
	public final static String[] JOB51_PROVINCE={
		"1500","3400","0100","1100","2700","0300","1400","2600","1000","1600",
		"1700","2200","1800","1900","2400","0700","1300","2300","2800","2900",
		"3200","1200","2100","2000","0200","0900","3500","0500","3000","3300",
		"3100","2500","0800","0600"};

	/**
	 * 51Job  职位分类及其编码 55个 
	 * */
	public final static String[] JOB51_CATEGORY={
		"2400","0100","2500","2600","2700","2800","2900","0200","3000","3100",
		"3200","0400","3300","2200","3400","3500","3600","0500","5400","3700",
		"3800","3900","4000","0800","4100","5500","1300","4200","4300","0300",
		"4400","4500","0900","2100","4600","4700","0600","0700","2300","1400",
		"1100","1200","1000","4800","4900","5000","5100","1800","5200","1500",
		"2000","1600","1700","5300","1900"};
	
	public final static String[] JOB51_CATEGORY_CN={
		"计算机硬件类","计算机软件类","互联网开发及应用类","IT-管理类","IT-品管、技术支持及其它类",
		"通信技术类","电子/电器/半导体/仪器仪表类","销售管理类","销售人员类","销售行政及商务类",
		"客服及技术支持类","财务/审计/税务类","证券/金融/投资类","银行类","保险类",
		"生产/营运类","质量/安全管理类","工程/机械/能源类","汽车类","技工类",
		"服装/纺织/皮革类","采购类","贸易类","物流/仓储类","生物/制药/医疗器械类",
		"化工类","医院/医疗/护理类","广告类","公关/媒介类","市场/营销类",
		"影视/媒体类","写作/出版/印刷类","艺术/设计类","建筑工程类","房地产类",
		"物业管理类","人力资源类","高级管理类","行政/后勤类","咨询/顾问类",
		"律师/法务类","教师类","科研人员类","餐饮/娱乐类","酒店/旅游类",
		"美容/健身类","百货/连锁/零售服务类","交通运输服务类","保安/家政/其他服务类","公务员类",
		"翻译类","在校学生类","储备干部/培训生/实习生类","兼职类","其他类"};
	
	/**
	 * 51Job  设置翻页页码 翻页的上限 
	 * */
	public final static int JOB51_SEARCHLIST_MAX_PAGE = 1;  
	
	/**
	 * 51Job  判断对某一个省市的翻页是否结束 
	 * */
	public static boolean JOB51_SEARCHLIST_IS_PAGE_OVER = false;

	/**
	 * 51Job  一个51job搜索页面的url地址（通过post提交搜索请求） 
	 * */
	public final static String JOB51_SEARCHLIST_ACTION_URL = "http://search.51job.com/jobsearch/keyword_search.php";
	
	/**
	 * 51Job  一个51job高级搜索页面的url地址（通过post提交搜索请求） 
	 * */
	public final static String STR_51JOB_ADVANCE = "http://search.51job.com/jobsearch/search_result.php";
	
	/**
	 * 51Job  一个51job详细信息页面的url地址（后面加上括号和id） 
	 * */
	public final static String JOB51_CONTENT_DETAIL_URL = "http://search.51job.com/jobsearch/show_job_detail.php?id=";
	
	/**
	 * 51Job  取html代码时（对每一个ID） 设置最多尝试次数 
	 * */
	public static int JOB51_CONTENT_MAX_TRY_NUM = 1;  

	/**
	 * 遍历51Job搜索接口时的Log文件的路径
	 * */
	public final static String JOB51_SEARCHLIST_LOG_PATH = "log/Job51SearchListLog_"
			+ (new SimpleDateFormat("yyyy-MM-dd")).format(new Date()) + ".ini";
	
	public static boolean JOB51_SEARCHLIST_RUNNING = true;
	
	public static int JOB51_SEARCHLIST_TIMER = 0;
	
	public final static int JOB51_SEARCHLIST_DELAY_SECONDS = 10;
	
	public static boolean JOB51_CONTENT_RUNNING = true;
	
	public static int JOB51_CONTENT_TIMER = 0;
	
	public final static int JOB51_CONTENT_DELAY_SECONDS = 10;
	
	public final static int JOB51_SEARCHLIST_ID_NUM_EACH_PAGE = 30;
}

