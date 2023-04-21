package org.dizem.sanguosha.model.card;

public interface ICard {

    /**
	 * 黑桃
	 */
    int PATTERN_SPADE = 1;

    /**
	 * 红桃
	 */
    int PATTERN_HEART = 2;

    /**
	 * 方块
	 */
    int PATTERN_DIAMOND = 3;

    /**
	 * 梅花
	 */
    int PATTERN_CLUB = 4;

    /**
	 * 黑色花色
	 */
    int COLOR_BLACK = 10;

    /**
	 * 红色花色
	 */
    int COLOR_RED = 20;

    /**
	 * 基本牌
	 */
    int CATEGORY_BASIC = 100;

    /**
	 * 锦囊牌
	 */
    int CATEGORY_SKILL = 200;

    /**
	 * 装备牌
	 */
    int CATEGORY_EQUIPMENT = 300;

    /**
	 * 得到牌的花色
	 *
	 * @return 返回黑桃、红桃、方块或梅花中的一种
	 */
    public int getSuit();

    /**
	 * 得到牌的颜色
	 *
	 * @return 返回黑色或红色
	 */
    public int getColor();

    /**
	 * 得到牌的种类
	 *
	 * @return 返回基本牌，锦囊牌或装备牌中的一种
	 */
    public int getCategory();

    /**
	 * 返回牌的名字
	 *
	 * @return
	 */
    public String getName();

    /**
	 * 返回牌的点数
	 *
	 * @return
	 */
    public String getRank();
}
