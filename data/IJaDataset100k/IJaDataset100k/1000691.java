package org.compiere.util;

import java.util.ListResourceBundle;

/**
 *  License Dialog Translation
 *
 *  @author     Vyacheslav Pedak
 *  @version    $Id: IniRes_ru.java,v 1.2 2006/07/30 00:52:23 jjanke Exp $
 */
public class IniRes_ru extends ListResourceBundle {

    /** Translation Content     */
    static final Object[][] contents = new String[][] { { "Adempiere_License", "Лицензионное соглашение" }, { "Do_you_accept", "Вы принимаете лицензионное соглашение?" }, { "No", "Нет" }, { "Yes_I_Understand", "Да, Я принимаю" }, { "license_htm", "org/adempiere/license.htm" }, { "License_rejected", "Лицензия не принята или истек срок использования" } };

    /**
	 *  Get Content
	 *  @return Content
	 */
    public Object[][] getContents() {
        return contents;
    }
}
