package yaw.cjef.templates.projectinitializing;

import yaw.cjef.util.TMCWizard;
import yaw.core.wizard.WizardFile;

public class Controller extends TMCWizard<Model> {

    Template template0 = new Template();

    public Model createModel() {
        return new Model();
    }

    public void exec() {
        execTemplate(template0, model.getProjectPath() + getPropertiesFilename("/wizard", "", ""));
        String path = model.getSrcRoot() + model.projectPackage.replace('.', '/');
        this.ctx.getFile(path + "/" + WizardFile.CREATEFOLDER);
    }
}
