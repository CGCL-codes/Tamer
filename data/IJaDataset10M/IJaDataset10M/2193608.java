package de.shandschuh.jaolt.gui.settings;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.jgoodies.forms.builder.PanelBuilder;
import de.shandschuh.jaolt.core.Language;
import de.shandschuh.jaolt.core.Member;
import de.shandschuh.jaolt.core.PictureUploadService;
import de.shandschuh.jaolt.gui.FormManager;
import de.shandschuh.jaolt.gui.core.ConfigurationValuesFormManager;
import de.shandschuh.jaolt.gui.core.JHyperLink;
import de.shandschuh.jaolt.gui.core.LineWrapJLabel;
import de.shandschuh.jaolt.gui.listener.core.urllabel.URLOpenAction;

public class PictureUploadServicesFormManager extends FormManager implements ListSelectionListener {

    private Member member;

    private LineWrapJLabel descriptionJLabel;

    private JList list;

    private ConfigurationValuesFormManager configurationValuesFormManager;

    private PictureUploadService currentPictureUploadService;

    private JHyperLink helpURLJLabel;

    public PictureUploadServicesFormManager(Member member) {
        this.member = member;
        list = new JList(PictureUploadService.getInstances());
        descriptionJLabel = new LineWrapJLabel();
        list.addListSelectionListener(this);
        configurationValuesFormManager = new ConfigurationValuesFormManager();
        currentPictureUploadService = null;
        helpURLJLabel = new JHyperLink();
    }

    @Override
    protected void addPanelBuilderComponents(PanelBuilder panelBuilder) {
        panelBuilder.add(new JScrollPane(list), getCellConstraints(1, 1, 1, 5));
        panelBuilder.add(descriptionJLabel, getCellConstraints(3, 1));
        panelBuilder.add(configurationValuesFormManager.getJComponent(false), getCellConstraints(3, 3));
        panelBuilder.add(helpURLJLabel, getCellConstraints(3, 5));
    }

    @Override
    protected String getColumnLayout() {
        return "max(p;50dlu), 4dlu, fill:3dlu:grow";
    }

    @Override
    public String getName() {
        return Language.translateStatic("PICTUREUPLOADSERVICES");
    }

    @Override
    protected String getRowLayout() {
        return "p, 3dlu, fill:p:grow, 3dlu, p";
    }

    @Override
    public boolean rebuildNeeded() {
        return false;
    }

    @Override
    protected void reloadLocal(boolean rebuild) {
    }

    @Override
    protected void saveLocal() throws Exception {
        if (currentPictureUploadService != null && currentPictureUploadService.getConfigurationValues() != null) {
            member.setPictureServiceConfigurationValues(configurationValuesFormManager.getConfigurationValues(), currentPictureUploadService.getClass());
        }
    }

    @Override
    protected void validateLocal() throws Exception {
    }

    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (!listSelectionEvent.getValueIsAdjusting()) {
            try {
                saveLocal();
            } catch (Exception e) {
            }
            currentPictureUploadService = (PictureUploadService) list.getSelectedValue();
            if (currentPictureUploadService.getHelpURL() != null) {
                helpURLJLabel.setAction(new URLOpenAction(Language.translateStatic("HELP"), currentPictureUploadService.getHelpURL()));
            } else {
                helpURLJLabel.setEmpty();
            }
            descriptionJLabel.setText(currentPictureUploadService.getDescription());
            configurationValuesFormManager.setConfigurationValues(currentPictureUploadService.getConfigurationValues(), member.getPictureServiceConfigurationValues(currentPictureUploadService.getClass()));
            configurationValuesFormManager.reload();
        }
    }
}
