package com.technoetic.xplanner.actions;

import static org.easymock.EasyMock.*;
import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.NamedObject;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import org.apache.struts.action.ActionForward;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import com.technoetic.xplanner.forms.AbstractEditorForm;

public class TestEditObjectAction extends AbstractActionTestCase {

    private AbstractEditorForm mockAbstractEditorForm;

    private Project mockDomainObject;

    private final int OID = 11;

    @Override
    @Before
    public void setUp() throws Exception {
        action = new EditObjectAction<Project>();
        super.setUp();
        support.setForward(AbstractAction.TYPE_KEY, Project.class.getName());
        mockAbstractEditorForm = createLocalMock(AbstractEditorForm.class);
        mockDomainObject = createLocalMock(Project.class);
        support.form = mockAbstractEditorForm;
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testPreSubmitNewObject() throws Exception {
        EasyMock.expect(mockAbstractEditorForm.isSubmitted()).andReturn(false);
        EasyMock.expect(mockAbstractEditorForm.getOid()).andReturn(null);
        support.mapping.setInput("editor.jsp");
        replay();
        ActionForward forward = support.executeAction(action);
        verify();
        assertNotNull("null forward", forward);
        assertEquals("wrong forward", "editor.jsp", forward.getPath());
    }

    public void testPreSubmitExistingObject() throws Exception {
        expectObjectRepositoryAccess(Project.class);
        expect(mockObjectRepository.load(OID)).andReturn(mockDomainObject);
        support.mapping.setInput("editor.jsp");
        expect(mockDomainObject.getId()).andReturn(44);
        mockAbstractEditorForm.setId(44);
        expect(mockAbstractEditorForm.isSubmitted()).andReturn(false);
        expect(mockAbstractEditorForm.getOid()).andReturn(Integer.toString(OID));
        replay();
        ActionForward forward = support.executeAction(action);
        verify();
        assertNotNull("null forward", forward);
        assertEquals("wrong forward", "editor.jsp", forward.getPath());
    }

    public void testSubmitExistingObject() throws Exception {
        expectObjectRepositoryAccess(Project.class);
        EasyMock.expect(mockAbstractEditorForm.getId()).andReturn(OID);
        EasyMock.expect(mockAbstractEditorForm.isSubmitted()).andReturn(true);
        EasyMock.expect(mockAbstractEditorForm.getOid()).andReturn(Integer.toString(OID));
        EasyMock.expect(mockAbstractEditorForm.getAction()).andReturn(EditObjectAction.UPDATE_ACTION);
        mockAbstractEditorForm.setAction(null);
        expect(mockObjectRepository.load(OID)).andReturn(mockDomainObject).anyTimes();
        mockObjectRepository.update(isA(NamedObject.class));
        EasyMock.expect(mockDomainObject.getId()).andReturn(OID);
        support.request.setParameterValue("returnto", new String[] { "object.jsp" });
        eventBus.publishUpdateEvent(mockAbstractEditorForm, mockDomainObject, null);
        replay();
        ActionForward forward = support.executeAction(action);
        verify();
        assertEquals("wrong forward", "object.jsp", forward.getPath());
    }

    public void testSubmitNewObject() throws Exception {
        expectObjectRepositoryAccess(Project.class);
        EasyMock.expect(mockAbstractEditorForm.isSubmitted()).andReturn(true);
        EasyMock.expect(mockAbstractEditorForm.getOid()).andReturn(null);
        EasyMock.expect(mockAbstractEditorForm.getId()).andReturn(0);
        EasyMock.expect(mockAbstractEditorForm.getAction()).andReturn(EditObjectAction.CREATE_ACTION);
        mockAbstractEditorForm.setAction(null);
        mockAbstractEditorForm.setId(OID);
        expect(mockObjectRepository.insert(isA(Project.class))).andReturn(OID);
        support.request.setParameterValue("returnto", new String[] { "object.jsp" });
        eventBus.publishCreateEvent(isA(Project.class), (Person) isNull());
        replay();
        ActionForward forward = support.executeAction(action);
        verify();
        assertEquals("wrong forward", "object.jsp", forward.getPath());
    }

    public void testNoReturnTo() throws Exception {
        expectObjectRepositoryAccess(Project.class);
        EasyMock.expect(mockAbstractEditorForm.isSubmitted()).andReturn(true);
        EasyMock.expect(mockAbstractEditorForm.getOid()).andReturn(null);
        EasyMock.expect(mockAbstractEditorForm.getId()).andReturn(OID);
        EasyMock.expect(mockAbstractEditorForm.getAction()).andReturn(EditObjectAction.CREATE_ACTION);
        mockAbstractEditorForm.setAction(null);
        mockAbstractEditorForm.setId(OID);
        expect(mockObjectRepository.insert(isA(Project.class))).andReturn(OID);
        support.setForward("view/projects", "projects.jsp");
        eventBus.publishCreateEvent(isA(Project.class), (Person) isNull());
        replay();
        ActionForward forward = support.executeAction(action);
        verify();
        assertEquals("wrong forward", "projects.jsp", forward.getPath());
        assertEquals("wrong forward", "view/projects", forward.getName());
        assertFalse("wrong forward", forward.getRedirect());
    }
}
