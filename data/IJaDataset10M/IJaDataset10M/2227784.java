package com.ballroomregistrar.compinabox.online.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ com.ballroomregistrar.compinabox.online.web.action.ActionSuite.class, com.ballroomregistrar.compinabox.online.web.converters.ConverterSuite.class, com.ballroomregistrar.compinabox.online.web.interceptors.InterceptorSuite.class })
public class WebSuite {
}
