package com.google.code.genclipse.ide.editor.syntax;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

public class EbuildSyntaxBean {

    private class EclassFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".eclass");
        }
    }

    private String[] BASH_KEYWORDS = { "if", "then", "elif", "else", "fi" };

    private String[] EBUILD_VARIABLES = { "P", "PN", "PV", "PR", "PVR", "PF", "A", "DISTDIR", "FILESDIR", "WORKDIR", "S", "T", "D", "SLOT", "LICENSE", "KEYWORDS", "DESCRIPTION", "SRC_URI", "HOMEPAGE", "IUSE", "DEPEND", "RDEPEND", "RESTRICT" };

    private String[] EBUILD_METHODES = { "pkg_setup", "pkg_nofetch", "src_unpack", "src_compile", "src_install", "src_test", "pkg_preinst", "pkg_postinst", "pkg_prerm", "pkg_postrm", "pkg_config" };

    private String[] EBUILD_BUILDTIN_FUNCTIONS = { "use", "has_version", "best_version", "use_with", "use_enable", "check_KV", "keepdir", "econf", "einstall", "die", "elog", "einfo", "eerror", "ewarn" };

    private Vector<String> eClasses;

    private Vector<String> ebuildBuildinFunctions;

    private Vector<String> ebuildVariables;

    private Vector<String> ebuildMethods;

    private Vector<String> bashKeywords;

    public Vector<String> getBashKeywords() {
        return bashKeywords;
    }

    public Vector<String> getEbuildMethods() {
        return ebuildMethods;
    }

    public Vector<String> getEbuildVariables() {
        return ebuildVariables;
    }

    public Vector<String> getEbuildBuildinFunctions() {
        return ebuildBuildinFunctions;
    }

    public EbuildSyntaxBean() {
        this.eClasses = new Vector<String>();
        this.ebuildBuildinFunctions = new Vector<String>();
        this.ebuildVariables = new Vector<String>();
        this.ebuildMethods = new Vector<String>();
        this.bashKeywords = new Vector<String>();
        this.initEclasses();
        this.initEbuildBuildinFunctions();
        this.initEbuildVariables();
        this.initEbuildMethods();
        this.initBashKeywords();
    }

    public Vector<String> getEClasses() {
        return eClasses;
    }

    private void initEclasses() {
        File f = new File("/usr/portage/eclass");
        String[] eclasses = f.list(new EclassFilenameFilter());
        for (String eclassFile : eclasses) {
            eclassFile = eclassFile.substring(0, eclassFile.indexOf("."));
            this.eClasses.add(eclassFile);
        }
    }

    private void initEbuildBuildinFunctions() {
        for (String buildin : this.EBUILD_BUILDTIN_FUNCTIONS) {
            this.ebuildBuildinFunctions.add(buildin);
        }
    }

    private void initEbuildVariables() {
        for (String vars : this.EBUILD_VARIABLES) {
            this.ebuildVariables.add(vars);
        }
    }

    private void initEbuildMethods() {
        for (String method : this.EBUILD_METHODES) {
            this.ebuildMethods.add(method);
        }
    }

    private void initBashKeywords() {
        for (String keyword : this.BASH_KEYWORDS) {
            this.bashKeywords.add(keyword);
        }
    }
}
