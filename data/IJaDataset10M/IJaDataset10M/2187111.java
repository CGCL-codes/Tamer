package commons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Adrian
 */
public class ChangePasswordManager {

    private String pass_old, pass1, pass2, login;

    public String change() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpSession mySession = myRequest.getSession();
            this.login = (String) mySession.getAttribute(AuthenticationBean.AUTH_KEY);
            Connection conn = (Connection) DbConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT haslo FROM dane_logowania where login=?");
            ps.setString(1, this.login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String haslo = rs.getString("haslo");
                if (haslo.equals(this.pass_old)) {
                    if (pass1.equals(pass2)) {
                        ps = (PreparedStatement) conn.prepareStatement("UPDATE `biblioteka`.`dane_logowania` SET `haslo` = ? WHERE login = ?");
                        ps.setString(1, this.pass1);
                        ps.setString(2, this.login);
                        ps.executeUpdate();
                        return "zaktualizowano";
                    }
                    return "niezaktualizowano";
                }
                return "niezaktualizowano";
            }
            return "error";
        } catch (SQLException ex) {
            Logger.getLogger(ChangePasswordManager.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }
    }

    public String getPass1() {
        return pass1;
    }

    public void setPass1(String pass1) {
        this.pass1 = pass1;
    }

    public String getPass2() {
        return pass2;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }

    public String getPass_old() {
        return pass_old;
    }

    public void setPass_old(String pass_old) {
        this.pass_old = pass_old;
    }
}
