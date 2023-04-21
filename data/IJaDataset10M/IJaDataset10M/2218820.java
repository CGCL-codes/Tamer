package com.appspot.ajnweb.service;

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import appengine.test.util.AppEngineTestUtil;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * {@link ApplicationSettingService}のテストケース。
 * @author shin1ogawa
 */
public class ApplicationSettingServiceTest {

    /**
	 * 設定キーに対応する値が保存されている状態で、値を取得する。
	 */
    @Test
    public void 存在する設定値を取得する() {
        ApplicationSettingService.put(ApplicationSettingService.SettingKey.TWITTER_OAUTH_CONSUMER_KEY, "hoge");
        String value = ApplicationSettingService.get(ApplicationSettingService.SettingKey.TWITTER_OAUTH_CONSUMER_KEY, "default");
        assertThat(value, is(equalTo("hoge")));
    }

    /**
	 * 設定キーに対応する値が保存されていない状態で、値を取得する。
	 */
    @Test
    public void 存在しない設定値を取得する() {
        String value = ApplicationSettingService.get(ApplicationSettingService.SettingKey.TWITTER_OAUTH_CONSUMER_KEY, "default");
        assertThat(value, is(equalTo("default")));
    }

    LocalServiceTestHelper helper;

    /**
	 * テスト環境を起動する。
	 * @throws IOException
	 */
    @Before
    public void setUp() throws IOException {
        helper = AppEngineTestUtil.setUpAppEngine(new LocalEnvironment("ajn-web", "serviceTest"), "target/serviceTest", "war", true);
    }

    /**
	 * テスト環境を終了する。
	 */
    @After
    public void tearDown() {
        AppEngineTestUtil.tearDownAppEngine(helper);
    }
}
