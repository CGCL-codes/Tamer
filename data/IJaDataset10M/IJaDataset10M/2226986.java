package net.sf.rcpforms.emf.test;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;
import net.sf.rcpforms.emf.EMFModelAdapter;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.tablesupport.tables.PropertyLabelProviderAndCellModifier;
import net.sf.rcpforms.test.adapter.ITestModel;
import net.sf.rcpforms.test.adapter.PropertyLabelProviderAndCellModifierBean_Test;
import net.sf.rcpforms.test.adapter.TestModel;
import org.eclipse.emf.ecore.EClass;

/**
 * Test for EMFModelAdapter
 * TODO: make tests work with locale specific formats
 * @author Marco van Meegen
 */
public class PropertyLabelProviderAndCellModifierEMF_Test extends PropertyLabelProviderAndCellModifierBean_Test {

    private Locale originalLocale;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ModelAdapter.registerAdapter(EMFModelAdapter.getInstance());
        originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        ModelAdapter.unregisterAdapter(EMFModelAdapter.getInstance());
        Locale.setDefault(originalLocale);
    }

    protected Object getGender(ITestModel model) {
        return ((net.sf.rcpforms.emf.test.TestModel) model).getGender();
    }

    protected void setGender(ITestModel model, Object gender) {
        ((net.sf.rcpforms.emf.test.TestModel) model).setGender((net.sf.rcpforms.emf.test.Gender) gender);
    }

    protected Object getUnknownConstant() {
        return net.sf.rcpforms.emf.test.Gender.UNKNOWN;
    }

    protected Object getFemaleConstant() {
        return net.sf.rcpforms.emf.test.Gender.FEMALE;
    }

    protected Object[] getGenderValues() {
        return net.sf.rcpforms.emf.test.Gender.values();
    }

    /**
     * set nested zip code; abstracted since AddressModel is different for EMF and Bean
     */
    protected void setNestedZip(ITestModel model, Integer i) {
        ((net.sf.rcpforms.emf.test.TestModel) model).getAddress().setZipCode(i);
    }

    protected Integer getNestedZip(ITestModel model) {
        return ((net.sf.rcpforms.emf.test.TestModel) model).getAddress().getZipCode();
    }

    protected PropertyLabelProviderAndCellModifier createLabelProviderAndCellModifier() {
        EClass rowElementMetaClass = TestPackage.eINSTANCE.getTestModel();
        assertSame("EMFModelAdapter not registered or not retrieved correctly", EMFModelAdapter.getInstance(), ModelAdapter.getAdapterForMetaClass(rowElementMetaClass));
        PropertyLabelProviderAndCellModifier cellmodifier = new PropertyLabelProviderAndCellModifier(columnConfiguration, rowElementMetaClass);
        return cellmodifier;
    }

    protected ITestModel createTestModel() {
        return TestFactory.eINSTANCE.createTestModel();
    }
}
