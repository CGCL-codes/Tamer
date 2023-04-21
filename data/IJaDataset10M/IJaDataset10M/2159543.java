package com.inetmon.jn.healthmonitor.listener;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;
import com.inetmon.jn.healthmonitor.extraViews.AddServiceTab;
import com.inetmon.jn.healthmonitor.extraViews.AddServiceTypeTab;
import com.inetmon.jn.healthmonitor.extraViews.DeleteServiceTab;
import com.inetmon.jn.healthmonitor.extraViews.ServiceMenuPage;
import com.inetmon.jn.healthmonitor.extraViews.ServiceTypeMenuPage;

public class TextModifyListener implements ModifyListener {

    private Object tab;

    public TextModifyListener(Object tab) {
        this.tab = tab;
    }

    public void modifyText(ModifyEvent e) {
        if (ServiceMenuPage.getDefault() != null && tab instanceof AddServiceTab && ServiceMenuPage.getDefault().getButton() != null) {
            ((AddServiceTab) tab).storeCompomentValue((Text) e.getSource());
            String ipTemplate = "000.000.000.000";
            if (!(((AddServiceTab) tab).getNetbios().equals("")) && !(((AddServiceTab) tab).getPort().equals("")) && !(((AddServiceTab) tab).getServiceName().equals("")) && !(((AddServiceTab) tab).getIpv4().equals(ipTemplate))) {
                ServiceMenuPage.getDefault().getButton().setEnabled(true);
            } else {
                ServiceMenuPage.getDefault().getButton().setEnabled(false);
            }
            return;
        }
        if (ServiceMenuPage.getDefault() != null && tab instanceof DeleteServiceTab) {
            return;
        }
        if (ServiceTypeMenuPage.getDefault() != null && tab instanceof AddServiceTypeTab) {
            if (ServiceTypeMenuPage.getDefault().getAddServiceTab().getServiceNameText().getText().equals("")) {
                ServiceTypeMenuPage.getDefault().getButton().setEnabled(false);
                ServiceTypeMenuPage.getDefault().getAddServiceTab().getAddedServiceLabel().setText("               ");
                return;
            } else {
                ServiceTypeMenuPage.getDefault().getAddServiceTab().getAddedServiceLabel().setText("               ");
                ServiceTypeMenuPage.getDefault().getButton().setEnabled(true);
                return;
            }
        }
    }
}
