package org.jia.ptrack.domain.hibernate;

import net.chrisrichardson.ormunit.hibernate.HibernateMappingTests;
import net.chrisrichardson.ormunit.hibernate.ListPropertyMapping;
import org.hibernate.engine.CascadeStyle;
import org.jia.ptrack.domain.Project;

public class HibernateProjectUserTypeFieldTests extends HibernateMappingTests {

    protected String[] getConfigLocations() {
        return HibernatePTrackTestConstants.PTRACK_APP_CTXS;
    }

    public void testMappingUserTypeFields() {
        assertClassMapping(Project.class, "Project");
        assertField("type", "TYPE", ProjectTypeUserType.class);
        assertListField("artifacts");
        ListPropertyMapping artifactsMapping = getListFieldMapping("artifacts");
        artifactsMapping.assertForeignKey("PROJECT_ID");
        artifactsMapping.assertIndexColumn("ARTIFACT_INDEX");
        artifactsMapping.assertTable("PROJECT_ARTIFACTS");
        artifactsMapping.assertElementClass(ArtifactTypeUserType.class);
        artifactsMapping.cascade(CascadeStyle.ALL);
    }
}
