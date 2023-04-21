package org.loon.framework.android.game.core.graphics.window;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LComponent;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import android.graphics.Bitmap.Config;

/**
 * 
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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class UIConfig {

    private final UIFactory EMPTY_FACTORY = new NullFactory();

    private final Map<String, UIFactory> UIRenderers = new HashMap<String, UIFactory>();

    /**
	 * 构造UI配置器
	 * 
	 */
    public UIConfig() {
        this.setupUI(this.EMPTY_FACTORY);
    }

    /**
	 * 设定指定UI
	 * 
	 * @param ui
	 * @return
	 */
    public boolean setupUI(UIFactory ui) {
        if (ui != this.EMPTY_FACTORY) {
            UIFactory factory = this.createUIFactory(ui.getUIName());
            if (factory != null && factory.immutable) {
                return false;
            }
        }
        this.UIRenderers.put(ui.getUIName(), ui);
        return true;
    }

    /**
	 * 设定一个空UI
	 * 
	 * @param UIName
	 * @return
	 */
    public boolean setupEmptyUI(String name) {
        return this.setupUI(new EmptyFactory(name));
    }

    /**
	 * 创建UI工厂
	 * 
	 * @param UIName
	 * @return
	 */
    public UIFactory createUIFactory(String name) {
        if (name == null) {
            return this.EMPTY_FACTORY;
        }
        UIFactory factory = null;
        StringTokenizer st = new StringTokenizer(name, ".");
        if (st.countTokens() > 0) {
            String[] tokens = new String[st.countTokens()];
            for (int i = tokens.length - 1; i >= 0; i--) {
                tokens[i] = st.nextToken();
            }
            int num = 0;
            while (factory == null && num < tokens.length) {
                factory = (UIFactory) this.UIRenderers.get(tokens[num]);
                num++;
            }
        }
        return (factory != null) ? factory : this.EMPTY_FACTORY;
    }

    /**
	 * 获得当前配置名
	 * 
	 * @return
	 */
    public String getName() {
        return "Null Config";
    }

    /**
	 * 获得指定的工厂对象
	 * 
	 * @param name
	 * @return
	 */
    public UIFactory getUIFactory(String name) {
        UIFactory renderer = null;
        StringTokenizer st = new StringTokenizer(name, ".");
        if (st.countTokens() > 0) {
            String[] tokens = new String[st.countTokens()];
            for (int i = tokens.length - 1; i >= 0; i--) {
                tokens[i] = st.nextToken();
            }
            int num = 0;
            while (renderer == null && num < tokens.length) {
                renderer = (UIFactory) this.UIRenderers.get(tokens[num]);
                num++;
            }
        }
        return renderer;
    }

    public UIFactory[] getInstalledUI() {
        return (UIFactory[]) this.UIRenderers.values().toArray(new UIFactory[0]);
    }
}

class NullFactory extends UIFactory {

    public NullFactory() {
    }

    public String getUIName() {
        return "Invalid Component";
    }

    public String[] getUIDescription() {
        return new String[] { "Invalid Component" };
    }

    public LImage[] createUI(LComponent component, int width, int height) {
        LImage[] ui = LImage.createImage(1, width, height, Config.RGB_565);
        LGraphics g = ui[0].getLGraphics();
        g.setColor(LColor.white);
        g.fill3DRect(4, 4, width - 8, height - 8, true);
        g.setColor(LColor.black);
        g.drawRect(0, 0, width, height);
        g.dispose();
        return ui;
    }

    public void processUI(LComponent component, LImage[] ui) {
    }

    public void createUI(LGraphics g, int x, int y, LComponent component, LImage[] ui) {
        g.drawImage(ui[0], x, y);
    }
}

class EmptyFactory extends UIFactory {

    private String name;

    private LImage[] ui = new LImage[0];

    public EmptyFactory(String name) {
        this.name = name;
    }

    public String getUIName() {
        return this.name;
    }

    public String[] getUIDescription() {
        return new String[] { "Empty Component" };
    }

    public LImage[] createUI(LComponent component, int w, int h) {
        return this.ui;
    }

    public void processUI(LComponent component, LImage[] ui) {
    }

    public void createUI(LGraphics g, int x, int y, LComponent component, LImage[] ui) {
    }
}
