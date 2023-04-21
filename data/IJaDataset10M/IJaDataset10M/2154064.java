package cn.myapps.core.multilanguage.ejb;

import cn.myapps.base.ejb.IDesignTimeProcess;

public interface MultiLanguageProcess extends IDesignTimeProcess {

    /**
	 * 
	 * @param languageType 
	 * @param label
	 * @return
	 * @throws Exception
	 */
    public MultiLanguage doView(int languageType, String label) throws Exception;
}
