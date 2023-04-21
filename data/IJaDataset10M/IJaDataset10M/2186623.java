package ca.sqlpower.architect;

import ca.sqlpower.architect.util.ArchitectNewValueMaker;
import ca.sqlpower.object.PersistedSPObjectTest;
import ca.sqlpower.object.SPObject;
import ca.sqlpower.sql.DataSourceCollection;
import ca.sqlpower.sql.SPDataSource;
import ca.sqlpower.testutil.NewValueMaker;

public class TestProjectSettings extends PersistedSPObjectTest {

    private ProjectSettings settings;

    public TestProjectSettings(String name) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        settings = new ProjectSettings();
        getRootObject().addChild(settings, 0);
    }

    @Override
    protected Class<? extends SPObject> getChildClassType() {
        return null;
    }

    @Override
    public SPObject getSPObjectUnderTest() {
        return settings;
    }

    @Override
    public NewValueMaker createNewValueMaker(SPObject root, DataSourceCollection<SPDataSource> dsCollection) {
        return new ArchitectNewValueMaker(root, dsCollection);
    }
}
