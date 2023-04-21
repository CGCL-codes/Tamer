package sun.text.resources;

import java.util.ListResourceBundle;

public class FormatData_nl_BE extends ListResourceBundle {

    /**
     * Overrides ListResourceBundle
     */
    protected final Object[][] getContents() {
        return new Object[][] { { "NumberPatterns", new String[] { "#,##0.###;-#,##0.###", "#,##0.00 ¤;-#,##0.00 ¤", "#,##0%" } }, { "DateTimePatterns", new String[] { "H.mm' u. 'z", "H:mm:ss z", "H:mm:ss", "H:mm", "EEEE d MMMM yyyy", "d MMMM yyyy", "d-MMM-yyyy", "d/MM/yy", "{1} {0}" } }, { "DateTimePatternChars", "GyMdkHmsSEDFwWahKzZ" } };
    }
}
