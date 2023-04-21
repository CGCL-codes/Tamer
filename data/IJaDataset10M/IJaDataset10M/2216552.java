package edu.caece.langprocessor.code;

import java.io.Reader;
import java.io.StringReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import edu.caece.langprocessor.Processor;

public class CodeGenerationTest {

    private Processor processor;

    private String startProg;

    private String endProg;

    @Before
    public void SetupTest() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("classpath:/main-context.xml");
        processor = (Processor) appContext.getBean("processor");
        startProg = "\nSTACK SEGMENT\nDB 200 DUP(0)\nSTACK ENDS\n" + "\nCODE SEGMENT\nASSUME CS:CODE,DS:DATA,SS:STACK\n" + "MAIN PROC FAR\nMOV AX, DATA\nMOV DS, AX\n";
        endProg = "MOV AX, 4C00H\nINT 21h\nMAIN ENDP\nCODE ENDS\n";
    }

    @Test
    public void NumberAssignmentShouldGenerateCorrectASMCode() {
        String code = "var a; begin\na := 3;end.";
        String expected = "DATA SEGMENT\na_0 DW 0\nDATA ENDS\n" + startProg + "PUSH 3\nPOP a_0\n" + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void ConstantAssignmentShouldGenerateCorrectASMCode() {
        String code = "const z = 7;var a; begin\na := z;end.";
        String expected = "DATA SEGMENT\nz_1 DW 7\na_0 DW 0\nDATA ENDS\n" + startProg + "PUSH z_1\nPOP a_0\n" + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void AssignmentWithExpressionWithConstAddNumberShouldGenerateCorrectASMCode() {
        String code = "var a; const z = 7; begin\na := z + 3;end.";
        String expected = "DATA SEGMENT\nz_0 DW 7\na_1 DW 0\nDATA ENDS\n" + startProg + "PUSH 3\nPUSH z_0\nPOP AX\nPOP BX\nADD AX, BX\n" + "PUSH AX\nPOP a_1\n" + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void AssignmentWithExpressionWithConstSubsNumberShouldGenerateCorrectASMCode() {
        String code = "var a; const z = 7; begin\na := z - 3;end.";
        String expected = "DATA SEGMENT\nz_0 DW 7\na_1 DW 0\nDATA ENDS\n" + startProg + "PUSH 3\nPUSH z_0\nPOP AX\nPOP BX\nSUB AX, BX\n" + "PUSH AX\nPOP a_1\n" + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void AssignmentWithTermWithConstMultNumberShouldGenerateCorrectASMCode() {
        String code = "var a; const z = 7; begin\na := z * 3;end.";
        String expected = "DATA SEGMENT\nz_0 DW 7\na_1 DW 0\nDATA ENDS\n" + startProg + "PUSH 3\nPUSH z_0\nPOP AX\nPOP BX\nMUL BX\n" + "PUSH AX\nPOP a_1\n" + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void AssignmentWithTermWithNumberDivConstShouldGenerateCorrectASMCode() {
        String code = "var a; const z = 3; begin\na := 9 / z;end.";
        String expected = "DATA SEGMENT\nz_0 DW 3\na_1 DW 0\nDATA ENDS\n" + startProg + "PUSH z_0\nPUSH 9\nPOP AX\nPOP BX\nDIV BX\n" + "PUSH AX\nPOP a_1\n" + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void ComplexExpressionsShouldGenerateCorrectASMCode() {
        String code = "var a, b, c; const z = 3; begin\nb := 5 - z; c:= b + 1;a := (9 / z) + (5 * c) - b;end.";
        String expected = "DATA SEGMENT\nz_2 DW 3\na_3 DW 0\nc_0 DW 0\nb_1 DW 0\nDATA ENDS\n" + startProg + "PUSH z_2\nPUSH 5\nPOP AX\nPOP BX\nSUB AX, BX\nPUSH AX\nPOP b_1\n" + "PUSH 1\nPUSH b_1\nPOP AX\nPOP BX\nADD AX, BX\nPUSH AX\nPOP c_0\nPUSH b_1\nPUSH c_0\n" + "PUSH 5\nPOP AX\nPOP BX\nMUL BX\nPUSH AX\nPUSH z_2\nPUSH 9\nPOP AX\nPOP BX\n" + "DIV BX\nPUSH AX\nPOP AX\nPOP BX\nADD AX, BX\nPUSH AX\nPOP AX\nPOP BX\n" + "SUB AX, BX\nPUSH AX\nPOP a_3\n" + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void IfStatementWithEqualsConditionFalseShouldGenerateCorrectASMCode() {
        String code = "var a; const z = 3; begin\n a := 5 - z; if a = z then a := a + 1; end.";
        String expected = "DATA SEGMENT\nz_0 DW 3\na_1 DW 0\nDATA ENDS\n" + startProg + "PUSH z_0\nPUSH 5\nPOP AX\nPOP BX\nSUB AX, BX\nPUSH AX\nPOP a_1\n" + "PUSH z_0\nPUSH a_1\nPOP AX\nPOP BX\nCMP AX, BX\nJNE LABEL_1\n" + "PUSH 1\nPUSH a_1\nPOP AX\nPOP BX\nADD AX, BX\nPUSH AX\nPOP a_1\n" + "LABEL_1: " + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void IfStatementWithGreaterConditionTrueShouldGenerateCorrectASMCode() {
        String code = "var a; const z = 1; begin\n a := 5 - z; if a > z then a := a + 1; end.";
        String expected = "DATA SEGMENT\nz_0 DW 1\na_1 DW 0\nDATA ENDS\n" + startProg + "PUSH z_0\nPUSH 5\nPOP AX\nPOP BX\nSUB AX, BX\nPUSH AX\nPOP a_1\n" + "PUSH z_0\nPUSH a_1\nPOP AX\nPOP BX\nCMP AX, BX\nJNG LABEL_1\n" + "PUSH 1\nPUSH a_1\nPOP AX\nPOP BX\nADD AX, BX\nPUSH AX\nPOP a_1\n" + "LABEL_1: " + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void IfStatementWithGreaterEqualsConditionTrueShouldGenerateCorrectASMCode() {
        String code = "var a; const z = 3; begin\n a := 6 - z; if a >= z then a := a + z; end.";
        String expected = "DATA SEGMENT\nz_0 DW 3\na_1 DW 0\nDATA ENDS\n" + startProg + "PUSH z_0\nPUSH 6\nPOP AX\nPOP BX\nSUB AX, BX\nPUSH AX\nPOP a_1\n" + "PUSH z_0\nPUSH a_1\nPOP AX\nPOP BX\nCMP AX, BX\nJNGE LABEL_1\n" + "PUSH z_0\nPUSH a_1\nPOP AX\nPOP BX\nADD AX, BX\nPUSH AX\nPOP a_1\n" + "LABEL_1: " + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void IfStatementWithLowerConditionFalseShouldGenerateCorrectASMCode() {
        String code = "var a; const z = 2; begin\n a := 6 - z; if a < z then a := a + 1; end.";
        String expected = "DATA SEGMENT\nz_0 DW 2\na_1 DW 0\nDATA ENDS\n" + startProg + "PUSH z_0\nPUSH 6\nPOP AX\nPOP BX\nSUB AX, BX\nPUSH AX\nPOP a_1\n" + "PUSH z_0\nPUSH a_1\nPOP AX\nPOP BX\nCMP AX, BX\nJNL LABEL_1\n" + "PUSH 1\nPUSH a_1\nPOP AX\nPOP BX\nADD AX, BX\nPUSH AX\nPOP a_1\n" + "LABEL_1: " + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void IfStatementWithLowerEqualsConditionTrueShouldGenerateCorrectASMCode() {
        String code = "var a; const z = 3; begin\n a := 5 - z; if a <= z then a := a + 1; end.";
        String expected = "DATA SEGMENT\nz_0 DW 3\na_1 DW 0\nDATA ENDS\n" + startProg + "PUSH z_0\nPUSH 5\nPOP AX\nPOP BX\nSUB AX, BX\nPUSH AX\nPOP a_1\n" + "PUSH z_0\nPUSH a_1\nPOP AX\nPOP BX\nCMP AX, BX\nJNLE LABEL_1\n" + "PUSH 1\nPUSH a_1\nPOP AX\nPOP BX\nADD AX, BX\nPUSH AX\nPOP a_1\n" + "LABEL_1: " + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void IfStatementWithEvenConditionTrueShouldGenerateCorrectASMCode() {
        String code = "var a; const z = 3; begin\n a := 5 - z; if even a then a := a + 1; end.";
        String expected = "DATA SEGMENT\nz_0 DW 3\na_1 DW 0\nDATA ENDS\n" + startProg + "PUSH z_0\nPUSH 5\nPOP AX\nPOP BX\nSUB AX, BX\nPUSH AX\nPOP a_1\n" + "PUSH a_1\nPOP AX\nSHR AX, 1\nJC LABEL_1\n" + "PUSH 1\nPUSH a_1\nPOP AX\nPOP BX\nADD AX, BX\nPUSH AX\nPOP a_1\n" + "LABEL_1: " + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void IfStatementWithOddConditionFalseShouldGenerateCorrectASMCode() {
        String code = "var a; const z = 3; begin\n a := 5 - z; if odd a then a := a + 1; end.";
        String expected = "DATA SEGMENT\nz_0 DW 3\na_1 DW 0\nDATA ENDS\n" + startProg + "PUSH z_0\nPUSH 5\nPOP AX\nPOP BX\nSUB AX, BX\nPUSH AX\nPOP a_1\n" + "PUSH a_1\nPOP AX\nSHR AX, 1\nJNC LABEL_1\n" + "PUSH 1\nPUSH a_1\nPOP AX\nPOP BX\nADD AX, BX\nPUSH AX\nPOP a_1\n" + "LABEL_1: " + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void WhileStatementWithShouldGenerateCorrectASMCode() {
        String code = "var a; const z = 4; begin\n a := 5 - z; while a < z do a := a + 1; end.";
        String expected = "DATA SEGMENT\nz_0 DW 4\na_1 DW 0\nDATA ENDS\n" + startProg + "PUSH z_0\nPUSH 5\nPOP AX\nPOP BX\nSUB AX, BX\nPUSH AX\nPOP a_1\n" + "LABEL_1: PUSH z_0\nPUSH a_1\nPOP AX\nPOP BX\nCMP AX, BX\nJNL LABEL_2\n" + "PUSH 1\nPUSH a_1\nPOP AX\nPOP BX\nADD AX, BX\nPUSH AX\nPOP a_1\nJMP LABEL_1\n" + "LABEL_2: " + endProg;
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void ReadlnStatementShouldGenerateCorrectASMCode() {
        String code = "var a; const z = 4; begin\nreadln a; while a < z do a := a + 1; end.";
        String expected = "DATA SEGMENT\nz_0 DW 4\na_1 DW 0\nDATA ENDS\n" + "\nSTACK SEGMENT\nDB 200 DUP(0)\nSTACK ENDS\n" + "\nCODE SEGMENT\nASSUME CS:CODE,DS:DATA,SS:STACK\n" + "MAIN PROC FAR\nMOV AX, DATA\nMOV DS, AX\n" + "PUSH 0000h\nPUSH 0001h\nCALL readln\nPOP AX\n" + "MOV a_1, AX\nLABEL_1: PUSH z_0\nPUSH a_1\n" + "POP AX\nPOP BX\nCMP AX, BX\nJNL LABEL_2\nPUSH 1\n" + "PUSH a_1\nPOP AX\nPOP BX\nADD AX, BX\nPUSH AX\n" + "POP a_1\nJMP LABEL_1\nLABEL_2: MOV AX, 4C00H\n" + "INT 21h\nMAIN ENDP\n\nDIG MACRO DIGBASE\n" + "CMP AL, digbase\nJL inicioread\nCMP AL, '9'\n" + "JG inicioread\nMOV AH, 0Eh\nINT 10h\n" + "MOV [BP-1], 03h\nMOV CL, AL\nSUB CL, 48\nMOV AX, SI\n" + "MOV BX, 000Ah\nMUL BX\nADD AX, CX\nMOV SI, AX\n" + "ENDM\nwriteBS MACRO\nMOV AH, 0Eh\nINT 10h\n" + "MOV AL, ' '\nINT 10h\nMOV AL, 08h\nINT 10h\n" + "ENDM\nreadln PROC NEAR\nPUSH  BP\nMOV BP, SP\n" + "SUB SP, 1\nSUB SP, 1\nPUSH AX\nPUSH  BX\nPUSH CX\n" + "PUSH DX\nPUSH SI\nMOV [BP-1], 00h\nMOV [BP-2], 00h\n" + "MOV SI, 0000h\nMOV BX, 0\nMOV CX, 0\ninicioread:\n" + "MOV AH, 0\nINT 16h\nCMP [BP-1], 00h\nJE estado0\n" + "CMP [BP-1], 01h\nJE estado1\nCMP [BP-1], 02h\n" + "JE estado2\nCMP [BP-1], 03h\nJE estado3\nestado0:\n" + "CMP AL, 0Dh\nJE inicioread\nCMP AL, '0'\n" + "JNE estado0a\nMOV [BP-1], 01h\nMOV AH, 0Eh\n" + "INT 10h\nJMP inicioread\nestado0a:\nCMP AL, '-'\n" + "JNE estado0b\nCMP [BP+4], 0000h\nJE inicioread\n" + "MOV [BP-1], 02h\nMOV [BP-2], 01h\nMOV AH, 0Eh\n" + "INT 10h\nJMP  inicioread\nestado0b:\nDIG '1'\n" + "JMP  inicioread\nestado1:\nCMP AL, 0Dh\nJE finread\n" + "CMP AL, 08h\nJNE inicioread\nwriteBS\nMOV [BP-1], 00h\n" + "JMP inicioread\nestado2:\nCMP AL, 0Dh\nJE inicioread\n" + "CMP AL, 08h\nJNE estado2a\nwriteBS\nMOV [BP-1], 00h\n" + "MOV [BP-2], 00h\nJMP inicioread\nestado2a:\nDIG '1'\n" + "JMP inicioread\nestado3:\nCMP AL, 0Dh\nJE finread\n" + "CMP AL, 08h\nJNE estado3a\nwriteBS\nMOV AX, SI\n" + "MOV dx, 0\nMOV BX, 000Ah\nDIV BX\nMOV SI, AX\n" + "CMP SI, 0\nJNE inicioread\nCMP [BP-2], 00h\n" + "JNE estado3bs1\nMOV [BP-1], 00h\nJMP inicioread\n" + "estado3bs1:\nMOV [BP-1], 02h\nJMP inicioread\n" + "estado3a:\nDIG '0'\nJMP inicioread\n\nfinread:\n" + "CMP [BP-2], 00h\nJE finread2\nNEG SI\nfinread2:\n" + "MOV [BP+6], SI\nPOP SI\nPOP DX\nPOP CX\nPOP BX\n" + "POP AX\nMOV SP, BP\nPOP BP\nRET 2\nreadln ENDP\n" + "CODE ENDS\n";
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void WritelnStatementShouldGenerateCorrectASMCode() {
        String code = "var a; const z = 4; begin\na := 10; writeln 'a =', a; end.";
        String expected = "DATA SEGMENT\nz_0 DW 4\na_1 DW 0\nstr_1 DB 'a ='\nDATA ENDS\n" + "\nSTACK SEGMENT\nDB 200 DUP(0)\nSTACK ENDS\n\nCODE SEGMENT\n" + "ASSUME CS:CODE,DS:DATA,SS:STACK\nMAIN PROC FAR\n" + "MOV AX, DATA\nMOV DS, AX\nPUSH 10\nPOP a_1\n" + "PUSH OFFSET str_1 \nPUSH 5 \nCALL writeSTR \n" + "PUSH 0000h \nPUSH a_1\nCALL writeNUM\nCALL writeCRLF\n" + "MOV AX, 4C00H\nINT 21h\nMAIN ENDP\n\nwriteCRLF PROC NEAR\n" + "PUSH AX\nMOV AL, 0Dh\nMOV AH, 0Eh\nINT 10h\nMOV AL, 0Ah\n" + "MOV AH, 0Eh\nINT 10h\nPOP AX\nRET\nwriteCRLF ENDP\n" + "\nwriteNUM PROC NEAR\nPUSH BP\nMOV BP, SP\nSUB SP, 1\n" + "SUB SP, 6\nPUSH AX\nPUSH BX\nPUSH CX\nPUSH DX\nPUSH SI\n" + "MOV [BP-1], 00h\nMOV AX, [BP+4]\nCMP [BP+6], 0\n" + "JE comenzar\nCMP AX, 0\nJGE comenzar\nNEG AX\n" + "MOV [BP-1], 01h\ncomenzar:\nMOV BX, 10\nMOV CX, 0\n" + "MOV SI, BP\nSUB SI, 8\nproxdiv:\nDEC SI\nXOR DX,DX\n" + "DIV BX\nADD dl, 48\nMOV [SI], dl\nINC CX\nCMP AX, 0\n" + "JNZ proxdiv\nCMP [BP-1], 00h\nJZ mostrar\nDEC SI\n" + "MOV [SI], '-'\nINC CX\nmostrar:\nPUSH SI\nPUSH CX\n" + "CALL writeSTR\nPOP SI\nPOP DX\nPOP CX\nPOP BX\nPOP AX\n" + "MOV SP, BP\nPOP BP\nRET 4\nwriteNUM ENDP\n" + "\nwriteSTR PROC NEAR \nPUSH  BP\nMOV BP, SP\n" + "PUSH AX\nPUSH BX\nPUSH CX\nPUSH SI\nMOV SI, [BP+6]\n" + "MOV CX, [BP+4]\nXOR BX, BX\nloop:\nMOV AL, [SI]\n" + "MOV AH, 0Eh\nINT 10h\nINC BX\nINC SI\nCMP BX, CX\n" + "JNE loop\nPOP SI\nPOP CX\nPOP BX\nPOP AX\nPOP BP\n" + "RET 4\nwriteSTR ENDP \nCODE ENDS\n";
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void ProcStatementShouldGenerateCorrectASMCode() {
        String code = "var z; procedure proc1; var x; begin x:=1; end; begin call proc1; z:= 5; end.";
        String expected = "DATA SEGMENT\nz_1 DW 0\nx_0 DW 0\nDATA ENDS\n" + startProg + "CALL proc1\nPUSH 5\nPOP z_1\nMOV AX, 4C00H\nINT 21h\nMAIN ENDP\n" + "\nproc1 PROC NEAR\nPUSH AX\nPUSH BX\nPUSH CX\nPUSH DX\nPUSH 1\nPOP x_0\n" + "POP DX\nPOP CX\nPOP BX\nPOP AX\nRET\nproc1 ENDP\nCODE ENDS\n";
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void ProgramWithTwoProcStatementShouldGenerateCorrectASMCode() {
        String code = "var z; procedure proc1; var x; begin x:=1; end; " + "procedure proc2; var y; begin y:=2; end;" + "begin call proc1; z:= 5; call proc2; end.";
        String expected = "DATA SEGMENT\nz_2 DW 0\ny_1 DW 0\nx_0 DW 0\nDATA ENDS\n" + startProg + "CALL proc1\nPUSH 5\nPOP z_2\nCALL proc2\nMOV AX, 4C00H\n" + "INT 21h\nMAIN ENDP\n\nproc1 PROC NEAR\nPUSH AX\nPUSH BX\nPUSH CX\n" + "PUSH DX\nPUSH 1\nPOP x_0\nPOP DX\nPOP CX\nPOP BX\nPOP AX\nRET\nproc1 ENDP\n\n" + "proc2 PROC NEAR\nPUSH AX\nPUSH BX\nPUSH CX\nPUSH DX\nPUSH 2\nPOP y_1\n" + "POP DX\nPOP CX\nPOP BX\nPOP AX\nRET\nproc2 ENDP\nCODE ENDS\n";
        String actual = "";
        Reader r = new StringReader(code);
        this.processor.proccess(r);
        Boolean status = this.processor.getStatus();
        if (status) actual = this.processor.getGeneratedCode();
        Assert.assertEquals(expected, actual);
    }
}
