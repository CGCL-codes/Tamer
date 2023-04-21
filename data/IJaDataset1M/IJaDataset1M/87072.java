package se.kth.cid.imsevimse.panels;

import se.kth.cid.metadata.*;
import se.kth.cid.imsevimse.components.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class DublinCorePanel extends JScrollPane {

    MetaData metaData;

    MetaDataPanel metaDataPanel;

    LangStringList general_title;

    LanguageList general_language;

    LangStringTypeList general_description;

    LangStringTypeList general_coverage;

    ContributionList lifecycle_contribute;

    LangStringList technical_format;

    LangStringList educational_learningresourcetype;

    RightsPanel rights;

    RelationList relation;

    ClassificationList classification;

    public DublinCorePanel(MetaData md) {
        this.metaData = md;
        metaDataPanel = new MetaDataPanel();
        setViewportView(metaDataPanel);
        makePanelLater();
    }

    public void detach() {
        detachFields();
    }

    void detachFields() {
        general_title.detach();
        general_language.detach();
        general_description.detach();
        general_coverage.detach();
        lifecycle_contribute.detach();
        technical_format.detach();
        educational_learningresourcetype.detach();
        relation.detach();
        rights.detach();
        classification.detach();
    }

    void updateMetaData(String target) {
        detachFields();
        metaDataPanel.removeAll();
        makePanelLater();
    }

    void makePanelLater() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                makePanel();
            }
        });
    }

    void makePanel() {
        general_title = new LangStringList(metaData.get_general_title(), true, false, null, "general_title");
        if (metaData.get_general_title() != null) metaDataPanel.addPanel("Title", general_title);
        general_language = new LanguageList(metaData.get_general_language(), false, null, "general_language");
        if (metaData.get_general_language() != null) metaDataPanel.addPanel("Language", general_language);
        general_description = new LangStringTypeList(metaData.get_general_description(), true, false, null, "general_description");
        if (metaData.get_general_description() != null) metaDataPanel.addPanel("Description", general_description);
        general_coverage = new LangStringTypeList(metaData.get_general_coverage(), false, false, null, "general_coverage");
        if (metaData.get_general_coverage() != null) metaDataPanel.addPanel("Coverage", general_coverage);
        lifecycle_contribute = new ContributionList(metaData.get_lifecycle_contribute(), false, null, "lifecycle_contribute");
        if (metaData.get_lifecycle_contribute() != null) metaDataPanel.addPanel("Contribution", lifecycle_contribute);
        technical_format = new LangStringList(metaData.get_technical_format(), false, false, null, "technical_format");
        if (metaData.get_technical_format() != null) metaDataPanel.addPanel("Format", technical_format);
        educational_learningresourcetype = new LangStringList(metaData.get_educational_learningresourcetype(), false, false, null, "educational_learningresourcetype");
        if (metaData.get_educational_learningresourcetype() != null) metaDataPanel.addPanel("Learning Resource Type", educational_learningresourcetype);
        rights = new RightsPanel(metaData.get_rights(), false, null, "rights");
        if (metaData.get_rights() != null) metaDataPanel.addPanel("Rights", rights);
        relation = new RelationList(metaData.get_relation(), false, null, "relation");
        if (metaData.get_relation() != null) metaDataPanel.addPanel("Relation", relation);
        classification = new ClassificationList(metaData.get_classification(), false, null, "classification");
        if (metaData.get_classification() != null) metaDataPanel.addPanel("Classification", classification);
    }
}
