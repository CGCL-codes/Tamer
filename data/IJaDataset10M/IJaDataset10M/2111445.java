package com.doculibre.intelligid.webservices.plugin.impl;

import org.jdom.Element;
import com.doculibre.intelligid.delegate.FGDDelegate;
import com.doculibre.intelligid.entites.FichierElectronique;
import com.doculibre.intelligid.entites.SupportDocument;
import com.doculibre.intelligid.webservices.plugin.AddinUtils;
import com.doculibre.intelligid.webservices.plugin.base.BaseElectronicFileAddinService;
import com.doculibre.intelligid.webservices.plugin.model.AddinElectronicFileRequest;
import com.doculibre.intelligid.webservices.plugin.model.AddinException;

@SuppressWarnings("serial")
public class SetCollaborativeWorkService extends BaseElectronicFileAddinService {

    @Override
    protected Element doPluginService(AddinElectronicFileRequest request) throws AddinException {
        FichierElectronique fichier = request.getElectronicFile();
        SupportDocument support = fichier.getSupport();
        boolean desiredCollaborativeWorkValue = request.getRequiredServiceBooleanParameter("collaborativeWork");
        if (support.isTravailCollaboratif() != desiredCollaborativeWorkValue) {
            new FGDDelegate().setTravailCollaboratif(support, desiredCollaborativeWorkValue);
        }
        return AddinUtils.sendInformation(request);
    }
}
