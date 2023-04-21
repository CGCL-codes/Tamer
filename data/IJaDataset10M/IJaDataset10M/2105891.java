package com.db4o.internal.activation;

public interface FixedDepth<T extends FixedDepth> {

    T adjustDepthToBorders();
}
