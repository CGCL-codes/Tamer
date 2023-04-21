package showParameters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HelloWorld extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String gam = request.getParameter("firstName");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Crazy Maths Time for </title>");
        out.println(gam);
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");
        out.println("<h1>Gamma of what?!!!!</h1>");
        for (int i = 1; i < 20; i++) {
            out.println(gamma(0.000000009 + i));
        }
        out.println("T� t� crazy!!!!</body>");
        out.println("</html>");
    }

    public static double gamma(double x) {
        double result = 0;
        if (x < 1 && x > 0) {
            double n = 10;
            double xn = x + n;
            double value = gamma(xn);
            xn--;
            while (xn > 0) {
                value = value / (xn);
                xn--;
            }
            result = value;
        } else if (x >= 1) {
            int n = 30;
            double fact = (factorial(x - 1) / factorial(x + n - 1));
            x = x + n;
            result = fact * Math.pow(Math.E, lnGamma(x));
            return result;
        } else if (x < 0) {
            double n = 1;
            while (x < 0) {
                n = n * x;
                x = x + 1;
            }
            result = (1 / n) * gamma(x);
        }
        return result;
    }

    public static double factorial(double n) {
        if (n < 0) throw new IllegalArgumentException("Expected non-negative integer, actual value was " + n);
        double result = 1;
        double k = 1;
        while (n >= 1) {
            k = k * n;
            n--;
            result = k;
        }
        return result;
    }

    public static double lnGamma(double x) {
        return 0.5 * (Math.log(2 * Math.PI)) + (x - 0.5) * (Math.log(x)) - x + 1 / (12 * (x)) - 1 / (360 * x * x * x) + 1 / (1260 * x * x * x * x * x);
    }
}
