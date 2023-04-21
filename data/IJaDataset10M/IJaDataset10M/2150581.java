package fr;

public class SommerSoapBindingImpl implements fr.Sommer {

    public int getsomme(int in0, int in1) throws java.rmi.RemoteException {
        return in0 + in1;
    }
}
