package org.loon.framework.javase.game.action.sprite;

import java.awt.Rectangle;
import org.loon.framework.javase.game.GameManager;
import org.loon.framework.javase.game.action.map.shapes.RectBox;
import org.loon.framework.javase.game.core.LObject;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;

/**
 * Copyright 2008 - 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public abstract class AbstractBackground extends LObject implements ISprite {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int width, height;

    protected float alpha;

    protected boolean visible;

    public AbstractBackground(int w, int h) {
        this.width = w;
        this.height = h;
        this.visible = true;
    }

    public AbstractBackground() {
        this(GameManager.getSystemHandler().getWidth(), GameManager.getSystemHandler().getHeight());
    }

    public void createUI(LGraphics g) {
        createUI(g, x(), y(), width, height);
    }

    public abstract void createUI(LGraphics g, int x, int y, int w, int h);

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
    }

    public void setToCenter(int x, int y, int w, int h) {
        this.setLocation(x + (w / 2) - (width / 2), y + (h / 2) - (height / 2));
    }

    public void setToCenter(Sprite centered) {
        this.setToCenter(centered.x(), centered.y(), centered.getWidth(), centered.getHeight());
    }

    public void setClip(int x, int y, int width, int height) {
        this.setLocation(x, y);
        this.width = width;
        this.width = height;
    }

    public void setClip(Rectangle r) {
        this.setLocation(r.x, r.y);
        this.width = r.width;
        this.width = r.height;
    }

    public Rectangle getClip() {
        return new Rectangle(x(), y(), width, height);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getAlpha() {
        return alpha;
    }

    public RectBox getCollisionBox() {
        return new RectBox(x(), y(), width, height);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void update(long timer) {
    }
}
