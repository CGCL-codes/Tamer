package de.campussource.cse.cdmm.domain;

import static org.junit.Assert.*;
import org.junit.Test;
import de.campussource.cse.cdmm.domain.visitors.DeleteReferenceVisitor;

public class DeleteReferenceVisitorTest {

    @Test
    public void testDeleteChildCategoryFromCategory() {
        Category category = DomainFactory.category(1L);
        Category parent = DomainFactory.category(2L);
        category.addToParentCategory(parent);
        parent.accept(new DeleteReferenceVisitor(category, category, ReferenceType.CHILD));
        assertEquals(0, parent.getChildren().size());
        assertNull(category.getParent());
    }

    @Test
    public void testDeleteParentCategoryFromCategory() {
        Category category = DomainFactory.category(1L);
        Category parent = DomainFactory.category(2L);
        category.addToParentCategory(parent);
        category.accept(new DeleteReferenceVisitor(parent, parent, ReferenceType.PARENT));
        assertEquals(0, parent.getChildren().size());
        assertNull(category.getParent());
    }

    @Test
    public void testDeleteCourseFromCategory() {
        Category category = DomainFactory.category(1L);
        Course course = DomainFactory.course(2L);
        course.addToCategory(category);
        category.accept(new DeleteReferenceVisitor(course, course, ReferenceType.COURSE));
        assertNull(course.getParent());
        assertEquals(0, category.getCourses().size());
    }

    @Test
    public void testDeleteCategoryFromCourse() {
        Course course = DomainFactory.course(1L);
        Category category = DomainFactory.category(2L);
        course.addToCategory(category);
        course.accept(new DeleteReferenceVisitor(category, category, ReferenceType.CATEGORY));
        assertEquals(0, course.getCategories().size());
        assertEquals(0, category.getCourses().size());
    }

    @Test
    public void testDeleteWorkgroupFromCourse() {
        Course course = DomainFactory.course(1L);
        Course workgroup = DomainFactory.course(2L);
        workgroup.addAsWorkgroupTo(course);
        course.accept(new DeleteReferenceVisitor(workgroup, workgroup, ReferenceType.WORKGROUP));
        assertNull(workgroup.getParent());
        assertEquals(0, course.getWorkgroups().size());
    }

    @Test
    public void testDeleteParentFromCourse() {
        Course course = DomainFactory.course(1L);
        Course workgroup = DomainFactory.course(2L);
        workgroup.addAsWorkgroupTo(course);
        workgroup.accept(new DeleteReferenceVisitor(course, course, ReferenceType.PARENT));
        assertNull(workgroup.getParent());
        assertEquals(0, course.getWorkgroups().size());
    }

    @Test
    public void testDeleteRoleFromCourse() {
        Course course = DomainFactory.course(1L);
        Account account = DomainFactory.account(2L);
        Role role = DomainFactory.role(3L);
        role.setAccount(account);
        course.addRole(role);
        course.accept(new DeleteReferenceVisitor(role, role, ReferenceType.ROLE));
        assertNull(role.getCourse());
        assertEquals(0, course.getRoles().size());
    }

    @Test
    public void testDeleteCourseFromRole() {
        Course course = DomainFactory.course(1L);
        Account account = DomainFactory.account(2L);
        Role role = DomainFactory.role(3L);
        role.setAccount(account);
        course.addRole(role);
        role.accept(new DeleteReferenceVisitor(course, course, ReferenceType.COURSE));
        assertNull(role.getCourse());
        assertEquals(0, course.getRoles().size());
    }

    @Test
    public void testDeleteAccountFromRole() {
        Course course = DomainFactory.course(1L);
        Account account = DomainFactory.account(2L);
        Role role = DomainFactory.role(3L);
        role.setCourse(course);
        account.addRole(role);
        role.accept(new DeleteReferenceVisitor(account, account, ReferenceType.ACCOUNT));
        assertNull(role.getAccount());
        assertEquals(0, account.getRoles().size());
    }

    @Test
    public void testDeleteRoleFromAccount() {
        Course course = DomainFactory.course(1L);
        Account account = DomainFactory.account(2L);
        Role role = DomainFactory.role(3L);
        role.setCourse(course);
        account.addRole(role);
        account.accept(new DeleteReferenceVisitor(role, role, ReferenceType.ROLE));
        assertNull(role.getAccount());
        assertEquals(0, account.getRoles().size());
    }

    @Test
    public void testDeleteGroupFromAccount() {
        Account account = DomainFactory.account(1L);
        Group group = DomainFactory.group(2L);
        account.addToGroup(group);
        account.accept(new DeleteReferenceVisitor(group, group, ReferenceType.GROUP));
        assertEquals(0, group.getMembers().size());
        assertEquals(0, account.getGroups().size());
    }

    @Test
    public void testDeleteMemberFromGroup() {
        Account account = DomainFactory.account(1L);
        Group group = DomainFactory.group(2L);
        account.addToGroup(group);
        group.accept(new DeleteReferenceVisitor(account, account, ReferenceType.MEMBER));
        assertEquals(0, group.getMembers().size());
        assertEquals(0, account.getGroups().size());
    }
}
