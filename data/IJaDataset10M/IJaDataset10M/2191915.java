package jade.tools.sniffer;

import java.awt.event.ActionEvent;
import jade.util.leap.List;
import jade.util.leap.ArrayList;

/**
   * This class includes the method ActionPerformed that is
   * associated with the PopupMenu of the Agent in the canvas.
   * @see jade.tools.sniffer.PopSniffAgent
   * @see jade.tools.sniffer.PopNoSniffAgent
   */
public class PopShowAgent extends AbstractPopup {

    private PopupAgent popAg;

    private List noSniffAgent = new ArrayList();

    private Sniffer mySniffer;

    private MMCanvas canvAgent;

    public PopShowAgent(PopupAgent popAg, Sniffer mySniffer, MMCanvas canvAgent) {
        super("Show Only Agent");
        this.popAg = popAg;
        this.canvAgent = canvAgent;
        this.mySniffer = mySniffer;
    }

    public void actionPerformed(ActionEvent avt) {
        noSniffAgent.add(popAg.agent);
        canvAgent.repaintNoSniffedAgent(popAg.agent);
        mySniffer.sniffMsg(noSniffAgent, Sniffer.SNIFF_OFF);
        noSniffAgent.clear();
    }
}
