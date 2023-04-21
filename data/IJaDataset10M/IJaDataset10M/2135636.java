package emu.hw;

import java.util.logging.Logger;
import emu.util.Utilities;

/**
 * Representation of Physical Memory
 * @author b.j.drew@gmail.com
 * @author willaim.mosley@gmail.com
 * @author claytonannam@gmail.com
 *
 */
public class PhysicalMemory {

    /**
	 * For tracing
	 */
    Logger trace = Logger.getLogger("emuos");

    /**
	 * blanks to initialize memeory to
	 */
    public static final String BLANKS = "    ";

    /**
	 * Memory array
	 */
    char[][] memory;

    /**
	 * Constructor 
	 */
    public PhysicalMemory(int size, int wordLength) {
        memory = new char[size][wordLength];
    }

    /**
	 * Write 1 block into memory
	 * @param addr
	 */
    public void writeBlock(int addr, String data) {
        trace.info("writeBlock(): " + addr + ":" + data);
        data = Utilities.padStringToLength(data, " ", 40, false);
        int blockAddr = getBlockAddr(addr);
        for (int i = 0; i < 10; i++) {
            String word = data.substring(0, 4);
            store(blockAddr + i, word);
            data = data.substring(4);
        }
    }

    /**
	 * Read 1 block from memory
	 * @param addr
	 * @return
	 */
    public String readBlock(int addr) {
        trace.info("readBlock(): " + addr);
        String block = "";
        int blockAddr = getBlockAddr(addr);
        for (int i = 0; i < 10; i++) {
            block += new String(memory[blockAddr + i]);
        }
        return block;
    }

    /**
	 * Converts any address into a block address
	 * @param addr
	 * @return
	 */
    private int getBlockAddr(int addr) {
        int blockOffset = addr % 10;
        int blockAddr = addr - blockOffset;
        trace.fine(blockOffset + "," + blockAddr);
        return blockAddr;
    }

    /**
	 * Load a word from memory
	 * @param addr
	 * @return
	 */
    public String load(int addr) {
        return new String(memory[addr]);
    }

    /**
	 * Store a word into memory
	 * @param addr
	 */
    public void store(int addr, String data) {
        memory[addr] = data.toCharArray();
    }

    /**
	 * Dumps the memory contents to a single string
	 */
    public String toString() {
        int i;
        String dump = "0   |1   |2   |3   |4   |5   |6   |7   |8   |9   |\n";
        for (i = 0; i < memory.length; i = i + 10) {
            dump += new String(memory[i]) + " ";
            dump += new String(memory[i + 1]) + " ";
            dump += new String(memory[i + 2]) + " ";
            dump += new String(memory[i + 3]) + " ";
            dump += new String(memory[i + 4]) + " ";
            dump += new String(memory[i + 5]) + " ";
            dump += new String(memory[i + 6]) + " ";
            dump += new String(memory[i + 7]) + " ";
            dump += new String(memory[i + 8]) + " ";
            dump += new String(memory[i + 9]) + "|";
            dump += i + "\n";
        }
        return dump;
    }

    /**
	 * Clears memory.
	 */
    public void clear() {
        memory = new char[100][4];
        for (int i = 0; i < memory.length; i++) {
            memory[i] = BLANKS.toCharArray();
        }
    }
}
