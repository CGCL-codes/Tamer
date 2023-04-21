package com.threerings.antidote.field;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Reference;
import org.junit.Test;
import com.threerings.antidote.AntTestHelper;
import com.threerings.antidote.TestViolation;
import com.threerings.antidote.ValidStatus;
import com.threerings.antidote.property.OneViolationProperty;
import com.threerings.antidote.property.SetProperty;
import com.threerings.antidote.property.TestProperty;
import com.threerings.antidote.property.UnsetProperty;
import static com.threerings.antidote.ValidationTestHelper.assertNoViolations;
import static com.threerings.antidote.ValidationTestHelper.assertOneViolation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BaseFieldTest extends AntTestHelper {

    @Test
    public void testReference() {
        final TestBaseField refField = new TestBaseField();
        final TestBaseField field = new TestBaseField();
        assertFalse(field.isReference());
        field.getProject().addReference("test.ref", refField);
        final Reference ref = new Reference(field.getProject(), "test.ref");
        field.setRefid(ref);
        assertTrue(field.isReference());
        assertEquals(refField, field.getReferencedField());
    }

    @Test(expected = UnsetReferenceException.class)
    public void testReferenceUnset() {
        final TestBaseField field = new TestBaseField();
        assertFalse(field.isReference());
        field.getReferencedField();
    }

    @Test
    public void testValidateChildFields() {
        final TestBaseField parentField = new TestBaseField();
        final TestBaseField noViolations = new TestBaseField();
        final OneViolationField oneViolation = new OneViolationField();
        final RequiredField<TestBaseField> noViolationsWrap = new RequiredField<TestBaseField>(noViolations, parentField);
        final RequiredField<OneViolationField> oneViolationWrap = new RequiredField<OneViolationField>(oneViolation, parentField);
        assertEquals(ValidStatus.ALL_VALID, parentField.validateChildFields(noViolationsWrap, noViolationsWrap));
        assertEquals(ValidStatus.SOME_INVALID, parentField.validateChildFields(oneViolationWrap, noViolationsWrap));
        assertEquals(ValidStatus.ALL_INVALID, parentField.validateChildFields(oneViolationWrap, oneViolationWrap));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateNullChildField() {
        final TestBaseField parentField = new TestBaseField();
        final FieldWrapper<?>[] fields = { null };
        parentField.validateChildFields(fields);
    }

    @Test
    public void testValidateProperties() {
        TestBaseField field = new TestBaseField();
        final TestProperty noViolations = new TestProperty(field);
        noViolations.setValue("testvalue");
        final OneViolationProperty oneViolation = new OneViolationProperty(field);
        oneViolation.setValue("testvalue");
        assertEquals(ValidStatus.ALL_VALID, field.validateProperties(noViolations, noViolations));
        assertNoViolations(field);
        field = new TestBaseField();
        assertEquals(ValidStatus.SOME_INVALID, field.validateProperties(oneViolation, noViolations));
        assertOneViolation(field, TestViolation.class);
        field = new TestBaseField();
        assertEquals(ValidStatus.ALL_INVALID, field.validateProperties(oneViolation, oneViolation));
    }

    @Test
    public void testValidateOptionalProperties() {
        TestBaseField field = new TestBaseField();
        final TestProperty noViolations = new TestProperty(field);
        noViolations.setValue("testvalue");
        final OneViolationProperty oneViolation = new OneViolationProperty(field);
        oneViolation.setValue("testvalue");
        final TestProperty unsetProperty = new TestProperty(field);
        assertEquals(ValidStatus.ALL_VALID, field.validateOptionalProperties(noViolations, noViolations));
        field = new TestBaseField();
        assertEquals(ValidStatus.ALL_VALID, field.validateOptionalProperties(noViolations, unsetProperty));
        assertNoViolations(field);
        field = new TestBaseField();
        assertEquals(ValidStatus.SOME_INVALID, field.validateOptionalProperties(oneViolation, noViolations));
        assertOneViolation(field, TestViolation.class);
        field = new TestBaseField();
        assertEquals(ValidStatus.ALL_INVALID, field.validateOptionalProperties(oneViolation, unsetProperty));
        field = new TestBaseField();
        assertEquals(ValidStatus.ALL_INVALID, field.validateOptionalProperties(oneViolation, oneViolation));
        field = new TestBaseField();
        assertEquals(ValidStatus.ALL_VALID, field.validateOptionalProperties(unsetProperty, unsetProperty));
        assertNoViolations(field);
    }

    @Test
    public void testReportUnsetDependentProperties() {
        TestBaseField field = new TestBaseField();
        final SetProperty dependent = new SetProperty(field);
        field.reportUnsetDependentProperties(new SetProperty(field), dependent);
        assertNoViolations(field);
        field = new TestBaseField();
        field.reportUnsetDependentProperties(new UnsetProperty(field), dependent);
        assertOneViolation(field, UnsetDependentPropertyViolation.class);
    }

    @Test
    public void testReportConflictingProperties() {
        TestBaseField field = new TestBaseField();
        final SetProperty conflict = new SetProperty(field);
        field.reportConflictingProperties(new UnsetProperty(field), conflict);
        assertNoViolations(field);
        field = new TestBaseField();
        field.reportConflictingProperties(new SetProperty(field), conflict);
        assertOneViolation(field, ConflictingPropertiesViolation.class);
    }

    @Test
    public void testReportUnsetField() {
        final TestBaseField parent = new TestBaseField();
        final TestBaseField field = new TestBaseField();
        final RequiredField<TestBaseField> setField = new RequiredField<TestBaseField>(field, parent);
        field.reportUnsetField(setField);
        assertNoViolations(field);
        final RequiredField<TestBaseField> unsetField = new RequiredField<TestBaseField>(TestBaseField.class, parent);
        field.reportUnsetField(unsetField);
        assertOneViolation(field, UnsetFieldViolation.class);
    }

    @Test
    public void testReportUnsetDependentFields() {
        final TestBaseField parent = new TestBaseField();
        final TestBaseField otherField = new TestBaseField();
        final TestBaseField field = new TestBaseField();
        final RequiredField<TestBaseField> otherFieldWrap = new RequiredField<TestBaseField>(otherField, parent);
        final RequiredField<TestBaseField> setField = new RequiredField<TestBaseField>(field, parent);
        field.reportUnsetDependentFields(setField, otherFieldWrap);
        assertNoViolations(field);
        final RequiredField<TestBaseField> unsetField = new RequiredField<TestBaseField>(TestBaseField.class, parent);
        field.reportUnsetDependentFields(unsetField, otherFieldWrap);
        assertOneViolation(field, UnsetDependentFieldViolation.class);
    }

    @Test
    public void testRegisterField() {
        final TestBaseField field = new TestBaseField();
        final Project project = field.getProject();
        field.registerField(TestBaseField.class);
        assertTrue(project.getTaskDefinitions().containsKey(TestBaseField.FIELD_NAME));
    }

    @Test(expected = RuntimeException.class)
    public void testRegisterFieldNoProject() {
        final TestBaseField field = new TestBaseField();
        field.setProject(null);
        field.registerField(TestBaseField.class);
    }
}
