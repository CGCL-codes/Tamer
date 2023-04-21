package org.jalgo.module.am1simulator.model.am1;

import org.jalgo.module.am1simulator.model.LineAddress;
import org.jalgo.module.am1simulator.model.am1.StatementResource;
import org.jalgo.module.am1simulator.model.am1.machine.MachineConfiguration;

/**
 * Statement factory for all arithmetic statements.
 * 
 * @author Max Leuth&auml;user
 */
public class ArithmeticStatementFactory implements AbstractStatementFactory {

    /**
	 * Updates the AM after calling ADD.
	 * 
	 * @author Max Leuth&auml;user
	 */
    private class Add extends SimulationStatement {

        public Add(LineAddress address) {
            super(address);
        }

        @Override
        public MachineConfiguration apply(MachineConfiguration configuration) throws IllegalArgumentException {
            if (configuration.getStack().getStackAsList().size() < 2) {
                throw new IllegalArgumentException("Stack does not contain 2 or more values.");
            }
            configuration.getProgramCounter().inc();
            int arg0 = configuration.getStack().pop();
            int arg1 = configuration.getStack().pop();
            configuration.getStack().push(arg1 + arg0);
            return configuration;
        }

        @Override
        public String getDescription() {
            return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n ≥ 2 then (<i>m</i>+1,(<i>d</i>.2+<i>d</i>.1):<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)";
        }

        @Override
        public String getCodeText() {
            return "ADD;";
        }
    }

    /**
	 * Updates the AM after calling DIV.
	 * 
	 * @author Max Leuth&auml;user
	 */
    private class Div extends SimulationStatement {

        public Div(LineAddress address) {
            super(address);
        }

        @Override
        public MachineConfiguration apply(MachineConfiguration configuration) throws IllegalArgumentException, ArithmeticException {
            if (configuration.getStack().getStackAsList().size() < 2) {
                throw new IllegalArgumentException("Stack does not contain 2 or more values.");
            }
            configuration.getProgramCounter().inc();
            int arg0 = configuration.getStack().pop();
            int arg1 = configuration.getStack().pop();
            if (arg0 == 0) {
                throw new ArithmeticException("Division by zero.");
            }
            configuration.getStack().push(arg1 / arg0);
            return configuration;
        }

        @Override
        public String getDescription() {
            return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n ≥ 2 then (<i>m</i>+1,(<i>d</i>.2/<i>d</i>.1):<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)";
        }

        @Override
        public String getCodeText() {
            return "DIV;";
        }
    }

    /**
	 * Updates the AM after calling MOD.
	 * 
	 * @author Max Leuth&auml;user
	 */
    private class Mod extends SimulationStatement {

        public Mod(LineAddress address) {
            super(address);
        }

        @Override
        public MachineConfiguration apply(MachineConfiguration configuration) throws IllegalArgumentException {
            if (configuration.getStack().getStackAsList().size() < 2) {
                throw new IllegalArgumentException("Stack does not contain 2 or more values.");
            }
            configuration.getProgramCounter().inc();
            int arg0 = configuration.getStack().pop();
            int arg1 = configuration.getStack().pop();
            configuration.getStack().push(arg1 % arg0);
            return configuration;
        }

        @Override
        public String getDescription() {
            return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n ≥ 2 then (<i>m</i>+1,(<i>d</i>.2%<i></i>.1):<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)";
        }

        @Override
        public String getCodeText() {
            return "MOD;";
        }
    }

    /**
	 * Updates the AM after calling MUL.
	 * 
	 * @author Max Leuth&auml;user
	 */
    private class Mul extends SimulationStatement {

        public Mul(LineAddress address) {
            super(address);
        }

        @Override
        public MachineConfiguration apply(MachineConfiguration configuration) throws IllegalArgumentException {
            if (configuration.getStack().getStackAsList().size() < 2) {
                throw new IllegalArgumentException("Stack does not contain 2 or more values.");
            }
            configuration.getProgramCounter().inc();
            int arg0 = configuration.getStack().pop();
            int arg1 = configuration.getStack().pop();
            configuration.getStack().push(arg1 * arg0);
            return configuration;
        }

        @Override
        public String getDescription() {
            return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n ≥ 2 then (<i>m</i>+1,(<i>d</i>.2*<i>d</i>.1):<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)";
        }

        @Override
        public String getCodeText() {
            return "MUL;";
        }
    }

    /**
	 * Updates the AM after calling SUB.
	 * 
	 * @author Max Leuth&auml;user
	 */
    private class Sub extends SimulationStatement {

        public Sub(LineAddress address) {
            super(address);
        }

        @Override
        public MachineConfiguration apply(MachineConfiguration configuration) throws IllegalArgumentException {
            if (configuration.getStack().getStackAsList().size() < 2) {
                throw new IllegalArgumentException("Stack does not contain 2 or more values.");
            }
            configuration.getProgramCounter().inc();
            int arg0 = configuration.getStack().pop();
            int arg1 = configuration.getStack().pop();
            configuration.getStack().push(arg1 - arg0);
            return configuration;
        }

        @Override
        public String getDescription() {
            return "if <i>d</i>=<i>d</i>.1:<i>d</i>.2:<i>d</i>.3: ... :<i>d</i>.n with n ≥ 2 then (<i>m</i>+1,(<i>d</i>.2-<i>d</i>.1):<i>d</i>.3: ... :<i>d</i>.n,<i>h</i>, <i>inp</i>, <i>out</i>)";
        }

        @Override
        public String getCodeText() {
            return "SUB;";
        }
    }

    @Override
    public SimulationStatement newStatement(Statement statement, StatementResource resource) {
        LineAddress address = resource.getAddress();
        switch(statement) {
            case ADD:
                return new Add(address);
            case DIV:
                return new Div(address);
            case MOD:
                return new Mod(address);
            case MUL:
                return new Mul(address);
            case SUB:
                return new Sub(address);
            default:
                throw new AssertionError("Unknown statement type:" + statement);
        }
    }
}
