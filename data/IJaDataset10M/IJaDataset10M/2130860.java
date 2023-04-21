package net.sf.revolver.core;

import net.sf.bulletlib.core.logging.Logging;

/**
 * null object of RvDoCore.
 *
 * @author ykhr
 */
public class NullRvDo extends RvDoCore {

    /**
     * フレームワーク初期処理.
     *
     * @param rvContext Revolver Framework Context
     */
    @Override
    protected void init(RvContext rvContext) {
    }

    /**
     * 初期処理.
     *
     * @param rvContext Revolver Framework Context
     */
    @Override
    protected void initProcess(RvContext rvContext) throws BusinessLogicException {
    }

    /**
     * 終了処理.
     *
     * @param rvContext Revolver Framework Context
     */
    @Override
    protected void endProcess(RvContext rvContext) throws BusinessLogicException {
    }

    /**
     * フレームワーク終了処理.
     *
     * @param rvContext Revolver Framework Context
     */
    @Override
    protected void end(RvContext rvContext) {
    }

    /**
     * ビジネスロジックを実行し,処理結果をResultで返す.
     *
     * @param rvContext Revolver Framework Context
     * @return Result
     */
    @Override
    protected Result doLogic(RvContext rvContext) throws BusinessLogicException {
        Logging.debug(this.getClass(), "BLI999999", rvContext.getLoginId(), "RvDo is null.RvDo bypass");
        return Result.SUCCESS;
    }
}
