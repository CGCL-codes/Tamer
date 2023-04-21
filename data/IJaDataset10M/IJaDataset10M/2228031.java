package uk.gov.dti.og.fox.io;

import java.io.InputStream;
import java.sql.SQLException;
import oracle.sql.BLOB;
import uk.gov.dti.og.fox.FoxRequest;
import uk.gov.dti.og.fox.UCon;
import uk.gov.dti.og.fox.XFUtil;
import uk.gov.dti.og.fox.ex.ExInternal;
import uk.gov.dti.og.fox.track.Track;

public class StreamParcelInputBLOB extends Track implements StreamParcelInput {

    private String mFileName;

    private String mPath;

    private String mFileType;

    private BLOB mBlobRef;

    private String mSize = "";

    /**
   * Constructor to create a new StreamParcelInputBLOB
   * 
   * @param pFileName The name of the file
   * @param pPath The directory mPath of the file
   * @param pBlobRef The reference to the blob data
   */
    public StreamParcelInputBLOB(String pFileName, String pPath, String pFileType, BLOB pBlobRef) {
        mFileName = pFileName;
        if (pPath.length() == 0) {
            mPath = "";
        } else if (pPath.equals("\\")) {
            mPath = "";
        } else if (pPath.endsWith("\\")) {
            mPath = pPath;
        } else {
            mPath = pPath + "\\";
        }
        mBlobRef = pBlobRef;
        mFileType = pFileType;
    }

    public StreamParcelInputBLOB(String pFileName, String pPath, String pFileType, BLOB pBlobRef, String pSize) {
        this(pFileName, pPath, pFileType, pBlobRef);
        mSize = pSize;
    }

    /**
   * Get the filename of this StreamParcelInput
   * 
   * @return  The filename
   */
    public String getFileName() {
        return mFileName;
    }

    /**
   * Get the mPath of this StreamParcelInput
   * 
   * @return The mPath
   */
    public String getPath() {
        return mPath;
    }

    /**
   * Get an InputStream for this StreamParcelInput
   * 
   * @param pFoxRequest
   * @return  The InputStream
   */
    public InputStream getInputStream() {
        try {
            return mBlobRef.getBinaryStream();
        } catch (SQLException e) {
            throw new ExInternal("Error accessing BLOB data from StreamParcel", e);
        }
    }

    public String getFileType() {
        return mFileType;
    }

    public long getSize() {
        if (!XFUtil.isNull(mSize)) {
            return Long.parseLong(mSize);
        } else {
            return mBlobRef.getLength();
        }
    }
}
