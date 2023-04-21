package web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import model.conexaoDB;

public class listeners implements ServletContextListener, HttpSessionListener {

    ServletContext servCon;

    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName(conexaoDB.dbDriver);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        servCon = sce.getServletContext();
        servCon.setAttribute("dbUrl", conexaoDB.dbPath);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Finalizando");
    }

    public void sessionCreated(HttpSessionEvent hse) {
        HttpSession sessao;
        sessao = hse.getSession();
    }

    public void sessionDestroyed(HttpSessionEvent hse) {
        HttpSession sessao;
        Connection con;
        sessao = hse.getSession();
        con = (Connection) sessao.getAttribute("conexao");
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar conexao: " + e.getMessage());
        }
        sessao.removeAttribute("conexao");
    }
}
