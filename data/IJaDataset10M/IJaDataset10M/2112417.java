package com.g2d;

import com.g2d.display.Stage;

public interface Canvas {

    public boolean isFocusOwner();

    public int getUpdateIntervalMS();

    /**
	 * 是否在进行文本输入
	 * @return
	 */
    public boolean isOnTextInput(int keycode);

    /**
	 * 设置默认鼠标指针图形
	 * @param cursor
	 */
    public void setDefaultCursor(AnimateCursor cursor);

    /**
	 * 设置默认渲染子体
	 * @param font
	 */
    public void setDefaultFont(Font font);

    public Font getDefaultFont();

    /**
	 * 切换场景
	 * @param stage
	 * @param args 传入参数，参数会在Stage.inited(Object[] args)通知stage
	 */
    public void changeStage(Stage stage, Object... args);

    /**
	 * 切换场景
	 * @param stageClass
	 * @param args 传入参数，参数会在Stage.inited(Object[] args)通知stage
	 */
    public void changeStage(Class<? extends Stage> stageClass, Object... args);

    /***
	 * 立刻切换当前场景
	 * @param stage
	 */
    public void setStage(Stage stage);

    /**
	 * 设置场景象素大小
	 * @param width
	 * @param height
	 */
    public void setStageSize(int width, int height);

    public int getStageWidth();

    public int getStageHeight();

    public Stage getStage();

    public Class<?> getLastStageType();

    /**
	 * 设置游戏更新速度
	 * @param fps
	 */
    public void setFPS(double fps);

    /**
	 * 得到帧延迟，单位ms
	 * @return
	 */
    public int getFrameDelay();

    /**
	 * 设置游戏更新速度(未活动窗口)
	 * @param fps
	 */
    public void setUnactiveFPS(double fps);

    /**
	 * 得到帧延迟，单位ms(未活动窗口)
	 * @return
	 */
    public int getUnactiveFrameDelay();

    /**
	 * 得到游戏更新速度
	 * @return
	 */
    public int getFPS();

    /**
	 * 检测当前帧键有没有被按住
	 * @param keycode
	 * @return
	 */
    public boolean isKeyHold(int... keycode);

    /**
	 * 检测当前帧键有没有被按下
	 * @param keycode
	 * @return
	 */
    public boolean isKeyDown(int... keycode);

    /**
	 * 检测当前帧键有没有被松开
	 * @param keycode
	 * @return
	 */
    public boolean isKeyUp(int... keycode);

    /**
	 * 检测当前帧被按下的键的个数
	 * @return
	 */
    public int getDownKeyCount();

    /**
	 * 检测当前帧被松开的键的个数
	 * @return
	 */
    public int getUpKeyCount();

    /**
	 *  检测当前帧鼠标有没有被按住
	 * @param button
	 * @return
	 */
    public boolean isMouseHold(int... button);

    /**
	 *  检测当前帧鼠标被连续的按下
	 * @param freeze_time
	 * @param button
	 * @return
	 */
    public boolean isMouseContinuous(long freeze_time, int... button);

    /**
	 *  检测当前帧鼠标有没有被按下
	 * @param button
	 * @return
	 */
    public boolean isMouseDown(int... button);

    /**
	 * 在上次鼠标被按下后多少时间，检查鼠标被按下
	 * @param time 如果(现在时间-上次按下时间 < time)则进行判断
	 * @param button
	 * @return
	 */
    public boolean isMouseDoubleDown(long time, int... button);

    /**
	 *  检测当前帧鼠标有没有被松开
	 * @param button
	 * @return
	 */
    public boolean isMouseUp(int... button);

    /**
	 *  检测当前帧鼠标滚轮有没有向上滚
	 * @param button
	 * @return
	 */
    public boolean isMouseWheelUP();

    /**
	 *  检测当前帧鼠标滚轮有没有向下滚
	 * @param button
	 * @return
	 */
    public boolean isMouseWheelDown();

    /**
	 * 得到鼠标指针在stage的位置
	 * @param button
	 * @return
	 */
    public int getMouseX();

    /**
	 * 得到鼠标指针在stage的位置
	 * @param button
	 * @return
	 */
    public int getMouseY();
}
