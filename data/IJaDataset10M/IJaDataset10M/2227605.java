package jblip.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MultipartData {

    static final byte[] CRLF = "\r\n".getBytes();

    private final String boundary;

    private final List<DataPart> parts;

    public MultipartData() {
        parts = new LinkedList<DataPart>();
        final Random rand = new Random();
        byte[] boundary_bin = new byte[12];
        rand.nextBytes(boundary_bin);
        boundary = Base64Encoder.encode(boundary_bin);
        System.err.println("Multipart data boundary: " + boundary);
    }

    public void addText(final String name, final String content) {
        final String disposition = String.format("form-data; name=\"%s\";", name);
        parts.add(new DataPart(disposition, "text/plain", content));
    }

    public void addStreamData(final String name, final InputStream stream) {
        final String disposition = String.format("form-data; name=\"%s\"; filename=\"unknown\"", name);
        parts.add(new DataPart(disposition, "application/octet-stream", stream));
    }

    public void addFileData(final String name, final File file) throws FileNotFoundException {
        if (!file.canRead() || !file.isFile()) {
            throw new IllegalArgumentException("File isn't a regular file or is inaccessible");
        }
        final String disposition = String.format("form-data; name=\"%s\"; filename=\"%s\"", name, file.getName());
        parts.add(new DataPart(disposition, "application/octet-stream", new FileInputStream(file)));
    }

    public void writeToStream(final OutputStream output) throws IOException {
        final byte[] bound_bytes = ("--" + boundary).getBytes("UTF-8");
        for (DataPart part : parts) {
            output.write(bound_bytes);
            output.write(CRLF);
            part.writeToStream(output);
        }
        output.write(("--" + boundary + "--").getBytes("UTF-8"));
        output.write(CRLF);
    }

    public String getContentType() {
        return String.format("multipart/form-data; boundary=%s", boundary);
    }
}

class DataPart {

    final String disposition;

    final String type;

    final InputStream input_stream;

    final String input_sequence;

    DataPart(final String disposition, final String type, final String input) {
        this.disposition = disposition;
        this.type = type;
        this.input_sequence = input;
        this.input_stream = null;
    }

    DataPart(final String disposition, final String type, final InputStream input) {
        this.disposition = disposition;
        this.type = type;
        this.input_sequence = null;
        this.input_stream = input;
    }

    void writeToStream(final OutputStream output) {
        final String content_disposition = String.format("Content-Disposition: %s\r\n", this.disposition);
        final String content_type = String.format("Content-Type: %s\r\n", this.type);
        try {
            output.write(content_disposition.getBytes("UTF-8"));
            output.write(content_type.getBytes("UTF-8"));
            output.write(MultipartData.CRLF);
            if (input_sequence != null) {
                output.write(input_sequence.getBytes("UTF-8"));
                output.write(MultipartData.CRLF);
            } else if (input_stream != null) {
                final byte[] buff = new byte[1024];
                int read = 0;
                while ((read = input_stream.read(buff)) != -1) {
                    output.write(buff, 0, read);
                }
                output.write(MultipartData.CRLF);
                input_stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
