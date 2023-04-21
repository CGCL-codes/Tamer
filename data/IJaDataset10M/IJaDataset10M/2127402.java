package misusecase.diagram.providers;

import misusecase.diagram.edit.parts.AttackerActorNameEditPart;
import misusecase.diagram.edit.parts.GoodActorNameEditPart;
import misusecase.diagram.edit.parts.InsiderActorNameEditPart;
import misusecase.diagram.edit.parts.MisuseCaseNodeName2EditPart;
import misusecase.diagram.edit.parts.MisuseCaseNodeNameEditPart;
import misusecase.diagram.edit.parts.NormalUseCaseNodeName2EditPart;
import misusecase.diagram.edit.parts.NormalUseCaseNodeNameEditPart;
import misusecase.diagram.edit.parts.SecurityResourceNodeNameEditPart;
import misusecase.diagram.edit.parts.SecurityUseCaseNodeName2EditPart;
import misusecase.diagram.edit.parts.SecurityUseCaseNodeNameEditPart;
import misusecase.diagram.edit.parts.VulnerabilityUseCaseNodeName2EditPart;
import misusecase.diagram.edit.parts.VulnerabilityUseCaseNodeNameEditPart;
import misusecase.diagram.parsers.MessageFormatParser;
import misusecase.diagram.part.SeaMonsterVisualIDRegistry;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class SeaMonsterParserProvider extends AbstractProvider implements IParserProvider {

    /**
	 * @generated
	 */
    private IParser misuseCaseNodeName_5001Parser;

    /**
	 * @generated
	 */
    private IParser getMisuseCaseNodeName_5001Parser() {
        if (misuseCaseNodeName_5001Parser == null) {
            EAttribute[] features = new EAttribute[] { SeaMonster.SeaMonsterPackage.eINSTANCE.getResource_Name() };
            MessageFormatParser parser = new MessageFormatParser(features);
            misuseCaseNodeName_5001Parser = parser;
        }
        return misuseCaseNodeName_5001Parser;
    }

    /**
	 * @generated
	 */
    private IParser vulnerabilityUseCaseNodeName_5003Parser;

    /**
	 * @generated
	 */
    private IParser getVulnerabilityUseCaseNodeName_5003Parser() {
        if (vulnerabilityUseCaseNodeName_5003Parser == null) {
            EAttribute[] features = new EAttribute[] { SeaMonster.SeaMonsterPackage.eINSTANCE.getResource_Name() };
            MessageFormatParser parser = new MessageFormatParser(features);
            vulnerabilityUseCaseNodeName_5003Parser = parser;
        }
        return vulnerabilityUseCaseNodeName_5003Parser;
    }

    /**
	 * @generated
	 */
    private IParser securityResourceNodeName_5018Parser;

    /**
	 * @generated
	 */
    private IParser getSecurityResourceNodeName_5018Parser() {
        if (securityResourceNodeName_5018Parser == null) {
            EAttribute[] features = new EAttribute[] { SeaMonster.SeaMonsterPackage.eINSTANCE.getResource_Name() };
            MessageFormatParser parser = new MessageFormatParser(features);
            securityResourceNodeName_5018Parser = parser;
        }
        return securityResourceNodeName_5018Parser;
    }

    /**
	 * @generated
	 */
    private IParser normalUseCaseNodeName_5005Parser;

    /**
	 * @generated
	 */
    private IParser getNormalUseCaseNodeName_5005Parser() {
        if (normalUseCaseNodeName_5005Parser == null) {
            EAttribute[] features = new EAttribute[] { SeaMonster.SeaMonsterPackage.eINSTANCE.getResource_Name() };
            MessageFormatParser parser = new MessageFormatParser(features);
            normalUseCaseNodeName_5005Parser = parser;
        }
        return normalUseCaseNodeName_5005Parser;
    }

    /**
	 * @generated
	 */
    private IParser securityUseCaseNodeName_5007Parser;

    /**
	 * @generated
	 */
    private IParser getSecurityUseCaseNodeName_5007Parser() {
        if (securityUseCaseNodeName_5007Parser == null) {
            EAttribute[] features = new EAttribute[] { SeaMonster.SeaMonsterPackage.eINSTANCE.getResource_Name() };
            MessageFormatParser parser = new MessageFormatParser(features);
            securityUseCaseNodeName_5007Parser = parser;
        }
        return securityUseCaseNodeName_5007Parser;
    }

    /**
	 * @generated
	 */
    private IParser attackerActorName_5006Parser;

    /**
	 * @generated
	 */
    private IParser getAttackerActorName_5006Parser() {
        if (attackerActorName_5006Parser == null) {
            EAttribute[] features = new EAttribute[] { SeaMonster.SeaMonsterPackage.eINSTANCE.getActor_Name() };
            MessageFormatParser parser = new MessageFormatParser(features);
            attackerActorName_5006Parser = parser;
        }
        return attackerActorName_5006Parser;
    }

    /**
	 * @generated
	 */
    private IParser goodActorName_5008Parser;

    /**
	 * @generated
	 */
    private IParser getGoodActorName_5008Parser() {
        if (goodActorName_5008Parser == null) {
            EAttribute[] features = new EAttribute[] { SeaMonster.SeaMonsterPackage.eINSTANCE.getActor_Name() };
            MessageFormatParser parser = new MessageFormatParser(features);
            goodActorName_5008Parser = parser;
        }
        return goodActorName_5008Parser;
    }

    /**
	 * @generated
	 */
    private IParser insiderActorName_5009Parser;

    /**
	 * @generated
	 */
    private IParser getInsiderActorName_5009Parser() {
        if (insiderActorName_5009Parser == null) {
            EAttribute[] features = new EAttribute[] { SeaMonster.SeaMonsterPackage.eINSTANCE.getActor_Name() };
            MessageFormatParser parser = new MessageFormatParser(features);
            insiderActorName_5009Parser = parser;
        }
        return insiderActorName_5009Parser;
    }

    /**
	 * @generated
	 */
    private IParser vulnerabilityUseCaseNodeName_5014Parser;

    /**
	 * @generated
	 */
    private IParser getVulnerabilityUseCaseNodeName_5014Parser() {
        if (vulnerabilityUseCaseNodeName_5014Parser == null) {
            EAttribute[] features = new EAttribute[] { SeaMonster.SeaMonsterPackage.eINSTANCE.getResource_Name() };
            MessageFormatParser parser = new MessageFormatParser(features);
            vulnerabilityUseCaseNodeName_5014Parser = parser;
        }
        return vulnerabilityUseCaseNodeName_5014Parser;
    }

    /**
	 * @generated
	 */
    private IParser securityUseCaseNodeName_5015Parser;

    /**
	 * @generated
	 */
    private IParser getSecurityUseCaseNodeName_5015Parser() {
        if (securityUseCaseNodeName_5015Parser == null) {
            EAttribute[] features = new EAttribute[] { SeaMonster.SeaMonsterPackage.eINSTANCE.getResource_Name() };
            MessageFormatParser parser = new MessageFormatParser(features);
            securityUseCaseNodeName_5015Parser = parser;
        }
        return securityUseCaseNodeName_5015Parser;
    }

    /**
	 * @generated
	 */
    private IParser normalUseCaseNodeName_5016Parser;

    /**
	 * @generated
	 */
    private IParser getNormalUseCaseNodeName_5016Parser() {
        if (normalUseCaseNodeName_5016Parser == null) {
            EAttribute[] features = new EAttribute[] { SeaMonster.SeaMonsterPackage.eINSTANCE.getResource_Name() };
            MessageFormatParser parser = new MessageFormatParser(features);
            normalUseCaseNodeName_5016Parser = parser;
        }
        return normalUseCaseNodeName_5016Parser;
    }

    /**
	 * @generated
	 */
    private IParser misuseCaseNodeName_5017Parser;

    /**
	 * @generated
	 */
    private IParser getMisuseCaseNodeName_5017Parser() {
        if (misuseCaseNodeName_5017Parser == null) {
            EAttribute[] features = new EAttribute[] { SeaMonster.SeaMonsterPackage.eINSTANCE.getResource_Name() };
            MessageFormatParser parser = new MessageFormatParser(features);
            misuseCaseNodeName_5017Parser = parser;
        }
        return misuseCaseNodeName_5017Parser;
    }

    /**
	 * @generated
	 */
    protected IParser getParser(int visualID) {
        switch(visualID) {
            case MisuseCaseNodeNameEditPart.VISUAL_ID:
                return getMisuseCaseNodeName_5001Parser();
            case VulnerabilityUseCaseNodeNameEditPart.VISUAL_ID:
                return getVulnerabilityUseCaseNodeName_5003Parser();
            case SecurityResourceNodeNameEditPart.VISUAL_ID:
                return getSecurityResourceNodeName_5018Parser();
            case NormalUseCaseNodeNameEditPart.VISUAL_ID:
                return getNormalUseCaseNodeName_5005Parser();
            case SecurityUseCaseNodeNameEditPart.VISUAL_ID:
                return getSecurityUseCaseNodeName_5007Parser();
            case AttackerActorNameEditPart.VISUAL_ID:
                return getAttackerActorName_5006Parser();
            case GoodActorNameEditPart.VISUAL_ID:
                return getGoodActorName_5008Parser();
            case InsiderActorNameEditPart.VISUAL_ID:
                return getInsiderActorName_5009Parser();
            case VulnerabilityUseCaseNodeName2EditPart.VISUAL_ID:
                return getVulnerabilityUseCaseNodeName_5014Parser();
            case SecurityUseCaseNodeName2EditPart.VISUAL_ID:
                return getSecurityUseCaseNodeName_5015Parser();
            case NormalUseCaseNodeName2EditPart.VISUAL_ID:
                return getNormalUseCaseNodeName_5016Parser();
            case MisuseCaseNodeName2EditPart.VISUAL_ID:
                return getMisuseCaseNodeName_5017Parser();
        }
        return null;
    }

    /**
	 * Utility method that consults ParserService
	 * @generated
	 */
    public static IParser getParser(IElementType type, EObject object, String parserHint) {
        return ParserService.getInstance().getParser(new HintAdapter(type, object, parserHint));
    }

    /**
	 * @generated
	 */
    public IParser getParser(IAdaptable hint) {
        String vid = (String) hint.getAdapter(String.class);
        if (vid != null) {
            return getParser(SeaMonsterVisualIDRegistry.getVisualID(vid));
        }
        View view = (View) hint.getAdapter(View.class);
        if (view != null) {
            return getParser(SeaMonsterVisualIDRegistry.getVisualID(view));
        }
        return null;
    }

    /**
	 * @generated
	 */
    public boolean provides(IOperation operation) {
        if (operation instanceof GetParserOperation) {
            IAdaptable hint = ((GetParserOperation) operation).getHint();
            if (SeaMonsterElementTypes.getElement(hint) == null) {
                return false;
            }
            return getParser(hint) != null;
        }
        return false;
    }

    /**
	 * @generated
	 */
    private static class HintAdapter extends ParserHintAdapter {

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
