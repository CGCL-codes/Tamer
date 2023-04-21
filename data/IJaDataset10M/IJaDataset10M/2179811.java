package com.liferay.portlet.journal.model.impl;

import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.util.LocaleTransformerListener;
import com.liferay.util.LocalizationUtil;

/**
 * <a href="JournalArticleImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class JournalArticleImpl extends JournalArticleModelImpl implements JournalArticle {

    public static final double DEFAULT_VERSION = 1.0;

    public static final String[] TYPES = PropsUtil.getArray(PropsUtil.JOURNAL_ARTICLE_TYPES);

    public static final String PORTLET = "portlet";

    public static final String STAND_ALONE = "stand-alone";

    public JournalArticleImpl() {
    }

    public String[] getAvailableLocales() {
        return LocalizationUtil.getAvailableLocales(getContent());
    }

    public String getContentByLocale(String languageId) {
        LocaleTransformerListener listener = new LocaleTransformerListener();
        listener.setTemplateDriven(isTemplateDriven());
        listener.setLanguageId(languageId);
        return listener.onXml(getContent());
    }

    public String getDefaultLocale() {
        String xml = getContent();
        if (xml == null) {
            return StringPool.BLANK;
        }
        if (isTemplateDriven()) {
            String defaultLanguageId = LocaleUtil.toLanguageId(LocaleUtil.getDefault());
            return defaultLanguageId;
        } else {
            return LocalizationUtil.getDefaultLocale(xml);
        }
    }

    public boolean isTemplateDriven() {
        if (Validator.isNull(getStructureId())) {
            return false;
        } else {
            return true;
        }
    }
}
