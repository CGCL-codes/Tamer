package org.sourceforge.jvb3d.Network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

/**
 * @author �ukasz Krzy�ak
 * 
 * Klasa odpowiedzialna za kontakt pakietu Network z reszt� "�wiata", oraz
 * inicjalizacj� cz�ci sieciowej - cz�� serwerowa.
 */
public class ServerFacade extends NetworkServerFacade {

    private ClientManager clientManager;

    private NetworkReceiver receiver;

    private ReceivedPacketBuffer receiveBuffer;

    private Logger fLogger = Logger.getLogger("ServerFacade");

    /**
	 * tworzy i inicjalizuje podsystem sieci w wersji serwerowej.
	 * 
	 */
    public ServerFacade() {
    }

    /**
	 * wysy�a do IModelNetwork informacj� o konieczno�ci utworzenia nowego
	 * avatara u�ytkownika. Zwraca ID avatara, kt�ry musi zosta� wys�any do
	 * klienta.
	 * 
	 * @return ID avatara
	 */
    public String createAvatar() {
        return modelInterface.createPlayer();
    }

    /**
	 * zwraca now� fabryke obiekt�w ClientData.
	 * @return nowa ClientFactory
	 */
    ClientFactory getClientFactory() {
        return new ClientDataFactory();
    }

    /**
	 * powoduje przes�anie update otrzymanego z sieci do modelu. Ma wywo�ywa�
	 * metod� update z interfejsu IModelNetwork.
	 * @param packet pakiet do przetworzenia
	 */
    void forwardUpdate(UpdatePacket packet) {
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(packet.getUpdateData());
            modelInterface.readExternal(new ObjectInputStream(byteStream));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * uruchamiana gdy wyst�pi timeout klienta. Ma wywo�a� metod� removeObject z
	 * avatarem do usuni�cia
	 * @param clientAvatar ID avatara klienta
	 */
    public void forwardClientTimeout(String clientAvatar) {
        modelInterface.removeObject(clientAvatar);
    }

    /**
	 * pobiera dane z modelu, i rozsy�a je do wszystkich klient�w.
	 * 
	 * @param avatarID
	 *            id obiektu kt�ry mamy uaktualni�
	 */
    public void sendUpdate(String avatarID) {
        modelInterface.setSerialize(avatarID);
        sendUpdate();
    }

    /**
	 * pobiera dane z modelu, i rozsy�a je do wszystkich klient�w.
	 *  
	 */
    public void sendAllUpdate() {
        modelInterface.setSerializeAll();
        sendUpdate();
    }

    private void sendUpdate() {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            modelInterface.writeExternal(objectStream);
            clientManager.sendUpdate(byteStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            fLogger.warning("Z�apano wyj�tek w ServerFacade.sendUpdate " + e);
        }
    }

    /**
	 * powoduje uruchomienie serwera i nas�uch na podanym porcie
	 * @param port port na kt�rym serwer ma s�ucha�
	 * @throws IOException je�li wyst�pi b��d socketu
	 */
    public void start(int port) throws IOException {
        receiver = new NetworkReceiver(port);
        receiveBuffer = new ReceivedPacketBuffer();
        receiver.connectToBuffer(receiveBuffer);
        clientManager = new ClientManager();
        receiveBuffer.addObserver(clientManager);
        receiver.start();
    }
}
