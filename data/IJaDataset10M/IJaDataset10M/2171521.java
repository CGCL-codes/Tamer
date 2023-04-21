package bma.bricks.otter.publish.publet.common;

import java.util.LinkedList;
import java.util.List;
import bma.bricks.json.JSONObject;
import bma.bricks.otter.model.publet.core.feature.IPublet;
import bma.bricks.otter.model.publet.core.feature.IPubletContainer;
import bma.bricks.otter.model.publet.core.feature.IPubletService;
import bma.bricks.otter.model.publet.core.feature.PubletContentPublishContext;
import bma.bricks.otter.model.publet.core.feature.PubletContentPublishResult;
import bma.bricks.otter.model.publet.core.feature.PubletPagePublishContext;
import bma.bricks.otter.model.publet.core.feature.PubletPagePublishResult;
import bma.bricks.otter.publish.publet.PubletUtil;

public class PagePublet implements IPublet, IPubletService, IPubletContainer {

    public static final String NAME = "page";

    private List<IPublet> panel = new LinkedList<IPublet>();

    private IPublet body;

    @Override
    public IPublet findPublet(int id) {
        for (IPublet pl : this.panel) {
            if (pl.getId() == id) {
                return pl;
            }
        }
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public boolean buildContent(PubletContentPublishContext context, PubletContentPublishResult result) {
        context.setService(this);
        if (this.body != null) {
            return this.body.buildContent(context, result);
        }
        return false;
    }

    @Override
    public boolean deserialize(JSONObject context) {
        PubletUtil.readList(context, "panel", this.panel);
        this.body = PubletUtil.read(context, "body");
        return true;
    }

    @Override
    public boolean publishPage(PubletPagePublishContext context, PubletPagePublishResult result) {
        context.setService(this);
        boolean r = false;
        for (IPublet pl : this.panel) {
            if (pl.publishPage(context, result)) {
                r = true;
            }
        }
        return r;
    }

    @Override
    public List<IPublet> getChildren() {
        List<IPublet> r = new LinkedList<IPublet>(this.getChildren());
        if (this.body != null) {
            r.add(this.body);
        }
        return r;
    }
}
