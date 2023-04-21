package admintool.imp03_data;

import admintool.AdminToolView;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class IMP03_Company {

    private class Company {

        private int companyID = 0;

        private String name = "";

        private String branch = "";

        private int size = 0;

        private double revenue = 0;

        private String comment = "";

        public Company(int companyID, String name, String branch, int size, double revenue, String comment) {
            this.companyID = companyID;
            this.name = name;
            this.branch = branch;
            this.size = size;
            this.revenue = revenue;
            this.comment = comment;
        }

        public String getBranch() {
            return branch;
        }

        public String getComment() {
            return comment;
        }

        public int getCompanyID() {
            return companyID;
        }

        public String getName() {
            return name;
        }

        public double getRevenue() {
            return revenue;
        }

        public int getSize() {
            return size;
        }
    }

    private static ArrayList<IMP03_Company> companies = new ArrayList<IMP03_Company>();

    /**
     * 
     * @return
     */
    public static IMP03_Company getEmptyCompanyObject() {
        return new IMP03_Company(-1, AdminToolView.EMPTY, AdminToolView.EMPTY, -1, -1, AdminToolView.EMPTY);
    }

    /**
     * 
     * @param reload
     * @return
     */
    public static ArrayList<IMP03_Company> getAllAvailableCompaniesFromDB(boolean reload) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (reload == true || companies.isEmpty()) {
                companies.clear();
                ps = AdminToolView.getDBConnection().prepareStatement("SELECT * FROM company");
                rs = ps.executeQuery();
                while (rs.next()) {
                    companies.add(new IMP03_Company(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getDouble(5), rs.getString(6)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
            }
        }
        return companies;
    }

    private Company company = null;

    /**
     * 
     * @param companyID
     * @param name
     * @param branch
     * @param size
     * @param revenue
     * @param comment
     */
    public IMP03_Company(int companyID, String name, String branch, int size, double revenue, String comment) {
        this.company = new Company(companyID, name, branch, size, revenue, comment);
    }

    @Override
    public String toString() {
        return company.getName();
    }

    /**
     * 
     * @return
     */
    public int getCompanyID() {
        return company.getCompanyID();
    }

    /**
     * 
     * @return
     */
    public String getName() {
        return company.getName();
    }

    /**
     * 
     * @return
     */
    public String getBranch() {
        return company.getBranch();
    }

    /**
     * 
     * @return
     */
    public int getSize() {
        return company.getSize();
    }

    /**
     * 
     * @return
     */
    public double getRevenue() {
        return company.getRevenue();
    }

    /**
     * 
     * @return
     */
    public String getComment() {
        return company.getComment();
    }
}
