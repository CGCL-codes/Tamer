package com.mw3d.game.ui;

import java.util.ArrayList;
import java.util.List;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

public class MenuItem {

    private String id;

    private Text menuItem;

    private List itemList = new ArrayList();

    private float size, selectSize;

    private ColorRGBA onColor, offColor = null;

    private MaterialState onMat, offMat = null;

    private int selected = 0;

    private Node container;

    public MenuItem(Node container, String id, String text, Vector3f pos, float size, float selectSize, ColorRGBA onColor, ColorRGBA offColor) {
        this.container = container;
        this.id = id;
        this.itemList.add(text);
        this.size = size;
        this.selectSize = selectSize;
        this.onColor = onColor;
        this.offColor = offColor;
        this.onMat = makeMaterial(onColor);
        this.offMat = makeMaterial(offColor);
        this.selected = 0;
        menuItem = new Text(id, text);
        menuItem.setLocalScale(this.size);
        menuItem.setLocalTranslation(new Vector3f(pos.x, pos.y, 0.0F));
        menuItem.setRenderState(this.offMat);
        menuItem.setForceView(true);
        menuItem.setTextureCombineMode(TextureState.REPLACE);
        container.attachChild(menuItem);
        container.updateGeometricState(0.0f, true);
        container.updateRenderState();
    }

    private MaterialState makeMaterial(ColorRGBA colorRGBA) {
        MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        ms.setEnabled(true);
        ms.setAmbient(colorRGBA);
        ms.setAlpha(0.5F);
        ms.setDiffuse(colorRGBA);
        ms.setEmissive(colorRGBA);
        return ms;
    }

    public void addMenuItemValue(String text) {
        if (itemList == null) itemList = new ArrayList();
        itemList.add(text);
    }

    public void switchUp() {
        selected++;
        if (selected >= itemList.size()) {
            selected = 0;
        }
    }

    public void switchDown() {
        selected--;
        if (selected < 0) {
            selected = itemList.size() - 1;
        }
    }

    public void selected(boolean selected) {
        if (selected) {
            menuItem.setRenderState(this.onMat);
            menuItem.setLocalScale(this.selectSize);
        } else {
            menuItem.setRenderState(this.offMat);
            menuItem.setLocalScale(this.size);
        }
        menuItem.print(itemList.get(this.selected).toString());
    }

    public String getId() {
        return id;
    }

    public Text getMenuItem() {
        return menuItem;
    }

    public String getSelectedText() {
        return itemList.get(selected).toString();
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
