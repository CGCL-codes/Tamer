package models.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import model.pojo.Customer;
import model.util.MySqlDataAccessHelper;

public class CusDAO {

    public ArrayList<Customer> curylst() {
        ArrayList<Customer> lst = new ArrayList<Customer>();
        MySqlDataAccessHelper mysql = new MySqlDataAccessHelper();
        try {
            mysql.open();
            String sql = "select * from customer";
            ResultSet rs = mysql.executeQuery(sql);
            Customer cg;
            while (rs.next()) {
                cg = new Customer();
                cg.setCustomerID(rs.getString("CusID"));
                cg.setCustomerName(rs.getString("CusName"));
                cg.setAttn(rs.getString("Attn"));
                cg.setAddress(rs.getString("Address"));
                cg.setPhone(rs.getString("Phone"));
                cg.setFax(rs.getString("Fax"));
                cg.setTaxRegNumber(rs.getString("TaxRegNbr"));
                cg.setTerms(rs.getString("Terms"));
                cg.setTermsDescription(rs.getString("TermsDesr"));
                cg.setTaxId(rs.getString("TaxID"));
                cg.setTaxDescription(rs.getString("TaxDesc"));
                cg.setArAcct(rs.getString("ARAcct"));
                cg.setArAccrDescription(rs.getString("ARAcctDescr"));
                cg.setSlsAcct(rs.getString("SlsAcct"));
                cg.setSlsAcctDescription(rs.getString("SlsAcctDescr"));
                cg.setPrepayAcct(rs.getString("PrepayAcct"));
                cg.setPrepayAcctDescr(rs.getString("PrepayAcctDescr"));
                cg.setCuryId(rs.getString("CuryID"));
                cg.setRateExchange(rs.getFloat("RateExchange"));
                cg.setCardNumber(rs.getString("CardNbr"));
                cg.setCardHolderName(rs.getString("CardHldrName"));
                cg.setCardExpiredDate(rs.getDate("CardExpDate"));
                cg.setCardType(rs.getString("CardType"));
                cg.setS4Furture01(rs.getString("S4Furture01"));
                cg.setS4Furture02(rs.getString("S4Furture02"));
                cg.setS4Furture03(rs.getString("S4Furture03"));
                cg.setS4Furture04(rs.getFloat("S4Furture04"));
                cg.setS4Furture05(rs.getFloat("S4Furture05"));
                cg.setCreateUser(rs.getString("CrtdUser"));
                cg.setCreateDate(rs.getDate("CrtdDate"));
                cg.setLastUpdateDate(rs.getDate("LUpdDate"));
                cg.setLastUpdateUSer(rs.getString("LUpdUser"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mysql.close();
        }
        return lst;
    }
}
