package files.handlers;

import javax.xml.xpath.XPathExpressionException;
import network.ConnectionManager;
import org.w3c.dom.Element;
import protocol.ProtocolConnection;
import protocol.handlers.HandlePackets;
import protocol.handlers.PacketHandler;
import files.FileReceiptManager;
import files.packets.FilePart;

@HandlePackets("FilePart")
public class FileReceiptHandler extends PacketHandler {

    private FileReceiptManager frm;

    public FileReceiptHandler() {
        this.frm = FileReceiptManager.getInstance();
    }

    public FileReceiptHandler(FileReceiptManager manager) {
        this.frm = manager;
    }

    @Override
    public void handle(Element p, ProtocolConnection from) {
        FilePart filePart = null;
        try {
            filePart = new FilePart(p);
            this.frm.newFilePart(filePart, ConnectionManager.getInstance().getPeer(from));
        } catch (XPathExpressionException e) {
            return;
        }
    }
}
