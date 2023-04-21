package org.eoti.ai.genetic.gp.example1;

import org.eoti.ai.genetic.gp.Criteria;
import org.eoti.ai.genetic.gp.Function;
import org.eoti.ai.genetic.gp.ProgramException;
import org.eoti.ai.genetic.gp.Node;
import java.math.BigInteger;

/**
 * The MIT License
 * <p/>
 * Copyright (c) 2009 Malachi de AElfweald (malachid@gmail.com)
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class Divide extends Function<BigInteger, BigInteger> {

    public Divide() {
        super();
    }

    @Override
    public String getDisplayString() {
        return "/";
    }

    @Override
    public Divide newInstance() {
        return new Divide();
    }

    @Override
    public boolean isPopulated() {
        return size() == 2;
    }

    @Override
    public void autogenerate(Criteria<BigInteger, BigInteger> criteria) {
        while (size() < 2) {
            Node<BigInteger, BigInteger> node = criteria.getRandomNode();
            if (!node.isPopulated()) node.autogenerate(criteria);
            add(node);
        }
    }

    public BigInteger execute() throws ProgramException {
        if (super.size() != 2) throw new ProgramException("Divide requires 2 parameters, but have %d", super.size());
        BigInteger left = get(0).execute();
        BigInteger right = get(1).execute();
        if (right.equals(BigInteger.ZERO)) throw new ProgramException("Divide/0 not allowed");
        return left.divide(right);
    }
}
