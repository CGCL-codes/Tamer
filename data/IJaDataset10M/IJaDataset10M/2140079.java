package org.systemsbiology.apps.gui.server.provider.lob;

import java.io.InputStream;
import org.systemsbiology.apps.gui.server.LargeClobFile;
import org.systemsbiology.apps.gui.server.MediumBlobFile;
import org.systemsbiology.apps.gui.server.MediumClobFile;

/**
 * Interface for the large object file provider
 * <p> Includes support for Blobs and Clobs of various sizes
 * <p>MediumBlob - A binary large object column with a maximum length of 16777215 (2^24 - 1) bytes.
 * <p>LongBlob/LargeBlob - A binary large object column with a maximum length of 4294967295 (2^32 - 1) bytes. 
 * @author Mark Christiansen
 * 
 * @see LargeClobFile
 * @see MediumBlobFile
 * @see MediumClobFile
 */
public interface ILobProvider {

    /**
	 * @param bArray array of bytes to write to file
	 * @param fileName name of file
	 * @return MediumBlobFile
	 */
    public MediumBlobFile createMediumBlobFile(byte[] bArray, String fileName);

    /**
	 * @param is InputStream used to construct MediumBlobFile
	 * @param fileName name of file
	 * @return MediumBlobFile
	 */
    public MediumBlobFile createMediumBlobFile(InputStream is, String fileName);

    /**
	 * @param id identifier of MediumBlobFile to retrieve from the database
	 * @return MediumBlobFile
	 */
    public MediumBlobFile getMediumBlobFile(Long id);

    /**
	 * @param mediumBlobFile MediumBlobFile to delete
	 * @return boolean - success of deletion
	 */
    public boolean deleteMediumBlobFile(MediumBlobFile mediumBlobFile);

    /**
	 * @param string String of characters to create MediumClobFile for
	 * @param fileName name of file
	 * @return MediumClobFile
	 */
    public MediumClobFile createMediumClobFile(String string, String fileName);

    /**
	 * @param id identifier of MediumClobFile to retrieve from the database
	 * @return MediumClobFile
	 */
    public MediumClobFile getMediumClobFile(Long id);

    /**
	 * @param mediumClobFile MediumClobFile to delete
	 * @return boolean - success of deletion
	 */
    public boolean deleteMediumClobFile(MediumClobFile mediumClobFile);

    /**
	 * @param string String of characters to create LargeClobFile (aka Long Clob) for
	 * @param fileName name of file
	 * @return LargeClobFile
	 */
    public LargeClobFile createLargeClobFile(String string, String fileName);

    /**
	 * @param id identifier of LargeClobFile to retrieve from the database
	 * @return LargeClobFile
	 */
    public LargeClobFile getLargeClobFile(Long id);

    /**
	 * @param largeClobFile LargeClobFile to delete from the database
	 * @return boolean - success of deletion
	 */
    public boolean deleteLargeClobFile(LargeClobFile largeClobFile);
}
