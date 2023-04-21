package Assembler2;

import Assembler2.DataTypes.Token;
import Assembler2.SubLinkers.*;
import Assembler2.SubLinkers.RLink;
import java.util.Vector;
import Project.Assembled;

/**
 *
 * @author tim
 */
public class Linker {

    public static void Link(Vector localLabelList, Vector globalLabelList, Vector tokenAssembled, Assembled assembled) {
        int i = 0;
        tokenAssembled.trimToSize();
        while (i < tokenAssembled.size()) {
            Vector tokenLine = (Vector) tokenAssembled.get(i);
            buildLine(assembled, tokenLine, localLabelList, globalLabelList, i);
            ++i;
        }
    }

    public static void buildLine(Assembled assembled, Vector tokenLine, Vector localLabelList, Vector globalLabelList, int pc) {
        Token primary = (Token) tokenLine.get(0);
        int type = primary.getType();
        if (type == Token.RTYPE) {
            RLink.link(assembled, pc, tokenLine);
        } else if (type == Token.ITYPE) {
            ILink.link(assembled, pc, tokenLine);
        } else if (type == Token.SYSTEMTYPE) {
            int[] build = new int[8];
            build[0] = 4;
            build[1] = 0;
            build[2] = 4;
            build[3] = 0;
            build[4] = 2;
            build[5] = 0;
            build[6] = 12;
            build[7] = primary.getLineNumber();
            assembled.addLine(new Code.ExtendedSystemTypes.SystemPrintString(build, pc, primary.getLineNumber()));
        } else if (type == Token.BRANCHTYPE) {
            BranchLink.link(assembled, pc, tokenLine, globalLabelList, localLabelList);
        } else if (type == Token.LSTYPE) {
            LSLink.link(assembled, pc, tokenLine, globalLabelList, localLabelList);
        } else if (type == Token.MOVETYPE) {
            MoveLink.link(assembled, pc, tokenLine);
        } else if (type == Token.PITYPE || type == Token.PRTYPE) {
            PseudoLink.link(assembled, pc, tokenLine);
        } else if (type == Token.ENDTYPE) {
            assembled.addLine(new Code.ExitCode());
        }
    }

    public static Code.Code buildLine(Vector tokenLine, Vector localLabelList, Vector[] globalLabelList) {
        Code.Code build = null;
        Token primary = (Token) tokenLine.get(0);
        int type = primary.getType();
        if (type == Token.RTYPE) {
        } else if (type == Token.ITYPE) {
        } else if (type == Token.SYSTEMTYPE) {
        } else if (type == Token.BRANCHTYPE) {
        } else if (type == Token.LSTYPE) {
        } else if (type == Token.PITYPE) {
        } else if (type == Token.PRTYPE) {
        } else if (type == Token.ENDTYPE) {
        }
        return build;
    }
}
