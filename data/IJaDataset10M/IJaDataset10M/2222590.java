package fr.insa.rennes.pelias.pcreator.preferences;

import java.io.IOException;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import fr.insa.rennes.pelias.pcreator.Application;

/**
 * @author otilia damian
 * description de la page de préférences pour la connection avec PExecutor
 *
 */
public class ConnectionPExecutorPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public static final String ID = "fr.insa.rennes.pelias.pcreator.PExecutorPreferences";

    public static final String USER = "user";

    public static final String PASSWORD = "password";

    public static final String SERVER = "server";

    public static final String PORT = "port";

    public static final String APPLICATION = "application";

    private ScopedPreferenceStore preferences;

    private StringFieldEditor user = null;

    private StringFieldEditor password = null;

    private StringFieldEditor application = null;

    private StringFieldEditor server = null;

    private IntegerFieldEditor port = null;

    /**
	 * Constructeur par default. Il charge les preferences sauvegardées dans le fichier de préférences 
	 */
    public ConnectionPExecutorPage() {
        super();
        preferences = new ScopedPreferenceStore(new ConfigurationScope(), Application.PLUGIN_ID);
        setPreferenceStore(preferences);
    }

    @Override
    protected void createFieldEditors() {
        user = new StringFieldEditor(USER, "Identifiant ", getFieldEditorParent());
        addField(user);
        password = new StringFieldEditor(PASSWORD, "Mot de passe ", getFieldEditorParent()) {

            protected void doFillIntoGrid(Composite parent, int numColumns) {
                super.doFillIntoGrid(parent, numColumns);
                getTextControl().setEchoChar('*');
            }
        };
        addField(password);
        server = new StringFieldEditor(SERVER, "Adresse du serveur ", getFieldEditorParent());
        addField(server);
        port = new IntegerFieldEditor(PORT, "Numéro de port ", getFieldEditorParent());
        addField(port);
        application = new StringFieldEditor(APPLICATION, "Nom de l'application ", getFieldEditorParent());
        addField(application);
    }

    public void init(IWorkbench workbench) {
    }

    public boolean performOk() {
        try {
            preferences.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Application.initiateRemoteRepositories(user.getStringValue(), password.getStringValue(), "http://" + server.getStringValue() + ":" + port.getStringValue() + "/" + application.getStringValue());
        return super.performOk();
    }
}
