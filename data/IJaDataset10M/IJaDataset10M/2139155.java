package com.nullfish.app.jfd2.ui.container2;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author shunji
 *
 */
public class ContainerPosition {

    private static Map nameInstanceMap = new HashMap();

    private String name;

    /**
	 * 新しいメインパネル
	 */
    public static final ContainerPosition MAIN_PANEL = new ContainerPosition("new_main_panel", "new_sub_panel");

    /**
	 * 新しいサブパネル
	 */
    public static final ContainerPosition SUB_PANEL = new ContainerPosition("new_sub_panel", "new_main_panel");

    /**
	 * 独立したウインドウ
	 */
    public static final ContainerPosition NEW_WINDOW = new ContainerPosition("new_window", "new_window");

    /**
	 * それ以外（jFDで提供しない）
	 */
    public static final ContainerPosition ETC = new ContainerPosition("etc", null);

    /**
	 * 対象名
	 */
    private String opponent;

    /**
	 * コンストラクタ
	 * @param name
	 */
    private ContainerPosition(String name, String opponent) {
        this.name = name;
        this.opponent = opponent;
        nameInstanceMap.put(name, this);
    }

    public static ContainerPosition getInstance(String name) {
        return (ContainerPosition) nameInstanceMap.get(name);
    }

    public String toString() {
        return "ContainerConstraints : " + name;
    }

    public ContainerPosition getOpponent() {
        return (ContainerPosition) nameInstanceMap.get(opponent);
    }
}
