package edu.upmc.opi.caBIG.caTIES.client.vr.order;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import edu.upmc.opi.caBIG.caTIES.client.config.ClientConfiguration;
import edu.upmc.opi.caBIG.caTIES.client.dispatcher.emailer.CaTIES_EmailerClient;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_Desktop;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.views.CaTIES_ButtonTaskImpl;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.OrganizationImpl;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.UserImpl;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_MiddleTierImpl;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_OrderSetImpl;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_UserImpl;

public class ConfirmOrderTask extends CaTIES_ButtonTaskImpl {

    ConfirmOrderTask self;

    public ConfirmOrderTask(final CaTIES_OrderSetImpl order) {
        super();
        self = this;
        setButtonText("Confirm");
        addButtonActionListener(new ActionListener() {

            private boolean sendEmail(CaTIES_OrderSetImpl order) throws Exception {
                CaTIES_UserImpl user = CaTIES_Desktop.getCurrentUser();
                OrganizationImpl org = user.getCurrentUserOrganization().obj.getOrganization();
                List<CaTIES_UserImpl> hbList = CaTIES_MiddleTierImpl.getHonestBrokersOnProtocol(user.getCurrentDistributionProtocolAssignment().obj.getDistributionProtocolOrganization().getDistributionProtocol());
                List<UserImpl> recipients = CaTIES_MiddleTierImpl.unwrapUserObj(hbList);
                CaTIES_EmailerClient emailer = CaTIES_Desktop.getEmailerClient();
                return emailer.sendOrderStatusEmail(recipients, order.obj, org);
            }

            public void actionPerformed(ActionEvent arg0) {
                int confirm = JOptionPane.showConfirmDialog(CaTIES_Desktop.getInstance(), "Are you sure you want to confirm the order? This step cannot be reverted.", "Confirm Order", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
                setButtonEnabled(false);
                setButtonText("Confirming");
                order.obj.setStatus(CaTIES_OrderSetImpl.STATUS_CONFIRMED);
                CaTIES_MiddleTierImpl mt = CaTIES_MiddleTierImpl.getInstance();
                mt.saveOrUpdateObject(order.obj);
                if (mt.commitTransaction()) {
                    boolean useEmail = ClientConfiguration.isUsingEmailer();
                    boolean status = false;
                    if (useEmail) try {
                        status = sendEmail(order);
                    } catch (Exception e) {
                        e.printStackTrace();
                        status = false;
                    }
                    if (status) {
                        JOptionPane.showMessageDialog(CaTIES_Desktop.getInstance(), "Your Order was confirmed for distribution.\n\n" + "A notification email was sent to the Honest Broker(s). You were CC'd on the email.", "Order Confirmed, Email Sent.", JOptionPane.INFORMATION_MESSAGE);
                    } else JOptionPane.showMessageDialog(CaTIES_Desktop.getInstance(), "Your Order was confirmed for distribution. \n\n" + "There was an error sending a notification email to the Honest Broker(s).", "Order Confirmed, Email Failed.", JOptionPane.ERROR_MESSAGE);
                    setButtonText("Order Confirmed");
                } else {
                    JOptionPane.showMessageDialog(CaTIES_Desktop.getInstance(), "There was an error while confirming your order", "Order Confirmation Failed", JOptionPane.ERROR_MESSAGE);
                    setButtonEnabled(true);
                    setButtonText("Confirm");
                }
            }
        });
    }
}
