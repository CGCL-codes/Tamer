package ru.usu.urmi.serviceFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ru.usu.urmi.exceptions.RMIException;
import ru.usu.urmi.transport.ExceptionResponsePacket;
import ru.usu.urmi.transport.Transport;

/**
 * ������� ����� ��������� �������. ��������� ��������, ������� ��� ���
 * ��������� ����� � ���� �������.
 * 
 * @author floatdrop
 * 
 */
public class ClientWaiter implements Runnable {

    private LocalSysId m_lsid;

    private boolean working = true;

    private HashMap<String, Class<?>> st_servicesMap;

    private HashMap<UUID, Object> st_objectsMap;

    private ExecutorService m_threadPool = Executors.newFixedThreadPool(100);

    public ClientWaiter(HashMap<String, Class<?>> servicesMap, HashMap<UUID, Object> objectsMap) {
        st_servicesMap = servicesMap;
        st_objectsMap = objectsMap;
    }

    public ClientWaiter setLocalSysId(LocalSysId lsid) {
        m_lsid = lsid;
        return this;
    }

    public void run() {
        while (working) {
            Transport clientTransport = null;
            try {
                clientTransport = m_lsid.getServerTransport().accept();
                Thread cp = new ClientProcessor(clientTransport, st_servicesMap, st_objectsMap);
                m_threadPool.execute(cp);
            } catch (Throwable e) {
                if (clientTransport != null) {
                    try {
                        clientTransport.send(new ExceptionResponsePacket(new RMIException(RMIException.serverError)));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                throw new RuntimeException(e);
            }
        }
    }
}
