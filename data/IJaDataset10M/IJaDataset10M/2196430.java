package com.foursoft.fourever.variants.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import com.foursoft.fourever.objectmodel.EntityInstance;
import com.foursoft.fourever.objectmodel.Link;
import com.foursoft.fourever.objectmodel.ObjectModel;
import com.foursoft.fourever.variants.ModelManipulationManager;
import com.foursoft.fourever.vmodell.regular.V_Modell;
import com.foursoft.fourever.vmodell.regular.V_Modellvariante;
import com.foursoft.fourever.vmodell.regular.impl.V_ModellImpl;
import com.foursoft.fourever.xmlfileio.Document;
import com.foursoft.fourever.xmlfileio.XMLFileIOManager;
import com.foursoft.fourever.xmlfileio.exception.DocumentLockedException;
import com.foursoft.fourever.xmlfileio.exception.MissingLocationException;
import com.foursoft.fourever.xmlfileio.exception.SchemaProcessingException;
import com.foursoft.fourever.xmlfileio.exception.XMLProcessingException;

public abstract class ModelManipulationManagerImpl implements ModelManipulationManager {

    protected XMLFileIOManager xmlManager = null;

    protected ModelManipulationManagerImpl(XMLFileIOManager xmlManager) {
        this.xmlManager = xmlManager;
    }

    public void doModelManipulation(File sourceFile, File targetFile, File manipulationSpecification) throws VariantException {
        try {
            Document document = this.xmlManager.openDocument(sourceFile);
            doModelManipulation(document, manipulationSpecification);
            document.getRootFragment().setLocation(targetFile);
            document.getRootFragment().save();
            this.xmlManager.closeDocument(document);
        } catch (IOException e) {
            throw new VariantException("Could not save manipulated result XML file to " + targetFile.getPath(), e);
        } catch (DocumentLockedException e) {
            throw new VariantException("Could not save manipulated result XML file to " + targetFile.getPath(), e);
        } catch (MissingLocationException e) {
            throw new VariantException("Could not save manipulated result XML file to " + targetFile.getPath(), e);
        } catch (SchemaProcessingException e) {
            throw new VariantException("Could not open/close XML file " + sourceFile.getPath(), e);
        } catch (XMLProcessingException e) {
            throw new VariantException("Could not open/close XML file " + sourceFile.getPath(), e);
        }
    }

    public void doModelManipulation(Document document, File manipulationSpecification) throws VariantException {
        ObjectModel om = document.getObjectModel();
        Iterator<Link> rootLinks = om.getRootLinks();
        Link rootLink = rootLinks.next();
        EntityInstance root = (EntityInstance) rootLink.getFirstTarget();
        V_Modell vmInstance = new V_ModellImpl(root);
        for (V_Modellvariante vmvInstance : vmInstance.getV_Modellvariantes()) {
            doModelManipulation(vmvInstance, manipulationSpecification);
        }
    }
}
