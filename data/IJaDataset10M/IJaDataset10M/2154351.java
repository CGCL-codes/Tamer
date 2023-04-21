package be.oniryx.lean.session;

import be.oniryx.lean.entity.ItemType;
import be.oniryx.lean.entity.Lane;
import be.oniryx.lean.entity.Project;
import javax.ejb.Stateful;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;

/**
 * User: cedric
 * Date: May 4, 2009
 */
@Name("itemTypeManager")
@Stateful
@Scope(ScopeType.CONVERSATION)
public class ItemTypeManagerAction extends AbstractManager<ItemType> implements ItemTypeManager {

    @In
    private Project project;

    @In(create = true)
    private Factories factories;

    private Long currentLaneId;

    protected ItemType newInstance() {
        ItemType it = new ItemType();
        it.setLane(factories.getLane());
        return it;
    }

    public ItemType getSelected() {
        return factories.getItemTypeSelected();
    }

    public String getManagedEntityName() {
        return "ItemType";
    }

    private void select(ItemType it) {
        currentLaneId = it.getLane().getId();
    }

    private void update(ItemType it) {
        it.setLane(em.find(Lane.class, currentLaneId));
    }

    @Override
    public void add() {
        super.add();
        select(current);
    }

    @Override
    public void create() {
        update(current);
        super.create();
    }

    @Override
    public void edit() {
        super.edit();
        select(current);
    }

    @Override
    public void save() {
        update(current);
        super.save();
    }

    public Long getCurrentLaneId() {
        return currentLaneId;
    }

    public void setCurrentLaneId(Long currentLaneId) {
        this.currentLaneId = currentLaneId;
    }
}
