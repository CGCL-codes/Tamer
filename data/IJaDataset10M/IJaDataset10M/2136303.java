package emulator.util.file;

import java.io.FileInputStream;
import java.io.IOException;
import emulator.hardware.memory.MemoryBlockInterface;
import emulator.shell.DebugShell;

public class FileLoadStrategyPcVic implements FileLoadStrategy {

    @Override
    public void loadToMemory(String file_name, MemoryBlockInterface memory, int start_address, int block_size) {
        FileInputStream file = null;
        try {
            file = new FileInputStream(file_name);
            skipInput(file, 0x18);
            int register_block_size = file.read();
            register_block_size += 256 * file.read();
            skipInput(file, register_block_size);
            readByterun(file, memory, 0x0000, 0x8000);
            readByterun(file, memory, 0x9000, 0x3000);
        } catch (IOException e) {
            DebugShell.getInstance().getErr().println("File read failed for \"" + file_name + "\" (prg): " + e.getMessage());
        } finally {
            if (file != null) try {
                file.close();
            } catch (IOException e) {
            }
        }
    }

    private void readByterun(FileInputStream file, MemoryBlockInterface memory, int start, int count) throws IOException {
        while (count > 0) {
            int leader_byte = file.read();
            if (leader_byte < 0) break;
            if (leader_byte < 128) {
                for (int i = 0; i <= leader_byte; i++) {
                    int by = file.read();
                    if (by < 0) break;
                    memory.getData()[start++] = (byte) by;
                    count--;
                }
            } else if (leader_byte > 128) {
                int by = file.read();
                if (by < 0) break;
                for (int i = 0; i < 257 - leader_byte; i++) {
                    memory.getData()[start++] = (byte) by;
                    count--;
                }
            }
        }
        if (count > 0) System.err.println("Not enough bytes read: " + count);
    }

    private void skipInput(FileInputStream file, int num_bytes) throws IOException {
        while (num_bytes > 0) num_bytes -= file.skip(num_bytes);
    }
}
