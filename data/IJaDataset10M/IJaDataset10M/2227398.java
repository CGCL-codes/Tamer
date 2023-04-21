package agex.view;

import java.io.BufferedReader;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import utils.xml.Utils;

public class TraderSnapShotTO {

    private String idAsset;

    private String idStrategy;

    private double money;

    private int shares;

    private double assetPrice;

    private int order;

    public TraderSnapShotTO() {
    }

    public String toXML() {
        StringBuffer str = new StringBuffer("");
        str.append("<trader>\n");
        str.append("<idAsset value=\"" + idAsset + "\"/>\n");
        str.append("<idStrategy value=\"" + idStrategy + "\"/>\n");
        str.append("<money value=\"" + money + "\"/>\n");
        str.append("<shares value=\"" + shares + "\"/>\n");
        str.append("<assetPrice value=\"" + assetPrice + "\"/>\n");
        str.append("<order value=\"" + order + "\"/>\n");
        str.append("</trader>\n");
        return str.toString();
    }

    public String toString() {
        StringBuffer str = new StringBuffer("");
        str.append(idAsset + ";");
        str.append(idStrategy + ";");
        str.append(money + ";");
        str.append(shares + ";");
        str.append(assetPrice + ";");
        str.append(order + ";");
        return str.toString();
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public double getCurrentPosition() {
        return money + assetPrice * shares;
    }

    public TraderSnapShotTO(String asset, String strategy, double money, int shares, double price) {
        super();
        idAsset = asset;
        idStrategy = strategy;
        this.money = money;
        this.shares = shares;
        assetPrice = price;
    }

    public double getAssetPrice() {
        return assetPrice;
    }

    public void setAssetPrice(double assetPrice) {
        this.assetPrice = assetPrice;
    }

    public String getIdAsset() {
        return idAsset;
    }

    public void setIdAsset(String idAsset) {
        this.idAsset = idAsset;
    }

    public String getIdStrategy() {
        return idStrategy;
    }

    public void setIdStrategy(String idStrategy) {
        this.idStrategy = idStrategy;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public static TraderSnapShotTO parse(Element element) throws Exception {
        TraderSnapShotTO trader = new TraderSnapShotTO();
        if (element != null) {
            NodeList lista = element.getElementsByTagName("idAsset");
            if (lista.getLength() == 1) trader.setIdAsset(Utils.getAtributteValue(lista.item(0)));
            lista = element.getElementsByTagName("idStrategy");
            if (lista.getLength() == 1) trader.setIdStrategy(Utils.getAtributteValue(lista.item(0)));
            lista = element.getElementsByTagName("money");
            if (lista.getLength() == 1) trader.setMoney(Double.parseDouble(Utils.getAtributteValue(lista.item(0))));
            lista = element.getElementsByTagName("shares");
            if (lista.getLength() == 1) trader.setShares(Integer.parseInt(Utils.getAtributteValue(lista.item(0))));
            lista = element.getElementsByTagName("assetPrice");
            if (lista.getLength() == 1) trader.setAssetPrice(Double.parseDouble(Utils.getAtributteValue(lista.item(0))));
            lista = element.getElementsByTagName("order");
            if (lista.getLength() == 1) trader.setOrder(Integer.parseInt(Utils.getAtributteValue(lista.item(0))));
            return trader;
        }
        return null;
    }

    public static TraderSnapShotTO parse(BufferedReader bin) throws Exception {
        TraderSnapShotTO trader = new TraderSnapShotTO();
        String str;
        if ((str = bin.readLine()) != null) {
            String dados[] = str.split(";");
            if (dados == null || dados.length < 6) throw new Exception("Arquivo Invalido - Leitura SocietySnapShot");
            trader.setIdAsset(dados[0]);
            trader.setIdStrategy(dados[1]);
            trader.setMoney(Double.parseDouble(dados[2]));
            trader.setShares(Integer.parseInt(dados[3]));
            trader.setAssetPrice(Double.parseDouble(dados[4]));
            trader.setOrder(Integer.parseInt(dados[5]));
            return trader;
        }
        return null;
    }
}
