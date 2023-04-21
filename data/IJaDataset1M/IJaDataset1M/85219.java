package org.esprit.ocm.client.ui.ec2;

import org.esprit.ocm.client.rpc.ec2.RulesService;
import org.esprit.ocm.client.rpc.ec2.RulesServiceAsync;
import org.esprit.ocm.dto.impl.AwsCredentialsDto;
import org.esprit.ocm.dto.impl.ServerDto;
import org.esprit.ocm.dto.impl.UserDto;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class AddGroupPermissions extends Window {

    private final RulesServiceAsync rulesService = GWT.create(RulesService.class);

    public AddGroupPermissions(final ServerDto server, final AwsCredentialsDto credentials, final UserDto user, final String groupName) {
        setTitle("Add Group");
        setWidth(250);
        setHeight(350);
        setIsModal(true);
        setShowModalMask(true);
        setShowCloseButton(true);
        centerInPage();
        final DynamicForm formGroup = new DynamicForm();
        formGroup.setWidth(200);
        formGroup.setHeight(300);
        formGroup.setPadding(5);
        formGroup.setLayoutAlign(VerticalAlignment.TOP);
        final TextItem cidr = new TextItem("cidr");
        cidr.setTitle("CIDR");
        cidr.setRequired(true);
        final ComboBoxItem protocolCb = new ComboBoxItem();
        protocolCb.setTitle("Protocol");
        protocolCb.setType("comboBox");
        protocolCb.setValueMap("tcp", "udp", "icmp");
        final TextItem fromPort = new TextItem("fromPort");
        fromPort.setTitle("From Port");
        fromPort.setRequired(true);
        final TextItem toPort = new TextItem("toPort");
        toPort.setTitle("To Port");
        toPort.setRequired(true);
        ButtonItem okButton = new ButtonItem("ok", "OK");
        okButton.setAlign(Alignment.CENTER);
        okButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                rulesService.addPermission(groupName, protocolCb.getValue().toString(), Integer.parseInt(fromPort.getValue().toString()), Integer.parseInt(toPort.getValue().toString()), cidr.getValue().toString(), server, credentials, new AsyncCallback<Boolean>() {

                    public void onSuccess(Boolean arg0) {
                        destroy();
                        SC.say("Permission added");
                    }

                    public void onFailure(Throwable arg0) {
                    }
                });
            }
        });
        formGroup.setFields(cidr, protocolCb, fromPort, toPort, okButton);
        formGroup.redraw();
        addItem(formGroup);
        draw();
    }
}
