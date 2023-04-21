package net.sf.springlayout.web.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.springlayout.AbstractTestBase;
import net.sf.springlayout.model.MockModelObject;
import net.sf.springlayout.model.Person;
import net.sf.springlayout.web.layout.FieldDefinition;
import net.sf.springlayout.web.layout.LayoutFieldDefinition;
import net.sf.springlayout.web.layout.LayoutPanelForm;
import net.sf.springlayout.web.layout.LayoutPanelFormGroup;
import net.sf.springlayout.web.layout.PanelForm;
import net.sf.springlayout.web.layout.PanelFormGroup;
import net.sf.springlayout.web.validator.ValidationRule;
import org.easymock.MockControl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

public class PanelFormGroupTest extends AbstractTestBase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testValidate() {
        FieldDefinition fieldDefinition1 = new LayoutFieldDefinition();
        MockHttpServletRequest request = createMockHttpServletRequest("post", "");
        Person person = new Person(new Long(1));
        person.setFirstName("test first name");
        person.setLastName("test last name");
        Errors errors = new BindException(person, "commandName");
        fieldDefinition1.setFieldKey("firstName");
        fieldDefinition1.setMandatory(true);
        MockControl control = MockControl.createControl(ValidationRule.class);
        ValidationRule mock1 = (ValidationRule) control.getMock();
        ValidationRule mock2 = (ValidationRule) control.getMock();
        List validationRules1 = new ArrayList();
        validationRules1.add(mock1);
        validationRules1.add(mock2);
        fieldDefinition1.setValidationRules(validationRules1);
        FieldDefinition fieldDefinition2 = new LayoutFieldDefinition();
        fieldDefinition2.setFieldKey("lastName");
        fieldDefinition2.setMandatory(true);
        ValidationRule mock3 = (ValidationRule) control.getMock();
        ValidationRule mock4 = (ValidationRule) control.getMock();
        List validationRules2 = new ArrayList();
        validationRules2.add(mock3);
        validationRules2.add(mock4);
        fieldDefinition1.setValidationRules(validationRules2);
        PanelForm panel = new LayoutPanelForm();
        Map fieldDefinitions = new HashMap();
        fieldDefinitions.put(fieldDefinition1.getFieldKey(), fieldDefinition1);
        fieldDefinitions.put(fieldDefinition2.getFieldKey(), fieldDefinition2);
        panel.setFieldDefinitions(fieldDefinitions);
        PanelFormGroup panelFormGroup = new LayoutPanelFormGroup();
        List panels = new ArrayList();
        panels.add(panel);
        panelFormGroup.setPanelForms(panels);
        panelFormGroup.validate(request, person, errors);
    }

    public void testHasErrorsWithoutErrors() {
        PanelFormGroup panelFormGroup = new LayoutPanelFormGroup();
        Object command = new Object();
        Errors errors = new BindException(command, "dummyCommand");
        assertFalse(panelFormGroup.hasErrors(errors));
    }

    public void testHasErrorsWithErrors() {
        PanelFormGroup panelFormGroup = new LayoutPanelFormGroup();
        MockModelObject command = new MockModelObject();
        Errors errors = new BindException(command, "dummyCommand");
        errors.rejectValue("testStringField", "aaa");
        PanelForm panel = new LayoutPanelForm();
        panel.setFormName("Test LayoutPanelForm");
        panel.setIndex(0);
        FieldDefinition fieldDefinition = new LayoutFieldDefinition();
        fieldDefinition.setFieldKey("testStringField");
        Map fieldMap = new HashMap();
        fieldMap.put("testStringField", fieldDefinition);
        panel.setFieldDefinitions(fieldMap);
        List panels = new ArrayList();
        panels.add(panel);
        panelFormGroup.setPanelForms(panels);
        assertTrue(panelFormGroup.hasErrors(errors));
    }

    public void testGetErrors() {
        PanelFormGroup panelFormGroup = new LayoutPanelFormGroup();
        Object command = new Object();
        Errors errors = new BindException(command, "dummyCommand");
        panelFormGroup.getErrors(errors);
    }

    public void testGetErrorsForPanelWithNoErrors() {
        PanelFormGroup panelFormGroup = new LayoutPanelFormGroup();
        Object command = new Object();
        Errors errors = new BindException(command, "dummyCommand");
        panelFormGroup.getErrorsForPanelForm(errors, 0);
    }

    public void testGetErrorsForPanelWithErrors() {
        PanelFormGroup panelFormGroup = new LayoutPanelFormGroup();
        MockModelObject command = new MockModelObject();
        Errors errors = new BindException(command, "dummyCommand");
        errors.rejectValue("testStringField", "aaa");
        PanelForm panel = new LayoutPanelForm();
        panel.setFormName("Test LayoutPanelForm");
        panel.setIndex(0);
        FieldDefinition fieldDefinition = new LayoutFieldDefinition();
        fieldDefinition.setFieldKey("testStringField");
        Map fieldMap = new HashMap();
        fieldMap.put("testStringField", fieldDefinition);
        panel.setFieldDefinitions(fieldMap);
        List panels = new ArrayList();
        panels.add(panel);
        panelFormGroup.setPanelForms(panels);
        assertTrue(panelFormGroup.getErrorsForPanelForm(errors, 0).size() == 1);
    }

    public void testGetPanelByLabel() {
        PanelFormGroup panelFormGroup = new LayoutPanelFormGroup();
        PanelForm panel1 = new LayoutPanelForm();
        panel1.setPanelLabel("LayoutPanelForm 1");
        PanelForm panel2 = new LayoutPanelForm();
        panel2.setPanelLabel("LayoutPanelForm 2");
        List panels = new ArrayList();
        panels.add(panel1);
        panels.add(panel2);
        panelFormGroup.setPanelForms(panels);
        assertTrue(panelFormGroup.getPanelFormByLabel("LayoutPanelForm 1").equals(panel1));
        assertTrue(panelFormGroup.getPanelFormByLabel("LayoutPanelForm 2").equals(panel2));
    }

    public void testGetPanelByIncorrectLabel() {
        PanelFormGroup panelFormGroup = new LayoutPanelFormGroup();
        PanelForm panel1 = new LayoutPanelForm();
        panel1.setPanelLabel("LayoutPanelForm 1");
        PanelForm panel2 = new LayoutPanelForm();
        panel2.setPanelLabel("LayoutPanelForm 2");
        List panels = new ArrayList();
        panels.add(panel1);
        panels.add(panel2);
        panelFormGroup.setPanelForms(panels);
        assertTrue(panelFormGroup.getPanelFormByLabel("LayoutPanelForm xxx") == null);
    }
}
