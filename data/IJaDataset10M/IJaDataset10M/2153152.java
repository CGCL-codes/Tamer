package Assembler2.SubLinkers;

import SimulatorPack.Memory;
import Assembler2.DataTypes.*;
import java.util.Vector;
import Assembler2.DataTypes.LocalType;
import Project.Assembled;
import Code.ExtendedTypes.*;

/**
 *
 * @author tim
 */
public class LSLink {

    public static void link(Assembled assembled, int pc, Vector line, Vector globalLabelList, Vector localLabelList) {
        Token command = (Token) line.get(0);
        String commandString = command.getContained();
        if (commandString.equals("la")) {
            int address = findAddress(((Token) line.get(2)).getContained(), globalLabelList, localLabelList, command.getFileNumber());
            assembled.addLine(new laCode(pc, command.getLineNumber(), Memory.memory(((Token) line.get(1)).getContained()), address));
            if (address == -1) assembled.addError("cannot find referenced label at line " + command.getLineNumber() + " in file " + command.getFileNumber() + " label is " + ((Token) line.get(3)).getContained());
        } else if (commandString.equals("lw")) {
            assembled.addLine(new lwCode(pc, command.getLineNumber(), Memory.memory(((Token) line.get(1)).getContained()), Integer.parseInt(((Token) line.get(2)).getContained()), Memory.memory(((Token) line.get(3)).getContained())));
        } else if (commandString.equals("sw")) {
            assembled.addLine(new swCode(pc, command.getLineNumber(), Memory.memory(((Token) line.get(1)).getContained()), Integer.parseInt(((Token) line.get(2)).getContained()), Memory.memory(((Token) line.get(3)).getContained())));
        }
    }

    public static int findAddress(String label, Vector globalLabelList, Vector localLabelList, int fileNumber) {
        if (fileNumber > localLabelList.size()) return -1;
        Vector localTypes = (Vector) localLabelList.get(fileNumber);
        localTypes.trimToSize();
        for (int i = 0; i < localTypes.size(); ++i) {
            LocalType localData = (LocalType) localTypes.get(i);
            if (localData.compareLabelName(label)) return localData.getLocation();
        }
        int i = 0;
        while (i < globalLabelList.size()) {
            GlobalType globalTemp = (GlobalType) globalLabelList.get(i++);
            if (globalTemp.compareLabel(label)) return findAddress(label, globalLabelList, localLabelList, globalTemp.getFileNumber());
        }
        return -1;
    }
}
