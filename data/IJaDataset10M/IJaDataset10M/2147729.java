package lang2.vm.op;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import lang2.vm.*;

/**
 * Конструктция вызова функции.<br/>
 * Синтаксически так: <code>fun( аргумент1, аргумент2, ... аргументN )</code><br/>
 * В качестве функции могут выступать объекты поддерживающие интефейс Callable, а так же массивы, списки, карты.<br/>
 * В случаи с массивами, картами, списками <br/>
 * <b>первый аргумент</b> - указывает на ключ, по которому извлекается значение и если указан только один аргумент,
 * то он возвращается. Инчае к нему снова происходит обращение такое же, но за вычитом первого аргумента.<br/>
 * <b>Если аргументов нет</b> - то возвращается сам массив/карта/список.
 * @author gocha
 * @see Callable
 */
public class Call extends AbstractTreeNode<Value> implements Value {

    protected Value function = null;

    protected Value[] arguments = null;

    private void assignParent() {
        if (function != null) function.setParent(this);
        if (arguments != null) for (Value a : arguments) if (a != null) a.setParent(this);
    }

    /**
     * Конструктор
     */
    public Call() {
    }

    /**
     * Конструктор копирования
     * @param src Образец для копирования
     */
    public Call(Call src) {
        if (src == null) {
            throw new IllegalArgumentException("src==null");
        }
        function = src.function;
        if (src.arguments != null) arguments = Arrays.copyOf(src.arguments, src.arguments.length);
        assignParent();
    }

    /**
     * Конструктор копирования
     * @param src Образец для копирования
     * @param deep Глубокое копирование
     */
    public Call(Call src, boolean deep) {
        if (src == null) {
            throw new IllegalArgumentException("src==null");
        }
        function = deep && src.function != null ? src.function.deepClone() : src.function;
        if (src.arguments != null) arguments = Arrays.copyOf(src.arguments, src.arguments.length);
        if (deep && arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i] != null) arguments[i] = arguments[i].deepClone();
            }
        }
        assignParent();
    }

    /**
     * Конструктор
     * @param function Функция
     * @param arguments Аргументы функции
     */
    public Call(Value function, Value... arguments) {
        setFunction(function);
        setArguments(arguments);
        assignParent();
    }

    /**
     * Конструктор
     * @param function Функция
     * @param arguments Аргументы функции
     */
    public Call(Value function, Iterable<Value> arguments) {
        setFunction(function);
        if (arguments == null) {
            setArguments(new Value[] {});
        } else {
            ArrayList<Value> l = new ArrayList<Value>();
            for (Value v : arguments) l.add(v);
            setArguments(l.toArray(new Value[] {}));
        }
        assignParent();
    }

    @Override
    public Value[] getChildren() {
        if (arguments == null) throw new IllegalStateException("arguments==null");
        Value[] res = new Value[arguments.length + 1];
        res[0] = function;
        for (int i = 0; i < arguments.length; i++) res[i + 1] = arguments[i];
        return res;
    }

    @Override
    public void setChild(int index, Value tn) {
        if (tn == null) {
            throw new IllegalArgumentException("tn==null");
        }
        if (arguments == null) throw new IllegalStateException("arguments==null");
        if (index < 0 || index > arguments.length) throw new IndexOutOfBoundsException();
        if (index == 0) function = tn; else arguments[index - 1] = tn;
        tn.setParent(this);
    }

    /**
     * Указывает функцию
     * @return Функция
     */
    public Value getFunction() {
        return function;
    }

    /**
     * Указывает функцию
     * @param function Функция
     */
    public void setFunction(Value function) {
        this.function = function;
        if (function != null) function.setParent(this);
    }

    /**
     * Добавляет аргумент
     * @param argument аргумент
     */
    public void addArgument(Value argument) {
        if (argument == null) return;
        if (arguments == null) {
            arguments = new Value[] { argument };
            return;
        }
        arguments = Arrays.copyOf(arguments, arguments.length + 1);
        arguments[arguments.length - 1] = argument;
        argument.setParent(this);
    }

    /**
     * Указывает аргументы функции
     * @return Аргументы функции
     */
    public Value[] getArguments() {
        return arguments;
    }

    /**
     * Указывает аргументы функции
     * @param arguments Аргументы функции
     */
    public void setArguments(Value[] arguments) {
        this.arguments = arguments;
        assignParent();
    }

    /**
     * Вызывает указанную функцию и возвращает значение вызова
     * @return значение вызова функции
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object evaluate() {
        if (function == null) {
            Logger.getLogger(Block.class.getName()).severe("function not set");
            throw new CompileException(this, "function==null");
        }
        Object f = function.evaluate();
        if (f == null) {
            Logger.getLogger(Call.class.getName()).severe("can't call null");
            throw new NullRefError(this, "function is null");
        }
        if (f instanceof Callable) return evalCallable(f);
        if (f.getClass().isArray()) return evalArray(f);
        if (f instanceof List) return evalList(f);
        if (f instanceof Map) return evalMap(f);
        Logger.getLogger(Call.class.getName()).fine("can't call " + f.getClass().getName());
        throw new CastError(this, "can't call " + f.getClass().getName());
    }

    /**
     * Вызывает указанную функцию
     * @param f Функция
     * @return Значение вызова (возможно null)
     */
    protected Object evalCallable(Object f) {
        if (arguments == null) {
            Logger.getLogger(Call.class.getName()).severe("can't call " + f.getClass().getName() + " arguments is null (vm error)");
            throw new CompileException(this, "arguments==null");
        }
        Object[] args = new Object[arguments.length];
        for (int i = 0; i < args.length; i++) {
            Value argument = arguments[i];
            if (argument == null) {
                Logger.getLogger(Call.class.getName()).severe("can't call " + f.getClass().getName() + " argument[" + i + "] is null (vm error)");
                throw new CompileException(this, "arguments[" + i + "]==null");
            }
            args[i] = argument.evaluate();
        }
        return ((Callable) f).call(args);
    }

    /**
     * Осушествляет доступ к элементу карты
     * @param f Карта (java.util.Map)
     * @return Значение (возможно null)
     */
    protected Object evalMap(Object f) {
        if (arguments == null || arguments.length == 0) {
            return f;
        }
        if (arguments[0] == null) {
            String message = "can't call " + f.getClass().getName() + " argument[0] is null (vm error)";
            Logger.getLogger(Call.class.getName()).severe(message);
            throw new CompileException(this, message);
        }
        ArrayIndex _ai = new ArrayIndex(new Const(f), arguments[0]);
        Object _v = _ai.evaluate();
        if (arguments.length == 1) {
            return _v;
        } else {
            Call c = new Call();
            c.setFunction(new Const(_v));
            Value[] args = Arrays.copyOfRange(arguments, 1, arguments.length);
            c.setArguments(args);
            return c.evaluate();
        }
    }

    /**
     * Осушествляет доступ к элементу списка
     * @param f Список (java.util.Map)
     * @return Значение (возможно null)
     */
    @SuppressWarnings("unchecked")
    protected Object evalList(Object f) {
        if (arguments == null || arguments.length == 0) {
            return f;
        }
        if (arguments[0] == null) {
            String message = "can't call " + f.getClass().getName() + " argument[0] is null (vm error)";
            Logger.getLogger(Call.class.getName()).severe(message);
            throw new CompileException(this, message);
        }
        ArrayIndex _ai = new ArrayIndex(new Const(f), arguments[0]);
        Object _v = _ai.evaluate();
        if (arguments.length == 1) {
            return _v;
        } else {
            Call c = new Call();
            c.setFunction(new Const(_v));
            Value[] args = Arrays.copyOfRange(arguments, 1, arguments.length);
            c.setArguments(args);
            return c.evaluate();
        }
    }

    /**
     * Осушествляет доступ к элементу массива
     * @param f Массив
     * @return Значение (возможно null)
     */
    protected Object evalArray(Object f) {
        if (arguments == null || arguments.length == 0) {
            return f;
        }
        if (arguments[0] == null) {
            String message = "can't call " + f.getClass().getName() + " argument[0] is null (vm error)";
            Logger.getLogger(Call.class.getName()).severe(message);
            throw new CompileException(this, message);
        }
        ArrayIndex _ai = new ArrayIndex(new Const(f), arguments[0]);
        Object _v = _ai.evaluate();
        if (arguments.length == 1) {
            return _v;
        } else {
            Call c = new Call();
            c.setFunction(new Const(_v));
            Value[] args = Arrays.copyOfRange(arguments, 1, arguments.length);
            c.setArguments(args);
            return c.evaluate();
        }
    }

    @Override
    public Value deepClone() {
        return new Call(this, true);
    }
}
