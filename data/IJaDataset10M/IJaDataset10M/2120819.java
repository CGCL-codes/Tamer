package nps.extra.trade;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

/**
 *  ��Ʒ��������
 *  Copyright (c) 2007

 *  @author jialin
 *  @version 1.0
 */
public class ProductLanguage {

    public static List GetKnownLanguages() {
        List known_languages = new ArrayList();
        known_languages.add(Locale.CHINESE);
        known_languages.add(Locale.ENGLISH);
        return known_languages;
    }
}
