package org.fudaa.dodico.crue.projet.migrate;

import java.io.File;
import org.fudaa.ctulu.CtuluLibFile;
import org.fudaa.ctulu.CtuluLog;
import org.fudaa.ctulu.CtuluLogGroup;
import org.fudaa.dodico.crue.common.BusinessMessages;
import org.fudaa.dodico.crue.common.ConnexionInformation;
import org.fudaa.dodico.crue.common.ConnexionInformationDefault;
import org.fudaa.dodico.crue.config.CrueOptions;
import org.fudaa.dodico.crue.io.Crue10FileFormat;
import org.fudaa.dodico.crue.io.Crue10FileFormatFactory;
import org.fudaa.dodico.crue.io.common.CrueFileType;
import org.fudaa.dodico.crue.metier.etude.EMHProjet;
import org.fudaa.dodico.crue.projet.coeur.CoeurConfigContrat;

public class ScenarioMigrateProcessor {

    private final EMHProjet initial;

    private final CrueOptions crueOptions;

    private ConnexionInformation connexionInformation;

    /**
   * @param base
   */
    public ScenarioMigrateProcessor(EMHProjet base, ConnexionInformation connexionInformation, CrueOptions crueOptions) {
        super();
        this.initial = base;
        this.crueOptions = crueOptions;
        this.connexionInformation = connexionInformation;
        if (this.connexionInformation == null) {
            this.connexionInformation = ConnexionInformationDefault.getDefault();
        }
    }

    /**
   * @param base
   */
    public ScenarioMigrateProcessor(EMHProjet base, CrueOptions crueOptions) {
        this(base, null, crueOptions);
    }

    public CtuluLogGroup exportTo(File targetDir, String etuFileName, CoeurConfigContrat version) {
        CtuluLogGroup logs = new CtuluLogGroup(BusinessMessages.RESOURCE_BUNDLE);
        CtuluLog log = logs.getMainLog();
        TargetDirValidator validator = new TargetDirValidator(this.initial.getParentDirOfEtuFile());
        validator.valid(targetDir, log, new File(targetDir, etuFileName));
        if (log.containsFatalError()) {
            return logs;
        }
        final boolean targetDirExists = targetDir.exists();
        FilteredProjectCreator creator = new FilteredProjectCreator(initial, connexionInformation);
        EMHProjet targetProject = creator.create(targetDir, version, log);
        if (log.containsFatalError()) {
            return logs;
        }
        ScenarioCopier scenarioCopier = new ScenarioCopier(initial, crueOptions);
        scenarioCopier.copy(targetProject, logs);
        String filename = etuFileName;
        Crue10FileFormat<Object> fileFormat = Crue10FileFormatFactory.getVersion(CrueFileType.ETU, version);
        if (!filename.endsWith(fileFormat.getExtension())) {
            filename = filename + "." + fileFormat.getExtension();
        }
        fileFormat.writeMetierDirect(targetProject, new File(targetDir, filename), logs.createLog(), initial.getPropDefinition());
        if (logs.containsFatalError()) {
            this.deleteChildren(targetDir);
            if (!targetDirExists) {
                targetDir.delete();
            }
        }
        return logs;
    }

    private void deleteChildren(File directory) {
        for (File file : directory.listFiles()) {
            CtuluLibFile.deleteDir(file);
        }
    }
}
