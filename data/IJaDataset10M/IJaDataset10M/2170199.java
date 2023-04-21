package ch.superj.functions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author superj
 *
 * tag: 
 */
public class IndexHolder implements IGet<Integer> {

    private Index _index;

    private List<Operation> _stack = new ArrayList<Operation>();

    public IndexHolder(Index index) {
        _index = index;
    }

    public Integer get() {
        int value = _index.get();
        for (Operation op : _stack) {
            value = op.calc(value);
        }
        return value;
    }

    public IndexHolder add(int value) {
        _stack.add(new Add(value));
        return this;
    }

    public IndexHolder sub(int value) {
        _stack.add(new Sub(value));
        return this;
    }

    public IndexHolder mult(int value) {
        _stack.add(new Mult(value));
        return this;
    }

    private static class Add implements Operation {

        private int _value;

        public Add(int value) {
            _value = value;
        }

        public int calc(int target) {
            return target + _value;
        }
    }

    private static class Sub implements Operation {

        private int _value;

        public Sub(int value) {
            _value = value;
        }

        public int calc(int target) {
            return target - _value;
        }
    }

    private static class Mult implements Operation {

        private int _value;

        public Mult(int value) {
            _value = value;
        }

        public int calc(int target) {
            return target * _value;
        }
    }

    private static interface Operation {

        public int calc(int target);
    }
}
