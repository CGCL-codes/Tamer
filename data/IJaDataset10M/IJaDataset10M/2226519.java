package org.wikiup.romulan.gom.imp.rc;

import java.sql.Timestamp;
import org.wikiup.core.inf.Document;
import org.wikiup.core.inf.DocumentAware;
import org.wikiup.core.inf.Getter;
import org.wikiup.core.util.ContextUtil;
import org.wikiup.core.util.Documents;
import org.wikiup.database.orm.Entity;
import org.wikiup.database.orm.WikiupEntityManager;
import org.wikiup.romulan.gom.Model;
import org.wikiup.romulan.gom.Player;

public class EntityResourceContainer extends ResourceContainer implements DocumentAware, Getter<Object> {

    private Document configure;

    private Model model;

    private String id;

    private Entity selectedEntity;

    private Timestamp lastModified;

    public EntityResourceContainer(Player player, Model model) {
        this.model = model;
    }

    public String getId() {
        return id;
    }

    public long getLastModified() {
        return lastModified.getTime() / 1000;
    }

    public void update() {
        getResourceEntity().update();
    }

    public void select(boolean force) {
        if (selectedEntity == null || force) (selectedEntity = getResourceEntity()).select();
    }

    public void select() {
        select(false);
    }

    private Entity getResourceEntity() {
        Entity entity = WikiupEntityManager.getInstance().getEntity(Documents.getDocumentValue(configure, "entity-name"));
        entity.bind(this);
        ContextUtil.setProperties(configure, this, model);
        return entity;
    }

    @Override
    public void aware(Document desc) {
        configure = desc;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }

    public Object get(String name) {
        select();
        return ContextUtil.getBeanProperty(this, name);
    }
}
