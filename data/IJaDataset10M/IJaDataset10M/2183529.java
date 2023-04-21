package net.sourceforge.offsprings.splitter;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

/**
 *
 * @author Edward Salatovka
 */
public class SplitterAdvice implements MethodInterceptor {

    private Executor _executor;

    private int _parallelism;

    private int _batchsize;

    private int _idsArgPosition = -1;

    private SplitterFactory _factory;

    private Map<String, String> _properties;

    class Worker<IDS, RESULT> implements Runnable {

        IdsIterator<IDS> _idsIterator;

        BarrierResult<RESULT> _barrier;

        int _threadNumber;

        int _idsParamPosition;

        String _workerId = null;

        OutputStream _cacheOutputStream = null;

        MethodInvocation _invocation;

        Worker(IdsIterator<IDS> idsIterator, BarrierResult<RESULT> barrier, MethodInvocation invocation, int idsParamPosition, int threadNumber) {
            _idsIterator = idsIterator;
            _barrier = barrier;
            _idsParamPosition = idsParamPosition;
            _invocation = invocation;
            _threadNumber = threadNumber;
        }

        @SuppressWarnings("unchecked")
        public void run() {
            Batch<IDS, RESULT> batch;
            while ((batch = (Batch<IDS, RESULT>) _idsIterator.nextBatch()) != null && _barrier.getException() == null) {
                IDS idsObj = batch.getIds();
                RESULT answerObj = null;
                try {
                    MethodInvocation clone = ((ReflectiveMethodInvocation) _invocation).invocableClone();
                    Object[] args = clone.getArguments();
                    args[_idsParamPosition] = idsObj;
                    answerObj = (RESULT) clone.proceed();
                    batch.setResult(answerObj);
                    batch.setThreadNumber(_threadNumber);
                    _barrier.addBatchResult(batch);
                } catch (Throwable thr) {
                    _barrier.setException(thr);
                    break;
                }
            }
            _barrier.mergeThread(_threadNumber);
            _barrier.quitThread(_threadNumber);
        }
    }

    public SplitterAdvice() {
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        defineArgumentPosition(invocation);
        Object result = split(invocation);
        return result;
    }

    private void defineArgumentPosition(MethodInvocation invocation) {
        if (_idsArgPosition == -1) {
            Class<?>[] paramTypes = invocation.getMethod().getParameterTypes();
            boolean isFound = false;
            for (int i = 0; i < paramTypes.length; i++) {
                Class<?> cls = paramTypes[i];
                if (cls.isArray() || cls == List.class) {
                    _idsArgPosition = i;
                    isFound = true;
                    break;
                }
            }
            if (!isFound) throw new RuntimeException(new java.lang.IllegalArgumentException());
        }
    }

    @SuppressWarnings("unchecked")
    private Object split(MethodInvocation invocation) throws Exception {
        Object[] arguments = invocation.getArguments();
        Object ids = arguments[_idsArgPosition];
        arguments[_idsArgPosition] = null;
        Class<?> answerType = invocation.getMethod().getReturnType();
        Class<?> idsType = (invocation.getMethod().getParameterTypes())[_idsArgPosition];
        BarrierResult<?> barrier;
        barrier = _factory.getBarrier(answerType);
        barrier.init(_parallelism, answerType, _properties);
        IdsIterator<?> idsIterator;
        idsIterator = _factory.getIdsIterator(idsType);
        idsIterator.init(idsType.cast(ids), _parallelism, _batchsize);
        for (int i = 0; i < _parallelism; i++) {
            Worker<?, ?> worker = new Worker(idsIterator, barrier, invocation, _idsArgPosition, i);
            _executor.execute(worker);
        }
        arguments[_idsArgPosition] = ids;
        if (barrier.getException() != null) {
            throw new Exception(barrier.getException());
        }
        barrier.await();
        Object answer = barrier.merge();
        return answer;
    }

    public void setExecutor(Executor executor) {
        this._executor = executor;
    }

    public void setParallelism(int parallelism) {
        this._parallelism = parallelism;
    }

    public int getParallelism() {
        return _parallelism;
    }

    public void setBatchsize(int batchsize) {
        _batchsize = batchsize;
    }

    public int getBatchSize() {
        return _batchsize;
    }

    public void setIdsArgPosition(int idsArgPosition) {
        this._idsArgPosition = idsArgPosition;
    }

    public void setFactory(SplitterFactory factory) {
        _factory = factory;
    }

    public Map<String, String> getProperties() {
        return _properties;
    }

    public void setProperties(Map<String, String> properties) {
        this._properties = properties;
    }
}
