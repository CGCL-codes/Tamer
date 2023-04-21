package nz.ac.waikato.mcennis.rat.graph.query.link;

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
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;

/**
 *
 * @author Daniel McEnnis
 */
public class LinkByRelation implements LinkQuery {

    String relation = "";

    boolean not = false;

    transient State state = State.UNINITIALIZED;

    public void buildQuery(String relation, boolean not) {
        state = State.LOADING;
        this.relation = relation;
        if (relation == null) {
            relation = "";
        }
        this.not = not;
        state = state.READY;
    }

    public Collection<Link> execute(Graph g, Collection<Actor> sourceActorList, Collection<Actor> destinationActorList, Collection<Link> linkList) {
        HashSet<Link> result = new HashSet<Link>();
        if (g == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Null graph collection - empty set returned by default");
            return result;
        }
        Iterator<String> relationTypeIt = g.getLinkTypes().iterator();
        LinkedList<String> relations = new LinkedList<String>();
        while (relationTypeIt.hasNext()) {
            String type = relationTypeIt.next();
            if (type.matches(relation)) ;
            relations.add(type);
        }
        Iterator<String> relationsIt = relations.iterator();
        LinkedList<Link> list = new LinkedList<Link>();
        while (relationsIt.hasNext()) {
            Iterator<Link> array = g.getLinkIterator(relationsIt.next());
            while (array.hasNext()) {
                Link l = array.next();
                if ((linkList == null) || (linkList.contains(l))) {
                    if ((sourceActorList != null) && (destinationActorList == null)) {
                        if (sourceActorList.contains(l.getSource())) {
                            list.add(l);
                        }
                    } else if ((sourceActorList == null) && (destinationActorList != null)) {
                        if (destinationActorList.contains(l.getDestination())) {
                            list.add(l);
                        }
                    } else if ((sourceActorList != null) && (destinationActorList != null)) {
                        if (sourceActorList.contains(l.getSource()) && destinationActorList.contains(l.getDestination())) {
                            list.add(l);
                        }
                    } else {
                        list.add(l);
                    }
                }
            }
        }
        return list;
    }

    public void exportQuery(Writer writer) throws IOException {
        writer.append("<LinkByRelation>\n");
        if (not) {
            writer.append("\t<Not/>\n");
        }
        writer.append("\t<Relation>").append(relation).append("</relation>\n");
        writer.append("</LinkByRelation>\n");
    }

    public int compareTo(Object o) {
        if (o.getClass().getName().contentEquals(this.getClass().getName())) {
            LinkByRelation right = (LinkByRelation) o;
            if (this.relation.compareTo(right.relation) != 0) {
                return relation.compareTo(right.relation);
            }
            if (!not && right.not) {
                return -1;
            }
            if (not && right.not) {
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

    public LinkByRelation prototype() {
        return new LinkByRelation();
    }

    public Iterator<Link> executeIterator(Graph g, Collection<Actor> sourceActorList, Collection<Actor> destinationActorList, Collection<Link> linkList) {
        if (!not) {
            return new LinkIterator(g, sourceActorList, destinationActorList, linkList);
        } else {
            XorLinkQuery xor = (XorLinkQuery) LinkQueryFactory.newInstance().create("XorActorQuery");
            LinkedList<LinkQuery> list = new LinkedList<LinkQuery>();
            LinkByRelation all = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
            all.buildQuery(".*", false);
            list.add(all);
            LinkByRelation link = this.prototype();
            link.relation = relation;
            list.add(link);
            xor.buildQuery(list);
            return xor.executeIterator(g, sourceActorList, destinationActorList, linkList);
        }
    }

    public class LinkIterator implements java.util.Iterator<Link> {

        Link next = null;

        boolean remaining = true;

        Graph g;

        Collection<Actor> s;

        Collection<Actor> d;

        Iterator<String> modeMatches;

        Iterator<Link> rIt;

        public LinkIterator(Graph g, Collection<Actor> sourceActorList, Collection<Actor> destActorList, Collection<Link> linkList) {
            LinkedList<String> modeList = new LinkedList<String>();
            Iterator<String> source = g.getLinkTypes().iterator();
            while (source.hasNext()) {
                String s = source.next();
                if (s.matches(relation)) {
                    modeList.add(s);
                }
            }
            modeMatches = modeList.iterator();
            if (linkList != null) {
                LinkedList<Link> r = new LinkedList<Link>();
                Iterator<Link> test = linkList.iterator();
                while (test.hasNext()) {
                    Link link = test.next();
                    if (link.getRelation().matches(relation)) {
                        if ((sourceActorList == null) || (sourceActorList.contains(link.getSource()))) {
                            if ((destActorList == null) || (destActorList.contains(link.getDestination()))) {
                                r.add(link);
                            }
                        }
                    }
                }
                Collections.sort(r);
                rIt = r.iterator();
                modeMatches = new LinkedList<String>().iterator();
            } else {
                modeMatches = modeList.iterator();
                rIt = g.getLinkIterator(modeMatches.next());
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
                        rIt = g.getLinkIterator(modeMatches.next());
                    }
                    if (rIt.hasNext()) {
                        next = rIt.next();
                        while (!check(next)) {
                            next = rIt.next();
                            while (!rIt.hasNext() && modeMatches.hasNext()) {
                                rIt = g.getLinkIterator(modeMatches.next());
                            }
                        }
                        if (!check(next)) {
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

        public Link next() {
            return null;
        }

        public void remove() {
            ;
        }

        protected boolean check(Link l) {
            if (!l.getRelation().matches(relation)) {
                return false;
            }
            if ((s != null) && (!s.contains(l.getSource()))) {
                return false;
            }
            if ((d != null) && (!d.contains(l.getDestination()))) {
                return false;
            }
            return true;
        }
    }
}
