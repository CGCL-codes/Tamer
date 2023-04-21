package org.jazzteam.data;

import org.jazzteam.model.Bank;
import org.jazzteam.model.Credit;
import org.jazzteam.util.Database;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Andrey
 * Date: 05.01.12
 * Time: 17:41
 * To change this template use File | Settings | File Templates.
 */
public class SearchDataService {

    protected Database db;

    private Bank bank;

    private Credit credit;

    private ResultSet result;

    private List<Bank> banks;

    private List<Credit> credits;

    private Blob image;

    public SearchDataService() throws ClassNotFoundException, SQLException {
        db = new Database();
        banks = new ArrayList<Bank>();
        credits = new ArrayList<Credit>();
    }

    public List<Bank> searchBank(String text) throws SQLException {
        String sql = "SELECT * FROM bank WHERE name LIKE '%" + text + "%';";
        result = db.query(sql);
        while (result.next()) {
            bank = new Bank();
            bank.setId(result.getInt("id"));
            bank.setName(result.getString("name"));
            bank.setOperations(result.getString("operations"));
            bank.setAbout(result.getString("about"));
            bank.setAddress(result.getString("address"));
            bank.setEmail(result.getString("email"));
            bank.setPhone(result.getString("phone"));
            bank.setSwift(result.getString("swift"));
            bank.setTelex(result.getString("telex"));
            bank.setWebSite(result.getString("website"));
            image = result.getBlob("logo");
            bank.setLogo(image.getBytes(1, (int) image.length()));
            banks.add(bank);
        }
        return banks;
    }

    public List<Credit> searchCredit(String text) throws SQLException {
        String sql = "SELECT * FROM credit WHERE title LIKE '%" + text + "%';";
        result = db.query(sql);
        while (result.next()) {
            credit = new Credit();
            credit.setId(result.getInt("id"));
            credit.setBankId(result.getLong("bank_id"));
            credit.setDescription(result.getString("description"));
            credit.setTitle(result.getString("title"));
            credits.add(credit);
        }
        result.close();
        return credits;
    }
}
