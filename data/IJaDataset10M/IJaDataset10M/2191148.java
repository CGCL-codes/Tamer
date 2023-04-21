package de.mpiwg.vspace.languages.validation.validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import de.mpiwg.vspace.languages.language.Language;
import de.mpiwg.vspace.languages.validation.Activator;
import de.mpiwg.vspace.languages.validation.controller.LanguageStateController;
import de.mpiwg.vspace.metamodel.ExhibitionModuleLink;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;
import de.mpiwg.vspace.metamodel.Link;
import de.mpiwg.vspace.util.ExtendedPropertyHandler;
import de.mpiwg.vspace.util.PropertyHandlerRegistry;

public class EObjectValidator implements IValidator {

    private Map<EObject, Map<EStructuralFeature, String[]>> messages;

    private Map<EStructuralFeature, String> checkableFeatures;

    public EObjectValidator() {
        messages = new HashMap<EObject, Map<EStructuralFeature, String[]>>();
        checkableFeatures = new HashMap<EStructuralFeature, String>();
        checkableFeatures.put(ExhibitionPackage.Literals.SLIDE__TITLE, "_slide_title_missing");
        checkableFeatures.put(ExhibitionPackage.Literals.SLIDE__FOOTER, "_slide_footer_missing");
        checkableFeatures.put(ExhibitionPackage.Literals.SLIDE__SUB_TITLE, "_slide_subtitle_missing");
        checkableFeatures.put(ExhibitionPackage.Literals.TEXT__TEXT, "_text_text_missing");
        checkableFeatures.put(ExhibitionPackage.Literals.MEDIA__TITLE, "_text_title_missing");
        checkableFeatures.put(ExhibitionPackage.Literals.BRANCHING_POINT_CHOICE__TITLE, "_choice_title_missing");
        checkableFeatures.put(ExhibitionPackage.Literals.COPYRIGHT__TEXT, "_copyright_text_missing");
        checkableFeatures.put(ExhibitionPackage.Literals.COPYRIGHT__TITLE, "_copyright_title_missing");
        checkableFeatures.put(ExhibitionPackage.Literals.LINK__TITLE, "_link_title_missing");
    }

    public boolean checkEObject(EObject slide, EStructuralFeature feature) {
        if (checkableFeatures.containsKey(feature)) {
            LanguageStateController.INSTANCE.init();
            LanguageStateController.INSTANCE.setProcessing(true);
            boolean ok = true;
            List<String> msgs = new ArrayList<String>();
            Language language = LanguageStateController.INSTANCE.nextLanguage();
            while (language != null) {
                Object featureContent = slide.eGet(feature);
                if ((featureContent == null) || featureContent.toString().trim().equals("")) {
                    ok = false;
                    ExtendedPropertyHandler handler = PropertyHandlerRegistry.REGISTRY.getPropertyHandler(Activator.PLUGIN_ID, Activator.PROPERTIES_FILE);
                    String msg = handler.getExtendedProperty(checkableFeatures.get(feature), new String[] { language.getLanguage() });
                    msgs.add(msg);
                }
                language = LanguageStateController.INSTANCE.nextLanguage();
            }
            Map<EStructuralFeature, String[]> msgsByFeature = messages.get(slide);
            if (msgsByFeature == null) msgsByFeature = new HashMap<EStructuralFeature, String[]>();
            msgsByFeature.put(feature, msgs.toArray(new String[msgs.size()]));
            messages.put(slide, msgsByFeature);
            return ok;
        }
        return true;
    }

    public String[] getValidationMessages(EObject object, EStructuralFeature feature) {
        Map<EStructuralFeature, String[]> msgsByFeature = messages.get(object);
        if (msgsByFeature == null) return new String[] {};
        String[] msgs = msgsByFeature.get(feature);
        if (msgs == null) msgs = new String[] {};
        return msgs;
    }
}
