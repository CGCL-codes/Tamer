package au.gov.qld.dnr.dss.v1.ui.option.comp;

import au.gov.qld.dnr.dss.v1.framework.Framework;
import au.gov.qld.dnr.dss.v1.framework.interfaces.ResourceManager;
import org.swzoo.log2.core.*;
import au.gov.qld.dnr.dss.v1.ui.option.AbstractPropertiesComponent;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;

/**
 * Report panel.
 */
public class ReportComponent extends DefaultComponent {

    String repHeader;

    String repText;

    String indexHeader;

    String indexText;

    int indexTextLen;

    String browserHeader;

    String browserText;

    Frame frame;

    public ReportComponent(Frame frame) {
        this.frame = frame;
        repHeader = resources.getProperty("dss.report.option.loc.header", "BASE LOCATION FOR:");
        repText = resources.getProperty("dss.report.option.loc.text", "REPORTING DIRECTORY:");
        indexHeader = resources.getProperty("dss.report.option.index.header", "INDEX TO APPEND TO GENERATED REPORT LOCATION:");
        indexText = resources.getProperty("dss.report.option.index.text", "INDEX VALUE");
        indexTextLen = resources.getIntProperty("dss.report.option.index.text.length", 10);
        browserHeader = resources.getProperty("dss.report.option.browser.header", "WHEN REPORTING HAS COMPLETED:");
        browserText = resources.getProperty("dss.report.option.browser.text", "SHOW THE RESULT IN A BROWSER WINDOW");
        initGUI();
    }

    void initGUI() {
        FileChooserComponent reportDir = new FileChooserComponent(frame, repText, "dss.report.loc", true);
        add(reportDir);
        TextComponent indexValue = new TextComponent(indexText, indexTextLen, "dss.report.generate.next.index");
        add(indexValue);
        BooleanComponent browserTarget = new BooleanComponent(browserText, "dss.report.target.browser");
        add(browserTarget);
        VerticalStackerContainer content = new VerticalStackerContainer();
        BorderedContainer loc = new BorderedContainer(repHeader);
        loc.add(reportDir);
        BorderedContainer index = new BorderedContainer(indexHeader);
        index.add(indexValue);
        BorderedContainer browser = new BorderedContainer(browserHeader);
        browser.add(browserTarget);
        content.add(loc.getUIComponent());
        content.add(index.getUIComponent());
        content.add(browser.getUIComponent());
        setContent(content);
    }

    /**
     * Provide the title of the content panel.
     *
     * @return the title.
     */
    public String getTitle() {
        return resources.getProperty("dss.report.option.title", "REPORT");
    }

    /**
     * Provide the description of the content panel.
     *
     * @return the description.
     */
    public String getDescription() {
        return resources.getProperty("dss.report.option.description", "CONFIGURE REPORTING");
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();

    /** Resource manager. */
    static ResourceManager resources = Framework.getGlobalManager().getResourceManager();
}
