package com.jz.manage;

import java.util.List;
import com.jz.model.Test;

public interface TestManage {

    public void insertTest(Test test);

    public void updateTest(Test test);

    public List<Test> testList();

    public Test getTest(int number);
}
