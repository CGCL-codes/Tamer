package ch.genidea.bb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import ch.genidea.jpa.Company;
import ch.genidea.jpa.EntityManagerHelper;
import ch.genidea.jpa.User;
import ch.genidea.jpa.UserDAO;
import ch.genidea.util.SessionValues;

public class MainPage {

    private static String incomeSQL = "SELECT sum(importoCHF) as sum , Month(data_emissione) as month FROM fattura where CompanyID = :CompanyID and Year(data_emissione)= :Year Group by Month(data_emissione) Order by Year(data_emissione), Month(data_emissione)";

    private static String openInvoicesSQL = "Select count(ID) as count, Sum(ImportoCHF) as sum from fattura Where chiusa = 0 AND CompanyID = :CompanyID";

    private BigInteger openInvoices;

    private BigDecimal openInvoicesSum;

    public BigInteger getOpenInvoices() {
        return openInvoices;
    }

    public void setOpenInvoices(BigInteger openInvoices) {
        this.openInvoices = openInvoices;
    }

    public BigDecimal getOpenInvoicesSum() {
        return openInvoicesSum;
    }

    public void setOpenInvoicesSum(BigDecimal openInvoicesSum) {
        this.openInvoicesSum = openInvoicesSum;
    }

    public DefaultCategoryDataset getChartDataPie() {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        int companyID = SessionValues.getSessionUser().getCompanyID();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        try {
            EntityManager em = EntityManagerHelper.getEntityManager();
            List<Object[]> lst = em.createNativeQuery(incomeSQL.toString()).setParameter("CompanyID", companyID).setParameter("Year", year).getResultList();
            for (int k = 0; k < 10; k++) {
                dataSet.setValue(0, "Ricavi", (Integer) k);
            }
            for (int i = 0; i < lst.size(); i++) {
                Object ls[] = lst.get(i);
                dataSet.setValue((BigDecimal) ls[0], "Ricavi", (Integer) ls[1]);
                ls = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSet;
    }

    public DefaultPieDataset getBestCustomers() {
        int companyID = SessionValues.getSessionUser().getCompanyID();
        DefaultPieDataset dataSet = new DefaultPieDataset();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT sum(importoCHF) as sum , IFNULL(cliente.nome,'') ");
        sql.append("FROM fattura left join cliente on fattura.clienteID = cliente.ID ");
        sql.append("where fattura.CompanyID = :CompanyID and Year(data_emissione)=:Year ");
        sql.append("group by cliente.ID, cliente.nome");
        int year = Calendar.getInstance().get(Calendar.YEAR);
        try {
            EntityManager em = EntityManagerHelper.getEntityManager();
            List<Object[]> lst = em.createNativeQuery(sql.toString()).setParameter("CompanyID", companyID).setParameter("Year", year).getResultList();
            for (int i = 0; i < lst.size(); i++) {
                Object ls[] = lst.get(i);
                dataSet.setValue((String) ls[1], (BigDecimal) ls[0]);
                ls = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSet;
    }

    public List getListaSocieta() {
        List result = null;
        UserDAO dao = new UserDAO();
        User user = dao.findById(SessionValues.getSessionUser().getUserID());
        List<Company> companies = (List) user.getCompanies();
        result = companies;
        return result;
    }

    public void getData() {
        try {
            EntityManager em = EntityManagerHelper.getEntityManager();
            int companyID = SessionValues.getSessionUser().getCompanyID();
            List<Object[]> lst = em.createNativeQuery(openInvoicesSQL).setParameter("CompanyID", companyID).getResultList();
            openInvoices = (BigInteger) (lst.get(0)[0]);
            openInvoicesSum = (BigDecimal) (lst.get(0)[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MainPage() {
        getData();
    }
}
