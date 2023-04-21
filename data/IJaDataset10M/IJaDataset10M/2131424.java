package net.sf.revolver.sample.service.locale.change;

import java.util.Locale;
import net.sf.bulletlib.core.logging.Logging;
import net.sf.revolver.core.BusinessLogicException;
import net.sf.revolver.core.Result;
import net.sf.revolver.core.RvContext;
import net.sf.revolver.s2.RvValidateS2;

/**
 * ロケール変更Validate処理クラス.
 *
 * @author bose999
 *
 */
public class ChangeLocaleValidate extends RvValidateS2 {

    /**
     * ローケール変更Validate処理.
     *
     * @param rvContext RvContext
     * @return Result
     * @throws BusinessLogicException BusinessLogicException
     */
    @Override
    protected Result doValidate(RvContext rvContext) throws BusinessLogicException {
        isIsoLanguage(rvContext, (String) rvContext.getInValue("locale"));
        return returnValidateResult(rvContext);
    }

    /**
     * ISO Language確認Validate処理.
     *
     * @param rvContext RvContext
     * @param locale String
     */
    protected void isIsoLanguage(RvContext rvContext, String locale) {
        if (locale == null) {
            Logging.debug(this.getClass(), "RVS999999", rvContext.getLoginId(), "" + locale + "is null.");
            addErrorMessage(rvContext, "viewValues.locale", "changeLocaleCtrl.locale01");
            return;
        }
        String[] isoLanguages = Locale.getISOLanguages();
        boolean noIsoLanguage = true;
        for (String isoLanguage : isoLanguages) {
            if (isoLanguage.equals(locale)) {
                noIsoLanguage = false;
                Logging.debug(this.getClass(), "RVS999999", rvContext.getLoginId(), "" + locale + "is ISO Language code.");
            }
        }
        if (noIsoLanguage) {
            Logging.debug(this.getClass(), "RVS999999", rvContext.getLoginId(), "" + locale + "isn't ISO Language code.");
            addErrorMessage(rvContext, "viewValues.locale", "changeLocaleCtrl.locale02");
        }
    }
}
