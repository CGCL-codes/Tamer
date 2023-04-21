package de.schwarzrot.data.test.dom2;

import java.util.ArrayList;
import java.util.List;
import de.schwarzrot.data.ChildEntity;
import de.schwarzrot.data.support.AbstractEntity;

public class TestRSChild extends AbstractEntity implements ChildEntity<TestMasterEntity> {

    private static final long serialVersionUID = 713L;

    private TestMasterEntity parent;

    private String name;

    private int weight;

    private int ordinal;

    @Override
    public TestMasterEntity getParent() {
        return parent;
    }

    @Override
    public Class<TestMasterEntity> getParentType() {
        return TestMasterEntity.class;
    }

    @Override
    public void setParent(TestMasterEntity parent) {
        this.parent = parent;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final int getWeight() {
        return weight;
    }

    public final void setWeight(int weight) {
        this.weight = weight;
    }

    public final int getOrdinal() {
        return ordinal;
    }

    public final void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public String getSchemaName() {
        return TestMasterEntity.TEST_SCHEMA;
    }

    @Override
    public String getPersistenceName() {
        return "trce";
    }

    @Override
    public List<String> getUniqColumnNames() {
        List<String> rv = new ArrayList<String>();
        rv.add("parent");
        rv.add("name");
        return rv;
    }
}
