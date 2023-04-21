package py.edu.ucom.cadira.net.serializables;

import org.jdom.Document;
import org.jdom.Element;
import py.edu.ucom.cadira.game.war.Board;
import py.edu.ucom.cadira.logic.Mesa;
import py.edu.ucom.cadira.net.XMLSerializable;

public class JoinResponse implements XMLSerializable {

    private static final String NODE_JOIN_TABLE = "joinTable";

    private Mesa mesa;

    private Board board;

    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public JoinResponse(Mesa mesa) {
        this.mesa = mesa;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public JoinResponse() {
        this.mesa = new Mesa();
        this.board = new Board();
    }

    public void fromXML(Document xml) {
        Element root = xml.getRootElement();
        Element joinTable = root.getChild(NODE_JOIN_TABLE);
        Element statusNode = joinTable.getChild("status");
        setStatus(Boolean.valueOf(statusNode.getValue()));
        Element tableNode = joinTable.getChild("table");
        mesa.setMesaId(Integer.parseInt(tableNode.getAttributeValue("id")));
        mesa.fromXml(tableNode);
        Element gameNode = joinTable.getChild("game");
        Element statusTableNode = gameNode.getChild("status");
        mesa.setStatusBoard(Boolean.valueOf(statusTableNode.getValue()));
        if (mesa.getStatusBoard()) {
            Element tableroNode = gameNode.getChild("tablero");
            board.fromXml(tableroNode);
        }
    }

    public void toXML(Element root) {
        Element joinTableNode = new Element(NODE_JOIN_TABLE);
        Element statusNode = new Element("status");
        statusNode.addContent(String.valueOf(isStatus()));
        joinTableNode.addContent(statusNode);
        Element tableNode = new Element("table");
        mesa.toXml(tableNode);
        joinTableNode.addContent(tableNode);
        Element gameNode = new Element("game");
        Element statusGameNode = new Element("status");
        statusGameNode.addContent(String.valueOf(mesa.getStatusBoard()));
        gameNode.addContent(statusGameNode);
        Element tableroNode = new Element("tablero");
        if (mesa.getStatusBoard()) {
            board.toXml(tableroNode);
            gameNode.addContent(tableroNode);
        }
        joinTableNode.addContent(gameNode);
        root.addContent(joinTableNode);
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JoinResponse)) {
            return false;
        }
        JoinResponse otherObj = (JoinResponse) obj;
        if (otherObj.getMesa().getMesaId().equals(getMesa().getMesaId())) {
            return true;
        }
        return false;
    }
}
