package pl.edu.agh.ssm.web.client.component;

import pl.edu.agh.ssm.web.client.rpc.SSMServerConnectorAsync;
import pl.edu.agh.ssm.web.client.rpc.wsComponent.BeanOperationInfo;
import pl.edu.agh.ssm.web.client.rpc.wsComponent.MBeanInfo;
import pl.edu.agh.ssm.web.client.utils.ValueField;
import pl.edu.agh.ssm.web.service.SSMServerConnectorImpl;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.tips.ToolTip;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;

public class BeanOperationInvokerDialog extends Dialog implements AsyncCallback, Listener<ButtonEvent> {

    private ValueField[] tf;

    private final MBeanInfo mBeanInfo;

    private BeanOperationInfo beanOperationInfo;

    public BeanOperationInvokerDialog(MBeanInfo mBeanInfo, BeanOperationInfo beanOperationInfo) {
        setModal(true);
        setHeading("Invoke method");
        this.mBeanInfo = mBeanInfo;
        this.beanOperationInfo = beanOperationInfo;
        FlexTable table = new FlexTable();
        add(table);
        FlexTable tableOperation = new FlexTable();
        tf = new ValueField[beanOperationInfo.getWsBeanParameterInfoList().size()];
        for (int i = 0; i < tf.length; i++) {
            String type = beanOperationInfo.getWsBeanParameterInfoList().get(i).getType();
            String name = beanOperationInfo.getWsBeanParameterInfoList().get(i).getName();
            LabelField l3 = new LabelField(name);
            tableOperation.setWidget(i, 0, l3);
            LabelField l2 = new LabelField("(" + type.substring(type.lastIndexOf('.') + 1) + ")");
            new ToolTip(l2, new ToolTipConfig("Type", type));
            tableOperation.setWidget(i, 1, l2);
            tf[i] = new ValueField(type);
            tableOperation.setWidget(i, 2, tf[i]);
        }
        table.setWidget(0, 0, tableOperation);
        setButtons(Dialog.OKCANCEL);
        getButtonById("ok").addListener(Events.OnClick, this);
        getButtonById("cancel").addListener(Events.OnClick, new Listener<ButtonEvent>() {

            public void handleEvent(ButtonEvent be) {
                BeanOperationInvokerDialog.this.close();
            }
        });
    }

    public void onFailure(Throwable arg0) {
        System.out.println(arg0);
    }

    public void onSuccess(Object arg0) {
        Dialog d = new Dialog();
        d.setHeading("Operation success");
        if (arg0 != null) d.addText("Operation returned: " + (String) arg0); else d.addText("Operation success");
        d.setBodyStyle("fontWeight:bold;padding:13px;");
        d.setSize(300, 100);
        d.setHideOnButtonClick(true);
        d.setButtons(Dialog.OK);
        d.show();
    }

    public void handleEvent(ButtonEvent be) {
        SSMServerConnectorAsync service = SSMServerConnectorImpl.App.getInstance();
        String[] parameters = new String[tf.length];
        for (int i = 0; i < parameters.length; i++) {
            if (tf[i].isValid()) parameters[i] = tf[i].getValue(); else {
                Window.alert("some fields isnt correct");
                return;
            }
        }
        service.invokeMBeanOperation(mBeanInfo, beanOperationInfo, parameters, this);
    }
}
