package checker.gui.tree;

import java.util.ArrayList;
import java.util.Collection;
import checker.FileID;
import checker.gui.filter.Criteria;
import checker.license.License;
import checker.matching.LicenseMatch;

/**
 * This class represents an unknown file in a tree.
 * 
 * @author Veli-Jussi Raitila
 *
 */
public class FileUnknown extends FileAbstract {

    public FileUnknown(FileID f) {
        super(f);
    }

    @Override
    public ArrayList<LicenseMatch> getMatches() {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Collection<? extends LicenseTreeNode> getChildren() {
        return null;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public boolean isViewable() {
        return true;
    }

    @Override
    public boolean hasConflict() {
        return false;
    }

    @Override
    public boolean hasLicense() {
        return false;
    }

    @Override
    public boolean missLicense() {
        return false;
    }

    @Override
    public boolean meetsCriteria(Criteria c) {
        return false;
    }
}
