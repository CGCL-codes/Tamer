package com.feature50.app;

import com.steadystate.css.CSS2Parser;
import com.steadystate.css.parser.CSSOMParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.css.sac.InputSource;

public class CSSTest {

    public static void main(String[] args) throws Exception {
        InputStream in = CSSTest.class.getClassLoader().getResourceAsStream("css/primary.css");
        CSSOMParser parser = new CSSOMParser();
        InputSource is = new InputSource(new InputStreamReader(in));
        CSSStyleSheet stylesheet = parser.parseStyleSheet(is);
        CSSRuleList list = stylesheet.getCssRules();
        for (int i = 0; i < list.getLength(); i++) {
            CSSStyleRule rule = (CSSStyleRule) list.item(i);
            System.out.println(rule.getSelectorText());
            CSSStyleDeclaration style = rule.getStyle();
            for (int j = 0; j < style.getLength(); j++) {
                System.out.println("  " + style.item(j) + " - " + style.getPropertyValue(style.item(j)));
            }
        }
    }
}
