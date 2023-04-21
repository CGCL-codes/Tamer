package bufferings.ktr.wjr.server.fortest;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.util.logging.Logger;
import org.junit.Ignore;
import org.junit.Test;

public class 日本語テストクラス {

    private static final Logger logger = Logger.getLogger(日本語テストクラス.class.getName());

    @Test
    public void 成功() {
        logger.info("成功メソッド");
    }

    @Test
    public void 失敗() {
        logger.info("失敗メソッド");
        assertThat("", is("aaaa"));
    }

    @Test
    public void エラー() {
        logger.info("エラーメソッド");
        throw new RuntimeException();
    }

    @Test
    @Ignore
    public void 無視() {
        logger.info("無視メソッド");
    }
}
