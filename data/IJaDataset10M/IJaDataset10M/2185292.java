package net.sf.revolver.sample.service.userdata.search;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import mockit.Mockit;
import net.sf.bulletlib.authentication.core.LoginUser;
import net.sf.bulletlib.authentication.s2jdbc.UserData;
import net.sf.revolver.core.Result;
import net.sf.revolver.sample.dao.UserDataDaoImpl;
import net.sf.revolver.sample.dto.SearchUserDataConditionDto;
import net.sf.revolver.units2.RvS2TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;

/**
 * SearchUserDataCtrlTest.
 * 
 * @author redlly
 */
@RunWith(Seasar2.class)
public class SearchUserDataCtrlTest extends RvS2TestCase {

    /**
     * テスト用ログインユーザ.
     */
    private LoginUser loginUser = new LoginUser(1, "TEST", new String[] { "admin" }, new Locale("ja"), "127.0.0.1");

    /**
     * テスト用Actionクラス名.
     */
    private String testActionClassName = "UserDataAction";

    /**
     * テスト用Actionクラスでコールされたメソッド名.
     */
    private String testActionMethodName = "search";

    /**
     * SearchUserDataCtrl.
     */
    private SearchUserDataCtrl searchUserDataCtrl;

    /**
     * MAX_PAGE.
     */
    private static final Long MAX_PAGE = Long.valueOf(10);

    /**
     * doValidate(RvContext rvContext)メソッド 正常系テスト01.<br />
     * <br />
     * [テスト内容]<br />
     * userId文字列が20バイト<br />
     * locale文字列が2バイトで渡ってきた際にvalidateする事をテストする。<br />
     * <br />
     * [前提条件]<br />
     * rvContextのinValueにuserIdが20バイトでセットされている。<br />
     * rvContextのinValueにlocaleが2バイトでセットされている。<br />
     * <br />
     * [確認項目]<br />
     * Result.SUCCESSが返ってくる事。<br />
     * エラーメッセージが返ってこない事。<br />
     */
    @Test
    public void testDoValidateNormal01() {
        init(loginUser, testActionClassName, testActionMethodName);
        setInValue("userId", "12345678901234567890");
        setInValue("locale", "ja");
        Result result = searchUserDataCtrl.doValidate(getRvContext());
        assertThat(result, is(Result.SUCCESS));
        assertThat(getErrorMessages("viewValues.userId"), nullValue());
        assertThat(getErrorMessages("viewValues.locale"), nullValue());
    }

    /**
     * doValidate(RvContext rvContext)メソッド 正常系テスト02.<br />
     * <br />
     * [テスト内容]<br />
     * userId文字列が19バイト<br />
     * locale文字列が1バイトで渡ってきた際にvalidateする事をテストする。<br />
     * <br />
     * [前提条件]<br />
     * rvContextのinValueにuserIdが19バイトでセットされている。<br />
     * rvContextのinValueにlocaleが1バイトでセットされている。<br />
     * <br />
     * [確認項目]<br />
     * Result.SUCCESSが返ってくる事。<br />
     * エラーメッセージが返ってこない事。<br />
     */
    @Test
    public void testDoValidateNormal02() {
        init(loginUser, testActionClassName, testActionMethodName);
        setInValue("userId", "1234567890123456789");
        setInValue("locale", "j");
        Result result = searchUserDataCtrl.doValidate(getRvContext());
        assertThat(result, is(Result.SUCCESS));
        assertThat(getErrorMessages("viewValues.userId"), nullValue());
        assertThat(getErrorMessages("viewValues.locale"), nullValue());
    }

    /**
     * doValidate(RvContext rvContext)メソッド 正常系テスト03.<br />
     * <br />
     * [テスト内容]<br />
     * userId文字列がnull<br />
     * locale文字列がnullで渡ってきた際にvalidateする事をテストする。<br />
     * <br />
     * [前提条件]<br />
     * rvContextのinValueにuserIdがnullでセットされている。<br />
     * rvContextのinValueにlocaleがnullでセットされている。<br />
     * <br />
     * [確認項目]<br />
     * Result.SUCCESSが返ってくる事。<br />
     * エラーメッセージが返ってこない事。<br />
     */
    @Test
    public void testDoValidateNormal03() {
        init(loginUser, testActionClassName, testActionMethodName);
        setInValue("userId", null);
        setInValue("locale", null);
        Result result = searchUserDataCtrl.doValidate(getRvContext());
        assertThat(result, is(Result.SUCCESS));
        assertThat(getErrorMessages("viewValues.userId"), nullValue());
        assertThat(getErrorMessages("viewValues.locale"), nullValue());
    }

    /**
     * doValidate(RvContext rvContext)メソッド 正常系テスト04.<br />
     * <br />
     * [テスト内容]<br />
     * userId文字列が空文字<br />
     * locale文字列が空文字で渡ってきた際にvalidateする事をテストする。<br />
     * <br />
     * [前提条件]<br />
     * rvContextのinValueにuserIdが空文字でセットされている。<br />
     * rvContextのinValueにlocaleが空文字でセットされている。<br />
     * <br />
     * [確認項目]<br />
     * Result.SUCCESSが返ってくる事。<br />
     * エラーメッセージが返ってこない事。<br />
     */
    @Test
    public void testDoValidateNormal04() {
        init(loginUser, testActionClassName, testActionMethodName);
        setInValue("userId", "");
        setInValue("locale", "");
        Result result = searchUserDataCtrl.doValidate(getRvContext());
        assertThat(result, is(Result.SUCCESS));
        assertThat(getErrorMessages("viewValues.userId"), nullValue());
        assertThat(getErrorMessages("viewValues.locale"), nullValue());
    }

    /**
     * doValidate(RvContext rvContext)メソッド 異常系テスト01.<br />
     * <br />
     * [テスト内容]<br />
     * userId文字列が21バイト<br />
     * locale文字列が3バイトで渡ってきた際にvalidateする事をテストする。<br />
     * <br />
     * [前提条件]<br />
     * rvContextのinValueにuserIdが21バイトでセットされている。<br />
     * rvContextのinValueにlocaleが3バイトでセットされている。<br />
     * <br />
     * [確認項目]<br />
     * Result.FAILUREが返ってくる事。<br />
     * エラーメッセージが返ってくる事。<br />
     */
    @Test
    public void testDoValidateDetectFailure01() {
        init(loginUser, testActionClassName, testActionMethodName);
        setInValue("userId", "123456789012345678901");
        setInValue("locale", "123");
        Result result = searchUserDataCtrl.doValidate(getRvContext());
        assertThat(result.toString(), is(Result.FAILURE.toString()));
        assertThat(getErrorMessages("viewValues.userId").get(0), is("ユーザIDが最大バイト数20バイトを越えています。"));
        assertThat(getErrorMessages("viewValues.locale").get(0), is("ロケールが最大バイト数2バイトを越えています。"));
    }

    /**
     * doLogic(RvContext rvContext)メソッド 正常系テスト01.<br />
     * <br />
     * [テスト内容]<br />
     * UserData検索ビジネスロジック処理のテストをする。<br />
     * <br />
     * [前提条件]<br />
     * UserDataDaoImplのメソッドがMockオブジェクトから値を返すように設定されている。<br />
     * UserDataDaoImplのsearchPageWhereMapメソッドが空でないListを返すように設定されている。<br />
     * <br />
     * [確認項目]<br />
     * Result.SUCCESSが返ってくる事。<br />
     * エラーメッセージが返ってこない事。<br />
     * 処理結果の値maxPage, nowPage, userDatasのチェックが想定通りである事。<br />
     * Function Scopeの値userId, localeが引き継がれている事。<br />
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDoLogicNormal01() {
        Mockit.redefineMethods(UserDataDaoImpl.class, UserDataDaoMockImpl.class);
        init(loginUser, testActionClassName, testActionMethodName);
        String userId = "123";
        setInValue("userId", userId);
        String locale = "jp";
        setInValue("locale", locale);
        Result result = searchUserDataCtrl.doLogic(getRvContext());
        assertThat(result, is(Result.SUCCESS));
        assertThat((Long) getResultValue("maxPage"), is(MAX_PAGE));
        assertThat((Long) getResultValue("nowPage"), is(Long.valueOf(1)));
        assertThat(((List<UserData>) getResultValue("userDatas")).size(), is(1));
        assertThat(getErrorMessages("viewValues.search"), nullValue());
        SearchUserDataConditionDto searchUserDataConditionDto = getFunctionScope("searchUserDataConditionDto");
        assertThat(searchUserDataConditionDto.userId, is(userId));
        assertThat(searchUserDataConditionDto.locale, is(locale));
    }

    /**
     * doLogic(RvContext rvContext)メソッド 正常系テスト02.<br />
     * <br />
     * [テスト内容]<br />
     * UserData検索ビジネスロジック処理のテストをする。<br />
     * <br />
     * [前提条件]<br />
     * UserDataDaoImplのメソッドがMockオブジェクトから値を返すように設定されている。<br />
     * UserDataDaoImplのsearchPageWhereMapメソッドが空のListを返すように設定されている。<br />
     * <br />
     * [確認項目]<br />
     * Result.SUCCESSが返ってくる事。<br />
     * エラーメッセージが返ってくる事。<br />
     * 処理結果の値maxPage, nowPage, userDatasのチェックが想定通りである事。<br />
     * Function Scopeの値userId, localeが引き継がれている事。<br />
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDoLogicNormal02() {
        Mockit.redefineMethods(UserDataDaoImpl.class, UserDataDaoFailureMockImpl.class);
        init(loginUser, testActionClassName, testActionMethodName);
        String userId = "123";
        setInValue("userId", userId);
        String locale = "jp";
        setInValue("locale", locale);
        Result result = searchUserDataCtrl.doLogic(getRvContext());
        assertThat(result, is(Result.SUCCESS));
        assertThat((Long) getResultValue("maxPage"), is(MAX_PAGE));
        assertThat((Long) getResultValue("nowPage"), is(Long.valueOf(1)));
        assertThat(((List<UserData>) getResultValue("userDatas")).size(), is(0));
        assertThat(getErrorMessages("viewValues.search").get(0), is("検索結果がありません。"));
        SearchUserDataConditionDto searchUserDataConditionDto = getFunctionScope("searchUserDataConditionDto");
        assertThat(searchUserDataConditionDto.userId, is(userId));
        assertThat(searchUserDataConditionDto.locale, is(locale));
    }

    /**
     * setReturnViewValues(RvContext rvContext)メソッド 正常系テスト01.<br />
     * <br />
     * [テスト内容]<br />
     * ビジネスロジックの処理結果を渡してviewValuesに変換される事をテストする。<br />
     * <br />
     * [前提条件]<br />
     * ビジネスロジックの処理結果userDatas, maxPage, nowPageがresultValueにセットされている事。<br />
     * <br />
     * [確認項目]<br />
     * viewValuesに変換された値が想定されている値である事。<br />
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSetReturnViewValuesNormal01() {
        init(loginUser, testActionClassName, testActionMethodName);
        List<UserData> userDatas = new ArrayList<UserData>();
        userDatas.add(new UserData());
        setResultValue("userDatas", userDatas);
        Long maxPage = Long.valueOf(10);
        setResultValue("maxPage", maxPage);
        Long nowPage = Long.valueOf(1);
        setResultValue("nowPage", nowPage);
        searchUserDataCtrl.setReturnViewValues(getRvContext());
        Map<String, Object> viewValues = getAttributeValue("viewValues");
        assertThat(((List<UserData>) viewValues.get("userDatas")).size(), is(1));
        assertThat((Long) viewValues.get("maxPage"), is(maxPage));
        assertThat((Long) viewValues.get("nowPage"), is(nowPage));
    }

    /**
     * UserDataDaoMockImpl.
     * 
     * @author redlly
     */
    public static final class UserDataDaoMockImpl {

        /**
         * searchPageWhereMap
         * 
         * @param nowPage
         * @param pageCount
         * @param whereMap
         * @return UserData List(size 1)
         */
        public List<UserData> searchPageWhereMap(Long nowPage, Long pageCount, Map<String, Object> whereMap) {
            List<UserData> list = new ArrayList<UserData>();
            list.add(new UserData());
            return list;
        }

        /**
         * searchMaxPageWhereMap
         * 
         * @param pageCount
         * @param whereMap
         * @return MAX_PAGE
         */
        public Long searchMaxPageWhereMap(Long pageCount, Map<String, Object> whereMap) {
            return MAX_PAGE;
        }
    }

    /**
     * UserDataDaoFailureMockImpl.
     * 
     * @author redlly
     */
    public static final class UserDataDaoFailureMockImpl {

        /**
         * searchPageWhereMap
         * 
         * @param nowPage
         * @param pageCount
         * @param whereMap
         * @return UserData List(size 0)
         */
        public List<UserData> searchPageWhereMap(Long nowPage, Long pageCount, Map<String, Object> whereMap) {
            return new ArrayList<UserData>();
        }

        /**
         * searchMaxPageWhereMap
         * 
         * @param pageCount
         * @param whereMap
         * @return MAX_PAGE
         */
        public Long searchMaxPageWhereMap(Long pageCount, Map<String, Object> whereMap) {
            return MAX_PAGE;
        }
    }
}
