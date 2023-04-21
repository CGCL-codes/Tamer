package eibstack.layer7;

public interface A_DataConnectedService {

    public void setListener(Listener l);

    public A_Connection connect(int da, A_Connection.Listener l);

    public interface Listener {

        public A_Connection.Listener connected(int sa, A_Connection conn);
    }
}
