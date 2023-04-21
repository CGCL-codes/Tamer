package edu.asu.vogon.embryo.ui;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.wizard.Wizard;
import edu.asu.vogon.embryo.Activator;
import edu.asu.vogon.embryo.embryomodel.EmbryoMetadata;
import edu.asu.vogon.embryo.embryomodel.EmbryomodelPackage;
import edu.asu.vogon.embryo.service.Constants;
import edu.asu.vogon.model.DCMetadata;
import edu.asu.vogon.model.ModelFactory;
import edu.asu.vogon.model.ModelPackage;
import edu.asu.vogon.model.Text;
import edu.asu.vogon.model.TextMetadata;
import edu.asu.vogon.util.domain.EditingDomainManager;
import edu.asu.vogon.util.properties.PropertyHandler;
import edu.asu.vogon.util.properties.PropertyHandlerRegistry;

public class TextInfoWizard extends Wizard {

    private MetadataWizardPage metadataPage;

    private Text text;

    public TextInfoWizard(Text text) {
        this.text = text;
    }

    @Override
    public void addPages() {
        PropertyHandler handler = PropertyHandlerRegistry.REGISTRY.getPropertyHandler(Activator.PLUGIN_ID, Constants.PROPERTIES_FILE);
        metadataPage = new MetadataWizardPage(handler.getProperty("_text_info_page_title"), text.getMetadata());
        addPage(metadataPage);
        super.addPages();
    }

    @Override
    public boolean performFinish() {
        TextMetadata metadata = text.getMetadata();
        if (!(metadata instanceof EmbryoMetadata)) {
            return false;
        }
        {
            Command cmd = SetCommand.create(EditingDomainManager.INSTANCE.getEditingDomain(), metadata, EmbryomodelPackage.Literals.EMBRYO_METADATA__CREATOR, metadataPage.getAuthors());
            EditingDomainManager.INSTANCE.getEditingDomain().getCommandStack().execute(cmd);
        }
        {
            Command cmd = SetCommand.create(EditingDomainManager.INSTANCE.getEditingDomain(), metadata, ModelPackage.Literals.TEXT_METADATA__TITLE, metadataPage.getTextTitle());
            EditingDomainManager.INSTANCE.getEditingDomain().getCommandStack().execute(cmd);
        }
        {
            Command cmd = SetCommand.create(EditingDomainManager.INSTANCE.getEditingDomain(), metadata, EmbryomodelPackage.Literals.EMBRYO_METADATA__DESCRIPTION, metadataPage.getTextDescription());
            EditingDomainManager.INSTANCE.getEditingDomain().getCommandStack().execute(cmd);
        }
        {
            Command cmd = SetCommand.create(EditingDomainManager.INSTANCE.getEditingDomain(), metadata, EmbryomodelPackage.Literals.EMBRYO_METADATA__KEYWORDS, metadataPage.getKeywords());
            EditingDomainManager.INSTANCE.getEditingDomain().getCommandStack().execute(cmd);
        }
        {
            Command cmd = SetCommand.create(EditingDomainManager.INSTANCE.getEditingDomain(), metadata, EmbryomodelPackage.Literals.EMBRYO_METADATA__PID, metadataPage.getPID());
            EditingDomainManager.INSTANCE.getEditingDomain().getCommandStack().execute(cmd);
        }
        {
            Command cmd = SetCommand.create(EditingDomainManager.INSTANCE.getEditingDomain(), metadata, EmbryomodelPackage.Literals.EMBRYO_METADATA__SUBJECT_CATEGORY, metadataPage.getSubjectCategory());
            EditingDomainManager.INSTANCE.getEditingDomain().getCommandStack().execute(cmd);
        }
        {
            Command cmd = SetCommand.create(EditingDomainManager.INSTANCE.getEditingDomain(), metadata, EmbryomodelPackage.Literals.EMBRYO_METADATA__SUBJECT_FIELD, metadataPage.getSubjectField());
            EditingDomainManager.INSTANCE.getEditingDomain().getCommandStack().execute(cmd);
        }
        {
            Command cmd = SetCommand.create(EditingDomainManager.INSTANCE.getEditingDomain(), metadata, EmbryomodelPackage.Literals.EMBRYO_METADATA__SUBJECT_METATAGS, metadataPage.getSubjectMetatags());
            EditingDomainManager.INSTANCE.getEditingDomain().getCommandStack().execute(cmd);
        }
        return true;
    }
}
