package org.dueam.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="windonly@gmail.com">Anemone</a>
 * hz,zj,china(2006-11-5)
 */
public class RegexUtils {

    /**
	 * ����ƥ�������
	 * @param text ��������
	 * @param regex ������ʽ
	 * @return
	 */
    public static List<String> getMatched(String text, String regex) {
        List<String> result = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    /**
	 * ���е�GROUP,���е�һ��Ϊȫƥ���ֶ�,��:text = abccddd,regex=b([c]+)d�򷵻�[[bccd,cc]]
	 * @param text ��������
	 * @param regex ������ʽ
	 * @return
	 */
    public static List<List> getGroups(String text, String regex) {
        List<List> result = new ArrayList<List>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < matcher.groupCount() + 1; i++) {
                list.add(matcher.group(i));
            }
            result.add(list);
        }
        return result;
    }
}
