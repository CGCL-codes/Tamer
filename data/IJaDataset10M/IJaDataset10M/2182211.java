package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.*;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entities.*;
import controller.EmployeeDAORemote;

/**
 * Servlet implementation class getListEmpServlet
 */
@EJB
@WebServlet("/getListEmpServlet")
public class getListEmpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
	 * @see HttpServlet#HttpServlet()
	 */
    public getListEmpServlet() {
        super();
    }

    public EmployeeDAORemote lookup() {
        EmployeeDAORemote remote1 = null;
        try {
            InitialContext ctx = new InitialContext();
            remote1 = (EmployeeDAORemote) ctx.lookup("java:global/GoodHealth/GoodHealth-ejb/EmployeeDAO!controller.EmployeeDAORemote");
            System.out.print(remote1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return remote1;
    }

    protected void server(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        EmployeeDAORemote remote = lookup();
        String par = request.getParameter("idEmp");
        if (par != null) {
            String value = "test";
            String form = "<form action='/empaction.do?act=create' method='post' >" + "<table>" + "<tr><td> Emp ID</td><td>" + value + "<td></tr>" + "<tr><td>idUser</td><td><input type='text' name='idUser'value='" + value + "'></input><td></tr>" + "<tr><td>Password</td><td><input type='text' name='password'value='" + value + "'></input><td></tr>" + "<tr><td>First Name</td><td><input type='text' name='firstName'value='" + value + "'></input><td></tr>" + "<tr><td>Last Name</td><td><input type='text' name='lastName'value='" + value + "'></input><td></tr>" + "<tr><td>Contact</td><td><input type='text' name='emgContact'value='" + value + "'></input><td></tr>" + "<tr><td>Address</td><td><textarea name='address'value='" + value + "'></textarea><td></tr>" + "<tr><td><input type='submit' value='Update' ></input></td><td><td></tr>" + "</table>" + "</form>";
            out.write(form);
            System.out.println("1");
        } else {
            List<Employee> list = new ArrayList<Employee>();
            list = remote.getAllEmp();
            String listEmp = "";
            for (Employee emp : list) {
                listEmp = listEmp + "<tr><td>" + emp.getIdEmployee() + "</td><td>" + emp.getUser().getIdUser() + "</td><td>" + emp.getFirstName() + "</td><td>" + emp.getLastName() + "</td><td><a onclick='editEmp(" + emp.getIdEmployee() + ")'>Edit</a> <a>Delete</a></td></tr>";
            }
            String result = "<table class='jtable'><thead><tr><th>Employee ID</th>" + "<th>Username</th>" + "<th>First Name</th>" + "<th>Last Name</th>" + "<th></th>" + "</thead>" + "<tbody>" + listEmp + "</tbody></table>";
            out.write(result);
            System.out.println(result);
        }
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        server(request, response);
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        server(request, response);
    }
}
