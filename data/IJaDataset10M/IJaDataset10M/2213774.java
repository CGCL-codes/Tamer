package com.ballroomregistrar.compinabox.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ com.ballroomregistrar.compinabox.web.action.ActionSuite.class, com.ballroomregistrar.compinabox.web.interceptors.InterceptorSuite.class })
public class WebSuite {
}
