package net.sourceforge.tile3d.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import net.sourceforge.tile3d.core.ClientServerConstants;
import net.sourceforge.tile3d.view.adminGUI.AdminMainForm;

public class ServerOneThread extends Thread implements ClientServerConstants {

    Socket client = null;

    ObjectOutputStream objectOutputStream;

    ObjectInputStream objectInputStream;

    TranferObject currentTranferObject;

    private AdminMainForm m_mainForm;

    /**
     * @param p_mainForm
     *
     */
    public ServerOneThread(Socket client, AdminMainForm p_mainForm) {
        this.client = client;
        m_mainForm = p_mainForm;
        currentTranferObject = new TranferObject();
        try {
            objectInputStream = new ObjectInputStream(client.getInputStream());
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                TranferObject tranferObject = (TranferObject) objectInputStream.readObject();
                sendObjectFromMessage(tranferObject);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendObjectFromMessage(TranferObject tranferObject) {
        System.out.println("<server message:> " + tranferObject.getMessage());
        if (tranferObject.getMessage().equals(SEND_INFO_CUSTOMER)) {
            System.out.println(tranferObject.getObject());
            CustomerInfomation customerInfomation = (CustomerInfomation) tranferObject.getObject();
            m_mainForm.addCustomerInfomation(customerInfomation);
        } else if (tranferObject.getMessage().equals(THE_END)) {
            try {
                objectInputStream.close();
                objectInputStream.close();
                client.close();
            } catch (IOException e) {
            }
        }
    }

    public TranferObject getCurrentTranferObject() {
        return currentTranferObject;
    }

    public void setCurrentTranferObject(TranferObject p_currentTranferObject) {
        currentTranferObject = p_currentTranferObject;
    }
}
