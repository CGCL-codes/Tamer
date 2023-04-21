package org.xiangxji.util.framework.framework;

/**
 * 管道接口
 * @author xiangxji
 *
 */
public interface IPipe<I extends IPipeInput, O extends IPipeResult> {

    /**
	 * 获取pipe的名称
	 * @return
	 */
    public String getPipeName();

    /**
	 * 设置pipe的名称
	 * @param pipeName
	 */
    public void setPipeName(String pipeName);

    /**
	 * 执行管道具体逻辑
	 * @param pipeInput
	 * @param pipeResult
	 */
    public void doPipe(I pipeInput, O pipeResult);

    /**
	 * 获取当前处理上下文
	 * @return
	 */
    public IPipeContext getContext();

    /**
	 * 根据传入参数和上下文
	 * 判断是否忽略此管道的处理
	 * @param pipeInput
	 * @return
	 */
    public boolean ignoreIt(I pipeInput, O pipeResult);

    /**
	 * 是否是异步化的管道
	 * @return
	 */
    public boolean isAsynMode();

    /**
	 * 在特定情况下可以不按照异步模式执行，避免一刀切带来的性能损耗
	 * @param pipeInput
	 * @param pipeResult
	 * @return
	 */
    public boolean ignoreAsynMode(I pipeInput, O pipeResult);

    /**
	 * 设置管道的模式
	 * @param mode
	 */
    public void setAsynMode(boolean mode);
}
