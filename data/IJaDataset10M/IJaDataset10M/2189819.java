package r2q2.content.ws;

public interface ContentUnpacker_PortType extends java.rmi.Remote {

    public byte[] processQTIItem(byte[] _xml, java.lang.String _itemPath) throws java.rmi.RemoteException, r2q2.content.ws.UnavailableFileException, r2q2.content.ws.UnsupportedProtocolException, r2q2.content.ws.NoSuchPropertyException, r2q2.content.ws.JDOMException;

    public r2q2.content.ws.EncodedFileList collateFiles(java.lang.String _xml, java.lang.String _xmlName, java.lang.String _httpRoot) throws java.rmi.RemoteException, r2q2.content.ws.UnavailableFileException, r2q2.content.ws.DuplicateFileException, r2q2.content.ws.UnsupportedProtocolException, r2q2.content.ws.NoSuchFileException, r2q2.content.ws.JDOMException, r2q2.content.ws.NoSuchPropertyException;

    public r2q2.content.ws.EncodedFileList unpack(java.lang.String _xmlName, java.lang.String _httpRoot) throws java.rmi.RemoteException, r2q2.content.ws.NoAttachmentException, r2q2.content.ws.UnpackingException;
}
