package org.keyintegrity.webbeans.demo.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DemoBeans {

    private static Random random = new Random(System.currentTimeMillis());

    public static PlainJavaBean getPlainJavaBean() {
        PlainJavaBean result = new PlainJavaBean();
        result.setBigDecimal(getCoolBigDecimal());
        result.setDate(new Date());
        result.setInteger(random.nextInt());
        result.setString("Hello String");
        result.setListOfStrings(getListOfStrings());
        return result;
    }

    private static BigDecimal getCoolBigDecimal() {
        String d = String.valueOf(random.nextDouble());
        return new BigDecimal(d.substring(1, 6));
    }

    private static List<String> getListOfStrings() {
        List<String> result = new ArrayList<String>();
        for (int i = 1; i <= 3; i++) {
            result.add("Hello #" + String.valueOf(i));
        }
        return result;
    }

    public static ComplexJavaBean getComplexJavaBean() {
        ComplexJavaBean result = getRawComplexJavaBean();
        result.setComplexJavaBean(getRawComplexJavaBean());
        return result;
    }

    private static ComplexJavaBean getRawComplexJavaBean() {
        ComplexJavaBean result = new ComplexJavaBean();
        result.setBool(true);
        List<Date> listOfDates = new ArrayList<Date>();
        List<PlainJavaBean> plainJavaBeans = new ArrayList<PlainJavaBean>();
        for (int i = 1; i <= 2; i++) {
            listOfDates.add(new Date());
            plainJavaBeans.add(getPlainJavaBean());
        }
        result.setListOfDates(listOfDates);
        result.setPlainJavaBeans(plainJavaBeans);
        return result;
    }
}
