package uk.co.jemos.clanker.jelly.tags;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Logger;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import uk.co.jemos.clanker.exceptions.ConfigurationException;
import uk.co.jemos.clanker.util.PathUtils;
import uk.co.jemos.clanker.util.PathUtils.PathType;

/**
 * Sub element of Fileset which allows users to specify an include pattern
 *  
 * Copyright (c) 2007, Marco Tedone (Jemos - www.jemos.co.uk)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer. 
 *
 * Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution. 
 *
 * Neither the name of Jemos/Marco Tedone nor the names of its contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 * @author Marco Tedone <mtedone@jemos.co.uk>
 */
public class IncludeTag extends TagSupport {

    private static final Logger LOG = Logger.getLogger(IncludeTag.class.getName());

    /** The name passed as attribute */
    private String name;

    /** The name normalized for filtering */
    private String normalizedName;

    /** This tag's parent */
    private FileSetTag parent = null;

    /** The Heartbit parent */
    private HeartBitTag superParent;

    /** The Heartbit buffer for logging */
    private StringBuffer buff;

    public void doTag(XMLOutput output) throws MissingAttributeException, JellyTagException {
        this.validate(output);
        try {
            PathType pathType = PathUtils.resolvePathType(getName());
            File root = new File(parent.getDir());
            switch(pathType) {
                case ALL_IN_ALL_DIRS:
                case ALL_STARTS_WITH_IN_ALL_DIRS:
                case ALL_ENDS_WITH_IN_ALL_DIRS:
                case ALL_INCLUDED_WITHIN_IN_ALL_DIRS:
                case ALL_SPECIFIC_IN_ALL_DIRS:
                    this.fillSourcePathsWithAllPattern(root, pathType);
                    break;
                default:
                    File[] files = root.listFiles();
                    if (files.length == 0) {
                        LOG.warning("The folder: " + root.getAbsolutePath() + " doesn't contain any files.");
                        return;
                    }
                    for (File f : files) {
                        this.resolveFile(f, pathType);
                    }
                    break;
            }
        } catch (ConfigurationException e) {
            throw new JellyTagException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HeartBitTag getSuperParent() {
        return superParent;
    }

    public void setSuperParent(HeartBitTag superParent) {
        this.superParent = superParent;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    /**
	 * Validates the tag's attributes
	 * @param output
	 * @throws JellyTagException
	 */
    public void validate(XMLOutput output) throws JellyTagException {
        if (null == getName() || "".equals(getName())) {
            throw new MissingAttributeException("The name attribute is mandatory");
        }
        parent = (FileSetTag) findAncestorWithClass(FileSetTag.class);
        if (null == parent) {
            throw new JellyTagException("This tag must have a FileSet tag as parent");
        }
        superParent = (HeartBitTag) findAncestorWithClass(HeartBitTag.class);
        if (null == superParent) {
            throw new JellyTagException("This tag must have a root parent tag as HeartBit");
        }
        buff = superParent.getBuff();
        setNormalizedName(PathUtils.normalizeIncludePattern(getName()));
        buff.append("[Include]Normalized attribute name: " + getNormalizedName() + "\n");
    }

    /**
	 * Loops through all the subdirectories and delegates to another method the actual processing 
	 * @param root The dir attribute of this tag's parent
	 */
    private void fillSourcePathsWithAllPattern(File root, PathType pathType) {
        File[] subFiles = null;
        if (!root.isDirectory()) {
            return;
        }
        subFiles = root.listFiles();
        if (subFiles.length == 0) {
            return;
        }
        for (File subF : subFiles) {
            if (subF.isFile()) {
                this.resolveFile(subF, pathType);
            } else if (subF.isDirectory()) {
                fillSourcePathsWithAllPattern(subF, pathType);
            }
        }
    }

    /**
	 * Fills the super parent list of sources if the filter matches
	 * @param subF
	 * @param pathType
	 */
    private void resolveFile(File subF, PathType pathType) {
        if (!subF.isFile()) return;
        AllFilesFilter allFilter = null;
        BeginWithFilter beginFilter = null;
        EndWithFilter endFilter = null;
        FullNameFilter fullFilter = null;
        IncludedWithinFilter includeFilter = null;
        switch(pathType) {
            case ALL_IN_ALL_DIRS:
            case ALL_IN_THIS_DIR:
                allFilter = new AllFilesFilter();
                if (allFilter.accept(subF)) {
                    buff.append("[Include]Adding: " + subF.getAbsolutePath() + " to Heartbit list.\n");
                    superParent.addSourcePath(subF.getAbsolutePath());
                }
                break;
            case STARTS_WITH:
            case ALL_STARTS_WITH_IN_ALL_DIRS:
                if (null == beginFilter) beginFilter = new BeginWithFilter();
                if (beginFilter.accept(subF)) {
                    buff.append("[Include]Adding: " + subF.getAbsolutePath() + " to Heartbit list.\n");
                    superParent.addSourcePath(subF.getAbsolutePath());
                }
                break;
            case ENDS_WITH:
            case ALL_ENDS_WITH_IN_ALL_DIRS:
                if (null == endFilter) endFilter = new EndWithFilter();
                if (endFilter.accept(subF)) {
                    buff.append("[Include]Adding: " + subF.getAbsolutePath() + " to Heartbit list.\n");
                    superParent.addSourcePath(subF.getAbsolutePath());
                }
                break;
            case INCLUDED_WITHIN:
            case ALL_INCLUDED_WITHIN_IN_ALL_DIRS:
                if (null == includeFilter) includeFilter = new IncludedWithinFilter();
                if (includeFilter.accept(subF)) {
                    buff.append("[Include]Adding: " + subF.getAbsolutePath() + " to Heartbit list.\n");
                    superParent.addSourcePath(subF.getAbsolutePath());
                }
                break;
            case FULL_NAME:
            case ALL_SPECIFIC_IN_ALL_DIRS:
                if (null == fullFilter) fullFilter = new FullNameFilter();
                if (fullFilter.accept(subF)) {
                    buff.append("[Include]Adding: " + subF.getAbsolutePath() + " to Heartbit list.\n");
                    superParent.addSourcePath(subF.getAbsolutePath());
                }
                break;
        }
    }

    /**
	 * Accepts all files with extension '.java'
	 * @author mtedone 
	 *
	 */
    private class AllFilesFilter implements FileFilter {

        public boolean accept(File pathname) {
            return pathname.getName().toLowerCase().endsWith(".java") ? true : false;
        }
    }

    /**
	 * Accepts only Java source files with name starting with the name passed as attribute
	 * @author mtedone
	 *
	 */
    private class BeginWithFilter implements FileFilter {

        public boolean accept(File pathname) {
            if (!pathname.getAbsolutePath().endsWith(".java")) return false;
            int idx = pathname.getName().lastIndexOf(".java");
            String fileName = pathname.getName();
            if (idx > -1) {
                fileName = pathname.getName().substring(0, idx);
            }
            buff.append("\n[Include]File name: " + fileName + " Pattern: " + getNormalizedName() + "\n\n");
            return fileName.startsWith(getNormalizedName()) ? true : false;
        }
    }

    /**
	 * Accepts only files with file name ending with endPattern
	 * @author mtedone
	 *
	 */
    private class EndWithFilter implements FileFilter {

        public boolean accept(File pathname) {
            if (!pathname.getAbsolutePath().endsWith(".java")) return false;
            int idx = pathname.getName().lastIndexOf(".java");
            String fileName = pathname.getName();
            if (idx > -1) {
                fileName = pathname.getName().substring(0, idx);
            }
            buff.append("\n[Include]File name: " + fileName + " Pattern: " + getNormalizedName() + "\n\n");
            return fileName.endsWith(getNormalizedName()) ? true : false;
        }
    }

    /**
	 * Accepts only file names which contain the included Pattern
	 * @author mtedone
	 *
	 */
    private class IncludedWithinFilter implements FileFilter {

        public boolean accept(File pathname) {
            if (!pathname.getAbsolutePath().endsWith(".java")) return false;
            int idx = pathname.getName().lastIndexOf(".java");
            String fileName = pathname.getName();
            if (idx > -1) {
                fileName = pathname.getName().substring(0, idx);
            }
            buff.append("\n[Include]File name: " + fileName + " Pattern: " + getNormalizedName() + "\n\n");
            return fileName.indexOf(getNormalizedName()) > -1;
        }
    }

    /**
	 * Accepts only file names exactly matching fullNamePattern
	 * @author mtedone
	 *
	 */
    private class FullNameFilter implements FileFilter {

        public boolean accept(File pathname) {
            if (!pathname.getAbsolutePath().endsWith(".java")) return false;
            int idx = pathname.getName().lastIndexOf(".java");
            String fileName = pathname.getName();
            if (idx > -1) {
                fileName = pathname.getName().substring(0, idx);
            }
            buff.append("\n[Include]File name: " + fileName + " Pattern: " + getNormalizedName() + "\n\n");
            return fileName.equals(getNormalizedName());
        }
    }
}
