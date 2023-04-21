package wilos.test.model.spem2.role;

import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wilos.model.spem2.guide.Guidance;
import wilos.model.spem2.role.RoleDefinition;
import wilos.model.spem2.role.RoleDescriptor;

public class RoleDefinitionTest {

    private RoleDefinition roleDefinition;

    public static final String NAME = "name";

    public static final String NAME2 = "name1";

    public static final String DESCRIPTION = "roleDescriptor description";

    public static final String DESCRIPTION2 = "description";

    public static final String PREFIX = "prefix";

    public static final Boolean IS_OPTIONAL = true;

    public static final Boolean HAS_MULTIPLE_OCCURENCES = true;

    public static final Boolean IS_PLANNED = true;

    @Before
    public void setUp() {
        this.roleDefinition = new RoleDefinition();
        this.roleDefinition.setDescription(DESCRIPTION);
        this.roleDefinition.setName(NAME);
    }

    @After
    public void tearDown() {
    }

    @Test
    public final void testClone() {
        try {
            assertEquals(this.roleDefinition.clone(), this.roleDefinition);
        } catch (CloneNotSupportedException e) {
            fail("Error CloneNotSupportedException in the testClone method");
        }
    }

    @Test
    public void testHashCode() {
        RoleDefinition rd = new RoleDefinition();
        rd.setDescription(DESCRIPTION);
        rd.setName(NAME);
        assertNotNull(this.roleDefinition.hashCode());
        assertNotNull(rd.hashCode());
        assertEquals(this.roleDefinition.hashCode(), rd.hashCode());
    }

    @Test
    public void testEquals() {
        RoleDefinition tmp = null;
        try {
            tmp = this.roleDefinition.clone();
        } catch (CloneNotSupportedException e) {
            fail("Error CloneNotSupportedException in the testEquals method");
        }
        assertTrue(this.roleDefinition.equals(tmp));
        RoleDefinition role = new RoleDefinition();
        role.setName("name2");
        role.setDescription(DESCRIPTION);
        assertFalse(this.roleDefinition.equals(role));
    }

    @Test
    public void testAddRoleDescriptor() {
        RoleDescriptor role = new RoleDescriptor();
        role.setName(NAME);
        role.setDescription(DESCRIPTION);
        this.roleDefinition.addRoleDescriptor(role);
        assertFalse(this.roleDefinition.getRoleDescriptors().isEmpty());
        assertTrue(this.roleDefinition.getRoleDescriptors().size() == 1);
    }

    @Test
    public void testRemoveRoleDescriptor() {
        RoleDescriptor role = new RoleDescriptor();
        role.setName(NAME);
        role.setDescription(DESCRIPTION);
        this.roleDefinition.addRoleDescriptor(role);
        assertFalse(this.roleDefinition.getRoleDescriptors().isEmpty());
        this.roleDefinition.removeRoleDescriptor(role);
        assertTrue(this.roleDefinition.getRoleDescriptors().isEmpty());
    }

    @Test
    public void testRemoveAllRoleDescriptor() {
        RoleDescriptor role = new RoleDescriptor();
        role.setName(NAME);
        role.setDescription(DESCRIPTION);
        RoleDescriptor tmp = new RoleDescriptor();
        tmp.setName(NAME2);
        tmp.setDescription(DESCRIPTION);
        Set<RoleDescriptor> set = new HashSet<RoleDescriptor>();
        set.add(role);
        set.add(tmp);
        this.roleDefinition.addAllRoleDescriptors(set);
        assertNotNull(role.getRoleDefinition());
        assertNotNull(tmp.getRoleDefinition());
        assertTrue(this.roleDefinition.getRoleDescriptors().size() == set.size());
        this.roleDefinition.removeAllRoleDescriptors();
        assertNull(role.getRoleDefinition());
        assertNull(tmp.getRoleDefinition());
        assertTrue(this.roleDefinition.getRoleDescriptors().isEmpty());
    }

    @Test
    public void testAddToAllRoleDescriptors() {
        RoleDescriptor role = new RoleDescriptor();
        role.setName(NAME2);
        role.setDescription(DESCRIPTION2);
        RoleDescriptor tmp = new RoleDescriptor();
        tmp.setName(NAME);
        tmp.setDescription(DESCRIPTION);
        Set<RoleDescriptor> set = new HashSet<RoleDescriptor>();
        set.add(role);
        set.add(tmp);
        this.roleDefinition.addAllRoleDescriptors(set);
        assertFalse(this.roleDefinition.getRoleDescriptors().isEmpty());
        assertTrue(this.roleDefinition.getRoleDescriptors().size() == 2);
        assertNotNull(role.getRoleDefinition());
        assertNotNull(tmp.getRoleDefinition());
    }

    @Test
    public void testAddGuidance() {
        Guidance guidance = new Guidance();
        guidance.setName("name");
        this.roleDefinition.addGuidance(guidance);
        assertTrue(this.roleDefinition.getGuidances().size() == 1);
        assertTrue(guidance.getRoleDefinitions().contains(this.roleDefinition));
    }

    @Test
    public void testaddAllGuidances() {
        Guidance g1 = new Guidance();
        g1.setName("name1");
        Guidance g2 = new Guidance();
        g2.setName("name2");
        Set<Guidance> set = new HashSet<Guidance>();
        set.add(g1);
        set.add(g2);
        this.roleDefinition.addAllGuidances(set);
        assertFalse(this.roleDefinition.getGuidances().isEmpty());
        assertEquals(2, this.roleDefinition.getGuidances().size());
        assertTrue(g1.getRoleDefinitions().contains(this.roleDefinition));
        assertTrue(g2.getRoleDefinitions().contains(this.roleDefinition));
    }

    @Test
    public void testRemoveGuidance() {
        Guidance guidance = new Guidance();
        guidance.setName("name");
        this.roleDefinition.removeGuidance(guidance);
        assertTrue(this.roleDefinition.getGuidances().isEmpty());
        assertFalse(guidance.getRoleDefinitions().contains(this.roleDefinition));
    }

    @Test
    public void testRemoveAllGuidances() {
        Guidance g1 = new Guidance();
        g1.setName("name1");
        Guidance g2 = new Guidance();
        g2.setName("name2");
        Set<Guidance> set = new HashSet<Guidance>();
        set.add(g1);
        set.add(g2);
        this.roleDefinition.addAllGuidances(set);
        assertTrue(this.roleDefinition.getGuidances().size() == 2);
        assertTrue(g1.getRoleDefinitions().contains(this.roleDefinition));
        assertTrue(g2.getRoleDefinitions().contains(this.roleDefinition));
        this.roleDefinition.removeAllGuidances();
        assertTrue(this.roleDefinition.getGuidances().isEmpty());
        assertFalse(g1.getRoleDefinitions().contains(this.roleDefinition));
        assertFalse(g2.getRoleDefinitions().contains(this.roleDefinition));
    }
}
