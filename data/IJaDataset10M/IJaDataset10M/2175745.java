package ru.sgu.diploma.practic;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.sgu.diploma.practic.domain.model.Automat;
import ru.sgu.diploma.practic.domain.model.AutomatResult;
import ru.sgu.diploma.practic.domain.model.GeometricModel;
import ru.sgu.diploma.practic.domain.model.Point;
import ru.sgu.diploma.practic.errors.AlphabetExcephion;
import ru.sgu.diploma.practic.errors.FatalException;
import ru.sgu.diploma.practic.errors.NextLetterException;
import ru.sgu.diploma.practic.errors.NoStartStateException;
import ru.sgu.diploma.practic.service.AutomatGenerator;
import ru.sgu.diploma.practic.service.AutomatResultConverter;
import ru.sgu.diploma.practic.service.AutomatRunner;
import ru.sgu.diploma.practic.service.ImageConstructor;
import ru.sgu.diploma.practic.service.impl.AutomatGeneratorImpl;
import ru.sgu.diploma.practic.service.impl.AutomatResultConverterImpl;
import ru.sgu.diploma.practic.service.impl.AutomatRunnerImpl;
import ru.sgu.diploma.practic.service.impl.ImageConstructorImpl;
import ru.sgu.diploma.practic.utils.GeometricModelUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

@Deprecated
public class App {

    static Logger logger = Logger.getLogger("modelGen");

    static String helpMessage = "Geometric model generator of DDA. \n" + "necessary parameters: \n" + "\tpath/to/the/input/file.csv\n" + "\n" + "\texample of the input file: \n" + "\tstartstate;s1\n" + "\talphabet;a;b\n" + "\ta;s1;s2;b\n" + "\tb;s1;s1;b\n" + "\ta;s2;s2;b\n" + "\tb;s2;s1;a\n" + "\n" + "unnecessary parameters: \n" + "\t -w NUMBER : specify the word size for input [DEFAULT: 4]\n" + "\t -l INFO/DEBUG/ERROR : specify logging level. [DEFAULT: INFO]\n" + "\t -s NUMBER : specify scaling factor. [DEFAULT: 2]\n";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.print(helpMessage);
            System.exit(0);
        }
        File file = new File(args[0]);
        if (!file.exists() || !file.isFile()) {
            System.out.print("file " + args[0] + " - not found.");
            System.exit(0);
        }
        String loggingLevel = "INFO";
        Integer wordSize = 4;
        Integer scale = 2;
        int options = 1;
        while (options < args.length) {
            String flag = args[options];
            if ((options + 1) >= args.length) {
                System.out.print("please specify parameter for " + flag + " option");
                System.exit(0);
            } else {
                if (flag.equals("-l")) loggingLevel = args[options + 1]; else if (flag.equals("-w")) {
                    String wordSizeStr = args[options + 1];
                    try {
                        wordSize = Integer.parseInt(wordSizeStr);
                    } catch (Exception e) {
                        System.out.print("please specify correct number for " + flag + " option");
                        System.exit(0);
                    }
                } else if (flag.equals("-s")) {
                    String scaleStr = args[options + 1];
                    try {
                        scale = Integer.parseInt(scaleStr);
                    } catch (Exception e) {
                        System.out.print("please specify correct number for " + flag + " option");
                        System.exit(0);
                    }
                }
            }
            options = options + 2;
        }
        if (loggingLevel.equals("INFO")) logger.getLoggerRepository().getRootLogger().setLevel(Level.INFO); else if (loggingLevel.equals("DEBUG")) logger.getLoggerRepository().getRootLogger().setLevel(Level.DEBUG); else if (loggingLevel.equals("ERROR")) logger.getLoggerRepository().getRootLogger().setLevel(Level.ERROR); else {
            System.out.print("wrong value " + loggingLevel + " for -l parameter. \n" + "only DEBUG/INFO/ERROR can be used.\n");
            System.exit(0);
        }
        logger.info("trying open file " + file.getPath() + "\n");
        String inputSettings = "";
        try {
            inputSettings = FileUtils.readFileToString(file);
        } catch (IOException e) {
            System.out.print("fatal error: " + e.getMessage());
            System.exit(0);
        }
        if (StringUtils.isEmpty(inputSettings)) {
            System.out.print("file " + file.getAbsolutePath() + " is empty");
            System.exit(0);
        }
        logger.info("file has been parsed successfully.\n");
        logger.info("trying to generate an automate.\n");
        AutomatGenerator automatGenerator = new AutomatGeneratorImpl();
        Automat automat = null;
        try {
            automat = automatGenerator.generateAutomatFromFile(new BufferedReader(new FileReader(file)));
        } catch (FileNotFoundException e) {
            System.out.print("file " + file.getAbsolutePath() + " not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.print("file " + file.getAbsolutePath() + " i/o error");
            System.exit(0);
        } catch (NoStartStateException e) {
            System.out.print("please, specify start state in input file");
            System.exit(0);
        } catch (AlphabetExcephion e) {
            System.out.print("error in alphabet declaration: " + e.getMessage());
            System.exit(0);
        }
        if (automat == null) {
            System.out.print("fatal error: automat can not be initializated");
            System.exit(0);
        }
        logger.info("automate has been generated successfully.\n");
        logger.info("trying to translate some input values.\n");
        AutomatRunner automatRunner = new AutomatRunnerImpl();
        automatRunner.setLength(wordSize);
        List<AutomatResult> automatResults = null;
        try {
            automatResults = automatRunner.run(automat);
        } catch (FatalException e) {
            System.out.print("fatal error: " + e.getMessage());
            System.exit(0);
        }
        if (automatResults == null) {
            System.out.print("fatal error: automat not properly prepeared");
            System.exit(0);
        }
        logger.info("automate has been launched successfully.\n");
        logger.info("trying to collect output values.\n");
        AutomatResultConverter automatResultConverter = new AutomatResultConverterImpl();
        List<Point> pointList = null;
        try {
            pointList = automatResultConverter.convert(automatResults, automat.getAlphabet());
        } catch (NextLetterException e) {
            System.out.print("fatal error: " + e.getMessage());
            System.exit(0);
        }
        if (pointList == null) {
            System.out.print("fatal error: point list has not been successful initializated");
            System.exit(0);
        }
        logger.info("ouput values has been collected successfully.\n");
        logger.info("trying to construct geometric model.\n");
        ImageConstructor imageConstructor = new ImageConstructorImpl();
        GeometricModel geometricModel = new GeometricModel();
        geometricModel.setPointList(pointList);
        geometricModel.setImageResolution(GeometricModelUtils.calculateResolution(geometricModel));
        BufferedImage bufferedImage = imageConstructor.constructWithScale(geometricModel, scale);
        logger.info("geometric model has been constructed successfully.\n");
        logger.info("trying to save the result.\n");
        bufferedImage = GeometricModelUtils.verticalflip(bufferedImage);
        File outputfile = new File(file.getName() + ".png");
        try {
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException e) {
            System.out.print("fatal error while saving result: " + e.getMessage());
            System.exit(0);
        }
        logger.info("the result has been saved successfully to " + outputfile.getAbsolutePath() + "\n");
        System.exit(0);
    }
}
