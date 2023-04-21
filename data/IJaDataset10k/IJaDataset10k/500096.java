package net.sourceforge.plantuml.code;

import java.io.IOException;

public class TranscoderImpl implements Transcoder {

    private final Compression compression;

    private final URLEncoder urlEncoder;

    private final StringCompressor stringCompressor;

    public TranscoderImpl() {
        this(new AsciiEncoder(), new CompressionHuffman());
    }

    public TranscoderImpl(URLEncoder urlEncoder, Compression compression) {
        this(urlEncoder, new ArobaseStringCompressor(), compression);
    }

    public TranscoderImpl(URLEncoder urlEncoder, StringCompressor stringCompressor, Compression compression) {
        this.compression = compression;
        this.urlEncoder = urlEncoder;
        this.stringCompressor = stringCompressor;
    }

    public String encode(String text) throws IOException {
        final String stringAnnoted = stringCompressor.compress(text);
        final byte[] data = stringAnnoted.getBytes("UTF-8");
        final byte[] compressedData = compression.compress(data);
        return urlEncoder.encode(compressedData);
    }

    public String decode(String code) throws IOException {
        final byte compressedData[] = urlEncoder.decode(code);
        final byte data[] = compression.decompress(compressedData);
        return stringCompressor.decompress(new String(data, "UTF-8"));
    }
}
