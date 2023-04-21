package source.view.viewobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import source.model.ObjectID;
import source.model.type.AreaEffectType;
import source.view.MainScreen;

public class ViewAreaEffect extends ViewObject {

    private AreaEffectType aeType;

    public ViewAreaEffect(AreaEffectType aeType, ObjectID oid) {
        this.aeType = aeType;
        this.setObjectID(oid);
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.RED);
        g.drawString(aeType.toString(), 0, 0);
    }
}
