package fr.fg.client.map.miniitem;

import fr.fg.client.data.AsteroidsData;
import fr.fg.client.map.UIMiniItemRenderingHints;

public class AsteroidsMiniItem extends AnimatedMiniItem {

    private AsteroidsData asteroidsData;

    public AsteroidsMiniItem(AsteroidsData asteroidsData, UIMiniItemRenderingHints hints) {
        super(asteroidsData.getX(), asteroidsData.getY(), hints);
        this.asteroidsData = asteroidsData;
        setStyleName("asteroid" + (asteroidsData.getType().equals("asteroid") ? "" : " " + asteroidsData.getType()));
        getElement().setAttribute("unselectable", "on");
        boolean hide = asteroidsData.getType().equals("asteroid") || asteroidsData.getType().equals("asteroid_dense");
        getElement().getStyle().setProperty("display", hide ? "none" : "");
    }

    @Override
    public void onDataUpdate(Object newData) {
        AsteroidsData newAsteroidsData = (AsteroidsData) newData;
        if (asteroidsData.getX() != newAsteroidsData.getX() || asteroidsData.getY() != newAsteroidsData.getY()) setLocation(newAsteroidsData.getX(), newAsteroidsData.getY());
        if (!asteroidsData.getType().equals(newAsteroidsData.getType())) {
            setStyleName("asteroid" + (newAsteroidsData.getType().equals("asteroid") ? "" : " " + newAsteroidsData.getType()));
            boolean hide = newAsteroidsData.getType().equals("asteroid") || newAsteroidsData.getType().equals("asteroid_dense");
            getElement().getStyle().setProperty("display", hide ? "none" : "");
        }
        asteroidsData = newAsteroidsData;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        asteroidsData = null;
    }
}
