package org.loon.framework.game.simple.core.graphics;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Window;
import org.loon.framework.game.simple.Java2DView;
import org.loon.framework.game.simple.IView;
import org.loon.framework.game.simple.core.EmulatorButtons;
import org.loon.framework.game.simple.core.EmulatorListener;
import org.loon.framework.game.simple.core.IHandler;
import org.loon.framework.game.simple.core.LSystem;

/**
 * Copyright 2008 - 2009
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
 * @email ceponline@yahoo.com.cn
 * @version 0.1
 */
public class Deploy {

    private int left, top;

    private Window screen;

    private IHandler game;

    private IView view;

    public Deploy(IHandler game) {
        LSystem.gc();
        game.setDeploy(this);
        this.game = game;
        this.screen = game.getScene().getWindow();
        initView(screen);
    }

    public EmulatorButtons getEmulatorButtons() {
        return view.getEmulatorButtons();
    }

    public void setEmulatorListener(EmulatorListener emulator) {
        view.setEmulatorListener(emulator);
    }

    public EmulatorListener getEmulatorListener() {
        return view.getEmulatorListener();
    }

    public void setLogo(boolean logo) {
        view.setShowLogo(logo);
    }

    public void mainLoop() {
        view.mainLoop();
    }

    public void setShowFPS(boolean isFPS) {
        view.setShowFPS(isFPS);
    }

    public void setFPS(long frames) {
        view.setFPS(frames);
    }

    public long getCurrentFPS() {
        return view.getCurrentFPS();
    }

    public IHandler getGame() {
        return this.game;
    }

    public void makeScreen(Class clazz) {
        setScreen(ScreenManager.makeScreen(clazz));
    }

    public void makeScreen(Class clazz, Object[] args) {
        setScreen(ScreenManager.makeScreen(clazz, args));
    }

    public void setScreen(IScreen screen) {
        if (screen == null) {
            throw new RuntimeException("Cannot create a [IScreen] instance !");
        }
        screen.setupHandler(game);
        this.game.setScreen(screen);
    }

    private void initView(final Window screen) {
        view = new Java2DView(game);
        view.startPaint();
        left = screen.getInsets().left;
        top = screen.getInsets().top;
        screen.add((Canvas) view);
        screen.pack();
        screen.invalidate();
        screen.validate();
        view.createScreen();
        screen.doLayout();
        view.requestFocus();
    }

    public synchronized boolean addComponent(Component component) {
        return addComponent(0, 0, component);
    }

    public synchronized boolean addComponent(int x, int y, Component component) {
        return addComponent(x, y, component.getWidth(), component.getHeight(), component);
    }

    public synchronized boolean addComponent(int x, int y, int w, int h, Component component) {
        if (component == null) {
            return false;
        }
        Component[] components = screen.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] == component) {
                return false;
            }
        }
        if (!LSystem.isApplet && game.getScene().getIScene().getType() == Model.Awt) {
            component.setBounds(x + left, y + top, w, h);
        } else {
            component.setBounds(x, y, w, h);
        }
        screen.setLayout(null);
        screen.add(component, 0);
        screen.repaint();
        screen.validate();
        return true;
    }

    public synchronized void removeComponent(Component component) {
        if (component == null) {
            return;
        }
        screen.remove(component);
        screen.repaint();
        screen.validate();
    }

    public synchronized void removeComponent(int index) {
        screen.remove(index);
        screen.repaint();
        screen.validate();
    }

    public Window getScreen() {
        return screen;
    }

    public IView getView() {
        return view;
    }

    public void setGame(IHandler game) {
        this.game = game;
    }

    public void setLayout(LayoutManager layout) {
        this.screen.setLayout(layout);
    }
}
