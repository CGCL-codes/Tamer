package vsha.data;

import java.util.*;
import sale.*;
import vsha.*;

/**
 * Diese Klasse repraesentiert eine Bestellung der Versandhausagentur.
 * Es darf nur �ber die Methoden insertArticle() und removeArticle() eingef�gt
 * bzw. entfernt werden. Diese Methoden pr�fen gleichzeitig alle internen
 * Variablen und aktualisieren sie ggf. (z.B. Lieferzeit)
 * @author Daniel Schuster
 */
public class Order {

    private static final int m_nDeletionTime = 62;

    private int m_nOrderID = -1;

    private int m_nCreationDate = -1;

    private int m_nPickupDate = -1;

    private int m_nDeletionDate = -1;

    private int m_nReturnCount = 0;

    private int m_nOrderedCount = 0;

    private String m_sStatus = "leer";

    private String m_sCustomerID = "";

    private List m_lArticleList = new LinkedList();

    /**Konstante fuer Status: 'leer'*/
    public static final String STATUS_EMPTY = "leer";

    /**Konstante fuer Status: 'in Bearbeitung'*/
    public static final String STATUS_IN_PROCESS = "in Bearbeitung";

    /**Konstante fuer Status: 'bereit'*/
    public static final String STATUS_READY = "bereit";

    /**Konstante fuer Status: 'abgeholt'*/
    public static final String STATUS_PICKED_UP = "abgeholt";

    /**Konstante fuer Status: 'storniert'*/
    public static final String STATUS_CANCELLED = "storniert";

    /**
   * Setzt das Bestelldatum auf das aktuelle Datum,
   * Abholdatum = Bestelldatum; Loeschdatum nach 2 Monaten
   */
    public Order() {
        m_nCreationDate = ((Agency) Shop.getTheShop()).getDay();
        m_nPickupDate = m_nCreationDate;
        m_nDeletionDate = (m_nCreationDate + m_nDeletionTime);
        m_sStatus = STATUS_EMPTY;
    }

    /**
   * @return ID-Nummer der Order
   */
    public int getOrderID() {
        return this.m_nOrderID;
    }

    /**
   * Legt die ID-Nummer der Order fest.
   * @param nID die Nummer, mit der die ID belegt wird
   */
    public void setOrderID(int nID) {
        this.m_nOrderID = nID;
    }

    /**
   * Liefert das Erstelldatum der Order.
   * @return Erstelldatum Tag n der Simulation
   */
    public int getCreationDate() {
        return this.m_nCreationDate;
    }

    /**
   * Legt das Erstelldatum der Order fest.
   * @param nDate Datum als Tag n der Simulation
   */
    public void setCreationDate(int nDate) {
        this.m_nCreationDate = nDate;
    }

    /**
   * Liefert das Abholdatum der Order.
   * @return Abholdatum Tag n der Simulation
   */
    public int getPickupDate() {
        return this.m_nPickupDate;
    }

    /**
   * Legt das Abholdatum der Order fest.
   * @param nDate Datum als Tag n der Simulation
   */
    public void setPickupDate(int nDate) {
        this.m_nPickupDate = nDate;
    }

    /**
   * Liefert das Loeschdatum der Order.
   * @return Loeschdatum Tag n der Simulation
   */
    public int getDeletionDate() {
        return this.m_nDeletionDate;
    }

    /**
   * Legt das Lvschdatum der Order fest.
   * @param nDate Datum als Tag n der Simulation
   */
    public void setDeletionDate(int nDate) {
        this.m_nDeletionDate = nDate;
    }

    /**
   * Liefert die Customer-ID der Order.
   * @return ID des zugehoerigen Customers
   */
    public String getCustomerID() {
        return this.m_sCustomerID;
    }

    /**
   * Legt die Customer-ID der Order fest.
   * @param sID die ID des zugehoerigen Customers
   */
    public void setCustomerID(String sID) {
        this.m_sCustomerID = sID;
    }

    /**
   * Liefert alle Artikel der Order.
   * @return Liste der Artikel als LinkedList
   */
    public List getArticleList() {
        return this.m_lArticleList;
    }

    /**
   * Liefert die Anzahl aller bestellten Artikel der Order.
   * @return absolute Anzahl der bestellten Artikel
   */
    public int getOrderedCount() {
        Iterator it = m_lArticleList.iterator();
        Article a = new Article();
        int oc = 0;
        while (it.hasNext()) {
            a = (Article) it.next();
            oc += a.getCount();
        }
        return oc;
    }

    /**
   * Liefert die Anzahl der zurueckgegebenen Artikel einer Bestellung
   * @return Anzahl der zurueckgegebenen Artikel
   */
    public int getReturnedCount() {
        Iterator it = m_lArticleList.iterator();
        Article a = new Article();
        int oc = 0;
        while (it.hasNext()) {
            a = (Article) it.next();
            oc += a.getReturnCount();
        }
        return oc;
    }

    /**
   * Sucht einen Artikel in der Order und entfernt diesen ggf.
   * Identifikation erfolgt ueber Bestellnummer
   * @param article der zu suchende Artikel
   * @param true, wenn der Artikel entfernt werden soll
   * @return gefundener Artikel bzw. null wenn nicht enthalten
   */
    public Article findArticle(Article article, boolean remove) {
        Article a = new Article();
        int searchKey = article.getArticleNumber();
        Iterator it = m_lArticleList.iterator();
        while (it.hasNext()) {
            a = (Article) it.next();
            if (a.getArticleNumber() == searchKey) {
                if (remove) {
                    it.remove();
                }
                return a;
            }
        }
        return null;
    }

    /**
   * Gibt den Wert einer Order in Pfennigen an
   */
    public int getIntValue() {
        int sum = 0;
        Iterator it = m_lArticleList.iterator();
        Article a = new Article();
        while (it.hasNext()) {
            a = (Article) it.next();
            sum += (a.getPrice() * a.getCount());
        }
        return sum;
    }

    /**
   * Gibt den Wert einer Order an
   * @return String im Format: "120,00 DM"
   */
    public String getStrValue() {
        String txt = "" + getIntValue();
        int l = txt.length();
        for (int i = l; i < 3; ++i) txt = "0" + txt;
        txt = txt.substring(0, txt.length() - 2) + "," + txt.substring(txt.length() - 2, txt.length());
        txt = txt + " DM";
        return txt;
    }

    /**
   * Fuegt einen Artikel in die Order ein.
   * @param article der einzufuegende Artikel
   * @param nCount die Anzahl der einzufuegenden Artikel dieses Typs
   * @return true, wenn erfolgreich, false bei Fehler
   */
    public boolean insertArticle(Article article, int nCount) {
        Article a = findArticle(article, false);
        if (m_sStatus == STATUS_EMPTY) {
            m_sStatus = STATUS_IN_PROCESS;
        }
        if (m_nPickupDate < getCreationDate() + article.getDeliveryTime()) {
            m_nPickupDate = getCreationDate() + article.getDeliveryTime();
        }
        if (a != null) {
            a.setCount(a.getCount() + nCount);
            return true;
        } else {
            a = new Article();
            a.setArticleNumber(article.getArticleNumber());
            a.setCategory(article.getCategory());
            a.setDeliveryTime(article.getDeliveryTime());
            a.setDescription(article.getDescription());
            a.setName(article.getName());
            a.setPicFileName(article.getPicFileName());
            a.setPrice(article.getPrice());
            a.setProducer(article.getProducer());
            a.setReturnCount(article.getReturnCount());
            a.setCount(nCount);
            return m_lArticleList.add(a);
        }
    }

    /**
   * Entfernt einen Artikel aus der Order.
   * @param article der zu entfernende Artikel
   * @param nCount die Anzahl der zu entfernenden Artikel dieses Typs
   * @return true, wenn erfolgreich, false bei Fehler
   */
    public boolean removeArticle(Article article, int nCount) {
        int delivTime = m_nPickupDate - m_nCreationDate;
        Article a = findArticle(article, false);
        if (a != null) {
            int mCount = a.getCount();
            mCount -= nCount;
            if (mCount <= 0) {
                a = findArticle(a, true);
                if (delivTime <= article.getDeliveryTime()) {
                    Iterator i = m_lArticleList.iterator();
                    delivTime = 0;
                    while (i.hasNext()) {
                        Article ac = (Article) i.next();
                        if (ac.getDeliveryTime() > delivTime) {
                            delivTime = ac.getDeliveryTime();
                        }
                    }
                    m_nPickupDate = getCreationDate() + delivTime;
                }
                return true;
            }
            a.setCount(mCount);
            return true;
        } else {
            return false;
        }
    }

    /**
   * Prueft, ob alle Artikel der Order schon geliefert sind.
   * @return true, wenn alle geliefert, false sonst
   */
    public boolean checkDelivery() {
        return (((Agency) Shop.getTheShop()).getDay()) >= m_nPickupDate;
    }

    /**
   * Setzt den Status dieser Order. Dafuer gibt es der Klasse Order einige
   * statische Konstanten.
   * @param sStatus der Status, der gesetzt werden soll
   */
    public void setStatus(String sStatus) {
        this.m_sStatus = sStatus;
    }

    /**
   * @return den Status der Order.
   */
    public String getStatus() {
        return this.m_sStatus;
    }

    /**
   * F�gt alle Artikel einer anderen Order in die Order ein
   */
    public void insertOrder(Order o) {
        Article a = new Article();
        Iterator it = o.getArticleList().iterator();
        while (it.hasNext()) {
            a = (Article) it.next();
            this.insertArticle(a, a.getCount());
        }
    }
}
