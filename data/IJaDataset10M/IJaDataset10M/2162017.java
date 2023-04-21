package net.sf.revolver.sample.action;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import java.util.HashMap;
import java.util.Map;
import net.sf.revolver.core.BusinessLogicException;
import net.sf.revolver.core.Result;
import net.sf.revolver.core.RvContext;
import net.sf.revolver.s2.RvCtrlS2;
import org.junit.Test;

public class LocaleActionTest {

    /**
     * index()メソッド 正常系テスト01.<br />
     * <br />
     * [テスト内容]<br />
     * アクセス時、/locale/changelocale/ja?redirect=trueへ遷移する。<br />
     * <br />
     * [前提条件]<br />
     * インスタンス変数localeにjaがセットされている事<br />
     * <br />
     * [確認項目]<br />
     * /locale/changelocale/ja?redirect=trueが返ってくる事。<br />
     */
    @Test
    public void indexTest01() {
        LocaleAction localeAction = new LocaleAction();
        localeAction.locale = "ja";
        assertThat(localeAction.index(), is("/locale/changeLocale/ja?redirect=true"));
    }

    /**
     * changeLocale()メソッド 正常系テスト01.<br />
     * <br />
     * [テスト内容]<br />
     * Exceptionを起こさずに、/book/search?redirect=trueへ遷移する。<br />
     * <br />
     * [前提条件]<br />
     * viewValuesにHashMapがセットされている事。<br />
     * <br />
     * [確認項目]<br />
     * /book/search?redirect=trueが返ってくる事。<br />
     */
    @Test
    public void changeLocaleTest01() {
        Map<String, Object> viewValues = new HashMap<String, Object>();
        RvContext rvContext = new RvContext("TEST", false);
        rvContext.setAttributeValue("viewValues", viewValues);
        RvCtrlS2 ctrl = new SuccessCtrlMock();
        LocaleAction localeAction = new LocaleAction();
        localeAction.viewValues = viewValues;
        localeAction.changeRvContext(rvContext);
        localeAction.changeLocaleCtrl = ctrl;
        assertThat(localeAction.changeLocale(), is("/book/search?redirect=true"));
    }

    /**
     * RvSAStrutsCtrlのMockクラス.
     *
     * @author bose999
     *
     */
    private static class SuccessCtrlMock extends RvCtrlS2 {

        /**
         * 処理結果として空のHashMapを返す.
         * @param rvContext RvContext
         */
        @Override
        protected void setReturnValue(RvContext rvContext) throws BusinessLogicException {
            rvContext.setReturnObject(new HashMap<String, Object>());
        }
    }

    /**
     * RvSAStrutsCtrlのMockクラス.
     *
     * @author bose999
     *
     */
    private static class FailureCtrlMock extends RvCtrlS2 {

        /**
         * 処理結果として空のHashMapを返す.
         * @param rvContext RvContext
         */
        @Override
        protected void setReturnValue(RvContext rvContext) throws BusinessLogicException {
            rvContext.setReturnObject(new HashMap<String, Object>());
            rvContext.setResult(Result.FAILURE);
        }
    }
}
