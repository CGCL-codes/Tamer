package gelations;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Runnable class that is responsible for creating and 
 * serializing a Configuration object
 * from a set of command-line arguments. Some kind of script to 
 * execute this class with 
 * multiple sets of command-line arguments would be useful if 
 * one wants to create multiple
 * different configurations.
 * 
 * @author conrada
 *
 */
public class MakeConfiguration {

    public static final int TOTAL_ARGS = 17;

    public static final String CALL = "int data_format  " + "int mutation_operator  int crossover_operator  " + "int selection_method  int fitness_transform  " + "double mutation_rate  double child_representation  " + "int pop_size  int max_time  double target_fitness  " + "int max_stagnancy  int metric  " + "String coverage_data_file  String timing_data_file  " + "String seed_data_file  String output_data_file  " + "int number_of_repetitions";

    public static void main(String[] args) {
        if (args.length < TOTAL_ARGS) {
            System.out.println("Incorrect arguments provided. " + "Proper arguments are:");
            System.out.println(" " + CALL);
            System.exit(1);
        }
        Configuration config = new Configuration(args);
        String filename = config.getObjectName();
        serialize(filename, config);
    }

    public static boolean serialize(String filename, Configuration config, String filepath) {
        try {
            FileOutputStream fos = new FileOutputStream(filepath + filename);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(config);
            out.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean serialize(String filename, Configuration config) {
        return serialize(filename, config, "/home/cs2/conrada/Catterwampus/configs/");
    }
}
