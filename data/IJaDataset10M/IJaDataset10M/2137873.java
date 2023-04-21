package yaw.cjef.templates.bu.model.createModel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import yaw.cjef.templates.bu.model.addMember.ModelMember;
import yaw.cjef.util.CJEFUtil;
import yaw.cjef.util.Import;
import yaw.cjef.util.TMCWizard;
import yaw.core.wizard.WizardFile;

public class Controller extends TMCWizard<Model> {

    private TemplateModel tModel = new TemplateModel();

    private TemplateIModel tIModel = new TemplateIModel();

    private TemplateDTO tDTO = new TemplateDTO();

    private TemplatePersistence tPers = new TemplatePersistence();

    public Model createModel() {
        return new Model();
    }

    public void exec() {
        ctx.setDescription("Erzeugung des cJEF-Models <" + model.modelName + ">");
        String fileModel = model.getProjectSrc() + getJavaFilenameModel(model.getModelClassname(model.modelName), model.subPackageName);
        String fileDA = model.getProjectSrc() + getJavaFilenamePersistence(model.getDAClassname(model.modelName, true), model.subPackageName);
        String fileDTO = model.getProjectSrc() + getJavaFilenameDTO(model.getDTOClassname(model.modelName), model.subPackageName);
        String fileIModel = model.getProjectSrc() + getJavaFilenameIModel(model.getIModelClassname(model.modelName), model.subPackageName);
        Set<String> noCopyUserRange = new HashSet<String>();
        noCopyUserRange.add("-IMPORT");
        WizardFile wFileModel = ctx.getFile(fileModel);
        WizardFile wFileDTO = ctx.getFile(fileDTO);
        WizardFile wFileIModel = ctx.getFile(fileIModel);
        String rangeImportModel = wFileModel.getOldRange("USER-IMPORT");
        String rangeUserDataModel = wFileModel.getOldRange("USER-DATA");
        String rangeImportDTO = wFileDTO.getOldRange("USER-IMPORT");
        String rangeUserDataDTO = wFileDTO.getOldRange("USER-DATA");
        String rangeImportIModel = wFileIModel.getOldRange("USER-IMPORT");
        ctx.exec(tModel, model, wFileModel, rangeImportModel != null ? noCopyUserRange : null);
        ctx.exec(tModel, model, wFileModel, rangeUserDataModel != null ? noCopyUserRange : null);
        ctx.exec(tIModel, model, wFileIModel, rangeImportIModel != null ? noCopyUserRange : null);
        ctx.exec(tDTO, model, wFileDTO, rangeImportDTO != null ? noCopyUserRange : null);
        ctx.exec(tDTO, model, wFileDTO, rangeUserDataDTO != null ? noCopyUserRange : null);
        if (rangeImportModel != null) {
            wFileModel.replace("USER-IMPORT", rangeImportModel);
        }
        if (rangeImportDTO != null) {
            wFileDTO.replace("USER-IMPORT", rangeImportDTO);
        }
        if (rangeImportIModel != null) {
            wFileIModel.replace("USER-IMPORT", rangeImportIModel);
        }
        if (rangeUserDataDTO != null) {
            wFileDTO.replace("USER-DATA", rangeUserDataDTO);
        }
        if (rangeUserDataModel != null) {
            wFileModel.replace("USER-DATA", rangeUserDataModel);
        }
        WizardFile wFileDA = null;
        if (!model.isTransitory()) {
            wFileDA = ctx.getFile(fileDA);
            ctx.exec(tPers, model, wFileDA, null);
            String rangeImportDA = wFileDA.getOldRange("USER-IMPORT");
            if (rangeImportDA != null) {
                wFileDA.replace("USER-IMPORT", rangeImportDA);
            }
        }
        if (!wFileDTO.hasItem("-DATA", CJEFUtil.SERIALVERSION_RE)) {
            wFileDTO.insert("USER-DATA", CJEFUtil.SERIALVERSION_LINE + wFileDTO.getLineDelimiter());
        }
        for (ModelMember modelMember : model.member) {
            for (Iterator<Import> iterator = modelMember.getImportCode(true); iterator.hasNext(); ) {
                Import importCode = iterator.next();
                if (!wFileModel.hasItem("-IMPORT", importCode.getQualifiedModel()) && !wFileModel.hasItem("-IMPORT", importCode.getQualifiedPackage())) {
                    wFileModel.insert("USER-IMPORT", importCode.getQualifiedModel() + wFileModel.getLineDelimiter());
                }
            }
            for (Iterator<Import> iterator = modelMember.getImportCode(false); iterator.hasNext(); ) {
                Import importCode = iterator.next();
                if (!wFileDTO.hasItem("-IMPORT", importCode.getQualifiedModel()) && !wFileDTO.hasItem("-IMPORT", importCode.getQualifiedPackage())) {
                    wFileDTO.insert("USER-IMPORT", importCode.getQualifiedModel() + wFileDTO.getLineDelimiter());
                }
            }
            if (wFileDA != null) {
                for (Iterator<Import> iterator = modelMember.getImportCodeDA(); iterator.hasNext(); ) {
                    Import importCode = iterator.next();
                    if (!wFileDA.hasItem("-IMPORT", importCode.getQualifiedModel()) && !wFileDA.hasItem("-IMPORT", importCode.getQualifiedPackage())) {
                        wFileDA.insert("USER-IMPORT", importCode.getQualifiedModel() + wFileDA.getLineDelimiter());
                    }
                }
            }
        }
        if (wFileModel.isNew()) {
            WizardFile wSubsystemFile = ctx.getFile(model.getProjectSrc() + getJavaFilenameSubsystem());
            wSubsystemFile.insert("CID", "  public static final TMCID " + model.modelName + " = new TMCID(\"" + model.modelName + "\");" + wSubsystemFile.getLineDelimiter());
            if (model.isTransitory()) {
                if (model.subPackageName.equals("")) {
                    wSubsystemFile.insert("FACTORY", "    addModelFactory(" + model.modelName + ", null);" + wSubsystemFile.getLineDelimiter());
                } else {
                    wSubsystemFile.insert("FACTORY", "    addModelFactory(" + model.modelName + ", \"" + model.subPackageName + "\");" + wSubsystemFile.getLineDelimiter());
                }
            } else {
                if (model.subPackageName.equals("")) {
                    wSubsystemFile.insert("FACTORY", "    addPersistentModelFactory(" + model.modelName + ", null);" + wSubsystemFile.getLineDelimiter());
                } else {
                    wSubsystemFile.insert("FACTORY", "    addPersistentModelFactory(" + model.modelName + ", \"" + model.subPackageName + "\");" + wSubsystemFile.getLineDelimiter());
                }
            }
        }
    }
}
