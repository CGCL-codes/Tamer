package com.corpus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author LvSaiHui {gurnfe@163.com}
 * @version 1.0 <br>
 *          Copyright (C), 2007-2008, ZJUT <br>
 *          This program is protected by copyright laws. <br>
 *          Program Name: KeyWordSet.java <br>
 *          Date: 2009-2-17 <br>
 *          Description:
 */
public class KeyWordSet {

    public static Map<String, Double> Theme;

    static {
        Theme = new HashMap<String, Double>();
        Theme.put("信用", 9.981319400063215);
        Theme.put("信用等级", 8.8090354888959124);
        Theme.put("信用评级", 8.8090354888959124);
        Theme.put("信用卡", 8.6090354888959124);
        Theme.put("评级", 8.8090354888959124);
        Theme.put("风险", 8.1090354888959124);
        Theme.put("信贷", 8.0317766166719344);
        Theme.put("担保", 8.041598629223295);
        Theme.put("征信", 8.021598629223295);
        Theme.put("资信", 8.021598629223295);
        Theme.put("信用管理", 8.021598629223295);
        Theme.put("信用建设", 8.021598629223295);
        Theme.put("信用单位", 8.021598629223295);
        Theme.put("信用风险", 8.021598629223295);
        Theme.put("信用研究", 8.021598629223295);
        Theme.put("信用体系", 8.021598629223295);
        Theme.put("信用制度", 7.021598629223295);
        Theme.put("信誉", 7.856998629229863);
        Theme.put("信用经济", 7.456998629229863);
        Theme.put("国际信用", 7.456998629229863);
        Theme.put("诚实", 6.721598629223295);
        Theme.put("诚信", 6.4260151319598084);
        Theme.put("违信", 6.256598629223295);
        Theme.put("失信", 6.056598629223295);
        Theme.put("征信业", 5.236598629223295);
        Theme.put("企业信用", 5.23625629223295);
        Theme.put("商业信用", 5.23625629223295);
        Theme.put("规范", 7.070101241711441);
        Theme.put("市场经济", 1.2476649250079015);
        Theme.put("制度性", 0.9887510598012985);
        Theme.put("权威性", 0.6591673732008657);
        Theme.put("完善", 0.6238324625039507);
        Theme.put("整顿", 0.4158883083359672);
        Theme.put("公开", 0.4158883083359672);
        Theme.put("素质", 0.4158883083359672);
        Theme.put("责任感", 0.32958368660043286);
        Theme.put("房贷", 3.6238324625039507);
        Theme.put("3.15", 1.6238324625039507);
        Theme.put("金融机构", 2.495329850015803);
        Theme.put("风险管理", 1.2358324625039507);
        Theme.put("市场经济", 1.6635532333438687);
        Theme.put("金融", 1.6635532333438687);
        Theme.put("研究", 1.316979643063896);
        Theme.put("信用卡", 0.9887510598012985);
        Theme.put("政策", 0.9704060527839233);
        Theme.put("制度", 0.9704060527839233);
        Theme.put("信贷支持", 0.8317766166719344);
        Theme.put("银行", 0.6238324625039507);
        Theme.put("贷款人", 0.6238324625039507);
        Theme.put("抵押", 0.6238324625039507);
        Theme.put("监督", 0.6931471805599453);
        Theme.put("商业", 0.6238324625039507);
        Theme.put("安博尔", 0.5493061443340548);
        Theme.put("中诚信", 0.5493061443340548);
        Theme.put("消费信贷", 0.4158883083359672);
        Theme.put("还贷", 0.4158883083359672);
        Theme.put("利率", 0.4158883083359672);
        Theme.put("商业银行 ", 0.4158883083359672);
        Theme.put("赖账", 0.32958368660043286);
        Theme.put("公信力", 0.32958368660043286);
        Theme.put("行业", 0.2772588722239781);
        Theme.put("道德", 0.2772588722239781);
        Theme.put("坏账", 0.2079441541679836);
        Theme.put("记录", 0.2079441541679836);
        Theme.put("防范", 0.2079441541679836);
        Theme.put("污点", 0.2079441541679836);
        Theme.put("欠债", 0.2079441541679836);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Iterator it = KeyWordSet.Theme.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            String keyword = (String) key;
            Object val = entry.getValue();
            if (keyword.equals("诚信")) {
                System.out.println("$$$$$$$$$$$$$$$$$");
            }
            System.out.println(key + "--->" + val);
        }
    }
}
