package rcscene.tools;

import java.io.*;
import java.util.*;
import rcscene.Action;
import sceneInfo.*;

public class LogToFuzzyScenes extends LogToScenes {

    FuzzySceneLib fsceneLib;

    FuzzyFramework framework;

    float[] angleparameters = new float[] { -88.0f, -27.0f, -9.0f, 0.0f, 3.0f, 13.0f, 29.0f, 88.0f };

    float[] distanceparameters = new float[] { 1.1f, 4.1f, 22.2f, 48.4f, 73.0f };

    public LogToFuzzyScenes(String inLog, String outScene, int numRow, int numCol) {
        super(inLog, outScene, numRow, numCol);
    }

    public LogToFuzzyScenes(String inFile, String outFile, int numRows, int numCols, Vector<Float> angleParameters2, Vector<Float> distanceParameters2) {
        super(inFile, outFile, numRows, numCols);
    }

    public static void main(String[] args) {
        int numRows;
        int numCols;
        int fuzz = 0;
        String inFile, outFile;
        LogToFuzzyScenes lp;
        if (args.length < 2) {
            System.err.println("LogToScenes - converts captured Robocup log files to Scenes");
            System.err.println("\nUsage: java LogToFuzzyScenes in-file out-file [rows] [cols] [bin_count]");
            System.err.println("\nRows - specifies discretization levels for distances - default 3");
            System.err.println("Cols - specifies discretization levels for left/right angles - default 5");
            System.err.println("\nbin_count - bin count not supported just yet");
            System.err.println("\nTwo files will be written: out-file.scene and out-file.text (human-readable)\n");
            return;
        }
        inFile = args[0];
        outFile = args[1];
        try {
            numRows = Integer.parseInt(args[2]);
            numCols = Integer.parseInt(args[3]);
            fuzz = Integer.parseInt(args[4]);
            if (numRows < 1) {
                System.err.println("Error: invalid row size, reverting to default");
                numRows = 3;
            }
            if (numCols < 1) {
                System.err.println("Error: invalid column size, reverting to default");
                numCols = 5;
            }
        } catch (Exception e) {
            System.err.println("Error: unable to parse row/col size, using defaults");
            numRows = 3;
            numCols = 5;
        }
        lp = new LogToFuzzyScenes(inFile, outFile, numRows, numCols);
        lp.parseLog();
        lp.fuzzify(fuzz);
        lp.writeScenes();
    }

    /** create fuzzy scene library and populate it using the crisp scene library
	 * uses default fuzzy framework parameters
	 */
    public void fuzzify(int fuzztype) {
        float[] angleparameters = new float[numCols];
        float[] distanceparameters = new float[numRows];
        if (numCols > 1) {
            for (int i = 0; i < numCols; i++) {
                angleparameters[i] = 180 * i / (numCols - 1) - 90;
            }
        } else {
            angleparameters[0] = 0;
        }
        for (int i = 0; i < numRows; i++) {
            distanceparameters[i] = 16 * i;
        }
        if (fuzztype == 0) {
            framework = new FuzzyFramework(angleparameters, distanceparameters);
        } else {
            framework = new NonFuzzyFramework(angleparameters, distanceparameters);
        }
        fsceneLib = new FuzzySceneLib(sceneLib, framework);
    }

    /**
	 * allows to specify the parameters for the fuzzy framework
	 * @param fuzztype : what kind of fuzzy framework, currently 0 or 1
	 * @param aparams
	 * @param dparams
	 */
    public void fuzzify(int fuzztype, Vector<Float> aparams, Vector<Float> dparams) {
        if (fuzztype == 0) {
            framework = new FuzzyFramework(aparams, dparams);
        } else {
            framework = new NonFuzzyFramework(aparams, dparams);
        }
        fsceneLib = new FuzzySceneLib(sceneLib, framework);
    }

    public void parseLog() {
        int linecount = 0;
        String my_team;
        StringTokenizer m_tokenizer;
        try {
            BufferedReader in = new BufferedReader(new FileReader(logFileName));
            StringBuffer test = new StringBuffer(logFileName.substring(logFileName.lastIndexOf("/") + 1));
            my_team = new String(test);
            try {
                my_team = new String(test.substring(0, test.lastIndexOf("_")));
            } catch (Exception e) {
            }
            System.out.println("Using teamname " + my_team);
            System.out.println("Using table dimensions (" + numRows + ", " + numCols + ")");
            String str;
            String mytoken;
            Scene currentScene = new Scene(numRows, numCols);
            currentScene.setIdentString("0");
            currentScene.setTeamName(my_team);
            int currentTime = 0;
            System.out.println("Reading log data...");
            while ((str = in.readLine()) != null) {
                linecount++;
                str.trim();
                m_tokenizer = new StringTokenizer(str, "() ", true);
                if (m_tokenizer.hasMoreTokens()) {
                    m_tokenizer.nextToken();
                    mytoken = m_tokenizer.nextToken();
                    if (mytoken.equals("init")) {
                        m_tokenizer.nextToken();
                        String myTeamName = "\"" + m_tokenizer.nextToken() + "\"";
                        System.out.println("My team is " + myTeamName);
                        currentScene.setTeamName(myTeamName);
                        my_team = myTeamName;
                    }
                    if (mytoken.equals("sense_body")) {
                        SenseBodyInfo logBody = new SenseBodyInfo(str);
                        if ((int) logBody.time == currentTime) {
                        } else {
                            sceneLib.placeScene(currentScene);
                            currentScene = new Scene(numRows, numCols);
                            currentScene.setTeamName(my_team);
                            currentScene.setIdentString("" + ((int) logBody.time));
                            currentTime = (int) logBody.time;
                        }
                    } else if (mytoken.equals("see")) {
                        VisualInfo logSee = new VisualInfo(str, my_team, true);
                        logSee.parse();
                        if (logSee.getTime() != 0) {
                            if (logSee.getTime() == currentTime) {
                                parseSeeInfo(logSee, numRows, numCols);
                                currentScene.setVision(logSee);
                            } else {
                                sceneLib.placeScene(currentScene);
                                currentScene = new Scene(numRows, numCols);
                                currentScene.setIdentString("" + ((int) logSee.getTime()));
                                currentScene.setTeamName(my_team);
                                currentTime = (int) logSee.getTime();
                                parseSeeInfo(logSee, numRows, numCols);
                                currentScene.setVision(logSee);
                            }
                        }
                    } else if (mytoken.equals("dash")) {
                        Action newDash = new Action(Action.ACTION_DASH);
                        m_tokenizer.nextToken();
                        newDash.setActionPower(Float.valueOf(m_tokenizer.nextToken()).floatValue());
                        m_tokenizer.nextToken();
                        currentScene.addAction(newDash);
                    } else if (mytoken.equals("turn")) {
                        Action newTurn = new Action(Action.ACTION_TURN);
                        m_tokenizer.nextToken();
                        newTurn.setActionDirection(Float.valueOf(m_tokenizer.nextToken()).floatValue());
                        m_tokenizer.nextToken();
                        currentScene.addAction(newTurn);
                    } else if (mytoken.equals("turn_neck")) {
                        Action newTurn = new Action(Action.ACTION_TURNNECK);
                        m_tokenizer.nextToken();
                        newTurn.setActionDirection(Float.valueOf(m_tokenizer.nextToken()).floatValue());
                        m_tokenizer.nextToken();
                        currentScene.addAction(newTurn);
                    } else if (mytoken.equals("kick")) {
                        Action newKick = new Action(Action.ACTION_KICK);
                        m_tokenizer.nextToken();
                        newKick.setActionPower(Float.valueOf(m_tokenizer.nextToken()).floatValue());
                        m_tokenizer.nextToken();
                        newKick.setActionDirection(Float.valueOf(m_tokenizer.nextToken()).floatValue());
                        m_tokenizer.nextToken();
                        currentScene.addAction(newKick);
                    } else if (mytoken.equals("catch")) {
                        currentScene.addAction(new Action(Action.ACTION_CATCH));
                    }
                }
            }
            sceneLib.placeScene(currentScene);
            in.close();
            pruneScenes();
            System.out.println(getPrunedSceneCount() + " scenes were pruned.");
            System.out.println("Done (read " + linecount + " lines).\n");
        } catch (Exception e) {
        }
    }

    void writeScenes() {
        try {
            FileOutputStream fileOut = new FileOutputStream(sceneFileName + ".text");
            OutputStreamWriter outWriter = new OutputStreamWriter(fileOut, "US-ASCII");
            PrintWriter out = new PrintWriter(outWriter);
            System.out.println("Writing Scene Pictures file to " + sceneFileName + ".text");
            out.println("% Robocup Scene pictures generated by LogToScene.java");
            out.println("% Using rowsize=" + numRows + ", colsize=" + numCols);
            Iterator<FuzzyScene> it = fsceneLib.getFuzzySceneList().iterator();
            int[] avg = new int[numRows * numCols];
            for (int j = 0; j < numRows * numCols; j++) {
                avg[j] = 0;
            }
            while (it.hasNext()) {
                FuzzyScene thisScene = it.next();
                out.println("Scene " + thisScene.getFuzzyVInfo().getTime());
                out.println("Team " + thisScene.getTeamName());
                out.println("# of Ball/Teammates/Oppos/Unknown/Goal/Flags/Lines = ");
                int[] numbers = thisScene.getFuzzyVInfo().BigHistogram;
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < numRows * numCols; j++) {
                        out.print(numbers[i * (numRows * numCols) + j] + " ");
                        avg[j] += numbers[i * (numRows * numCols) + j];
                    }
                    out.println();
                }
                out.println("  action:" + thisScene.getActions());
                Iterator actionIt = thisScene.getActions().iterator();
                while (actionIt.hasNext()) {
                    Action action = (Action) actionIt.next();
                    out.println("  actionDirection: " + action.getActionDirection() + "  actionPower: " + action.getActionPower());
                }
                out.println();
            }
            out.close();
            outWriter.close();
            fileOut.close();
            System.out.println("Writing Scene Store file to " + sceneFileName + ".scene");
            System.out.println("average cell fill: ");
            float av;
            for (int j = 0; j < numRows * numCols; j++) {
                av = avg[j] / fsceneLib.getFuzzySceneList().size();
                System.out.print(av + " ");
            }
            fileOut = new FileOutputStream(sceneFileName + ".scene");
            ObjectOutputStream oos = new ObjectOutputStream(fileOut);
            oos.writeObject(new Integer(numRows));
            oos.writeObject(new Integer(numCols));
            oos.writeObject(fsceneLib);
            oos.close();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Wrote " + sceneLib.getSceneList().size() + " scenes.");
    }
}
