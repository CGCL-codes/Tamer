package org.modelibra.swing.domain.model.concept.entity;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.modelibra.Entity;
import org.modelibra.IEntities;
import org.modelibra.IEntity;
import org.modelibra.config.NeighborConfig;
import org.modelibra.swing.domain.model.concept.entity.neighbor.EntityParentLookupPanel;
import org.modelibra.swing.widget.ModelibraFrame;
import org.modelibra.swing.widget.ModelibraPanel;

@SuppressWarnings("serial")
public class EntityParentLookupsPanel extends ModelibraPanel implements Observer {

    private ModelibraFrame contentFrame;

    private boolean add;

    private IEntities<?> entities;

    private IEntity<?> entity;

    private List<NeighborConfig> parentNeighborConfigList;

    public EntityParentLookupsPanel(ModelibraFrame contentFrame, boolean add, IEntities<?> entities, IEntity<?> entity, List<NeighborConfig> parentNeighborConfigList) {
        this.contentFrame = contentFrame;
        this.add = add;
        this.entities = entities;
        this.entity = entity;
        this.parentNeighborConfigList = parentNeighborConfigList;
        ((Entity<?>) entity).addObserver(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addParentLookups(add, entities, entity, parentNeighborConfigList);
    }

    protected void addParentLookups(boolean add, IEntities<?> entities, IEntity<?> entity, List<NeighborConfig> parentNeighborConfigList) {
        setLayout(new GridLayout(parentNeighborConfigList.size(), 2, 4, 4));
        for (NeighborConfig parentNeighborConfig : parentNeighborConfigList) {
            if (parentNeighborConfig.isUpdate() && parentNeighborConfig.isParent()) {
                add(new EntityParentLookupPanel(contentFrame, add, entities, entity, parentNeighborConfig));
            }
        }
    }

    public void update(Observable o, Object arg) {
        if (o == entity) {
            if (arg instanceof NeighborConfig) {
                NeighborConfig neighborConfig = (NeighborConfig) arg;
                for (Component component : getComponents()) {
                    if (component instanceof JPanel) {
                        JPanel panel = (JPanel) component;
                        for (Component subcomponent : panel.getComponents()) {
                            if (subcomponent instanceof EntityParentLookupPanel) {
                                EntityParentLookupPanel entityParentLookupPanel = (EntityParentLookupPanel) subcomponent;
                                if (entityParentLookupPanel.getParentNeighborName().equals(neighborConfig.getCode())) {
                                    removeAll();
                                    addParentLookups(add, entities, entity, parentNeighborConfigList);
                                    validate();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
