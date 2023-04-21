package net.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import logic.Template;
import net.retrievers.Retriever;

public class Client {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry("127.0.0.1");
            Retriever<Template> r = (Retriever<Template>) registry.lookup("r_" + "logic.Template");
            System.out.println(r.add(null, ""));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
