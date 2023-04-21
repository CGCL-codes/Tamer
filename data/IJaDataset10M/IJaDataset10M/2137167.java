package net.sf.revolver.sample.service.book.list;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import java.util.Locale;
import net.sf.bulletlib.authentication.core.LoginUser;
import net.sf.revolver.units2.RvS2TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;

/**
 * ListBookCtrlTest.
 * 
 * @author mabe54
 */
@RunWith(Seasar2.class)
public class ListBookCtrlTest extends RvS2TestCase {

    /**
     * テスト用ログインユーザ.
     */
    private LoginUser loginUser = new LoginUser(1, "TEST", new String[] { "admin" }, new Locale("ja"), "127.0.0.1");

    /**
     * テスト用Actionクラス名.
     */
    private String testActionClassName = "BookAction";

    /**
     * テスト用Actionクラスでコールされたメソッド名.
     */
    private String testActionMethodName = "list";

    /**
     * ListBookCtrl.
     */
    private ListBookCtrl listBookCtrl;

    /**
     * setReturnViewValues(RvContext rvContext)メソッド 正常系テスト01.<br />
     * <br />
     * [テスト内容]<br />
     * rvContextにエラーメッセージがある時、画面への戻り値がセットされることをテストする。<br />
     * <br />
     * [前提条件]<br />
     * 引数のrvContextにエラーメッセージがある事。<br />
     * <br />
     * [確認項目]<br />
     * 画面への戻り値がセットされる事。<br />
     */
    @Test
    public void testSetReturnViewValuesNormal01() {
        init(loginUser, testActionClassName, testActionMethodName);
        setResultValue("books", "books");
        setResultValue("maxPage", "100");
        setResultValue("nowPage", "10");
        listBookCtrl.setReturnViewValues(getRvContext());
        assertThat((String) getViewValues().get("books"), is("books"));
        assertThat((String) getViewValues().get("maxPage"), is("100"));
        assertThat((String) getViewValues().get("nowPage"), is("10"));
    }
}
