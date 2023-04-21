package vsha;

import vsha.data.*;
import vsha.gui.*;
import sale.*;
import users.*;
import java.util.*;

/**
* Stellt den Prozess zum Reklamieren dar. Umfasst das Ausw�hlen bis zur Best�tigung und
* Aufnahme in die ReturnOrderList
*
* @author Andr� Kupsch
*/
public class ReturnProcess extends SaleProcess {

    private AgencyCustomer m_cust;

    private int m_nProcessEnd = 2;

    private final int END_BY_LEAVE_SHOP = 0;

    DefaultReturnOrderOverviewFormCreator m_roOverview;

    DefaultReturnOrderDetailFormCreator m_roDetail;

    protected UIGate m_uigROrderOverview;

    private FormSheet m_ROrderOverviewSheet;

    protected UIGate m_uigROrderDetail;

    private FormSheet m_ROrderDetailSheet;

    protected UIGate m_uigConfirmation;

    private FormSheet m_ConfirmationSheet;

    protected Transition m_tOvToDet;

    protected Transition m_tDetToOv;

    protected Transition m_tOvToCommit;

    public ReturnProcess(AgencyCustomer cust) {
        super("ReturnProcess");
        m_cust = cust;
    }

    protected void setupAutomaten() {
        ((Agency) Shop.getTheShop()).disableSave_Tag();
        m_roOverview = new DefaultReturnOrderOverviewFormCreator(m_cust);
        m_roDetail = new DefaultReturnOrderDetailFormCreator(m_cust, new Order());
        m_ROrderOverviewSheet = new FormSheet("�berblick", m_roOverview, false);
        m_ROrderDetailSheet = new FormSheet("Best�tigung", m_roDetail, false);
        m_uigROrderOverview = new UIGate(m_ROrderOverviewSheet, null);
        ReturnCounterPanel ovp = (ReturnCounterPanel) m_ROrderOverviewSheet.getComponent();
        if (ovp.getNumOrders() > 0) {
            m_ROrderOverviewSheet.getButton(0).setAction(new sale.Action() {

                public void doAction(SaleProcess p, SalesPoint sp) {
                    System.out.println("OK");
                    m_uigROrderOverview.setNextTransition(m_tOvToDet);
                }
            });
        }
        m_ROrderOverviewSheet.getButton(2).setAction(new sale.Action() {

            public void doAction(SaleProcess p, SalesPoint sp) {
                System.out.println("Verlassen Button");
                ((Agency) Shop.getTheShop()).enableSave_Tag();
                m_nProcessEnd = END_BY_LEAVE_SHOP;
                m_uigROrderOverview.setNextTransition(GateChangeTransition.CHANGE_TO_COMMIT_GATE);
            }
        });
        m_uigROrderDetail = new UIGate(m_ROrderDetailSheet, null);
        m_ROrderDetailSheet.getButton(0).setAction(new sale.Action() {

            public void doAction(SaleProcess p, SalesPoint sp) {
                System.out.println("Reklamation best�tigen");
                m_uigROrderDetail.setNextTransition(m_tOvToCommit);
            }
        });
        m_ROrderDetailSheet.getButton(1).setAction(new sale.Action() {

            public void doAction(SaleProcess p, SalesPoint sp) {
                System.out.println("Zur�ck");
                m_uigROrderDetail.setNextTransition(m_tDetToOv);
            }
        });
        initTransitions();
    }

    /**
  * Initialisiert die Transitionen.
  */
    private void initTransitions() {
        m_tOvToDet = new Transition() {

            public Gate perform(SaleProcess pOwner, User usr) {
                Order o = m_roOverview.getBasket();
                m_roDetail.setBasket(o);
                return m_uigROrderDetail;
            }
        };
        m_tDetToOv = new Transition() {

            public Gate perform(SaleProcess pOwner, User usr) {
                return m_uigROrderOverview;
            }
        };
        m_tOvToCommit = new Transition() {

            public Gate perform(SaleProcess pOwner, User usr) {
                Order newOrder = m_roDetail.getBasket();
                System.out.println(newOrder.getOrderedCount() + ".. newOrderCount");
                java.util.List returnArticle = newOrder.getArticleList();
                java.util.List payedOrders = ((Agency) Shop.getTheShop()).getOrderStock().getOrders(m_cust.getID());
                Iterator pOI = payedOrders.iterator();
                Vector vec = new Vector();
                while (pOI.hasNext()) {
                    Order helpOrder = (Order) pOI.next();
                    System.out.println(helpOrder.getOrderedCount() + ".. helpOrderCount");
                    Order returnOrder = new Order();
                    returnOrder.setOrderID(helpOrder.getOrderID());
                    java.util.List payedArticle = helpOrder.getArticleList();
                    Iterator pAI = payedArticle.iterator();
                    while (pAI.hasNext()) {
                        Article pa = (Article) pAI.next();
                        System.out.println("PayedArtikelname..." + pa.getArticleNumber());
                        Iterator rAI = returnArticle.iterator();
                        while (rAI.hasNext()) {
                            Article ra = (Article) rAI.next();
                            System.out.println("ReturnArtikelname..." + ra.getArticleNumber());
                            int retcount = ra.getCount();
                            boolean doubleName = false;
                            if ((ra.getArticleNumber() == pa.getArticleNumber()) && (ra.getCount() <= pa.getCount())) {
                                System.out.println(ra.getName() + "....gleicht....." + pa.getName());
                                System.out.println(retcount + " " + ra.getName() + " werden zur�ckgegeben");
                                for (int index = 0; index <= (vec.size() - 1); index++) {
                                    System.out.println(" in der Forschleife");
                                    Integer a = (Integer) vec.get(index);
                                    Integer b = new Integer(ra.getArticleNumber());
                                    System.out.println(a + "Vector");
                                    System.out.println(b + "Artikelnummer");
                                    if (a.intValue() == b.intValue()) {
                                        System.out.println(" Artikel schon vorhanden im ReturnOrderList");
                                        doubleName = true;
                                    }
                                }
                                if (!doubleName) {
                                    returnOrder.insertArticle(ra, retcount);
                                    vec.add(new Integer(ra.getArticleNumber()));
                                }
                            }
                        }
                    }
                    if (returnOrder.getArticleList() != null) {
                        System.out.println("Schreiben in ReturnOrderList..." + ((Agency) Shop.getTheShop()).getReturnOrderList().addReturnOrder(returnOrder));
                        System.out.println("ReturnOrderListOrder count..." + returnOrder.getReturnedCount());
                    }
                }
                ((Agency) Shop.getTheShop()).enableSave_Tag();
                return getCommitGate();
            }
        };
    }

    public Gate getInitialGate() {
        setupAutomaten();
        return m_uigROrderOverview;
    }

    public void onFinished() {
        super.onFinished();
        if (m_nProcessEnd == this.END_BY_LEAVE_SHOP) {
            Shop.getTheShop().removeActiveCustomer(m_cust);
            UserManager.getGlobalUM().deleteUser(m_cust.getName());
        }
    }
}
