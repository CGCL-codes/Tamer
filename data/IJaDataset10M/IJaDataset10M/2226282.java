package asmdev.editors;

import org.eclipse.swt.graphics.RGB;

public interface IASMColorConstants {

    RGB COMMENT = new RGB(128, 0, 0);

    RGB STRING = new RGB(0, 128, 0);

    RGB DIRECTIVE = new RGB(0, 0, 128);

    RGB INSTRUCTION = new RGB(0, 0, 0);

    RGB OPCODE = new RGB(128, 0, 128);

    RGB OPERAND = new RGB(128, 128, 0);
}
