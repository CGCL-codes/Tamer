package org.emftext.language.office.resource.office.grammar;

/**
 * This class provides the follow sets for all terminals of the grammar. These
 * sets are used during code completion.
 */
public class OfficeFollowSetProvider {

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_0 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedCsString(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_0_0_0_0);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_1 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedStructuralFeature(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_0_0_0_1);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_2 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedCsString(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_0_0_0_2);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_3 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedCsString(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_1_0_0_0);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_4 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedCsString(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_2_0_0_0);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_5 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedCsString(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_0_0_0_4);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_6 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedStructuralFeature(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_1_0_0_1);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_7 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedStructuralFeature(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_2_0_0_1);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_8 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedCsString(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_2_0_0_2);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_9 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedCsString(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_2_0_0_3);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_10 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedStructuralFeature(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_2_0_0_4);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_11 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedCsString(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_2_0_0_5);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_12 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedCsString(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_2_0_0_6);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_13 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedStructuralFeature(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_2_0_0_7);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_14 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedCsString(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_2_0_0_8_0_0_0);

    public static final org.emftext.language.office.resource.office.IOfficeExpectedElement TERMINAL_15 = new org.emftext.language.office.resource.office.mopp.OfficeExpectedStructuralFeature(org.emftext.language.office.resource.office.grammar.OfficeGrammarInformationProvider.OFFICE_2_0_0_8_0_0_1);

    public static final org.eclipse.emf.ecore.EStructuralFeature FEATURE_0 = org.emftext.language.office.OfficePackage.eINSTANCE.getOfficeModel().getEStructuralFeature(org.emftext.language.office.OfficePackage.OFFICE_MODEL__ELEMENTS);

    public static final org.eclipse.emf.ecore.EStructuralFeature[] EMPTY_FEATURE_ARRAY = new org.eclipse.emf.ecore.EStructuralFeature[0];

    public static void wire0() {
        TERMINAL_0.addFollower(TERMINAL_1, EMPTY_FEATURE_ARRAY);
        TERMINAL_1.addFollower(TERMINAL_2, EMPTY_FEATURE_ARRAY);
        TERMINAL_2.addFollower(TERMINAL_3, new org.eclipse.emf.ecore.EStructuralFeature[] { FEATURE_0 });
        TERMINAL_2.addFollower(TERMINAL_4, new org.eclipse.emf.ecore.EStructuralFeature[] { FEATURE_0 });
        TERMINAL_2.addFollower(TERMINAL_5, EMPTY_FEATURE_ARRAY);
        TERMINAL_3.addFollower(TERMINAL_6, EMPTY_FEATURE_ARRAY);
        TERMINAL_6.addFollower(TERMINAL_3, new org.eclipse.emf.ecore.EStructuralFeature[] { FEATURE_0 });
        TERMINAL_6.addFollower(TERMINAL_4, new org.eclipse.emf.ecore.EStructuralFeature[] { FEATURE_0 });
        TERMINAL_6.addFollower(TERMINAL_5, EMPTY_FEATURE_ARRAY);
        TERMINAL_4.addFollower(TERMINAL_7, EMPTY_FEATURE_ARRAY);
        TERMINAL_7.addFollower(TERMINAL_8, EMPTY_FEATURE_ARRAY);
        TERMINAL_8.addFollower(TERMINAL_9, EMPTY_FEATURE_ARRAY);
        TERMINAL_9.addFollower(TERMINAL_10, EMPTY_FEATURE_ARRAY);
        TERMINAL_10.addFollower(TERMINAL_11, EMPTY_FEATURE_ARRAY);
        TERMINAL_11.addFollower(TERMINAL_12, EMPTY_FEATURE_ARRAY);
        TERMINAL_12.addFollower(TERMINAL_13, EMPTY_FEATURE_ARRAY);
        TERMINAL_13.addFollower(TERMINAL_14, EMPTY_FEATURE_ARRAY);
        TERMINAL_13.addFollower(TERMINAL_3, new org.eclipse.emf.ecore.EStructuralFeature[] { FEATURE_0 });
        TERMINAL_13.addFollower(TERMINAL_4, new org.eclipse.emf.ecore.EStructuralFeature[] { FEATURE_0 });
        TERMINAL_13.addFollower(TERMINAL_5, EMPTY_FEATURE_ARRAY);
        TERMINAL_14.addFollower(TERMINAL_15, EMPTY_FEATURE_ARRAY);
        TERMINAL_15.addFollower(TERMINAL_14, EMPTY_FEATURE_ARRAY);
        TERMINAL_15.addFollower(TERMINAL_3, new org.eclipse.emf.ecore.EStructuralFeature[] { FEATURE_0 });
        TERMINAL_15.addFollower(TERMINAL_4, new org.eclipse.emf.ecore.EStructuralFeature[] { FEATURE_0 });
        TERMINAL_15.addFollower(TERMINAL_5, EMPTY_FEATURE_ARRAY);
    }

    static {
        wire0();
    }
}
