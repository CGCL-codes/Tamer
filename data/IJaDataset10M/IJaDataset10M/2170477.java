package org.neuroph.samples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.io.FileInputAdapter;
import org.neuroph.util.io.FileOutputAdapter;
import org.neuroph.util.io.IOHelper;
import org.neuroph.util.io.InputAdapter;
import org.neuroph.util.io.OutputAdapter;

/**
 * This sample shows how to read from and write to external data sources using 
 * Neuroph file IO adapters.
 * 
 * Note: Just make sure you specify right file names/path below, when you run 
 * this example, otherwise you'll get FileNotFoundException.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 * @see InputAdapter
 * @see OutputAdapter
 * @see IOHelper
 */
public class FileIOSample {

    /**
     * Runs this sample
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(2, 3, 1);
        String inputFileName = FileIOSample.class.getResource("data/xor_data.txt").getFile();
        FileInputAdapter fileIn = new FileInputAdapter(inputFileName);
        FileOutputAdapter fileOut = new FileOutputAdapter("some_output_file.txt");
        double[] input;
        while ((input = fileIn.readInput()) != null) {
            neuralNet.setInput(input);
            neuralNet.calculate();
            double[] output = neuralNet.getOutput();
            fileOut.writeOutput(output);
        }
        fileIn.close();
        fileOut.close();
    }
}
