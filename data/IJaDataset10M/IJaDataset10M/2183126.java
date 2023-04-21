package com.thoughtworks.fireworks.core;

import com.thoughtworks.shadow.TestShadowResult;

public interface ResultOfTestEndListener {

    void testEnd(TestShadowResult result);
}
