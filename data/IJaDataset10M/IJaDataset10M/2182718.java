package moxie.rw;

import ostore.util.RabinFingerprint;
import ostore.util.SecureHash;
import ostore.util.SHA1Hash;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class RabinUpdateEncoder extends BlockUpdateEncoder {

    private static final int MIN_BLOCK_SIZE = 1024;

    private static final int MAX_BLOCK_SIZE = 1024 * 8;

    private static final int FILE_BUFFER_SIZE = 1024 * 8;

    protected FileRecipe computeRecipe(String fs_path, String cache_path) {
        if (logger.isInfoEnabled()) logger.info("computing file recipe: path=" + fs_path);
        FileRecipe recipe = new FileRecipe();
        try {
            File f = new File(cache_path);
            FileInputStream f_in = new FileInputStream(f);
            FileChannel f_channel = f_in.getChannel();
            RabinFingerprint rf = new RabinFingerprint(MIN_BLOCK_SIZE, MAX_BLOCK_SIZE);
            byte[] rf_chunk_buffer = new byte[rf.getMaxSize()];
            int[] rf_offset = { 0 };
            int num_chunks = 0;
            int fpos = 0;
            boolean eof = false;
            while (!eof) {
                ByteBuffer file_buffer = ByteBuffer.allocate(FILE_BUFFER_SIZE);
                do {
                    int read_length = f_channel.read(file_buffer);
                    eof = (read_length == -1);
                } while (file_buffer.hasRemaining() && (!eof));
                if (logger.isDebugEnabled()) logger.debug("reading source file: path=" + fs_path + " bytes_read=" + file_buffer.position() + " eof=" + eof);
                if (file_buffer.remaining() < FILE_BUFFER_SIZE) {
                    file_buffer.flip();
                    rf.add_data(file_buffer);
                }
                if (eof) {
                    rf.set_all_data_added();
                }
                while (true) {
                    int chunk_size = rf.next_chunk(rf_chunk_buffer, rf_offset);
                    if (chunk_size <= 0) {
                        break;
                    }
                    rf_offset[0] = 0;
                    if (logger.isDebugEnabled()) logger.debug("identified chunk: path=" + fs_path + " chunk_num=" + num_chunks + " file_offset=" + fpos + " chunk_size=" + chunk_size);
                    SecureHash hash = new SHA1Hash(rf_chunk_buffer, 0, chunk_size);
                    recipe.addChunk(hash, chunk_size, fpos);
                    fpos += chunk_size;
                    ++num_chunks;
                }
            }
            if (logger.isInfoEnabled()) logger.info("computed file recipe: path=" + fs_path + " num_chunks=" + num_chunks + " file_size=" + fpos);
        } catch (Exception e) {
            BUG("Failed to compute file recipe of file in local cache: " + "path=" + fs_path + " exception=" + e);
        }
        return recipe;
    }
}
