package com.ehsunbehravesh.mypasswords.multilingual;

import com.ehsunbehravesh.mypasswords.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

public class LanguageManager {

    public static String[] getAvailableLanguages() throws LanguageException {
        File languagePath = new File(Utils.getPathOfJar() + "lang");
        if (languagePath.exists() && languagePath.isDirectory()) {
            FilenameFilter languageFileNameFilter = new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    File file = new File(dir, name);
                    if (file.exists() && file.isFile()) {
                        return name.toLowerCase().endsWith(".lang");
                    }
                    return false;
                }
            };
            String[] fileNames = languagePath.list(languageFileNameFilter);
            return fileNames;
        } else {
            throw new LanguageException("Language directory NOT found!");
        }
    }

    public static Properties getLanguage(String languageFile) throws IOException {
        File languagePath = new File(String.format("%slang/%s", Utils.getPathOfJar(), languageFile));
        Properties result = new Properties();
        FileReader reader = new FileReader(languagePath);
        result.load(reader);
        return result;
    }

    public static void main(String[] args) throws LanguageException, IOException {
        String[] availableLanguages = getAvailableLanguages();
        if (availableLanguages.length > 0) {
            Properties lang = getLanguage(availableLanguages[0]);
            System.out.println(lang.getProperty("DoYouWantToDeleteThePassword"));
        }
    }
}
