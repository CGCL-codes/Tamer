package malictus.klang.test;

import java.io.*;
import malictus.klang.*;
import malictus.klang.file.*;
import malictus.klang.chunk.*;

/**
 * FileReadWriteTest is a test to read, parse, and write data to a sample WAV file. It
 * tests file reading and writing, as well as checksums.
 * @author Jim Halliday
 */
public class FileReadWriteTest {

    private static final String wavSamp = "samples/sample.wav";

    /**
	 * Run the FileInfoTest application.
	 * @param args currently not used
	 */
    public static void main(String[] args) {
        System.out.println("File read/write test - Using Klang Version " + KlangConstants.KLANG_VERSION + "\n");
        File x = new File(wavSamp);
        System.out.println("Testing " + x.getPath());
        RandomAccessFile raf = null;
        try {
            System.out.println("Calculating checksum of original file");
            String checksum = KlangUtil.getChecksum(x, 0, x.length());
            System.out.println("Copying... ");
            File temp = File.createTempFile("klang", ".data");
            KlangUtil.copyFile(x, temp);
            System.out.println("Verifying checksum...");
            String newchecksum = KlangUtil.getChecksum(temp, 0, temp.length());
            if (!(checksum.equals(newchecksum))) {
                throw new Exception("Checksums are not equal!");
            }
            System.out.println("Parsing");
            raf = new RandomAccessFile(temp, "rw");
            KlangFile kf = KlangFileFactory.makeNewKlangFile(temp, raf);
            if (!(kf instanceof WAVEFile)) {
                throw new Exception("File was not parsed correctly");
            }
            System.out.println("Exporting fmt chunk");
            Chunk fmtChunk = kf.getChunkNamed("fmt ", true);
            if (fmtChunk == null) {
                throw new Exception("FMT chunk does not exist");
            }
            File chunkTemp = File.createTempFile("klang", ".data");
            kf.exportChunk(fmtChunk, chunkTemp);
            System.out.println("Deleting fmt chunk");
            EditableContainerChunk riff = ((EditableContainerChunk) kf.getChunkNamed("RIFF", false));
            if (riff == null) {
                throw new Exception("RIFF chunk not found");
            }
            fmtChunk = kf.getChunkNamed("fmt ", true);
            fmtChunk.deleteChunk(raf);
            System.out.println("Verifying file validity");
            riff = ((EditableContainerChunk) kf.getChunkNamed("RIFF", false));
            if (((ContainerChunk) riff).getSubChunks().size() != 1) {
                throw new Exception("FMT chunk not deleted properly");
            }
            kf = KlangFileFactory.makeNewKlangFile(temp, raf);
            riff = ((EditableContainerChunk) kf.getChunkNamed("RIFF", false));
            if (((ContainerChunk) riff).getSubChunks().size() != 1) {
                throw new Exception("FMT chunk not deleted properly");
            }
            System.out.println("Reimporting fmt chunk");
            riff.addChunkFromFile(chunkTemp, "fmt ", 1, raf);
            System.out.println("Verifying file validity");
            riff = ((EditableContainerChunk) kf.getChunkNamed("RIFF", false));
            if (((ContainerChunk) riff).getSubChunks().size() != 2) {
                throw new Exception("FMT chunk not imported properly");
            }
            kf = KlangFileFactory.makeNewKlangFile(temp, raf);
            riff = ((EditableContainerChunk) kf.getChunkNamed("RIFF", false));
            if (((ContainerChunk) riff).getSubChunks().size() != 2) {
                throw new Exception("FMT chunk not imported properly");
            }
            System.out.println("Verifying checksum");
            newchecksum = KlangUtil.getChecksum(temp, 0, temp.length());
            if (!(checksum.equals(newchecksum))) {
                throw new Exception("Checksums are not equal!");
            }
            System.out.println("Altering fmt chunk data");
            fmtChunk = kf.getChunkNamed("fmt ", true);
            fmtChunk.getPrimitiveValueNamed(KlangConstants.PRIMITIVE_DATA_FMT_CHUNK_COMP_CODE).getPrimitive().setValueFromString("2");
            fmtChunk.reparseChunkPrimitives(raf);
            System.out.println("Verifying file validity and new value");
            WAVEFile wf = new WAVEFile(temp, raf);
            if (wf.getCompressionCode() != 2) {
                throw new Exception("Error altering fmt chunk data");
            }
            raf.close();
            System.out.println("Deleting... ");
            temp.delete();
            chunkTemp.delete();
            if (temp.exists()) {
                throw new Exception("Temp file was not deleted!");
            }
            if (chunkTemp.exists()) {
                throw new Exception("Chunktemp file was not deleted!");
            }
            System.out.println("All tests completed successfully!");
        } catch (Exception err) {
            err.printStackTrace();
            System.out.println("ERROR");
        }
    }
}
