package collab.fm.server.bean.persist.relation;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import collab.fm.server.bean.persist.Element;
import collab.fm.server.bean.persist.Model;
import collab.fm.server.util.DaoUtil;
import collab.fm.server.util.exception.ItemPersistenceException;
import collab.fm.server.util.exception.StaleDataException;

public abstract class Relation extends Element {

    private static Logger logger = Logger.getLogger(Relation.class);

    protected Model model;

    protected Set<Element> elements = new HashSet<Element>();

    public String toValueString() {
        if (this.getId() != null) {
            return this.getId().toString();
        }
        return this.elements.toString();
    }

    @Override
    public int vote(boolean yes, Long userId) {
        if (yes) {
            for (Element f : this.getElements()) {
                f.vote(true, userId);
                try {
                    DaoUtil.getElementDao().save(f);
                } catch (ItemPersistenceException e) {
                    logger.warn("Vote on entity failed.", e);
                } catch (StaleDataException e) {
                    logger.warn("Vote on entity failed.", e);
                }
            }
        }
        return super.vote(yes, userId);
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Set<Element> getElements() {
        return elements;
    }

    public void setElements(Set<Element> elements) {
        this.elements = elements;
    }
}
