package com.nhncorp.cubridqa.test.console.util;

import org.junit.Test;
import com.nhncorp.cubridqa.console.util.SystemUtil;

public class TestSystemUtil {

    @Test
    public void testGetUserHomePath() {
        String path = SystemUtil.getUserHomePath();
        System.out.println(path);
    }
}
