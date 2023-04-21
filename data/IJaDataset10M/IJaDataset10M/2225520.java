package org.tolk.process.node.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.tolk.BaseTestCase;

public class PrependTranslatorTest extends BaseTestCase {

    private PrependTranslator prependTranslator;

    private String pattern;

    private String sysDateFormat;

    private SimpleDateFormat simpleDateFormat;

    @Override
    public void setUp() {
        this.prependTranslator = new PrependTranslator();
        this.pattern = "@sysdate@ is the date";
        this.sysDateFormat = "yyyy-MM-dd";
        this.prependTranslator.setPattern(this.pattern);
        this.prependTranslator.setSysDateFormat(this.sysDateFormat);
        this.simpleDateFormat = new SimpleDateFormat(this.sysDateFormat);
    }

    public void testTranslate() {
        String translated = this.prependTranslator.translate("now");
        assertEquals(this.simpleDateFormat.format(new Date(System.currentTimeMillis())) + " is the date now", translated);
    }
}
