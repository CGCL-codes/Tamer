package javax.i18n4java.proc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.i18n4java.data.I18NFile;
import javax.i18n4java.data.LanguageSet;
import javax.i18n4java.data.MultiLanguageTranslations;
import javax.i18n4java.data.SingleLanguageTranslations;
import javax.i18n4java.data.TRFile;
import javax.i18n4java.proc.I18NProjectConfiguration;
import javax.i18n4java.utils.FileSearch;

/**
 * This application converts all i18n files in the the specified directory and
 * converts them into tr files for usage in internationalized applications.
 * 
 * @author Rick-Rainer Ludwig
 */
public class I18NRelease {

    public static void release(File projectDirectory) throws IOException {
        new I18NRelease(projectDirectory).release();
    }

    private final I18NProjectConfiguration configuration;

    private final List<File> inputFiles = new ArrayList<File>();

    private I18NRelease(File projectDirectory) throws IOException {
        configuration = new I18NProjectConfiguration(projectDirectory);
    }

    private void release() throws IOException {
        findAllInputFiles();
        processFiles();
    }

    private void findAllInputFiles() {
        inputFiles.addAll(FileSearch.find(configuration.getI18nDirectory(), "*.i18n"));
    }

    private void processFiles() throws IOException {
        for (File file : inputFiles) {
            processFile(file);
        }
    }

    private void processFile(File file) throws IOException {
        File sourceFile = new File(configuration.getI18nDirectory(), file.getPath());
        MultiLanguageTranslations mlTranslations = I18NFile.read(sourceFile);
        for (Locale language : mlTranslations.getAvailableLanguages()) {
            File destinationFile = new File(configuration.getDestinationDirectory(), file.getPath().replaceAll("\\.i18n", "." + language + ".tr"));
            release(mlTranslations, language, destinationFile);
        }
    }

    private void release(MultiLanguageTranslations mlTranslations, Locale language, File file) throws IOException {
        SingleLanguageTranslations translations = new SingleLanguageTranslations();
        Set<String> sources = mlTranslations.getSources();
        for (String source : sources) {
            LanguageSet set = mlTranslations.get(source);
            if (set.has(language)) {
                translations.add(source, set.get(language));
            }
        }
        TRFile.write(file, translations);
    }
}
