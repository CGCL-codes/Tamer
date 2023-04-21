package sf.net.algotrade.iblink;

import java.util.HashMap;
import java.util.Observer;
import com.ib.client.Contract;
import com.ib.client.Order;

public interface IB {

    public static final int DEFAULT_PORT = 7496;

    public static final int ERR_NOT_CONNECTED = 504;

    public abstract boolean isConnected();

    public abstract int getLastOrderId();

    public abstract Integer placeOrder(Contract contract, Order order);

    public abstract Integer placeOrder(Contract contract, Order order, Observer observer);

    public abstract void cancelOrder(int id);

    public abstract void cancelOrderPermId(int permId);

    public abstract HashMap getOpenOrders();

    /**
	 * 
	 * @param string
	 *            host name. "" for localhost
	 * @param port
	 *            the server port to TWS
	 * @param clientID
	 *            IB TWS Client ID
	 */
    public abstract void connect(String string, int port, int clientId);

    public abstract void disconnect();

    public abstract IBOrderStatus getIBOrderStatusPermId(int permId);
}
