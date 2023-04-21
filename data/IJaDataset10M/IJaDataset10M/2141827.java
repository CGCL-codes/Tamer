package net.sf.revolver.sample.service.roledata.list;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import mockit.Mockit;
import net.sf.bulletlib.authentication.core.LoginUser;
import net.sf.bulletlib.authentication.s2jdbc.RoleData;
import net.sf.revolver.core.Result;
import net.sf.revolver.sample.dao.RoleDataDaoImpl;
import net.sf.revolver.sample.dto.SearchRoleDataConditionDto;
import net.sf.revolver.sample.dto.SearchUserDataConditionDto;
import net.sf.revolver.units2.RvS2TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;

/**
 * ListRoleDataDoTest.
 * 
 * @author redlly
 */
@RunWith(Seasar2.class)
public class ListRoleDataDoTest extends RvS2TestCase {

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
     * ListRoleDataDo.
     */
    private ListRoleDataDo listRoleDataDo;

    /**
     * doLogic(RvContext rvContext)メソッド 正常系テスト01.<br />
     * <br />
     * [テスト内容]<br />
     * 検索結果がある場合のRoleData検索処理のテストする。<br />
     * <br />
     * [前提条件]<br />
     * RoleDataDaoImplのメソッドがMockオブジェクトから値を返すように設定されている。<br />
     * rvContextのinValueのroleNameに値がセットされている。<br />
     * <br />
     * [確認項目]<br />
     * Result.SUCCESSが返ってくる事。<br />
     * 処理結果の値が引き継がれている事。<br />
     * Function Scopeに値がセットされている事。<br />
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDoLogicNormal01() {
        Mockit.redefineMethods(RoleDataDaoImpl.class, RoleDataDaoMockImpl.class);
        init(loginUser, testActionClassName, testActionMethodName);
        setInValue("action", "same");
        String roleName = "123456789012345678901234567890";
        setInValue("roleName", roleName);
        SearchRoleDataConditionDto searchRoleDataConditionDto = new SearchRoleDataConditionDto();
        searchRoleDataConditionDto.roleName = roleName;
        setFunctionScope("searchRoleDataConditionDto", searchRoleDataConditionDto);
        Result result = listRoleDataDo.doLogic(getRvContext());
        assertThat(result, is(Result.SUCCESS));
        List<RoleData> roleDatas = (List<RoleData>) getResultValue("roleDatas");
        assertThat((Long) getResultValue("maxPage"), is(Long.valueOf(10)));
        assertThat((Long) getResultValue("nowPage"), is(Long.valueOf(1)));
        assertThat(roleDatas.size(), is(1));
        searchRoleDataConditionDto = getFunctionScope("searchRoleDataConditionDto");
        assertThat(searchRoleDataConditionDto.nowPage, is(Long.valueOf(1)));
    }

    /**
     * doLogic(RvContext rvContext)メソッド 正常系テスト01.<br />
     * <br />
     * [テスト内容]<br />
     * 検索結果がある場合のRoleData検索処理のテストする。<br />
     * <br />
     * [前提条件]<br />
     * RoleDataDaoImplのメソッドがMockオブジェクトから値を返すように設定されている。<br />
     * rvContextのinValueのroleNameに値がセットされている。<br />
     * <br />
     * [確認項目]<br />
     * Result.FAILUREが返ってくる事。<br />
     * 処理結果の値が引き継がれていない事。<br />
     * Function Scopeに値がセットされていない事。<br />
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDoLogicNormal02() {
        Mockit.redefineMethods(RoleDataDaoImpl.class, RoleDataDaoMockImpl.class);
        init(loginUser, testActionClassName, testActionMethodName);
        setInValue("action", null);
        String roleName = "123456789012345678901234567890";
        setInValue("roleName", roleName);
        SearchRoleDataConditionDto searchRoleDataConditionDto = new SearchRoleDataConditionDto();
        searchRoleDataConditionDto.roleName = roleName;
        setFunctionScope("searchRoleDataConditionDto", searchRoleDataConditionDto);
        Result result = listRoleDataDo.doLogic(getRvContext());
        assertThat(result, is(Result.FAILURE));
        List<RoleData> roleDatas = (List<RoleData>) getResultValue("roleDatas");
        assertThat(getResultValue("maxPage"), nullValue());
        assertThat(getResultValue("nowPage"), nullValue());
        assertThat(roleDatas, nullValue());
        searchRoleDataConditionDto = getFunctionScope("searchRoleDataConditionDto");
        assertThat(searchRoleDataConditionDto.nowPage, is(Long.valueOf(1)));
    }

    /**
     * getRoleDatas(RvContext rvContext, Long nowPage, Map < String, Object > whereMap)メソッド 正常系テスト01.<br />
     * <br />
     * [テスト内容]<br />
     * RoleData List取得処理ができる事をテストする。<br />
     * <br />
     * [前提条件]<br />
     * RoleDataDaoImplのメソッドがMockオブジェクトから値を返すように設定されている。<br />
     * roleDataDaoImplのsearchPageWhereMapメソッドが空のListを返さない事。<br />
     * <br />
     * [確認項目]<br />
     * RoleData List取得が想定されている値である事。<br />
     * エラーメッセージが返ってこない事。<br />
     */
    @Test
    public void testGetRoleDatasNormal01() {
        Mockit.redefineMethods(RoleDataDaoImpl.class, RoleDataDaoMockImpl.class);
        init(loginUser, testActionClassName, testActionMethodName);
        List<RoleData> roleDatas = listRoleDataDo.getRoleDatas(getRvContext(), Long.valueOf(1), new HashMap<String, Object>());
        assertThat(getErrorMessages("viewValues.search"), nullValue());
        assertThat(roleDatas.size(), is(1));
    }

    /**
     * getRoleDatas(RvContext rvContext, Long nowPage, Map < String, Object > whereMap)メソッド 正常系テスト02.<br />
     * <br />
     * [テスト内容]<br />
     * RoleData List取得処理が0件の時、エラーメッセージが追加されている事をテストする。<br />
     * <br />
     * [前提条件]<br />
     * RoleDataDaoImplのメソッドがMockオブジェクトから値を返すように設定されている。<br />
     * roleDataDaoImplのsearchPageWhereMapメソッドが空のListを返す事。<br />
     * <br />
     * [確認項目]<br />
     * RoleData List取得が想定されている値である事。<br />
     * エラーメッセージが返ってくる事。<br />
     */
    @Test
    public void testGetRoleDatasNormal02() {
        Mockit.redefineMethods(RoleDataDaoImpl.class, RoleDataDaoNotFindDataMockImpl.class);
        init(loginUser, testActionClassName, testActionMethodName);
        List<RoleData> roleDatas = listRoleDataDo.getRoleDatas(getRvContext(), Long.valueOf(1), new HashMap<String, Object>());
        assertThat(getErrorMessages("viewValues.search").get(0), is("検索結果がありません。"));
        assertThat(roleDatas.size(), is(0));
    }

    /**
     * getNowPage((RvContext rvContext, PageTransitionDto searchRoleDataConditionDto,
            Map<String, Object> whereMap, Long maxPage)メソッド 正常系テスト01.<br />
     * <br />
     * [テスト内容]<br />
     * 現在のページ数の取得処理ができる事をテストする。<br />
     * <br />
     * [前提条件]<br />
     * UserDataDaoImplのメソッドがMockオブジェクトから値を返すように設定されている。<br />
     * nowPage < maxPageである事。<br />
     * <br />
     * [確認項目]<br />
     * 現在のページ数が想定されている値である事。<br />
     */
    @Test
    public void testGetNowPageNormal01() {
        init(loginUser, testActionClassName, testActionMethodName);
        setInValue("action", "1");
        Long nowPage = listRoleDataDo.getNowPage(getRvContext(), new SearchUserDataConditionDto(), new HashMap<String, Object>(), Long.valueOf(10));
        assertThat(getErrorMessages("viewValues.search"), nullValue());
        assertThat(nowPage, is(Long.valueOf(1)));
    }

    /**
     * getNowPage(RvContext rvContext, PageTransitionDto searchUserDataConditionDto, Map < String, Object > whereMap,
     *  Long maxPage)メソッド 正常系テスト02.<br />
     * <br />
     * [テスト内容]<br />
     * 現在のページ数の取得処理ができる事をテストする。<br />
     * <br />
     * [前提条件]<br />
     * UserDataDaoImplのメソッドがMockオブジェクトから値を返すように設定されている。<br />
     * nowPageがnullで返される事。<br />
     * <br />
     * [確認項目]<br />
     * 現在のページ数がnullである事。<br />
     * エラーメッセージが返ってくる事。<br />
     */
    @Test
    public void testGetNowPageNormal02() {
        init(loginUser, testActionClassName, testActionMethodName);
        setInValue("action", null);
        Long nowPage = listRoleDataDo.getNowPage(getRvContext(), new SearchUserDataConditionDto(), new HashMap<String, Object>(), Long.valueOf(0));
        assertThat(getErrorMessages("viewValues.search").get(0), is("該当ページが存在しません。"));
        assertThat(nowPage, nullValue());
    }

    /**
     * getNowPage(RvContext rvContext, PageTransitionDto searchUserDataConditionDto, Map < String, Object > whereMap,
     *  Long maxPage)メソッド 正常系テスト03.<br />
     * <br />
     * [テスト内容]<br />
     * 現在のページ数の取得処理ができる事をテストする。<br />
     * <br />
     * [前提条件]<br />
     * UserDataDaoImplのメソッドがMockオブジェクトから値を返すように設定されている。<br />
     * nowPageに不正な値が入力されNumberFormatExceptionが発行される事。<br />
     * <br />
     * [確認項目]<br />
     * 現在のページ数がnullである事。<br />
     * エラーメッセージが返ってくる事。<br />
     */
    @Test
    public void testGetNowPageNormal03() {
        init(loginUser, testActionClassName, testActionMethodName);
        setInValue("action", "aaaa");
        Long nowPage = listRoleDataDo.getNowPage(getRvContext(), new SearchUserDataConditionDto(), new HashMap<String, Object>(), Long.valueOf(10));
        assertThat(getErrorMessages("viewValues.search").get(0), is("該当ページが存在しません。"));
        assertThat(nowPage, nullValue());
    }

    /**
     * RoleDataDaoMockImpl.
     * 
     * @author redlly
     */
    public static class RoleDataDaoMockImpl {

        /**
         * searchMaxPageWhereMap
         *
         * @param pageCount
         * @param whereMap
         * @return Long.valueOf(10)
         */
        public Long searchMaxPageWhereMap(Long pageCount, Map<String, Object> whereMap) {
            return Long.valueOf(10);
        }

        /**
         * searchPageWhereMap.
         *
         * @param nowPage
         * @param pageCount
         * @param whereMap
         * @return RoleData List
         */
        public List<RoleData> searchPageWhereMap(Long nowPage, Long pageCount, Map<String, Object> whereMap) {
            List<RoleData> roleDatas = new ArrayList<RoleData>();
            roleDatas.add(new RoleData());
            return roleDatas;
        }
    }

    /**
     * RoleDataDaoNotFindDataMockImpl.
     * 
     * @author redlly
     */
    public static class RoleDataDaoNotFindDataMockImpl {

        /**
         * searchPageWhereMap.
         *
         * @param nowPage
         * @param pageCount
         * @param whereMap
         * @return RoleData List
         */
        public List<RoleData> searchPageWhereMap(Long nowPage, Long pageCount, Map<String, Object> whereMap) {
            return new ArrayList<RoleData>();
        }
    }
}
