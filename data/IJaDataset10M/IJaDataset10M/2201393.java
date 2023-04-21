package sun.text.resources;

import java.util.ListResourceBundle;

public class FormatData_ru_RU extends ListResourceBundle {

    /**
     * Overrides ListResourceBundle
     */
    protected final Object[][] getContents() {
        return new Object[][] { { "NumberPatterns", new String[] { "#,##0.###;-#,##0.###", "#,##0.## ¤;-#,##0.## ¤", "#,##0%" } } };
    }
}
