package edu.asu.vspace.dspace.properties;

import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.Wizard;
import de.mpiwg.vspace.diagram.providers.EditingDomainManager;
import de.mpiwg.vspace.languages.LanguagePropertyExtension;
import de.mpiwg.vspace.languages.language.Language;
import de.mpiwg.vspace.languages.providers.LanguagePropertyHelper;
import de.mpiwg.vspace.languages.providers.LanguagePropertyProvider;
import de.mpiwg.vspace.util.PropertyHandler;
import de.mpiwg.vspace.util.PropertyHandlerRegistry;
import edu.asu.vspace.dspace.Activator;
import edu.asu.vspace.dspace.Constants;
import edu.asu.vspace.dspace.dspaceMetamodel.DSpaceImage;
import edu.asu.vspace.dspace.dspaceMetamodel.DSpaceText;
import edu.asu.vspace.dspace.dspaceMetamodel.DspaceMetamodelPackage;
import edu.asu.vspace.dspace.dspaceMetamodel.extension.datamodel.IDatastream;
import edu.asu.vspace.dspace.internal.DSpaceFolderManager;

public class DSpaceTextWizard extends Wizard {

    private SelectionWizardPage selectionPage;

    private DSpaceText text;

    private Language language;

    public DSpaceTextWizard(DSpaceText text, Language language) {
        this.text = text;
        this.language = language;
    }

    @Override
    public void addPages() {
        PropertyHandler handler = PropertyHandlerRegistry.REGISTRY.getPropertyHandler(Activator.PLUGIN_ID, Constants.PROPERTIES_FILE);
        selectionPage = new TextSelectionWizardPage(handler.getProperty("_selection_wizard_page_title"));
        addPage(selectionPage);
    }

    @Override
    public boolean performFinish() {
        IDatastream datastream = selectionPage.getSelectedDatastream();
        LanguagePropertyHelper.INSTANCE.setPropertyValue(text, DspaceMetamodelPackage.Literals.DSPACE_TEXT__HANDLE, datastream.getHandle(), language);
        LanguagePropertyHelper.INSTANCE.setPropertyValue(text, DspaceMetamodelPackage.Literals.DSPACE_TEXT__SEQUENCE_ID, datastream.getSequenceID(), language);
        LanguagePropertyHelper.INSTANCE.setPropertyValue(text, DspaceMetamodelPackage.Literals.DSPACE_TEXT__DSPACE_HOST_ID, selectionPage.getSelectedHost().getId(), language);
        LanguagePropertyHelper.INSTANCE.setPropertyValue(text, DspaceMetamodelPackage.Literals.DSPACE_TEXT__FILENAME, datastream.getFilename(), language);
        LanguagePropertyHelper.INSTANCE.setPropertyValue(text, DspaceMetamodelPackage.Literals.DSPACE_TEXT__MIME_TYPE, datastream.getMIMEType(), language);
        Path path = new Path(datastream.getLocalPath());
        String ext = path.getFileExtension();
        String localFile = DSpaceFolderManager.INSTANCE.addLocalFile(datastream.getLocalPath(), datastream.getCleanedHandle() + "_" + datastream.getSequenceID() + "." + ext);
        LanguagePropertyHelper.INSTANCE.setPropertyValue(text, DspaceMetamodelPackage.Literals.DSPACE_TEXT__LOCAL_COPY_PATH, localFile, language);
        DSpaceFolderManager.INSTANCE.cleanDownloads();
        return true;
    }

    @Override
    public boolean performCancel() {
        DSpaceFolderManager.INSTANCE.cleanDownloads();
        return super.performCancel();
    }
}
