package com.seitenbau.testing.shared.dao;

import com.seitenbau.testing.shared.model.RecordedTest;
import com.seitenbau.testing.shared.model.RecordedTestCase;

public interface IRecordedTestDao extends IBasicDao<RecordedTest> {

    public RecordedTest createNew(RecordedTestCase rrc);
}
