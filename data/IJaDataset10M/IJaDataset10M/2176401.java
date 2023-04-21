package nz.ac.waikato.mcennis.rat.graph.query.actor;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;

/**
 *
 * @author Daniel McEnnis
 */
public class ActorByMode implements ActorQuery {

    String mode = ".*";

    String id = ".*";

    boolean not = false;

    transient State state = State.UNINITIALIZED;

    public Collection<Actor> execute(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        HashSet<Actor> result = new HashSet<Actor>();
        if (g == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Null graph collection - empty set returned by default");
            return result;
        }
        LinkedList<Actor> actor = new LinkedList<Actor>();
        Iterator<String> modes = g.getActorTypes().iterator();
        while (modes.hasNext()) {
            String modeName = modes.next();
            if (!not && modeName.matches(mode)) {
                actor.addAll(g.getActor(modeName));
            } else if (not && !modeName.matches(mode)) {
                actor.addAll(g.getActor(modeName));
            }
        }
        if (actorList != null) {
            actor.retainAll(actorList);
        }
        Iterator<Actor> it_actor = actor.iterator();
        while (it_actor.hasNext()) {
            Actor a = it_actor.next();
            if (a.getID().matches(id)) {
                if (!not) {
                    result.add(a);
                }
            } else if (not) {
                result.add(a);
            }
        }
        return result;
    }

    public void buildQuery(String mode, String id, boolean not) {
        state = State.LOADING;
        this.mode = mode;
        if (mode == null) {
            this.mode = "";
        }
        this.id = id;
        if (id == null) {
            this.id = "";
        }
        this.not = not;
        state = State.READY;
    }

    public void exportQuery(Writer writer) throws IOException {
        writer.append("<ActorByMode>\n");
        if (not) {
            writer.append("<Not/>");
        }
        writer.append("<Mode>").append(mode).append("</Mode>\n");
        writer.append("</ActorByMode>");
    }

    public int compareTo(Object o) {
        if (o.getClass().getName().contentEquals(this.getClass().getName())) {
            ActorByMode right = (ActorByMode) o;
            if (mode.compareTo(right.mode) != 0) {
                return mode.compareTo(right.mode);
            }
            if (not && !right.not) {
                return -1;
            }
            if (!not && right.not) {
                return 1;
            }
            return 0;
        } else {
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }

    public State buildingStatus() {
        return state;
    }

    public ActorByMode prototype() {
        return new ActorByMode();
    }

    public Iterator<Actor> executeIterator(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        if (!not) {
            return new ActorIterator(g, actorList, linkList);
        } else {
            XorActorQuery xor = (XorActorQuery) ActorQueryFactory.newInstance().create("XorActorQuery");
            LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
            ActorByMode all = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
            all.buildQuery(".*", ".*", false);
            list.add(all);
            ActorByMode link = this.prototype();
            link.id = id;
            link.mode = mode;
            list.add(link);
            xor.buildQuery(list);
            return xor.executeIterator(g, actorList, linkList);
        }
    }

    public class ActorIterator implements java.util.Iterator<Actor> {

        Actor next = null;

        boolean remaining = true;

        Graph g;

        Iterator<String> modeMatches;

        Iterator<Actor> rIt;

        public ActorIterator(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
            LinkedList<String> modeList = new LinkedList<String>();
            Iterator<String> source = g.getActorTypes().iterator();
            while (source.hasNext()) {
                String s = source.next();
                if (s.matches(mode)) {
                    modeList.add(s);
                }
            }
            modeMatches = modeList.iterator();
            if (actorList != null) {
                LinkedList<Actor> r = new LinkedList<Actor>();
                r.addAll(actorList);
                Collections.sort(r);
                rIt = r.iterator();
                modeMatches = new LinkedList<String>().iterator();
            } else {
                modeMatches = modeList.iterator();
                rIt = g.getActorIterator(modeMatches.next());
            }
        }

        public boolean hasNext() {
            if (remaining) {
                if (next == null) {
                    if (!rIt.hasNext() && !modeMatches.hasNext()) {
                        remaining = false;
                        return false;
                    }
                    while (!rIt.hasNext() && modeMatches.hasNext()) {
                        rIt = g.getActorIterator(modeMatches.next());
                    }
                    if (rIt.hasNext()) {
                        next = rIt.next();
                        while ((!next.getMode().matches(mode) || !next.getID().matches(id)) && (rIt.hasNext())) {
                            next = rIt.next();
                            while (!rIt.hasNext() && modeMatches.hasNext()) {
                                rIt = g.getActorIterator(modeMatches.next());
                            }
                        }
                        if ((!next.getMode().matches(mode) || !next.getID().matches(id))) {
                            remaining = false;
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        remaining = false;
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }

        public Actor next() {
            hasNext();
            Actor ret = next;
            next = null;
            return ret;
        }

        public void remove() {
            ;
        }
    }
}
