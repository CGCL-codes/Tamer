package com.pbxworkbench.campaign.ui.model.mock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JPanel;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import com.pbxworkbench.campaign.domain.ICallStatus;
import com.pbxworkbench.campaign.ui.model.CampaignStatus;
import com.pbxworkbench.commons.DefaultChangeable;
import com.pbxworkbench.commons.PlayerStatus;
import com.pbxworkbench.commons.SwingHelper;

public class MockCampaignStatus extends DefaultChangeable implements CampaignStatus {

    private EventList<ICallStatus> calls = new BasicEventList<ICallStatus>();

    {
        calls.add(new MockCall("Tony", ICallStatus.PENDING));
        calls.add(new MockCall("Mary", ICallStatus.PENDING));
        calls.add(new MockCall("Bill", ICallStatus.PENDING));
        calls.add(new MockCall("Joe", ICallStatus.PENDING));
    }

    public MockCampaignStatus() {
        SwingHelper.openComponent(new MockControlPanel());
    }

    public String getStatus() {
        return PlayerStatus.RUNNING;
    }

    public EventList<ICallStatus> getCalls() {
        return calls;
    }

    private class MockControlPanel extends JPanel {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public MockControlPanel() {
            final Iterator<ICallStatus> callIter = calls.iterator();
            JButton btn = new JButton("Succeed");
            btn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    MockCall mockCall = (MockCall) callIter.next();
                    mockCall.setStatus("Succeeded");
                    fireChanged();
                }
            });
            add(btn);
        }
    }
}
