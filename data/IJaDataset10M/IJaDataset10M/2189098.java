package libjdc.dc.io;

/**
 *
 * @author root
 */
public class RemoteFile {

    private String localFileName;

    private int length;

    private int offset;

    public RemoteFile() {
        this.setLocalFileName("");
        this.setLength(0);
        this.setOffset(0);
    }

    /** Creates a new instance of RemotFile */
    private RemoteFile(String localFileName) {
        this.setLocalFileName(localFileName);
        this.setLength(0);
        this.setOffset(0);
    }

    /**
    public char[] readBytes() throws IOException{

        System.out.println("size:"+length);
        BufferedReader buffer = new BufferedReader(inputFile);
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
            throw new IOException("File is too large ");
        }
    
        // Create the byte array to hold the data
        char[] bytes = new char[(int)length];
    
        // Read in the bytes
        //int offset = 0;
        //int numRead = 0;
        while (offset < bytes.length
               && (readedBytes = buffer.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += readedBytes;
        }
        System.out.println("Ficheiro lido:"+bytes);
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file ");
        }
        
        buffer.close();
        //inputFile.close();
        
        return bytes;
    }*/
    public String getLocalFileName() {
        return localFileName;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
