package edu.asu.vogon.ontology.ui.control;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import edu.asu.vogon.digitalHPS.IConcept;
import edu.asu.vogon.digitalHPS.IVocabularyEntry;
import edu.asu.vogon.digitalHPS.provider.DigitalHPSItemProviderAdapterFactory;
import edu.asu.vogon.digitalHPS.provider.IConceptItemProvider;
import edu.asu.vogon.model.Ontology;
import edu.asu.vogon.ontology.ui.Activator;
import edu.asu.vogon.util.files.FileHandler;
import edu.asu.vogon.util.images.ImageRegistry;

public class OntologyListLabelProvider extends LabelProvider {

    public String getText(Object element) {
        if (element instanceof Ontology) {
            return ((Ontology) element).getName();
        }
        if (element instanceof IConcept) {
            DigitalHPSItemProviderAdapterFactory factoryRel = new DigitalHPSItemProviderAdapterFactory();
            IConceptItemProvider provider = (IConceptItemProvider) factoryRel.createIConceptAdapter();
            return provider.getText(element);
        }
        return super.getText(element);
    }

    public Image getImage(Object element) {
        if (element instanceof Ontology) {
            File file = FileHandler.getAbsoluteFileFromProject(Activator.PLUGIN_ID, "icons/ontology.png");
            if (file != null) {
                try {
                    URL url = file.toURL();
                    return ImageRegistry.REGISTRY.getImage(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (element instanceof IConcept) {
            DigitalHPSItemProviderAdapterFactory factoryRel = new DigitalHPSItemProviderAdapterFactory();
            IConceptItemProvider provider = (IConceptItemProvider) factoryRel.createIConceptAdapter();
            return ImageRegistry.REGISTRY.getImage((URL) provider.getImage(element));
        }
        return super.getImage(element);
    }
}
