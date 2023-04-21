package org.mobicents.slee.container.management.console.client.deployableunits;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 * 
 */
public class DeployableUnitInstallPanel extends FormPanel {

    private String extractMessage(String result) {
        String startPreTag = "<pre>";
        String endPreTag = "</pre>";
        result = result.trim();
        if (result.startsWith(startPreTag) && result.endsWith(endPreTag)) {
            result = result.substring(startPreTag.length(), result.length() - endPreTag.length());
        }
        return result;
    }

    public DeployableUnitInstallPanel(BrowseContainer browseContainer) {
        super();
        setAction("DeployableUnitsInstallService");
        setEncoding(FormPanel.ENCODING_MULTIPART);
        setMethod(FormPanel.METHOD_POST);
        ControlContainer panel = new ControlContainer();
        panel.setWidth("");
        setWidget(panel);
        Label label = new Label("Package file:");
        final FileUpload fileUpload = new FileUpload();
        fileUpload.setName("uploadFormElement");
        final Button submit = new Button("Install", new ClickListener() {

            public void onClick(Widget sender) {
                submit();
            }
        });
        addFormHandler(new FormHandler() {

            public void onSubmitComplete(final FormSubmitCompleteEvent event) {
                String result = event.getResults();
                if (result == null || result.length() == 0) {
                    return;
                }
                result = extractMessage(result);
                if (result.indexOf(DeployableUnitsService.SUCCESS_CODE) != -1) {
                    Logger.info("Deployable unit installed");
                } else {
                    Logger.error(result);
                }
            }

            public void onSubmit(FormSubmitEvent event) {
                if (fileUpload.getFilename().trim().length() == 0) {
                    Logger.error("Please specify a package file");
                    event.setCancelled(true);
                }
            }
        });
        panel.setWidget(0, 0, label);
        panel.setWidget(0, 1, fileUpload);
        panel.setWidget(0, 2, submit);
        browseContainer.add("Install a deployable unit", this);
    }
}
