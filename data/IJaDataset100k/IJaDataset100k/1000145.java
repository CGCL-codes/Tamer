package com.digiburo.backprop1b.demo3;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import com.digiburo.backprop1b.network.BackProp;

public class BpDemo3 extends BackProp {

    /**
     * Constructor for new backpropagation network.
     *
     * @param input_population input node count
     * @param middle_population middle node count
     * @param output_population output node count
     * @param learning_rate 
     * @param momentum
     */
    public BpDemo3(int input_population, int middle_population, int output_population, double learning_rate, double momentum) {
        super(input_population, middle_population, output_population, learning_rate, momentum);
    }

    /**
     * Constructor for existing backpropagation network.
     *
     * @param file serialized Network memento
     */
    public BpDemo3(File file) throws IOException, FileNotFoundException, ClassNotFoundException {
        super(file);
    }
}
