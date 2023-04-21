package org.speakright.srfsurvey;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import org.speakright.core.IFlow;
import org.speakright.core.ModelBinder;
import org.speakright.core.SRFactory;
import org.speakright.core.SRInstance;
import org.speakright.core.SRLocations;
import org.speakright.core.SRLogger;
import org.speakright.core.SRRunner;
import org.speakright.srfsurvey.App.YesNoSurveyQuestion;
import org.speakright.sro.BaseSROQuestion;

public class AppFactory extends SRFactory {

    @Override
    protected void onCreateRunner(SRRunner run) {
        run.registerPromptFile(run.locations().projectDir() + "prompts.xml");
        String currentDir = SRLocations.fixupDir(System.getProperty("user.dir"));
        run.log("current dir: " + currentDir);
        Model M = new Model();
        M.m_dp = new DataProvider(SRLogger.createLogger());
        run.setModelBinder(M, new ModelBinder(M));
        run.getImpl().setInstrumentation(new Probe(M));
    }
}
