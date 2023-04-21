package com.googlecode.formomatic.test;

import com.googlecode.formomatic.Builder;
import com.googlecode.formomatic.Resource;
import com.googlecode.formomatic.bean.Config;
import com.googlecode.formomatic.exception.MalformedConfException;
import java.io.InputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParseTest {

    InputStream is;

    String file;

    Config conf;

    @Before
    public void setUp() {
        file = "/com/googlecode/formomatic/test/config.yml";
        is = Resource.getResourceAsInputStream(file);
    }

    @Test
    public void config() throws MalformedConfException {
        conf = Builder.prepare(is);
    }
}
