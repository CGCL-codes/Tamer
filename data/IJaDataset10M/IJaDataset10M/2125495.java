package org.modss.facilitator.port.ui.option.comp;

import org.modss.facilitator.port.ui.option.*;
import org.modss.facilitator.shared.singleton.*;
import org.modss.facilitator.shared.resource.*;
import org.swzoo.log2.core.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Report content panel.
 */
public class ReportResultComponent extends DefaultComponent {

    String headerText;

    String incRankingText;

    String incBarText;

    String incGraphText;

    String incRawText;

    String rawTypeHeaderText;

    String rawCommaText;

    String rawTabText;

    public ReportResultComponent() {
        headerText = resources.getProperty("dss.report.result.option.type", "INCLUDE THE FOLLOWING:");
        incRankingText = resources.getProperty("dss.report.result.option.type.ranking", "RANKING");
        incBarText = resources.getProperty("dss.report.result.option.type.bar", "BAR GRAPH");
        incGraphText = resources.getProperty("dss.report.result.option.type.polar", "POLAR GRAPH");
        incRawText = resources.getProperty("dss.report.result.option.type.raw", "RAW DATA");
        rawTypeHeaderText = resources.getProperty("dss.report.result.option.type.raw.border", "Export RAW DATA as type:");
        rawCommaText = resources.getProperty("dss.report.result.option.type.raw.csv", "COMMA SEPARATED (CSV)");
        rawTabText = resources.getProperty("dss.report.result.option.type.raw.tab", "TAB SEPARATED (TAB)");
        initGUI();
    }

    void initGUI() {
        BooleanComponent ranking = new BooleanComponent(incRankingText, "dss.report.result.ranking");
        BooleanComponent bar = new BooleanComponent(incBarText, "dss.report.result.bar");
        BooleanComponent polar = new BooleanComponent(incGraphText, "dss.report.result.polar");
        final BooleanComponent raw = new BooleanComponent(incRawText, "dss.report.result.raw");
        add(ranking);
        add(bar);
        add(polar);
        add(raw);
        BorderedContainer s1 = new BorderedContainer(headerText);
        s1.add(ranking);
        s1.add(bar);
        s1.add(polar);
        s1.add(raw);
        BooleanComponent comma = new BooleanComponent(rawCommaText, "dss.report.result.raw.csv");
        BooleanComponent tab = new BooleanComponent(rawTabText, "dss.report.result.raw.tab");
        add(comma);
        add(tab);
        final ButtonGroup group = new ButtonGroup();
        group.add(comma);
        group.add(tab);
        BorderedContainer s2 = new BorderedContainer(rawTypeHeaderText);
        s2.add(comma);
        s2.add(tab);
        raw.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent ev) {
                bindThem(raw, group);
            }
        });
        bindThem(raw, group);
        VerticalStackerContainer content = new VerticalStackerContainer();
        content.add(s1.getUIComponent());
        content.add(s2.getUIComponent());
        setContent(content);
    }

    void bindThem(AbstractButton raw, ButtonGroup group) {
        Enumeration e = group.getElements();
        while (e.hasMoreElements()) {
            AbstractButton b = (AbstractButton) e.nextElement();
            b.setEnabled(raw.isSelected());
        }
    }

    /**
     * Provide the title of the content panel.
     *
     * @return the title.
     */
    public String getTitle() {
        return resources.getProperty("dss.report.result.option.title", "RESULT (REPORT)");
    }

    /**
     * Provide the description of the content panel.
     *
     * @return the description.
     */
    public String getDescription() {
        return resources.getProperty("dss.report.result.option.description", "SPECIFY BEHAVIOUR OF RESULT REPORTING");
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();

    /** Resource provider. */
    private static final ResourceProvider resources = Singleton.Factory.getInstance().getResourceProvider();
}
