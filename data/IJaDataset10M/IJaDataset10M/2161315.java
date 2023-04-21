package com.g2d.display.particle;

import java.awt.Graphics2D;
import java.io.Serializable;

/**
 * 粒子渲染出的外观
 * @author WAZA
 */
public interface ParticleAppearance extends Serializable {

    /**
	 * 渲染该单位
	 * @param g
	 */
    public void render(Graphics2D g, Layer layer);

    /**
	 * 该方法只在渲染前被调用
	 * @return
	 */
    public ParticleAppearance cloneDisplay();
}
