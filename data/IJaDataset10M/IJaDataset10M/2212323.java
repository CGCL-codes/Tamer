package org.opu.db_vdumper.ui.i18n;

/**
 *
 * @author yura
 */
public class MenuI18nImpl extends AbstractI18N implements MenuI18n {

    public MenuI18nImpl() {
        super("org.opu.db_vdumper.ui.MenuI18N");
    }

    @Override
    public String getMenuText(String key) {
        return getSafe(key);
    }
}
