package de.unikoblenz.isweb.xcosima.communication;

import java.util.GregorianCalendar;
import javax.xml.datatype.XMLGregorianCalendar;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import de.unikoblenz.isweb.xcosima.aprocessor.OntologyClass;
import de.unikoblenz.isweb.xcosima.dns.ConversationStart;
import de.unikoblenz.isweb.xcosima.ontologies.ONT;
import de.unikoblenz.isweb.xcosima.task.Action;

@OntologyClass(ONT.ComModule_C_Conversation)
public class Conversation extends Action {

    public Conversation() {
        super();
    }

    public void addStartTime(GregorianCalendar gc) {
        XMLGregorianCalendar xcal = new XMLGregorianCalendarImpl(gc);
        addQuality(new ConversationStart(valueFactory.createLiteral(xcal)));
    }
}
