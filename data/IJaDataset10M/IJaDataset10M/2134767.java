package org.eclipse.dltk.freemarker.internal.launching;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.dltk.freemarker.core.manager.FreemarkerSettingsManager;
import org.eclipse.dltk.freemarker.core.manager.FreemarkerTemplateManager;
import org.eclipse.dltk.freemarker.core.settings.IFreemarkerTemplateSettings;
import org.eclipse.dltk.freemarker.core.settings.provider.InstanceProviderException;
import org.eclipse.dltk.freemarker.core.util.SettingsUtils;
import org.eclipse.dltk.launching.DLTKRunnableProcess;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterConfig;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 
 * Freemarker runnable process implementation. The
 * {@link DLTKRunnableProcess#run()} method, execute merge Model with Template
 * (written into Template page from the Freemarker Editor) and display result of
 * the merge into Eclipse console.
 * 
 */
public class FreemarkerRunnableProcess extends DLTKRunnableProcess {

    public FreemarkerRunnableProcess(IInterpreterInstall install, ILaunch launch, InterpreterConfig config) {
        super(install, launch, config);
    }

    public void run() {
        IFile resourcesFile = super.getResourcesFile();
        File templateFile = super.getFile();
        Reader templateReader;
        try {
            templateReader = new FileReader(templateFile);
            String templateName = templateFile.getAbsolutePath();
            Configuration cfg = getConfiguration(resourcesFile);
            Object model = getModel(resourcesFile);
            String result = FreemarkerTemplateManager.getManager().process(templateName, templateReader, cfg, model);
            super.out(result);
        } catch (FileNotFoundException e) {
            super.err(e);
        } catch (IOException e) {
            super.err(e);
        } catch (TemplateException e) {
            super.err(e);
        } catch (InstanceProviderException e) {
            super.err(e);
        }
    }

    ;

    private Configuration getConfiguration(IFile templateFile) throws InstanceProviderException {
        return SettingsUtils.getConfiguration(templateFile);
    }

    public Object getModel(IFile templateFile) throws InstanceProviderException {
        return SettingsUtils.getModel(templateFile);
    }

    public IFreemarkerTemplateSettings getFreemarkerTemplate(IFile templateFile) throws CoreException {
        return FreemarkerSettingsManager.getManager().getTemplateSettings(templateFile);
    }
}
