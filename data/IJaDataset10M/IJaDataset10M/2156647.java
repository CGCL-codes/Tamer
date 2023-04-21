package me.fantasy2.stg.rootcell.ext;

import me.fantasy2.stg.STGCell;
import me.fantasy2.stg.rootcell.Player;

/**
 * 用于显示玩家判定点的物件
 * 
 * @author Cloudee
 */
public class Checker extends STGCell {

    private int alpha;

    private boolean hide = true;

    private Player player;

    /**
	 * 是否将判定点隐藏
	 * 
	 * @param b
	 */
    public void setHide(boolean b) {
        hide = b;
    }

    /**
	 * 构造判定点
	 * 
	 * @param p
	 *            - 判定点绑定的玩家
	 * @param spid
	 *            - 判定点的图像Id
	 */
    public Checker(Player p, String imageId, String animId) {
        this.setImageId(imageId);
        this.setAnimId(animId);
        player = p;
    }

    public boolean process() {
        if (alpha < 255 && !hide) {
            alpha += 7;
            if (alpha > 255) alpha = 255;
        } else if (alpha > 0 && hide) {
            alpha -= 120;
            if (alpha < 0) alpha = 0;
        }
        return true;
    }

    public int getAlpha() {
        return alpha;
    }
}
