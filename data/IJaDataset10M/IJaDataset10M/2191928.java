package me.fantasy2.stg.rootcell.ext;

import me.fantasy2.Cell;
import me.fantasy2.stg.STGCell;

/**
 * 圆形的魔法阵转啊转
 * 
 * @author Cloudee
 */
public class CircleBarrier extends STGCell {

    protected float minScale = 1;

    protected float maxScale = 1;

    protected float currentScale = 1;

    protected float scaleSpeed = 0;

    protected float rotSpeed;

    protected float imageDir;

    protected int firstAlpha = 0;

    protected int lastAlpha = 0;

    protected Cell bind;

    public void bind(Cell bo) {
        bind = bo;
    }

    public void init() {
    }

    /**
	 * 固定魔法阵的不透明度
	 * 
	 * @param epAlpha
	 */
    public CircleBarrier setAlpha(int alpha) {
        firstAlpha = alpha;
        lastAlpha = alpha;
        return this;
    }

    /**
	 * 魔法阵开始时候的透明度
	 * 
	 * @param epFirstAlpha
	 */
    public void setFirstAlpha(int firstAlpha) {
        this.firstAlpha = firstAlpha;
    }

    /**
	 * 魔法阵的最后的时候的透明度
	 * 
	 * @param epLastAlpha
	 */
    public void setLastAlpha(int lastAlpha) {
        this.lastAlpha = lastAlpha;
    }

    /**
	 * 魔法阵最大的时候的尺寸（大于这个尺寸的时候就消亡）
	 * 
	 * @param epMaxScale
	 */
    public void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
    }

    /**
	 * 魔法阵最小的时候的尺寸（小于这个尺寸的时候就消亡）
	 * 
	 * @param epMinScale
	 */
    public void setMinScale(float minScale) {
        this.minScale = minScale;
    }

    /**
	 * 魔法阵的转动速度
	 * 
	 * @param epRotSpeed
	 */
    public void setRotSpeed(float rotSpeed) {
        this.rotSpeed = rotSpeed;
    }

    /**
	 * 魔法阵的扩张速度（负数就是缩小）
	 * 
	 * @param epScaleSpeed
	 */
    public void setScaleSpeed(float scaleSpeed) {
        this.scaleSpeed = scaleSpeed;
    }

    public float getImageDir() {
        return imageDir;
    }

    public float getScaleX() {
        return currentScale;
    }

    public float getScaleY() {
        return currentScale;
    }

    public boolean process() {
        currentScale += scaleSpeed;
        imageDir += rotSpeed;
        if (bind != null && !bind.isDead()) {
            setPos(bind.getX(), bind.getY());
        }
        if (currentScale > maxScale || currentScale < minScale) {
            return false;
        }
        return true;
    }

    public int getAlpha() {
        int dAlpha;
        if (firstAlpha == lastAlpha) {
            return firstAlpha;
        }
        if (maxScale == minScale) {
            return firstAlpha;
        }
        dAlpha = (int) ((currentScale - minScale) * (lastAlpha - firstAlpha) / (maxScale - minScale));
        if (scaleSpeed > 0) {
            return firstAlpha + dAlpha;
        } else {
            return lastAlpha - dAlpha;
        }
    }
}
