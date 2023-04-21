package yaw.cjef.templates.bu.usecase.addMember;

import yaw.cjef.util.TMCWizard;
import yaw.core.wizard.WizardFile;

public class Controller extends TMCWizard<Model> {

    public Model createModel() {
        return new Model();
    }

    public void exec() {
        String fileUsecaseDTO = getJavaFilenameDTO(model.getUsecaseDTOClassname(model.usecaseName), model.subPackageName);
        WizardFile wUsecaseDTO = ctx.getFile(model.getProjectSrc() + fileUsecaseDTO);
        wUsecaseDTO.insert("DATA-MEMBER", model.getDTODataMember());
    }
}
