package com.pixelmed.utils;

import java.io.*;

/**
 * <p>A class for copying an entire input stream to an output stream.</p>
 *
 * @author	dclunie
 */
public class CopyStream {

    private static final String identString = "@(#) $Header: /userland/cvs/pixelmed/imgbook/com/pixelmed/utils/CopyStream.java,v 1.7 2004/05/01 20:18:56 dclunie Exp $";

    private static final int readBufferSize = 32768;

    private CopyStream() {
    }

    /**
	 * <p>Copy an entire input stream to an output stream.</p>
	 *
	 * <p>The data is copied in chunks rather than as individual bytes, but the input and output
	 * streams are used as is, and no {@link java.io.BufferedInputStream BufferedInputStream}
	 * or {@link java.io.BufferedOutputStream BufferedOutputStream} is inserted; the caller
	 * is expected to do that if maximum performance is desired.</p>
	 *
	 * <p>Also, neither the input nor the output streams are explicitly closed after the
	 * copying has complete; the caller is expected to do that as well, since there may
	 * be occasions when there is more to be written to the output, or the input is to
	 * be rewound and reused, or whatever.</p>
	 *
	 * @param	in		the source
	 * @param	out		the destination
	 * @exception	IOException	thrown if the copying fails for any reason
	 */
    public static final void copy(InputStream in, OutputStream out) throws IOException {
        byte[] readBuffer = new byte[readBufferSize];
        while (true) {
            int n = in.read(readBuffer);
            if (n > 0) {
                out.write(readBuffer, 0, n);
            } else {
                break;
            }
        }
        out.flush();
    }

    /**
	 * <p>Copy an entire input file to an output file.</p>
	 *
	 * @param	inFile		the source
	 * @param	outFile		the destination
	 * @exception	IOException	thrown if the copying fails for any reason
	 */
    public static final void copy(File inFile, File outFile) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(inFile));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
        copy(in, out);
        in.close();
        out.close();
    }

    /**
	 * <p>Copy an entire input file to an output file.</p>
	 *
	 * @param	inFile		the source
	 * @param	outFile		the destination
	 * @exception	IOException	thrown if the copying fails for any reason
	 */
    public static final void copy(String inFile, String outFile) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(inFile));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
        copy(in, out);
        in.close();
        out.close();
    }
}
