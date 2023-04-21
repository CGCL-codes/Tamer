package npc.kernel.simplepipelining;

import java.awt.Color;
import java.util.HashMap;
import java.lang.Number;
import npc.kernel.ICpu;
import npc.kernel.IMemory;
import npc.kernel.Instruction;
import npc.kernel.Opcode;
import npc.kernel.PipelineInfo;
import npc.kernel.Register;
import npc.kernel.Assembler;
import java.util.*;
import javax.swing.JOptionPane;

public class Cpu implements ICpu {

    /**
	 * �ڴ�
	 */
    private IMemory memory;

    /**
	 * �Ĵ���
	 */
    private Register register;

    /**
	 * ��¼ִ���˶��ٲ�ָ��
	 */
    private long instructionCounter = 0;

    /**
	 * ��¼ִ���˶��ٸ�����
	 */
    private long cycleCounter = 0;

    /**
	 * ��¼һ��������ٸ�����
	 */
    private long stallCounter = 0;

    /**
	 * ��¼һ��flush�˶�����ָ��
	 */
    private long flushCounter = 0;

    /**
	 * ��¼һ���ж��ٸ���תָ��
	 */
    private long jumpCounter = 0;

    /**
	 * ��¼��ָͬ���ex��ִ�����������˵lw��sw��2�����ڡ�mult��divҲ�������Զ���������<br>
	 * ���ָ������������Ĭ��Ϊ����1������
	 */
    private static final HashMap<Instruction, Integer> cycleLength = new HashMap<Instruction, Integer>();

    static {
        cycleLength.put(Instruction.LW, 2);
        cycleLength.put(Instruction.SW, 2);
        cycleLength.put(Instruction.MULT, 5);
        cycleLength.put(Instruction.MULTU, 5);
        cycleLength.put(Instruction.DIV, 11);
        cycleLength.put(Instruction.DIVU, 11);
    }

    /**
	 * ��ɫ��������Ĵ�С
	 */
    private static final int COLORARRAYSIZE = 8;

    /**
	 * ��ɫ��������
	 */
    private static final Color[] colorArray = new Color[COLORARRAYSIZE];

    static {
        colorArray[0] = Color.BLUE;
        colorArray[1] = Color.red;
        colorArray[2] = Color.GREEN;
        colorArray[3] = Color.pink;
        colorArray[4] = Color.MAGENTA;
        colorArray[5] = Color.ORANGE;
        colorArray[6] = Color.LIGHT_GRAY;
        colorArray[7] = Color.CYAN;
    }

    /**
	 * ��¼�˶μ�Ĵ�������Ϣ,����������Ϊ�˷������
	 */
    private PipeLineReg oldInfo = new PipeLineReg();

    private PipeLineReg newInfo = new PipeLineReg();

    /**
	 * ��¼��Ex�κ�Mem�λ��м������ڿ��Խ���
	 */
    private int exCycle;

    private int memCycle;

    /**
	 * ��¼�˱�forwarding�����
	 * ���û��forwarding�������¼ԭʼ���
	 * forwardData[0]~[3]�ֱ��ʾ��ID, EX.A, EX.B, MEM
	 */
    private Number[] forwardData = new Number[4];

    /**
	 * ��¼�Ƿ���ҪStall
	 * ifStall[0]~[4]�ֱ��ʾ��IF, ID, EX, MEM, WB�Ƿ���stall����(1���stall)
	 * isStall = 0: ��ʾ����stall
	 * isStall = 1: ��ʾҪstall
	 * isStall = 2: ��ʾҪstall������Ҫ����һ��nopָ��
	 */
    private int[] isStall = new int[5];

    /**
	 * ��¼������ˮ���Ƿ����ſ�״̬(1������Ҫ�ſ�,2һ����ȷҪ�ſ�)
	 */
    private int cleanup;

    /**
	 * ��¼�����Ƿ���Ҫ�Ե�һ��ָ�����flush(1�����Ҫflush) 
	 */
    private boolean isFlush;

    /**
	 * ��ת��ַ
	 */
    private int NPC;

    /**
	 * ��¼ʱ��ͼtable����Ҫ����Ϣ
	 */
    private ShikongInfo shikongInfo = new ShikongInfo();

    /**
	 * ��¼forwarding�������Ϣ
	 */
    private ArrayList<PipelineInfo> pipelineInfo = new ArrayList<PipelineInfo>(3);

    /**
	 * Ĭ�Ϲ��캯�����memory��register
	 */
    public Cpu() {
        reset();
    }

    /**
	 * �������ڴ��Լ��Ĵ����Ĺ��캯��
	 * @param memory �ڴ�
	 * @param register �Ĵ���
	 */
    public Cpu(IMemory memory, Register register) {
        this();
        this.memory = memory;
        this.register = register;
    }

    /**
	 * ��ȡʱ��ͼ��Ϣ
	 * @return ����ʱ��ͼ��Ϣ
	 */
    public String[][] getShikongInfo() {
        return shikongInfo.getShikongInfo();
    }

    public int getShikongCount() {
        return shikongInfo.getShikongCount();
    }

    /**
	 * ��ȡʱ��ͼ����
	 * @return ʱ��ͼ����
	 */
    public int[] getShikongCycle() {
        return shikongInfo.getShikongCycle();
    }

    /**
	 * ��ȡ���µ�һ��pipelineinfo��Ҳ����stepCycle�������һ��Pipelineinfo.<br>
	 * ������µ�һ������û�в���stall/forwarding����ô�ͷ���null
	 * @return ���µ�һ�����ڲ����stall/forwarding�����û�в����򷵻�null
	 */
    public PipelineInfo[] getPipelineInfo() {
        PipelineInfo[] answer = new PipelineInfo[pipelineInfo.size()];
        for (int i = 0; i < pipelineInfo.size(); i++) {
            answer[i] = pipelineInfo.get(i);
        }
        return answer;
    }

    /**
	 * �������ִ�й�������е�stall/forwarding�������ִ�й�̡�ָ�����Դ�reset����
	 */
    public PipelineInfo[] getAllPipelineInfo() {
        return null;
    }

    /**
	 * ����picPanel����Ҫ��ָ����Ϣ
	 * @return �������ϵ�ָ�String[0]��ʾIF�Σ�String[1]��ʾID�Σ��Դ�����
	 * 			���ĳ���η���ֵΪnull�����ʾ�ö���һ������
	 * 			���ĳ���εķ���ֵΪflush������ʾ�ö�Ϊflush
	 */
    public String[] getInstructionInfo() {
        int[] ir = new int[5];
        String[] answer = new String[5];
        ir[0] = newInfo.ifIR;
        ir[1] = newInfo.idIR;
        ir[2] = newInfo.exIR;
        ir[3] = newInfo.memIR;
        ir[4] = newInfo.wbIR;
        for (int i = 0; i < 5; i++) {
            if (ir[i] == Opcode.NOP) answer[i] = null; else answer[i] = Assembler.decode(ir[i]);
        }
        if (newInfo.idIR == Opcode.NOP && newInfo.idIRinfo.id != -1) {
            answer[1] = Opcode.IF_FLUSH;
        }
        return answer;
    }

    /**
	 * ���ظ����ε���ɫ
	 * @return
	 */
    public Color[] getColorInfo() {
        Color[] color = new Color[5];
        long[] id = new long[5];
        id[0] = newInfo.ifIRinfo.id;
        id[1] = newInfo.idIRinfo.id;
        id[2] = newInfo.exIRinfo.id;
        id[3] = newInfo.memIRinfo.id;
        id[4] = newInfo.wbIRinfo.id;
        for (int i = 0; i < 5; i++) {
            if (id[i] != -1) color[i] = colorArray[(int) id[i] % COLORARRAYSIZE]; else color[i] = Color.BLACK;
        }
        return color;
    }

    /**
	 * ��ȡ��ǰһ�������˶��ٴ�flush
	 * @return flush����
	 */
    public long getFlushCounter() {
        return flushCounter;
    }

    /**
	 * ��ȡ��ǰstall����
	 * @return stall����
	 */
    public long getStallCounter() {
        return stallCounter;
    }

    /**
	 * ��ȡ��ǰһ��ִ���˼�������
	 * @return ִ�е����ڼ���
	 */
    public long getCycleCounter() {
        return cycleCounter;
    }

    /**
	 * ��ȡ��ǰһ��ִ���˶�����ָ��
	 * @return
	 */
    public long getInsCounter() {
        return instructionCounter;
    }

    /**
	 * ��ȡһ��ִ���˶��ٸ���תָ��
	 * @return jump����
	 */
    public long getJumpCounter() {
        return jumpCounter;
    }

    /**
	 * reset CPU
	 */
    @Override
    public void reset() {
        newInfo.ifPC = 0;
        cycleCounter = 0;
        instructionCounter = 0;
        stallCounter = 0;
        flushCounter = 0;
        jumpCounter = 0;
        cleanup = 0;
        newInfo.ifIRinfo = new InstructionInfo(Opcode.NOP, -1);
        newInfo.idIRinfo = new InstructionInfo(Opcode.NOP, -1);
        newInfo.exIRinfo = new InstructionInfo(Opcode.NOP, -1);
        newInfo.memIRinfo = new InstructionInfo(Opcode.NOP, -1);
        newInfo.wbIRinfo = new InstructionInfo(Opcode.NOP, -1);
        newInfo.ifIR = Opcode.NOP;
        newInfo.idIR = Opcode.NOP;
        newInfo.exIR = Opcode.NOP;
        newInfo.memIR = Opcode.NOP;
        newInfo.wbIR = Opcode.NOP;
        oldInfo.ifIRinfo = new InstructionInfo(Opcode.NOP, -1);
        oldInfo.idIRinfo = new InstructionInfo(Opcode.NOP, -1);
        oldInfo.exIRinfo = new InstructionInfo(Opcode.NOP, -1);
        oldInfo.memIRinfo = new InstructionInfo(Opcode.NOP, -1);
        oldInfo.wbIRinfo = new InstructionInfo(Opcode.NOP, -1);
        oldInfo.ifIR = Opcode.NOP;
        oldInfo.idIR = Opcode.NOP;
        oldInfo.exIR = Opcode.NOP;
        oldInfo.memIR = Opcode.NOP;
        oldInfo.wbIR = Opcode.NOP;
        isFlush = false;
        exCycle = 0;
        memCycle = 0;
        shikongInfo.reset();
    }

    /**
	 * ����ִ�У�ֱ������haltָ����߶ϵ� <br>
	 * <b><font color = red>�Զϵ�Ĵ���:</font></b><br>
	 * ������һ��ָ��I�������˶ϵ㣬��ô������ִ������ָ��֮ǰͣ�¡� <br>
	 * ����˵ R1 = 0��Ȼ������һ��ָ���жϵ� <br>
	 * <h1>addi r1, r1, 1 </h1><br>
	 * ��ô������r1 ����1 ֮ǰͣ�£��ϵ�ʱ�� r1��ȻΪ0 <p>
	 * 
	 * <b><font color = magenta>�������run��ʱ�����һ���ϵ�</font></b><br>
	 * ����һ�ε���run֮�󣬻�ͣ��һ���ϵ��ϣ�Ȼ�󷵻ء���һ���ٵ���run��ʱ�� <br>
	 * ����ϵ��Ҫ�����
	 * 
	 * @return �������Ϊ halt ��ͣ�£���Ҫ���� true�����򷵻� false
	 * @throws RuntimeException �������쳣������˵ipԽ���ڴ�߽磬ָ���޷�ʶ�𡭡�
	 */
    @Override
    public boolean run() {
        RunState runState;
        do {
            runState = cpuCycle(true);
        } while (runState != RunState.HALT && runState != RunState.BK);
        return (runState.equals(RunState.HALT));
    }

    /**
	 * ִ��һ����ˮ������ <br>
	 * <font color = red>��ȫ���Ӷϵ�</font>
	 * @return �������Ϊ halt ��ͣ�£���Ҫ���� true�����򷵻� false
	 * @throws RuntimeException �������쳣������˵ipԽ���ڴ�߽磬ָ���޷�ʶ�𡭡�
	 */
    @Override
    public boolean stepCycle() {
        return (cpuCycle(false) == RunState.HALT);
    }

    /**
	 * CPU�ڲ�����ִ��һ�����ڵĺ���
	 * @param enableBK �Ƿ��Ƕϵ�
	 * @return ����ֵ��ʾ�Ƿ���һ��ָ��ִ�����
	 * 		Runstate.NORM - û��ָ��ִ�����
	 * 		Runstate.INS_OVER - ��ָ��ִ����ϣ����ǲ��Ƕϵ����HALT
	 * 		Runstate.HALT - HALTָ��ִ�����
	 * 		Runstate.BK - �ϵ�ָ��ִ�����
	 */
    private RunState cpuCycle(boolean enableBK) {
        PipeLineReg temp = oldInfo;
        oldInfo = newInfo;
        newInfo = temp;
        pipelineInfo.clear();
        for (int i = 0; i < 5; i++) {
            isStall[i] = 0;
        }
        cycleCounter++;
        if (oldInfo.wbIRinfo.id != -1 && isStall[4] == 0) instructionCounter++;
        if (isFlush) {
            oldInfo.ifPC = NPC;
            if (cleanup == 1) {
                if (oldInfo.idIRinfo.runState == RunState.BK) {
                    cleanup = 0;
                    oldInfo.idIRinfo.runState = RunState.INS_OVER;
                }
                if (oldInfo.ifIRinfo.runState == RunState.HALT) {
                    cleanup = 0;
                    oldInfo.ifIRinfo.runState = RunState.INS_OVER;
                }
            }
            oldInfo.ifIR = Opcode.NOP;
            oldInfo.ifIRinfo = new InstructionInfo(oldInfo.ifIR, oldInfo.ifIRinfo.id);
            isFlush = false;
            flushCounter++;
        }
        if (exCycle != 0 && memCycle != 0) {
            isStall[0] = 1;
            isStall[1] = 1;
            isStall[2] = 0;
            isStall[3] = 0;
            isStall[4] = 1;
            pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.exIR), null, PipelineInfo.Type.StallMEM));
        } else if (exCycle == 0 && memCycle != 0) {
            isStall[0] = 1;
            isStall[1] = 1;
            isStall[2] = 1;
            isStall[3] = 0;
            isStall[4] = 1;
            pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.memIR), null, PipelineInfo.Type.StallMEM));
        } else if (exCycle != 0 && memCycle == 0) {
            isStall[0] = 1;
            isStall[1] = 1;
            isStall[2] = 0;
            isStall[3] = 1;
            isStall[4] = 1;
            pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.exIR), null, PipelineInfo.Type.StallMEM));
        } else checkForwarding();
        try {
            WB();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        MEM();
        EX();
        ID(enableBK);
        IF(enableBK);
        shikongInfo.update(newInfo, isStall, cycleCounter, (oldInfo.wbIRinfo.id != -1) && (oldInfo.wbIRinfo.id != newInfo.wbIRinfo.id));
        if (isStall[4] != 0 || newInfo.wbIRinfo.id == -1) {
            return RunState.NORM;
        } else {
            if (newInfo.wbIRinfo.runState != RunState.INS_OVER) cleanup = 0;
            return newInfo.wbIRinfo.runState;
        }
    }

    @SuppressWarnings("unused")
    private void outputStepInfo() {
        System.out.println("cleanup = " + cleanup);
        System.out.println("forwardData[1] = " + forwardData[1] + " forwardData[2] = " + forwardData[2]);
        System.out.println("ifPC = " + newInfo.ifPC);
        System.out.println();
        System.out.println("ifIR = " + Assembler.decode(newInfo.ifIR));
        System.out.println("idIR = " + Assembler.decode(newInfo.idIR));
        System.out.println("exIR = " + Assembler.decode(newInfo.exIR));
        System.out.println("memIR = " + Assembler.decode(newInfo.memIR));
        System.out.println("wbIR = " + Assembler.decode(newInfo.wbIR));
        for (int i = 0; i < 4; i++) {
            System.out.print("R" + i + "=" + register.R[i] + " ");
        }
        System.out.println();
        System.out.print("isStall = ");
        for (int i = 0; i < 5; i++) System.out.print(isStall[i] + " ");
        System.out.println("");
    }

    /**
	 * ִ�е���ָ�ֱ����ָ�����������<br>
	 * <font color = red>��ȫ���Ӷϵ�</font>
	 * @return �������Ϊ halt ��ͣ�£���Ҫ���� true�����򷵻� false
	 * @throws RuntimeException �������쳣������˵ipԽ���ڴ�߽磬ָ���޷�ʶ�𡭡�
	 */
    @Override
    public boolean stepInstruction() {
        RunState runState;
        do {
            runState = cpuCycle(false);
        } while (runState == RunState.NORM);
        return runState == RunState.HALT;
    }

    private void IF(boolean enableBK) {
        if (isStall[0] == 1) {
            newInfo.ifIR = oldInfo.ifIR;
            newInfo.ifIRinfo = oldInfo.ifIRinfo;
            newInfo.ifPC = oldInfo.ifPC;
            return;
        } else if (isStall[0] == 2) {
            newInfo.ifIR = Opcode.NOP;
            newInfo.ifIRinfo = new InstructionInfo(Opcode.NOP, -1);
            newInfo.ifPC = oldInfo.ifPC;
            return;
        } else if (cleanup != 0) {
            newInfo.ifIR = Opcode.NOP;
            newInfo.ifIRinfo = new InstructionInfo(Opcode.NOP, -1);
            newInfo.ifPC = oldInfo.ifPC;
            return;
        }
        newInfo.ifIR = memory.getWord(oldInfo.ifPC);
        newInfo.ifIRinfo = new InstructionInfo(newInfo.ifIR, cycleCounter);
        newInfo.ifPC = oldInfo.ifPC + 4;
        if (newInfo.ifIRinfo.type == InstructionType.CondJump || newInfo.ifIRinfo.type == InstructionType.Jump) jumpCounter++;
        if (newInfo.ifIR == Opcode.HALT) {
            cleanup = 1;
            newInfo.ifIRinfo.runState = RunState.HALT;
        } else if (memory.isBreakpoint(newInfo.ifPC) && enableBK) {
            cleanup = 1;
            newInfo.ifIRinfo.runState = RunState.BK;
        }
    }

    private void ID(boolean enableBK) {
        if ((isStall[1] == 1)) {
            newInfo.idA = oldInfo.idA;
            newInfo.idB = oldInfo.idB;
            newInfo.idImm = oldInfo.idImm;
            newInfo.idIR = oldInfo.idIR;
            newInfo.idIRinfo = oldInfo.idIRinfo;
            return;
        } else if (isStall[1] == 2) {
            newInfo.idA = oldInfo.idA;
            newInfo.idB = oldInfo.idB;
            newInfo.idImm = oldInfo.idImm;
            newInfo.idIR = Opcode.NOP;
            newInfo.idIRinfo = new InstructionInfo(Opcode.NOP, -1);
            return;
        }
        if (oldInfo.ifIRinfo.type == InstructionType.Jump || ((oldInfo.ifIR & Opcode.OP_MASK) == Opcode.BEQZ) && (forwardData[0].intValue() == 0) || ((oldInfo.ifIR & Opcode.OP_MASK) == Opcode.BNEZ) && (forwardData[0].intValue() != 0)) {
            isFlush = true;
            NPC = oldInfo.ifIRinfo.imm + oldInfo.ifPC;
            if (memory.isBreakpoint(NPC) && enableBK) {
                cleanup = 2;
                newInfo.idIRinfo.runState = RunState.BK;
            }
        }
        newInfo.idImm = oldInfo.ifIRinfo.imm;
        newInfo.idA = getRegVal(oldInfo.ifIRinfo.rs1);
        newInfo.idB = getRegVal(oldInfo.ifIRinfo.rs2);
        newInfo.idIR = oldInfo.ifIR;
        newInfo.idIRinfo = oldInfo.ifIRinfo;
    }

    private void EX() {
        if ((isStall[2] == 1) || exCycle != 0) {
            newInfo.exAluout = oldInfo.exAluout;
            newInfo.exB = oldInfo.exB;
            newInfo.exIR = oldInfo.exIR;
            newInfo.exIRinfo = oldInfo.exIRinfo;
            if (exCycle != 0) --exCycle;
            return;
        } else if (isStall[2] == 2) {
            newInfo.exAluout = oldInfo.exAluout;
            newInfo.exB = oldInfo.exB;
            newInfo.exIR = Opcode.NOP;
            newInfo.exIRinfo = new InstructionInfo(Opcode.NOP, -1);
            return;
        }
        InstructionInfo idir = oldInfo.idIRinfo;
        newInfo.exAluout = 0;
        if ((oldInfo.idIR & Opcode.OP_MASK) == 0x04000000) {
            int i;
            i = 0;
            System.out.println(i);
        }
        switch(oldInfo.idIR & Opcode.OP_MASK) {
            case Opcode.LW:
            case Opcode.SW:
                newInfo.exAluout = forwardData[1].intValue() + idir.imm;
                break;
            case Opcode.J:
            case Opcode.BEQZ:
            case Opcode.BNEZ:
                break;
            case Opcode.ADDI:
            case Opcode.ADDUI:
                newInfo.exAluout = forwardData[1].intValue() + idir.imm;
                break;
            case Opcode.SUBI:
            case Opcode.SUBUI:
                newInfo.exAluout = forwardData[1].intValue() - idir.imm;
                break;
            case Opcode.ANDI:
                newInfo.exAluout = forwardData[1].intValue() & idir.imm;
                break;
            case Opcode.ORI:
                newInfo.exAluout = forwardData[1].intValue() | idir.imm;
                break;
            case Opcode.XORI:
                newInfo.exAluout = forwardData[1].intValue() ^ idir.imm;
                break;
            case Opcode.SLLI:
                newInfo.exAluout = forwardData[1].intValue() << (idir.imm & 0x1f);
                break;
            case Opcode.SRLI:
                newInfo.exAluout = forwardData[1].intValue() >>> (idir.imm & 0x1f);
                break;
            case Opcode.SRAI:
                newInfo.exAluout = forwardData[1].intValue() >> (idir.imm & 0x1f);
                break;
            case Opcode.SEQI:
                newInfo.exAluout = (forwardData[1].intValue() == idir.imm) ? 1 : 0;
                break;
            case Opcode.SNEI:
                newInfo.exAluout = (forwardData[1].intValue() != idir.imm) ? 1 : 0;
                break;
            case Opcode.SLTI:
                newInfo.exAluout = (forwardData[1].intValue() < idir.imm) ? 1 : 0;
                break;
            case Opcode.SGTI:
                newInfo.exAluout = (forwardData[1].intValue() > idir.imm) ? 1 : 0;
                break;
            case Opcode.SLEI:
                newInfo.exAluout = (forwardData[1].intValue() <= idir.imm) ? 1 : 0;
                break;
            case Opcode.ALU_GROUP_0:
                switch(oldInfo.idIR & (Opcode.OP_MASK | Opcode.FUNC_MASK)) {
                    case Opcode.SLL:
                        newInfo.exAluout = forwardData[1].intValue() << (forwardData[2].intValue() & 0x1f);
                        break;
                    case Opcode.SRL:
                        newInfo.exAluout = forwardData[1].intValue() >>> (forwardData[2].intValue() & 0x1f);
                        break;
                    case Opcode.SRA:
                        newInfo.exAluout = forwardData[1].intValue() >> (forwardData[2].intValue() & 0x1f);
                        break;
                    case Opcode.ADD:
                    case Opcode.ADDU:
                        newInfo.exAluout = forwardData[1].intValue() + forwardData[2].intValue();
                        break;
                    case Opcode.SUB:
                    case Opcode.SUBU:
                        newInfo.exAluout = forwardData[1].intValue() - forwardData[2].intValue();
                        break;
                    case Opcode.AND:
                        newInfo.exAluout = forwardData[1].intValue() & forwardData[2].intValue();
                        break;
                    case Opcode.OR:
                        newInfo.exAluout = forwardData[1].intValue() | forwardData[2].intValue();
                        break;
                    case Opcode.XOR:
                        newInfo.exAluout = forwardData[1].intValue() ^ forwardData[2].intValue();
                        break;
                    case Opcode.SEQ:
                        newInfo.exAluout = (forwardData[1].intValue() == forwardData[2].intValue()) ? 1 : 0;
                        break;
                    case Opcode.SNE:
                        newInfo.exAluout = (forwardData[1].intValue() != forwardData[2].intValue()) ? 1 : 0;
                        break;
                    case Opcode.SLT:
                        newInfo.exAluout = (forwardData[1].intValue() < forwardData[2].intValue()) ? 1 : 0;
                        break;
                    case Opcode.SGT:
                        newInfo.exAluout = (forwardData[1].intValue() > forwardData[2].intValue()) ? 1 : 0;
                        break;
                    case Opcode.SLE:
                        newInfo.exAluout = (forwardData[1].intValue() <= forwardData[2].intValue()) ? 1 : 0;
                        break;
                    default:
                        throw new RuntimeException("Unknown Instruction 0x" + Integer.toHexString(oldInfo.idIR) + "!");
                }
                break;
            case Opcode.ALU_GROUP_1:
                switch(oldInfo.idIR & (Opcode.OP_MASK | Opcode.FUNC_MASK)) {
                    case Opcode.MULT:
                        exCycle = cycleLength.get(Instruction.MULT) - 1;
                        newInfo.exAluout = forwardData[1].intValue() * forwardData[2].intValue();
                        break;
                    case Opcode.MULTU:
                        exCycle = cycleLength.get(Instruction.MULTU) - 1;
                        newInfo.exAluout = forwardData[1].intValue() * forwardData[2].intValue();
                        break;
                    case Opcode.DIV:
                        exCycle = cycleLength.get(Instruction.DIV) - 1;
                        if (forwardData[2].intValue() == 0) throw new RuntimeException("div : Divide by 0");
                        newInfo.exAluout = forwardData[1].intValue() / forwardData[2].intValue();
                        break;
                    case Opcode.DIVU:
                        exCycle = cycleLength.get(Instruction.DIVU) - 1;
                        if (forwardData[2].intValue() == 0) throw new RuntimeException("divu : Divide by 0");
                        newInfo.exAluout = forwardData[1].intValue() / forwardData[2].intValue();
                        break;
                    default:
                        throw new RuntimeException("Unknown Instruction 0x" + Integer.toHexString(oldInfo.idIR) + "!");
                }
                break;
            case Opcode.ALU_GROUP_FLOAT:
                switch(oldInfo.idIR & (Opcode.OP_MASK | Opcode.FUNC_MASK)) {
                    case Opcode.ADDF:
                        newInfo.exAluout = forwardData[1].floatValue() + forwardData[2].floatValue();
                        break;
                    case Opcode.SUBF:
                        newInfo.exAluout = forwardData[1].floatValue() - forwardData[2].floatValue();
                        break;
                    case Opcode.MULTF:
                        newInfo.exAluout = forwardData[1].floatValue() * forwardData[2].floatValue();
                        break;
                    case Opcode.DIVF:
                        newInfo.exAluout = forwardData[1].floatValue() / forwardData[2].floatValue();
                        break;
                    case Opcode.LTF:
                        newInfo.exAluout = (forwardData[1].floatValue() < forwardData[2].floatValue()) ? 1 : 0;
                        break;
                    case Opcode.GTF:
                        newInfo.exAluout = (forwardData[1].floatValue() > forwardData[2].floatValue()) ? 1 : 0;
                        break;
                    case Opcode.LEF:
                        newInfo.exAluout = (forwardData[1].floatValue() <= forwardData[2].floatValue()) ? 1 : 0;
                        break;
                    case Opcode.GEF:
                        newInfo.exAluout = (forwardData[1].floatValue() >= forwardData[2].floatValue()) ? 1 : 0;
                        break;
                    case Opcode.EQF:
                        newInfo.exAluout = (forwardData[1].floatValue() == forwardData[2].floatValue()) ? 1 : 0;
                        break;
                    case Opcode.NEF:
                        newInfo.exAluout = (forwardData[1].floatValue() != forwardData[2].floatValue()) ? 1 : 0;
                        break;
                    case Opcode.CVT12F:
                        newInfo.exAluout = forwardData[1].floatValue();
                        break;
                    case Opcode.CVTF21:
                        newInfo.exAluout = forwardData[1].intValue();
                        break;
                    default:
                        throw new RuntimeException("Unknown Instruction 0x" + Integer.toHexString(newInfo.idIR) + "!");
                }
            case Opcode.HALT:
                break;
            default:
                throw new RuntimeException("Unknown Instruction 0x" + Integer.toHexString(oldInfo.idIR) + "!");
        }
        newInfo.exB = forwardData[2].intValue();
        newInfo.exIR = oldInfo.idIR;
        newInfo.exIRinfo = oldInfo.idIRinfo;
    }

    private void MEM() {
        if ((isStall[3] == 1) || memCycle != 0) {
            newInfo.memAluout = oldInfo.memAluout;
            newInfo.memIR = oldInfo.memIR;
            newInfo.memIRinfo = oldInfo.memIRinfo;
            newInfo.memLMD = oldInfo.memLMD;
            if (memCycle != 0) --memCycle;
            return;
        } else if (isStall[3] == 2) {
            newInfo.memAluout = oldInfo.memAluout;
            newInfo.memLMD = oldInfo.memLMD;
            newInfo.memIR = Opcode.NOP;
            newInfo.memIRinfo = new InstructionInfo(Opcode.NOP, -1);
            return;
        }
        if (oldInfo.exIRinfo.type == InstructionType.Load || oldInfo.exIRinfo.type == InstructionType.Store) {
            if (oldInfo.exIRinfo.type == InstructionType.Load) {
                memCycle = cycleLength.get(Instruction.LW) - 1;
                newInfo.memLMD = memory.getWord(oldInfo.exAluout.intValue());
            } else {
                memCycle = cycleLength.get(Instruction.SW) - 1;
                memory.putWord(oldInfo.exAluout.intValue(), forwardData[3].intValue());
            }
        }
        newInfo.memIR = oldInfo.exIR;
        newInfo.memIRinfo = oldInfo.exIRinfo;
        newInfo.memAluout = oldInfo.exAluout;
    }

    private void WB() {
        if ((isStall[4] == 1)) {
            newInfo.wbIR = oldInfo.wbIR;
            newInfo.wbIRinfo = oldInfo.wbIRinfo;
            return;
        } else if (isStall[4] == 2) {
            newInfo.wbIR = Opcode.NOP;
            newInfo.wbIRinfo = new InstructionInfo(Opcode.NOP, -1);
            return;
        }
        newInfo.wbIR = oldInfo.memIR;
        newInfo.wbIRinfo = oldInfo.memIRinfo;
        if (oldInfo.memIRinfo.rd != -2) {
            if (oldInfo.memIRinfo.rd == 0) {
                if (oldInfo.memIR != Opcode.NOP) throw (new RuntimeException("Illigal tempt to write R0:  " + Integer.toHexString(oldInfo.memIR) + "!"));
                return;
            }
            if (oldInfo.memIRinfo.type == InstructionType.Load) register.R[oldInfo.memIRinfo.rd] = oldInfo.memLMD; else if (oldInfo.memIRinfo.rd < InstructionInfo.FOFF) register.R[oldInfo.memIRinfo.rd] = oldInfo.memAluout.intValue(); else if (oldInfo.memIRinfo.rd == InstructionInfo.FSTATE) register.fState = oldInfo.memAluout.intValue(); else register.F[oldInfo.memIRinfo.rd - InstructionInfo.FOFF] = oldInfo.memAluout.floatValue();
        }
    }

    private void checkForwarding() {
        if (oldInfo.ifIRinfo.rs1 != -1) forwardData[0] = getRegVal(oldInfo.ifIRinfo.rs1);
        if (oldInfo.idIRinfo.rs1 != -1) forwardData[1] = oldInfo.idA;
        if (oldInfo.idIRinfo.rs2 != -1) forwardData[2] = oldInfo.idB;
        if (oldInfo.exIRinfo.rs2 != -1) forwardData[3] = oldInfo.exB;
        if (oldInfo.ifIRinfo.type == InstructionType.CondJump && oldInfo.ifIRinfo.rs1 != 0) if (oldInfo.ifIRinfo.rs1 == oldInfo.idIRinfo.rd) {
            pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.ifIR), Assembler.decode(oldInfo.idIR), PipelineInfo.Type.StallINS));
            stall(1);
        } else if (oldInfo.ifIRinfo.rs1 == oldInfo.exIRinfo.rd) {
            if (oldInfo.memIRinfo.type == InstructionType.Load) {
                pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.ifIR), Assembler.decode(oldInfo.exIR), PipelineInfo.Type.StallINS));
                stall(1);
            } else {
                pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.ifIR), Assembler.decode(oldInfo.exIR), PipelineInfo.Type.Ex2ID));
                forwardData[0] = oldInfo.exAluout;
            }
        } else if (oldInfo.ifIRinfo.rs1 == oldInfo.memIRinfo.rd) {
            if (oldInfo.memIRinfo.type == InstructionType.Load) {
                pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.ifIR), Assembler.decode(oldInfo.memIR), PipelineInfo.Type.LWD2ID));
                forwardData[0] = oldInfo.memLMD;
            } else {
                pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.ifIR), Assembler.decode(oldInfo.memIR), PipelineInfo.Type.Aout2ID));
                forwardData[0] = oldInfo.memAluout;
            }
        }
        if ((oldInfo.idIRinfo.type == InstructionType.Load || oldInfo.idIRinfo.type == InstructionType.Store || oldInfo.idIRinfo.type == InstructionType.RegImm || oldInfo.idIRinfo.type == InstructionType.RegReg) && oldInfo.idIRinfo.rs1 != 0) if (oldInfo.idIRinfo.rs1 == oldInfo.exIRinfo.rd) {
            if (oldInfo.exIRinfo.type == InstructionType.Load) {
                pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.idIR), Assembler.decode(oldInfo.exIR), PipelineInfo.Type.StallINS));
                stall(2);
            } else {
                pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.idIR), Assembler.decode(oldInfo.exIR), PipelineInfo.Type.Ex2A));
                forwardData[1] = oldInfo.exAluout;
            }
        } else if (oldInfo.idIRinfo.rs1 == oldInfo.memIRinfo.rd) {
            if (oldInfo.memIRinfo.type == InstructionType.Load) {
                pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.idIR), Assembler.decode(oldInfo.memIR), PipelineInfo.Type.LWD2ID));
                forwardData[1] = oldInfo.memLMD;
            } else {
                pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.idIR), Assembler.decode(oldInfo.memIR), PipelineInfo.Type.Aout2A));
                forwardData[1] = oldInfo.memAluout;
            }
        }
        if (oldInfo.idIRinfo.type == InstructionType.RegReg && oldInfo.idIRinfo.rs2 != 0) if (oldInfo.idIRinfo.rs2 == oldInfo.exIRinfo.rd) {
            if (oldInfo.exIRinfo.type == InstructionType.Load) {
                pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.idIR), Assembler.decode(oldInfo.exIR), PipelineInfo.Type.StallINS));
                stall(2);
            } else {
                pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.idIR), Assembler.decode(oldInfo.exIR), PipelineInfo.Type.Ex2B));
                forwardData[2] = oldInfo.exAluout;
            }
        } else if (oldInfo.idIRinfo.rs2 == oldInfo.memIRinfo.rd) {
            if (oldInfo.memIRinfo.type == InstructionType.Load) {
                pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.idIR), Assembler.decode(oldInfo.memIR), PipelineInfo.Type.LWD2B));
                forwardData[2] = oldInfo.memLMD;
            } else {
                pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.idIR), Assembler.decode(oldInfo.memIR), PipelineInfo.Type.Aout2B));
                forwardData[2] = oldInfo.memAluout;
            }
        }
        if (oldInfo.exIRinfo.type == InstructionType.Store && oldInfo.exIRinfo.rs2 != 0) if (oldInfo.exIRinfo.rs2 == oldInfo.memIRinfo.rd) {
            if (oldInfo.memIRinfo.type == InstructionType.Load) {
                pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.exIR), Assembler.decode(oldInfo.memIR), PipelineInfo.Type.LWD2Mem));
                forwardData[3] = oldInfo.memLMD;
            } else {
                pipelineInfo.add(new PipelineInfo(Assembler.decode(oldInfo.exIR), Assembler.decode(oldInfo.memIR), PipelineInfo.Type.Aout2Mem));
                forwardData[3] = oldInfo.memAluout;
            }
        }
    }

    /**
	 * ���жϳ�stall����һ������stall����
	 * @param segIndex ��0~segIndex�Ķζ�Ҫstall
	 */
    private void stall(int segIndex) {
        for (int i = 0; i < segIndex; i++) {
            isStall[i] = 1;
        }
        isStall[segIndex] = 2;
        stallCounter++;
    }

    public Number getRegVal(int regIndex) {
        if (regIndex == -1) return 0; else if (regIndex < InstructionInfo.FOFF) return new Integer(register.R[regIndex]); else if (regIndex == InstructionInfo.FSTATE) return new Integer(register.fState); else return new Float(register.F[regIndex - InstructionInfo.FOFF]);
    }

    @Override
    public void setMemory(IMemory memory) {
        this.memory = memory;
    }

    /**
	 * ��ȡ�ڴ�
	 * @return �ڴ�
	 */
    public IMemory getMemory() {
        return memory;
    }

    @Override
    public void setRegister(Register register) {
        this.register = register;
    }

    /**
	 * ��ȡ�Ĵ���
	 * @return �Ĵ���
	 */
    public Register getRegister() {
        return register;
    }

    /**
	 * ��ȡ��ǰIP
	 * @return ��ǰ��IP
	 */
    @Override
    public int getIp() {
        return newInfo.ifPC;
    }

    public PipeLineReg getNewInfo() {
        return newInfo;
    }
}
