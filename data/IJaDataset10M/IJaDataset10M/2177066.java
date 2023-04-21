package edu.ksu.cis.bnj.ver3.streams;

import java.io.OutputStream;

/**
 * file: Exporter.java
 * 
 * @author Jeffrey M. Barber
 */
public interface Exporter {

    /**
	 * export to a stream
	 * 
	 * @param out
	 *            the stream to save too
	 */
    public void save(OutputStream out);

    public String getExt();

    public String getDesc();

    public OmniFormatV1 getStream1();
}
