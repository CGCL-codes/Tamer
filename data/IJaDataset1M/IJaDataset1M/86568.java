package lang2.vm.op;

import lang2.vm.op.ExecuteFlow.Target;
import lang2.vm.StackedError;
import lang2.vm.Value;

/**
 * Специальное значение определяющее направление потока вычислений
 * @author gocha
 */
public class ExecuteFlowValue extends AbstractTreeNode<Value> implements Value, ExecuteFlow {

    /**
     * Конструктор по умолчанию
     */
    public ExecuteFlowValue() {
        this.target = null;
        this.hasValue = false;
        this.value = null;
    }

    /**
     * Конструктор копирования
     * @param src Образец для копирования
     */
    public ExecuteFlowValue(ExecuteFlowValue src) {
        if (src == null) {
            throw new IllegalArgumentException("src==null");
        }
        target = src.target;
        hasValue = src.hasValue;
        value = src.value;
        if (value != null) value.setParent(this);
    }

    /**
     * Конструктор копирования
     * @param src Образец для копирования
     */
    public ExecuteFlowValue(ExecuteFlowValue src, boolean deep) {
        if (src == null) {
            throw new IllegalArgumentException("src==null");
        }
        target = src.target;
        hasValue = src.hasValue;
        value = deep && src.value != null ? src.value.deepClone() : src.value;
        if (value != null) value.setParent(this);
    }

    /**
     * Конструктор
     * @param target Цель/Направление потока вычислений
     */
    public ExecuteFlowValue(ExecuteFlow.Target target) {
        this.target = target;
        this.hasValue = false;
        this.value = null;
        if (value != null) value.setParent(this);
    }

    /**
     * Конструктор
     * @param target Цель/Направление потока вычислений
     * @param value Возвращаемое значение (return/break/continue)
     */
    public ExecuteFlowValue(ExecuteFlow.Target target, Value value) {
        this.target = target;
        this.value = value;
        this.hasValue = true;
        if (value != null) value.setParent(this);
    }

    /**
     * Создает инструкцию RETURN
     * @param value Возвращаемое значение (return/break/continue)
     * @return Инструкция RETURN
     */
    public static ExecuteFlowValue createReturn(Value value) {
        return new ExecuteFlowValue(Target.Return, value);
    }

    /**
     * Создает инструкцию RETURN
     * @return Инструкция RETURN
     */
    public static ExecuteFlowValue createReturn() {
        return new ExecuteFlowValue(Target.Return);
    }

    /**
     * Создает инструкцию BREAK
     * @return Инструкция BREAK
     */
    public static ExecuteFlowValue createBreak() {
        return new ExecuteFlowValue(Target.Break);
    }

    /**
     * Создает инструкцию BREAK
     * @param value Возвращаемое значение (return/break/continue)
     * @return Инструкция BREAK
     */
    public static ExecuteFlowValue createBreak(Value value) {
        return new ExecuteFlowValue(Target.Break, value);
    }

    /**
     * Создает инструкцию BREAK
     * @param Возвращаемое значение (return/break/continue)
     * @return Инструкция BREAK
     */
    public static ExecuteFlowValue createContinue(Value value) {
        return new ExecuteFlowValue(Target.Continue, value);
    }

    /**
     * Создает инструкцию CONTINUE
     * @return Инструкция CONTINUE
     */
    public static ExecuteFlowValue createContinue() {
        return new ExecuteFlowValue(Target.Continue);
    }

    /**
     * Создает инструкцию THROW
     * @param Возвращаемое значение (throw e...)
     * @return Инструкция THROW
     */
    public static ExecuteFlowValue createThrow(Value value) {
        return new ExecuteFlowValue(Target.Throw, value);
    }

    /**
     * Создает инструкцию THROW
     * @param Возвращаемое значение (throw e...)
     * @return Инструкция THROW
     */
    public static ExecuteFlowValue createThrow(Value valueParent, Value child) {
        StackedError errParent = new StackedError(valueParent.evaluate().toString());
        errParent.setErrorValue(valueParent);
        StackedError errChild = new StackedError(child.evaluate().toString());
        errChild.setErrorValue(child);
        errChild.setParentError(errParent);
        return new ExecuteFlowValue(Target.Throw, new Const(errChild));
    }

    protected boolean hasValue = false;

    protected Value value = null;

    protected ExecuteFlow.Target target = null;

    /**
     * Указывает что данная инструкция возвращает значение
     * @param hasValue true - возвращает значение; false - не возвращает
     */
    public void setHasFlowResult(boolean hasValue) {
        this.hasValue = hasValue;
    }

    /**
     * Указывает цель/направление потока вычислений
     * @param target Цель/Направление потока вычислений
     */
    public void setExecuteFlowTarget(Target target) {
        this.target = target;
    }

    /**
     * Указывает возвращаемое значение (return/break/continue)
     * @return Возвращаемое значение
     */
    public Value getValue() {
        return value;
    }

    /**
     * Указывает возвращаемое значение (return/break/continue)
     * @param value Возвращаемое значение
     */
    public void setValue(Value value) {
        this.value = value;
        if (value != null) value.setParent(this);
    }

    @Override
    public Object evaluate() {
        return this;
    }

    /**
     * Указывает цель/направление потока вычислений
     * @return Цель/Направление потока вычислений
     */
    @Override
    public Target getExecuteFlowTarget() {
        return target;
    }

    /**
     * Указывает что данная инструкция возвращает значение
     * @return true - возвращает значение; false - не возвращает
     */
    @Override
    public boolean isHasFlowResult() {
        return hasValue;
    }

    @Override
    public Object getFlowResult() {
        Value v = getValue();
        if (v != null) return v.evaluate();
        return null;
    }

    @Override
    public Value deepClone() {
        return new ExecuteFlowValue(this, true);
    }

    @Override
    public Value[] getChildren() {
        return new Value[] { value };
    }

    @Override
    public void setChild(int index, Value tn) {
        if (tn == null) {
            throw new IllegalArgumentException("tn==null");
        }
        if (index != 0) throw new IndexOutOfBoundsException();
        value = tn;
        value.setParent(this);
    }
}
