package com.ballroomregistrar.compinabox.web.action;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import com.ballroomregistrar.compinabox.web.action.account.AccountActionSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ IndexTest.class, LoginTest.class, AccountActionSuite.class })
public class ActionSuite {
}
