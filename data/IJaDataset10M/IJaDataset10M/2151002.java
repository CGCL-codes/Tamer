package ru.ecom.jbossinstaller.service.impl.modify;

import ru.ecom.jbossinstaller.service.impl.file.IFile;
import ru.ecom.jbossinstaller.service.impl.file.JBossDefaultLibFile;

/**
 * Копирование ecom-jboss.jar
 */
public class CopyEcomJbossJarModify extends CopyFilesModify {

    public CopyEcomJbossJarModify() {
        super("ecom-jboss.jar", new IFile[] { new JBossDefaultLibFile("ecom-jboss.jar") }, "Копирование ecom-jboss.jar", "ecom-jboss-jar");
    }
}
