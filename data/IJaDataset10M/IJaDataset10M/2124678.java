package net.sf.revolver.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class NullRvDoTest {

    /**
     * [Title] initメソッド正常系テスト.<br />
     * <br />
     * [Condition]<br />
     * 特になし<br />
     * <br />
     * [Item]<br />
     * 引数にRvContextを渡してExceptionが発生せずにRvContextの値が<br />
     * 変更されない事を確認する。
     * 
     * @throws Exception
     */
    @Test
    public void initTest() throws Exception {
        RvContext rvContext = new RvContext("testId", false);
        NullRvDo nullRvDo = new NullRvDo();
        nullRvDo.init(rvContext);
        assertThat(rvContext.getLoginId(), is("testId"));
        assertThat(rvContext.getAllErrorMessages().size(), is(0));
        assertThat(rvContext.getInValues().size(), is(0));
        assertThat(rvContext.getResultValues().size(), is(0));
        assertThat(rvContext.isNoPrintStackTraceFlag(), is(false));
        assertThat(rvContext.getReturnObject(), nullValue());
        assertThat(rvContext.getResult(), nullValue());
    }

    /**
     * [Title] initProcessTestメソッド正常系テスト<.br />
     * <br />
     * [Condition]<br />
     * 特になし<br />
     * <br />
     * [Item]<br />
     * 引数にRvContextを渡してExceptionが発生せずにRvContextの値が<br />
     * 変更されない事を確認する。
     *
     * @throws Exception
     */
    @Test
    public void initProcessTest() throws Exception {
        RvContext rvContext = new RvContext("testId", false);
        NullRvDo nullRvDo = new NullRvDo();
        nullRvDo.initProcess(rvContext);
        assertThat(rvContext.getLoginId(), is("testId"));
        assertThat(rvContext.getAllErrorMessages().size(), is(0));
        assertThat(rvContext.getInValues().size(), is(0));
        assertThat(rvContext.getResultValues().size(), is(0));
        assertThat(rvContext.isNoPrintStackTraceFlag(), is(false));
        assertThat(rvContext.getReturnObject(), nullValue());
        assertThat(rvContext.getResult(), nullValue());
    }

    /**
     * [Title] endメソッド正常系テスト.<br />
     * <br />
     * [Condition]<br />
     * 特になし<br />} catch (Exception e) {
     *
     * fail("Exceptionは発生しないこと"); e.printStackTrace(); } }
     *
     * <br />
     * [Item]<br />
     * 引数にRvContextを渡してExceptionが発生せずにRvContextの値が<br />
     * 変更されない事を確認する。
     *
     * @throws Exception
     */
    @Test
    public void endTest() throws Exception {
        RvContext rvContext = new RvContext("testId", false);
        NullRvDo nullRvDo = new NullRvDo();
        nullRvDo.end(rvContext);
        assertThat(rvContext.getLoginId(), is("testId"));
        assertThat(rvContext.getAllErrorMessages().size(), is(0));
        assertThat(rvContext.getInValues().size(), is(0));
        assertThat(rvContext.getResultValues().size(), is(0));
        assertThat(rvContext.isNoPrintStackTraceFlag(), is(false));
        assertThat(rvContext.getReturnObject(), nullValue());
        assertThat(rvContext.getResult(), nullValue());
    }

    /**
     * [Title] endProcessメソッド正常系テスト.<br />
     * <br />
     * [Condition]<br />
     * 特になし<br />
     * <br />
     * [Item]<br />
     * 引数にRvContextを渡してExceptionが発生せずにRvContextの値が<br />
     * 変更されない事を確認する。
     *
     * @throws Exception
     */
    @Test
    public void endProcessTest() throws Exception {
        RvContext rvContext = new RvContext("testId", false);
        NullRvDo nullRvDo = new NullRvDo();
        nullRvDo.endProcess(rvContext);
        assertThat(rvContext.getLoginId(), is("testId"));
        assertThat(rvContext.getAllErrorMessages().size(), is(0));
        assertThat(rvContext.getInValues().size(), is(0));
        assertThat(rvContext.getResultValues().size(), is(0));
        assertThat(rvContext.isNoPrintStackTraceFlag(), is(false));
        assertThat(rvContext.getReturnObject(), nullValue());
        assertThat(rvContext.getResult(), nullValue());
    }

    /**
     * [Title] doBizLogicMainメソッド正常系テスト.<br />
     * <br />
     * [Condition]<br />
     * 特になし<br />
     * <br />
     * [Item]<br />
     * 引数にRvContextを渡してExceptionが発生せずにRvContextの値が<br />
     * result以外変更されない事を確認する。<br />
     * また、返り値のresultはSUCCESSである事を確認する。
     *
     * @throws Exception
     */
    @Test
    public void doBizLogicMainTest() throws Exception {
        RvContext rvContext = new RvContext("testId", false);
        NullRvDo nullRvDo = new NullRvDo();
        Result result = nullRvDo.doLogic(rvContext);
        assertThat(rvContext.getLoginId(), is("testId"));
        assertThat(rvContext.getAllErrorMessages().size(), is(0));
        assertThat(rvContext.getInValues().size(), is(0));
        assertThat(rvContext.getResultValues().size(), is(0));
        assertThat(rvContext.isNoPrintStackTraceFlag(), is(false));
        assertThat(rvContext.getReturnObject(), nullValue());
        assertThat(rvContext.getResult(), nullValue());
        assertThat(result, is(Result.SUCCESS));
    }
}
