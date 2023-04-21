package net.sf.ocmscripting.php;

import net.sf.ocmscripting.CmsScriptingUtil;
import org.opencms.file.CmsObject;

/**
 * A Visitor which transforms the given source code to contain
 * normalized/absolute RFS paths.
 * 
 */
public class PHPPathTranslationVisitor implements PHPIncludeVisitor {

    /**
     * The translator applied to the paths.
     */
    private PHPPathTranslator pathTranslator;

    /**
     * The translated source code.
     */
    private StringBuffer translatedContents;

    /**
     * The path to the repository.
     */
    private String repositoryPath;

    /**
     * The CmsObject used for reading the code.
     */
    private CmsObject cms;

    /**
     * Constructs a new Visitor of this kind.
     * 
     * @param pathTranslator
     *            The translator to be used
     * @param repositoryPath
     *            The path to the repository
     * @param cms
     *            The CmsObject used to read the source
     */
    public PHPPathTranslationVisitor(PHPPathTranslator pathTranslator, String repositoryPath, CmsObject cms) {
        this.pathTranslator = pathTranslator;
        this.repositoryPath = repositoryPath;
        this.cms = cms;
        this.translatedContents = new StringBuffer();
    }

    /**
     * @see PHPIncludeVisitor#visitIncludeFile(String)
     */
    public void visitIncludeFile(String file) {
        String translatedPath = pathTranslator.translate(file);
        translatedPath = CmsScriptingUtil.computeRFSPath(repositoryPath, cms, translatedPath);
        translatedPath = CmsScriptingUtil.ensureSuffix(translatedPath, CmsPHPScriptLoader.PHP_SUFFIX);
        translatedContents.append(translatedPath);
    }

    /**
     * @see PHPIncludeVisitor#visitSurroundingText(String)
     */
    public void visitSurroundingText(String text) {
        translatedContents.append(text);
    }

    /**
     * Returns the translated source code.
     * 
     * @return The translated source code
     */
    public String getTranslatedContents() {
        return translatedContents.toString();
    }
}
