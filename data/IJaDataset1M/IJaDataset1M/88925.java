package com.rapidminer.operator.nio.xml;

import java.io.File;
import com.rapidminer.gui.wizards.ConfigurationListener;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.nio.AbstractDataImportWizard;
import com.rapidminer.operator.nio.MetaDataDeclarationWizardStep;
import com.rapidminer.operator.nio.StoreDataWizardStep;
import com.rapidminer.operator.nio.model.AbstractDataResultSetReader;
import com.rapidminer.operator.nio.model.DataResultSetFactory;
import com.rapidminer.repository.RepositoryLocation;

/**
 * This is the Wizard for XML Import. It consists of several steps:
 * - Selecting XML file
 * - Specifying XPath Expression for examples
 * - Specifying XPath Expressions for attributes
 * - Defining Meta Data
 * 
 * @author Sebastian Land
 */
public class XMLImportWizard extends AbstractDataImportWizard {

    private static final long serialVersionUID = 1L;

    public XMLImportWizard() throws OperatorException {
        this(null, null, null);
    }

    public XMLImportWizard(File file, RepositoryLocation preselectedLocation) throws OperatorException {
        super(null, preselectedLocation, "data_import_wizard");
        addStep(new MetaDataDeclarationWizardStep(getState()));
        if (getReader() == null) {
            addStep(new StoreDataWizardStep(this, getState(), (preselectedLocation != null) ? preselectedLocation.getAbsoluteLocation() : null));
        }
        layoutDefault(HUGE);
    }

    public XMLImportWizard(XMLExampleSource source, ConfigurationListener listener, RepositoryLocation preselectedLocation) throws OperatorException {
        super(source, preselectedLocation, "data_import_wizard");
        addStep(new XMLFileSelectionWizardStep(this, (XMLResultSetConfiguration) getState().getDataResultSetFactory()));
        addStep(new XMLNamespaceMapWizardStep(this, (XMLResultSetConfiguration) getState().getDataResultSetFactory()));
        addStep(new XMLExampleExpressionWizardStep(this, (XMLResultSetConfiguration) getState().getDataResultSetFactory()));
        addStep(new XMLAttributeExpressionWizardStep(this, (XMLResultSetConfiguration) getState().getDataResultSetFactory()));
        addStep(new MetaDataDeclarationWizardStep(getState()));
        if (getReader() == null) {
            addStep(new StoreDataWizardStep(this, getState(), (preselectedLocation != null) ? preselectedLocation.getAbsoluteLocation() : null));
        }
        layoutDefault(HUGE);
    }

    @Override
    protected DataResultSetFactory makeFactory(AbstractDataResultSetReader reader) throws OperatorException {
        if (reader != null) {
            return new XMLResultSetConfiguration((XMLExampleSource) reader);
        } else {
            return new XMLResultSetConfiguration();
        }
    }
}
