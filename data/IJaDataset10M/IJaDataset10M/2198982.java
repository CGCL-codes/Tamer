package com.jes.classfinder.helpers.preferences;

import java.io.File;
import org.w3c.dom.Element;

public class JadPathXml {

    private static String RELATIVE_TO_CLASSFINDER_ROOT_PATH_ATTRIBUTE = "relativeToClassfinderRoot";

    private Element jadPathElement;

    private String jadPath;

    private String relativeToClassfinderRoot;

    public JadPathXml(Element jadPathElement) {
        this.jadPathElement = jadPathElement;
        parseJadPathElement(jadPathElement);
    }

    private void parseJadPathElement(Element jadPathElement) {
        this.jadPath = jadPathElement.getTextContent();
        this.relativeToClassfinderRoot = jadPathElement.getAttribute(RELATIVE_TO_CLASSFINDER_ROOT_PATH_ATTRIBUTE);
    }

    /**TODO correct BUG
     * this function assumes that developer checks out code from repository 
     * with command  svn co https://classfinder.svn.sourceforge.net/svnroot/classfinder
     * when  svn co https://classfinder.svn.sourceforge.net/svnroot/classfinder/trunk 
     * or different -- funciton returns incorrect absolute path to jad.exe
     * With non relative path to jad.exe in preferences it works OK.
     */
    public File getAbsolutePathToJad() {
        File absolutePathToJad;
        if (relativeToClassfinderRoot.trim().equals("0")) {
            absolutePathToJad = new File(jadPath);
        } else if (relativeToClassfinderRoot.trim().equals("1")) {
            String classfinderRootDirPath = System.getProperty("classfinder.root.dir");
            if (classfinderRootDirPath == null) {
                throw new RuntimeException("The classfinder.root.dir system property has not been " + "set when running classfinder. Above the " + "classfinder.root.dir you will see a structure " + "like: trunk/SVN_LOCAL/masterbuild/etc");
            }
            File classfinderRootDir = new File(classfinderRootDirPath);
            absolutePathToJad = new File(classfinderRootDir, this.jadPath.trim());
        } else {
            throw new RuntimeException(RELATIVE_TO_CLASSFINDER_ROOT_PATH_ATTRIBUTE + " attribute must have value 0 or 1. Instead it was: " + relativeToClassfinderRoot);
        }
        return absolutePathToJad;
    }

    public void setJadPath(String jadPath, boolean isRelativeToClassfinderRoot) {
        if (isRelativeToClassfinderRoot) {
            this.relativeToClassfinderRoot = "1";
            jadPathElement.setAttribute(this.RELATIVE_TO_CLASSFINDER_ROOT_PATH_ATTRIBUTE, "1");
        } else {
            this.relativeToClassfinderRoot = "0";
            jadPathElement.setAttribute(this.RELATIVE_TO_CLASSFINDER_ROOT_PATH_ATTRIBUTE, "0");
        }
        this.jadPath = jadPath;
        String nodeValue = jadPathElement.getTextContent();
        jadPathElement.setTextContent(jadPath.trim());
    }
}
