package org.loon.framework.javase.game.action.avg;

import java.awt.Image;
import org.loon.framework.javase.game.utils.GraphicsUtils;
import org.loon.framework.javase.game.utils.collection.ArrayMap;

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
public class CG {

    private Image backgroundCG;

    private ArrayMap charas;

    public CG() {
        charas = new ArrayMap(100);
    }

    public Image getBackgroundCG() {
        return backgroundCG;
    }

    public void noneBackgroundCG() {
        this.backgroundCG = null;
    }

    public void setBackgroundCG(Image backgroundCG) {
        this.backgroundCG = backgroundCG;
    }

    public void setBackgroundCG(String resName) {
        this.backgroundCG = GraphicsUtils.loadImage(resName);
    }

    public void addChara(String file, Chara role) {
        charas.put(file.replaceAll(" ", "").toLowerCase(), role);
    }

    public void addImage(String name, int x, int y, int w) {
        String keyName = name.replaceAll(" ", "").toLowerCase();
        Chara chara = (Chara) charas.get(keyName);
        if (chara == null) {
            charas.put(keyName, new Chara(name, x, y, w));
        } else {
            chara.setX(x);
            chara.setY(y);
        }
    }

    public Chara removeImage(String file) {
        return (Chara) charas.remove(file.replaceAll(" ", "").toLowerCase());
    }

    public void dispose() {
        for (int i = 0; i < charas.size(); i++) {
            Chara ch = (Chara) charas.get(i);
            ch.dispose();
            ch = null;
        }
        charas.clear();
    }

    public void clear() {
        charas.clear();
    }

    public ArrayMap getCharas() {
        return charas;
    }
}
