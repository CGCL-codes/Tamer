package com.chronomus.workflow.execution.expressions.primitives;

public interface Primitive {

    Primitive NULL = new Primitive() {

        @Override
        public String evaluate() {
            return null;
        }

        @Override
        public String toString() {
            return "NULL";
        }
    };

    String evaluate();
}
