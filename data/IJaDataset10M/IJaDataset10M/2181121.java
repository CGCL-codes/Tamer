package tr.view.reference.screen;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node.Cookie;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.information.Information;
import tr.model.thought.Thought;
import tr.view.process.ProcessThoughtsAction;
import tr.view.reference.ReferenceDeleteCookie;
import tr.view.reference.ReferenceEditCookie;
import tr.view.reference.ReferenceReprocessCookie;

/**
 * Node for a referenced information item.
 *
 * @author <a href="mailto:jimoore@netspace.net.au">Jeremy Moore</a>
 */
public class ReferenceNode extends AbstractNode implements ReferenceEditCookie, ReferenceDeleteCookie, ReferenceReprocessCookie {

    public final Information info;

    /** Constructs a new instance. */
    public ReferenceNode(Information info) {
        super(Children.LEAF, Lookups.singleton(info));
        this.info = info;
    }

    @Override
    public String getName() {
        return (info == null) ? "" : info.getDescription();
    }

    @Override
    public Cookie getCookie(Class clazz) {
        if (clazz == ReferenceEditCookie.class) return this;
        if (clazz == ReferenceDeleteCookie.class) return this;
        if (clazz == ReferenceReprocessCookie.class) return this;
        return super.getCookie(clazz);
    }

    public void editReference() {
        if (info == null) return;
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        ReferenceTopComponent rtc = ReferenceTopComponent.findInstance();
        if (rtc == null) return;
        rtc.edit();
    }

    public void deleteReference() {
        if (info == null) return;
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        data.getInformationManager().remove(info);
    }

    public void reprocessReference() {
        if (info == null) return;
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        data.getInformationManager().remove(info);
        Thought thought = new Thought();
        thought.setDescription(info.getDescription());
        thought.setTopic(info.getTopic());
        thought.setNotes(info.getNotes());
        data.getThoughtManager().insert(thought, 0);
        try {
            Thread.sleep(400);
        } catch (InterruptedException ex) {
        }
        ProcessThoughtsAction pta = (ProcessThoughtsAction) SystemAction.get(ProcessThoughtsAction.class);
        pta.performAction();
    }
}
