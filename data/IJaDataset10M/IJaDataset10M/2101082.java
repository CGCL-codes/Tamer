package j8086emu.model.progcode;

import j8086emu.model.utils.Properties;
import java.io.*;
import java.util.ArrayList;

/**
 * @author vlad
 *         Date: 16.04.2010
 *         Time: 13:20:21
 */
public class TasmListingParser {

    private String listingPath = Properties.EXTERNAL_PROGS_PATH + Properties.TASM_OUTPUT_FILENAME;

    private ArrayList<InstructionStructure> machineInstructions = new ArrayList<InstructionStructure>();

    private ArrayList<String> errorMessages = new ArrayList<String>();

    private ArrayList<String> warningMessages = new ArrayList<String>();

    private boolean hyphenation = false;

    private String longString = "";

    public void parseListing() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(listingPath));
            String line;
            boolean errorSection = false;
            boolean instructionsSection = true;
            int strCounter = 1;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equalsIgnoreCase("Symbol Table")) {
                    instructionsSection = false;
                } else if (instructionsSection && line.startsWith(String.valueOf(strCounter))) {
                    parseInstruction(line, strCounter);
                    strCounter++;
                } else if (line.equalsIgnoreCase("Error Summary")) {
                    errorSection = true;
                } else {
                    if (errorSection) {
                        if (line.indexOf("**Error**") != -1) errorMessages.add(line); else if (line.indexOf("*Warning*") != -1) warningMessages.add(line);
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            errorMessages.add("***Fatal Error*** Unexpected end of file or file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseInstruction(String instructionStr, int instructionNum) {
        boolean isData = false;
        instructionStr = instructionStr.replaceAll("\t{2,}", "\t").replaceAll("\t ", "\t");
        instructionStr = instructionStr.replaceFirst(String.valueOf(instructionNum), "");
        instructionStr = instructionStr.replaceAll(";.*", "").trim();
        String[] instructionParts = instructionStr.split("\t");
        int partsNum = instructionParts.length;
        if (hyphenation) {
            isData = true;
            longString += instructionParts[0];
        }
        if (instructionParts[partsNum - 1].charAt(2) == '+') {
            hyphenation = true;
            isData = true;
            String lastSymbol = instructionParts[partsNum - 1].substring(0, 2);
            instructionParts[partsNum - 2] = instructionParts[partsNum - 2] + " " + lastSymbol + " ";
            if (longString.isEmpty()) {
                longString += instructionParts[partsNum - 2];
            }
        } else {
            hyphenation = false;
        }
        if (instructionParts[partsNum - 1].matches("(.+ )?[Dd][BbWw] .+")) {
            isData = true;
        }
        if (!hyphenation) {
            if (instructionParts[0].matches("[0-9A-F]{4}  [0-9A-F]{2}.*")) {
                String offset = instructionParts[0].substring(0, 4);
                String hexCommand = instructionParts[0].substring(6);
                machineInstructions.add(new InstructionStructure(offset, hexCommand, isData));
                longString = "";
            }
            if (!longString.isEmpty() && isData) {
                String offset = longString.substring(0, 4);
                String hexCommand = longString.substring(6);
                machineInstructions.add(new InstructionStructure(offset, hexCommand, isData));
                longString = "";
            }
        }
    }

    public void resetListing() {
        File listingFile = new File(listingPath);
        listingFile.delete();
        machineInstructions.clear();
        errorMessages.clear();
        warningMessages.clear();
    }

    public ArrayList<InstructionStructure> getMachineInstructions() {
        return machineInstructions;
    }

    public ArrayList<String> getErrorMessages() {
        return errorMessages;
    }

    public ArrayList<String> getWarningMessages() {
        return warningMessages;
    }
}
