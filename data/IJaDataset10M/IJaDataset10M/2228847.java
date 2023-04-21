package it.unisannio.rcost.callgraphanalyzer.diagram.providers;

import it.unisannio.rcost.callgraphanalyzer.CallGraphPackage;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.AspectName2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.AspectName3EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.AspectNameEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ClassName2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ClassName3EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ClassNameEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ExplicitCallGroupIdEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.InterfaceName2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.InterfaceName3EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.InterfaceNameEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.PackageName2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.PackageNameEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.parsers.MessageFormatParser;
import it.unisannio.rcost.callgraphanalyzer.diagram.part.CallGraphVisualIDRegistry;
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
public class CallGraphParserProvider extends AbstractProvider implements IParserProvider {

    /**
	 * @generated
	 */
    private IParser packageName_4008Parser;

    /**
	 * @generated
	 */
    private IParser getPackageName_4008Parser() {
        if (packageName_4008Parser == null) {
            packageName_4008Parser = createPackageName_4008Parser();
        }
        return packageName_4008Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createPackageName_4008Parser() {
        EAttribute[] features = new EAttribute[] { CallGraphPackage.eINSTANCE.getNode_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser aspectName_4009Parser;

    /**
	 * @generated
	 */
    private IParser getAspectName_4009Parser() {
        if (aspectName_4009Parser == null) {
            aspectName_4009Parser = createAspectName_4009Parser();
        }
        return aspectName_4009Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createAspectName_4009Parser() {
        EAttribute[] features = new EAttribute[] { CallGraphPackage.eINSTANCE.getNode_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser className_4010Parser;

    /**
	 * @generated
	 */
    private IParser getClassName_4010Parser() {
        if (className_4010Parser == null) {
            className_4010Parser = createClassName_4010Parser();
        }
        return className_4010Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createClassName_4010Parser() {
        EAttribute[] features = new EAttribute[] { CallGraphPackage.eINSTANCE.getNode_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser interfaceName_4011Parser;

    /**
	 * @generated
	 */
    private IParser getInterfaceName_4011Parser() {
        if (interfaceName_4011Parser == null) {
            interfaceName_4011Parser = createInterfaceName_4011Parser();
        }
        return interfaceName_4011Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createInterfaceName_4011Parser() {
        EAttribute[] features = new EAttribute[] { CallGraphPackage.eINSTANCE.getNode_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser aspectName_4004Parser;

    /**
	 * @generated
	 */
    private IParser getAspectName_4004Parser() {
        if (aspectName_4004Parser == null) {
            aspectName_4004Parser = createAspectName_4004Parser();
        }
        return aspectName_4004Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createAspectName_4004Parser() {
        EAttribute[] features = new EAttribute[] { CallGraphPackage.eINSTANCE.getNode_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser aspectName_4001Parser;

    /**
	 * @generated
	 */
    private IParser getAspectName_4001Parser() {
        if (aspectName_4001Parser == null) {
            aspectName_4001Parser = createAspectName_4001Parser();
        }
        return aspectName_4001Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createAspectName_4001Parser() {
        EAttribute[] features = new EAttribute[] { CallGraphPackage.eINSTANCE.getNode_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser className_4005Parser;

    /**
	 * @generated
	 */
    private IParser getClassName_4005Parser() {
        if (className_4005Parser == null) {
            className_4005Parser = createClassName_4005Parser();
        }
        return className_4005Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createClassName_4005Parser() {
        EAttribute[] features = new EAttribute[] { CallGraphPackage.eINSTANCE.getNode_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser interfaceName_4006Parser;

    /**
	 * @generated
	 */
    private IParser getInterfaceName_4006Parser() {
        if (interfaceName_4006Parser == null) {
            interfaceName_4006Parser = createInterfaceName_4006Parser();
        }
        return interfaceName_4006Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createInterfaceName_4006Parser() {
        EAttribute[] features = new EAttribute[] { CallGraphPackage.eINSTANCE.getNode_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser packageName_4007Parser;

    /**
	 * @generated
	 */
    private IParser getPackageName_4007Parser() {
        if (packageName_4007Parser == null) {
            packageName_4007Parser = createPackageName_4007Parser();
        }
        return packageName_4007Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createPackageName_4007Parser() {
        EAttribute[] features = new EAttribute[] { CallGraphPackage.eINSTANCE.getNode_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser explicitCallGroupId_4012Parser;

    /**
	 * @generated
	 */
    private IParser getExplicitCallGroupId_4012Parser() {
        if (explicitCallGroupId_4012Parser == null) {
            explicitCallGroupId_4012Parser = createExplicitCallGroupId_4012Parser();
        }
        return explicitCallGroupId_4012Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createExplicitCallGroupId_4012Parser() {
        EAttribute[] features = new EAttribute[] { CallGraphPackage.eINSTANCE.getExplicitCall_GroupId() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser className_4002Parser;

    /**
	 * @generated
	 */
    private IParser getClassName_4002Parser() {
        if (className_4002Parser == null) {
            className_4002Parser = createClassName_4002Parser();
        }
        return className_4002Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createClassName_4002Parser() {
        EAttribute[] features = new EAttribute[] { CallGraphPackage.eINSTANCE.getNode_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    private IParser interfaceName_4003Parser;

    /**
	 * @generated
	 */
    private IParser getInterfaceName_4003Parser() {
        if (interfaceName_4003Parser == null) {
            interfaceName_4003Parser = createInterfaceName_4003Parser();
        }
        return interfaceName_4003Parser;
    }

    /**
	 * @generated
	 */
    protected IParser createInterfaceName_4003Parser() {
        EAttribute[] features = new EAttribute[] { CallGraphPackage.eINSTANCE.getNode_Name() };
        MessageFormatParser parser = new MessageFormatParser(features);
        return parser;
    }

    /**
	 * @generated
	 */
    protected IParser getParser(int visualID) {
        switch(visualID) {
            case PackageNameEditPart.VISUAL_ID:
                return getPackageName_4008Parser();
            case AspectNameEditPart.VISUAL_ID:
                return getAspectName_4009Parser();
            case ClassNameEditPart.VISUAL_ID:
                return getClassName_4010Parser();
            case InterfaceNameEditPart.VISUAL_ID:
                return getInterfaceName_4011Parser();
            case AspectName2EditPart.VISUAL_ID:
                return getAspectName_4004Parser();
            case InterfaceName2EditPart.VISUAL_ID:
                return getInterfaceName_4003Parser();
            case ClassName2EditPart.VISUAL_ID:
                return getClassName_4002Parser();
            case AspectName3EditPart.VISUAL_ID:
                return getAspectName_4001Parser();
            case ClassName3EditPart.VISUAL_ID:
                return getClassName_4005Parser();
            case InterfaceName3EditPart.VISUAL_ID:
                return getInterfaceName_4006Parser();
            case PackageName2EditPart.VISUAL_ID:
                return getPackageName_4007Parser();
            case ExplicitCallGroupIdEditPart.VISUAL_ID:
                return getExplicitCallGroupId_4012Parser();
        }
        return null;
    }

    /**
	 * @generated
	 */
    public IParser getParser(IAdaptable hint) {
        String vid = (String) hint.getAdapter(String.class);
        if (vid != null) {
            return getParser(CallGraphVisualIDRegistry.getVisualID(vid));
        }
        View view = (View) hint.getAdapter(View.class);
        if (view != null) {
            return getParser(CallGraphVisualIDRegistry.getVisualID(view));
        }
        return null;
    }

    /**
	 * @generated
	 */
    public boolean provides(IOperation operation) {
        if (operation instanceof GetParserOperation) {
            IAdaptable hint = ((GetParserOperation) operation).getHint();
            if (CallGraphElementTypes.getElement(hint) == null) {
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
