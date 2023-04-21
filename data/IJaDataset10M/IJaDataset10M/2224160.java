package ist.ac.simulador.assembler.p3;

import java.io.*;
import java.util.*;

public interface P3TokenTypes {

    int EOF = 1;

    int NULL_TREE_LOOKAHEAD = 3;

    int NL = 4;

    int LITERAL_orig = 5;

    int IDENT = 6;

    int LITERAL_equ = 7;

    int LITERAL_word = 8;

    int LITERAL_str = 9;

    int COMMA = 10;

    int LITERAL_tab = 11;

    int STRING_LITERAL = 12;

    int COLON = 13;

    int DOT = 14;

    int R0 = 15;

    int R1 = 16;

    int R2 = 17;

    int R3 = 18;

    int R4 = 19;

    int R5 = 20;

    int R6 = 21;

    int R7 = 22;

    int SP = 23;

    int PC = 24;

    int LBRACK = 25;

    int RBRACK = 26;

    int PLUS = 27;

    int MINUS = 28;

    int CHAR_LITERAL = 29;

    int NUM_INT = 30;

    int NUM_BIN = 31;

    int NUM_OCT = 32;

    int NUM_HEX = 33;

    int MOV = 34;

    int ADD = 35;

    int SUB = 36;

    int CMP = 37;

    int ADDC = 38;

    int SUBB = 39;

    int MUL = 40;

    int AND = 41;

    int OR = 42;

    int XOR = 43;

    int TEST = 44;

    int MVBL = 45;

    int MVBH = 46;

    int XCH = 47;

    int IDIV = 48;

    int SHL = 49;

    int SHR = 50;

    int SHLA = 51;

    int SHRA = 52;

    int ROL = 53;

    int ROR = 54;

    int ROLC = 55;

    int RORC = 56;

    int INC = 57;

    int DEC = 58;

    int POP = 59;

    int NEG = 60;

    int COM = 61;

    int PUSH = 62;

    int JMP = 63;

    int CALL = 64;

    int BR = 65;

    int NOP = 66;

    int RET = 67;

    int RTI = 68;

    int STC = 69;

    int CLC = 70;

    int CMC = 71;

    int ENI = 72;

    int DSI = 73;

    int INT = 74;

    int RETN = 75;

    int FZ = 76;

    int FNZ = 77;

    int FC = 78;

    int FNC = 79;

    int FN = 80;

    int FNN = 81;

    int FO = 82;

    int FNO = 83;

    int FP = 84;

    int FNP = 85;

    int FI = 86;

    int FNI = 87;

    int LPAREN = 88;

    int RPAREN = 89;

    int LCURLY = 90;

    int RCURLY = 91;

    int DIV = 92;

    int STAR = 93;

    int MOD = 94;

    int DOLLAR = 95;

    int WS = 96;

    int SL_COMMENT = 97;

    int VOCAB = 98;
}
