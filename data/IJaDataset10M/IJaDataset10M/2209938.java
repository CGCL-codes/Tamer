package com.jeantessier.dependencyfinder.webwork;

import org.junit.runner.*;
import org.junit.runners.*;
import static org.junit.runners.Suite.*;

@RunWith(Suite.class)
@SuiteClasses({ TestActionBase.class, TestExtractAction.class })
public class TestAll {
}
