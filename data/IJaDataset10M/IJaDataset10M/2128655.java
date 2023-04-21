package research.domain;

import java.util.Set;
import research.entity.Entity;
import research.entity.EntityType;

public final class TestParameter extends Entity {

    private String name;

    private String description;

    private String units;

    private boolean reported;

    private TestType testType;

    private Set<TestParameterValue> testParameterValues;

    public TestParameter() {
        super(EntityType.TestParameter);
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    public Set<TestParameterValue> getTestParameterValues() {
        return testParameterValues;
    }

    public void setTestParameterValues(Set<TestParameterValue> testParameterValues) {
        this.testParameterValues = testParameterValues;
    }

    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean isReported) {
        this.reported = isReported;
    }

    public static TestParameter getNew() {
        TestParameter p = new TestParameter();
        p.setName("");
        p.setDescription("");
        p.setUnits("");
        return p;
    }
}
