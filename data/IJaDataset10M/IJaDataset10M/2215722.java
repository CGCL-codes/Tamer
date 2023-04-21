package de.schwarzrot.data.test.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.schwarzrot.data.support.AbstractEntity;

public class TestGroupUnnamedChildEntity extends AbstractEntity {

    private static final long serialVersionUID = 713L;

    private Map<Integer, List<SimpleTestEntity>> children;

    public TestGroupUnnamedChildEntity() {
        children = new HashMap<Integer, List<SimpleTestEntity>>();
    }

    @Override
    public Map<String, String> getMappings() {
        Map<String, String> mappings = super.getMappings();
        mappings.put("children", "weight|" + SimpleTestEntity.class.getName());
        return mappings;
    }

    public final void setChildren(Map<Integer, List<SimpleTestEntity>> children) {
        this.children = children;
    }

    public final Map<Integer, List<SimpleTestEntity>> getchildren() {
        return children;
    }

    @Override
    public String getPersistenceName() {
        return "tguce";
    }

    @Override
    public String getSchemaName() {
        return SimpleTestEntity.TEST_SCHEMA;
    }
}
