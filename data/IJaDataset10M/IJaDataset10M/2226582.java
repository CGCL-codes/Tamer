package net.sf.revolver.core;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import net.sf.bulletlib.core.logging.Logging;
import org.junit.Test;

public class BusinessLogicExceptionTest {

    /**
     * BusinessLogicException テスト01.<br />
     * <br />
     * [テスト内容]<br />
     * BusinessLogicExceptionから元のExceptionを取得する。<br />
     * [前提条件]<br />
     * BusinessLogicExceptionにClassCastExceptionを内容させてインスタンス化させる。<br />
     * [事後条件]<br />
     * getCauseExceptionメソッドでClassCastExceptionを取得できること。<br />
     */
    @Test
    public void businessLogicExceptionTest01() throws Exception {
        RvContext rvContext = new RvContext("testId", true);
        ClassCastException exception = new ClassCastException("意図的に発生");
        BusinessLogicException businessLogicException = new BusinessLogicException(rvContext, exception);
        Logging.info(this.getClass(), "TEST99999", rvContext.getLoginId(), "ClassCastExceptionのログが出力されない事");
        assertThat(businessLogicException.getCauseException(), instanceOf(ClassCastException.class));
    }

    /**
     * BusinessLogicException テスト02.<br />
     * <br />
     * [テスト内容]<br />
     * BusinessLogicExceptionから元のExceptionを取得する。<br />
     * [前提条件]<br />
     * BusinessLogicExceptionにClassCastExceptionを内容させてインスタンス化させる。<br />
     * [事後条件]<br />
     * getCauseExceptionメソッドでClassCastExceptionを取得できること。<br />
     */
    @Test
    public void businessLogicExceptionTest02() throws Exception {
        RvContext rvContext = new RvContext("testId", false);
        ClassCastException exception = new ClassCastException("意図的に発生");
        BusinessLogicException businessLogicException = new BusinessLogicException(rvContext, exception);
        Logging.info(this.getClass(), "TEST99999", rvContext.getLoginId(), "ClassCastExceptionのログが出力される事");
        assertThat(businessLogicException.getCauseException(), instanceOf(ClassCastException.class));
    }
}
