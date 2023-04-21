package classifiers.command;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.log4j.Logger;

/**
 * @author Maha
 *
 */
public class LibLinearCommandClassifier extends CommandClassifier {

    private static final transient Logger logger = Logger.getLogger(LibLinearCommandClassifier.class);

    Logger logLibLinear = Logger.getLogger("libLinearLogging");

    public LibLinearCommandClassifier() {
        this.TrainCommand = "train ";
        this.TestCommand = "predict  ";
    }

    @Override
    public double CrossValidate(String filename) {
        return 0;
    }

    @Override
    public void SetOptions(String[] names, double[] options) {
    }

    @Override
    public void Test(String filename, String modelfile, String perdictFilename) {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        try {
            String s = this.TestCommand + " " + filename + " " + modelfile + "  " + perdictFilename;
            logger.info(" Testing result for  " + filename);
            logger.info(s);
            p = r.exec(s);
            InputStream in = p.getInputStream();
            OutputStream out = p.getOutputStream();
            InputStream err = p.getErrorStream();
            BufferedReader input = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = input.readLine()) != null) {
                logLibLinear.info(line);
            }
            input.close();
            out.write(4);
        } catch (Exception e) {
            logger.error("error===" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void Train(String filename, String modelfile) {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        try {
            logger.info("--------Training the svm light-------------- with " + filename);
            String s = this.TrainCommand + " " + this.OptionString + "  " + filename + "  " + modelfile;
            logger.info(s);
            p = r.exec(s);
            InputStream in = p.getInputStream();
            OutputStream out = p.getOutputStream();
            InputStream err = p.getErrorStream();
            BufferedReader input = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = input.readLine()) != null) {
                logLibLinear.info(line);
            }
            input.close();
            out.write(4);
            logger.info("---------------finihsed training-------------------- ");
        } catch (Exception e) {
            logger.error("error===" + e.getMessage());
            e.printStackTrace();
        }
    }
}
