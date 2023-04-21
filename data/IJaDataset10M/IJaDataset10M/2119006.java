package xhtmldoclet.pages;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import xhtmldoclet.XhtmlPageWriter;
import com.sun.javadoc.PackageDoc;

/**
 * Creates "overview-summary.html" which details documented packages.
 */
@SuppressWarnings("unchecked")
public class OverviewSummary extends XhtmlPageWriter {

    /**
	 * Array of Packages to be documented.
	 */
    private PackageDoc[] packages;

    /**
	 * Map representing the group of packages as specified on the command line.
	 * 
	 * @see com.sun.tools.doclets.internal.toolkit.util.Group
	 */
    private Map groupPackageMap;

    /**
	 * List to store the order groups as specified on the command line.
	 */
    private List groupList;

    /**
	 * Generate page containing a summary of packages and project description.
	 */
    public static void generatePage() {
        String filename = "overview-summary" + conf.ext;
        try {
            new OverviewSummary(filename);
        } catch (IOException exc) {
            throw XhtmlPageWriter.docletException(exc, filename);
        }
    }

    /**
	 * Initialize and create overview summary page.
	 * 
	 * @param filename The desired name of the file (with extension).
	 * @throws IOException If creation of {@link FileOutputStream} fails.
	 */
    private OverviewSummary(String filename) throws IOException {
        super(filename);
        pageType = PageType.OVERVIEW;
        packages = conf.packages;
        groupPackageMap = (packages != null) ? conf.group.groupPackages(packages) : new java.util.HashMap();
        groupList = conf.group.getGroupList();
        windowTitle = conf.getText("OverviewTitle");
        String[] metakeywords = { windowTitle };
        if (conf.doctitle.length() > 0) {
            metakeywords[0] += ", " + conf.doctitle;
        }
        String setFrameScript = "parent.packageFrame.location.href = 'allclasses-frame" + conf.ext + "';";
        printXhtmlHeader(metakeywords, true, setFrameScript);
        println(open("h1") + conf.getText("OverviewTitle") + close("h1"));
        printOverviewHeader();
        if (conf.packages != null && conf.packages.length > 0) {
            Arrays.sort(conf.packages);
            println(open("h2") + conf.getText("summary.Package") + close("h2"));
            println(open("table"));
            for (PackageDoc pkg : packages) printIndexRow(pkg);
            println(close("table"));
        }
        printOverview();
        printXhtmlFooter();
        this.close();
    }

    /**
	 * Print the overview summary comment for this documentation. Print one line
	 * summary at the top of the page and generate a link to the description,
	 * which is generated at the end of this page.
	 */
    private void printOverviewHeader() {
        if (conf.root.inlineTags().length > 0) {
            println(conf.getText("doclet.See"));
            println(linkToLabelHref(conf.getText("Description"), "#overview_description"));
        }
    }

    /**
	 * Print each package in separate rows in the index table. Generate link to
	 * each package.
	 * 
	 * @param pkg Package to which link is to be generated.
	 */
    private void printIndexRow(PackageDoc pkg) {
        if (pkg != null && pkg.name().length() > 0) {
            println(open("tr"));
            println(new TagBuilder("td").add("width", "20%").getOpen() + linkToLabelHref(pkg.name(), pathToPackageFile(pkg, "package-summary" + conf.ext)) + close("td"));
            print(open("td"));
            print(getCommentSummary(pkg));
            println(close("td"));
            println(close("tr"));
        }
    }

    /**
	 * Print the overview comment, then generate the tag information, as
	 * directed in the file specified by the "-overview" option.
	 */
    private void printOverview() {
        if (conf.root.inlineTags().length > 0) {
            println(openDivWithID("overview_description"));
            println(close("div"));
        }
    }

    /** Highlight "Overview" as current section, don't create link. */
    protected void navLinkOverview() {
        println(listItemCurrent(OVERVIEW));
    }
}
