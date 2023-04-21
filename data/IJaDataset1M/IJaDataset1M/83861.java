package es.seat131.viewerfree.util;

import static org.junit.Assert.assertEquals;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import es.seat131.viewerfree.common.Action;
import es.seat131.viewerfree.common.ParamKey;

public class UrlComposerTest {

    private static final String PARAM_ENCRIPTED = "w3rHHf%2BsLp3irm%2FKIYS25abWFTvWBQTcXoJftu9XLc8SrElzHk6tlwTWh0nzzwWR";

    private static final String PEPE = "pepe";

    private static final String FOTO = "FOTO";

    private static final String ALBUM_NAME = "ALBUM_NAME";

    public static final String URL = "/main/" + Action.SHOW_PICTURE + "/?" + ParamKey.ALBUM_NAME + "=" + ALBUM_NAME + "&" + ParamKey.PICTURE_NAME + "=" + FOTO + "&";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getUrlMain() {
        assertEquals(URL, UrlComposer.getUrlMain(Action.SHOW_PICTURE, new ParamKey[] { ParamKey.ALBUM_NAME, ParamKey.PICTURE_NAME }, new String[] { ALBUM_NAME, FOTO }));
    }

    @Test
    public void getUrlMainEncripter() throws UnsupportedEncodingException {
        assertEquals("/main/" + Action.SHOW_PICTURE.getValue() + "/?" + PARAM_ENCRIPTED, UrlComposer.getUrlMainEncripted(Action.SHOW_PICTURE, new ParamKey[] { ParamKey.ALBUM_NAME, ParamKey.PICTURE_NAME }, new String[] { ALBUM_NAME, FOTO }, PEPE));
    }

    @Test
    public void getParams() throws UnsupportedEncodingException {
        Map<String, String> params = UrlComposer.getParams(URLDecoder.decode(PARAM_ENCRIPTED, "utf-8"), PEPE);
        assertEquals(ALBUM_NAME, params.get(ParamKey.ALBUM_NAME.toString()));
        assertEquals(FOTO, params.get(ParamKey.PICTURE_NAME.toString()));
    }

    @Test
    public void getParamsNull() throws UnsupportedEncodingException {
        Map<String, String> params = UrlComposer.getParams(null, PEPE);
        assertEquals(0, params.size());
    }

    @Test
    public void getParamsNEmpty() throws UnsupportedEncodingException {
        Map<String, String> params = UrlComposer.getParams("", PEPE);
        assertEquals(0, params.size());
    }

    @Test
    public void getParamsNullKey() throws UnsupportedEncodingException {
        Map<String, String> params = UrlComposer.getParams(URLDecoder.decode(PARAM_ENCRIPTED, "utf-8"), null);
        assertEquals(0, params.size());
    }

    @Test
    public void getParamsEmptyKey() throws UnsupportedEncodingException {
        Map<String, String> params = UrlComposer.getParams(URLDecoder.decode(PARAM_ENCRIPTED, "utf-8"), "");
        assertEquals(0, params.size());
    }

    @Test
    public void getParamsTokeInvalid() throws UnsupportedEncodingException {
        Map<String, String> params = UrlComposer.getParams(URLDecoder.decode(PARAM_ENCRIPTED, "utf-8"), "Lala");
        assertEquals(0, params.size());
    }

    @Test
    public void getParamsNoParams() throws UnsupportedEncodingException {
        Map<String, String> params = UrlComposer.getParams(CriptoUtil.encrypt("holaaaaa", PEPE), PEPE);
        assertEquals(0, params.size());
    }
}
