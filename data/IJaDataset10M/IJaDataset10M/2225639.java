package com.firescrum.testmodule.service;

import java.util.List;
import com.firescrum.testmodule.model.TestPriority;

public interface IServiceTestPriority {

    TestPriority addTestPriority(TestPriority testPriority);

    List<TestPriority> getAllTestPriority();
}
