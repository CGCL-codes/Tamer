package jimporter.grouping;

import gnu.regexp.RE;
import gnu.regexp.REException;
import gnu.regexp.RESyntax;
import java.util.Iterator;
import jimporter.grouping.ImportGroupItem;
import jimporter.grouping.ImportGroupOption;
import jimporter.grouping.WhiteSpaceGroupItem;
import jimporter.importer.JavaImportList;
import org.gjt.sp.jedit.Buffer;

/**
 * Add spaces between import groups in the import statement list.
 *
 * @author Matthew Flower
 */
public class GroupingSpacer {

    /**
     *
     */
    private void createSpacerList() {
        Iterator importGroups = ImportGroupOption.load().iterator();
        while (importGroups.hasNext()) {
            ImportGroupItem igi = (ImportGroupItem) importGroups.next();
            if (igi instanceof PackageGroupItem) {
                String regexString = ((PackageGroupItem) igi).getPackagePattern();
                regexString.replaceAll("\\.", "[.]");
                regexString.replaceAll("\\*", "[^.;]*");
                regexString = "[[:space:]]*import[[:space:]]+" + regexString + ";";
                try {
                    RE re = new RE(regexString, RE.REG_MULTILINE, RESyntax.RE_SYNTAX_POSIX_EXTENDED);
                } catch (REException ree) {
                    System.out.println(ree);
                }
            } else if (igi instanceof AllOtherImportsItem) {
            }
        }
    }

    public static void addGroupingSpaces(Buffer buffer, JavaImportList list) {
        Iterator importStatements = new JavaImportList().getImportList().iterator();
    }
}

class GroupingItem {

    private RE re;

    private boolean spaceBefore;

    public GroupingItem(RE re, boolean spaceBefore) {
        this.re = re;
        this.spaceBefore = spaceBefore;
    }

    public boolean isMatch(String statement) {
        return re.isMatch(statement);
    }

    public boolean isSpaceBefore() {
        return spaceBefore;
    }
}
