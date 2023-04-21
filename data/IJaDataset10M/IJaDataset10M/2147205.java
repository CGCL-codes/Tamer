package cn.nkjobsearch.convert;

import cn.nkjobsearch.config.Config;

/**
0 其他
1 北京
2 上海
3 天津
4 广州
5 深圳
6 南京
7 苏州
8 杭州
9 重庆
10 合肥
11 福州
12 兰州
13 南宁
14 贵阳
15 海口
16 石家庄
17 郑州
18 哈尔滨
19 武汉
20 长沙
21 长春
22 南昌
23 沈阳
24 呼和浩特
25 银川
26 西宁
27 济南
28 太原
29 西安
30 成都
31 拉萨
32 乌鲁木齐
33 昆明
34 芜湖
35 安庆
36 马鞍山
37 巢湖
38 滁州
39 黄山
40 淮南
41 蚌埠
42 阜阳
43 六安
44 泉州
45 厦门
46 漳州
47 莆田
48 河源
49 汕头
50 汕尾
51 湛江
52 中山
53 东莞
54 江门
55 潮州
56 佛山
57 珠海
58 惠州
59 北海
60 桂林
61 柳州
62 遵义
63 三亚
64 保定
65 廊坊
66 秦皇岛
67 唐山
68 邯郸
69 邢台
70 开封
71 洛阳
72 大庆
73 佳木斯
74 牡丹江
75 齐齐哈尔
76 十堰
77 襄樊
78 宜昌
79 荆门
80 荆州
81 黄石
82 湘潭
83 株洲
84 常德
85 衡阳
86 益阳
87 郴州
88 岳阳
89 吉林
90 辽源
91 通化
92 常州
93 昆山
94 连云港
95 南通
96 张家港
97 无锡
98 徐州
99 扬州
100 镇江
101 盐城
102 九江
103 上饶
104 赣州
105 鞍山
106 大连
107 葫芦岛
108 营口
109 锦州
110 本溪
111 抚顺
112 丹东
113 铁岭
114 包头
115 赤峰
116 德州
117 东营
118 济宁
119 临沂
120 青岛
121 日照
122 泰安
123 威海
124 潍坊
125 烟台
126 淄博
127 菏泽
128 枣庄
129 聊城
130 莱芜
131 临汾
132 运城
133 宝鸡
134 咸阳
135 乐山
136 泸州
137 绵阳
138 内江
139 宜宾
140 自贡
141 日喀则
142 喀什
143 克拉玛依
144 伊犁
145 吐鲁番
146 大理
147 丽江
148 玉溪
149 曲靖
150 金华
151 丽水
152 宁波
153 嘉兴
154 舟山
155 温州
156 台州
157 衢州
158 绍兴
159 湖州
160 香港
161 澳门
162 台湾
163 不限/全国/空值
 * */
public class City {

    public static int convert(String city, char fromSite) {
        if (fromSite == '5') {
            return job51(city);
        } else if (fromSite == 'c') {
            return chinaHR(city);
        } else if (fromSite == 'z') {
            return zhilian(city);
        } else {
            return 163;
        }
    }

    private static int job51(String city) {
        if (city == null || city.equals("null")) {
            return 163;
        }
        int index = -1;
        String subCity = null;
        int indexOfBar = city.charAt('-');
        if (indexOfBar != -1) {
            subCity = city.substring(0, indexOfBar - 1);
        } else {
            city.substring(0, city.length() - 1);
        }
        for (int i = 0; i < Config.CITY.length - 3; i++) {
            if (subCity.equals(Config.CITY[i])) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            if (city.equals("全国")) {
                return 163;
            }
            if (city.equals("香港")) {
                return 160;
            }
            if (city.equals("澳门")) {
                return 161;
            }
            if (city.equals("台湾")) {
                return 162;
            }
        }
        return index + 1;
    }

    private static int zhilian(String city) {
        if (city == null || city.equals("null")) {
            return 163;
        }
        int index = -1;
        for (int i = 0; i < Config.CITY.length - 3; i++) {
            if (city.equals(Config.CITY[i])) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            if (city.equals("全国")) {
                return 163;
            }
        }
        return index + 1;
    }

    private static int chinaHR(String city) {
        if (city == null || city.equals("null")) {
            return 163;
        }
        int index = -1;
        for (int i = 0; i < Config.CITY.length - 3; i++) {
            if (city.equals(Config.CITY[i])) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            if (city.equals("全国")) {
                return 163;
            }
        }
        return index + 1;
    }

    /**
	 * 由 城市名称 获得 城市编码
	 * @param city
	 * @return
	 */
    public static int getCityCode(String city) {
        for (int i = 0; i < Config.CITY.length; i++) {
            if (city.equals(Config.CITY[i])) {
                return i + 1;
            }
        }
        return 163;
    }

    /**
	 * 由 城市编码 获得 城市名称
	 * @param code
	 * @return
	 */
    public static String getCityViaCode(int code) {
        if (code >= 1 && code <= 162) {
            return Config.CITY[code - 1];
        } else return "不限";
    }
}
