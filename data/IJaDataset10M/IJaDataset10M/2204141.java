package hub.sam.mof.simulator.editor.diagram.providers;

import hub.sam.mof.simulator.editor.diagram.parsers.MessageFormatParser;
import hub.sam.mof.simulator.editor.diagram.part.M3ActionsVisualIDRegistry;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class M3ActionsParserProvider extends AbstractProvider implements IParserProvider {

    /**
	 * @generated
	 */
    private IParser eClassName_4001Parser;

    /**
	 * @generated
	 */
    private IParser getEClassName_4001Parser() {
        if (eClassName_4001Parser == null) {
            eClassName_4001Parser = createEClassName_4001Parser();
        }
        return eClassName_4001Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEClassName_4001Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser ePackageName_4006Parser;

    /**
	 * @generated
	 */
    private IParser getEPackageName_4006Parser() {
        if (ePackageName_4006Parser == null) {
            ePackageName_4006Parser = createEPackageName_4006Parser();
        }
        return ePackageName_4006Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEPackageName_4006Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eAnnotationSource_4007Parser;

    /**
	 * @generated
	 */
    private IParser getEAnnotationSource_4007Parser() {
        if (eAnnotationSource_4007Parser == null) {
            eAnnotationSource_4007Parser = createEAnnotationSource_4007Parser();
        }
        return eAnnotationSource_4007Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEAnnotationSource_4007Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEAnnotation_Source() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eDataTypeName_4008Parser;

    /**
	 * @generated
	 */
    private IParser getEDataTypeName_4008Parser() {
        if (eDataTypeName_4008Parser == null) {
            eDataTypeName_4008Parser = createEDataTypeName_4008Parser();
        }
        return eDataTypeName_4008Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEDataTypeName_4008Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eDataTypeInstanceClassName_4009Parser;

    /**
	 * @generated
	 */
    private IParser getEDataTypeInstanceClassName_4009Parser() {
        if (eDataTypeInstanceClassName_4009Parser == null) {
            eDataTypeInstanceClassName_4009Parser = createEDataTypeInstanceClassName_4009Parser();
        }
        return eDataTypeInstanceClassName_4009Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEDataTypeInstanceClassName_4009Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEClassifier_InstanceClassName() };
        MessageFormatParser parser = new MessageFormatParser(features);
        parser.setViewPattern("<<javaclass>> {0}");
        parser.setEditorPattern("<<javaclass>> {0}");
        parser.setEditPattern("<<javaclass>> {0}");
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eEnumName_4010Parser;

    /**
	 * @generated
	 */
    private IParser getEEnumName_4010Parser() {
        if (eEnumName_4010Parser == null) {
            eEnumName_4010Parser = createEEnumName_4010Parser();
        }
        return eEnumName_4010Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEEnumName_4010Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser mClassName_4011Parser;

    /**
	 * @generated
	 */
    private IParser getMClassName_4011Parser() {
        if (mClassName_4011Parser == null) {
            mClassName_4011Parser = createMClassName_4011Parser();
        }
        return mClassName_4011Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createMClassName_4011Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser mActivityName_4012Parser;

    /**
	 * @generated
	 */
    private IParser getMActivityName_4012Parser() {
        if (mActivityName_4012Parser == null) {
            mActivityName_4012Parser = createMActivityName_4012Parser();
        }
        return mActivityName_4012Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createMActivityName_4012Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eAttribute_2001Parser;

    /**
	 * @generated
	 */
    private IParser getEAttribute_2001Parser() {
        if (eAttribute_2001Parser == null) {
            eAttribute_2001Parser = createEAttribute_2001Parser();
        }
        return eAttribute_2001Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEAttribute_2001Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eOperation_2002Parser;

    /**
	 * @generated
	 */
    private IParser getEOperation_2002Parser() {
        if (eOperation_2002Parser == null) {
            eOperation_2002Parser = createEOperation_2002Parser();
        }
        return eOperation_2002Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEOperation_2002Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser mOperation_2003Parser;

    /**
	 * @generated
	 */
    private IParser getMOperation_2003Parser() {
        if (mOperation_2003Parser == null) {
            mOperation_2003Parser = createMOperation_2003Parser();
        }
        return mOperation_2003Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createMOperation_2003Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eClassName_4002Parser;

    /**
	 * @generated
	 */
    private IParser getEClassName_4002Parser() {
        if (eClassName_4002Parser == null) {
            eClassName_4002Parser = createEClassName_4002Parser();
        }
        return eClassName_4002Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEClassName_4002Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eDataTypeName_4003Parser;

    /**
	 * @generated
	 */
    private IParser getEDataTypeName_4003Parser() {
        if (eDataTypeName_4003Parser == null) {
            eDataTypeName_4003Parser = createEDataTypeName_4003Parser();
        }
        return eDataTypeName_4003Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEDataTypeName_4003Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eDataTypeInstanceClassName_4004Parser;

    /**
	 * @generated
	 */
    private IParser getEDataTypeInstanceClassName_4004Parser() {
        if (eDataTypeInstanceClassName_4004Parser == null) {
            eDataTypeInstanceClassName_4004Parser = createEDataTypeInstanceClassName_4004Parser();
        }
        return eDataTypeInstanceClassName_4004Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEDataTypeInstanceClassName_4004Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEClassifier_InstanceClassName() };
        MessageFormatParser parser = new MessageFormatParser(features);
        parser.setViewPattern("<<javaclass>> {0}");
        parser.setEditorPattern("<<javaclass>> {0}");
        parser.setEditPattern("<<javaclass>> {0}");
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eEnumName_4005Parser;

    /**
	 * @generated
	 */
    private IParser getEEnumName_4005Parser() {
        if (eEnumName_4005Parser == null) {
            eEnumName_4005Parser = createEEnumName_4005Parser();
        }
        return eEnumName_4005Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEEnumName_4005Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eEnumLiteral_2007Parser;

    /**
	 * @generated
	 */
    private IParser getEEnumLiteral_2007Parser() {
        if (eEnumLiteral_2007Parser == null) {
            eEnumLiteral_2007Parser = createEEnumLiteral_2007Parser();
        }
        return eEnumLiteral_2007Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEEnumLiteral_2007Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eStringToStringMapEntry_2008Parser;

    /**
	 * @generated
	 */
    private IParser getEStringToStringMapEntry_2008Parser() {
        if (eStringToStringMapEntry_2008Parser == null) {
            eStringToStringMapEntry_2008Parser = createEStringToStringMapEntry_2008Parser();
        }
        return eStringToStringMapEntry_2008Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEStringToStringMapEntry_2008Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEStringToStringMapEntry_Key() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eAttribute_2009Parser;

    /**
	 * @generated
	 */
    private IParser getEAttribute_2009Parser() {
        if (eAttribute_2009Parser == null) {
            eAttribute_2009Parser = createEAttribute_2009Parser();
        }
        return eAttribute_2009Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEAttribute_2009Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eOperation_2010Parser;

    /**
	 * @generated
	 */
    private IParser getEOperation_2010Parser() {
        if (eOperation_2010Parser == null) {
            eOperation_2010Parser = createEOperation_2010Parser();
        }
        return eOperation_2010Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEOperation_2010Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser mOperation_2011Parser;

    /**
	 * @generated
	 */
    private IParser getMOperation_2011Parser() {
        if (mOperation_2011Parser == null) {
            mOperation_2011Parser = createMOperation_2011Parser();
        }
        return mOperation_2011Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createMOperation_2011Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eReferenceName_4013Parser;

    /**
	 * @generated
	 */
    private IParser getEReferenceName_4013Parser() {
        if (eReferenceName_4013Parser == null) {
            eReferenceName_4013Parser = createEReferenceName_4013Parser();
        }
        return eReferenceName_4013Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEReferenceName_4013Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getENamedElement_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser eReferenceLowerBoundUpperBound_4014Parser;

    /**
	 * @generated
	 */
    private IParser getEReferenceLowerBoundUpperBound_4014Parser() {
        if (eReferenceLowerBoundUpperBound_4014Parser == null) {
            eReferenceLowerBoundUpperBound_4014Parser = createEReferenceLowerBoundUpperBound_4014Parser();
        }
        return eReferenceLowerBoundUpperBound_4014Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createEReferenceLowerBoundUpperBound_4014Parser() {
        EAttribute[] features = new EAttribute[] { org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getETypedElement_LowerBound(), org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getETypedElement_UpperBound() };
        MessageFormatParser parser = new MessageFormatParser(features);
        parser.setViewPattern("{0}..{1,choice,-2#?|-1#*|-1<{1}}");
        parser.setEditorPattern("{0}..{1,choice,-2#?|-1#*|-1<{1}}");
        parser.setEditPattern("{0}..{1}");
        return parser;
    }

    /**
	 * @generated
	 */
    protected IParser getParser(int visualID) {
        switch(visualID) {
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EClassNameEditPart.VISUAL_ID:
                return getEClassName_4001Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EPackageNameEditPart.VISUAL_ID:
                return getEPackageName_4006Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EAnnotationSourceEditPart.VISUAL_ID:
                return getEAnnotationSource_4007Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EDataTypeNameEditPart.VISUAL_ID:
                return getEDataTypeName_4008Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EDataTypeInstanceClassNameEditPart.VISUAL_ID:
                return getEDataTypeInstanceClassName_4009Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EEnumNameEditPart.VISUAL_ID:
                return getEEnumName_4010Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.MClassNameEditPart.VISUAL_ID:
                return getMClassName_4011Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.WrappingLabelEditPart.VISUAL_ID:
                return getMActivityName_4012Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EAttributeEditPart.VISUAL_ID:
                return getEAttribute_2001Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EOperationEditPart.VISUAL_ID:
                return getEOperation_2002Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.MOperation2EditPart.VISUAL_ID:
                return getMOperation_2003Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EClassName2EditPart.VISUAL_ID:
                return getEClassName_4002Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EDataTypeName2EditPart.VISUAL_ID:
                return getEDataTypeName_4003Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EDataTypeInstanceClassName2EditPart.VISUAL_ID:
                return getEDataTypeInstanceClassName_4004Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EEnumName2EditPart.VISUAL_ID:
                return getEEnumName_4005Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EStringToStringMapEntryEditPart.VISUAL_ID:
                return getEEnumLiteral_2007Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EAttribute2EditPart.VISUAL_ID:
                return getEStringToStringMapEntry_2008Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EOperation2EditPart.VISUAL_ID:
                return getEAttribute_2009Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.MOperationEditPart.VISUAL_ID:
                return getEOperation_2010Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.MOperation3EditPart.VISUAL_ID:
                return getMOperation_2011Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EReferenceNameEditPart.VISUAL_ID:
                return getEReferenceName_4013Parser();
            case hub.sam.mof.simulator.editor.diagram.edit.parts.EReferenceLowerBoundUpperBoundEditPart.VISUAL_ID:
                return getEReferenceLowerBoundUpperBound_4014Parser();
        }
        return null;
    }

    /**
	 * @generated
	 */
    public IParser getParser(IAdaptable hint) {
        String vid = (String) hint.getAdapter(String.class);
        if (vid != null) {
            return getParser(M3ActionsVisualIDRegistry.getVisualID(vid));
        }
        View view = (View) hint.getAdapter(View.class);
        if (view != null) {
            return getParser(M3ActionsVisualIDRegistry.getVisualID(view));
        }
        return null;
    }

    /**
	 * @generated
	 */
    public boolean provides(IOperation operation) {
        if (operation instanceof GetParserOperation) {
            IAdaptable hint = ((GetParserOperation) operation).getHint();
            if (M3ActionsElementTypes.getElement(hint) == null) {
                return false;
            }
            return getParser(hint) != null;
        }
        return false;
    }

    /**
	 * @generated
	 */
    public static class HintAdapter extends ParserHintAdapter {

        /**
		 * @generated
		 */
        private final IElementType elementType;

        /**
		 * @generated
		 */
        public HintAdapter(IElementType type, EObject object, String parserHint) {
            super(object, parserHint);
            assert type != null;
            elementType = type;
        }

        /**
		 * @generated
		 */
        public Object getAdapter(Class adapter) {
            if (IElementType.class.equals(adapter)) {
                return elementType;
            }
            return super.getAdapter(adapter);
        }
    }
}
