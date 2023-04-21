package cn.nkjobsearch.convert;

public class Category {

    /**
	 * 把 职业分类编码 转化为对应的 string
	 */
    public static final String CATEGORY[] = { "计算机软、硬件·互联网·IT类", "行政·后勤类", "电子·电器·半导体·通信技术类", "翻译类", "销售类", "客户服务·技术支持类", "市场·营销·公关·媒介类", "咨询·顾问类", "财务·会计·审计·税务·统计类", "人力资源类", "教育·培训类", "质量管理·安全管理类", "美术·设计·艺术·广告·创意类", "技工类", "金融·银行·保险·基金·证券·期货·投资类", "贸易·采购·物流·仓储·运输类", "百货·连锁·零售类", "建筑·房地产·装饰装修·物业管理类", "法律类", "酒店·娱乐·餐饮·旅游·服务类", "生物·制药·医疗器械·化工·环保类", "新闻·编辑·传媒·影视·出版·印刷类", "在校学生类", "机械·仪器仪表·能源类", "科研·学术类", "生产·加工·制造·营运类", "公务员类", "医疗·护理·美容·保健类", "储备干部·培训生·实习生·兼职类", "汽车·电气·动力·能源·地质勘查类", "高级管理·项目管理·经营管理类", "保安·家政·其他服务类", "服装·纺织·皮革类", "农·林·牧·渔业类", "其他类" };

    public static String getCategoryViaCode(int code) {
        return CATEGORY[code - 1];
    }

    public static int convert(String category, char fromSite) {
        if (fromSite == '5') {
            return job51(category);
        } else if (fromSite == 'c') {
            return chinaHR(category);
        } else if (fromSite == 'z') {
            return zhilian(category);
        } else {
            return 35;
        }
    }

    private static int job51(String category) {
        if (category == null || category.equals("null")) {
            return 35;
        }
        int category_int = Integer.parseInt(category);
        switch(category_int) {
            case 2400:
                return 1;
            case 100:
                return 1;
            case 2500:
                return 1;
            case 2600:
                return 1;
            case 2700:
                return 1;
            case 2300:
                return 2;
            case 2800:
                return 3;
            case 2900:
                return 3;
            case 2000:
                return 4;
            case 200:
                return 5;
            case 3000:
                return 5;
            case 3100:
                return 5;
            case 3200:
                return 6;
            case 4300:
                return 7;
            case 300:
                return 7;
            case 1400:
                return 8;
            case 400:
                return 9;
            case 600:
                return 10;
            case 1200:
                return 11;
            case 3600:
                return 12;
            case 900:
                return 13;
            case 4200:
                return 13;
            case 3700:
                return 14;
            case 3300:
                return 15;
            case 2200:
                return 15;
            case 3400:
                return 15;
            case 3900:
                return 16;
            case 4000:
                return 16;
            case 800:
                return 16;
            case 1800:
                return 16;
            case 5100:
                return 17;
            case 2100:
                return 18;
            case 4600:
                return 18;
            case 4700:
                return 18;
            case 1100:
                return 19;
            case 4800:
                return 20;
            case 4900:
                return 20;
            case 4100:
                return 21;
            case 5500:
                return 21;
            case 4400:
                return 22;
            case 4500:
                return 22;
            case 1600:
                return 23;
            case 500:
                return 24;
            case 1000:
                return 25;
            case 3500:
                return 26;
            case 1500:
                return 27;
            case 5000:
                return 28;
            case 1300:
                return 28;
            case 1700:
                return 29;
            case 5300:
                return 29;
            case 5400:
                return 30;
            case 700:
                return 31;
            case 5200:
                return 32;
            case 3800:
                return 33;
            case 1900:
                return 35;
            default:
                return 35;
        }
    }

    private static int zhilian(String category) {
        if (category == null || category.equals("null")) {
            return 35;
        }
        int category_int = Integer.parseInt(category);
        switch(category_int) {
            case 160000:
                return 1;
            case 3010000:
                return 2;
            case 160100:
                return 3;
            case 5001000:
                return 3;
            case 2120500:
                return 4;
            case 4010200:
                return 5;
            case 4000000:
                return 6;
            case 4082000:
                return 7;
            case 2140000:
                return 8;
            case 2060000:
                return 9;
            case 5002000:
                return 10;
            case 2090000:
                return 11;
            case 2023405:
                return 12;
            case 2100708:
                return 13;
            case 5004000:
                return 14;
            case 2070000:
                return 15;
            case 4083000:
                return 16;
            case 4010300:
                return 16;
            case 5005000:
                return 17;
            case 140000:
                return 18;
            case 2080000:
                return 19;
            case 4040000:
                return 20;
            case 121300:
                return 21;
            case 120500:
                return 21;
            case 2023100:
                return 21;
            case 2120000:
                return 22;
            case 5006000:
                return 23;
            case 5003000:
                return 24;
            case 2010000:
                return 25;
            case 121100:
                return 26;
            case 200100:
                return 27;
            case 2050000:
                return 28;
            case 1:
                return 29;
            case 130000:
                return 30;
            case 1050000:
                return 31;
            case 6270000:
                return 32;
            case 100000:
                return 34;
            case 2:
                return 35;
            default:
                return 35;
        }
    }

    private static int chinaHR(String category) {
        if (category == null || category.equals("null")) {
            return 35;
        }
        int category_int = Integer.parseInt(category);
        switch(category_int) {
            case 600:
                return 1;
            case 1035000:
                return 2;
            case 700:
                return 3;
            case 1016001:
                return 4;
            case 100:
                return 5;
            case 900:
                return 6;
            case 3600:
                return 7;
            case 2100:
                return 8;
            case 400:
                return 9;
            case 300:
                return 10;
            case 1900:
                return 11;
            case 1034000:
                return 12;
            case 3800:
                return 13;
            case 3300:
                return 14;
            case 1013000:
                return 15;
            case 1036000:
                return 15;
            case 1014000:
                return 16;
            case 1020000:
                return 17;
            case 702:
                return 18;
            case 1700:
                return 19;
            case 1017000:
                return 20;
            case 1026000:
                return 21;
            case 3500:
                return 22;
            case 2500:
                return 23;
            case 3420:
                return 24;
            case 800:
                return 25;
            case 3400:
                return 26;
            case 2600:
                return 27;
            case 2000:
                return 28;
            case 3700:
                return 29;
            case 2700:
                return 30;
            case 1200:
                return 31;
            case 604:
                return 31;
            case 1033000:
                return 35;
            default:
                return 35;
        }
    }
}
