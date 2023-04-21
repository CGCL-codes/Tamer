package edu.ucla.stat.SOCR.analyses.command.volume;

import java.io.*;
import java.util.*;
import edu.ucla.stat.SOCR.analyses.data.Data;
import edu.ucla.stat.SOCR.analyses.data.DataType;
import edu.ucla.stat.SOCR.analyses.model.AnalysisType;
import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;

public class Volume_2IndepSample_T_test {

    private static final String MISSING_MARK = ".";

    static FileInputStream[] fi_Grp1, fi_Grp2;

    static BufferedInputStream[] bi_Grp1, bi_Grp2;

    static DataInputStream[] in_Grp1, in_Grp2;

    static FileOutputStream pFOS, tFOS;

    static BufferedOutputStream pBOS, tBOS;

    static DataOutputStream pDataOS, tDataOS;

    static boolean byteswap = false;

    static String regressors;

    static String[] regressorLabels = new String[2];

    static boolean outputPValuesVolumes = false;

    static boolean outputTStatVolumes = false;

    public static void main(String[] args) {
        String designMatrixInputFile = null;
        String maskInputVolume = null;
        String pValue_Filename = null;
        String tStat_Filename = null;
        regressors = "";
        regressorLabels[0] = "";
        regressorLabels[1] = "";
        int regressorColumnIndex = 2;
        int[] dim = new int[3];
        boolean[][][] maskVolumeBoolean = new boolean[1][1][1];
        maskVolumeBoolean[0][0][0] = false;
        int data_type = 4;
        Vector<String> inputGrp1AnalyzeFilenames = new Vector<String>();
        Vector<String> inputGrp2AnalyzeFilenames = new Vector<String>();
        String regressorString = "";
        boolean header = false;
        boolean filesLoaded = false;
        int bufferSize = 1;
        int i, j, k;
        System.out.println("Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
        try {
            for (i = 0; i < args.length; i++) {
                if (args[i].compareToIgnoreCase("-h") == 0) header = true; else if (args[i].compareToIgnoreCase("-data_type") == 0) {
                    data_type = Integer.parseInt(args[++i]);
                    if (data_type != 0 && data_type != 1 && data_type != 2 && data_type != 3 && data_type != 4) {
                        System.out.println("Data Type must be 0(unsigned byte volume), 1(signed byte volume), " + "2(unsigned short int) 3(signed short int) or 4(float volume)!\n");
                        System.exit(1);
                    }
                } else if (args[i].compareToIgnoreCase("-dim") == 0) {
                    dim[2] = Integer.parseInt(args[++i]);
                    dim[1] = Integer.parseInt(args[++i]);
                    dim[0] = Integer.parseInt(args[++i]);
                    bufferSize = dim[0] * dim[1];
                    System.out.println("dim[2]=" + dim[2] + "\tdim[1]=" + dim[1] + "\tdim[0]=" + dim[0] + "\tbufferSize=" + bufferSize);
                } else if (args[i].compareToIgnoreCase("-p") == 0) {
                    outputPValuesVolumes = true;
                    pValue_Filename = args[++i];
                    System.out.println("pValue_Filename=" + pValue_Filename);
                } else if (args[i].compareToIgnoreCase("-byteswap") == 0) {
                    byteswap = true;
                    System.out.println("Byteswap=" + byteswap);
                } else if (args[i].compareToIgnoreCase("-t") == 0) {
                    outputTStatVolumes = true;
                    tStat_Filename = args[++i];
                    System.out.println("tStat_Filename=" + tStat_Filename);
                } else if (args[i].compareToIgnoreCase("-regressors") == 0) {
                    regressorString = args[++i];
                    System.err.println("regressorString=" + regressorString);
                    StringTokenizer Tok = new StringTokenizer(regressorString, "=");
                    regressors = "";
                    regressors = Tok.nextToken();
                    String allRegressorLabels = Tok.nextToken();
                    try {
                        StringTokenizer Tok1 = new StringTokenizer(allRegressorLabels, ",");
                        regressorLabels[0] = Tok1.nextToken();
                        regressorLabels[1] = Tok1.nextToken();
                        System.out.println("regressorColumnName=" + regressors + "\t Group1Label=" + regressorLabels[0] + "\t Group2Label=" + regressorLabels[1]);
                    } catch (Exception e) {
                        System.out.println("Regressor entry syntax incorrect. Please see online docs: \n" + "http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
                        return;
                    }
                } else if (args[i].compareToIgnoreCase("-dm") == 0) {
                    designMatrixInputFile = args[++i];
                    filesLoaded = true;
                    System.out.println("designMatrixInputFile=" + designMatrixInputFile);
                } else if (args[i].compareToIgnoreCase("-help") == 0) {
                    System.out.println("Please see the Web syntax for involking this tool: \n" + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
                    return;
                } else if (args[i].compareToIgnoreCase("-mask") == 0) {
                    maskInputVolume = args[++i];
                    System.out.println("maskInputVolume=" + maskInputVolume);
                }
            }
        } catch (Exception e) {
            System.out.println("Incorrect call 1! Please see the syntax for involking this tool\n" + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
        }
        if (!filesLoaded) {
            System.out.println("Incorrect call 2! Please see the syntax for involking this tool\n" + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
            return;
        }
        int independentLength = 1;
        if (independentLength < 1) {
            System.out.println("No Independent Variable Specified (independentLength=" + independentLength + ")\n Cannot run the regression\n" + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
            return;
        }
        TwoIndependentTResult result = null;
        StringTokenizer st = null;
        int designMatrixNumberColumns = 1000;
        String[] input = new String[independentLength];
        int xLengthGrp1 = 0, xLengthGrp2 = 0;
        String[] varHeader = new String[1];
        String line = null, stringTmp;
        boolean read = true;
        if (header) read = false;
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(designMatrixInputFile));
            while ((line = bReader.readLine()) != null) {
                st = new StringTokenizer(line, ",; \t");
                if (varHeader[0] == null && header) {
                    int n = 0;
                    while (st.hasMoreElements()) {
                        n++;
                        st.nextToken();
                    }
                    designMatrixNumberColumns = n;
                    System.err.println("Reading DM Header!\t designMatrixNumberColumns=" + designMatrixNumberColumns);
                    st = new StringTokenizer(line, ",; \t");
                    varHeader = new String[n];
                    for (k = 0; k < designMatrixNumberColumns; k++) {
                        varHeader[k] = st.nextToken().trim();
                        if (varHeader[k].compareToIgnoreCase(regressors) == 0) {
                            regressorColumnIndex = k;
                            System.err.println("varHeader[" + k + "]=" + varHeader[k] + "\t regressorColumnIndex =" + k);
                        }
                    }
                    read = true;
                } else if (!header) {
                    System.out.println("No Independent Variable Specified 1. Cannot run the regression\n" + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
                    return;
                } else {
                    try {
                        int n = 0;
                        int m = 0;
                        while (st.hasMoreElements()) {
                            stringTmp = st.nextToken().trim();
                            if (n == 1) {
                                inputGrp1AnalyzeFilenames.addElement(stringTmp);
                                inputGrp2AnalyzeFilenames.addElement(stringTmp);
                            } else if (n == regressorColumnIndex) {
                                input[m] = stringTmp;
                                if (!input[m].equalsIgnoreCase(regressorLabels[0])) inputGrp1AnalyzeFilenames.removeElementAt(inputGrp1AnalyzeFilenames.size() - 1); else System.out.println("ADDED NEW Group-1 INPUT FILE: inputGrp1AnalyzeFilenames[" + inputGrp1AnalyzeFilenames.size() + "]=" + inputGrp1AnalyzeFilenames.get(inputGrp1AnalyzeFilenames.size() - 1));
                                if (!input[m].equalsIgnoreCase(regressorLabels[1])) inputGrp2AnalyzeFilenames.removeElementAt(inputGrp2AnalyzeFilenames.size() - 1); else System.out.println("ADDED NEW Group-2 INPUT FILE: inputGrp2AnalyzeFilenames[" + inputGrp2AnalyzeFilenames.size() + "]=" + inputGrp2AnalyzeFilenames.get(inputGrp2AnalyzeFilenames.size() - 1));
                                System.out.println("Check: input[m]=" + input[m] + " Group1_Label=" + regressorLabels[0] + "; Group2_Label=" + regressorLabels[1]);
                                System.out.println("inputGrp1AnalyzeFilenames.size()=" + inputGrp1AnalyzeFilenames.size() + "; inputGrp2AnalyzeFilenames.size()=" + inputGrp2AnalyzeFilenames.size() + "\tcolumn=" + n + "\t input[" + m + "]=" + input[m]);
                                m++;
                            }
                            ++n;
                        }
                        read = true;
                    } catch (NoSuchElementException e) {
                        System.out.println("Volume 1-Sample T-test Analysis: 1");
                        return;
                    } catch (Exception e) {
                        System.out.println("Volume 1-Sample T-test  Analysis: 2");
                        return;
                    }
                }
            }
        } catch (Exception e) {
        }
        if (!header) {
            System.out.println("No Independent Variable Specified in Design-Matrix. Cannot run the analysis\n" + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
            return;
        }
        xLengthGrp1 = inputGrp1AnalyzeFilenames.size();
        xLengthGrp2 = inputGrp2AnalyzeFilenames.size();
        System.out.println("Number of volumes (meeting the paired criteria: variable=" + regressors + "; Group_1(" + regressorLabels[0] + ")= " + xLengthGrp1 + "; Group_2(" + regressorLabels[1] + ")= " + xLengthGrp2);
        double[] yDataGrp1 = new double[xLengthGrp1];
        double[] yDataGrp2 = new double[xLengthGrp2];
        fi_Grp1 = new FileInputStream[xLengthGrp1];
        bi_Grp1 = new BufferedInputStream[xLengthGrp1];
        in_Grp1 = new DataInputStream[xLengthGrp1];
        fi_Grp2 = new FileInputStream[xLengthGrp2];
        bi_Grp2 = new BufferedInputStream[xLengthGrp2];
        in_Grp2 = new DataInputStream[xLengthGrp2];
        for (i = 0; i < xLengthGrp1; i++) {
            try {
                fi_Grp1[i] = new FileInputStream(inputGrp1AnalyzeFilenames.elementAt(i));
                bi_Grp1[i] = new BufferedInputStream(fi_Grp1[i], bufferSize);
                in_Grp1[i] = new DataInputStream(bi_Grp1[i]);
            } catch (FileNotFoundException e) {
                System.err.println(e);
            }
        }
        for (i = 0; i < xLengthGrp2; i++) {
            try {
                fi_Grp2[i] = new FileInputStream(inputGrp2AnalyzeFilenames.elementAt(i));
                bi_Grp2[i] = new BufferedInputStream(fi_Grp2[i], bufferSize);
                in_Grp2[i] = new DataInputStream(bi_Grp2[i]);
            } catch (FileNotFoundException e) {
                System.err.println(e);
            }
        }
        System.err.println("Done opening INPUT volume streams!");
        if (maskInputVolume != null) {
            try {
                maskVolumeBoolean = new boolean[dim[2]][dim[1]][dim[0]];
                FileInputStream fis = new FileInputStream(maskInputVolume);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                int tmpByte;
                for (i = 0; i < dim[2]; i++) {
                    for (j = 0; j < dim[1]; j++) {
                        for (k = 0; k < dim[0]; k++) {
                            try {
                                if (byteswap == false) tmpByte = dis.readUnsignedByte(); else tmpByte = swap((byte) dis.readUnsignedByte());
                                if (tmpByte > 0) maskVolumeBoolean[i][j][k] = true; else maskVolumeBoolean[i][j][k] = false;
                            } catch (IOException e) {
                                maskVolumeBoolean[i][j][k] = false;
                                System.err.println("Exception: maskVolumeBoolean[" + i + "][" + j + "][" + k + "] = false\n");
                            }
                        }
                    }
                }
                dis.close();
                bis.close();
                fis.close();
            } catch (IOException e) {
                System.err.println("Exception: Can't open the mask input volume: " + maskInputVolume);
            }
        }
        Data data;
        float tStat, pValue;
        try {
            if (outputPValuesVolumes == true) {
                pFOS = new FileOutputStream(pValue_Filename + "_Reg_" + regressors + "_" + regressorLabels[0] + "_" + regressorLabels[1] + ".img");
                pBOS = new BufferedOutputStream(pFOS, bufferSize);
                pDataOS = new DataOutputStream(pBOS);
            }
            if (outputTStatVolumes) {
                tFOS = new FileOutputStream(tStat_Filename + "_TStat_" + regressors + "_" + regressorLabels[0] + "_" + regressorLabels[1] + ".img");
                tBOS = new BufferedOutputStream(tFOS, bufferSize);
                tDataOS = new DataOutputStream(tBOS);
            }
            System.err.println("Done opening OUTPUT result streams!");
            System.err.println("Beginning the stat analyses ... ");
            for (i = 0; i < dim[2]; i++) {
                for (j = 0; j < dim[1]; j++) {
                    for (k = 0; k < dim[0]; k++) {
                        if (maskInputVolume == null || (maskInputVolume != null && maskVolumeBoolean[i][j][k] == true)) {
                            data = new Data();
                            yDataGrp1 = getNextInputDataBuffer(data_type, 0, xLengthGrp1);
                            yDataGrp2 = getNextInputDataBuffer(data_type, 1, xLengthGrp2);
                            data.appendX("Group1Intensities", yDataGrp1, DataType.QUANTITATIVE);
                            data.appendY("Group2Intensities", yDataGrp2, DataType.QUANTITATIVE);
                            result = null;
                            try {
                                result = (TwoIndependentTResult) data.getAnalysis(AnalysisType.TWO_INDEPENDENT_T);
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            tStat = (float) result.getTStatPooled();
                            pValue = (float) result.getPValueTwoSidedPooled();
                            putNextOutputDataBuffer(tStat, pValue);
                        } else {
                            skipNextInputDataBuffer(data_type, 0, xLengthGrp1);
                            skipNextInputDataBuffer(data_type, 1, xLengthGrp2);
                            putNextOutputDataBuffer();
                        }
                    }
                }
            }
            System.err.println("Done WRITING all results to output streams!");
            for (int fileList = 0; fileList < xLengthGrp1; fileList++) {
                in_Grp1[fileList].close();
                bi_Grp1[fileList].close();
                fi_Grp1[fileList].close();
            }
            for (int fileList = 0; fileList < xLengthGrp2; fileList++) {
                in_Grp2[fileList].close();
                bi_Grp2[fileList].close();
                fi_Grp2[fileList].close();
            }
            if (outputPValuesVolumes == true) {
                pDataOS.flush();
                pDataOS.close();
                pBOS.flush();
                pBOS.close();
                pFOS.flush();
                pFOS.close();
                System.out.println("p-Value Output File =" + pValue_Filename + "_Reg_" + regressors + "_" + regressorLabels[0] + "_" + regressorLabels[1] + ".img");
            }
            if (outputTStatVolumes) {
                tDataOS.flush();
                tDataOS.close();
                tBOS.flush();
                tBOS.close();
                tFOS.flush();
                tFOS.close();
                System.out.println("T-Stats Output File =" + tStat_Filename + "_TStat_" + regressors + "_" + regressorLabels[0] + "_" + regressorLabels[1] + ".img");
            }
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!\n!!!!!!Complete!!!!!!!\n!!!!!!!!!!!!!!!!!!!!!!!!!!");
        } catch (Exception e) {
            System.err.println("Volume1Sample_T_test Error!!!!!!!!!!!!!!");
        }
    }

    /**
	 * This method READS IN a bufferSize chunk of all input volumes and returns it as a double[]
	 * @param data_type - type of the input data
	 * @param group - group index (group 0 or 1)
	 * @param xLength - number of volumes/subjects in the second-column (file-names) in the Design-matrix
	 */
    public static double[] getNextInputDataBuffer(int data_type, int group, int xLength) {
        double[] newInputData = new double[xLength];
        if (data_type == 0) {
            if (group == 0) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        if (byteswap == false) newInputData[fileList] = in_Grp1[fileList].readUnsignedByte(); else newInputData[fileList] = swap((byte) in_Grp1[fileList].readUnsignedByte());
                    } catch (IOException e) {
                        newInputData[fileList] = 0;
                        System.err.println("(Group 0, type 0) newInputData[" + fileList + "]= 0\n");
                    }
                }
            } else if (group == 1) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        if (byteswap == false) newInputData[fileList] = in_Grp2[fileList].readUnsignedByte(); else newInputData[fileList] = swap((byte) in_Grp2[fileList].readUnsignedByte());
                    } catch (IOException e) {
                        newInputData[fileList] = 0;
                        System.err.println("(Group 1, type 0) newInputData[" + fileList + "]= 0\n");
                    }
                }
            }
        } else if (data_type == 1) {
            if (group == 0) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        if (byteswap == false) newInputData[fileList] = in_Grp1[fileList].readByte(); else newInputData[fileList] = swap((byte) in_Grp1[fileList].readByte());
                    } catch (IOException e) {
                        newInputData[fileList] = 0;
                        System.err.println("(Group 0, type 1) newInputData[" + fileList + "]= 0\n");
                    }
                }
            } else if (group == 1) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        if (byteswap == false) newInputData[fileList] = in_Grp2[fileList].readByte(); else newInputData[fileList] = swap((byte) in_Grp2[fileList].readByte());
                    } catch (IOException e) {
                        newInputData[fileList] = 0;
                        System.err.println("(Group 1, type 1) newInputData[" + fileList + "]= 0\n");
                    }
                }
            }
        } else if (data_type == 2) {
            if (group == 0) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        if (byteswap == false) newInputData[fileList] = in_Grp1[fileList].readUnsignedShort(); else newInputData[fileList] = swap((short) in_Grp1[fileList].readUnsignedShort());
                    } catch (IOException e) {
                        newInputData[fileList] = 0;
                        System.err.println("(Group 0, type 2) newInputData[" + fileList + "]= 0\n");
                    }
                }
            } else if (group == 1) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        if (byteswap == false) newInputData[fileList] = in_Grp2[fileList].readUnsignedShort(); else newInputData[fileList] = swap((short) in_Grp2[fileList].readUnsignedShort());
                    } catch (IOException e) {
                        newInputData[fileList] = 0;
                        System.err.println("(Group 1, type 2) newInputData[" + fileList + "]= 0\n");
                    }
                }
            }
        } else if (data_type == 3) {
            if (group == 0) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        if (byteswap == false) newInputData[fileList] = in_Grp1[fileList].readShort(); else newInputData[fileList] = swap((short) in_Grp1[fileList].readShort());
                    } catch (IOException e) {
                        newInputData[fileList] = 0;
                        System.err.println("(Group 0, type 3) newInputData[" + fileList + "]= 0\n");
                    }
                }
            } else if (group == 1) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        if (byteswap == false) newInputData[fileList] = in_Grp2[fileList].readShort(); else newInputData[fileList] = swap((short) in_Grp2[fileList].readShort());
                    } catch (IOException e) {
                        newInputData[fileList] = 0;
                        System.err.println("(Group 1, type 3) newInputData[" + fileList + "]= 0\n");
                    }
                }
            }
        } else if (data_type == 4) {
            if (group == 0) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        if (byteswap == false) newInputData[fileList] = in_Grp1[fileList].readFloat(); else newInputData[fileList] = swap(in_Grp1[fileList].readFloat());
                    } catch (IOException e) {
                        newInputData[fileList] = 0;
                        System.err.println("(Group 0, type 4) newInputData[" + fileList + "]= 0\n");
                    }
                }
            } else if (group == 1) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        if (byteswap == false) newInputData[fileList] = in_Grp2[fileList].readFloat(); else newInputData[fileList] = swap(in_Grp2[fileList].readFloat());
                    } catch (IOException e) {
                        newInputData[fileList] = 0;
                        System.err.println("(Group 1, type 4) newInputData[" + fileList + "]= 0\n");
                    }
                }
            }
        } else {
            System.out.println("Incorrect Input-Volume Data Type: " + data_type + ". Cannot run the regression\n" + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
            return null;
        }
        return newInputData;
    }

    /**
	 * This method WRITES OUT bufferSize chunks of t-stats and p-values for all output volumes
	 * @param tStat t-statistics
	 * @param pValue - p-value
	 */
    public static void putNextOutputDataBuffer(float tStat, float pValue) {
        if (outputPValuesVolumes) {
            try {
                if (byteswap == false) pDataOS.writeFloat(pValue); else pDataOS.writeFloat(swap(pValue));
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        if (outputTStatVolumes) {
            try {
                if (byteswap == false) tDataOS.writeFloat(tStat); else tDataOS.writeFloat(swap(tStat));
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    /**
	 * This method WRITES OUT bufferSize chunks of background (trivial) values for all output volumes
	 */
    public static void putNextOutputDataBuffer() {
        float tempFloat = (float) 0.0;
        if (outputPValuesVolumes == true) {
            try {
                if (byteswap == false) pDataOS.writeFloat(tempFloat + 1); else pDataOS.writeFloat(swap(tempFloat + 1));
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        if (outputTStatVolumes == true) {
            try {
                if (byteswap == false) tDataOS.writeFloat(tempFloat); else tDataOS.writeFloat(swap(tempFloat));
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    /**
	 * This method SKIPS in reading data from the bufferSize chunk of all input volumes
	 * @param data_type - type of the input data
	 * @param group - group (0 or 1) index
	 * @param xLength - number of volumes/subjects in the second-column (file-names) in the Design-matrix
	 */
    public static void skipNextInputDataBuffer(int data_type, int group, int xLength) {
        if (group == 0) {
            if (data_type == 0 || data_type == 1) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        in_Grp1[fileList].skipBytes(1);
                    } catch (IOException e) {
                        System.err.print(e);
                    }
                }
            } else if (data_type == 2 || data_type == 3) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        in_Grp1[fileList].skipBytes(2);
                    } catch (IOException e) {
                        System.err.print(e);
                    }
                }
            } else if (data_type == 4) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        in_Grp1[fileList].skipBytes(4);
                    } catch (IOException e) {
                        System.err.print(e);
                    }
                }
            } else {
                System.out.println("Incorrect Input-Volume Data Type: " + data_type + ". Cannot run the analysis\n" + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
                return;
            }
        } else if (group == 1) {
            if (data_type == 0 || data_type == 1) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        in_Grp2[fileList].skipBytes(1);
                    } catch (IOException e) {
                        System.err.print(e);
                    }
                }
            } else if (data_type == 2 || data_type == 3) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        in_Grp2[fileList].skipBytes(2);
                    } catch (IOException e) {
                        System.err.print(e);
                    }
                }
            } else if (data_type == 4) {
                for (int fileList = 0; fileList < xLength; fileList++) {
                    try {
                        in_Grp2[fileList].skipBytes(4);
                    } catch (IOException e) {
                        System.err.print(e);
                    }
                }
            } else {
                System.out.println("Incorrect Input-Volume Data Type: " + data_type + ". Cannot run the analysis\n" + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
                return;
            }
        } else {
            System.out.println("Incorrect Group-Index: " + group + ". Cannot run the analysis\n" + "Docs: http://wiki.stat.ucla.edu/socr/index.php/SOCR_EduMaterials_AnalysesCommandLineVolume2Samples_T_test");
            return;
        }
        return;
    }

    /**
	   * Byte swap a single SHORT INT value.
	   *
	   * @param value  Value to byte swap.
	   * @return       Byte swapped representation.
	   */
    public static short swap(short value) {
        int b1 = value & 0xff;
        int b2 = (value >> 8) & 0xff;
        return (short) (b1 << 8 | b2 << 0);
    }

    /**
	   * Byte swap a single INTEGER value.
	   *
	   * @param value  Value to byte swap.
	   * @return       Byte swapped representation.
	   */
    public static int swap(int value) {
        int b1 = (value >> 0) & 0xff;
        int b2 = (value >> 8) & 0xff;
        int b3 = (value >> 16) & 0xff;
        int b4 = (value >> 24) & 0xff;
        return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
    }

    /**
	   * Byte swap a single LONG INTEGER value.
	   *
	   * @param value  Value to byte swap.
	   * @return       Byte swapped representation.
	   */
    public static long swap(long value) {
        long b1 = (value >> 0) & 0xff;
        long b2 = (value >> 8) & 0xff;
        long b3 = (value >> 16) & 0xff;
        long b4 = (value >> 24) & 0xff;
        long b5 = (value >> 32) & 0xff;
        long b6 = (value >> 40) & 0xff;
        long b7 = (value >> 48) & 0xff;
        long b8 = (value >> 56) & 0xff;
        return b1 << 56 | b2 << 48 | b3 << 40 | b4 << 32 | b5 << 24 | b6 << 16 | b7 << 8 | b8 << 0;
    }

    /**
	   * Byte swap a single FLOAT value.
	   *
	   * @param value  Value to byte swap.
	   * @return       Byte swapped representation.
	   */
    public static float swap(float value) {
        int intValue = Float.floatToIntBits(value);
        intValue = swap(intValue);
        return Float.intBitsToFloat(intValue);
    }

    /**
	   * Byte swap a single DOUBLE value.
	   *
	   * @param value  Value to byte swap.
	   * @return       Byte swapped representation.
	   */
    public static double swap(double value) {
        long longValue = Double.doubleToLongBits(value);
        longValue = swap(longValue);
        return Double.longBitsToDouble(longValue);
    }
}
