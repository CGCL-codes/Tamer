package it.eng.bxmodeller.activity.event;

import it.eng.bxmodeller.Description;
import it.eng.bxmodeller.application.ApplicationType;
import it.eng.bxmodeller.graphic.NodeGraphicsInfo;
import java.util.Iterator;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;

public class TriggerMultiple {

    private TriggerResultMessage triggerResultMessage = null;

    private TriggerTimer triggerTimer = null;

    private TriggerRule triggerRule = null;

    private TriggerResultLink triggerResultLink = null;

    public void generateClass(Element elementoAppoggio) {
        List elencoFigli = elementoAppoggio.getChildren();
        Iterator iteratore = elencoFigli.iterator();
        Element elementoAppoggioFigli = null;
        String nomeTagAppoggio = "";
        while (iteratore.hasNext()) {
            elementoAppoggioFigli = (Element) iteratore.next();
            nomeTagAppoggio = elementoAppoggioFigli.getName();
            if (nomeTagAppoggio.equals("TriggerResultMessage")) {
                TriggerResultMessage tmp = new TriggerResultMessage();
                tmp.generateClass(elementoAppoggioFigli);
                this.setTriggerResultMessage(tmp);
            } else if (nomeTagAppoggio.equals("TriggerTimer")) {
                TriggerTimer tmp = new TriggerTimer();
                tmp.generateClass(elementoAppoggioFigli);
                this.setTriggerTimer(tmp);
            } else if (nomeTagAppoggio.equals("TriggerRule")) {
                TriggerRule tmp = new TriggerRule();
                tmp.generateClass(elementoAppoggioFigli);
                this.setTriggerRule(tmp);
            } else if (nomeTagAppoggio.equals("TriggerResultLink")) {
                TriggerResultLink tmp = new TriggerResultLink();
                tmp.generateClass(elementoAppoggioFigli);
                this.setTriggerResultLink(tmp);
            }
        }
    }

    public TriggerResultLink getTriggerResultLink() {
        return triggerResultLink;
    }

    public void setTriggerResultLink(TriggerResultLink triggerResultLink) {
        this.triggerResultLink = triggerResultLink;
    }

    public TriggerResultMessage getTriggerResultMessage() {
        return triggerResultMessage;
    }

    public void setTriggerResultMessage(TriggerResultMessage triggerResultMessage) {
        this.triggerResultMessage = triggerResultMessage;
    }

    public TriggerRule getTriggerRule() {
        return triggerRule;
    }

    public void setTriggerRule(TriggerRule triggerRule) {
        this.triggerRule = triggerRule;
    }

    public TriggerTimer getTriggerTimer() {
        return triggerTimer;
    }

    public void setTriggerTimer(TriggerTimer triggerTimer) {
        this.triggerTimer = triggerTimer;
    }

    public Element generateXPDL() {
        Element item = new Element("TriggerMultiple");
        if (triggerResultMessage != null) item.addContent(triggerResultMessage.generateXPDL());
        if (triggerTimer != null) item.addContent(triggerTimer.generateXPDL());
        if (triggerRule != null) item.addContent(triggerRule.generateXPDL());
        if (triggerResultLink != null) item.addContent(triggerResultLink.generateXPDL());
        return item;
    }
}
