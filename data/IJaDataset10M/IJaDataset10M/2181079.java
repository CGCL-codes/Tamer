package com.g3d;

public interface G3DCanvas {

    /**
	 * 切换场景
	 * @param stage
	 * @param args 传入参数，参数会在Stage.inited(Object[] args)通知stage
	 */
    public void changeStage(G3DStage stage, Object... args);

    /**
	 * 切换场景
	 * @param stageName
	 * @param args 传入参数，参数会在Stage.inited(Object[] args)通知stage
	 */
    public void changeStage(String stageName, Object... args);

    /**
	 * 切换场景
	 * @param stageClass
	 * @param args 传入参数，参数会在Stage.inited(Object[] args)通知stage
	 */
    public void changeStage(Class<?> stageClass, Object... args);

    /***
	 * 立刻切换当前场景
	 * @param stage
	 */
    public void setStage(G3DStage stage);

    public int getWidth();

    public int getHeight();
}
