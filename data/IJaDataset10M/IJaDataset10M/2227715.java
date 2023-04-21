package org.pyre.foundry.util.validation;

import java.util.Date;
import java.util.List;

public class TestEntity {

    private String testString;

    private Date testDate;

    private Integer testInteger;

    private List<TestEntity> subListEntity;

    private TestEntity subEntity;

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public Integer getTestInteger() {
        return testInteger;
    }

    public void setTestInteger(Integer testInteger) {
        this.testInteger = testInteger;
    }

    public List<TestEntity> getSubListEntity() {
        return subListEntity;
    }

    public void setSubListEntity(List<TestEntity> subListEntity) {
        this.subListEntity = subListEntity;
    }

    public TestEntity getSubEntity() {
        return subEntity;
    }

    public void setSubEntity(TestEntity subEntity) {
        this.subEntity = subEntity;
    }
}
